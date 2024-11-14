package com.montecarlo.engine;

import com.montecarlo.data.GameData;
import com.montecarlo.data.Move;
import com.montecarlo.simulation.GameSimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class: MonteCarloEngine
 *
 * This class is responsible for managing the Monte Carlo Tree Search (MCTS)
 * algorithm for decision-making in the game. It simulates games, analyzes
 * potential moves, and decides the best move based on simulations.
 */
public class MonteCarloEngine {

    private static final int SIMULATION_COUNT = 1000;
    private Random random;
    private GameSimulator simulator;

    /**
     * Constructor to initialize the MonteCarloEngine with the required
     * components.
     */
    public MonteCarloEngine() {
        this.random = new Random();
        this.simulator = new GameSimulator();
    }

    /**
     * Runs the Monte Carlo simulations for the given game state to determine
     * the best move.
     *
     * @param gameData The current game state for which to evaluate moves.
     * @return The best move based on the results of the simulations.
     */
    public Move runSimulations(GameData gameData) {
        // Replace 'true' with logic to determine which player's turn it is if necessary
        boolean isWhiteTurn = determinePlayerTurn(gameData); // Implement this method if needed
        List<Move> possibleMoves = gameData.getLegalMoves(isWhiteTurn);

        if (possibleMoves.isEmpty()) {
            throw new IllegalStateException("No available moves to simulate.");
        }

        // Keep track of scores for each move.
        List<Integer> moveScores = new ArrayList<>();
        for (int i = 0; i < possibleMoves.size(); i++) {
            moveScores.add(0);
        }

        // Run simulations for each move.
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            int selectedMoveIndex = random.nextInt(possibleMoves.size());
            Move selectedMove = possibleMoves.get(selectedMoveIndex);

            // Create a MonteCarloNode for the selected move
            MonteCarloNode node = new MonteCarloNode(selectedMove, gameData, null); // Pass 'null' for parent if not needed

            // Simulate the game from this node and get the result.
            double result = simulator.simulateGame(node);
            if (result > 0) {
                moveScores.set(selectedMoveIndex, moveScores.get(selectedMoveIndex) + 1);
            }
        }

        // Find the best move based on the highest simulation score.
        int bestMoveIndex = 0;
        int highestScore = -1;
        for (int i = 0; i < moveScores.size(); i++) {
            if (moveScores.get(i) > highestScore) {
                bestMoveIndex = i;
                highestScore = moveScores.get(i);
            }
        }

        return possibleMoves.get(bestMoveIndex);
    }

    /**
     * Determines the current player's turn. This is a placeholder and should be
     * implemented based on your game's logic.
     *
     * @param gameData The current game state.
     * @return True if it's the white player's turn, false otherwise.
     */
    private boolean determinePlayerTurn(GameData gameData) {
        // Implement logic to check if it's white's turn
        // For now, returning true as a placeholder
        return true;
    }

    /**
     * Chooses the best move for the given game state and player based on Monte
     * Carlo simulations.
     *
     * @param gameData The current state of the game.
     * @param isWhiteTurn True if it's white's turn, false otherwise.
     * @return The chosen move or null if no valid moves are available.
     */
    public Move chooseMove(GameData gameData, boolean isWhiteTurn) {
        // Get all legal moves for the current player
        List<Move> possibleMoves = gameData.getLegalMoves(isWhiteTurn);

        if (possibleMoves.isEmpty()) {
            return null; // No valid moves available
        }

        // Run Monte Carlo simulations for each move to find the best one
        List<Integer> moveScores = new ArrayList<>();
        for (int i = 0; i < possibleMoves.size(); i++) {
            moveScores.add(0);
        }

        for (int i = 0; i < SIMULATION_COUNT; i++) {
            int selectedMoveIndex = random.nextInt(possibleMoves.size());
            Move selectedMove = possibleMoves.get(selectedMoveIndex);

            // Create a MonteCarloNode for the selected move
            MonteCarloNode node = new MonteCarloNode(selectedMove, gameData, null);

            // Simulate the game from this node and get the result
            double result = simulator.simulateGame(node);

            // Count wins for the current player
            if ((isWhiteTurn && result > 0.5) || (!isWhiteTurn && result < 0.5)) {
                moveScores.set(selectedMoveIndex, moveScores.get(selectedMoveIndex) + 1);
            }
        }

        // Find the move with the highest score
        int bestMoveIndex = 0;
        int highestScore = -1;
        for (int i = 0; i < moveScores.size(); i++) {
            if (moveScores.get(i) > highestScore) {
                bestMoveIndex = i;
                highestScore = moveScores.get(i);
            }
        }

        return possibleMoves.get(bestMoveIndex);
    }

}
