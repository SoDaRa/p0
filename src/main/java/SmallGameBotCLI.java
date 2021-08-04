import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SmallGameBotCLI {
    /**
     * Runs the main games
     * @param args Currently Unused
     */
    public static void main(String[] args) {
        var continue_flag = true;
        Scanner my_scanner = new Scanner(System.in);
        String input;
        Logger logger = LoggerFactory.getLogger(SmallGameBotCLI.class);
        logger.info("Starting Bot");
        System.out.println("Hello and thank you for running Small Name Bot.");
        // Main Loop
        while (continue_flag) {
            System.out.println("Currently we have TicTacToe and ConnectFour.");
            System.out.println("Which would you like to play? (Enter 'quit' to leave app)");
            input = my_scanner.nextLine();
            switch (input){
                case "TTT":
                case "TicTacToe":
                    logger.info("Entering Tic Tac Toe");
                    TicTacToe.main(new String[]{""});
                    logger.info("Exiting Tic Tac Toe");
                    break;
                case "C4":
                case "Connect4":
                case "ConnectFour":
                    logger.info("Entering Connect 4");
                    ConnectFour.main(new String[]{""});
                    logger.info("Exiting Connect 4");
                    break;
                case "Battleship":
                    logger.info("Entering Battleship");
                    Battleship.main(new String[]{""});
                    logger.info("Exiting Battleship");
                    break;
                case "quit":
                    continue_flag = false;
            }
        }
        System.out.println("Thanks for playing! Have a good one!");
    }
}
