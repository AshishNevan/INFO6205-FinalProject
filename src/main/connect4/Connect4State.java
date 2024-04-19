package connect4;

import core.Move;
import core.State;
import core.RandomState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Connect4State implements State<Connect4> {
    private int[][] board;
    private int currentPlayer;
    private Random random;
    private RandomState randomState;
    private int lastRow;
    private int lastCol;

    public Connect4State() {
        this.board = new int [Connect4.ROWS][Connect4.COLUMNS];
        this.currentPlayer = Connect4.RED;
        this.randomState = new RandomState(Connect4.COLUMNS);
        this.lastRow = -1;
        this.lastCol = -1;
    }

    @Override
    public Connect4 game() {
        return new Connect4();
    }

    @Override
    public boolean isTerminal() {
        // Check for a win or a draw
        return winner().isPresent() || moves(currentPlayer).isEmpty();
    }

    @Override
    public int player() {
        return currentPlayer;
    }

    @Override
    public Optional<Integer> winner() {
        return winner(lastRow, lastCol);
    }

    public Optional<Integer> winner(int lastRow, int lastCol) {
        if (lastRow == -1 || lastCol == -1) {
            // No move has been made yet, so there's no winner
            return Optional.empty();
        }

        int player = board[lastRow][lastCol];

        // Check horizontal
        if (countConsecutive(player, lastRow, lastCol, 0, 1) >= 4) {
            return Optional.of(player);
        }

        // Check vertical
        if (countConsecutive(player, lastRow, lastCol, 1, 0) >= 4) {
            return Optional.of(player);
        }

        // Check diagonal (bottom left to top right)
        if (countConsecutive(player, lastRow, lastCol, -1, 1) >= 4) {
            return Optional.of(player);
        }

        // Check diagonal (top left to bottom right)
        if (countConsecutive(player, lastRow, lastCol, 1, 1) >= 4) {
            return Optional.of(player);
        }

        // No winner yet
        return Optional.empty();
    }

    private int countConsecutive(int player, int row, int col, int dRow, int dCol) {
        int count = 0;
        while (row >= 0 && row < Connect4.ROWS && col >= 0 && col < Connect4.COLUMNS && board[row][col] == player) {
            count++;
            row += dRow;
            col += dCol;
        }
        return count;
    }

    @Override
    public Random random() {
        return random;
    }

    @Override
    public Collection<Move<Connect4>> moves (int player) {
        Collection<Move<Connect4>> moves = new ArrayList<>();
        for (int i = 0; i < Connect4.COLUMNS; i++) {
            if (board[0][i] == Connect4.EMPTY) {
                moves.add(new Connect4Move(player, i));
            }
        }
        return moves;
    }

    @Override
    public State<Connect4> next(Move<Connect4> move) {
        Connect4State nextState = clone();
        nextState.makeMove((Connect4Move) move);
        return nextState;
    }

    public void makeMove(Connect4Move move) {
        for (int i = Connect4.ROWS - 1; i >= 0; i--) {
            if (board[i][move.getColumn()] == Connect4.EMPTY) {
                board[i][move.getColumn()] = move.player();
                lastRow = i;
                lastCol = move.getColumn();
                currentPlayer = 3 - currentPlayer;
                break;
            }
        }
    }

    public Connect4State clone() {
        Connect4State clone = new Connect4State();
        for (int i = 0; i < Connect4.ROWS; i++) {
            System.arraycopy(board[i], 0, clone.board[i], 0, Connect4.COLUMNS);
        }
        clone.currentPlayer = currentPlayer;
        return clone;
    }

    public void printBoard() {
        for (int i = 0; i < Connect4.ROWS; i++) {
            for (int j = 0; j < Connect4.COLUMNS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Connect4State) {
            Connect4State other = (Connect4State) obj;
            for (int i = 0; i < Connect4.ROWS; i++) {
                for (int j = 0; j < Connect4.COLUMNS; j++) {
                    if (board[i][j] != other.board[i][j]) {
                        return false;
                    }
                }
            }
            return currentPlayer == other.currentPlayer;
        }
        return false;
    }
}
