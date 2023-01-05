package org.example;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends GridPane{
    /** Array of center points of every square field on board */
    private final Point2D[][] positions;
    /** Length of a single field side */
    private final int fieldLength;

    /**
     * Constructor of a play board for draughts
     * @param numberOfFields number of Fields in a row and column
     * @param boardSize size of a window
     */
    public Board(int numberOfFields,int boardSize){
        int count = 0;
        positions = new Point2D[numberOfFields][numberOfFields/2];

        // fieldLength of rectangle
        fieldLength = boardSize / numberOfFields;

        // Create 64 rectangles and add to gridPane
        for (int i = 0; i < numberOfFields; i++) {
            count++;
            for (int j = 0; j < numberOfFields; j++) {
                Rectangle rectangle = new Rectangle(fieldLength, fieldLength, fieldLength, fieldLength);
                if (count % 2 == 0) {
                    rectangle.setFill(Color.MOCCASIN);
                }
                else {
                    rectangle.setFill(Color.PERU);
                }
                add(rectangle, j, i);
                count++;
            }
        }
        setAlignment(Pos.CENTER);
    }

    /**
     * Function for setting points for pawns to move on
     * @param radius of a pawn to calculate offset
     */
    public void setPositionsToMoveOn (double radius) {
        double offset = (fieldLength-4*radius);
        for(int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < (getColumnCount()/2); j++){
                if( i % 2 == 0){
                    positions[i][j] = new Point2D((-0.5*fieldLength) + j*2*fieldLength - offset,(-0.5*fieldLength)+ i*2*(0.5*fieldLength) - offset);
                }
                else {
                    positions[i][j] = new Point2D((0.5*fieldLength) + j*2*fieldLength - offset,(-0.5*fieldLength)+ i*2*(0.5*fieldLength) - offset);
                }
            }
        }
    }

    /**
     * Function returning X position of fields' center position
     * @param row of filed
     * @param column of field
     * @return point of center position for given row and column
     */
    public double getXPosition(int row, int column) {
        return positions[row][column].getX();
    }
    /**
     * Function returning Y position of fields' center position
     * @param row of filed
     * @param column of field
     * @return point of center position for given row and column
     */
    public double getYPosition(int row, int column) {
        return positions[row][column].getY();
    }

}