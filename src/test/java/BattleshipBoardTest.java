import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleshipBoardTest {

    @Test
    void shoot() {
        var ship_1 = new Ship(1, 1,false, 4);
        var ship_2 = new Ship(3,4,true,3);
        var ship_3 = new Ship(6,7,false, 2);
        Ship[] ship_array = {ship_1, ship_2, ship_3};
        var my_board = new BattleshipBoard(8,8, ship_array);
        assertTrue(my_board.shoot(0,1));
        assertFalse(my_board.shoot(-1,0));
        assertFalse(my_board.shoot(0,-1));
        assertFalse(my_board.shoot(20,20));
        assertTrue(my_board.shoot(5,5));
        assertTrue(my_board.shoot(7,7));
        assertFalse(my_board.shoot(7,7));
    }

    @Test
    void checkWin() {
        var ship_1 = new Ship(1, 1,false, 4);
        var ship_2 = new Ship(3,4,true,3);
        var ship_3 = new Ship(6,7,false, 2);
        Ship[] ship_array = {ship_1, ship_2, ship_3};
        var my_board = new BattleshipBoard(8,8, ship_array);
        my_board.shoot(1,1);
        assertEquals(0, my_board.checkWin());
        my_board.shoot(2,1);
        my_board.shoot(3,1);
        my_board.shoot(4,1);
        assertEquals(0, my_board.checkWin());
        my_board.shoot(3,4);
        my_board.shoot(3,5);
        my_board.shoot(3,6);
        assertEquals(0, my_board.checkWin());
        my_board.shoot(6,7);
        my_board.shoot(7,7);
        assertEquals(1, my_board.checkWin());
        //Reset for fail case
        ship_1 = new Ship(1, 1,false, 4);
        ship_2 = new Ship(3,4,true,3);
        ship_3 = new Ship(6,7,false, 2);
        Ship[] ship_array_2 = {ship_1, ship_2, ship_3};
        my_board = new BattleshipBoard(8,8,ship_array_2);
        for (int i = 0; i < 3; i++)
            for(int j = 0; j < 8; j++)
                my_board.shoot(i, j);
        assertEquals(-1, my_board.checkWin());
    }
}
class ShipTest {
    @Test
    void checkHit() {
        var my_ship = new Ship(1,1,true, 4);
        assertEquals(0, my_ship.checkHit(0,0));
        assertEquals(1, my_ship.checkHit(1,2));
        assertEquals(0, my_ship.checkHit(2,2));
        assertEquals(0, my_ship.checkHit(7,2));
        assertEquals(1, my_ship.checkHit(1,3));
        assertEquals(1, my_ship.checkHit(1,4));
        assertEquals(2, my_ship.checkHit(1,1));
    }
}