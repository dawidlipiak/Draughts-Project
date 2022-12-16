package org.example;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
    private GridPane gridPane;
    public Board(){
        // Create new GridPane
        gridPane = new GridPane();

        // Create 64 rectangles and add to gridPane
        int count = 0;
        // side of rectangle
        double s = 100;
        for (int i = 0; i < 8; i++) {
            count++;
            for (int j = 0; j < 8; j++) {
                Rectangle r = new Rectangle(s, s, s, s);
                if (count % 2 == 0)
                    r.setFill(Color.WHITE);
                gridPane.add(r, j, i);
                count++;
            }
        }
    }
    /**
     *
     * @return GridPane
     */
    public GridPane getBoard() {
        return gridPane;
    }
}
