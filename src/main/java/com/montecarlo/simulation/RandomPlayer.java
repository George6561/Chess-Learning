package com.montecarlo.simulation;

import com.montecarlo.data.GameData;
import com.montecarlo.data.Move;
import java.util.List;
import java.util.Random;

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
 * Class: RandomPlayer
 *
 * This class represents a player that plays random moves during a game.
 * It is typically used in simulations and Monte Carlo-based learning to model
 * potential game outcomes without advanced strategy.
 *
 * Key functionalities include:
 * - Selecting and playing random moves based on the current game state.
 *
 * Dependencies:
 * - `GameData` for representing the game state and retrieving legal moves.
 * - `Move` for representing the moves made by the player.
 */

public class RandomPlayer {

    private final Random random;

    /**
     * Constructor for RandomPlayer.
     * Initializes the random number generator used for selecting moves.
     */
    public RandomPlayer() {
        this.random = new Random();
    }

    /**
     * Chooses a random valid move from the current game state.
     *
     * @param gameData The current state of the game.
     * @param isWhiteTurn True if it's the white player's turn, false otherwise.
     * @return A randomly chosen move from the list of legal moves, or null if no moves are available.
     */
    public Move chooseRandomMove(GameData gameData, boolean isWhiteTurn) {
        List<Move> legalMoves = gameData.getLegalMoves(isWhiteTurn);
        if (legalMoves.isEmpty()) {
            return null; // No valid moves available (e.g., game over).
        }
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    /**
     * Plays a random move by modifying the game state with the selected move.
     *
     * @param gameData The current state of the game to be modified.
     * @param isWhiteTurn True if it's the white player's turn, false otherwise.
     */
    public void playRandomMove(GameData gameData, boolean isWhiteTurn) {
        Move randomMove = chooseRandomMove(gameData, isWhiteTurn);
        if (randomMove != null) {
            gameData.applyMove(randomMove);
        }
    }
}
