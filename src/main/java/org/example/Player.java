package org.example;


import javafx.scene.paint.Color;

/**
 * Class to create player.
 */
public class Player {
    private final Pawn [][] playerPawns;
    private final int nrOfFields;

    /** If player is first, firstPlayer is true, otherwise is false. */
    private final boolean firstPlayer;
    private Color color;

    /**
     * Constructor of a player that creates pawns for him.
     * @param nrOfFields number of rows and columns for the board
     * @param firstPlayer true or false  depends on if the player is first
     */
    public Player(int nrOfFields, boolean firstPlayer){
        playerPawns = new Pawn[nrOfFields][nrOfFields];
        this.nrOfFields = nrOfFields;
        this.firstPlayer = firstPlayer;
        setPlayerColor();
    }

    /**
     * Method to set the Color of the player.
     * First player has White pawns, second one has Black pawns.
     */
    private void setPlayerColor(){
        if(firstPlayer){
            color = Color.WHITE;
        }
        else {
            color = Color.BLACK;
        }
    }

    /**
     * Method to add Pawns to Array with player Pawns
     * @param pawn pawn
     */
    public void addPawns(Pawn pawn){
        int row = pawn.getRow();
        int col = pawn.getCol();
        playerPawns[row][col] = pawn;
    }
    public Color getColor(){
        return color;
    }
    public Pawn [][] getPlayerPawns() {
        return playerPawns;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}