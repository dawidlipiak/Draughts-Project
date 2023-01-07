package org.example.Server;

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
        int FIRST=1;
        int SECOND=2;
        int turn=FIRST;

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

                output1.println("1");
                output2.println("2");

                String line = "";
                do{
                    if (turn==SECOND) {
                        // Receiving from socket
                        line = input2.readLine();
                        //Writing out on the server
                        System.out.println(line);
                        //Sending to socket
                        output1.println(line);
                        turn=FIRST;
                    }
                    if (turn==FIRST) {
                        // Receiving from socket
                        line = input1.readLine();
                        //Writing out on the server
                        System.out.println(line);
                        //Sending to socket
                        output2.println(line);
                        turn=SECOND;
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
}
