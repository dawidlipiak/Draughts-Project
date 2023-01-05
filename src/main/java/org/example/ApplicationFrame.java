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
     * @param gridPane contains board for playing
     */
    ApplicationFrame(GridPane gridPane) {
        getChildren().addAll(gridPane);
    }
}