import javafx.scene.paint.Color;
import org.example.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LegalMovesTest {
    private Pawn[][] pawnsBoard;
    int nrOfFields;
    private Player player;
    Movement[] legalMoves;
    LegalMoves legalMovesObj;

    /**
     * Set up board with no pawns, number of fields and player.
     */
    @Before
    public void setUp(){
        nrOfFields = 8;
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
     * Test for one black pawn beating up one white pawn.
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
    }

    /**
     * Test for one black pawn and two white in one line, so black has no moves.
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
     * Test for one black king separated from two white pawns. King can do only one move.
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
     * Test if a regular pawn can move back.
     */
    @Test
    public void legalMoveTestForMovesBack() {
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.BLACK);
        pawnsBoard[2][0].setState(PawnState.NORMAL);
        pawnsBoard[2][0].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        String result = "can not";
        if(legalMoves.length > 0) {
            result = " can ";
        }
        System.out.println("Test: legalMoveTestForMovesBack. Pawn" + result + "move back " );
    }

    /**
     * Test if a regular pawn can move back.
     */
    @Test
    public void legalMoveTestForKingsMovesBack() {
        pawnsBoard[1][1].setState(PawnState.KING);
        pawnsBoard[1][1].setColor(Color.BLACK);
        pawnsBoard[2][0].setState(PawnState.NORMAL);
        pawnsBoard[2][0].setColor(Color.WHITE);
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        pawnsBoard[3][3].setState(PawnState.NORMAL);
        pawnsBoard[3][3].setColor(Color.WHITE);
        legalMoves = legalMovesObj.getLegalMoves(player, pawnsBoard);
        String result = "can not";
        if(legalMoves.length > 0) {
            result = " can ";
        }
        System.out.println("Test: legalMoveTestForMovesBack. Pawn" + result + "move back " );
    }


}
