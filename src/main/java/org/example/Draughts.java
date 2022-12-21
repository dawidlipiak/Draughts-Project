package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Draughts extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationFrame frame;

        //Create new board for draughts
        Board board = new Board(8);

        // Create HBox with buttons that change versions of draughts
        ButtonsHBox hbox = new ButtonsHBox();

        // Create application frame
        frame = new ApplicationFrame(hbox, board);

        // Create a scene and place it in the stage
        Scene scene = new Scene(frame,600, 625);

        // Set non resizable window
        primaryStage.setResizable(false);
        primaryStage.setTitle("Warcaby");

        // Place in scene in the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
