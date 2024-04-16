package tictactoe;

import core.Move;
import core.Node;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicTacToeNodeTest {

    @Test
    public void winsAndPlayouts() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(Position.parsePosition("X . O\nX O .\nX . O", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.isLeaf());
        assertEquals(2, node.wins());
        assertEquals(1, node.playouts());
    }

    @Test
    public void state() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertEquals(state, node.state());
    }

    @Test
    public void white() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.white());
    }

    @Test
    public void children() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        assertEquals(node.children().size(), 0);
    }

    @Test
    public void addChild() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        TicTacToe.TicTacToeState newState = (TicTacToe.TicTacToeState) state.next(state.chooseMove(1 - state.player()));
        node.addChild(newState);
        assertEquals(node.children().size(), 1);
    }

    @Test
    public void backPropagate() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        node.backPropagate();
        assertEquals(node.playouts(), 0);
        assertEquals(node.wins(), 0);
        assertEquals(node.children().size(), 0);
        assertEquals(node.playouts(), 0);
        assertEquals(node.wins(), 0);
    }

    @Test
    public void testIsFullyExpanded() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState();
        TicTacToeNode node = new TicTacToeNode(state);
        int currentPlayer = state.player();
        Collection<Move<TicTacToe>> possibleMoves = state.moves(currentPlayer);
        for (Move<TicTacToe> possibleMove : possibleMoves) {
            node.addChild(state.next(possibleMove));
        }
        assertTrue(node.isFullyExpanded());
    }

    @Test
    public void testGetBestChild_draw() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(Position.parsePosition("X . O\nO O X\nX X O", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        node.explore();
        assertEquals(1, node.getBestChild().wins(), 0.01);
    }

    @Test
    public void testGetBestChild_win() {
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(Position.parsePosition("X X O\n. O .\nX . .", TicTacToe.X));
        TicTacToeNode node = new TicTacToeNode(state);
        node.explore();
        for (Node<TicTacToe> child: node.children()) {
            TicTacToe.TicTacToeState s = (TicTacToe.TicTacToeState)child.state();
            System.out.println(s.position().render());
            System.out.println(child.wins()+" "+child.playouts());
        }
        System.out.println("----------");
        TicTacToeNode bestChild = node.getBestChild();
        TicTacToe.TicTacToeState bestChildState = (TicTacToe.TicTacToeState) bestChild.state();
        System.out.println(bestChildState.position().render());
        bestChild.explore();
        for (Node<TicTacToe> child: bestChild.children()) {
            TicTacToe.TicTacToeState s = (TicTacToe.TicTacToeState)child.state();
            System.out.println(s.position().render());
            System.out.println(child.wins()+" "+child.playouts());
        }
        assertEquals(2, node.getBestChild().wins(), 0.01);
    }
}