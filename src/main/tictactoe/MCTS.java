package tictactoe;

import core.Move;
import core.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    private final Node<TicTacToe> root;

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    public static double uctValue(
            int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public TicTacToeNode getBestMove() {
        int currentPlayer = root.state().player();
        for (int i = 0; i < 1000; i++) {
            TicTacToeNode node = select((TicTacToeNode) root);
            if (!node.isLeaf()) {
                expand(node);
            }
            TicTacToeNode nodeToExplore = node;
            if (!node.children().isEmpty())
                nodeToExplore = pickRandomChild(node.children());
            Node<TicTacToe> endNode = simulate(nodeToExplore);
            backpropagate(nodeToExplore, (TicTacToeNode) endNode, currentPlayer);
        }
        return (TicTacToeNode) ((TicTacToeNode) root).getChildWithMaxWins();
    }

    public TicTacToeNode select(TicTacToeNode node) {
        while (!node.children().isEmpty()) {
            node = getBestChild(node);
        }
        return node;
    }

    public TicTacToeNode getBestChild(TicTacToeNode node) {
        return (TicTacToeNode) Collections.max(
                node.children(),
                Comparator.comparing(c -> uctValue(node.playouts(),
                        c.wins(), c.playouts())));
    }

    public void expand(TicTacToeNode node) {
        TicTacToe.TicTacToeState currentState = (TicTacToe.TicTacToeState) node.state();
        Collection<Move<TicTacToe>> possibleMoves = currentState.moves(currentState.player());
        for (Move<TicTacToe> possibleMove : possibleMoves) {
            node.addChild(currentState.next(possibleMove));
        }
    }

    private TicTacToeNode pickRandomChild(Collection<Node<TicTacToe>> children) {
        return (TicTacToeNode) children.stream().skip(root.state().random().nextInt(children.size())).findFirst().orElseThrow(() -> new RuntimeException("No children"));
    }

    public Node<TicTacToe> simulate(TicTacToeNode node) {
        while (!node.state().isTerminal()) {
            if (node.children().isEmpty())
                node.explore();
            node = getBestChild(node);
        }
        return node;
    }

    public void backpropagate(TicTacToeNode node, TicTacToeNode endNode, int currentPlayer) {
        if (endNode.state().winner().isPresent()) {
            if (endNode.state().winner().get() == currentPlayer)
                endNode.updateStats(endNode.wins(), endNode.playouts());
            else
                endNode.updateStats(0, endNode.playouts());
        }
        node.updateStats(endNode.wins(), endNode.playouts());
        node = (TicTacToeNode) node.getParent();
        while (node != null) {
            node.backPropagate();
            node = (TicTacToeNode) node.getParent();
        }
    }

}