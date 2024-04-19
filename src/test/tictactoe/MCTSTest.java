package tictactoe;

import core.Move;
import core.Node;
import core.State;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MCTSTest {
    @Test
    public void simulate() {
        Random rand = new Random();
        TicTacToeNode root = new TicTacToeNode(new TicTacToe(rand).new TicTacToeState());
        MCTS mcts = new MCTS(root);
        TicTacToeNode endNode = (TicTacToeNode) mcts.simulate(root);
        assertTrue(endNode.state().isTerminal());
    }

    @Test
    public void fullyExpanded() {
        TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        MCTS mcts = new MCTS(root);
        root.explore();
        for (Node<TicTacToe> child : root.children()) {
            TicTacToeNode endNode = (TicTacToeNode) mcts.simulate((TicTacToeNode) child);
            mcts.backpropagate((TicTacToeNode) child, endNode, root.state().player());
        }
        assertTrue(mcts.fullyExpanded(root));
    }

    @Test
    public void backpropagate() {
        TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        MCTS mcts = new MCTS(root);
        TicTacToeNode selectedNode = mcts.select(root);
        TicTacToeNode endNode = (TicTacToeNode) mcts.simulate(selectedNode);
        mcts.backpropagate(selectedNode, endNode, root.state().player());
        assertEquals(endNode.playouts(), root.playouts());
        assertEquals(endNode.wins(), root.wins());
    }

    @Test
    public void select() {
        TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        MCTS mcts = new MCTS(root);
        TicTacToeNode endNode = mcts.select(root);
        assertEquals(endNode, root);
    }

    @Test
    public void expand() {
        TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        MCTS mcts = new MCTS(root);
        mcts.expand(root);
        assertFalse(root.children().isEmpty());
    }

    @Test
    public void MCTSvsRandom() {
        TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        MCTS mcts = new MCTS(root);
        int randomPlayer = TicTacToe.O;
        int mctsPlayer = TicTacToe.X;
        while (!root.state().isTerminal()) {
            if (mctsPlayer == root.state().player()) {
                root = mcts.getBestMove();
                root = new TicTacToeNode(root.state());
                mcts = new MCTS(root);
            } else {
                Move<TicTacToe> randomMove = root.state().chooseMove(randomPlayer);
                State<TicTacToe> newState = root.state().next(randomMove);
                root = new TicTacToeNode(newState);
                mcts = new MCTS(root);
            }
        }
        if (root.state().winner().isPresent()) {
            assertEquals((int) root.state().winner().get(), mctsPlayer);
        }
    }

    @Test
    public void MCTSvsMCTS() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.new TicTacToeState());
        MCTS mcts = new MCTS(root);
        while (!root.state().isTerminal()) {
            root = mcts.getBestMove();
            root = new TicTacToeNode(root.state());
            mcts = new MCTS(root);
        }
        assertFalse(root.state().winner().isPresent());
    }
}