package tictactoe;

import core.Node;
import core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    private final Node<TicTacToe> parent;
    private int wins;
    private int playouts;

    public TicTacToeNode(State<TicTacToe> state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData(1-state.player());
        parent = null;
    }

    public TicTacToeNode(State<TicTacToe> state, Node<TicTacToe> parent, int currentPlayer) {
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
    public State<TicTacToe> state() {
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
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(State<TicTacToe> state) {
        children.add(new TicTacToeNode(state, this, state.player()));
    }

    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<TicTacToe> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    public void updateStats(int wins, int playouts) {
        this.wins = wins;
        this.playouts = playouts;
    }

    public Node<TicTacToe> getChildWithMaxWins() {
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

    public Node<TicTacToe> getParent() {
        return parent;
    }
}