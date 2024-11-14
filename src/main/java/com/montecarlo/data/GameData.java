package com.montecarlo.data;

import com.chess.board.ChessBoard;
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
 * Class: GameData
 *
 * This class is responsible for storing data about a single game session.
 * It captures the move history, the result of the game, and any other relevant
 * metrics or statistics that may be used in performance analysis or simulation.
 *
 * Key functionalities include:
 * - Storing and managing a list of moves with their evaluations.
 * - Recording the final result of the game (win, loss, draw).
 * - Providing access to the move history for analysis.
 *
 * Dependencies:
 * - `Move` class to represent each move and its associated score.
 */
public class GameData {

    private int gameId; // Added field for game ID
    private List<Move> moveHistory;
    private GameResult result;
    private ChessBoard chessBoard;

    /**
     * Constructor with a game ID.
     *
     * @param gameId The ID of the game.
     */
    public GameData(int gameId) {
        this.gameId = gameId;
        this.chessBoard = new ChessBoard(); // Initialize the chess board
        this.moveHistory = new ArrayList<>();
        this.result = GameResult.ONGOING; // Default result
    }

    /**
     * No-argument constructor.
     */
    public GameData() {
        this.chessBoard = new ChessBoard(); // Initialize the chess board
        this.moveHistory = new ArrayList<>();
        this.result = GameResult.ONGOING; // Default result
    }

    /**
     * Gets the game ID.
     *
     * @return The game ID.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the game ID.
     *
     * @param gameId The ID of the game.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Adds a move to the move history.
     *
     * @param move The move to be added.
     */
    public void addMove(Move move) {
        this.moveHistory.add(move);
    }

    /**
     * Gets the move history of the game.
     *
     * @return A list of moves representing the move history.
     */
    public List<Move> getMoveHistory() {
        return new ArrayList<>(this.moveHistory);
    }

    /**
     * Sets the result of the game.
     *
     * @param result The result of the game (WIN, LOSS, DRAW).
     */
    public void setResult(GameResult result) {
        this.result = result;
    }

    /**
     * Gets the result of the game.
     *
     * @return The result of the game (WIN, LOSS, DRAW).
     */
    public GameResult getResult() {
        return this.result;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return getLegalMoves(true).isEmpty() && getLegalMoves(false).isEmpty();
    }

    /**
     * Gets the legal moves for the current player.
     *
     * @param isWhite True if checking for white's moves, false otherwise.
     * @return A list of legal moves for the player.
     */
    public List<Move> getLegalMoves(boolean isWhite) {
        List<int[]> rawMoves = chessBoard.getAllLegalMoves(isWhite ? ChessBoard.Player.WHITE : ChessBoard.Player.BLACK);
        List<Move> legalMoves = new ArrayList<>();
        for (int[] rawMove : rawMoves) {
            legalMoves.add(new Move(rawMove[0], rawMove[1], rawMove[2], rawMove[3]));
        }
        return legalMoves;
    }

    /**
     * Applies a move to the current game state.
     *
     * @param move The move to apply.
     */
    public void applyMove(Move move) {
        chessBoard.movePiece(move.getStartRow(), move.getStartCol(), move.getEndRow(), move.getEndCol());
    }

    /**
     * Checks if the game is a win for the white side.
     *
     * @return True if white has won, false otherwise.
     */
    public boolean isWinForWhite() {
        // Implement a method to check if black is checkmated
        return chessBoard.isCheckmate(ChessBoard.Player.BLACK);
    }

    /**
     * Checks if the game is a win for the black side.
     *
     * @return True if black has won, false otherwise.
     */
    public boolean isWinForBlack() {
        // Implement a method to check if white is checkmated
        return chessBoard.isCheckmate(ChessBoard.Player.WHITE);
    }

    @Override
    public GameData clone() {
        try {
            return (GameData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should not happen, since we implement Cloneable
        }
    }

    /**
     * Provides a string representation of the game data for debugging and
     * analysis.
     *
     * @return A formatted string with the move history and the game result.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game ID: ").append(this.gameId).append("\n");
        sb.append("Game Result: ").append(this.result).append("\n");
        sb.append("Move History:\n");
        for (Move move : this.moveHistory) {
            sb.append(move).append("\n");
        }
        return sb.toString();
    }
}
