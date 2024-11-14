package com.montecarlo.data;

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
 * Class: Move
 *
 * This class represents a single move in the game. It includes information about
 * the position from which the piece is moved, the target position, the piece itself,
 * and the evaluation score associated with this move.
 *
 * Key functionalities include:
 * - Storing the start and end positions of a move.
 * - Capturing the piece that was moved.
 * - Storing the evaluation score of the move.
 * - Providing methods for formatted output and access to move data.
 */
public class Move {

    private String piece;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private double evaluationScore;
    private GameData gameData; // New field for associated GameData

    /**
     * Constructor to create a Move object with detailed parameters.
     *
     * @param piece The piece being moved (e.g., "Knight", "Rook").
     * @param startX The starting x-coordinate of the move.
     * @param startY The starting y-coordinate of the move.
     * @param endX The ending x-coordinate of the move.
     * @param endY The ending y-coordinate of the move.
     * @param evaluationScore The evaluation score for this move.
     * @param gameData The associated GameData object for the move.
     */
    public Move(String piece, int startX, int startY, int endX, int endY, double evaluationScore, GameData gameData) {
        this.piece = piece;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.evaluationScore = evaluationScore;
        this.gameData = gameData; // Set the associated GameData
    }

    /**
     * Constructor to create a simpler Move object without a piece name and
     * evaluation.
     *
     * @param startX The starting x-coordinate of the move.
     * @param startY The starting y-coordinate of the move.
     * @param endX The ending x-coordinate of the move.
     * @param endY The ending y-coordinate of the move.
     */
    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.evaluationScore = 0; // Default to 0 if not provided
        this.piece = ""; // Empty by default if not provided
        this.gameData = null; // Default to null if not provided
    }

    // Getter and setter methods
    public String getPiece() {
        return piece;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public double getEvaluationScore() {
        return evaluationScore;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    // Added methods to access start and end row/column
    public int getStartRow() {
        return startY;
    }

    public int getStartCol() {
        return startX;
    }

    public int getEndRow() {
        return endY;
    }

    public int getEndCol() {
        return endX;
    }

    public double getScore() {
        return this.evaluationScore;
    }

    @Override
    public String toString() {
        return "Move{"
                + "piece='" + piece + '\''
                + ", from=(" + startX + ", " + startY + ")"
                + ", to=(" + endX + ", " + endY + ")"
                + ", evaluationScore=" + evaluationScore
                + '}';
    }
}
