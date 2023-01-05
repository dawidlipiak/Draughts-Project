package org.example;

import java.util.ArrayList;

public interface LegalMovesInterface {
    void addLegalJumps();
    ArrayList<Movement> getLegalMoves(Pawn pawn, Board board);
    boolean legalJump(Pawn pawn, int fromRow, int fromCol, int betweenRow, int betweenCol, int toRow, int toCol);
    boolean legalMove(Pawn pawn, int fromRow, int fromCol, int toRow, int toCol);
}
