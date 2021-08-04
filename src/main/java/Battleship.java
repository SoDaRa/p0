import java.util.Scanner;
import java.util.Random;
public class Battleship {
    public static void main(String[] args) {
        BattleshipBoard myBoard = new BattleshipBoard(8, 8);
        Scanner my_scanner = new Scanner(System.in);

        myBoard.learnBoard();
        // Main game loop
        while (myBoard.checkWin() == 0){
            int input_x = -1;
            int input_y = -1;
            System.out.println("Shots Remaining: " + myBoard.shots);
            // Request input
            do {
                System.out.println("Please enter x coordinate:");
                my_scanner.hasNext();
                if (my_scanner.hasNextInt())
                    input_x = my_scanner.nextInt();
                else
                    my_scanner.next();
                my_scanner.nextLine();
            } while(input_x < 1 || input_x > 8);
            do {
                System.out.println("Please enter y coordinate:");
                my_scanner.hasNext();
                if (my_scanner.hasNextInt())
                    input_y = my_scanner.nextInt();
                else
                    my_scanner.next();
                my_scanner.nextLine();
            } while(input_y < 1 || input_y > 8);
            var shot_result = myBoard.shoot(input_x - 1, input_y - 1);
            if (shot_result)
                myBoard.outputBoard();
            else
                System.out.println("Invalid Shot. Please try again.");
        }
        var result = myBoard.checkWin();
        if (result == 1)
            System.out.println("You Win!!");
        else
            System.out.println("You Lose!!");
    }
}
class BattleshipBoard{
    boolean[][] shot_tiles; // 8 x 8 board
    public int shots;
    int rows;
    int columns;
    Ship[] my_ships;
    boolean[][] ship_grid; // Helps compare with the shots_tiles to figure out things
    public BattleshipBoard(int rows, int columns) {
        this.shot_tiles = new boolean[columns][rows];
        this.ship_grid = new boolean[columns][rows];

        this.rows = rows;
        this.columns = columns;
        this.my_ships = new Ship[3];
        // Place ships
        for (int i = 0; i < 3; i++)
            this.placeShip(2+i, i);
        this.shots = 24;
    }

    /**
     * Places a ship on the board of the given length
     * @param ship_length How long the ship is.
     * @param index the index into this.my_ships to add it to.
     */
    private void placeShip(int ship_length, int index){
        Random rand = new Random();
        int new_x;
        int new_y;
        boolean vertical;
        var placement_success = true;
        do{
            // Assume true until false
            placement_success = true;
            new_x = rand.nextInt(this.columns - 1);
            new_y = rand.nextInt(this.rows - 1);
            vertical = rand.nextBoolean();
            // Check if the area is available
            if (placement_success)
                for (int i = 0; i < ship_length; i++){
                    if (vertical){
                        if ((new_y + i) >= this.rows)
                            placement_success = false;
                        else if (this.ship_grid[new_x][new_y+i])
                            placement_success = false;
                    }
                    else{
                        if ((new_x + i) >= this.columns)
                            placement_success = false;
                        else if (this.ship_grid[new_x+i][new_y])
                            placement_success = false;
                    }
                }
        }while(!placement_success);
        // Create ship
        this.my_ships[index] = new Ship(new_x, new_y, vertical, ship_length);
        // Update ship_grid with ship
        for (int i = 0; i < ship_length; i++){
            if (vertical)
                this.ship_grid[new_x][new_y+i] = true;
            else
                this.ship_grid[new_x+i][new_y] = true;
        }
    }

    /**
     * Shoots a spot on the board and marks it as such.
     * @param x The shot's x
     * @param y The shot's y
     * @return If the shot was successful or denied
     */
    public boolean shoot(int x, int y){
        // Out of bounds shot
        if ((x < 0 && x >= this.columns) || (y < 0 && y >= this.rows))
            return false;
        // Already hit shot
        if (this.shot_tiles[x][y])
            return false;
        // Update ships
        for (Ship my_ship : this.my_ships) {
            if (my_ship.isAlive) {
                var hit_result = my_ship.checkHit(x, y);
                switch (hit_result){
                    case 1:
                        System.out.println("A Direct Hit!");
                        break;
                    case 2:
                        System.out.println("Ship Sunk!!");
                        break;
                }
            }
        }
        this.shot_tiles[x][y] = true;
        this.shots -= 1;
        return true;
    }

    /**
     * Whether the game is won, ongoing, or over
     * @return 0 for ongoing, 1 for win, -1 for lose
     */
    public int checkWin(){
        var count = 0;
        for (Ship my_ship : this.my_ships)
            if (!my_ship.isAlive)
                count += 1;
        if (count == this.my_ships.length)
            return 1;
        if (this.shots <= 0)
            return -1;
        return 0;
    }

    public String slotChar(int x, int y){
        // Successful Hit
        if (this.ship_grid[x][y] == this.shot_tiles[x][y] && this.ship_grid[x][y])
            return "X ";
        // Missed shot
        if (this.shot_tiles[x][y])
            return "O ";
        return ". ";
    }

    public void outputBoard(){
        StringBuilder print_line = new StringBuilder();
        for (int j = this.rows - 1; j > -1; j--){
            print_line = new StringBuilder();
            for (int i = 0; i < this.columns; i++){print_line.append(this.slotChar(i,j));}
            System.out.println(print_line);
        }
    }
    public void learnBoard(){
        StringBuilder print_line = new StringBuilder();
        // Top row
        for (int i = 1; i < this.columns + 1; i++){
            print_line.append(i);
            print_line.append(" ");
        }
        System.out.println(print_line);
        // Main rows
        for (int j = this.rows - 1; j > -1; j--){
            print_line = new StringBuilder();
            for (int i = 0; i < this.columns; i++){print_line.append(this.slotChar(i,j));}
            var temp = j + 1;
            print_line.append(" ");
            print_line.append(temp);
            System.out.println(print_line);
        }
    }
}

class Ship{
    public int x;
    public int y;
    public boolean vert;
    public int length;
    public boolean[] segments; // True if segment hit. False if not.
    public boolean isAlive;

    /**
     * Makes a ship
     * @param x Ship's left side
     * @param y Ship's bottom side
     * @param vert Whether it's oriented vertically or horizontally
     * @param length How long it is.
     */
    public Ship(int x, int y, boolean vert, int length){
        this.x = x;
        this.y = y;
        this.vert = vert;
        this.isAlive = true;
        this.length = length;
        this.segments = new boolean[length];
        for (int i = 0; i < length; i++)
            this.segments[i] = false;
    }

    /**
     * Checks if it's hit
     * @param x Column of hit
     * @param y Row of hit
     * @return 0 for miss, 1 for hit, 2 for sunk
     */
    public int checkHit(int x, int y){
        // If too low or too high to be hit
        if (x < this.x && y < this.y)
            return 0;
        if (x > (this.x + this.length) && y > (this.y + this.length))
            return 0;
        if (this.vert){
            if (x != this.x)
                return 0;
            for (int j = 0; j < this.length; j++){
                if (y == (this.y + j)) {
                    this.segments[j] = true;
                    this.updateStatus();
                    if (!this.isAlive)
                        return 2;
                    else
                        return 1;
                }
            }
        }
        else{
            if (y != this.y)
                return 0;
            for (int i = 0; i < this.length; i++){
                if (x == (this.x + i)) {
                    this.segments[i] = true;
                    this.updateStatus();
                    if (!this.isAlive)
                        return 2;
                    else
                        return 1;
                }
            }
        }
        return 0;
    }
    private void updateStatus(){
        var count = 0;
        for (int i = 0; i < this.length; i++)
            if (this.segments[i])
                count += 1;
        if (count == this.length)
            this.isAlive = false;
    }
}