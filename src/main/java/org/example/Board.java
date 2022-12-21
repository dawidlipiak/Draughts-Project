package org.example;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends GridPane{

    /**
     * Gridpane board for play
     */
    public Board(int numberOfFields){
        int boardSize = 600;
        int count = 0;

        // side of rectangle
        int s = boardSize / numberOfFields;

        // Create 64 rectangles and add to gridPane
        for (int i = 0; i < numberOfFields; i++) {
            count++;
            for (int j = 0; j < numberOfFields; j++) {
                Rectangle rectangle = new Rectangle(s, s, s, s);
                if (count % 2 == 0)
                    rectangle.setFill(Color.WHITE);
                this.add(rectangle, j, i);
                count++;
            }
        }
        setAlignment(Pos.CENTER);
    }

//   ? DO KONSULTACJI  ?
//    public GridPane getBoard() {
//        return this;
//    }
}
