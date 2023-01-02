import javafx.scene.paint.Color;
import org.example.Player;
import org.junit.Assert;

public class PlayerTest {

    @org.junit.Test
    public void testSetPlayerColor() {

       Player player = new Player(8, true);
       Color color = player.getColor();
       Assert.assertEquals(color, Color.WHITE);

        Player player2 = new Player(8, false);
        Color color2 = player2.getColor();
        Assert.assertEquals(color2, Color.BLACK);
    }
    @org.junit.Test
    public void testConstructor() {
        try {
            new Player(-1, true);
            Assert.fail("Wyjątek nie został rzucony.");
        }
        catch (IllegalArgumentException exception) {
            Assert.assertEquals("Liczba pól musi być dodatnia", exception.getMessage());
        }
    }
}
