package org.example;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of legal movements
 */
public class LegalMoves {
    private final int nrOfFileds;

    /**
     * Constructor of the legalMove object
     * @param nrOfFileds number of fields that the board contains in one direction
     */
    public LegalMoves(int nrOfFileds){
        this.nrOfFileds = nrOfFileds;
    }

    /**
     * Function returning all legal moves that can be made by a player
     * @param player which turn is now
     * @param pawnsBoard double array that contains pawns on every field
     * @return array of all legal moves that can be done
     */
    public Movement [] getLegalMoves(Player player, Pawn [][] pawnsBoard) {
//        for(int i  = 0; i < nrOfFileds; i ++){
//            for (int j = 0; j < nrOfFileds; j++){
//                System.out.print(pawnsBoard[i][j].getState() + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        ArrayList<Movement> moves = new ArrayList<Movement>();
        int distance;

        for(distance = 2; distance < nrOfFileds; distance++) {
            for (int row = 0; row < nrOfFileds; row++) {
                for (int col = 0; col < nrOfFileds; col++) {
                    if (pawnsBoard[row][col].getColor() == player.getColor()) {
                        addlegalJumps(moves, distance, pawnsBoard, player, row, col);
                    }
                }
            }
        }

        if(moves.size() == 0){
            for(distance = 1; distance < nrOfFileds; distance++) {
                for (int row = 0; row < nrOfFileds; row++) {
                    for (int col = 0; col < nrOfFileds; col++) {
                        if (pawnsBoard[row][col].getColor() == player.getColor()) {
                            addlegalMoves(moves, distance, pawnsBoard, player, row, col);
                        }
                    }
                }
            }
        }

        if (moves.size() == 0) {
            return null;
        }
        else {
            Movement[] moveArray = new Movement[moves.size()];
            for (int i = 0; i < moves.size(); i++) {
                moveArray[i] = moves.get(i);
            }
            return moveArray;
        }
    }

    /**
     * Function when we check if there are more jumps to make after first one
     * @param row from we are checking if there are more jumps
     * @param col from we are checking if there are more jumps
     * @param player for which we check jumps
     * @param pawnsBoard containing pawns on all places at the board
     * @return jumps that can be made from (row,col) position
     */
    public Movement [] getLegalJumpsFrom (int row, int col, Player player, Pawn[][] pawnsBoard) {
//        for(int i  = 0; i < nrOfFileds; i ++){
//            for (int j = 0; j < nrOfFileds; j++){
//                System.out.print(pawnsBoard[i][j].getState() + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        ArrayList<Movement> jumps = new ArrayList<>();
        int distance;

        for(distance = 2; distance < nrOfFileds; distance++) {
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


    /**
     * Method checking possible jumps that can be made from (row,col) position and adding them to the array list
     * @param moves array list of Movements for jumps that are legal to do
     * @param distance distance from (row,col) to the position we are jumping to
     * @param player for which we check jumps
     * @param pawnsBoard containing pawns on all places at the board
     * @param row from we are checking if there are more jumps
     * @param col from we are checking if there are more jumps
     */
    private void addlegalJumps(final ArrayList<Movement> moves, final int distance, final Pawn [][] pawnsBoard, final Player player, final int row, final int col){
        // Normal pawns can't do jumps with distance more than 2
        if(pawnsBoard[row][col].getState() == PawnState.NORMAL && distance > 2){
            return;
        }

        final ArrayList <Integer> betweenRows = new ArrayList<>();
        final ArrayList <Integer> betweenCols = new ArrayList<>();
        Function function = new Function() {
            @Override
            public void checkAndAdd (int direction){
                int rowDirection = 0;  // -1 UP   , 1 DOWN
                int colDirection = 0;  // -1 LEFT ,  1 RIGHT
                switch (direction){
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
                for(int i  = 1; i < distance; i++){
                    betweenRows.add(row+(i*rowDirection));
                    betweenCols.add(col+(i*colDirection));
                }

                if (legalJump(player,pawnsBoard, row, col, betweenRows, betweenCols, row+(distance*rowDirection), col+(distance*colDirection))) {
                    moves.add(new Movement(row, col, row + (distance*rowDirection), col + (distance*colDirection)));
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


    /**
     * Function checking if the concrete jump is legal
     * @param player for which we check jumps
     * @param pawnsBoard containing pawns on all places at the board
     * @param fromRow row from we are making the jump
     * @param fromCol col from we are making the jump
     * @param betweenRows Array list of rows between fromRow and toRow
     * @param betweenCols Array list of rows between fromCol and toCol
     * @param toRow row to which are making a jump
     * @param toCol col to which are making a jump
     * @return boolean value of if the jump is legal
     */
    private boolean legalJump(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol) {
        // CHECK DIRECTION OF MOVE
        boolean wrongDirection = false;
        if(player.isFirstPlayer()){
            if(toRow > fromRow) {
                wrongDirection = true;
            }
        }
        else {
            if(toRow < fromRow) {
                wrongDirection = true;
            }
        }

        // GET OPPONENT COLOR
        Color opponentColor;
        if(player.isFirstPlayer()){
            opponentColor = Color.BLACK;
        }
        else {
            opponentColor = Color.WHITE;
        }

        // CHECK IF THE MOVE TO A (toRow,toCol) POSITION IS OFF THE BOARD
        if (toRow < 0 || toRow >= nrOfFileds || toCol < 0 || toCol >= nrOfFileds ) {
            return false;
        }

        // CHECK IF (toRow,toCol) ALREADY CONTAINS A PIECE
        if (pawnsBoard[toRow][toCol].getState() != PawnState.EMPTY) {
            return false;
        }

        // CHECK IF ANY PLAYER'S PAWN IS ON THE WAY OF THE MOVE
        for(int i = 0; i < betweenRows.size(); i++){
            if(pawnsBoard[betweenRows.get(i)][betweenCols.get(i)].getColor() == player.getColor()){
                return false;
            }
        }

        // MOVEMENT WRONG DIRECTION
        if ((pawnsBoard[fromRow][fromCol].getState() == PawnState.NORMAL) && wrongDirection) {
            if (betweenRows.size() == 1) {
                return false;  // NORMAL white piece can only jump one and move one direction.
            }
        }

        if(betweenRows.size() > 1){
            ArrayList<Integer> beforeRows = new ArrayList<>();
            ArrayList<Integer> beforeCols = new ArrayList<>();
            int i = 0;
            while(i < betweenRows.size() && pawnsBoard[betweenRows.get(i)][betweenCols.get(i)].getColor() != opponentColor){
                beforeRows.add(betweenRows.get(i));
                beforeCols.add(betweenCols.get(i));
                i++;
            }

            if(beforeRows.size() == betweenRows.size()){
                return false; // NO PIECES TO JUMP THROUGH
            }

            if(i != 0) {
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
                            for (int j = i+1; j < betweenRows.size(); j++) {
                                afterRows.add(betweenRows.get(j));
                                afterCols.add(betweenCols.get(j));
                            }
                            // CHECK IF THE MOVE FROM BEHIND THE PAWN TO (toRow,toCol) IS LEGAL
                            if (!legalMove(player, pawnsBoard, betweenRows.get(i+1), betweenCols.get(i+1), afterRows, afterCols, toRow, toCol)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        else {
            if(pawnsBoard[betweenRows.get(0)][betweenCols.get(0)].getColor() != opponentColor){
                return false;
            }
        }
        return true;  // The jump is legal.
    }


    /**
     * Method checking possible moves that can be made from (row,col) position and adding them to the array list
     * @param moves array list of Movements for moves that are legal to do
     * @param distance distance from (row,col) to the position we are moving to
     * @param player for which we check the move
     * @param pawnsBoard containing pawns on all places at the board
     * @param row from we are checking if there are a move
     * @param col from we are checking if there are a move
     */
    public void addlegalMoves(final ArrayList<Movement> moves, final int distance, final Pawn [][] pawnsBoard, final Player player, final int row, final int col){
        final ArrayList <Integer> betweenRows = new ArrayList<>();
        final ArrayList <Integer> betweenCols = new ArrayList<>();
        Function function = new Function() {
            @Override
            public void checkAndAdd (int direction){
                int rowDirection = 0;  // -1 UP   , 1 DOWN
                int colDirection = 0;  // -1 LEFT ,  1 RIGHT
                switch (direction){
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
                for(int i  = 1; i < distance; i++){
                    betweenRows.add(row+(i*(rowDirection)));
                    betweenCols.add(col+i*(colDirection));
                }

                if (legalMove(player,pawnsBoard, row, col, betweenRows, betweenCols, row+(distance*rowDirection), col+(distance*colDirection))) {
                    moves.add(new Movement(row, col, row + (distance*rowDirection), col + (distance*colDirection)));
                }
                betweenRows.clear();
                betweenCols.clear();
            }
        };
        if(pawnsBoard[row][col].getState() == PawnState.KING || ((pawnsBoard[row][col].getState() == PawnState.NORMAL) && (distance == 1) )){

            function.checkAndAdd(0);  // MOVE DOWN RIGHT

            function.checkAndAdd(1);  // MOVE DOWN LEFT

            function.checkAndAdd(2);  // MOVE UP RIGHT

            function.checkAndAdd(3);  // MOVE UP LEFT
        }
    }



    /**
     * Function checking if the concrete move is legal
     * @param player for which we check the move
     * @param pawnsBoard containing pawns on all places at the board
     * @param fromRow row from we are making the move
     * @param fromCol col from we are making the move
     * @param betweenRows Array list of rows between fromRow and toRow
     * @param betweenCols Array list of rows between fromCol and toCol
     * @param toRow row to which are making the move
     * @param toCol col to which are making the move
     * @return boolean value of if the move is legal
     */
    private boolean legalMove(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol) {
        if (toRow < 0 || toRow >= nrOfFileds || toCol < 0 || toCol >= nrOfFileds) {
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
        if(player.isFirstPlayer()){
            if(toRow > fromRow) {
                wrongDirection = true;
            }
        }
        else {
            if(toRow < fromRow) {
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
    interface Function{
        /**
         * Method checking the direction of the move/jump, if it's legal and adds the move to the array if it is
         * @param direction
         */
        void checkAndAdd(int direction);}
}
