package tictactoe;

import core.Move;
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
        int RUNS = 100;
        int[] results = new int[RUNS];
        int randomPlayer = TicTacToe.O;
        int mctsPlayer = TicTacToe.X;
        for (int i = 0; i < RUNS; i++) {
            TicTacToeNode root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
            MCTS mcts = new MCTS(root);
            while (!root.state().isTerminal()) {
                if (mctsPlayer == root.state().player()) {
                    root = mcts.getBestMove();
                    root = new TicTacToeNode(root.state());
                } else {
                    Move<TicTacToe> randomMove = root.state().chooseMove(randomPlayer);
                    State<TicTacToe> newState = root.state().next(randomMove);
                    root = new TicTacToeNode(newState);
                }
                mcts = new MCTS(root);
            }
            results[i] = root.state().winner().orElse(-1);
        }
        // count occurances of mctsplayer in results array
        long loss = java.util.Arrays.stream(results).filter(x -> x == TicTacToe.O ).count();
        // mctsPlayer should win or draw more than 50% of the time
        System.out.println("MCTS vs Random: " + (RUNS - loss)*100/RUNS + "% wins + draws");
        assertTrue(RUNS - loss > 0.5 * RUNS);
    }

    @Test
    public void MCTSvsMCTS() {
        int RUNS = 100;
        int[] results = new int[RUNS];
        for (int i = 0; i < RUNS; i++) {
            TicTacToe game = new TicTacToe();
            TicTacToeNode root = new TicTacToeNode(game.new TicTacToeState());
            MCTS mcts = new MCTS(root);
            while (!root.state().isTerminal()) {
                root = mcts.getBestMove();
                root = new TicTacToeNode(root.state());
                mcts = new MCTS(root);
            }
            results[i] = root.state().winner().orElse(-1);
        }
        // occurances of -1 (draw) in results array
        long draws = java.util.Arrays.stream(results).filter(x -> x == -1 ).count();
        // MCTS vs MCTS should always end in a draw
        assertEquals(draws, RUNS, 0.05 * RUNS);
    }
}