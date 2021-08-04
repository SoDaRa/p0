import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectFourBoardTest {

    @Test
    void addPiece() {
        var my_board = new ConnectFourBoard(6,7,4);
        assertTrue(my_board.addPiece(1,1));
        assertFalse(my_board.addPiece(-1,-1));
        assertFalse(my_board.addPiece(20,-1));
        for(int i = 0; i < 6; i++){
            if (i % 2 == 0)
                assertTrue(my_board.addPiece(4,1));
            else
                assertTrue(my_board.addPiece(4,-1));
        }
        assertFalse(my_board.addPiece(4,1));
    }

    @Test
    void checkWin() {
        var my_board = new ConnectFourBoard(6,7,4);
        // Player 1 Win
        my_board.addPiece(1,1);
        my_board.addPiece(2,-1);
        my_board.addPiece(2,1);
        my_board.addPiece(3,-1);
        my_board.addPiece(2,1);
        my_board.addPiece(3,-1);
        my_board.addPiece(3,1);
        assertEquals(0,my_board.checkWin());
        my_board.addPiece(4,-1);
        my_board.addPiece(4,1);
        my_board.addPiece(4,-1);
        my_board.addPiece(4,1);
        assertEquals(2,my_board.checkWin());
        // Player 2 Win
        my_board = new ConnectFourBoard(6,7,4);
        for (int i = 0; i < 4; i++)
            my_board.addPiece(i, -1);
        assertEquals(-2, my_board.checkWin());
        // Draw Check
        my_board = new ConnectFourBoard(3,3,3);
        my_board.addPiece(0,1);
        my_board.addPiece(0,-1);
        my_board.addPiece(1,1);
        my_board.addPiece(2,-1);
        my_board.addPiece(2,1);
        my_board.addPiece(1,-1);
        my_board.addPiece(0,1);
        my_board.addPiece(1,-1);
        assertEquals(-10, my_board.checkWin());
    }
}