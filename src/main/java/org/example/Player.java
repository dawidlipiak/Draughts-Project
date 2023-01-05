package org.example;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player {
    /** List of pawns for a certain player */
    private List<Pawn> pawns;

    /** Is the player the first player */
    private final boolean firstPlayer;

    /**
     * Constructor of a player that creates pawns for him
     * @param nrOfPawns number of pawns at the board
     */
    public Player(int nrOfPawns, double radius, boolean firstPlayer){
        Color color;
        this.firstPlayer = firstPlayer;
        if(firstPlayer){
            color = Color.WHITE;
        }
        else {
            color = Color.BLACK;
        }

        initPawns();
        for(int i = 0; i < nrOfPawns; i++){
            Pawn pawn = new Pawn(color, radius);
            pawns.add(pawn);
        }
    }

    /**
     * Creating list of pawns
     */
    private void initPawns(){
        pawns = new ArrayList<>();
    }

    /**
     * Setting pawns position at the begging of the game regarding if that is the first player
     * @param board Gridpane type board
     * @param frame Pane layout for adding pawns to it
     */
    public void setPlayersPawnsStartPosition( Board board, ApplicationFrame frame) {
        int counter = 0;
        if(firstPlayer) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < board.getColumnCount()/2 ; j++) {
                    pawns.get(counter).setPosition(board.getXPosition(i,j),board.getYPosition(i,j));
                    frame.getChildren().add(pawns.get(counter));
                    counter++;
                }
            }
        }
        if(!firstPlayer){
            for (int i = board.getRowCount()-3; i < board.getRowCount() ; i++) {
                for (int j = 0; j < board.getColumnCount()/2 ; j++) {
                    pawns.get(counter).setPosition(board.getXPosition(i,j),board.getYPosition(i,j));
                    frame.getChildren().add(pawns.get(counter));
                    counter++;
                }
            }
        }
    }
}