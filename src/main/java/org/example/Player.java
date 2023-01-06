package org.example;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Pawn [][] pawnBoard;
    private final int nrOfFields;

    /** Is the player the first player */
    private final boolean firstPlayer;

    /**
     * Constructor of a player that creates pawns for him
     * @param nrOfFields number of pawns at the board
     */
    public Player(int nrOfFields, boolean firstPlayer){
        pawnBoard = new Pawn[nrOfFields][nrOfFields];
        this.nrOfFields = nrOfFields;
        this.firstPlayer = firstPlayer;
    }
    void setPositions() {
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3) {
                        pawnBoard[row][col] = new Pawn(Color.BLACK);
                    }
                    else if (row > 4) {
                        pawnBoard[row][col] = new Pawn(Color.WHITE);
                    }
                    else {
                        pawnBoard[row][col] = new Pawn(Color.TRANSPARENT);
                        pawnBoard[row][col].setState(PawnState.EMPTY);
                    }
                }
                else {
                    pawnBoard[row][col] = new Pawn(Color.TRANSPARENT);
                    pawnBoard[row][col].setState(PawnState.EMPTY);
                }
            }
        }
    }
    /**
     * Return Pawn array
     */
    public Pawn [][] getPawnBoard() {
        return pawnBoard;
    }
}