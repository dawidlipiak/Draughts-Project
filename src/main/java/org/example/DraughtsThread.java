package org.example;

import java.io.*;
import java.net.Socket;

/**
 * Class where thread is created.
 */
public class DraughtsThread extends Thread{
    protected Socket socket;
    private final int clientId;;
    private String version;

    public DraughtsThread(Socket playerSocket, int clientId ){
        this.socket = playerSocket;
        this.clientId = clientId;
    }

    public void run (){
        try{
            //Input from socket
            InputStream input = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));

            //Output for socket
            OutputStream output = socket.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);

            out.println(clientId);
            version = in.readLine();
            //out.println(version);

        }catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
