package org.example;

import javafx.scene.paint.Color;
import org.example.*;
import org.example.Strategy.GermanStrategy;
import org.example.Strategy.ItalianStrategy;
import org.example.Strategy.MovesStrategy;
import org.example.Strategy.SpanishStrategy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Class with server for draughts.
 */
public class DraughtsServer {
    /**
     * Main method to start a server.
     * @param args arguments
     */
    public static void main (String [] args) {
        int PORT = 4444;
        Player player, player1, player2 ;
        PrintWriter output;
        BufferedReader input;
        boolean moveCorrect;
        Player turn;
        Pawn[][] pawnsBoard;

        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT);

            while(true){
                Socket firstPlayer = serverSocket.accept();
                System.out.println("First client connected");

                //Download initialization from socket for player1
                InputStream inputF = firstPlayer.getInputStream();
                BufferedReader input1 = new BufferedReader(new InputStreamReader(inputF));

                //Sending initialization to socket for player1
                OutputStream outputF = firstPlayer.getOutputStream();
                PrintWriter output1 = new PrintWriter(outputF, true);

                output1.println("true");

                String version = input1.readLine();
                System.out.println(version);

                Socket secondPlayer = serverSocket.accept();
                System.out.println("Second client connected");

                //Download initialization from socket for player2
                InputStream inputS = secondPlayer.getInputStream();
                BufferedReader input2 = new BufferedReader(new InputStreamReader(inputS));

                //Sending initialization to socket for player2
                OutputStream outputS = secondPlayer.getOutputStream();
                PrintWriter output2 = new PrintWriter(outputS, true);

                output2.println("false");
                output2.println(version);

                int nrOfFields = Integer.parseInt(input1.readLine());
                pawnsBoard = new Pawn[nrOfFields][nrOfFields];
                player1 = new Player(nrOfFields, true);
                player2 = new Player(nrOfFields, false);
                turn = player1;
                setPawnsPositions(nrOfFields,pawnsBoard);

                MovesStrategy strategy = getStrategy(version);

                Movement [] legalMoves;
                String line = "";
                do{
                    if (turn==player1) {
                        input = input1;
                        output = output1;
                        player = player1;
                    }
                    else {
                        input = input2;
                        output = output2;
                        player = player2;
                    }
                    legalMoves = strategy.getLegalMoves(player,pawnsBoard);
                    moveCorrect = true;

                    // Receiving from socket
                    line = input.readLine();
                    //Writing out on the server
                    System.out.println(line);

                    String [] command = line.split(" ");
                    int nrOfWords = line.split("\\w+").length;

                    // Move command is 6 words "nrOfMoves move fromRow fromCol toRow toCol"
                    int nrOfMoves = Integer.parseInt(command[0]);

                    for(int i = 0; i < nrOfMoves; i++){
                        int fromRow = Integer.parseInt(command[2 + (i*6)]);
                        int fromCol = Integer.parseInt(command[3 + (i*6)]);
                        int toRow = Integer.parseInt(command[4 + (i*6)]);
                        int toCol = Integer.parseInt(command[5 + (i*6)]);
                        Movement move = new Movement(fromRow, fromCol, toRow, toCol);

                        if(!checkMoveLegality(legalMoves,move)){
                            moveCorrect = false;
                        }
                        else {
                            pawnsBoard = move.makeMove(pawnsBoard);
                            legalMoves = strategy.getLegalJumpsFrom(move.getToRow(),move.getToCol(),player,pawnsBoard);
                        }
                    }
                    if(moveCorrect){
                        output.println("Correct move");
                    }
                    else{
                        output.println("Wrong move");
                        continue;
                    }

                    //Sending to socket
                    if(turn == player1) {
                        output2.println(line);
                        System.out.println("wyslano do player2");
                        turn = player2;
                    }
                    else {
                        output1.println(line);
                        System.out.println("wyslano do player1");
                        turn = player1;
                    }
                    if((nrOfWords % 6)-2 == 0){
                        line = "Game over";
                    }
                }while (!line.equals("Game over"));
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if the move done by player is one of the legal moves for a player
     * @param legalMoves array containing all legal moves in this turn for a player
     * @param playerMove move which player has done
     * @return false - if the move was not legal, true - if the move was correct
     */
    private static boolean checkMoveLegality(Movement [] legalMoves, Movement playerMove){
        if(legalMoves == null){
            return false;
        }
        for (Movement legalMove : legalMoves) {
            if (legalMove.getFromCol() == playerMove.getFromCol()
                    && legalMove.getFromRow() == playerMove.getFromRow()
                    && legalMove.getFromCol() == playerMove.getFromCol()
                    && legalMove.getToRow() == playerMove.getToRow()
                    && legalMove.getToCol() == playerMove.getToCol()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to set starting positions of the pawns.
     */
    public static void setPawnsPositions(int nrOfFields, Pawn [][] pawnsBoard) {
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    if (row < 3) {
                        pawnsBoard[row][col] = new Pawn(Color.BLACK,row,col);;
                    }
                    else if (row > 4) {
                        pawnsBoard[row][col] = new Pawn(Color.WHITE,row,col);
                    }
                    else {
                        pawnsBoard[row][col] = new Pawn(Color.TRANSPARENT,row,col);;
                        pawnsBoard[row][col].setState(PawnState.EMPTY);
                    }
                }
                else {
                    pawnsBoard[row][col] = new Pawn(Color.TRANSPARENT,row,col);;
                    pawnsBoard[row][col].setState(PawnState.EMPTY);
                }
            }
        }
    }

    /**
     * Method changing the String of chosen version into concrete strategy
     * @param version String of chosen game version
     * @return strategy which implements MoveStrategy interface
     */
    private static MovesStrategy getStrategy (String version){
        if (version.equals("Włoska")) {
             return new ItalianStrategy(8);
        }
        if (version.equals("Niemiecka")) {
            return new GermanStrategy(8);
        }
        if (version.equals("Hiszpańska")) {
            return new SpanishStrategy(8);
        }
        return null;
    }
}
