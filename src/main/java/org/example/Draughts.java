package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Draughts extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationFrame frame;

        int boardsize = 600;

        // Pown circle radius
        double radius = 30;

        //Create new board for draughts
        Board board = new Board(8, boardsize);
        board.setPositionsToMoveOn(radius);

        // Create HBox with buttons that change versions of draughts
        ButtonsHBox hbox = new ButtonsHBox();

        // Create application frame
        frame = new ApplicationFrame(hbox, board);

        // Create players
        Player player1 = new Player(12,radius,true);
        Player player2 = new Player(12,radius, false);

        // Set start positions for pawns for every player
        player1.setPlayersPawnsStartPosition(board,frame);
        player2.setPlayersPawnsStartPosition(board,frame);

        // Create a scene and place it in the stage
        Scene scene = new Scene(frame,boardsize, boardsize+35);

        // Set non resizable window
        primaryStage.setResizable(false);
        primaryStage.setTitle("Warcaby");

        // Place in scene in the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}