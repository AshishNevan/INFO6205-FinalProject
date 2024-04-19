package connect4;

import core.Move;

import java.util.*;

public class MCTS {
    private MCTSNode root;
    private static final int SIMULATION_COUNT = 1000;
    private static final Random RANDOM = new Random();

    public MCTS(Connect4State initialState) {
        this.root = new MCTSNode(initialState, null);
    }

    public Connect4Move findBestMove() {
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            MCTSNode node = select(root);
            int result = simulate(node);
            backPropagate(node, result);
        }

        // Check if the root node has children
        if (root.getChildren().isEmpty()) {
            // If not, expand the root node
            root.expand();
        }

        List<MCTSNode> children = root.getChildren();
        Optional<MCTSNode> bestNode = children.stream().max((node1, node2) -> Double.compare(node1.getWins() / (double) node1.getVisits(), node2.getWins() / (double) node2.getVisits()));
        if (bestNode.isPresent()) {
            Connect4Move bestMove = (Connect4Move) bestNode.get().getMove();
            return bestMove;
        } else {
            return null;
        }
    }

    private MCTSNode select(MCTSNode node) {
        while (!node.getChildren().isEmpty()) {
            node = node.getChildren().stream().max((node1, node2) -> Double.compare(uctValue(node1), uctValue(node2))).orElseThrow();
        }
        return node;
    }

    private double uctValue(MCTSNode node) {
        return node.getWins() / (double) node.getVisits() + Math.sqrt(2 * Math.log(node.getParent().getVisits()) / node.getVisits());
    }

    private int simulate(MCTSNode node) {
        Connect4State state = node.getState();
        while(!state.isTerminal()) {
            List<Move<Connect4>> moves = new ArrayList<>(state.moves(state.player()));
            state = (Connect4State) state.next(moves.get(RANDOM.nextInt(moves.size())));
            if (state.winner().isPresent()) {
                return state.winner().get();
            }
        }
        return state.winner().orElse(0);
    }

    private void backPropagate(MCTSNode node, int result) {
        while(node != null) {
            node.incrementVisits();
            if (node.getState().player() == result) {
                node.incrementWins();
            }
            node = node.getParent();
        }
    }
}
