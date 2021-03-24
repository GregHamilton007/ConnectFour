
import java.util.Arrays;
import java.util.Observable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Game Engine for a game of Connect Four
 *
 * @author greg
 */
public class ConnectFourGame extends Observable {

    //Characterisitics of the board
    private int nColumns;
    private int nRows;
    private int numToWin;

    //grid of sqaures which can hold checkers
    private ConnectFourEnum grid[][];

    //Holds the state of the game (winner or in progress)
    private ConnectFourEnum gameState;

    //Stores who's turn it is to play
    private ConnectFourEnum turn;

    //Stores how many checkers have been dropped
    int nMarks;

    /**
     * ConnectFourGame - One argument constructor which creates a game engine
     * for playing ConnectFour
     *
     * @param initialTurn - Player to start (red or black)
     */
    public ConnectFourGame(ConnectFourEnum initialTurn) {
        this(8, 8, 4, initialTurn);
    }

    /**
     * ConnectFourGame - Constructor which creates a game engine for playing
     * ConnectFour
     *
     * @param nRows - number of rows on the game board
     * @param nColumns - number of columns on the game board
     * @param numToWin - number of checkers in a row to win the game
     * @param initialTurn - Player to start (red or black)
     */
    ConnectFourGame(int nRows, int nColumns, int numToWin, ConnectFourEnum initialTurn) {
        //Throw exception game is unplayable/not possible to win given parameters
        if (nRows < 1 || nColumns < 1 || numToWin < 1 || (numToWin > nRows && numToWin > nColumns)) {
            throw new IllegalArgumentException("The board dimensions and numToWIn must all be non-negative snd the board must be big enough to have numToWin elements in a row");
        }

        //paramaters of the gameboard
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.numToWin = numToWin;

        //holds grid of sqaures, and which player has checkers on each 
        this.grid = new ConnectFourEnum[nRows][nColumns];

        //Sets the game to IN PROGRESS, sets who starts the game, sets number of Checkers
        //played to zero, sets all squares to empty
        reset(initialTurn);

    }

    /**
     * sets up Connect Four game to be played again
     *
     * @param initialTurn - Player to start (red or black)
     */
    public void reset(ConnectFourEnum initialTurn) {

        //Sets the game to IN PROGRESS
        this.gameState = ConnectFourEnum.IN_PROGRESS;

        //sets who starts the game
        this.turn = initialTurn;

        //holds the number of tokens played so far 
        this.nMarks = 0;

        //sets all squares to empty
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nColumns; j++) {
                grid[i][j] = ConnectFourEnum.EMPTY;
            }
        }
    }

    /**
     * Takes user specified turn in Connect Four Game
     *
     * @param row - row user wants to place the checker
     * @param column - column user wants to place the checker
     * @return game state (IN_PROGRESS, RED, BLACK, or DRAW)
     */
    public ConnectFourEnum takeTurn(int row, int column) {
        //Checks if user selected sqaure is on the board
        if (row < 0 || row >= nRows || column < 0 || column >= nColumns) {
            throw new IllegalArgumentException("The inputs must correspond to spaces on the board");
        }

        //ensures the sqaure isnt't currently occupied
        if (this.grid[row][column] != ConnectFourEnum.EMPTY) {
            throw new IllegalArgumentException("That space is already taken");
        }

        //Checks that checker is dropped on the bottom of the board or on another checker
        if (row > 0) {
            if (this.grid[row - 1][column] == ConnectFourEnum.EMPTY) {
                throw new IllegalArgumentException("Attempted to place checker ontop of empty square");
            }
        }

        //add new move to the grid
        grid[row][column] = this.getTurn();

        //Create object which allows passing all details about move to observers
        ConnectMove thisMove = new ConnectMove(row, column, this.turn);

        //Changes player turns
        if (this.getTurn() == ConnectFourEnum.BLACK) {
            this.turn = ConnectFourEnum.RED;
        } else {
            this.turn = ConnectFourEnum.BLACK;
        }

        //increases the number of tokens on the board by 1
        nMarks++;

        //check for winner
        this.gameState = findWinner(row, column);

        //call update method to change GUI
        setChanged();
        notifyObservers(thisMove);

        return this.getGameState();
    }

    /**
     * Determines if the game has been one yet
     *
     * @param rowLastMove - row that the last checker was dropped in
     * @param columnLastMove - column that the last check was dropped in
     * @return game state (IN_PROGRESS, RED, BLACK, or DRAW)
     */
    private ConnectFourEnum findWinner(int rowLastMove, int columnLastMove) {
        //counts how many tokens of the current player are in a row
        int numInARow = 1;

        //search down from last placed checker
        for (int i = 1; i < numToWin; i++) {

            //if token being checked has row which is on the board
            if ((rowLastMove - i) >= 0) {
                //increment numInARow if checker is next to same coloured checker
                if ((grid[rowLastMove - i][columnLastMove].equals(grid[rowLastMove][columnLastMove]))) {
                    numInARow++;
                }

                //go here if checker to be check is below the bottom row
            } else {
                break;
            }
        }
        //return the colour of the last move if there are numToWin checkers in a row
        if (numInARow == numToWin) {
            return grid[rowLastMove][columnLastMove];
        }

        //move on to checking horizontal
        //iterates through all columns within NumToWin of the last move and with at least numToWin-1 elements right of it
        for (int j = Math.max(columnLastMove - numToWin + 1, 0); j <= this.nColumns - this.numToWin; j++) {

            //sets numinarow back to 1
            numInARow = 1;

            //enter if the current element being searched has been picked
            if (grid[rowLastMove][columnLastMove].equals(grid[rowLastMove][j])) {

                //searches right from the current element to see if they match the current element
                for (int k = 1; k < numToWin; k++) {
                    if (grid[rowLastMove][j + k].equals(grid[rowLastMove][j])) {
                        numInARow++;
                    }
                }

                //if numToWin consecutive values match enter here
                if (numInARow >= numToWin) {

                    //return the winner 
                    return grid[rowLastMove][columnLastMove];
                }
            }
        }

        //if all spaces filled and no winner, return Draw
        if (nMarks == this.nColumns * this.nRows) {
            return ConnectFourEnum.DRAW;
        }

        //no winner yet
        return ConnectFourEnum.IN_PROGRESS;
    }

    /**
     * Gets the current game state
     *
     * @return gameState (IN_PROGRESS, RED, BLACK, or DRAW)
     */
    public ConnectFourEnum getGameState() {
        return this.gameState;
    }

    /**
     * Gets the current players turn
     *
     * @return turn - player to play next
     */
    public ConnectFourEnum getTurn() {
        return this.turn;
    }

    /**
     * Returns the gameBoard as a string
     *
     * @return game board String
     */
    @Override
    public String toString() {
        //intializes blank string
        String s1 = "";

        //iterates through all rows
        for (int i = nRows - 1; i >= 0; i--) {

            //iterates through all elements in the j row 
            //and adds the element contained in grid seperated by ' | '
            for (int j = 0; j < nColumns; j++) {
                s1 += this.grid[i][j] + " | ";
            }

            //change cursor to the next line 
            s1 += "\n";
        }

        //return the string generated containing the board
        return s1;
    }

    /**
     * determines if object is equivalent to this ConnectFourGame (deep check)
     *
     * @param other - object to be compared to this game
     * @return true or false
     */
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if (other == null) {
//            return false;
//        }
//        if (getClass() != other.getClass()) {
//            return false;
//        }
//        ConnectFourGame game = (ConnectFourGame) other;
//        return (this.nRows == game.nRows && this.nColumns == game.nColumns
//                && this.numToWin == game.numToWin && Arrays.deepEquals(this.grid, game.grid)
//                && this.nMarks == game.nMarks && this.gameState.equals(game.gameState)
//                && this.turn.equals(game.turn));
//    }
}
