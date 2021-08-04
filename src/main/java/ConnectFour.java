import java.util.Scanner;
public class ConnectFour{
    public static void main(String[] args) {
        Scanner my_scanner = new Scanner(System.in);
        int result = 0;
        int selection;
        String input;
        var rows = 6;
        var columns = 7;
        var connect_n = 4;
        System.out.println("Would you like custom settings? (Y/N)");
        input = my_scanner.nextLine();
        if (input.equals("Y")) {
            do {
                System.out.println("How many tiles do you want to connect to win? (Default 4)");
                if (my_scanner.hasNextInt())
                    connect_n = my_scanner.nextInt();
                else
                    connect_n = 4;
                my_scanner.nextLine();
                if (connect_n <= 2)
                    System.out.println("Connect must be > 2.");
            } while(connect_n <= 2);
            do {
                System.out.println("How many rows do you want? (Default 6)");
                if (my_scanner.hasNextInt())
                    rows = my_scanner.nextInt();
                else
                    rows = 6;
                my_scanner.nextLine();
                if (rows < connect_n)
                    System.out.println("Rows must be >= connection.");
            }while(rows < connect_n);
            do {
                System.out.println("How many columns do you want? (Default 7)");
                if (my_scanner.hasNextInt())
                    columns = my_scanner.nextInt();
                else
                    columns = 7;
                my_scanner.nextLine();
                if (columns < connect_n)
                    System.out.println("Columns must be >= connection.");
            } while(columns < connect_n);
        }

        // Create Board
        ConnectFourBoard my_board = new ConnectFourBoard(rows, columns, connect_n);
        my_board.learnBoard();
        System.out.println();

        // Main Loop
        while (result == 0){
            System.out.println("Player 1: Please select a column: ");
            while(my_scanner.hasNext()){
                if (my_scanner.hasNextInt()) {
                    selection = my_scanner.nextInt() - 1;
                    if (my_board.addPiece(selection, 1)) {// If we can claim selection
                        my_scanner.nextLine(); // Discard any malicious input
                        break;
                    }
                    System.out.println("Player 1: Please select a valid column: ");
                }
                else {
                    my_scanner.next();
                    System.out.println("Player 1: Please select a column: ");
                }
            }
            result = my_board.checkWin(); // Updates board first if there's a win.
            my_board.outputBoard();
            if (result != 0)
                break;
            System.out.println("Player 2: Please select a column: ");
            while(my_scanner.hasNext()){

                if (my_scanner.hasNextInt()) {
                    selection = my_scanner.nextInt() - 1;
                    if (my_board.addPiece(selection, -1)) {// If we can claim selection
                        my_scanner.nextLine(); // Discard any malicious input
                        break;
                    }
                    System.out.println("Player 2: Please select a valid column: ");
                }
                else {
                    my_scanner.next();
                    System.out.println("Player 2: Please select a column: ");
                }
            }
            result = my_board.checkWin();
            my_board.outputBoard();
        }
        if (result == 2)
            System.out.println("Player 1 Wins!!");
        else if(result == -2)
            System.out.println("Player 2 Wins!!");
        else
            System.out.println("Draw!!");
    }
}

class ConnectFourBoard{
    private final int rows;
    private final int columns;
    private final int[][] slots;
    private final boolean[][][] check_again;
    private final int connect_n;

    /**
     * Initializes the Board
     * @param rows The Y height of the board
     * @param columns The X length of the board
     * @param connect_n How long a connection can be. Should be <= min(rows, columns)
     */
    public ConnectFourBoard(int rows, int columns, int connect_n) {
        this.rows = rows;
        this.columns = columns;
        this.slots = new int[columns][rows];
        this.check_again = new boolean[columns][rows][4];
        this.connect_n = connect_n;
        for (int i = 0; i < columns; i++){
            for (int j = 0; j < rows; j++){
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
        for (int j = 0; j < this.rows; j++){
            if (this.slots[x][j] == 0)
                return j;
        }
        return -1;
    }

    /**
     * Marks a tile on the board to a player
     * @param x The column to update
     * @param mark The player to mark the tile as. 1 for player 1. -1 for player 2
     * @return True if successful. Otherwise False
     */
    public boolean addPiece(int x, int mark){
        // Prevent illegal access
        if (x < 0 || x >= this.columns)
            return false;
        int y = this.getFirstEmpty(x);
        // If it can't find a spot, report it
        if (y == -1){
            return false;
        }
        this.slots[x][y] = mark;
        return true;
    }

    /**
     * Returns the winner if one exists.
     * @return 2 for player 1, -2 for player 2 and 0 for no winner yet, and -10 draw
     * This could likely be optimized by only checking slots surrounding the last entered
     * column. But then draw detection would likely have to be its own function. And likely
     * best to just run both together for now.
     */
    public int checkWin(){
        int count = 0;     // Counts how many tiles are checked
        int dud_tiles = 0; // How many tiles that cannot be won off of. If equal to count at end, then it's over
        // Check tiles below max vertical connection
        for (int i = 0; i < this.columns; i++){
            for (int j = 0; j < (this.rows - this.connect_n + 1); j++){
                int result = this.checkTileForWin(i, j);
                count += 1;
                if (result == 2 || result == -2)
                    return result;
                else if(result == -10)
                    dud_tiles += 1;
            }
        }
        // Check tiles above max vertical connection and left of max horizontal connection
        for (int i = 0; i < (this.columns - this.connect_n + 1); i++){
            for (int j = this.rows - this.connect_n + 1; j < this.rows; j++){
                int result = this.checkTileForWin(i, j);
                count += 1;
                if (result == 2 || result == -2)
                    return result;
                else if(result == -10)
                    dud_tiles += 1;
            }
        }
        if (dud_tiles == count)
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
        var horizontal_possible = (x <= (this.columns - this.connect_n));
        var vertical_possible = (y <= (this.rows - this.connect_n));
        var upright_possible = (vertical_possible && horizontal_possible);
        var upleft_possible = (vertical_possible && (x >= this.connect_n - 1));
        // Horizontal Check
        if ( this.check_again[x][y][0] && horizontal_possible ) {
            int win_count = 0; // Counts how many checks succeed
            var has_p1 = false;// Whether player 1 has a tile in this check
            var has_p2 = false;// Whether player 2 has a tile in this check
            for (int i = 0; i < this.connect_n; i++) {
                if (this.slots[x][y] == this.slots[x+i][y] && this.slots[x][y] != 0)
                    win_count += 1;
                if (this.slots[x+i][y] == 1)
                    has_p1 = true;
                else if(this.slots[x+i][y] == -1)
                    has_p2 = true;
            }
            if (win_count == this.connect_n){
                // Since we're winning, we're going to mark the board which connection won it before returning
                // If Player 1 winning, we'll mark 2, otherwise we'll mark -2
                var mark = 2;
                if (this.slots[x][y] == -1)
                    mark = -2;
                for (int i = 0; i < this.connect_n; i++){
                    this.slots[x+i][y] = mark;
                }
                return this.slots[x][y];
            }
            // If p1 and p2 both in this check, then no possible win here.
            if (has_p1 && has_p2)
                this.check_again[x][y][0] = false;
        }
        else if ( this.check_again[x][y][0] && !horizontal_possible )
            this.check_again[x][y][0] = false;

        // Vertical Check
        if (this.check_again[x][y][1] && vertical_possible ){
            int win_count = 0;
            var has_p1 = false;
            var has_p2 = false;
            for (int j = 0; j < this.connect_n; j++) {
                if (this.slots[x][y] == this.slots[x][y+j] && this.slots[x][y] != 0)
                    win_count += 1;

                if (this.slots[x][y+j] == 1)
                    has_p1 = true;
                else if(this.slots[x][y+j] == -1)
                    has_p2 = true;
                // Since there can't be anymore vertically
                else if (this.slots[x][y] == 0)
                    break;
            }
            if (win_count == this.connect_n){
                // Since we're winning, we're going to mark the board which connection won it before returning
                // If Player 1 winning, we'll mark 2, otherwise we'll mark -2
                var mark = 2;
                if (this.slots[x][y] == -1)
                    mark = -2;
                for (int j = 0; j < this.connect_n; j++){
                    this.slots[x][y+j] = mark;
                }
                return this.slots[x][y];
            }
            if (has_p1 && has_p2)
                this.check_again[x][y][1] = false;
        }
        else if ( this.check_again[x][y][1] && !vertical_possible )
            this.check_again[x][y][1] = false;

        // UpRight Check
        if (this.check_again[x][y][2] && upright_possible ){
            int win_count = 0;
            var has_p1 = false;
            var has_p2 = false;
            for (int k = 0; k < this.connect_n; k++) {
                if (this.slots[x][y] == this.slots[x+k][y+k] && this.slots[x][y] != 0)
                    win_count += 1;
                if (this.slots[x+k][y+k] == 1)
                    has_p1 = true;
                else if(this.slots[x+k][y+k] == -1)
                    has_p2 = true;
            }
            if (win_count == this.connect_n){
                // Since we're winning, we're going to mark the board which connection won it before returning
                // If Player 1 winning, we'll mark 2, otherwise we'll mark -2
                var mark = 2;
                if (this.slots[x][y] == -1)
                    mark = -2;
                for (int k = 0; k < this.connect_n; k++){
                    this.slots[x+k][y+k] = mark;
                }
                return this.slots[x][y];
            }
            if (has_p1 && has_p2)
                this.check_again[x][y][2] = false;
        }
        else if (this.check_again[x][y][2] && !upright_possible )
            this.check_again[x][y][2] = false;

        // Up Left Check
        if (this.check_again[x][y][3] && upleft_possible){
            int win_count = 0;
            var has_p1 = false;
            var has_p2 = false;
            for (int k = 0; k < this.connect_n; k++) {
                if (this.slots[x][y] == this.slots[x-k][y+k] && this.slots[x][y] != 0)
                    win_count += 1;
                if (this.slots[x-k][y+k] == 1)
                    has_p1 = true;
                else if(this.slots[x-k][y+k] == -1)
                    has_p2 = true;
            }
            if (win_count == this.connect_n){
                // Since we're winning, we're going to mark the board which connection won it before returning
                // If Player 1 winning, we'll mark 2, otherwise we'll mark -2
                var mark = 2;
                if (this.slots[x][y] == -1)
                    mark = -2;
                for (int k = 0; k < this.connect_n; k++){
                    this.slots[x-k][y+k] = mark;
                }
                return this.slots[x][y];
            }
            if (has_p1 && has_p2)
                this.check_again[x][y][3] = false;
        }
        else if (this.check_again[x][y][3] && !upleft_possible)
            this.check_again[x][y][3] = false;

        // If we made it down here, then there is no winner this tile can see.
        // So instead return if there is no possible win on this tile.
        for (int k = 0; k < 4; k++)
            if (this.check_again[x][y][k])
                return 0;
        return -10;
    }

    /**
     * Outputs the character for a tile
     * @param x tile column
     * @param y tile row
     * @return 'X' for player 1 piece, 'O' for player 2 piece, '.' for empty
     */
    private String slotChar(int x, int y){
        int slot_val = this.slots[x][y];
        switch (slot_val){
            case -1:
                return "O ";
            case 1:
                return "X ";
            case -2:
                return "O<";
            case 2:
                return "X<";
            default:
                return ". ";
        }
    }
    /**
     * Outputs the board to the screen
     */
    public void outputBoard(){
        StringBuilder print_line = new StringBuilder();
        for (int j = this.rows - 1; j > -1; j--){
            for (int i = 0; i < this.columns; i++) {print_line.append(this.slotChar(i,j));}
            System.out.println(print_line);
            print_line.delete(0,this.columns*2);
        }
    }

    /**
     * Outputs numbers to help illustrate which numbers correspond to which columns
     */
    public void learnBoard(){
        this.outputBoard();
        for (int i = 1; i <= this.columns; i++)
            System.out.print(i + " ");
    }
}