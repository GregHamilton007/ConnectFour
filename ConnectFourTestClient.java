
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Plays ConnectFour game in the Output window, using text inputs
 *
 * @author greg
 */
public class ConnectFourTestClient {

    /**
     * Main method which starts a ConnectFour game, prints out the game board,
     * prompts for player interaction, takes the players turn, and determines
     * the winner
     *
     */
    public static void main(String args[]) {

        //Creates new COnnectFourGame
        ConnectFourGame game = new ConnectFourGame(ConnectFourEnum.BLACK);

        //sets up scanner to read in user inputs
        Scanner scanner = new Scanner(System.in);

        //contiually prints gameboard and accepts players turns 
        //until game is finished
        do {
            //prints game board
            System.out.println(game.toString());

            //prompts user for input
            System.out.println(game.getTurn()
                    + ": Where do you want to mark? Enter row column");

            //loads in user inputs
            int row = scanner.nextInt();
            int column = scanner.nextInt();
            scanner.nextLine();

            //takes turn and searches for winner
            game.takeTurn(row, column);

            //keep playing until someone has one or their is a draw
        } while (game.getGameState() == ConnectFourEnum.IN_PROGRESS);

        //prints game winner or draw
        System.out.println(game.getGameState());

    }
}
