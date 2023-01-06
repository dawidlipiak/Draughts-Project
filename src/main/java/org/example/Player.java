package org.example;


import javafx.scene.paint.Color;

public class Player {
    private final Pawn [][] playerPawns;
    private final int nrOfFields;

    /** Is the player the first player */
    private final boolean firstPlayer;
    private Color color;

    /**
     * Constructor of a player that creates pawns for him
     * @param nrOfFields number of pawns at the board
     */
    public Player(int nrOfFields, boolean firstPlayer){
        playerPawns = new Pawn[nrOfFields][nrOfFields];
        this.nrOfFields = nrOfFields;
        this.firstPlayer = firstPlayer;
        setPlayerColor();
    }

    private void setPlayerColor(){
        if(firstPlayer){
            color = Color.WHITE;
        }
        else {
            color = Color.BLACK;
        }
    }
    public void addPawns(Pawn pawn){
        int row = pawn.getRow();
        int col = pawn.getCol();
        playerPawns[row][col] = pawn;
    }
    public Color getColor(){
        return color;
    }
    public Pawn [][] getPlayerPawns() {
        return playerPawns;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}