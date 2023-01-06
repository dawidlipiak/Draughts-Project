package org.example;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LegalMoves {
    private final int nrOfFileds;
    public LegalMoves(int nrOfFileds){
        this.nrOfFileds = nrOfFileds;
    }
    public Movement [] getLegalMoves(Player player, Pawn [][] pawnsBoard) {
        ArrayList<Movement> moves = new ArrayList<Movement>();

        for (int row = 0; row < nrOfFileds; row++) {
            for (int col = 0; col < nrOfFileds; col++) {
                if (pawnsBoard[row][col].getColor() == player.getColor()) {
                    if (legalJump(player,pawnsBoard, row, col, row+1, col+1, row+2, col+2)) {
                        moves.add(new Movement(row, col, row + 2, col + 2));
                    }
                    if (legalJump(player,pawnsBoard, row, col, row-1, col+1, row-2, col+2)) {
                        moves.add(new Movement(row, col, row - 2, col + 2));
                    }
                    if (legalJump(player,pawnsBoard, row, col, row+1, col-1, row+2, col-2)){
                        moves.add(new Movement(row, col, row+2, col-2));
                    }
                    if (legalJump(player,pawnsBoard, row, col, row-1, col-1, row-2, col-2))
                        moves.add(new Movement(row, col, row-2, col-2));
                }
            }
        }
        if(moves.size() == 0){
            for (int row = 0; row < nrOfFileds; row++) {
                for (int col = 0; col < nrOfFileds; col++) {
                    if (pawnsBoard[row][col].getColor() == player.getColor()) {
                        if (legalMove(player,pawnsBoard,row,col,row+1,col+1)) {
                            moves.add(new Movement(row, col, row + 1, col + 1));
                        }
                        if (legalMove(player,pawnsBoard,row,col,row-1,col+1)) {
                            moves.add(new Movement(row, col, row - 1, col + 1));
                        }
                        if (legalMove(player,pawnsBoard,row,col,row+1,col-1)){
                            moves.add(new Movement(row, col, row+1, col-1));
                        }
                        if (legalMove(player,pawnsBoard,row,col,row-1,col-1))
                            moves.add(new Movement(row, col, row-1, col-1));
                    }
                }
            }
        }
        if (moves.size() == 0) {
            return null;
        }
        else {
            Movement[] moveArray = new Movement[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    public Movement [] getLegalJumpsFrom (int row, int col, Player player, Pawn[][] pawnsBoard) {
        ArrayList<Movement> jumps = new ArrayList<>();
        if (pawnsBoard[row][col].getColor() == player.getColor()) {
            if (legalJump(player,pawnsBoard, row, col, row+1, col+1, row+2, col+2)) {
                jumps.add(new Movement(row, col, row + 2, col + 2));
            }
            if (legalJump(player,pawnsBoard, row, col, row-1, col+1, row-2, col+2)) {
                jumps.add(new Movement(row, col, row - 2, col + 2));
            }
            if (legalJump(player,pawnsBoard, row, col, row+1, col-1, row+2, col-2)){
                jumps.add(new Movement(row, col, row+2, col-2));
            }
            if (legalJump(player,pawnsBoard, row, col, row-1, col-1, row-2, col-2))
                jumps.add(new Movement(row, col, row-2, col-2));
        }

        if (jumps.size() == 0) {
            return null;
        } else {
            Movement[] jumpsArray = new Movement[jumps.size()];
            for (int i = 0; i < jumps.size(); i++)
                jumpsArray[i] = jumps.get(i);
            return jumpsArray;
        }
    }

    private boolean legalJump(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, int betweenRow, int betweenCol, int toRow, int toCol) {
        if (toRow < 0 || toRow >= nrOfFileds || toCol < 0 || toCol >= nrOfFileds ) {
            return false;  // (toRow,toCol) is off the board.
        }

        if (pawnsBoard[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;  // (toRow,toCol) already contains a piece.
        }

        if (player.isFirstPlayer()) {
            if (pawnsBoard[fromRow][fromCol].getColor() == Color.WHITE && toRow > fromRow) {
                return false;  // Regular white piece can only move up.
            }
            if (pawnsBoard[betweenRow][betweenCol].getColor() != Color.BLACK) {
                return false;  // There is no black piece to jump.
            }
            return true;  // The jump is legal.
        }
        else {
            if (pawnsBoard[fromRow][fromCol].getColor() == Color.BLACK && toRow < fromRow) {
                return false;  // Regular white piece can only move down.
            }
            if (pawnsBoard[betweenRow][betweenCol].getColor() != Color.WHITE) {
                return false;  // There is no black piece to jump.
            }
            return true;  // The jump is legal.
        }
    }

    private boolean legalMove(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, int toRow, int toCol) {
        if (toRow < 0 || toRow >= nrOfFileds || toCol < 0 || toCol >= nrOfFileds) {
            return false;  // (toRow,toCol) is off the board.
        }

        if (pawnsBoard[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;  // (toRow,toCol) already contains a piece.
        }

        if (player.isFirstPlayer()) {
            if (pawnsBoard[fromRow][fromCol].getColor() == Color.WHITE && toRow > fromRow)
                return false;  // Regular white piece can only move up.
            return true;  // The move is legal.
        }
        else {
            if (pawnsBoard[fromRow][fromCol].getColor() == Color.BLACK && toRow < fromRow)
                return false;  // Regular white piece can only move down.
            return true;  // The move is legal.
        }
    }
}
