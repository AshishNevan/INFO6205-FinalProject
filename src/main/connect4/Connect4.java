package connect4;

import core.Game;
import core.State;

import java.util.Optional;
import java.util.Random;

public class Connect4 implements Game<Connect4> {
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int EMPTY = 0;
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;

    private final Random random;

    public Connect4(Random random) {
        this.random = random;
    }

    public Connect4() {
        this(System.currentTimeMillis());
    }

    public Connect4(long seed) {
        this(new Random(seed));
    }

    public static void main(String[] args) {
        Connect4 game = new Connect4();
        State<Connect4> state = game.start();
        MCTS mcts = new MCTS((Connect4State) state);

        while (!state.isTerminal()) {
            Connect4Move move = mcts.findBestMove();
            if (move != null) {
                state = state.next(move);
                ((Connect4State) state).printBoard();
            } else {
                // Handle the case where no valid move was found
                System.out.println("No valid move found. Skipping player's turn.");
            }
        }

        Optional<Integer> winner = state.winner();
        if (winner.isPresent()) {
            System.out.println("Player " + (winner.get() == RED ? "red" : "green") + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }
    @Override
    public State<Connect4> start() {
        return new Connect4State();
    }

    @Override
    public int opener() {
        return RED;
    }
}