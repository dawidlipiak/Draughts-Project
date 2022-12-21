package org.example;


import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int nrOfPawns;
    private List<Pawn> pawns;

    // Is the player the first player
    private boolean firstplayer;

    private void initPawns(){
        pawns = new ArrayList<>();
    }

    /**
     * Constructor of a player that creates pawns for him
     * @param nrOfPawns number of pawns at the board
     */
    public Player(int nrOfPawns){
        Color color;
        this.nrOfPawns = nrOfPawns;

        if(firstplayer){
            color = Color.WHEAT;
        }
        else {
            color = Color.BROWN;
        }

        initPawns();
        for(int i = 0; i < nrOfPawns; i++){
            Pawn pawn = new Pawn(color);
            pawns.add(pawn);
        }
    }

    /**
     * Setting pawns position at the begging of the game regarding if that is the first player
     * @param nrOfPawns number of pawns on the board
     * @param board Gridpane type board
     */
    public void setPawnsStartPosition(int nrOfPawns, Board board) {
        if(firstplayer) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < board.getColumnCount(); j++) {

                }
            }
        }
        if(!firstplayer){
            for (int i = board.getRowCount()-2; i < board.getRowCount(); i++) {
                for (int j = 0; j < board.getColumnCount(); j++) {

                }
            }
        }
    }
}
