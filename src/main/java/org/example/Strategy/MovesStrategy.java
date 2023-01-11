package org.example.Strategy;

import org.example.Movement;
import org.example.Pawn;
import org.example.Player;

import java.util.ArrayList;

/**
 * Interface implemented by strategies with methods to check legality of the moves.
 * Used in Strategy Pattern.
 */
public interface MovesStrategy {
    /**
     * Function returning all legal moves that can be made by a player
     * @param player which turn is now
     * @param pawnsBoard double array that contains pawns on every field
     * @return array of all legal moves that can be done
     */
    Movement[] getLegalMoves(Player player, Pawn[][] pawnsBoard);

    /**
     * Function when we check if there are more jumps to make after first one
     *
     * @param row        from we are checking if there are more jumps
     * @param col        from we are checking if there are more jumps
     * @param player     for which we check jumps
     * @param pawnsBoard containing pawns on all places at the board
     * @return jumps that can be made from (row,col) position
     */
    Movement [] getLegalJumpsFrom (int row, int col, Player player, Pawn[][] pawnsBoard);

    /**
     * Method checking possible jumps that can be made from (row,col) position and adding them to the array list
     *
     * @param moves      array list of Movements for jumps that are legal to do
     * @param distance   distance from (row,col) to the position we are jumping to
     * @param player     for which we check jumps
     * @param pawnsBoard containing pawns on all places at the board
     * @param row        from we are checking if there are more jumps
     * @param col        from we are checking if there are more jumps
     */
    void addlegalJumps(final ArrayList<Movement> moves, final int distance, final Pawn [][] pawnsBoard, final Player player, final int row, final int col);

    /**
     * Function checking if the concrete jump is legal
     *
     * @param player      for which we check jumps
     * @param pawnsBoard  containing pawns on all places at the board
     * @param fromRow     row from we are making the jump
     * @param fromCol     col from we are making the jump
     * @param betweenRows Array list of rows between fromRow and toRow
     * @param betweenCols Array list of rows between fromCol and toCol
     * @param toRow       row to which are making a jump
     * @param toCol       col to which are making a jump
     * @return boolean value of if the jump is legal
     */
    boolean legalJump(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol);

    /**
     * Method checking possible moves that can be made from (row,col) position and adding them to the array list
     *
     * @param moves      array list of Movements for moves that are legal to do
     * @param distance   distance from (row,col) to the position we are moving to
     * @param player     for which we check the move
     * @param pawnsBoard containing pawns on all places at the board
     * @param row        from we are checking if there are a move
     * @param col        from we are checking if there are a move
     */
    void addlegalMoves(final ArrayList<Movement> moves, final int distance, final Pawn [][] pawnsBoard, final Player player, final int row, final int col);

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
    boolean legalMove(Player player, Pawn [][] pawnsBoard, int fromRow, int fromCol, ArrayList<Integer> betweenRows, ArrayList<Integer> betweenCols, int toRow, int toCol);

}
