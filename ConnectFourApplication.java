/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * ConnectFourApplication Plays game of connect four in a GUI window
 *
 * @author greg
 */
public class ConnectFourApplication extends Application implements Observer {

    //Game Board characterisitics
    public static int NUM_COLUMNS = 8;
    public static int NUM_ROWS = 8;
    public static int NUM_TO_WIN = 4;
    public static int BUTTON_SIZE = 40;

    //underlying class which plays the game
    private ConnectFourGame gameEngine;

    //clickable gameboard squares
    private ConnectButton[][] buttons;

    //holds the last clicked on button until user confirms move
    private static ConnectButton HighlightedButton;

    //prompts message for user to play
    private TextField message;

    /**
     * update Observes ConnectFourGame gameEngine, and changes the view of the
     * GUI accordingly Sets alert when game is over, and resets game and board
     * and prompts for player to start
     *
     * @param obs - object that is being observed, and is prompting an update
     * @param arg- data passed from the observable to the observer
     */
    @Override
    public void update(Observable obs, Object arg) {
        //cast move to ConnectMove object
        ConnectMove move = (ConnectMove) arg;

        //Changes board square corresponding to players move
        (buttons[move.getRow()][move.getColumn()]).setText(move.getColour().toString());

        //prompts next player to take their turn in TextField
        message.setText("It's " + gameEngine.getTurn() + "'s Turn");

        //check if game is over
        if (gameEngine.getGameState() != ConnectFourEnum.IN_PROGRESS) {

            //Alerts players who the winner is
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Connect Four Game");
            alert.setHeaderText("Game Over");
            alert.setContentText("Player " + gameEngine.getGameState() + " wins!");
            alert.showAndWait();

            //if odd number of tokens placed, next turn starts
            if (gameEngine.nMarks % 2 == 1) {
                gameEngine.reset(gameEngine.getTurn());
            } //if even number of tokens, who placed the last token starts 
            else {
                gameEngine.reset(move.getColour());
            }
            //sets all GUI sqaures back to EMPTY to match gameEngine
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < this.NUM_COLUMNS; j++) {
                    (buttons[i][j]).setText(ConnectFourEnum.EMPTY.toString());
                }
            }
            //prompt for alternating players to start
            message.setText(gameEngine.getTurn() + " Begins.");
        }
    }

    /**
     * creates instance of connectFourGame with a random player starting creates
     * JavaFX view and handles game play events
     *
     * @param primaryStage - the primary stage of the GUI window
     */
    @Override
    public void start(Stage primaryStage) {

        //Randomly pick which player starts ans start game
        if (new Random().nextInt(2) == 0) {
            gameEngine = new ConnectFourGame(NUM_ROWS, NUM_COLUMNS, NUM_TO_WIN, ConnectFourEnum.BLACK);
        } else {
            gameEngine = new ConnectFourGame(NUM_ROWS, NUM_COLUMNS, NUM_TO_WIN, ConnectFourEnum.RED);
        }

        //Subscribe this GUI to the gameEngine
        gameEngine.addObserver(this);

        //Sqaures of the gameboard
        buttons = new ConnectButton[NUM_ROWS][NUM_COLUMNS];

        //handler for ConnectButtons
        EventHandler<ActionEvent> gridButtonHandler = new ButtonHandler();

        //button to confirm last clicked square is where player wants to play
        Button confirmButton = new Button("Take my Turn");

        //handler for the confirm button
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            /**
             * handler for button confirming they want to place a checker is
             * last clicked ConnectButton
             */
            public void handle(ActionEvent event) {

                //takes current players turn based on last clicked on ConnectButton
                gameEngine.takeTurn(HighlightedButton.getRow(), HighlightedButton.getColumn());
            }
        });
        //uneditable textfield prompting player input 
        message = new TextField(gameEngine.getTurn() + " Begins.");
        message.setEditable(false);

        //sets the framework of the view as a BorderPane
        BorderPane root = new BorderPane();

        //arranges the message window at the top
        root.setTop(message);

        //sets the grid for placing ConnectButtons
        GridPane gameGrid = new GridPane();

        //creates and adds all ConnectButtons to the gameGrid
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {

                //Creates each game square
                buttons[i][j] = new ConnectButton(ConnectFourEnum.EMPTY.toString(), i, j);
                buttons[i][j].setMinHeight(BUTTON_SIZE);
                buttons[i][j].setMaxWidth(Double.MAX_VALUE);

                //sets the behaviour when pressed according to gridButtonHandler
                buttons[i][j].setOnAction(gridButtonHandler);

                //adds ConnectButton to gameGrid
                gameGrid.add(buttons[i][j], j, Math.abs(i - NUM_ROWS));
            }
        }

        //Arranges the gameGrid to the left of the BorderPane
        root.setLeft(gameGrid);

        //Arranges the COnfirmButton on the bottom of the borderPane
        root.setBottom(confirmButton);

        //BoilerPlate
        Scene scene = new Scene(root, 510, 380);
        primaryStage.setTitle("ConnectFour");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Stores the recently pressed ConnectButton's reference in Highlighted
     * button
     */
    class ButtonHandler implements EventHandler<ActionEvent> {

        /**
         * Saves the last pressed button
         *
         * @param event - user interaction
         */
        @Override
        public void handle(ActionEvent event) {
            //sets last button pressed as the source of the event
            ConnectFourApplication.HighlightedButton = (ConnectButton) event.getSource();
        }
    }

    /**
     * launches the GUI
     *
     * @param args - the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
