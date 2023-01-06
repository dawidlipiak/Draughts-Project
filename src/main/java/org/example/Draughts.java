package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Draughts extends Application {


    @Override
    public void start(Stage stage) throws Exception {
//        try {
//            Socket socket = new Socket("localhost", 4444);
//
//            // Output from the server
//            BufferedReader out = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            // Input for the server
//            PrintWriter serverInput = new PrintWriter(socket.getOutputStream());

            int boardsize = 600;

            /* Create the label that will show messages. */
            Label message = new Label("Click \"New Game\" to begin.");
            message.setTextFill( Color.BLACK );
            message.setFont(Font.font(null, FontWeight.BOLD, 25));

            /* Create the buttons and the board.  The buttons MUST be
             * created first, since they are used in the CheckerBoard
             * constructor! */
            Button newGameButton = new Button("Nowa gra");
            Button resignButton = new Button("Poddaj się");
            newGameButton.setFont(Font.font(15));
            resignButton.setFont(Font.font(15));


            //Create new board for draughts
            Game game = new Game(8, boardsize, true);
            game.setOnMousePressed(game::mousePressed);
            Pane frame = new Pane();

            game.relocate(0,75);
            message.relocate(5,15 );
            newGameButton.relocate(400, 20);
            resignButton.relocate(500,20);

            frame.getChildren().addAll(game,message,newGameButton, resignButton);

            // Create a scene and place it in the stage
            Scene scene = new Scene(frame,boardsize, boardsize+75);
            // Set non resizable window
            stage.setResizable(false);
            stage.setTitle("Warcaby");

            // Place in scene in the stage
            stage.setScene(scene);

            //Game game = new Game(board,player);

            // MOŻLIWOŚĆ DODANIA STAGE'Y INNYCH WERSJI
            VersionChoice versionWindow = new VersionChoice(stage,stage,stage);

//            String serverOutput  = out.readLine();
//            if(serverOutput.equals("1")) {
            versionWindow.show();
            versionWindow.choosingVersion();

            //serverInput.println(versionWindow.getChosenVersion());
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
//
//        }
//        catch (IOException ex){
//
//        }
    }
}