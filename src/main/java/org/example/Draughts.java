package org.example;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Draughts extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        try {
            Socket socket = new Socket("localhost", 4444);

            // Output from the server
            BufferedReader out = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Input for the server
            PrintWriter serverInput = new PrintWriter(socket.getOutputStream());

            //Console console = System.console();

            ApplicationFrame frame;

            int boardsize = 600;

            // Pown circle radius
            double radius = 30;

            //Create new board for draughts
            Board board = new Board(8, boardsize);
            board.setPositionsToMoveOn(radius);

            // Create application frame
            frame = new ApplicationFrame(board);

            Player player1 = new Player(12, radius, true);
            Player player2 = new Player(12, radius, false);

            // Set start positions for pawns for a player
            player1.setPlayersPawnsStartPosition(board, frame);
            player2.setPlayersPawnsStartPosition(board, frame);

            // Create a scene and place it in the stage
            Scene scene = new Scene(frame, boardsize, boardsize);

            // Set non resizable window
            stage.setResizable(false);
            stage.setTitle("Warcaby");

            // Place in scene in the stage
            stage.setScene(scene);

            // MOŻLIWOŚĆ DODANIA STAGE'Y INNYCH WERSJI
            VersionChoice versionWindow = new VersionChoice(stage,stage,stage);

//            String serverOutput  = out.readLine();
//            if(serverOutput.equals("1")) {
                versionWindow.show();
                versionWindow.choosingVersion();
                serverInput.println(versionWindow.getChosenVersion());
//            }

            // TU MIAŁO BYĆ OTWIERANIE OKNA DLA DRUGIEGO GRACZA BEZ WYBORU WERSJI
//            else {
//                serverOutput = out.readLine();
//                switch (serverOutput){
//                    case "Włoska":
//                        // Trzeba ustawić stage dla wloskiej wersji
//                        versionWindow.setVersion(serverOutput,stage);
//                        break;
//                    case "Niemiecka":
//                        // Trzeba ustawić stage dla niemieckiej wersji
//                        versionWindow.setVersion(serverOutput,stage);
//                        break;
//                    case "Hiszpańska":
//                        // Trzeba ustawić stage dla hiszpanskiej wersji
//                        versionWindow.setVersion(serverOutput,stage);
//                        break;
//                }
//            }

        }
        catch (IOException ex){

        }
    }
}