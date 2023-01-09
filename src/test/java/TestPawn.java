import javafx.scene.paint.Color;
import org.example.Pawn;
import org.example.PawnState;
import org.example.Player;
import org.junit.Assert;

public class TestPawn {

    @org.junit.Test
    public void testPawnSetState() {
        //Create empty pawn
        Pawn pawn1 = new Pawn(Color.TRANSPARENT, 0, 0);
        pawn1.setState(PawnState.EMPTY);
        //Black normal pawn
        Pawn pawn2 = new Pawn(Color.BLACK, 1, 1);
        pawn2.setState(PawnState.NORMAL);
        //Set state of first pawn
        pawn1.setState(PawnState.NORMAL);
        Assert.assertEquals(pawn2.getState(), pawn1.getState());
    }

    @org.junit.Test
    public void testPawnSetColor() {
        //Create empty pawn
        Pawn pawn1 = new Pawn(Color.TRANSPARENT, 0, 0);
        //Create black pawn
        Pawn pawn2 = new Pawn(Color.BLACK, 1, 1);
        //Set color of first pawn
        pawn1.setColor(Color.BLACK);
        Assert.assertEquals(pawn2.getColor(), pawn1.getColor());
    }
}
