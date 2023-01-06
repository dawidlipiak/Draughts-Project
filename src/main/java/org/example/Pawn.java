package org.example;
import javafx.scene.paint.Color;

/**
 * Class of a pawn
 */

public class Pawn {
    private PawnState state;
    private final int row;
    private final int col;
    Color color;

    /**
     * Constructor for pawn that extends after Circle
     * @param color of the pawn
     */
    public Pawn(Color color, int row, int col) {
        state = PawnState.NORMAL;
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public void setState(PawnState state) {
        this.state = state;
        if(state == PawnState.EMPTY){
            this.color = Color.TRANSPARENT;
        }
    }

    public PawnState getState(){
        return state;
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
}
