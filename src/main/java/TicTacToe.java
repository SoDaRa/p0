import java.util.Scanner;
public class TicTacToe{
    public static void main(String[] args) {
        TicTacToeBoard my_board = new TicTacToeBoard();
        Scanner my_scanner = new Scanner(System.in);
        int result;
        int selection;
        TicTacToeBoard.learnBoard();
        // Main loop
        while (my_board.checkWin() == 0){
            // Player 1 Turn
              // Request input
            System.out.println("Player 1: Please select an open tile: ");
              // Parse for input
            while (my_scanner.hasNext()) {
                // Attempt to get an int
                if (my_scanner.hasNextInt()) {
                    selection = my_scanner.nextInt() - 1;
                    if (my_board.markTile(selection, 1)) {// If we can claim selection
                        my_scanner.nextLine(); // Discard any malicious input
                        break;
                    }
                    else
                        System.out.println("Player 1: Please enter a valid tile");
                }
                // Throw out garbage
                else
                    my_scanner.next();
            }
            my_board.outputBoard();
            if (my_board.checkWin() != 0)
                break;
            // Player 2 Turn
            System.out.println("Player 2: Please select an open tile: ");
            while (my_scanner.hasNext()) {
                if (my_scanner.hasNextInt()) {
                    selection = my_scanner.nextInt() - 1;
                    if (my_board.markTile(selection, -1)) {
                        my_scanner.nextLine(); // Discard any malicious input
                        break;
                    }
                    else
                        System.out.println("Player 2: Please enter a valid tile");
                }
                else
                    my_scanner.next();
            }
            my_board.outputBoard();
        }
        result = my_board.checkWin();
        if (result == 1)
            System.out.println("Player 1 Wins!!");
        else if (result == -1)
            System.out.println("Player 2 Wins!!");
        else
            System.out.println("Draw!!");
    }

}
class TicTacToeBoard{
    private final int[] tiles = {0,0,0,0,0,0,0,0,0};

    /**
     * Shorthand for checking if a tile is already marked or not
     * @param tile The tile to check
     * @return True if player is using that spot. False otherwise
     */
    private boolean checkTileUsed(int tile){
        return this.tiles[tile] != 0;
    }

    /**
     * Marks a tile on the board to a player
     * @param tile The tile to update
     * @param mark The player to mark the tile as
     * @return True if successful. Otherwise False
     */
    public boolean markTile(int tile, int mark){
        if (tile < 0 || tile > 8 || this.checkTileUsed(tile))
            return false;
        this.tiles[tile] = mark;
        return true;
    }

    /**
     * Returns the winner if one exists. 
     * @return 1 for X, -1 for O, 0 for no winner yet and -10 for draw
     */
    public int checkWin(){
        // Vertical check
        for (int i = 0; i < 8; i=i+3){
            if (this.checkTileUsed(i) && this.tiles[i] == this.tiles[i+1] && this.tiles[i] == this.tiles[i+2])
                return this.tiles[i];
        }
        // Horizontal Check
        for (int i = 0; i < 3; i++){
            if (this.checkTileUsed(i) && this.tiles[i] == this.tiles[i+3] && this.tiles[i] == this.tiles[i+6])
                return this.tiles[i];
        }
        // Diagonals
        if (this.checkTileUsed(0) && this.tiles[0] == this.tiles[4] && this.tiles[0] == this.tiles[8])
            return this.tiles[0];
        if (this.checkTileUsed(2) && this.tiles[2] == this.tiles[4] && this.tiles[2] == this.tiles[6])
            return this.tiles[2];
        // Draw detection
        int marked_count = 0;
        for (int i = 0; i < 9; i++)
            if (this.checkTileUsed(i))
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
        StringBuilder print_line = new StringBuilder();
        for (int i = 0; i < 3; i++){
            print_line.append(this.getTileMark(i));
            if (i<2)
                print_line.append('|');
        }
        System.out.println(print_line);
        System.out.println("-----");
        print_line.delete(0,5);
        for(int i = 3; i < 6; i++){
            print_line.append(this.getTileMark(i));
            if (i<5)
                print_line.append('|');
        }
        System.out.println(print_line);
        System.out.println("-----");
        print_line.delete(0,5);
        for(int i = 6; i < 9; i++){
            print_line.append(this.getTileMark(i));
            if (i<8)
                print_line.append('|');
        }
        System.out.println(print_line);
    }

    /**
     * Gets the character to use to represent a tile on the board
     * @param tile The tile to get the character of
     * @return 'X' for player 1. 'O' for player 2. ' ' Otherwise
     */
    private char getTileMark(int tile){
        switch(this.tiles[tile]){
            case 1:
                return 'X';
            case -1:
                return 'O';
        }
        return ' ';
    }
}