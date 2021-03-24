/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Saves the parameters corresponding to move to be passed to observer
 *
 * @author greg
 */
public class ConnectMove {

    //params of current move
    private int row;
    private int column;
    private ConnectFourEnum colour;

    /**
     * ConnectMove Constructor
     *
     * @param row - row of the last played move
     * @param column - column of the last played move
     * @param colour - player who played the last move
     */
    public ConnectMove(int row, int column, ConnectFourEnum colour) {
        this.row = row;
        this.column = column;
        this.colour = colour;
    }

    /**
     * Returns the row that the current move was place in
     *
     * @return row - row that the checker was placed
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column that the current move was place in
     *
     * @return column - column that the checker was placed
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Returns the player that made the current move
     *
     * @return colour - player who played the last checker
     */
    public ConnectFourEnum getColour() {
        return this.colour;
    }
}
