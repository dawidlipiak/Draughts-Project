package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The main class to start Draughts application.
 */
public class Draughts extends Application {
        /**
         * starting method
         * @param stage stage for application
         * @throws Exception
         */
        @Override
        public void start(Stage stage) throws Exception {
                int boardsize = 600;

                // Create the label that will show messages. Set font and color for text.
                Label messagePROMPT = new Label("Naciśnij \"Nowa gra\" aby zacząć.");
                messagePROMPT.setTextFill( Color.BLACK );
                messagePROMPT.setFont(Font.font(null, FontWeight.BOLD, 25));

                Pane frame = new Pane();

                //Create new game.
                GameController gameController = new GameController(boardsize, messagePROMPT,stage);
                gameController.setOnMousePressed(gameController::mousePressed);



                //Set content of the Pane in accurate places.
                Line line = new Line(0,71,boardsize,71);
                gameController.relocate(0,75);
                messagePROMPT.relocate(5,15 );

                line.setStrokeWidth(8);
                line.setStroke(Color.BROWN);

                //Add content to the Pane.
                frame.getChildren().addAll(gameController,messagePROMPT,line);
                frame.setBackground(new Background(new BackgroundFill(Color.PEACHPUFF,null,null)));

                // Create a scene.
                Scene scene = new Scene(frame,boardsize, boardsize+75, Color.MOCCASIN);
                // Set non-resizable window and title for window
                stage.setResizable(false);
                stage.setTitle("Warcaby");

                // Place in scene in the stage.
                stage.setScene(scene);


        }
}