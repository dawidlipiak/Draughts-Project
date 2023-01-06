package org.example;

import javafx.scene.paint.Color;

public class Movement {
    // Position of piece to be moved.
    private final int fromRow, fromCol;
    private final int toRow, toCol;      // Square it is to move to.

    // Constructor.  Just set the values of the instance variables.
    Movement(int row1, int col1, int row2, int col2) {
        fromRow = row1;
        fromCol = col1;

        toRow = row2;
        toCol = col2;
    }
    boolean isJump() {
        // Test whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
        return (fromRow - toRow >= 2 || fromRow - toRow <= -2);
    }
    boolean moveCheckerFrom(int row, int col){
        if(fromRow == row && fromCol == col){
            return true;
        }
        else{
            return false;
        }
    }
    boolean moveCheckerTo(int row, int col){
        if(toRow == row && toCol == col){
            return true;
        }
        else{
            return false;
        }
    }

    public Pawn [][] makeMove(Pawn [][] pawnsBoard){
        Pawn tempPawn = pawnsBoard[toRow][toCol];
        pawnsBoard[toRow][toCol] = pawnsBoard[fromRow][fromCol];
        pawnsBoard[fromRow][fromCol] = tempPawn;

        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            // The move is a jump.  Remove the jumped piece from the board.
            int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
            int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
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
