import javafx.scene.paint.Color;
import org.example.Pawn;
import org.example.PawnState;
import org.example.Movement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMovement {
    int nrOfFields;
    private Pawn[][] pawnsBoard;
    private Pawn[][] pawnsBoard2;

    @Before
    public void setUp(){
        nrOfFields = 8;
        pawnsBoard =  new Pawn[nrOfFields][nrOfFields];
        pawnsBoard2 =  new Pawn[nrOfFields][nrOfFields];
        for(int i = 0; i < nrOfFields ; i++) {
            for(int j = 0; j < nrOfFields ; j++) {
                pawnsBoard[i][j] = new Pawn(Color.TRANSPARENT, i, j);
                pawnsBoard[i][j].setState(PawnState.EMPTY);
                pawnsBoard2[i][j] = new Pawn(Color.TRANSPARENT, i, j);
                pawnsBoard2[i][j].setState(PawnState.EMPTY);
            }
        }
        pawnsBoard[1][1].setState(PawnState.NORMAL);
        pawnsBoard[1][1].setColor(Color.BLACK);
    }

    /**
     * Test of method make move from class Movement.
     */
    @Test
    public void makeMove() {
        pawnsBoard2[2][2].setState(PawnState.NORMAL);
        pawnsBoard2[2][2].setColor(Color.BLACK);
        Movement movement = new Movement(1,1,2,2);
        movement.makeMove(pawnsBoard);
        for(int i = 0; i < nrOfFields ; i++) {
            for(int j = 0; j < nrOfFields ; j++) {
                Assert.assertEquals(pawnsBoard[i][j].getColor(), pawnsBoard2[i][j].getColor());
                Assert.assertEquals(pawnsBoard[i][j].getState(), pawnsBoard2[i][j].getState());
            }
        }
    }

    /**
     * Test of method is jump for class Movement.
     */
    @Test
    public void makeJump() {
        pawnsBoard[2][2].setState(PawnState.NORMAL);
        pawnsBoard[2][2].setColor(Color.WHITE);
        Movement movement = new Movement(1,1,3,3);
        movement.makeMove(pawnsBoard);
        boolean isJump = movement.isJump();
        Assert.assertTrue(isJump);
    }
}
