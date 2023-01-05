package org.example;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.awt.*;

public class Board extends Canvas {
    private final Pawn [][] board;
    private final int nrOfFields;
    private final int boardSize;
    private final int fieldSize;

    /**
     * Constructor of a play board for draughts
     * @param numberOfFields number of Fields in a row and column
     * @param boardSize size of a window
     */
    public Board(int numberOfFields,int boardSize, Pawn [][] pawnBoard){
        super(boardSize,boardSize);
        this.boardSize = boardSize;
        this.nrOfFields = numberOfFields;
        this.fieldSize = boardSize/nrOfFields;
        this.board = pawnBoard;
        drawBoard();
    }

    public void drawBoard() {
        Pawn pawn;
        GraphicsContext g = getGraphicsContext2D();
        g.setFont(Font.font(40));

        /* Draw the squares of the checkerboard and the checkers. */
        for (int row = 0; row < nrOfFields; row++) {
            for (int col = 0; col < nrOfFields; col++) {
                if ( row % 2 == col % 2 ) {
                    g.setFill(Color.PERU);
                }
                else {
                    g.setFill(Color.MOCCASIN);
                }
                g.fillRect(col*fieldSize,row*fieldSize, fieldSize, fieldSize);

                pawn = board[row][col];

                switch (pawn.getState()) {
                    case NORMAL:
                        if (pawn.getColor() == Color.WHITE) {
                            g.setFill(Color.WHITE);
                        } else {
                            g.setFill(Color.BLACK);
                        }
                        g.fillOval(col * fieldSize + 7.5, row * fieldSize + 7.5, 60, 60);
                        break;

                    case KING:
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
    }
    public Pawn [][] getBoard() {
        return board;
    }

    public int getNrOfFields() {
        return nrOfFields;
    }
}