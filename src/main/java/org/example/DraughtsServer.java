package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class with server for draughts.
 */
public class DraughtsServer {
    private static int nrOfClientsConnected;

    /**
     * main method to start server
     * @param args arguments
     */
    public static void main (String [] args) {
        int PORT = 4444;
        nrOfClientsConnected = 0;
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT);

            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New player connected");
                nrOfClientsConnected++;
                new DraughtsThread(socket,nrOfClientsConnected).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
