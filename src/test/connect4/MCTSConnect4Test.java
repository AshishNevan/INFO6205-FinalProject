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
            int RUNS = 10;
            int randomPlayer = Connect4.BLUE;
            int mctsPlayer = Connect4.RED;
            int[] res = new int[RUNS];
            for (int i = 0; i < RUNS; i++) {
                Connect4Node root = new Connect4Node(new Connect4State());
                MCTSConnect4 mcts = new MCTSConnect4(root);
                while (!root.state().isTerminal()) {
                    if (mctsPlayer == root.state().player()) {
                        root = mcts.getBestMove();
                        root = new Connect4Node(root.state());
                        mcts = new MCTSConnect4(root);
//                        System.out.println("mcts made move");
                    } else {
                        Move<Connect4> randomMove = root.state().chooseMove(randomPlayer);
                        State<Connect4> newState = root.state().next(randomMove);
                        root = new Connect4Node((Connect4State) newState);
                        mcts = new MCTSConnect4(root);
//                        System.out.println("random made move");
                    }
//                    root.state().printBoard();
                }
                res[i] = root.state().winner().orElse(-1);
            }
            // count occurances of mctsplayer in res
            long loss = java.util.Arrays.stream(res).filter(x -> x == randomPlayer).count();
            System.out.println("MCTSvsRandom wins+draws rate: " + (RUNS - loss)*100/RUNS);
            assertTrue(RUNS-loss>=0.3*RUNS);
        }

        @Test
        public void MCTSvsMCTS() {
            int RUNS = 10;
            int mctsPlayer = Connect4.RED;
            int[] res = new int[RUNS];
            for (int i = 0; i < RUNS; i++) {
                Connect4 game = new Connect4();
                Connect4Node root = new Connect4Node(new Connect4State());
                MCTSConnect4 mcts = new MCTSConnect4(root);
                while (!root.state().isTerminal()) {
                    root = mcts.getBestMove();
                    root = new Connect4Node(root.state());
                    mcts = new MCTSConnect4(root);
                }
                res[i] = root.state().winner().orElse(-1);
            }
            // count occurances of true in res
            long count = java.util.Arrays.stream(res).filter(x -> x == -1).count();
            System.out.println("MCTSvsMCTS draws rate: "+ count*100/RUNS);
//            System.out.println("Draws: " + count);
            assertEquals(RUNS, count, 0.5*RUNS);
        }
    }