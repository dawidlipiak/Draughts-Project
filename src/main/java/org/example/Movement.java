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
    // State if the move was a jump
    boolean isJump = false;

    // Constructor.  Just set the values of the instance variables.
    public Movement(int row1, int col1, int row2, int col2) {
        fromRow = row1;
        fromCol = col1;

        toRow = row2;
        toCol = col2;
    }
    /**
     * Method to test if this move is jump.
     */
    public boolean isJump() {
        return isJump;
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

        if (toRow == 0 && pawnsBoard[toRow][toCol].getColor() == Color.WHITE){
            pawnsBoard[toRow][toCol].setState(PawnState.KING);
        }
        if (toRow == 7 && pawnsBoard[toRow][toCol].getColor() == Color.BLACK) {
            pawnsBoard[toRow][toCol].setState(PawnState.KING);
        }

        int rowDirection = 1; // MOVE DOWN
        int colDirection = 1; // MOVE RIGHT
        if(fromRow > toRow){
            rowDirection = -1;
        }
        if(fromCol > toCol){
            colDirection = -1;
        }
        for(int i = 1; i < Math.abs(toRow-fromRow); i++){
            if(pawnsBoard[fromRow+(i*rowDirection)][fromCol+(i*colDirection)].getState() != PawnState.EMPTY){
                isJump = true;
                pawnsBoard[fromRow+(i*rowDirection)][fromCol+(i*colDirection)].setState(PawnState.EMPTY);
            }
        }

        return pawnsBoard;
    }

    /**
     * Get the row of a position move is made to
     * @return row
     */
    public int getToRow(){
        return toRow;
    }

    /**
     * Get the col of a position move is made to
     * @return col
     */
    public int getToCol() {
        return toCol;
    }

    /**
     * Get the col of a position move is made from
     * @return col
     */
    public int getFromCol() {
        return fromCol;
    }

    /**
     * Get the row of a position move is made from
     * @return row
     */
    public int getFromRow() {
        return fromRow;
    }
}
