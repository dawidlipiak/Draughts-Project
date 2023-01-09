package org.example;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameController extends Canvas implements Runnable{
    Thread thread;
    Game game;
    private final static int nrOfFields = 8;
    private final int fieldSize;
    private Player player;
    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private PrintWriter output;
    private BufferedReader input;


    public GameController(int boardSize, Label message, Stage gameStage){
        super(boardSize,boardSize);
        this.fieldSize = boardSize/nrOfFields;
        listenSocket();

        game = new Game(this, nrOfFields, boardSize, message, this);
        receiveInitFromServer();

        Platform.runLater(game::drawBoard);
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
            if( game.getTurnMade() == NONACTIVE) {
                game.playerTurn(row, col);
            }
        }
    }

    /**
     * Method to send a message to the server.
     * @param string message
     */
    public boolean send(String string){
        output.println(string);
        try{
            if(input.readLine().equals("Wrong move")) {
                Platform.runLater(()->game.setPROMPT("Zły ruch. Zrób nowy ruch"));
                game.rewindBoard();
                Platform.runLater(game::drawBoard);
                return false;
            }
            else {
                Platform.runLater(()->game.setPROMPT("Ruch przeciwnika"));
                game.setTurnMade(ACTIVE);
                game.setCurrentPlayer(player);
                Platform.runLater(game::drawBoard);
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
        return false;
    }

    /**
     * Connection to the socket.
     */
    private void listenSocket() {
        try {
            Socket socket = new Socket("localhost", 4444);
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
            boolean playerAssign = Boolean.parseBoolean(input.readLine());
            System.out.println(playerAssign);

            if (playerAssign) {
                output.println(nrOfFields);
                Platform.runLater(()->game.setPROMPT("Mój ruch"));
                player = new Player(nrOfFields,true);
                game.setTurnMade(NONACTIVE);
                game.gameSetup(player);
            } else {
                Platform.runLater(()->game.setPROMPT("Ruch przeciwnika"));
                player = new Player(nrOfFields,false);
                game.gameSetup(player);
            }
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }

    /**
     * Method to receive commands from server.
     */
    private void receiveCommand(){
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

                for(int i = 0; i < nrOfMoves; i++) {
                    int fromRow = Integer.parseInt(command[2 + (i * 6)]);
                    int fromCol = Integer.parseInt(command[3 + (i * 6)]);
                    int toRow = Integer.parseInt(command[4 + (i * 6)]);
                    int toCol = Integer.parseInt(command[5 + (i * 6)]);
                    Movement move = new Movement(fromRow, fromCol, toRow, toCol);

                    if(i+1 == nrOfMoves) {
                        game.makeRecievedMove(true, move);
                    }
                    else {
                        game.makeRecievedMove(false, move);
                    }
                    Platform.runLater(game::drawBoard);
                }
            }
            Platform.runLater(()->game.setPROMPT("Mój ruch"));
        }
        catch (IOException e) {
            System.out.println("Read failed"); System.exit(1);}
    }
    private void threadFunction(Player iPlayer){
        while(true) {
            synchronized (this) {
                if (game.getCurrentPlayer() == iPlayer) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (game.getTurnMade() == ACTIVE){
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
