package org.example;
import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

/**
 * Class of a pawn
 */
public class Pawn extends Circle {
    private double x;
    private double y;

    /**
     * Constructor for pawn that extends after Circle
     * @param color of the pawn
     * @param radius of the pawn circle
     */
    public Pawn(Color color, double radius ) {
        this.setFill(color);
        this.setRadius(radius);
    }

    /**
     * Function for setting position of a pawn
     * @param x position
     * @param y position
     */
    public void setPosition(double x, double y){
        this.relocate(x,y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
