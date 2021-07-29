import java.util.Scanner;
public class ConnectFour{
    public static void main(String[] args) {
        ConnectFourBoard myBoard = new ConnectFourBoard();
        Scanner my_scanner = new Scanner(System.in);
        int result = 0;
        int selection = -1;
        while (myBoard.checkWin() == 0){
            do{
                System.out.println("Player 1: Please select a tile: ");
                selection = my_scanner.nextInt() - 1;
            }while(!myBoard.addPiece(selection, 1));
            myBoard.outputBoard();
            if (myBoard.checkWin() != 0)
                break;
            do{
                System.out.println("Player 2: Please select a tile: ");
                selection = my_scanner.nextInt() - 1;
            }while(!myBoard.addPiece(selection, -1));
            myBoard.outputBoard();
        }
        result = myBoard.checkWin();
        if (result == 1)
            System.out.println("Player 1 Wins!!");
        else if(result == -1)
            System.out.println("Player 2 Wins!!");
        else
            System.out.println("Draw!!");
        my_scanner.close();
        return;
    }
}
class ConnectFourBoard{
    int[][] slots = new int[7][6];
    boolean[][][] check_again = new boolean[7][6][4];
    public ConnectFourBoard() {
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 6; j++){
                this.slots[i][j] = 0;
                for (int k = 0; k < 4; k++)
                    this.check_again[i][j][k] = true;
            }
        }
    }

    /**
     * Gets the first empty spot in the column
     * @param x The x row of the column
     * @return The y position of the first empty slot or -1 for full column
     */
    private int getFirstEmpty(int x){
        for (int j = 0; j < 6; j++){
            if (this.slots[x][j] == 0)
                return j;
        }
        return -1;
    }
    /**
     * Marks a tile on the board to a player
     * @param tile The tile to update
     * @param mark The player to mark the tile as
     * @return True if successful. Otherwise False
     */
    public boolean addPiece(int x, int mark){
        if (x < 0 || x > 6)
            return false;
        int y = this.getFirstEmpty(x);
        if (y == -1){
            return false;
        }
        this.slots[x][y] = mark;
        return true;
    }
    /**
     * Returns the winner if one exists.
     * @return 1 for player 1, -1 for player 2 and 0 for no winner yet, and -10 draw
     */
    public int checkWin(){
        int dud_tiles = 0; // How many tiles that cannot be won off of. If reaches 33, then it's over
        // Check tiles (0,0) through (6,2)
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 3; j++){
                int result = this.checkTileForWin(i, j);
                if (result == 1 || result == -1)
                    return result;
                else if(result == -10)
                    dud_tiles += 1;
            }
        }
        // Check tiles (0,3) through (3,5)
        for (int i = 0; i < 4; i++){
            for (int j = 3; j < 6; j++){
                int result = this.checkTileForWin(i, j);
                if (result == 1 || result == -1)
                    return result;
                else if(result == -10)
                    dud_tiles += 1;
            }
        }
        if (dud_tiles == 33)
            return -10;
        return 0;
    }

    /**
     * Checks a tile a particular way to see if a victory exists, if it could still be part of a
     * victory in the future. Or if it should not be checked again
     * @param x column of tile
     * @param y row of tile
     * @return 1 or -1 if there's a win. -10 if no win possible. 0 for undetermined
     */
    private int checkTileForWin(int x, int y){
        // Horizontal Check12
        if (x < 4) {
            if (this.check_again[x][y][0]) {
                if (this.slots[x][y] == this.slots[x + 1][y] &&
                        this.slots[x][y] == this.slots[x + 2][y] &&
                        this.slots[x][y] == this.slots[x + 3][y])
                    return this.slots[x][y];
                // Check if any slots in area are still empty
                outer_one:
                for (int i = 0; i < 4; i++) {
                    for (int j = i; j < 4; j++)
                        if (this.slots[x+i][y] != this.slots[x+j][y] && this.slots[x+i][y] != 0 && this.slots[x+j][y] != 0) {
                            this.check_again[x][y][0] = false;
                            break outer_one;
                        }
                }
            }
        }
        else
            this.check_again[x][y][0] = false;
        // Vertical Check
        if (y < 3){
            if (this.check_again[x][y][1]) {
                if (this.slots[x][y] == this.slots[x][y + 1] &&
                        this.slots[x][y] == this.slots[x][y + 2] &&
                        this.slots[x][y] == this.slots[x][y + 3])
                    return this.slots[x][y];
                // Check if any slots in area are still empty
                outer_two:
                for (int i = 0; i < 4; i++) {
                    for (int j = i; j < 4; j++)
                        if (this.slots[x][y + i] != this.slots[x][y + j] && this.slots[x][y + i] != 0 && this.slots[x][y + j] != 0) {
                            this.check_again[x][y][1] = false;
                            break outer_two;
                        }
                }
            }
        }
        else
            this.check_again[x][y][1] = false;
        // UpRight Check
        if (y < 3 && x < 4){
            if (this.check_again[x][y][2]) {
                if (this.slots[x][y] == this.slots[x + 1][y + 1] &&
                        this.slots[x][y] == this.slots[x + 2][y + 2] &&
                        this.slots[x][y] == this.slots[x + 3][y + 3])
                    return this.slots[x][y];
                // Check if any slots in area are still empty
                outer_three:
                for (int i = 0; i < 4; i++) {
                    for (int j = i; j < 4; j++)
                        if (this.slots[x+i][y + i] != this.slots[x+j][y + j] && this.slots[x+i][y + i] != 0 && this.slots[x+j][y + j] != 0) {
                            this.check_again[x][y][2] = false;
                            break outer_three;
                        }
                }
            }
        }
        else
            this.check_again[x][y][2] = false;
        // Up Left Check
        if (y < 3 && x > 2){
            if (this.check_again[x][y][3]) {
                if (this.slots[x][y] == this.slots[x - 1][y + 1] &&
                        this.slots[x][y] == this.slots[x - 2][y + 2] &&
                        this.slots[x][y] == this.slots[x - 3][y + 3])
                    return this.slots[x][y];
                // Check if any slots in area are still empty
                outer_four:
                for (int i = 0; i < 4; i++) {
                    for (int j = i; j < 4; j++)
                        if (this.slots[x-i][y + i] != this.slots[x-j][y + j] && this.slots[x-i][y + i] != 0 && this.slots[x-j][y + j] != 0) {
                            this.check_again[x][y][3] = false;
                            break outer_four;
                        }
                }
            }
        }
        else
            this.check_again[x][y][3] = false;
        // If we made it down here, then there is no winner this tile can see.
        // So instead return if there is no possible win on this tile.
        for (int k = 0; k < 4; k++)
            if (this.check_again[x][y][k] == true)
                return 0;
        return -10;
    }

    /**
     * Outputs the character for a tile
     * @param x tile column
     * @param y tile row
     * @return 'X' for player 1 piece, 'O' for player 2 piece, '.' for empty
     */
    private char slotChar(int x, int y){
        int slot_val = this.slots[x][y];
        if (slot_val == -1)
            return 'O';
        else if (slot_val == 1)
            return 'X';
        return '.';
    }
    /**
     * Outputs the board to the screen
     */
    public void outputBoard(){
        for (int j = 5; j > -1; j--){
            StringBuilder printline = new StringBuilder();
            for (int i = 0; i < 7; i++) {printline.append(this.slotChar(i,j));}
            System.out.println(printline);
            printline.delete(0,7);
        }
        return;
    }
}