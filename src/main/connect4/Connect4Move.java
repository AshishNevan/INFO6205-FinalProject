package connect4;

import core.Move;
import core.State;

public class Connect4Move implements Move<Connect4> {
    private final int player;
    private final int column;

    public Connect4Move(int player, int column) {
        this.player = player;
        this.column = column;
    }

    public Connect4 game() {
        return new Connect4();
    }

    public State<Connect4> next(State<Connect4> state) {
        Connect4State nextState = ((Connect4State) state).clone();
        nextState.makeMove(this);
        return nextState;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int player() {
        return player;
    }
}
