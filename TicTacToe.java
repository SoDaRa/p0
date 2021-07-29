import java.util.Scanner;
public class TicTacToe{
    public static void main(String[] args) {
        TicTacToeBoard myBoard = new TicTacToeBoard();
        Scanner my_scanner = new Scanner(System.in);
        int result = 0;
        int selection = -1;
        TicTacToeBoard.learnBoard();
        while (myBoard.checkWin() == 0){
            while(!myBoard.markTile(selection, 1)){
                System.out.println("Player 1: Please select a tile: ");
                selection = my_scanner.nextInt() - 1;
            }
            myBoard.outputBoard();
            if (myBoard.checkWin() != 0)
                break;
            while(!myBoard.markTile(selection, -1)){
                System.out.println("Player 2: Please select a tile: ");
                selection = my_scanner.nextInt() - 1;
            }
            myBoard.outputBoard();
        }
        result = myBoard.checkWin();
        if (result == 1)
            System.out.println("Player 1 Wins!!");
        else if (result == -1)
            System.out.println("Player 2 Wins!!");
        else
            System.out.println("Draw!!");
        my_scanner.close();
        return;
    }

}
class TicTacToeBoard{
    int[] tiles = {0,0,0,0,0,0,0,0,0};
    private boolean checkTileEmpty(int tile){
        return this.tiles[tile] == 0;
    }
    /**
     * Marks a tile on the board to a player
     * @param tile The tile to update
     * @param mark The player to mark the tile as
     * @return True if successful. Otherwise False
     */
    public boolean markTile(int tile, int mark){
        if (tile < 0 || tile > 8)
            return false;
        if (!this.checkTileEmpty(tile)){            
            return false;
        }
        this.tiles[tile] = mark;
        return true;
    }
    /**
     * Returns the winner if one exists. 
     * @return 1 for X, -1 for O, 0 for no winner yet and -10 for draw
     */
    public int checkWin(){
        for (int i = 0; i < 8; i=i+3){
            if (this.tiles[i] != 0 && this.tiles[i] == this.tiles[i+1] && this.tiles[i] == this.tiles[i+2])
                return this.tiles[i];
        }
        for (int i = 0; i < 3; i++){
            if (this.tiles[i] != 0 && this.tiles[i] == this.tiles[i+3] && this.tiles[i] == this.tiles[i+6])
                return this.tiles[i];
        }
        if (this.tiles[0] != 0 && this.tiles[0] == this.tiles[4] && this.tiles[0] == this.tiles[8])
            return this.tiles[0];
        if (this.tiles[2] != 0 && this.tiles[2] == this.tiles[4] && this.tiles[2] == this.tiles[6])
            return this.tiles[2];
        int marked_count = 0;
        for (int i = 0; i < 9; i++)
            if (this.tiles[i] != 0)
                marked_count += 1;
        if (marked_count == 9)
            return -10;
        return 0;
    }
    /**
     * Outputs a version of the board with each tile labeled
     */
    public static void learnBoard(){
        System.out.println("1|2|3");
        System.out.println("-----");
        System.out.println("4|5|6");
        System.out.println("-----");
        System.out.println("7|8|9");
    }
    /**
     * Outputs the board to the screen
     */
    public void outputBoard(){
        StringBuilder printline = new StringBuilder();
        for (int i = 0; i < 3; i++){
            printline.append(this.getTileMark(i));
            if (i<2)
                printline.append('|');
        }
        System.out.println(printline);
        System.out.println("-----");
        printline.delete(0,5);
        for(int i = 3; i < 6; i++){
            printline.append(this.getTileMark(i));
            if (i<5)
                printline.append('|');
        }
        System.out.println(printline);
        System.out.println("-----");
        printline.delete(0,5);
        for(int i = 6; i < 9; i++){
            printline.append(this.getTileMark(i));
            if (i<8)
                printline.append('|');
        }
        System.out.println(printline);
        return;
    }
    /**
     * Gets the character to use to represent a tile on the board
     * @param tile The tile to get the character of
     * @return 'X' for player 1. 'O' for player 2. ' ' Otherwise
     */
    private char getTileMark(int tile){
        if (this.tiles[tile] == 1)
            return 'X';
        else if (this.tiles[tile] == -1)
            return 'O';
        return ' ';
    }
}