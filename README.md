# Monte Carlo Tree Search

Monte Carlo Tree Search (MCTS) is a method for making decisions and solving search issues. The method creates a search tree of possible actions and outcomes, then explores it by simulating a number of games to discover the optimum choices. MCTS strikes a balance between exploring unknown nodes and exploiting those that have previously showed promise. The algorithm chooses the optimum move from the current game state based on the statistics gathered during the simulation phase.
The algorithm is implemented and is applied to TicTacToe and Connect Four.
Each game is implemented within its own package alongside MCTS code.


## TicTacToe

TicTacToe is a game with relatively small search space, therefore a pure MCTS performs surprisingly well. The game implementation is within `src/main/tictactoe`. The algorithm is testing against a random move agent and also itself. The tests are within `src/tests/tictactoe`.

## Connect Four
Connect Four is a game with larger search space (7x6) board, therefore the performance of pure MCTS is barely enough. It still performs better than a random agent but game-specific heuristics will be very beneficial. The game implementation is within `src/main/connect4`. The algorithm is testing against a random move agent and also itself. The tests are within `src/tests/connect4`.
[Link to full docs](https://docs.google.com/document/d/1rL6W3KgXEoc9OEvpDDeW7zdUFrFoe6NQ0GABFJJir7E/edit)
