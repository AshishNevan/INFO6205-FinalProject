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

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.new TicTacToeState());
        MCTS mcts = new MCTS(root);
//        int user = 1 - game.opener();
//        Scanner sc = new Scanner(System.in);
//        System.out.println(((TicTacToe.TicTacToeState) root.state()).position().render());
//        while (!root.state().isTerminal()) {
//            if (root.state().player() == user) {
//                try {
//                    System.out.println("Enter X,Y");
//                    String userMove = sc.nextLine();
////                    System.out.println("User input is " + userMove);
//                    String[] XY = userMove.split(",");
//                    Position newPosition = ((TicTacToe.TicTacToeState) root.state()).position().move(TicTacToe.O, Integer.parseInt(XY[0]), Integer.parseInt(XY[1]));
//                    Node<TicTacToe> childNode = findChildWithPosition(root, newPosition);
//                    System.out.println(childNode.state().toString());
//                    root = (TicTacToeNode) childNode;
//                } catch (Exception e) {
//                    System.out.println(e);
//                    System.out.println("Enter valid input");
//                }
//            } else {
//                root = mcts.getBestMove();
//                System.out.println("Player " + (root.state().player() == 0 ? "X" : "O") + " makes a move.");
//                System.out.println(((TicTacToe.TicTacToeState) root.state()).position().render());
//                mcts = new MCTS(root);
//            }
//        }

        while (!root.state().isTerminal()) {
            root = mcts.getBestMove();
            System.out.println("Player " + (root.state().player() == 0 ? "X" : "O") + " makes a move.");
            System.out.println(((TicTacToe.TicTacToeState) root.state()).position().render());
            root = new TicTacToeNode(root.state());
            mcts = new MCTS(root);
        }

        if (root.state().winner().isPresent()) {
            String res;
            switch (root.state().winner().get()) {
                case TicTacToe.X -> res = "X";
                case TicTacToe.O -> res = "O";
                default -> res = ".";
            }
            System.out.println("TicTacToe: winner is: " + res);
        } else {
            System.out.println("Draw");
        }
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

    private TicTacToeNode UnvisitedChild(TicTacToeNode node) {
        // return first child with playouts == 0
        if (node.children().isEmpty()) {
            expand(node);
            return node;
        }
        return (TicTacToeNode) node.children().stream().filter(child -> child.playouts() == 0).findFirst().orElseThrow(() -> new RuntimeException("No unvisited children"));
    }

    public boolean fullyExpanded(TicTacToeNode node) {
        // true if all the children have playouts > 0
        if (node.children().isEmpty()) {
            return false;
        }
        return node.children().stream().allMatch(child -> child.playouts() > 0);
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