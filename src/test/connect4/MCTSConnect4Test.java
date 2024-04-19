package connect4;

import core.Move;
import core.Node;
import core.State;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;
public class MCTSConnect4Test {
        @Test
        public void simulate() {
            Random rand = new Random();
            Connect4 game = new Connect4(rand);
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            Connect4Node endNode = (Connect4Node) mcts.simulate(root);
            assertTrue(endNode.state().isTerminal());
        }

        @Test
        public void fullyExpanded() {
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            root.explore();
            for (Node<Connect4> child : root.children()) {
                Connect4Node endNode = (Connect4Node) mcts.simulate((Connect4Node) child);
                mcts.backpropagate((Connect4Node) child, endNode, root.state().player());
            }
            assertTrue(mcts.fullyExpanded(root));
        }

        @Test
        public void backpropagate() {
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            Connect4Node selectedNode = mcts.select(root);
            Connect4Node endNode = (Connect4Node) mcts.simulate(selectedNode);
            mcts.backpropagate(selectedNode, endNode, root.state().player());
            assertEquals(endNode.playouts(), root.playouts());
            assertEquals(endNode.wins(), root.wins());
        }

        @Test
        public void select() {
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            Connect4Node endNode = mcts.select(root);
            assertEquals(endNode, root);
        }

        @Test
        public void expand() {
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            mcts.expand(root);
            assertFalse(root.children().isEmpty());
        }

        @Test
        public void MCTSvsRandom() {
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            int randomPlayer = Connect4.BLUE;
            int mctsPlayer = Connect4.RED;
            while (!root.state().isTerminal()) {
                if (mctsPlayer == root.state().player()) {
                    root = mcts.getBestMove();
                    root = new Connect4Node(root.state());
                    mcts = new MCTSConnect4(root);
                } else {
                    Move<Connect4> randomMove = root.state().chooseMove(randomPlayer);
                    State<Connect4> newState = root.state().next(randomMove);
                    root = new Connect4Node(newState);
                    mcts = new MCTSConnect4(root);
                }
            }
            if (root.state().winner().isPresent()) {
                assertEquals((int) root.state().winner().get(), mctsPlayer);
            }
        }

        @Test
        public void MCTSvsMCTS() {
            Connect4 game = new Connect4();
            Connect4Node root = new Connect4Node(new Connect4State());
            MCTSConnect4 mcts = new MCTSConnect4(root);
            while (!root.state().isTerminal()) {
                root = mcts.getBestMove();
                ((Connect4State)root.state()).printBoard();
                root = new Connect4Node(root.state());
                mcts = new MCTSConnect4(root);
            }
//            assertFalse(root.state().winner().isPresent());
        }
    }