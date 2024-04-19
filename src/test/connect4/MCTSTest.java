package connect4;

import core.Move;
import core.Node;
import core.State;
import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;
public class MCTSTest{
        @Test
        public void simulate() {
            Random rand = new Random();
            MCTSNode root = new MCTSNode(new Connect4(rand).new Connect4State());
            MCTS mcts = new MCTS(root);
            MCTSNode endNode = (MCTSNode) mcts.simulate(root);
            assertTrue(endNode.state().isTerminal());
        }

        @Test
        public void fullyExpanded() {
            MCTSNode root = new MCTSNode(new Connect4().new Connect4State());
            MCTS mcts = new MCTS(root);
            root.explore();
            for (Node<Connect4> child : root.children()) {
                MCTSNode endNode = (MCTSNode) mcts.simulate((MCTSNode) child);
                mcts.backpropagate((MCTSNode) child, endNode, root.state().player());
            }
            assertTrue(mcts.fullyExpanded(root));
        }

        @Test
        public void backpropagate() {
            MCTSNode root = new MCTSNode(new Connect4().new Connect4State());
            MCTS mcts = new MCTS(root);
            MCTSNode selectedNode = mcts.select(root);
            MCTSNode endNode = (MCTSNode) mcts.simulate(selectedNode);
            mcts.backpropagate(selectedNode, endNode, root.state().player());
            assertEquals(endNode.playouts(), root.playouts());
            assertEquals(endNode.wins(), root.wins());
        }

        @Test
        public void select() {
            MCTSNode root = new MCTSNode(new Connect4().new Connect4State());
            MCTS mcts = new MCTS(root);
            MCTSNode endNode = mcts.select(root);
            assertEquals(endNode, root);
        }

        @Test
        public void expand() {
            MCTSNode root = new MCTSNode(new Connect4().new Connect4State());
            MCTS mcts = new MCTS(root);
            mcts.expand(root);
            assertFalse(root.children().isEmpty());
        }

        @Test
        public void MCTSvsRandom() {
            MCTSNode root = new MCTSNode(new Connect4().new Connect4State());
            MCTS mcts = new MCTS(root);
            int randomPlayer = Connect4.BLUE;
            int mctsPlayer = Connect4.RED;
            while (!root.state().isTerminal()) {
                if (mctsPlayer == root.state().player()) {
                    root = mcts.getBestMove();
                    root = new MCTSNode(root.state());
                    mcts = new MCTS(root);
                } else {
                    Move<Connect4> randomMove = root.state().chooseMove(randomPlayer);
                    State<Connect4> newState = root.state().next(randomMove);
                    root = new MCTSNode(newState);
                    mcts = new MCTS(root);
                }
            }
            if (root.state().winner().isPresent()) {
                assertEquals((int) root.state().winner().get(), mctsPlayer);
            }
        }

        @Test
        public void MCTSvsMCTS() {
            Connect4 game = new Connect4();
            MCTSNode root = new MCTSNode(game.new Connect4State());
            MCTS mcts = new MCTS(root);
            while (!root.state().isTerminal()) {
                root = mcts.getBestMove();
                root = new MCTSNode(root.state());
                mcts = new MCTS(root);
            }
            assertFalse(root.state().winner().isPresent());
        }
    }

}