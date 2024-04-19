package connect4;

import core.Node;
import core.State;

import java.util.*;

public class Connect4Node implements Node<Connect4> {

    private final State<Connect4> state;
    private final ArrayList<Node<Connect4>> children;
    private final Node<Connect4> parent;
    private int wins;
    private int playouts;

    public Connect4Node(State<Connect4> state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData(1-state.player());
        parent = null;
    }

    public Connect4Node(State<Connect4> state, Node<Connect4> parent, int currentPlayer) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData(currentPlayer);
        this.parent = parent;
    }

    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * @return the State of the Game G that this Node represents.
     */
    public State<Connect4> state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * Method to yield the children of this Node.
     *
     * @return a Collection of Nodes.
     */
    @Override
    public Collection<Node<Connect4>> children() {
        return children;
    }

    /**
     * @return the children of this Node.
     */

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(State<Connect4> state) {
        children.add(new Connect4Node(state, this, state.player()));
    }

    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<Connect4> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    public void updateStats(int wins, int playouts) {
        this.wins = wins;
        this.playouts = playouts;
    }

    public Node<Connect4> getChildWithMaxWins() {
//        return child with max wins
        return children.stream().max(Comparator.comparingInt(Node::wins)).orElse(null);
    }

    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public int wins() {
        return wins;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int playouts() {
        return playouts;
    }

    private void initializeNodeData(int currentPlayer) {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            //                wins = 2; // CONSIDER check that the winner is the correct player. We shouldn't need to.
            // a draw.
            wins = winner.map(integer -> integer == currentPlayer ? 2 : 0).orElse(1);
        }
    }

    public Node<Connect4> getParent() {
        return parent;
    }

    public Collection<Node<Connect4>> getChildren() {
        return children;
    }
}