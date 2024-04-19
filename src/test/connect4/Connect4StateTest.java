package connect4;

import core.Move;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class Connect4StateTest{

    @Test
    public void testGame() {
        Connect4State state = new Connect4State();
        assertNotNull(state.game());
    }

    @Test
    public void testIsTerminal() {
        Connect4State state = new Connect4State();

        assertFalse(state.isTerminal());
    }

    @Test
    public void testPlayer() {
        Connect4State state = new Connect4State();
        assertEquals(Connect4.RED, state.player());
    }

    @Test
    public void testWinner() {
        Connect4State state = new Connect4State();
        assertFalse(state.winner().isPresent());
    }

    @Test
    public void testRandom() {
        Connect4State state = new Connect4State();
        assertNotNull(state.random());
    }

    @Test
    public void testMoves() {
        Connect4State state = new Connect4State();
        assertEquals(Connect4.COLUMNS, state.moves(Connect4.RED).size());
    }

    @Test
    public void testNext() {
        Connect4State state = new Connect4State();
        Move<Connect4> move = new Connect4Move(Connect4.RED, 0);
        Connect4State nextState = (Connect4State) state.next(move);
        assertNotNull(nextState);
        assertNotEquals(state, nextState);
    }

    @Test
    public void testMakeMove() {
        Connect4State state = new Connect4State();
        Connect4Move move = new Connect4Move(Connect4.RED, 0);
        state.makeMove(move);
        assertEquals(Connect4.BLUE, state.player());
    }
}