package connect4;

import core.State;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class Connect4MoveTest {

    @Test
    public void testGame() {
        Connect4Move move = new Connect4Move(1, 2);
        Connect4 game = move.game();
        assertNotNull(game);
    }

    @Test
    public void testNext() {
        Connect4Move move = new Connect4Move(1, 2);
        Connect4State state = new Connect4State();
        State<Connect4> nextState = move.next(state);
        assertNotNull(nextState);
        assertNotEquals(state, nextState);
    }

    @Test
    public void testGetColumn() {
        Connect4Move move = new Connect4Move(1, 2);
        int column = move.getColumn();
        assertEquals(2, column);
    }

    @Test
    public void testPlayer() {
        Connect4Move move = new Connect4Move(1, 2);
        int player = move.player();
        assertEquals(1, player);
    }
}