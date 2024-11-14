package com.montecarlo.analysis;

import com.montecarlo.data.GameData;
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
 * Class: GameAnalyzer
 *
 * This class is responsible for analyzing completed games and extracting useful
 * statistics and insights. It provides methods for evaluating individual games,
 * calculating metrics, and generating performance reports.
 *
 * Key functionalities include:
 * - Analyzing a single game to extract critical data.
 * - Calculating win rates and performance trends across multiple games.
 * - Generating detailed game reports.
 *
 * Dependencies:
 * - `GameData` for holding game-specific data for analysis.
 */

public class GameAnalyzer {

    /**
     * Analyzes a single game and extracts data on moves and game flow.
     *
     * @param gameData The GameData object containing moves and game state to analyze.
     */
    public void analyzeGame(GameData gameData) {
        // Analyze the game move-by-move and extract key insights.
        System.out.println("Analyzing game ID: " + gameData.getGameId());

        // Iterate through the moves in the game and log critical moments.
        gameData.getMoveHistory().forEach(move -> {
            System.out.println("Move: " + move);
            // Analyze specific characteristics of each move here.
        });
    }

    /**
     * Calculates the win rate based on a list of completed games.
     *
     * @param games The list of GameData objects representing completed games.
     * @return The win rate as a percentage.
     */
    public double calculateWinRate(List<GameData> games) {
        int wins = 0;
        int totalGames = games.size();

        for (GameData game : games) {
            if (game.getResult().isWin()) {
                wins++;
            }
        }

        return totalGames > 0 ? ((double) wins / totalGames) * 100 : 0;
    }

    /**
     * Generates a detailed report for a specific game.
     *
     * @param gameData The GameData object for which to generate the report.
     * @return A formatted report as a string.
     */
    public String generateGameReport(GameData gameData) {
        StringBuilder report = new StringBuilder();
        report.append("Game Report for Game ID: ").append(gameData.getGameId()).append("\n");
        report.append("Moves: \n");

        gameData.getMoveHistory().forEach(move -> {
            report.append(move).append("\n");
        });

        report.append("Game Result: ").append(gameData.getResult().toString()).append("\n");
        return report.toString();
    }

    /**
     * Analyzes the performance trend over a series of games.
     *
     * @param games The list of GameData objects representing the games played.
     */
    public void analyzePerformanceTrend(List<GameData> games) {
        // Analyze and log performance trends, e.g., average score improvement.
        System.out.println("Analyzing performance trend across games.");
        games.forEach(game -> {
            System.out.println("Game ID: " + game.getGameId() + " Result: " + game.getResult());
        });
    }
}
