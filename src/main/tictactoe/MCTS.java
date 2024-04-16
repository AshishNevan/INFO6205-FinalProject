package tictactoe;

import core.Move;
import core.Node;
import core.State;

import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    private final Node<TicTacToe> root;

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    public static void main(String[] args) {
        // MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        // Node<TicTacToe> root = mcts.root;
        // long end = System.currentTimeMillis()+1000;
        // while(System.currentTimeMillis() < end) {
        //     State<TicTacToe> leaf = mcts.runGame();
        //     root.explore();
        //     return root.bestChild();
        // }
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.new TicTacToeState());
        MCTS mcts = new MCTS(root);
        int user = 1 - game.opener();
        Scanner sc = new Scanner(System.in);
        System.out.println(((TicTacToe.TicTacToeState) root.state()).position().render());
//        while (!root.state().isTerminal()) {
//            if (root.state().player() == user) {
//                try {
//                    System.out.println("Enter X,Y");
//                    String userMove = sc.nextLine();
////                    System.out.println("User input is " + userMove);
//                    String[] XY = userMove.split(",");
////                    System.out.println(Arrays.toString(XY));
//                    Position newPosition = ((TicTacToe.TicTacToeState) root.state()).position().move(TicTacToe.O, Integer.parseInt(XY[0]), Integer.parseInt(XY[1]));
//                    root = new TicTacToeNode(game.new TicTacToeState(newPosition));
//                    mcts = new MCTS(root);
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

    public TicTacToeNode getBestMove() {
        for (int i = 0; i < 2000; i++) {
            TicTacToeNode node = select((TicTacToeNode) root);
            if (!node.isLeaf()) {
//                node.explore();
                node = expand(node);
            }
            State<TicTacToe> endState = simulate(node);
            TicTacToeNode endNode = new TicTacToeNode(endState);
            backpropagate(node, endNode);
        }

        return ((TicTacToeNode) root).getBestChild();
    }

    private TicTacToeNode select(TicTacToeNode node) {
        while (!node.isLeaf()) {
            if (!node.isFullyExpanded()) {
                return node;
            } else {
                node = node.getBestChild();
            }
        }
        return node;
    }

    private TicTacToeNode expand(TicTacToeNode node) {
        TicTacToe.TicTacToeState currenState = (TicTacToe.TicTacToeState) node.state();
        Collection<Move<TicTacToe>> possibleMoves = currenState.moves(currenState.player());
        node.addChild(currenState.next(currenState.moves(currenState.player()).stream().skip(new Random().nextInt(currenState.moves(currenState.player()).size())).findFirst().get()));
        return (TicTacToeNode) node.children().stream().reduce((first, second) -> second).orElse(null);
    }

    private State<TicTacToe> simulate(TicTacToeNode node) {
        TicTacToe.TicTacToeState state = (TicTacToe.TicTacToeState) node.state();
        while (!state.isTerminal()) {
            state = (TicTacToe.TicTacToeState) state.next(state.moves(state.player()).stream().skip(new Random().nextInt(state.moves(state.player()).size())).findFirst().get());
        }
        return state;
    }

    private void backpropagate(TicTacToeNode parent, TicTacToeNode child) {

        child.backPropagate();
        parent.backPropagate();
    }

}