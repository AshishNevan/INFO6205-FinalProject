package tictactoe;

import core.Move;
import core.Node;
import core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

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
        children.add(new TicTacToeNode(state));
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

    public TicTacToeNode getBestChild() {
        return (TicTacToeNode) children.stream()
                .max(Comparator.comparing(c -> c.wins() / (c.playouts()+epsilon) + Math.sqrt(Math.log(c.playouts()+1) / (c.playouts() + epsilon ))))
                .orElseThrow(() -> new RuntimeException("No children"));
    }

    public boolean isFullyExpanded() {
        TicTacToe.TicTacToeState s = (TicTacToe.TicTacToeState) state;
        return children.size() == s.moves(s.player()).size();
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

    public TicTacToeNode(State<TicTacToe> state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData();
    }

    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent())
                wins = 2; // CONSIDER check that the winner is the correct player. We shouldn't need to.
            else
                wins = 1; // a draw.
        }
    }

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;

    private int wins;
    private int playouts;
    static double epsilon = 1e-6;
}