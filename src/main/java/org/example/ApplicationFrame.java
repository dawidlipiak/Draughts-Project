package org.example;


import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


/**
 * Main application frame as BorderPane
 */
public class ApplicationFrame extends Pane {

    /**
     * Constructor of the frame with added layouts needed
     * @param hbox contains buttons for changing draughts version
     * @param gridPane contains board for playing
     */
    ApplicationFrame(HBox hbox, GridPane gridPane) {
        gridPane.relocate(0, 35);
        getChildren().addAll(gridPane,hbox);
    }
}