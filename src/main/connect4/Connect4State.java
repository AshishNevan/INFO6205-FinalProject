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
        //Logic to check if the game has ended
    }

    @Override
    public int player() {
        return currentPlayer;
    }

    @Override
    public Optional<Integer> winner() {
        //Logic to determine the winner

    }

    @Override
    public Random random() {
        return randomState;
    }

    @Override
    public Collection<Move<Connect4>> moves (int player) {
        Collection<Move<Connect4>> moves = new ArrayList<>();
        //Logic to generate all possible moves
        return moves;
    }

    @Override
    public State<Connect4> next(Move<Connect4> move) {
        return move.next(this);
    }

    public void makeMove(Connect4Move move) {
        //Logic to make a move on the board
    }

    public Connect4State clone() {
        //Logic to clone the state
    }
}
