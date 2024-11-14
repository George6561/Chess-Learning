package com.montecarlo.simulation;

import com.montecarlo.data.GameData;
import com.montecarlo.data.Move;
import com.montecarlo.engine.MonteCarloNode;
import java.util.Random;
import java.util.List;

/*
 * Copyright (c) 2024
 * George Miller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * ----------------------------------------------------------------------------
 *
 * Class: GameSimulator
 *
 * This class handles the simulation of a game from any given state for the Monte Carlo Tree Search (MCTS).
 * It plays random moves from a specific node until an outcome is reached.
 *
 * Key functionalities include:
 * - Simulating games from a given node until a terminal state is reached.
 * - Returning the outcome of the simulated game for backpropagation purposes.
 *
 * Dependencies:
 * - `MonteCarloNode` for accessing the current game state during simulations.
 * - `GameData` for representing game states and retrieving possible moves.
 * - `Move` for representing individual moves within the simulation.
 */

public class GameSimulator {

    private static final Random random = new Random();

    /**
     * Simulates a game starting from the given node by playing random moves
     * until a terminal state (win, loss, or draw) is reached.
     *
     * @param startNode The node representing the starting point of the simulation.
     * @return The result of the simulated game: 1.0 for win, 0.0 for loss, and 0.5 for draw.
     */
    public double simulateGame(MonteCarloNode startNode) {
        GameData currentGameState = startNode.getMove().getGameData().clone();
        boolean isCurrentPlayerWhite = true; // Alternate between players for simulation.

        while (!currentGameState.isGameOver()) {
            Move randomMove = selectRandomMove(currentGameState, isCurrentPlayerWhite);
            if (randomMove == null) {
                // No available moves: game is over.
                break;
            }
            currentGameState.applyMove(randomMove);
            isCurrentPlayerWhite = !isCurrentPlayerWhite;
        }

        return evaluateGameOutcome(currentGameState, isCurrentPlayerWhite);
    }

    /**
     * Selects a random move from the current game state for the given player.
     *
     * @param gameState The current state of the game.
     * @param isWhiteTurn True if it's the white player's turn, false otherwise.
     * @return A randomly selected valid move, or null if no moves are available.
     */
    private Move selectRandomMove(GameData gameState, boolean isWhiteTurn) {
        List<Move> legalMoves = gameState.getLegalMoves(isWhiteTurn);
        if (legalMoves.isEmpty()) {
            return null;
        }
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    /**
     * Evaluates the outcome of the game after the simulation ends.
     *
     * @param gameState The final state of the game after simulation.
     * @param isWhiteTurn True if it was white's turn at the end, false otherwise.
     * @return The result: 1.0 for a win, 0.0 for a loss, and 0.5 for a draw.
     */
    private double evaluateGameOutcome(GameData gameState, boolean isWhiteTurn) {
        if (gameState.isWinForWhite()) {
            return isWhiteTurn ? 0.0 : 1.0; // If it's white's turn and game over, black won.
        } else if (gameState.isWinForBlack()) {
            return isWhiteTurn ? 1.0 : 0.0; // If it's black's turn and game over, white won.
        } else {
            return 0.5; // Draw scenario.
        }
    }
}
