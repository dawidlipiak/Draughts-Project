package org.example;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Main application frame as BorderPane
 */
public class ApplicationFrame extends BorderPane {

    /**
     * Constructor of the frame with added layouts needed
     * @param hbox contains buttons for changing draughts version
     * @param gridPane contains board for playing
     */
    ApplicationFrame(HBox hbox, GridPane gridPane) {
        setCenter(gridPane);
        setTop(hbox);
        setAlignment(gridPane, Pos.CENTER);
    }

//    ? DO KONSULTACJI ?
//    public BorderPane getFrame(){
//        return this;
//    }
}