package org.example;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to create the hole game.
 */
public class Game extends Canvas  implements Runnable {
    Thread thread;
    Socket socket;
    private Pawn [][] pawnsBoard;
    private final int nrOfFields;
    private final int fieldSize;
    private Player realPlayer;
    private int player;
    public final static int PLAYER1 = 1;
    public final static int PLAYER2 = 2;
    private static int currentPlayer = PLAYER1;
    private int selectedRow, selectedCol;
    private final Label messagePROMT;
    Movement[] legalMoves;
    List <Movement> doneMoves;
    LegalMoves legalMovesObj;
    private PrintWriter output;
    private BufferedReader input;
    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private static int turnMade = ACTIVE;
    /**
     * Constructor of a play board for draughts
     * @param numberOfFields number of Fields in a row and column
     * @param boardSize size of a window
     */
    public Game(int numberOfFields, int boardSize, Label message){
        super(boardSize,boardSize);
        this.nrOfFields = numberOfFields;
        this.fieldSize = boardSize/nrOfFields;
        pawnsBoard = new Pawn[nrOfFields][nrOfFields];
        this.messagePROMT = message;
        thread = new Thread(this);

        listenSocket();
        receiveInitFromServer();
        setPositions();

        legalMovesObj = new LegalMoves(nrOfFields);
        legalMoves = legalMovesObj.getLegalMoves(realPlayer, pawnsBoard);
        drawBoard();
        thread.start();
    }

    /**
     * Method to draw a draughtboard and to create framing for pawns.
     */
    private void drawBoard() {
        Pawn pawn;
        GraphicsContext g = getGraphicsContext2D();
        g.setFont(Font.font(40));

        // Draw the squares of the draughtboard and the pawns.
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    g.setFill(Color.PERU);
                }
                else {
                    g.setFill(Color.MOCCASIN);
                }
                g.fillRect(col*fieldSize,row*fieldSize, fieldSize, fieldSize);

                pawn = pawnsBoard[row][col];

                switch (pawn.getState()) {
                    case NORMAL:
                        if (pawn.getColor() == Color.WHITE) {
                            g.setFill(Color.WHITE);
                        } else {
                            g.setFill(Color.BLACK);
                        }
                        g.fillOval(col * fieldSize + 7.5, row * fieldSize + 7.5, 60, 60);
                        break;

                    case KING:
                        if (pawn.getColor() == Color.WHITE) {
                            g.setFill(Color.WHITE);
                        } else {
                            g.setFill(Color.BLACK);
                        }
                        g.fillOval(col * fieldSize + 7.5, row * fieldSize + 7.5, 60, 60);
                        g.setFill(Color.RED);
                        g.fillText("K", 25+ col * fieldSize, 50 + row * fieldSize);
                        break;
                }
            }
        }
        //Add framing for the pawns to show possible moves.
        if(turnMade == NONACTIVE) {
            g.setStroke(Color.BROWN);
            g.setLineWidth(4);
            for (int i = 0; i < legalMoves.length; i++) {
                g.strokeRect(2 + legalMoves[i].getFromCol() * fieldSize, 2 + legalMoves[i].getFromRow() * fieldSize, fieldSize - 4, fieldSize - 4);
            }
            if (selectedRow >= 0) {
                g.setStroke(Color.CHOCOLATE);
                g.setLineWidth(4);
                g.strokeRect(2 + selectedCol * fieldSize, 2 + selectedRow * fieldSize, fieldSize - 4, fieldSize - 4);
                g.setStroke(Color.DARKGREEN);
                g.setLineWidth(4);
                for (int i = 0; i < legalMoves.length; i++) {
                    if (legalMoves[i].getFromCol() == selectedCol && legalMoves[i].getFromRow() == selectedRow) {
                        g.strokeRect(2 + legalMoves[i].getToCol() * fieldSize, 2 + legalMoves[i].getToRow() * fieldSize, fieldSize - 4, fieldSize - 4);
                    }
                }
            }
        }
    }
    /**
     * Method to set starting positions of the pawns.
     */
    private void setPositions() {
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3) {
                        pawnsBoard[row][col] = new Pawn(Color.BLACK,row,col);;
                    }
                    else if (row > 4) {
                        pawnsBoard[row][col] = new Pawn(Color.WHITE,row,col);
                    }
                    else {
                        pawnsBoard[row][col] = new Pawn(Color.TRANSPARENT,row,col);;
                        pawnsBoard[row][col].setState(PawnState.EMPTY);
                    }
                }
                else {
                    pawnsBoard[row][col] = new Pawn(Color.TRANSPARENT,row,col);;
                    pawnsBoard[row][col].setState(PawnState.EMPTY);
                }
            }
        }
    }

    /**
     * Event handler for Mouse Pressed on the board.
     * @param evt
     */
    public void mousePressed(MouseEvent evt) {
//        if (gameInProgress == false)
//            sendMessage("Click \"New Game\" to start a new game.");
//        else {
        int col = (int)((evt.getX()) / fieldSize);
        int row = (int)((evt.getY()) / fieldSize);
        if (col >= 0 && col < nrOfFields && row >= 0 && row < nrOfFields) {
            if(turnMade == NONACTIVE) {
                playerTurn(row, col);
            }
        }
//        }
    }

    /**
     * Method to hold turn of the game.
     * @param row row
     * @param col column
     */
    private void playerTurn(int row, int col) {

        // If the player clicked on one of the pawns that the player can move, mark this row and col as selected and return.
        if(legalMoves != null) {
            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].moveCheckerFrom(row, col)) {
                    selectedRow = row;
                    selectedCol = col;
                    drawBoard();
                    return;
                }
            }
        }
        // If no pawn has been selected to be moved, the user must first select a pawn.
        if (selectedRow < 0) {
            setPROMPT("Naciśnij pionka którym chcesz ruszyć");
            return;
        }

        //If the user clicked on a field where the selected pawn can be legally moved, then make the move and return.
        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i].moveCheckerFrom(selectedRow, selectedCol) && legalMoves[i].moveCheckerTo(row, col)) {
                pawnsBoard = legalMoves[i].makeMove(pawnsBoard);
                doneMoves.add(new Movement(legalMoves[i].getFromRow(), legalMoves[i].getFromCol(), legalMoves[i].getToRow(), legalMoves[i].getToCol()));

                if (legalMoves[i].isJump()) {
                    legalMoves = legalMovesObj.getLegalJumpsFrom(legalMoves[i].getToRow(), legalMoves[i].getToCol(), realPlayer, pawnsBoard);

                    if (legalMoves != null) {
                        if (pawnsBoard[row][col].getColor() == Color.WHITE) {
                            setPROMPT("Musisz kontynuować skok.");
                        } else {
                            setPROMPT("Musisz kontynuować skok.");
                        }
                        drawBoard();
                        return;
                    }
                }
                selectedRow = -1;

                String message;
                if (doneMoves.size() == 1) {
                    message = "";
                    for (int j = 0; j < doneMoves.size(); j++) {
                        message = message + doneMoves.size() + " move"
                                + " " + doneMoves.get(j).getFromRow()
                                + " " + doneMoves.get(j).getFromCol()
                                + " " + doneMoves.get(j).getToRow()
                                + " " + doneMoves.get(j).getToCol();
                    }
                    send(message);
                } else if (doneMoves.size() > 1) {
                    message = "";
                    for (int j = 0; j < doneMoves.size(); j++) {
                        message += doneMoves.size() + " move"
                                + " " + doneMoves.get(j).getFromRow()
                                + " " + doneMoves.get(j).getFromCol()
                                + " " + doneMoves.get(j).getToRow()
                                + " " + doneMoves.get(j).getToCol();
                        if(j != doneMoves.size()-1 ){
                            message += " ";
                        }
                    }
                    send(message);
                }
                return;
            }
        }
        setPROMPT("Naciśnij gdzie chcesz się ruszyć");
    }

    /**
     * Method to send a message to the server.
     * @param string message
     */
    private void send(String string){
        output.println(string);
        setPROMPT("Ruch przeciwnika");
        turnMade = ACTIVE;
        currentPlayer = player;
        drawBoard();
    }

    /**
     * Connection to the socket.
     */
    private void listenSocket() {
        try {
            socket = new Socket("localhost", 4444);
            // Inicjalizacja wysylania do serwera
            output = new PrintWriter(socket.getOutputStream(), true);
            // Inicjalizacja odbierania z serwera
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }
    /**
     * Initializing client. Settling which socket is which client.
     */
    private void receiveInitFromServer() {
        try {
            int in = Integer.parseInt(input.readLine());
            System.out.println(in);
            if (in == PLAYER1) {
                setPROMPT("Mój ruch");
                player = PLAYER1;
                turnMade = NONACTIVE;
                realPlayer = new Player(8, true);
            } else {
                setPROMPT("Ruch przeciwnika");
                player = PLAYER2;
                realPlayer = new Player(8, false);
            }
            doneMoves = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }

    /**
     * Method to receive commands from server.
     */
    public void receiveCommand(){
        try {
            // Reciving from server.
            String str = input.readLine();
            System.out.println(str);

            String [] command = str.split(" ");

            // Received command "nrOfMoves move fromRow fromCol toRow toCol"
            int nrOfWords = str.split("\\w+").length;
            System.out.println(nrOfWords + " slowa");

            if(nrOfWords % 6 == 0 ){
                int nrOfMoves = Integer.parseInt(command[0]);

                for(int i = 0; i < nrOfMoves; i++){
                    int fromRow = Integer.parseInt(command[2 + (i*6)]);
                    int fromCol = Integer.parseInt(command[3 + (i*6)]);
                    int toRow = Integer.parseInt(command[4 + (i*6)]);
                    int toCol = Integer.parseInt(command[5 + (i*6)]);
                    Movement move = new Movement(fromRow, fromCol, toRow, toCol);
                    pawnsBoard = move.makeMove(pawnsBoard);
                    turnMade = NONACTIVE;
                    legalMoves = legalMovesObj.getLegalMoves(realPlayer, pawnsBoard);
                    drawBoard();
                }
            }
            doneMoves = new ArrayList<>();
            Platform.runLater(()->setPROMPT("Mój ruch"));
        }
        catch (IOException e) {
            System.out.println("Read failed"); System.exit(1);}
    }

    /**
     * Method to set prompt message on the window.
     * @param message message
     */
    private void setPROMPT(String message) {
        messagePROMT.setText(message);
        System.out.println(message);
    }

    /**
     * Thread method.
     * @param iPlayer player which thread is run for
     */
    private void threadFunction(int iPlayer){
        while(true) {
            synchronized (this) {
                if (currentPlayer == iPlayer) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (turnMade == ACTIVE){
                    receiveCommand();
                }
                notifyAll();
            }
        }
    }

    @Override
    public void run() {
        threadFunction(player);
    }
}