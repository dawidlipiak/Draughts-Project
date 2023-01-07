package org.example;


import javafx.scene.paint.Color;

/**
 * Class to create player.
 */
public class Player {

    /** If player is first, firstPlayer is true, otherwise is false. */
    private final boolean firstPlayer;
    private Color color;

    /**
     * Constructor of a player that creates pawns for him.
     * @param nrOfFields number of rows and columns for the board
     * @param firstPlayer true or false  depends on if the player is first
     */
    public Player(int nrOfFields, boolean firstPlayer){
        if(nrOfFields > 0) {
            this.firstPlayer = firstPlayer;
            setPlayerColor();
        }
        else {
            throw new IllegalArgumentException("Liczba pól musi być dodatnia");
        }
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
     * Get color assigned to the player
     * @return color of the player
     */
    public Color getColor(){
        return color;
    }

    /**
     * get if player is a first player
     * @return boolean value if the player is first player
     */
    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}