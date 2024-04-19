package connect4;

import core.Move;
import core.Node;
import tictactoe.TicTacToeNode;

import java.util.Collection;
import java.util.Random;

public class MCTSConnect4 {
    private static final int SIMULATION_COUNT = 1000;
    private static final Random RANDOM = new Random();
    private final Connect4Node root;

    public MCTSConnect4(Connect4Node root) {
        this.root = root;
    }

    public Connect4Node getBestMove() {
        int currentPlayer = root.state().player();
        for (int i = 0; i < 1000; i++) {
            Connect4Node node = select(root);
            if (!node.isLeaf()) {
                expand(node);
            }
            Connect4Node nodeToExplore = node;
            if (!node.children().isEmpty())
                nodeToExplore = pickRandomChild(node.children());
            Node<Connect4> endNode = simulate(nodeToExplore);
            backpropagate(nodeToExplore, (Connect4Node) endNode, currentPlayer);
        }
        return (Connect4Node) root.getChildWithMaxWins();
    }

    void expand(Connect4Node node) {
        Connect4State currentState = (Connect4State) node.state();
        Collection<Move<Connect4>> possibleMoves = currentState.moves(currentState.player());
        for (Move<Connect4> possibleMove : possibleMoves) {
            node.addChild(currentState.next(possibleMove));
        }
    }

    private Connect4Node pickRandomChild(Collection<Node<Connect4>> children) {
        return (Connect4Node) children.stream().skip(root.state().random().nextInt(children.size())).findFirst().orElseThrow(() -> new RuntimeException("No children"));
    }

    public Connect4Node select(Connect4Node node) {
        while (!node.getChildren().isEmpty()) {
            node = (Connect4Node) node.getChildren().stream().max((node1, node2) -> Double.compare(uctValue((Connect4Node) node1), uctValue((Connect4Node) node2))).orElseThrow();
        }
        return node;
    }

    private double uctValue(Connect4Node node) {
        return node.wins() / (double) node.playouts() + Math.sqrt(2 * Math.log(node.getParent().wins()) / node.playouts());
    }

    public Node<Connect4> simulate(Connect4Node node) {
        while (!node.state().isTerminal()) {
            if (node.children().isEmpty())
                node.explore();
            node = select(node);
        }
        return node;
    }

    public boolean fullyExpanded(Connect4Node node) {
        // true if all the children have playouts > 0
        if (node.children().isEmpty()) {
            return false;
        }
        return node.children().stream().allMatch(child -> child.playouts() > 0);
    }

    public void backpropagate(Connect4Node node, Connect4Node endNode, int currentPlayer) {
        if (endNode.state().winner().isPresent()) {
            if (endNode.state().winner().get() == currentPlayer)
                endNode.updateStats(endNode.wins(), endNode.playouts());
            else
                endNode.updateStats(0, endNode.playouts());
        }
        node.updateStats(endNode.wins(), endNode.playouts());
        node = (Connect4Node) node.getParent();
        while (node != null) {
            node.backPropagate();
            node = (Connect4Node) node.getParent();
        }
    }
}
