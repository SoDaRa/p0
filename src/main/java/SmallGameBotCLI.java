import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
public class SmallGameBotCLI {
    static Logger logger = Logger.getLogger(SmallGameBotCLI.class);
    /**
     * Runs the main games
     * @param args Currently Unused
     */

    public static void main(String[] args) {
        DOMConfigurator.configure("log4j.xml");
        var continue_flag = true;
        Scanner my_scanner = new Scanner(System.in);
        String input;
        logger.info("Starting Bot");
        System.out.println("Hello and thank you for running Small Name Bot.");
        // Main Loop
        while (continue_flag) {
            System.out.println("Currently we have TicTacToe, ConnectFour and Battleship.");
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
        logger.info("Shutting down bot");
    }
}
