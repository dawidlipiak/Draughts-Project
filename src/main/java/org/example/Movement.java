package org.example;

import javafx.scene.paint.Color;

/**
 * Class with values of rows and columns for the move.
 */
public class Movement {
    // Position of the pawn to be moved.
    private final int fromRow, fromCol;
    // Position to move the pawn to.
    private final int toRow, toCol;

    // Constructor to set the values of the instance variables.
    Movement(int row1, int col1, int row2, int col2) {
        fromRow = row1;
        fromCol = col1;

        toRow = row2;
        toCol = col2;
    }

    /**
     * Method to test if this move is jump.
     */
    boolean isJump() {
        return (fromRow - toRow >= 2 || fromRow - toRow <= -2);
    }

    /**
     * Method to check if the row and column is the actual from column and from row.
     * @param row row
     * @param col column
     * @return true or false
     */
    boolean moveCheckerFrom(int row, int col){
        if(fromRow == row && fromCol == col){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Method to check if the row and column is the actual to column and to row.
     * @param row row
     * @param col column
     * @return true or false
     */
    boolean moveCheckerTo(int row, int col){
        if(toRow == row && toCol == col){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Method to update board of pawns after move.
     * @param pawnsBoard array of pawns
     * @return updated array of pawns
     */
    public Pawn [][] makeMove(Pawn [][] pawnsBoard){
        Pawn tempPawn = pawnsBoard[toRow][toCol];
        pawnsBoard[toRow][toCol] = pawnsBoard[fromRow][fromCol];
        pawnsBoard[fromRow][fromCol] = tempPawn;

        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            // The move is a jump so remove the jumped pawn from the board.
            int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped pawn.
            int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped pawn.
            pawnsBoard[jumpRow][jumpCol].setState(PawnState.EMPTY);
        }
        if (toRow == 0 && pawnsBoard[toRow][toCol].getColor() == Color.WHITE){
            pawnsBoard[toRow][toCol].setState(PawnState.KING);
        }
        if (toRow == 7 && pawnsBoard[toRow][toCol].getColor() == Color.BLACK) {
            pawnsBoard[toRow][toCol].setState(PawnState.KING);
        }
        return pawnsBoard;
    }

    public int getToRow(){
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getFromRow() {
        return fromRow;
    }
}
