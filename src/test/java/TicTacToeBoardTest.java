import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TicTacToeBoardTest {
    @Test
    public void testMarkTile() {
        TicTacToeBoard myBoard = new TicTacToeBoard();
        assertTrue(myBoard.markTile(1, 1));
        assertFalse(myBoard.markTile(1, -1));
        assertFalse(myBoard.markTile(-1, 0));
        assertFalse(myBoard.markTile(20, 0));
    }
    @Test
    public void testCheckWin() {
        TicTacToeBoard myBoard = new TicTacToeBoard();
        myBoard.markTile(0, 1);
        myBoard.markTile(5, -1);
        assertEquals(0, myBoard.checkWin());
        myBoard.markTile(1, 1);
        myBoard.markTile(4, -1);
        assertEquals(0, myBoard.checkWin());
        myBoard.markTile(2, 1);
        assertEquals(1, myBoard.checkWin());
    }
}