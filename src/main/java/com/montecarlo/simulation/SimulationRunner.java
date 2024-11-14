package com.montecarlo.simulation;

import com.montecarlo.data.GameData;
import com.montecarlo.data.Move;
import com.montecarlo.engine.MonteCarloEngine;
import java.util.ArrayList;
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
 * Class: SimulationRunner
 *
 * The SimulationRunner class manages the execution of Monte Carlo simulations
 * for games. It runs multiple games using random or strategic players to gather
 * data and analyze the potential outcomes.
 *
 * Key functionalities include:
 * - Running multiple simulations to gather data.
 * - Collecting results for analysis and performance tracking.
 * - Managing different types of players (e.g., random or strategic) during simulations.
 *
 * Dependencies:
 * - `GameData` for representing and manipulating the game state.
 * - `MonteCarloEngine` for decision-making using the Monte Carlo Tree Search (MCTS) algorithm.
 * - `RandomPlayer` for generating random moves during simulations.
 */

public class SimulationRunner {

    private final MonteCarloEngine monteCarloEngine;
    private final RandomPlayer randomPlayer;

    /**
     * Constructor for SimulationRunner.
     * Initializes the Monte Carlo engine and random player.
     */
    public SimulationRunner() {
        this.monteCarloEngine = new MonteCarloEngine();
        this.randomPlayer = new RandomPlayer();
    }

    /**
     * Runs a simulation of a single game using the Monte Carlo Engine and a random player.
     *
     * @param maxTurns The maximum number of turns allowed for the simulation.
     * @param verbose  If true, outputs details of each move to the console.
     * @return A list of moves representing the game played during the simulation.
     */
    public List<Move> runSimulation(int maxTurns, boolean verbose) {
        GameData gameData = new GameData();
        List<Move> gameMoves = new ArrayList<>();
        boolean isWhiteTurn = true;

        for (int turn = 0; turn < maxTurns; turn++) {
            if (gameData.isGameOver()) {
                if (verbose) {
                    System.out.println("Game over detected. Ending simulation.");
                }
                break;
            }

            Move selectedMove;
            if (isWhiteTurn) {
                selectedMove = monteCarloEngine.chooseMove(gameData, true);
            } else {
                selectedMove = randomPlayer.chooseRandomMove(gameData, false);
            }

            if (selectedMove == null) {
                if (verbose) {
                    System.out.println("No valid moves available. Ending simulation.");
                }
                break;
            }

            // Apply the selected move
            gameData.applyMove(selectedMove);
            gameMoves.add(selectedMove);

            if (verbose) {
                System.out.println("Turn " + (turn + 1) + " (" + (isWhiteTurn ? "White" : "Black") + "): " + selectedMove);
            }

            // Toggle the turn
            isWhiteTurn = !isWhiteTurn;
        }

        return gameMoves;
    }

    /**
     * Runs multiple simulations and returns the results of each game.
     *
     * @param numSimulations The number of simulations to run.
     * @param maxTurns       The maximum number of turns for each simulation.
     * @param verbose        If true, outputs details of each simulation to the console.
     * @return A list containing the results of each simulation.
     */
    public List<List<Move>> runMultipleSimulations(int numSimulations, int maxTurns, boolean verbose) {
        List<List<Move>> allGamesResults = new ArrayList<>();

        for (int i = 0; i < numSimulations; i++) {
            if (verbose) {
                System.out.println("Running simulation " + (i + 1) + "...");
            }
            List<Move> gameResult = runSimulation(maxTurns, verbose);
            allGamesResults.add(gameResult);
            if (verbose) {
                System.out.println("Simulation " + (i + 1) + " completed.\n");
            }
        }

        return allGamesResults;
    }
}
