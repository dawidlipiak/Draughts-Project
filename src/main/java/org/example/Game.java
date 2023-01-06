package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class where creates draughts game.
 */
public class Game extends Canvas {
    //table contains the pawns
    private Pawn [][] pawnsBoard;
    private final int nrOfFields;
    private final int boardSize;
    private final int fieldSize;
    // player whose turn is currently continue
    private Player currentPlayer;
    private int selectedRow;
    private int selectedCol;
    private String message;
    // table of legal moves
    Movement[] legalMoves;
    LegalMoves legalMovesObj;
    /**
     * Constructor of game board for draughts
     * @param numberOfFields number of Fields in a row and column
     * @param boardSize size of a window
     */
    public Game(int numberOfFields, int boardSize, boolean firstPlayer){
        super(boardSize,boardSize);
        this.boardSize = boardSize;
        this.nrOfFields = numberOfFields;
        this.fieldSize = boardSize/nrOfFields;
        pawnsBoard = new Pawn[nrOfFields][nrOfFields];
        currentPlayer = new Player(12, firstPlayer);
        legalMovesObj = new LegalMoves(nrOfFields);
        setPositions();
        legalMoves = legalMovesObj.getLegalMoves(currentPlayer, pawnsBoard);
        drawBoard();
    }

    /**
     * Method to draw a draughtboard and to create framing for pawns.
     */
    public void drawBoard() {
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
        g.setStroke(Color.BROWN);
        g.setLineWidth(4);
        for (int i = 0; i < legalMoves.length; i++) {
            g.strokeRect( 2+legalMoves[i].getFromCol()*fieldSize, 2+legalMoves[i].getFromRow()*fieldSize, fieldSize-4, fieldSize-4);
        }
        if (selectedRow >= 0) {
            g.setStroke(Color.CHOCOLATE);
            g.setLineWidth(4);
            g.strokeRect(2 + selectedCol*fieldSize, 2 + selectedRow*fieldSize, fieldSize-4, fieldSize-4);
            g.setStroke(Color.DARKGREEN);
            g.setLineWidth(4);
            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].getFromCol() == selectedCol && legalMoves[i].getFromRow() == selectedRow) {
                    g.strokeRect(2+ legalMoves[i].getToCol()*fieldSize, 2 + legalMoves[i].getToRow()*fieldSize, fieldSize-4, fieldSize-4);
                }
            }
        }
    }

    /**
     * Method to set start positions of the pawns.
     */
    private void setPositions() {
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3) {
                        Pawn pawn = new Pawn(Color.BLACK,row,col);
                        pawnsBoard[row][col] = pawn;
                        if(!currentPlayer.isFirstPlayer()) {
                            currentPlayer.addPawns(pawn);
                        }
                        else{
                            Pawn emptyPawn = new Pawn(Color.TRANSPARENT,row,col);
                            emptyPawn.setState(PawnState.EMPTY);
                            currentPlayer.addPawns(emptyPawn);
                        }
                    }
                    else if (row > 4) {
                        Pawn pawn = new Pawn(Color.WHITE,row,col);
                        pawnsBoard[row][col] = pawn;
                        if(currentPlayer.isFirstPlayer()) {
                            currentPlayer.addPawns(pawn);
                        }
                        else{
                            Pawn emptyPawn = new Pawn(Color.TRANSPARENT,row,col);
                            emptyPawn.setState(PawnState.EMPTY);
                            currentPlayer.addPawns(emptyPawn);
                        }
                    }
                    else {
                        Pawn pawn = new Pawn(Color.TRANSPARENT,row,col);
                        pawnsBoard[row][col] = pawn;
                        pawnsBoard[row][col].setState(PawnState.EMPTY);
                        currentPlayer.addPawns(pawn);
                    }
                }
                else {
                    Pawn pawn = new Pawn(Color.TRANSPARENT,row,col);
                    pawnsBoard[row][col] = pawn;
                    pawnsBoard[row][col].setState(PawnState.EMPTY);
                    currentPlayer.addPawns(pawn);
                }
            }
        }
    }

    /**
     * Event handler for Mouse Pressed
     * @param evt event
     */
    public void mousePressed(MouseEvent evt) {
//        if (gameInProgress == false)
//            sendMessage("Click \"New Game\" to start a new game.");
//        else {
            int col = (int)((evt.getX()) / fieldSize);
            int row = (int)((evt.getY()) / fieldSize);
            if (col >= 0 && col < nrOfFields && row >= 0 && row < nrOfFields)
                playerTurn(row,col);
//        }
    }

    /**
     * Method to hold turn of the game.
     * @param row row
     * @param col column
     */
    void playerTurn(int row, int col) {

        // If the player clicked on one of the pawns that the player can move, mark this row and col as selected and return.
        for( int i = 0; i < legalMoves.length; i++){
            if(legalMoves[i].moveCheckerFrom(row, col)){
                selectedRow = row;
                selectedCol = col;
                if(pawnsBoard[row][col].getColor() == Color.WHITE ){
                    sendMessage("BIAŁY: Twoja kolej.");
                }
                else{
                    sendMessage("CZARNY: Twoja kolej");
                }
                drawBoard();
                return;
            }
        }
        // If no pawn has been selected to be moved, the user must first select a pawn.
        if (selectedRow < 0) {
            sendMessage("Naciśnij pionka którym chcesz ruszyć");
            return;
        }

        //If the user clicked on a field where the selected pawn can be legally moved, then make the move and return.
        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].moveCheckerFrom(selectedRow,selectedCol) && legalMoves[i].moveCheckerTo(row,col)) {
                pawnsBoard = legalMoves[i].makeMove(pawnsBoard);

                if(legalMoves[i].isJump()) {
                    legalMoves = legalMovesObj.getLegalJumpsFrom(legalMoves[i].getToRow(), legalMoves[i].getToCol(), currentPlayer, pawnsBoard);
                    if (legalMoves != null) {
                        if (pawnsBoard[row][col].getColor() == Color.WHITE) {
                            sendMessage("BIAŁY: Musisz kontynuować skok.");
                        } else {
                            sendMessage("CZARNY:  Musisz kontynuować skok.");
                        }
                        drawBoard();
                        return;
                    }
                }
                selectedRow = -1;
                drawBoard();
            }

        sendMessage("Naciśnij gdzie chcesz się ruszyć");

    }

    /**
     * Method to send a message.
     * @param message
     */
    public void sendMessage(String message) {
        this.message =  message;
        System.out.println(message);
    }
    public String getMessage(){
        return message;
    }
    public Pawn [][] getPawnsBoard() {
        return pawnsBoard;
    }
    public int getNrOfFields() {
        return nrOfFields;
    }
}