package ConnectN;

import org.junit.Test;

import java.util.Iterator;

public class ConnectNModel {

    private final int width, height, n;
    private final PlayerIterator players;
    private String[][] grid;
    private enum Directions {UP, DOWN, LEFT, RIGHT, DIAGUPLEFT, DIAGUPRIGHT, DIAGDOWNLEFT, DIAGDOWNRIGHT}
    private String winner;

    /**
     *
     * @param width the width of the grid (> 0)
     * @param height the height of the grid (> 0)
     * @param n the length of the line required to win (> 0)
     * @param players the names of the players (non-null, non "", at least size 1)
     */

    private ConnectNModel(int width, int height, int n, String... players) {

        if (width <= 0 || height <= 0 || n <= 0) {
            throw new IllegalArgumentException("Invalid Parameters");
        }

        for (String s: players) {
            if (s == "" || s == null) {
                throw new IllegalArgumentException("Invalid Parameters");
            }
        }

        this.width = width;
        this.height = height;
        this.n = n;
        this.players = new PlayerIterator(players);
        this.grid = new String[this.width][this.height];
    }

    //TODO: FIX design so that we're not relying on null

    /**
     * Plays a move for the next player
     * @param col the column in which the next player drops a piece in
     */
    void playMove(int col) {
        int row = this.lowestPosition(col);
        this.grid[row][col] = this.players.next();
        if (this.playerWon()) {
            System.out.println();
        }


    }

    /**
     *
     * @param col which column to check
     * @return the lowest empty position in the column
     * @throws IllegalArgumentException if the column is out of bounds
     */
    int lowestPosition(int col) {
        for (int i = 0; i < this.height; i += 1) {
            if (this.grid[i][col] == null) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column out of bounds");
    }

    /**
     * Checks if a player has won (meaning the game is over)
     * @return whether a player has won, and sets this.winner to the respective winner if a player has won
     */
    private boolean playerWon() {
        //1. iterate through each item of the list
        for(int i = 0; i < this.width; i += 1) {
            for (int k = 0; k < this.height; k += 1) {
                //if not empty it should be a player name
                if (this.grid[i][k] != null) {
                    System.out.println(this.grid[i][k]);
                    if(this.gridCheckHelper(k, i, this.n, Directions.UP, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.DOWN, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.LEFT, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.RIGHT, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.DIAGDOWNLEFT, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.DIAGDOWNRIGHT, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.DIAGUPLEFT, this.grid[k][i])
                    || this.gridCheckHelper(k, i, this.n, Directions.DIAGUPRIGHT, this.grid[k][i])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method used to recursively check if there is a completed N-number of connected pieces somewhere in the grid
     * @param row the current row
     * @param col the current column
     * @param remaining the number of pieces remaining to make a complete N-Number of connected pieces and win the game
     * @param d the direction we are checking in
     * @param name the name of the player we are checking
     * @return whether this player's pieces starting at spot (row, col) form a fully connected N-Number of pieces
     * @throws IllegalArgumentException if the direction is invalid
     */
    private boolean gridCheckHelper(int row, int col, int remaining, Directions d, String name) {
        if (remaining <= 0) {
            this.winner = name;
            return true;
        }
        if (!this.inbounds(row, col) || this.grid[row][col] != name) {
            return false;
        }
        switch (d) {
            case UP:
                return this.gridCheckHelper(row - 1, col, remaining - 1, d, name);
            case DOWN:
                return this.gridCheckHelper(row + 1, col, remaining - 1, d, name);
            case LEFT:
                return this.gridCheckHelper(row, col - 1, remaining - 1, d, name);
            case RIGHT:
                return this.gridCheckHelper(row, col + 1, remaining - 1, d, name);
            case DIAGUPLEFT:
                return this.gridCheckHelper(row - 1, col - 1, remaining - 1, d, name);
            case DIAGUPRIGHT:
                return this.gridCheckHelper(row - 1, col + 1, remaining - 1, d, name);
            case DIAGDOWNLEFT:
                return this.gridCheckHelper(row + 1, col - 1, remaining - 1, d, name);
            case DIAGDOWNRIGHT:
                return this.gridCheckHelper(row + 1, col + 1, remaining - 1, d, name);
            default:
                throw new IllegalArgumentException("Invalid Direction");
        }
    }

    private boolean inbounds(int row, int col) {
        return row >= 0
                && row < this.height
                && col >= 0
                && col < this.width;
    }

    /**
     * Constructs a new builder
     * @return Builder object
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int width = 15;
        private int height = 15;
        private int n = 4;
        private String[] players = {};

        /**
         *
         * @param width the width of the board
         * @return {@code this} for method chaining
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         *
         * @param height the height of the board
         * @return {@code this} for method chaining
         */
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        /**
         *
         * @param n the number of connected pieces to win
         * @return {@code this} for method chaining
         */
        public Builder n(int n) {
            this.n = n;
            return this;
        }

        /**
         *
         * @param players the names of the players in the game
         * @return {@code this} for method chaining
         */
        public Builder players(String... players) {
            this.players = players;
            return this;
        }

        /**
         * Builds and returns the specified {@link ConnectNModel}
         * @return a new {@code ConnectNModel}
         */
        public ConnectNModel build() {
            return new ConnectNModel(this.width, this.height, this.n, this.players);
        }

    }

    private class PlayerIterator implements Iterator<String> {

        private final String[] players;
        private int index;

        private PlayerIterator(String[] players) {
            this.players = players;
            this.index = 0;
        }

        public String next() {
            this.index += 1;
            return this.players[(this.index - 1) % this.players.length];
        }

        public boolean hasNext() {
            return true;
        }

    }




}
