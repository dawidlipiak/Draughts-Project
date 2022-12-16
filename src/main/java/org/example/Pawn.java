package org.example;

import javafx.scene.shape.Circle;

public class Pawn extends Circle {
    private double x;
    private double y;
    private String color;
    public Pawn(double x, double y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
