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

    public Connect4State() {
        this.board = new int [Connect4.ROWS][Connect4.COLUMNS];
        this.currentPlayer = Connect4.PLAYER1;
        this.randomState = new RandomState(Connect4.COLUMNS);
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
        // Check for horizontal wins
        for (int row = 0; row < Connect4.ROWS; row++) {
            for (int col = 0; col < Connect4.COLUMNS - 3; col++) {
                if (board[row][col] != Connect4.EMPTY && board[row][col] == board[row][col + 1] && board[row][col] == board[row][col + 2] && board[row][col] == board[row][col + 3]) {
                    return Optional.of(board[row][col]);
                }
            }
        }

        // Check for vertical wins
        for (int col = 0; col < Connect4.COLUMNS; col++) {
            for (int row = 0; row < Connect4.ROWS - 3; row++) {
                if (board[row][col] != Connect4.EMPTY && board[row][col] == board[row + 1][col] && board[row][col] == board[row + 2][col] && board[row][col] == board[row + 3][col]) {
                    return Optional.of(board[row][col]);
                }
            }
        }

        // Check for diagonal wins (bottom left to top right)
        for (int row = 3; row < Connect4.ROWS; row++) {
            for (int col = 0; col < Connect4.COLUMNS - 3; col++) {
                if (board[row][col] != Connect4.EMPTY && board[row][col] == board[row - 1][col + 1] && board[row][col] == board[row - 2][col + 2] && board[row][col] == board[row - 3][col + 3]) {
                    return Optional.of(board[row][col]);
                }
            }
        }

        // Check for diagonal wins (top left to bottom right)
        for (int row = 0; row < Connect4.ROWS - 3; row++) {
            for (int col = 0; col < Connect4.COLUMNS - 3; col++) {
                if (board[row][col] != Connect4.EMPTY && board[row][col] == board[row + 1][col + 1] && board[row][col] == board[row + 2][col + 2] && board[row][col] == board[row + 3][col + 3]) {
                    return Optional.of(board[row][col]);
                }
            }
        }

        return Optional.empty();
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
                break;
            }
        }
        currentPlayer = 3 - currentPlayer;
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
    }
}
