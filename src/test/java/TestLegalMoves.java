import javafx.scene.paint.Color;
import org.example.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class TestLegalMoves {
    private Pawn[][] pawnsBoard;
    int nrOfFields;
    private Player player;
    Movement[] legalMoves;
    LegalMoves legalMovesObj;
    String version;

    /**
     * Set up board with no pawns, number of fields and player with Black pawns.
     */
    @Before
    public void setUp(){
        nrOfFields = 8;
        version = "Hiszpanska";
        pawnsBoard =  new Pawn[nrOfFields][nrOfFields];
        player = new Player(nrOfFields, false);
        legalMovesObj = new LegalMoves(nrOfFields);
        for(int i = 0; i < nrOfFields ; i++){
            for(int j = 0; j < nrOfFields ; j++){
                pawnsBoard[i][j] = new Pawn(Color.TRANSPARENT, i, j);
                pawnsBoard[i][j].setState(PawnState.EMPTY);
            }
        }
    }

    /**
     * 1. Test regular beat up. One black pawn beat up one white pawn.
     */
    @Test
    public void legalMoveTestForOnePawn() {
        pawnsBoard[0][0].setState(PawnState.NORMAL);
        pawnsBoard[0][0].setColor(Color.BLACK);
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertEquals(legalMoves[0].getToCol(), 2);
        Assert.assertEquals(legalMoves[0].getToRow(), 2);
        Assert.assertEquals(legalMoves.length, 1);
    }

    /**
     * 2. Test no moves for one pawn. One black pawn and two white in one line, so black has no moves.
     */
    @Test
    public void legalMoveTestNoMoves() {
        pawnsBoard[0][0].setState(PawnState.NORMAL);
        pawnsBoard[0][0].setColor(Color.BLACK);
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertNull(legalMoves);
    }

    /**
     * 3. Test no moves for many pawns. Black pawns in one row, and white pawns in next row, so that there is no moves.
     */
    @Test
    public void legalMoveTestTwoLinesOfPawns() {
        for(int i = 0; i < nrOfFields; i = i + 2) {
            pawnsBoard[6][i].setState(PawnState.NORMAL);
            pawnsBoard[6][i].setColor(Color.BLACK);
        }
        for(int j = 1; j < nrOfFields; j = j + 2) {
            pawnsBoard[7][j].setState(PawnState.NORMAL);
            pawnsBoard[7][j].setColor(Color.WHITE);
        }

        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertNull(legalMoves);
    }

    /**
     * 4. Test if beating up for king is correct. For one black king separated from two white pawns.
     * King can do only one move to 1,1 .
     */
    @Test
    public void legalMoveTestWithOneKing() {
        pawnsBoard[0][0].setState(PawnState.KING);
        pawnsBoard[0][0].setColor(Color.BLACK);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        pawnsBoard[3][3].setState(PawnState.NORMAL);
        pawnsBoard[3][3].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertEquals(legalMoves[0].getToCol(), 1);
        Assert.assertEquals(legalMoves[0].getToRow(), 1);
        Assert.assertEquals(legalMoves.length, 1);
    }

    /**
     * 4.2 Test if beating up for king is correct with two colors of pawns. King can do only one move.
     *     Two white pawns to block normal pawn.
     */
    @Test
    public void legalMoveTestWithOneKing2() {
        pawnsBoard[0][0].setState(PawnState.KING);
        pawnsBoard[0][0].setColor(Color.BLACK);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.BLACK);
        pawnsBoard[3][1].setState(PawnState.NORMAL);
        pawnsBoard[3][1].setColor(Color.WHITE);
        pawnsBoard[3][3].setState(PawnState.NORMAL);
        pawnsBoard[3][3].setColor(Color.WHITE);

        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertEquals(legalMoves[0].getToCol(), 1);
        Assert.assertEquals(legalMoves[0].getToRow(), 1);
        System.out.println("Legalne ruchy" + legalMoves.length);
        Assert.assertEquals(legalMoves.length, 1);
    }

    /**
     * 5. Test if a regular pawn can move back.
     */
    @Test
    public void legalMoveTestForMovesBack() {
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.BLACK);
        pawnsBoard[2][0].setState(PawnState.NORMAL);
        pawnsBoard[2][0].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        pawnsBoard[3][3].setState(PawnState.NORMAL);
        pawnsBoard[3][3].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertNull(legalMoves);
    }
    /**
     * 5.2 Test if a king pawn can move back.
     */
    @Test
    public void legalMoveTestForMovesBack2() {
        pawnsBoard[1][1].setState(PawnState.KING);
        pawnsBoard[1][1].setColor(Color.BLACK);
        pawnsBoard[2][0].setState(PawnState.NORMAL);
        pawnsBoard[2][0].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        pawnsBoard[3][3].setState(PawnState.NORMAL);
        pawnsBoard[3][3].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        Assert.assertNotEquals(legalMoves, null);
    }

    /**
     * 6. Test if beating back is correct in chosen version. Normal pawn case.
     */
    @Test
    public void legalMoveTestBeat() {
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.BLACK);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        // In Hiszpanska version we can not beat back, so we only have 2 moves forward.
        if(version == "Hiszpanska") {
            Assert.assertEquals(legalMoves.length, 2);
        }

    }



}
