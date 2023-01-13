package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.Strategy.MovesStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representation in MVC pattern responsible for running the game and making changes passed by controller.
 */
public class Game {
    Canvas gameView;
    GameController gameController;
    private Pawn [][] pawnsBoard;
    private Pawn [][] oldPawnsBoard;
    private final int nrOfFields;
    private final int fieldSize;
    private Player currentPlayer;
    private int selectedRow, selectedCol;
    private final Label messagePROMT;
    private Movement[] legalMoves;
    private List <Movement> doneMoves;
    public final static int ACTIVE = 0;
    public final static int NONACTIVE = 1;
    private static int turnMade = ACTIVE;
    private MovesStrategy strategy;

    /**
     * * Constructor of a play board for draughtsw
     * @param gameView Canvas on which the game is showed to user
     * @param numberOfFields number of fields in one direction
     * @param boardSize size of the window
     * @param message Label on which there are showed to user information about the game
     * @param gameController controller from which we're using send method to send made moves
     */
    public Game(Canvas gameView,int numberOfFields, int boardSize, Label message, GameController gameController){
        this.gameView = gameView;
        this.nrOfFields = numberOfFields;
        this.fieldSize = boardSize/nrOfFields;
        pawnsBoard = new Pawn[nrOfFields][nrOfFields];
        oldPawnsBoard = new Pawn[nrOfFields][nrOfFields];
        this.messagePROMT = message;
        this.gameController = gameController;
        setPositions();
    }

    /**
     * Method to draw a draughtboard and to create framing for pawns.
     */
    public void drawBoard() {
        Pawn pawn;
        GraphicsContext g = gameView.getGraphicsContext2D();
        g.setFont(Font.font(40));

        // Draw the squares of the draughtboard and the pawns.
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    g.setFill(Color.PERU);
                }
                else {
                    g.setFill(Color.MOCCASIN);
                }
                g.fillRect(col*fieldSize,row*fieldSize, fieldSize, fieldSize);

                pawn = pawnsBoard[row][col];

                switch (pawn.getState()) {
                    case NORMAL:
                        if (pawn.getColor() == Color.WHITE) {
                            g.setFill(Color.WHITE);
                        } else {
                            g.setFill(Color.BLACK);
                        }
                        g.fillOval(col * fieldSize + 7.5, row * fieldSize + 7.5, 60, 60);
                        break;

                    case QUEEN:
                        if (pawn.getColor() == Color.WHITE) {
                            g.setFill(Color.WHITE);
                        } else {
                            g.setFill(Color.BLACK);
                        }
                        g.fillOval(col * fieldSize + 7.5, row * fieldSize + 7.5, 60, 60);
                        g.setFill(Color.RED);
                        g.fillText("K", 25+ col * fieldSize, 50 + row * fieldSize);
                        break;
                }
            }
        }
        //Add framing for the pawns to show possible moves.
        if(turnMade == NONACTIVE && legalMoves != null) {
            g.setStroke(Color.BROWN);
            g.setLineWidth(4);
            for (int i = 0; i < legalMoves.length; i++) {
                g.strokeRect(2 + legalMoves[i].getFromCol() * fieldSize, 2 + legalMoves[i].getFromRow() * fieldSize, fieldSize - 4, fieldSize - 4);
            }
            if (selectedRow >= 0) {
                g.setStroke(Color.CHOCOLATE);
                g.setLineWidth(4);
                g.strokeRect(2 + selectedCol * fieldSize, 2 + selectedRow * fieldSize, fieldSize - 4, fieldSize - 4);
                g.setStroke(Color.DARKGREEN);
                g.setLineWidth(4);
                for (int i = 0; i < legalMoves.length; i++) {
                    if (legalMoves[i].getFromCol() == selectedCol && legalMoves[i].getFromRow() == selectedRow) {
                        g.strokeRect(2 + legalMoves[i].getToCol() * fieldSize, 2 + legalMoves[i].getToRow() * fieldSize, fieldSize - 4, fieldSize - 4);
                    }
                }
            }
        }
    }

    /**
     * Method to set starting positions of the pawns.
     */
    public void setPositions() {
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
     * Method handling user's actions on the board.
     * @param row row which is clicked by user
     * @param col column which is clicked by a user
     */
    public void playerTurn(int row, int col) {
        // If the player clicked on one of the pawns that the player can move, mark this row and col as selected and return.
        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i].moveCheckerFrom(row, col)) {
                selectedRow = row;
                selectedCol = col;
                drawBoard();
                return;
            }
        }

        // If no pawn has been selected to be moved, the user must first select a pawn.
        if (selectedRow < 0) {
            setPROMPT("Naciśnij pionka którym chcesz ruszyć");
            return;
        }

        //If the user clicked on a field where the selected pawn can be legally moved, then make the move and return.
        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i].moveCheckerFrom(selectedRow, selectedCol) && legalMoves[i].moveCheckerTo(row, col)) {
                // SAVE PREVIOUS BOARD IN CASE OF A WRONG MOVE
                for(int tempRow = 0; tempRow < nrOfFields; tempRow++){
                    for(int tempCol = 0; tempCol < nrOfFields; tempCol++){
                        oldPawnsBoard[tempRow][tempCol] = pawnsBoard[tempRow][tempCol];
                    }
                }

                // Make a move
                pawnsBoard = legalMoves[i].makeMove(pawnsBoard);
                doneMoves.add(new Movement(legalMoves[i].getFromRow(), legalMoves[i].getFromCol(), legalMoves[i].getToRow(), legalMoves[i].getToCol()));

                if (legalMoves[i].isJump()) {
                    legalMoves = strategy.getLegalJumpsFrom(legalMoves[i].getToRow(), legalMoves[i].getToCol(), currentPlayer, pawnsBoard);

                    if (legalMoves != null) {
                        if (pawnsBoard[row][col].getColor() == Color.WHITE) {
                            setPROMPT("Musisz kontynuować skok.");
                        } else {
                            setPROMPT("Musisz kontynuować skok.");
                        }
                        drawBoard();
                        return;
                    }
                }
                // When user doesn't select a piece to move set selectedRow to -1
                selectedRow = -1;

                // if there are any done moves send them to the server
                String message;
                if (doneMoves.size() > 0) {
                    message = "";
                    for (int j = 0; j < doneMoves.size(); j++) {
                        message += doneMoves.size() + " move"
                                + " " + doneMoves.get(j).getFromRow()
                                + " " + doneMoves.get(j).getFromCol()
                                + " " + doneMoves.get(j).getToRow()
                                + " " + doneMoves.get(j).getToCol();
                        if((doneMoves.size() != 1) && (j != doneMoves.size()-1)){
                            message += " ";
                        }
                    }
                    if(isGameOver()){
                        message += " game over";
                    }
                    // If the server returns to the GameController the move was wrong return board to the state before the move
                    if(!gameController.send(message)){
                        pawnsBoard = oldPawnsBoard;
                        oldPawnsBoard = new Pawn[fieldSize][fieldSize];
                        doneMoves.clear();
                    }
                }

                return;
            }
        }
        setPROMPT("Naciśnij gdzie chcesz się ruszyć");
    }

    // Do on the player's board done moves from his opponent.
    public void makeReceivedMove(boolean finishedAllMoves, Movement move){
        pawnsBoard = move.makeMove(pawnsBoard);
        setTurnMade(NONACTIVE);
        legalMoves = strategy.getLegalMoves(currentPlayer,pawnsBoard);
        if(finishedAllMoves){
            doneMoves = new ArrayList<>();
        }
    }

    /**
     * Method returning player's board to previous state before his wrong move.
     */
    public void rewindBoard(){
        pawnsBoard = oldPawnsBoard;
    }

    /**
     * Get the status of a player's turn.
     * @return ACTIVE = 0 if a player has done some move or NONACTIVE = 1 if the move isn't done yet
     */
    public int getTurnMade() {
        return turnMade;
    }

    /**
     * Set the status of a player's turn.
     * @param turnState 0 if the turn is done, 1 if the turn isn't done yet
     */
    public void setTurnMade(int turnState) {
        turnMade = turnState;
    }

    /**
     * Get which player's turn is it.
     * @return player which now is the turn.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set which player's turn is it
     * @param player which turn now it will be
     */
    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }

    /**
     * Pre-game setup after initial data received from the server.
     * @param player which will begin the game
     */
    public void gameSetup(Player player){
        currentPlayer = player;
        legalMoves = strategy.getLegalMoves(currentPlayer, pawnsBoard);
        doneMoves = new ArrayList<>();
    }

    /**
     * Method setting prompter showing a message from the game on the window.
     * @param message message
     */
    public void setPROMPT(String message) {
        messagePROMT.setText(message);
        System.out.println(message);
    }

    /**
     * Set the chosen strategy for the game.
     * @param strategy which was selected
     */
    public void setStrategy(MovesStrategy strategy) {
        this.strategy = strategy;
    }

    private boolean isGameOver (){
        Movement [] opponentLegalMoves;
        if(currentPlayer.isFirstPlayer()){
            opponentLegalMoves = strategy.getLegalMoves(new Player(8, false),pawnsBoard);
        }
        else {
            opponentLegalMoves = strategy.getLegalMoves(new Player(8, true),pawnsBoard);
        }
        return (opponentLegalMoves == null);
    }

}