package connect4;

import core.Move;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    private Connect4State state;
    private MCTSNode parent;
    private List<MCTSNode> children;
    private int wins;
    private int visits;
    private Move<Connect4> move;

    public MCTSNode(Connect4State state, Move<Connect4> move) {
        this.state = state;
        this.parent = null;
        this.children = new ArrayList<>();
        this.wins = 0;
        this.visits = 0;
        this.move = move;
    }

    public Connect4State getState() {
        return state;
    }

    public MCTSNode getParent() {
        return parent;
    }

    public List<MCTSNode> getChildren() {
        return children;
    }

    public int getWins() {
        return wins;
    }

    public int getVisits() {
        return visits;
    }

    public Move<Connect4> getMove() {
        return move;
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementVisits() {
        this.visits++;
    }
}