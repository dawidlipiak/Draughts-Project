package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Draughts extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create new board for draughts
        Board board = new Board();
        // Create a scene and place it in the stage
        Scene scene = new Scene(board.getBoard());
        primaryStage.setTitle("Warcaby");
        // Place in scene in the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
