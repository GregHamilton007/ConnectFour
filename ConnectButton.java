/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Buttons used in GUI window corresponding to Connect Four game board squares
 *
 * @author greg
 */
public class ConnectButton extends javafx.scene.control.Button {

    //location of button
    private int row;
    private int column;

    /**
     * ConnectButton Constructor
     *
     * @param label
     * @param row
     * @param column
     */
    public ConnectButton(String label, int row, int column) {
        super(label);
        this.row = row;
        this.column = column;
    }

    /**
     * returns the row that the button is in the game board grid
     *
     * @return row - the row that the button is on the game board
     */
    public int getRow() {
        return this.row;
    }

    /**
     * returns the column that the button is on the game board grid
     *
     * @return column - - the column that the button is on the game board
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Returns the position of the button on the game board grid as a string
     *
     * @return string with format <row>,<column>
     */
    public String toString() {
        return "(<" + row + ">,<" + column + ">)";
    }
}
