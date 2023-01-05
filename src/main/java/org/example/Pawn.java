package org.example;
import javafx.scene.paint.Color;

/**
 * Class of a pawn
 */

public class Pawn {
    private PawnState state;
    Color color;

    /**
     * Constructor for pawn that extends after Circle
     * @param color of the pawn
     */
    public Pawn(Color color) {
        state = PawnState.NORMAL;
        this.color = color;
    }

    public void setState(PawnState state) {
        this.state = state;
    }

    public PawnState getState(){
        return state;
    }

    public Color getColor() {
        return color;
    }
}
