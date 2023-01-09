package org.example;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to create the hole game.
 */
public class Game {
    Canvas gameView;
    GameController gameController;
    private Pawn [][] pawnsBoard;
    private Pawn [][] oldPawnsBoard;
    private final int nrOfFields;
    private final int fieldSize;
    private Player currentPlayer;
    private int selectedRow, selectedCol;
    private final Label messagePROMT;
    private Movement[] legalMoves;
    private List <Movement> doneMoves;
    private LegalMoves legalMovesObj;
    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private static int turnMade = ACTIVE;
    /**
     * Constructor of a play board for draughts
     * @param numberOfFields number of Fields in a row and column
     * @param boardSize size of a window
     */
    public Game(Canvas gameView,int numberOfFields, int boardSize, Label message, GameController gameController){
        this.gameView = gameView;
        this.nrOfFields = numberOfFields;
        this.fieldSize = boardSize/nrOfFields;
        pawnsBoard = new Pawn[nrOfFields][nrOfFields];
        oldPawnsBoard = new Pawn[nrOfFields][nrOfFields];
        this.messagePROMT = message;
        this.gameController = gameController;
        setPositions();
    }

    /**
     * Method to draw a draughtboard and to create framing for pawns.
     */
    public void drawBoard() {
        Pawn pawn;
        GraphicsContext g = gameView.getGraphicsContext2D();
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
    public void setPositions() {
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
     * Method to hold turn of the game.
     * @param row row
     * @param col column
     */
    public void playerTurn(int row, int col) {

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
                // SAVE PREVIOUS BOARD IN CASE OF A WRONG MOVE
                for(int tempRow = 0; tempRow < nrOfFields; tempRow++){
                    for(int tempCol = 0; tempCol < nrOfFields; tempCol++){
                        oldPawnsBoard[tempRow][tempCol] = pawnsBoard[tempRow][tempCol];
                    }
                }

                // Make a move
                pawnsBoard = legalMoves[i].makeMove(pawnsBoard);
                doneMoves.add(new Movement(legalMoves[i].getFromRow(), legalMoves[i].getFromCol(), legalMoves[i].getToRow(), legalMoves[i].getToCol()));

                if (legalMoves[i].isJump()) {
                    legalMoves = legalMovesObj.getLegalJumpsFrom(legalMoves[i].getToRow(), legalMoves[i].getToCol(), currentPlayer, pawnsBoard);

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
                if (doneMoves.size() > 0) {
                    message = "";
                    for (int j = 0; j < doneMoves.size(); j++) {
                        message += doneMoves.size() + " move"
                                + " " + doneMoves.get(j).getFromRow()
                                + " " + doneMoves.get(j).getFromCol()
                                + " " + doneMoves.get(j).getToRow()
                                + " " + doneMoves.get(j).getToCol();
                        if((doneMoves.size() != 1) && (j != doneMoves.size()-1)){
                            message += " ";
                        }
                    }

                    if(!gameController.send(message)){
                        pawnsBoard = oldPawnsBoard;
                        oldPawnsBoard = new Pawn[fieldSize][fieldSize];
                        doneMoves.clear();
                    }
                }

                return;
            }
        }
        setPROMPT("Naciśnij gdzie chcesz się ruszyć");
    }

    public void makeRecievedMove(boolean finishedAllMoves,Movement move){
        pawnsBoard = move.makeMove(pawnsBoard);
        setTurnMade(NONACTIVE);
        legalMoves = legalMovesObj.getLegalMoves(currentPlayer,pawnsBoard);
        if(finishedAllMoves){
            doneMoves = new ArrayList<>();
        }
    }
    public void rewindBoard(){
        pawnsBoard = oldPawnsBoard;
    }

    public int getTurnMade() {
        return turnMade;
    }

    public void setTurnMade(int turnState) {
        turnMade = turnState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }
    public void gameSetup(Player player){
        currentPlayer = player;
        legalMovesObj = new LegalMoves(nrOfFields);
        legalMoves = legalMovesObj.getLegalMoves(currentPlayer, pawnsBoard);
        doneMoves = new ArrayList<>();
    }

    /**
     * Method to set prompt message on the window.
     * @param message message
     */
    public void setPROMPT(String message) {
        messagePROMT.setText(message);
        System.out.println(message);
    }

}