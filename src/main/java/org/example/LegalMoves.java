package org.example;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LegalMoves implements LegalMovesInterface{
    private ArrayList<Movement> moves = new ArrayList<Movement>();
    private Board board;
    private Pawn pawn;
    @Override
    public ArrayList<Movement> getLegalMoves(Pawn pawn, Board board) {
        this.board = board;
        this.pawn = pawn;

        if(pawn.getColor() != Color.WHITE && pawn.getColor() != Color.BLACK){
            return null;
        }

        addLegalJumps();

        if(moves.size() == 0){
            for (int row = 0; row < board.getNrOfFields(); row++) {
                for (int col = 0; col < board.getNrOfFields(); col++) {
                    if (board.getBoard()[row][col].getState() == PawnState.NORMAL || board.getBoard()[row][col].getState() == PawnState.KING) {
                        if (legalMove(pawn,row,col,row+1,col+1)) {
                            moves.add(new Movement(row, col, row + 1, col + 1));
                        }
                        if (legalMove(pawn,row,col,row-1,col+1)) {
                            moves.add(new Movement(row, col, row - 1, col + 1));
                        }
                        if (legalMove(pawn,row,col,row+1,col-1)){
                            moves.add(new Movement(row, col, row+1, col-1));
                        }
                        if (legalMove(pawn,row,col,row-1,col-1))
                            moves.add(new Movement(row, col, row-1, col-1));
                    }
                }
            }
        }

        if (moves.size() == 0) {
            return null;
        }

        return moves;
    }

    @Override
    public void addLegalJumps() {
        for (int row = 0; row < board.getNrOfFields(); row++) {
            for (int col = 0; col < board.getNrOfFields(); col++) {
                if (board.getBoard()[row][col].getState() == PawnState.NORMAL || board.getBoard()[row][col].getState() == PawnState.KING) {
                    if (legalJump(pawn, row, col, row+1, col+1, row+2, col+2)) {
                        moves.add(new Movement(row, col, row + 2, col + 2));
                    }
                    if (legalJump(pawn, row, col, row-1, col+1, row-2, col+2)) {
                        moves.add(new Movement(row, col, row - 2, col + 2));
                    }
                    if (legalJump(pawn, row, col, row+1, col-1, row+2, col-2)){
                        moves.add(new Movement(row, col, row+2, col-2));
                    }
                    if (legalJump(pawn, row, col, row-1, col-1, row-2, col-2))
                        moves.add(new Movement(row, col, row-2, col-2));
                }
            }
        }
    }

    @Override
    public boolean legalJump(Pawn pawn, int fromRow, int fromCol, int betweenRow, int betweenCol, int toRow, int toCol) {
        if (toRow < 0 || toRow >= board.getNrOfFields() || toCol < 0 || toCol >= board.getNrOfFields()) {
            return false;  // (toRow,toCol) is off the board.
        }

        if (board.getBoard()[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;  // (toRow,toCol) already contains a piece.
        }

        if (pawn.getColor() == Color.WHITE) {
            if (board.getBoard()[fromRow][fromCol].getColor() == Color.WHITE && toRow > fromRow)
                return false;  // Regular white piece can only move up.
            if (board.getBoard()[betweenRow][betweenCol].getColor() != Color.BLACK && board.getBoard()[betweenRow][betweenCol].getState() != PawnState.KING)
                return false;  // There is no black piece to jump.
            return true;  // The jump is legal.
        }
        else {
            if (board.getBoard()[fromRow][fromCol].getColor() == Color.BLACK && toRow < fromRow)
                return false;  // Regular white piece can only move down.
            if (board.getBoard()[betweenRow][betweenCol].getColor() != Color.WHITE && board.getBoard()[betweenRow][betweenCol].getState() != PawnState.KING)
                return false;  // There is no white piece to jump.
            return true;  // The jump is legal.
        }
    }

    @Override
    public boolean legalMove(Pawn pawn, int fromRow, int fromCol, int toRow, int toCol) {
        if (toRow < 0 || toRow >= board.getNrOfFields() || toCol < 0 || toCol >= board.getNrOfFields()) {
            return false;  // (toRow,toCol) is off the board.
        }

        if (board.getBoard()[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;  // (toRow,toCol) already contains a piece.
        }

        if (pawn.getColor() == Color.WHITE) {
            if (board.getBoard()[fromRow][fromCol].getColor() == Color.WHITE && toRow > fromRow)
                return false;  // Regular white piece can only move up.
            return true;  // The move is legal.
        }
        else {
            if (board.getBoard()[fromRow][fromCol].getColor() == Color.BLACK && toRow < fromRow)
                return false;  // Regular white piece can only move down.
            return true;  // The move is legal.
        }
    }
}
