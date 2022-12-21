package org.example;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends GridPane{
    // Array of center points of every square field on board
    private final Point2D[][] centerFieldPosition;
    // Array of square fields
    private final Rectangle[][] fields;

    /**
     * Constructor of a play board for draughts
     * @param numberOfFields number of row fields
     * @param boardSize size of a window
     */
    public Board(int numberOfFields,int boardSize){
        int count = 0;
        fields = new Rectangle[numberOfFields][numberOfFields];
        centerFieldPosition = new Point2D[numberOfFields][numberOfFields];

        // side of rectangle
        int side = boardSize / numberOfFields;

        // Create 64 rectangles and add to gridPane
        for (int i = 0; i < numberOfFields; i++) {
            count++;
            for (int j = 0; j < numberOfFields; j++) {
                Rectangle rectangle = new Rectangle(side, side, side, side);
                if (count % 2 == 0) {
                    rectangle.setFill(Color.WHITE);
                    fields [i][j] = rectangle;
                    //System.out.println(fields[i][j]);
                }
                add(rectangle, j, i);
                count++;
            }
        }
        setAlignment(Pos.CENTER);
        //setCenterFieldPosition();
    }

    /**
     * Function for setting center positions of fields
     */
    private void setCenterFieldPosition () {

        for(int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < getColumnCount(); j++){
                Point2D point = new Point2D(fields[i][j].getX(),fields[i][j].getY());
                centerFieldPosition[i][j] = point;
            }
        }
    }

    /**
     * Function returning points of fields' center positions
     * @param row of filed
     * @param column of field
     * @return point of center position for given row and column
     */
    public Point2D getCenterFieldPosition (int row, int column) {
        return centerFieldPosition[row][column];
    }
}
