package org.example;
import javafx.scene.paint.Color;

/**
 * Class of a pawn
 */

public class Pawn {
    private PawnState state;
    Color color;

    /**
     * Constructor of a pawn.
     * @param color of pawn
     * @param row row of the field in which this pawn is
     * @param col column of the field in which this pawn is
     */
    public Pawn(Color color, int row, int col) {
        state = PawnState.NORMAL;
        this.color = color;
    }

    /**
     * Method to set state of the pawn.
     * If state is EMPTY set Color of the pawn to Transparent
     * @param state state of the pawn
     */
    public void setState(PawnState state) {
        this.state = state;
        if(state == PawnState.EMPTY){
            this.color = Color.TRANSPARENT;
        }
    }

    /**
     * Set color of the pawn
     * @param color we are changing to
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Get state of the pawn
     * @return state
     */
    public PawnState getState(){
        return state;
    }

    /**
     * Get color of the pawn
     * @return color;
     */
    public Color getColor() {
        return color;
    }
}