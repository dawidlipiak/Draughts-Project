package org.example.Strategy;

import javafx.scene.paint.Color;
import org.example.*;

import java.util.ArrayList;

/**
 * Class implements MoveStrategy interface where all methods are described.
 * Class with applied moves roles from Spanish version of the game.
 * In this version: normal pawn can not beat back, King can move multiple fields,
 * normal pawn can beat up a king.
 */

public class SpanishStrategy implements MovesStrategy {

    int nrOfFields;

    /**
     * Constructor of the legalMove object
     *
     * @param nrOfFields number of fields that the board contains in one direction
     */

    public SpanishStrategy(int nrOfFields) {
        this.nrOfFields = nrOfFields;
    }

    @Override
    public Movement[] getLegalMoves(Player player, Pawn[][] pawnsBoard) {

        ArrayList<Movement> moves = new ArrayList<Movement>();
        int distance;

        for (distance = 2; distance < nrOfFields; distance++) {
            for (int row = 0; row < nrOfFields; row++) {
                for (int col = 0; col < nrOfFields; col++) {
                    if (pawnsBoard[row][col].getColor() == player.getColor()) {
                        addlegalJumps(moves, distance, pawnsBoard, player, row, col);
                    }
                }
            }
        }

        if (moves.size() == 0) {
            for (distance = 1; distance < nrOfFields; distance++) {
                for (int row = 0; row < nrOfFields; row++) {
                    for (int col = 0; col < nrOfFields; col++) {
                        if (pawnsBoard[row][col].getColor() == player.getColor()) {
                            addlegalMoves(moves, distance, pawnsBoard, player, row, col);
                        }
                    }
                }
            }
        }

        if (moves.size() == 0) {
            return null;
        } else {
            Movement[] moveArray = new Movement[moves.size()];
            for (int i = 0; i < moves.size(); i++) {
                moveArray[i] = moves.get(i);
            }
            return moveArray;
        }
    }

    @Override
    public Movement[] getLegalJumpsFrom(int row, int col, Player player, Pawn[][] pawnsBoard) {

        ArrayList<Movement> jumps = new ArrayList<>();
        int distance;

        for (distance = 2; distance < nrOfFields; distance++) {
            if (pawnsBoard[row][col].getColor() == player.getColor()) {
                addlegalJumps(jumps, distance, pawnsBoard, player, row, col);
            }
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

    @Override
    public void addlegalJumps(final ArrayList<Movement> moves, final int distance, final Pawn[][] pawnsBoard, final Player player, final int row, final int col) {
        // Normal pawns can't do jumps with distance more than 2
        if (pawnsBoard[row][col].getState() == PawnState.NORMAL && distance > 2) {
            return;
        }

        final ArrayList<Integer> betweenRows = new ArrayList<>();
        final ArrayList<Integer> betweenCols = new ArrayList<>();
        SpanishStrategy.Function function = new SpanishStrategy.Function() {
            @Override
            public void checkAndAdd(int direction) {
                int rowDirection = 0;  // -1 UP   , 1 DOWN
                int colDirection = 0;  // -1 LEFT ,  1 RIGHT
                switch (direction) {
                    case 0: //JUMP DOWN RIGHT
                        rowDirection = 1;
                        colDirection = 1;
                        break;
                    case 1: // JUMP UP RIGHT
                        rowDirection = -1;
                        colDirection = 1;
                        break;
                    case 2: // JUMP DOWN LEFT
                        rowDirection = 1;
                        colDirection = -1;
                        break;
                    case 3: // JUMP UP LEFT
                        rowDirection = -1;
                        colDirection = -1;
                        break;
                }
                for (int i = 1; i < distance; i++) {
                    betweenRows.add(row + (i * rowDirection));
                    betweenCols.add(col + (i * colDirection));
                }

                if (legalJump(player, pawnsBoard, row, col, betweenRows, betweenCols, row + (distance * rowDirection), col + (distance * colDirection))) {
                    moves.add(new Movement(row, col, row + (distance * rowDirection), col + (distance * colDirection)));
                }
                betweenRows.clear();
                betweenCols.clear();
            }
        };

        // JUMP DOWN RIGHT
        function.checkAndAdd(0);

        // JUMP DOWN LEFT
        function.checkAndAdd(1);

        // JUMP UP RIGHT
        function.checkAndAdd(2);

        // JUMP UP LEFT
        function.checkAndAdd(3);
    }

    @Override
    public boolean legalJump(Player player, Pawn[][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol) {
        // CHECK DIRECTION OF MOVE
        boolean wrongDirection = false;
        if (player.isFirstPlayer()) {
            if (toRow > fromRow) {
                wrongDirection = true;
            }
        } else {
            if (toRow < fromRow) {
                wrongDirection = true;
            }
        }

        // GET OPPONENT COLOR
        Color opponentColor;
        if (player.isFirstPlayer()) {
            opponentColor = Color.BLACK;
        } else {
            opponentColor = Color.WHITE;
        }

        // CHECK IF THE MOVE TO A (toRow,toCol) POSITION IS OFF THE BOARD
        if (toRow < 0 || toRow >= nrOfFields || toCol < 0 || toCol >= nrOfFields) {
            return false;
        }

        // CHECK IF (toRow,toCol) ALREADY CONTAINS A PIECE
        if (pawnsBoard[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;
        }

        // CHECK IF ANY PLAYER'S PAWN IS ON THE WAY OF THE MOVE
        for (int i = 0; i < betweenRows.size(); i++) {
            if (pawnsBoard[betweenRows.get(i)][betweenCols.get(i)].getColor() == player.getColor()) {
                return false;
            }
        }

        // MOVEMENT WRONG DIRECTION
        if ((pawnsBoard[fromRow][fromCol].getState() == PawnState.NORMAL) && wrongDirection) {
            if (betweenRows.size() == 1) {
                return false;  // NORMAL white piece can only jump one and move one direction.
            }
        }

        if (betweenRows.size() > 1) {
            ArrayList<Integer> beforeRows = new ArrayList<>();
            ArrayList<Integer> beforeCols = new ArrayList<>();
            int i = 0;
            while (i < betweenRows.size() && pawnsBoard[betweenRows.get(i)][betweenCols.get(i)].getColor() != opponentColor) {
                beforeRows.add(betweenRows.get(i));
                beforeCols.add(betweenCols.get(i));
                i++;
            }

            if (beforeRows.size() == betweenRows.size()) {
                return false; // NO PIECES TO JUMP THROUGH
            }

            if (i != 0) {
                // CHECK IF THE MOVE TO THE PLACE ONE BEFORE AN OPPONENT PAWN IS LEGAL
                if (legalMove(player, pawnsBoard, fromRow, fromCol, beforeRows, beforeCols, betweenRows.get(i - 1), betweenCols.get(i - 1))) { // Pawn at i index position
                    // CHECK IF i INDEX IS NOT THE LAST ONE IN BETWEEN LIST
                    if (betweenRows.size() != i + 1) {
                        // CHECK IF THE PLACE AFTER THE PAWN IS EMPTY
                        if (pawnsBoard[betweenRows.get(i + 1)][betweenCols.get(i + 1)].getState() != PawnState.EMPTY) {
                            return false;
                        } else {
                            ArrayList<Integer> afterRows = new ArrayList<>();
                            ArrayList<Integer> afterCols = new ArrayList<>();
                            for (int j = i + 1; j < betweenRows.size(); j++) {
                                afterRows.add(betweenRows.get(j));
                                afterCols.add(betweenCols.get(j));
                            }
                            // CHECK IF THE MOVE FROM BEHIND THE PAWN TO (toRow,toCol) IS LEGAL
                            if (!legalMove(player, pawnsBoard, betweenRows.get(i + 1), betweenCols.get(i + 1), afterRows, afterCols, toRow, toCol)) {
                                return false;
                            }
                        }
                    }
                }
            }
            else {
                // CHECK IF i INDEX IS NOT THE LAST ONE IN BETWEEN LIST
                if (betweenRows.size() != i + 1) {
                    // CHECK IF THE PLACE AFTER THE PAWN IS EMPTY
                    if (pawnsBoard[betweenRows.get(i + 1)][betweenCols.get(i + 1)].getState() != PawnState.EMPTY) {
                        return false;
                    }
                }
            }
        } else {
            if (pawnsBoard[betweenRows.get(0)][betweenCols.get(0)].getColor() != opponentColor) {
                return false;
            }
        }
        return true;  // The jump is legal.
    }
    
    @Override
    public void addlegalMoves(final ArrayList<Movement> moves, final int distance, final Pawn[][] pawnsBoard, final Player player, final int row, final int col) {
        final ArrayList<Integer> betweenRows = new ArrayList<>();
        final ArrayList<Integer> betweenCols = new ArrayList<>();
        SpanishStrategy.Function function = new SpanishStrategy.Function() {
            @Override
            public void checkAndAdd(int direction) {
                int rowDirection = 0;  // -1 UP   , 1 DOWN
                int colDirection = 0;  // -1 LEFT ,  1 RIGHT
                switch (direction) {
                    case 0: //JUMP DOWN RIGHT
                        rowDirection = 1;
                        colDirection = 1;
                        break;
                    case 1: // JUMP UP RIGHT
                        rowDirection = -1;
                        colDirection = 1;
                        break;
                    case 2: // JUMP DOWN LEFT
                        rowDirection = 1;
                        colDirection = -1;
                        break;
                    case 3: // JUMP UP LEFT
                        rowDirection = -1;
                        colDirection = -1;
                        break;
                }
                for (int i = 1; i < distance; i++) {
                    betweenRows.add(row + (i * (rowDirection)));
                    betweenCols.add(col + i * (colDirection));
                }

                if (legalMove(player, pawnsBoard, row, col, betweenRows, betweenCols, row + (distance * rowDirection), col + (distance * colDirection))) {
                    moves.add(new Movement(row, col, row + (distance * rowDirection), col + (distance * colDirection)));
                }
                betweenRows.clear();
                betweenCols.clear();
            }
        };
        if (pawnsBoard[row][col].getState() == PawnState.KING || ((pawnsBoard[row][col].getState() == PawnState.NORMAL) && (distance == 1))) {

            function.checkAndAdd(0);  // MOVE DOWN RIGHT

            function.checkAndAdd(1);  // MOVE DOWN LEFT

            function.checkAndAdd(2);  // MOVE UP RIGHT

            function.checkAndAdd(3);  // MOVE UP LEFT
        }
    }

    @Override
    public boolean legalMove(Player player, Pawn[][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol) {
        if (toRow < 0 || toRow >= nrOfFields || toCol < 0 || toCol >= nrOfFields) {
            return false;  // (toRow,toCol) is off the board.
        }

        if (pawnsBoard[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;  // (toRow,toCol) already contains a piece.
        }

        // Check if all fields between (fromRow,fromCol) and (toRow,toCol) are empty
        for (int i = 0; i < betweenRows.size(); i++) {
            if (pawnsBoard[betweenRows.get(i)][betweenCols.get(i)].getState() != PawnState.EMPTY) {
                return false;
            }
        }

        boolean wrongDirection = false;
        if (player.isFirstPlayer()) {
            if (toRow > fromRow) {
                wrongDirection = true;
            }
        } else {
            if (toRow < fromRow) {
                wrongDirection = true;

            }
        }

        if (pawnsBoard[fromRow][fromCol].getState() == PawnState.NORMAL && wrongDirection) {
            return false;  // Regular white piece can only move up.
        }
        return true;  // The move is legal.
    }

    /**
     * Interface with method checkAndAdd
     */
    interface Function {
        /**
         * Method checking the direction of the move/jump, if it's legal and adds the move to the array if it is
         *
         * @param direction to move/jump
         */
        void checkAndAdd(int direction);}
}
