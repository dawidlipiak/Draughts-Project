package org.example;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Game controller class for handling actions from the user
 */
public class GameController extends Canvas implements Runnable{
    private final Game game;
    private final Stage gameStage;
    private final static int nrOfFields = 8;
    private final int fieldSize;
    private Player player;
    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private PrintWriter output;
    private BufferedReader input;
    private boolean soloMode;

    /**
     * Constructor of a game controller setting beginning variables
     * @param boardSize size of a playing board
     * @param message Label which will be prompter for messages for a user
     * @param gameStage stage on which the game is played
     */
    public GameController(int boardSize, Label message, Stage gameStage){
        super(boardSize,boardSize);
        this.fieldSize = boardSize/nrOfFields;
        this.gameStage = gameStage;
        listenSocket();

        game = new Game(this, nrOfFields, boardSize, message, this);
        receiveInitFromServer();
        Platform.runLater(game::drawBoard);
    }

    /**
     * Event handler for mouse being pressed by a user on the board.
     * @param evt mouse event
     */
    public void mousePressed(MouseEvent evt) {
        int col = (int)((evt.getX()) / fieldSize);
        int row = (int)((evt.getY()) / fieldSize);
        if (col >= 0 && col < nrOfFields && row >= 0 && row < nrOfFields) {
            if(soloMode) {
                if ((game.getTurnMade() == NONACTIVE) && (player.isFirstPlayer())) {
                    game.playerTurn(row, col);
                }
            }
            else{
                if (game.getTurnMade() == NONACTIVE) {
                    game.playerTurn(row, col);
                }
            }
        }
    }

    /**
     * Method to send a message to the server.
     * @param string message being sent
     */
    public boolean send(String string){
        output.println(string);

        try{
            // If the sent move was wrong server will return string "Wrong move"
            if(input.readLine().equals("Wrong move")) {
                Platform.runLater(()->game.setPROMPT("Zły ruch. Zrób nowy ruch"));
                game.rewindBoard();
                Platform.runLater(game::drawBoard);
                return false;
            }
            // If the move was correct proceed
            else {
                // If the player sent message contains "game over" set prompter message that he has win
                if(string.contains("game over")) {
                    Platform.runLater(() -> game.setPROMPT("Wygrałeś !"));
                }
                else {
                    Platform.runLater(() -> game.setPROMPT("Ruch przeciwnika"));
                }
                game.setTurnMade(ACTIVE);
                game.setCurrentPlayer(player);
                Platform.runLater(game::drawBoard);
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
        return false;
    }

    /**
     * Connect to the server socket and set output and input handlers
     */
    private void listenSocket() {
        try {
            Socket socket = new Socket("localhost", 4444);
            // Inicjalizacja wysylania do serwera
            output = new PrintWriter(socket.getOutputStream(), true);
            // Inicjalizacja odbierania z serwera
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }
    /**
     * Method assigning which player is first or second. Shows the version choosing window for the first player
     * and sends it to the server and parses it to the second player
     */
    private void receiveInitFromServer() {
        try {
            boolean playerAssign = Boolean.parseBoolean(input.readLine());
            System.out.println(playerAssign);
            Version version = new Version(gameStage);
            String versionName;

            // First player
            if (playerAssign) {
                version.showAndWait();
                versionName = version.getChosenVersion();
                soloMode = version.isSoloMode();
                game.setStrategy(version.getStrategy());
                output.println(versionName);
                output.println(nrOfFields);
                output.println(soloMode);
                Platform.runLater(()->game.setPROMPT("Mój ruch"));
                player = new Player(nrOfFields,true);
                game.setTurnMade(NONACTIVE);
                game.gameSetup(player);
            }
            // Second player
            else {
                versionName = input.readLine();
                soloMode = Boolean.parseBoolean(input.readLine());
                version.setVersion(versionName);
                game.setStrategy(version.getStrategy());
                gameStage.show();
                Platform.runLater(() -> game.setPROMPT("Ruch przeciwnika"));
                player = new Player(nrOfFields,false);
                game.gameSetup(player);
            }
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(1);
        }
    }

    /**
     * Method to receive commands from server.
     */
    private void receiveCommand(){
        try {
            // Reciving from server.
            String str = input.readLine();
            System.out.println(str);

            String [] command = str.split(" ");

            // Received command "nrOfMoves move fromRow fromCol toRow toCol"
            int nrOfWords = str.split("\\w+").length;
            System.out.println(nrOfWords + " slowa");

            int nrOfMoves = Integer.parseInt(command[0]);

            for (int i = 0; i < nrOfMoves; i++) {
                int fromRow = Integer.parseInt(command[2 + (i * 6)]);
                int fromCol = Integer.parseInt(command[3 + (i * 6)]);
                int toRow = Integer.parseInt(command[4 + (i * 6)]);
                int toCol = Integer.parseInt(command[5 + (i * 6)]);
                Movement move = new Movement(fromRow, fromCol, toRow, toCol);

                if (i + 1 == nrOfMoves) {
                    game.makeReceivedMove(true, move);
                } else {
                    game.makeReceivedMove(false, move);
                }

                Platform.runLater(game::drawBoard);
            }
            // 2-word message is "Game over"
            if(nrOfWords % 6 == 2) {
                Platform.runLater(() -> game.setPROMPT("Przegrałeś !"));
                return;
            }
            Platform.runLater(() -> game.setPROMPT("Mój ruch"));
            if(soloMode && !player.isFirstPlayer() && (game.getTurnMade() == NONACTIVE)){
                game.botMove();
            }
        }
        catch (IOException e) {
            System.out.println("Read failed"); System.exit(1);}
    }

    /**
     * Function called by a thread for making a concrete player receive command from opponent.
     * @param iPlayer player which turn has been now
     */
    private void threadFunction(Player iPlayer){
        while(true) {
            synchronized (this) {
                if (game.getCurrentPlayer() == iPlayer) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (game.getTurnMade() == ACTIVE){
                    System.out.println("Otrzymaj komende " + player.isFirstPlayer() );
                    receiveCommand();
                }
                notifyAll();
            }
        }
    }

    /**
     * Thread run function
     */
    @Override
    public void run() {
        threadFunction(player);
    }

}
