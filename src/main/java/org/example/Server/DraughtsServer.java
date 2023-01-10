package org.example.Server;

import javafx.scene.paint.Color;
import org.example.*;

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

                Socket secondPlayer = serverSocket.accept();
                System.out.println("Second client connected");

                //Download initialization from socket for player1
                InputStream inputF = firstPlayer.getInputStream();
                BufferedReader input1 = new BufferedReader(new InputStreamReader(inputF));

                //Download initialization from socket for player2
                InputStream inputS = secondPlayer.getInputStream();
                BufferedReader input2 = new BufferedReader(new InputStreamReader(inputS));

                //Sending initialization to socket for player1
                OutputStream outputF = firstPlayer.getOutputStream();
                PrintWriter output1 = new PrintWriter(outputF, true);

                //Sending initialization to socket for player2
                OutputStream outputS = secondPlayer.getOutputStream();
                PrintWriter output2 = new PrintWriter(outputS, true);

                output1.println("true");
                output2.println("false");

                int nrOfFields = Integer.parseInt(input1.readLine());
                pawnsBoard = new Pawn[nrOfFields][nrOfFields];
                player1 = new Player(nrOfFields, true);
                player2 = new Player(nrOfFields, false);
                turn = player1;
                setPawnsPositions(nrOfFields,pawnsBoard);

                LegalMoves legalMovesObj = new LegalMoves(nrOfFields);
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
                    legalMoves = legalMovesObj.getLegalMoves(player,pawnsBoard);
                    moveCorrect = true;

                    // Receiving from socket
                    line = input.readLine();
                    //Writing out on the server
                    System.out.println(line);

                    String [] command = line.split(" ");
                    int nrOfWords = line.split("\\w+").length;

                    // Move command is 6 words "nrOfMoves move fromRow fromCol toRow toCol"
                    if (nrOfWords % 6 == 0){
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
                                legalMoves = legalMovesObj.getLegalJumpsFrom(move.getToRow(),move.getToCol(),player,pawnsBoard);
                            }
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
                        turn = player2;
                    }
                    else {
                        output1.println(line);
                        turn = player1;
                    }
                }while (!line.equals("Game Over"));

                serverSocket.close();
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean checkMoveLegality(Movement [] legalMoves, Movement playerMove){
        if(legalMoves == null){
            return false;
        }
        for (Movement legalMove : legalMoves) {
            System.out.println("["+legalMove.getFromRow()+","+legalMove.getFromCol()+"]["+legalMove.getToRow()+","+legalMove.getToCol()+"]");
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
}
