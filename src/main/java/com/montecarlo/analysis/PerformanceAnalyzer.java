package com.montecarlo.analysis;

import com.montecarlo.data.GameData;
import java.util.List;
import java.util.stream.Collectors;

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
 * Class: PerformanceAnalyzer
 *
 * This class provides tools to evaluate and analyze the overall performance of
 * the Monte Carlo engine across multiple simulations and training iterations.
 *
 * Key functionalities include:
 * - Aggregating and analyzing game results.
 * - Calculating performance metrics such as average win rate and average move quality.
 * - Generating summary reports and performance trends for insights and debugging.
 *
 * Dependencies:
 * - `GameData` for holding game-specific data to be analyzed.
 */

public class PerformanceAnalyzer {

    /**
     * Analyzes the overall performance of the engine by calculating the average
     * win rate across a series of games.
     *
     * @param games The list of GameData objects representing completed games.
     * @return The average win rate as a percentage.
     */
    public double analyzeOverallPerformance(List<GameData> games) {
        double winRate = calculateWinRate(games);
        System.out.println("Overall Win Rate: " + winRate + "%");
        return winRate;
    }

    /**
     * Calculates the win rate from a list of GameData objects.
     *
     * @param games The list of GameData objects.
     * @return The win rate as a percentage.
     */
    private double calculateWinRate(List<GameData> games) {
        long totalWins = games.stream().filter(game -> game.getResult().isWin()).count();
        return (double) totalWins / games.size() * 100;
    }

    /**
     * Calculates the average rating of moves across all games.
     *
     * @param games The list of GameData objects.
     * @return The average move rating.
     */
    public double calculateAverageMoveRating(List<GameData> games) {
        double totalScore = 0;
        int totalMoves = 0;

        for (GameData game : games) {
            totalScore += game.getMoveHistory().stream()
                .mapToDouble(move -> move.getScore())
                .sum();
            totalMoves += game.getMoveHistory().size();
        }

        double averageMoveRating = totalMoves > 0 ? totalScore / totalMoves : 0;
        System.out.println("Average Move Rating: " + averageMoveRating);
        return averageMoveRating;
    }

    /**
     * Analyzes the trend of performance over multiple games, identifying
     * improvements or declines.
     *
     * @param games The list of GameData objects.
     */
    public void analyzePerformanceTrend(List<GameData> games) {
        System.out.println("Analyzing performance trend over " + games.size() + " games:");

        List<Double> scores = games.stream()
            .map(game -> game.getMoveHistory().stream()
                .mapToDouble(move -> move.getScore())
                .average().orElse(0))
            .collect(Collectors.toList());

        for (int i = 0; i < scores.size(); i++) {
            System.out.println("Game " + (i + 1) + " - Average Score: " + scores.get(i));
        }
    }

    /**
     * Generates a summary report for overall performance metrics.
     *
     * @param games The list of GameData objects.
     */
    public void generateSummaryReport(List<GameData> games) {
        System.out.println("\nPerformance Summary Report:");
        double winRate = analyzeOverallPerformance(games);
        double averageMoveRating = calculateAverageMoveRating(games);
        System.out.println("Win Rate: " + winRate + "%");
        System.out.println("Average Move Rating: " + averageMoveRating);
    }
}
