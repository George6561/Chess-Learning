package com.chess.main;

import com.chess.board.ChessBoard;
import com.chess.stockfish.StockfishConnector;
import com.chess.window.ChessWindow;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

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
 * Class: ChessGame
 *
 * This class represents the main game controller for a chess game. It integrates
 * various components including the UI, game board, and Stockfish engine to manage
 * the gameplay between a computer-controlled player (Stockfish) and a randomly
 * controlled player.
 *
 * Key functionalities include:
 * - Initializing and starting the chess game by integrating the Stockfish engine.
 * - Displaying the initial state of the chessboard on the UI.
 * - Managing the game loop, including determining turns between Stockfish (White)
 *   and a random player (Black).
 * - Making moves using the Stockfish engine for White and randomly generated legal
 *   moves for Black.
 * - Handling UI updates, game termination conditions, and move history management.
 *
 * Dependencies:
 * - `ChessWindow` for handling user interface interactions.
 * - `StockfishConnector` for interacting with the Stockfish chess engine.
 * - `ChessBoard` for representing the current state of the chessboard and retrieving
 *   legal moves for a player.
 */
public class ChessGame {

    private ChessWindow chessWindow;
    private StockfishConnector stockfish;
    private StringBuilder moveHistory;
    private Random random;

    /**
     * Constructor to initialize the ChessGame with a given ChessWindow instance.
     *
     * @param chessWindow The ChessWindow instance for displaying the chessboard and handling UI.
     */
    public ChessGame(ChessWindow chessWindow) {
        this.chessWindow = chessWindow;
        this.stockfish = new StockfishConnector();
        this.moveHistory = new StringBuilder();
        this.random = new Random();
    }

    /**
     * Starts the game, initializing the Stockfish engine and controlling the main game loop.
     *
     * @throws IOException If an I/O error occurs while communicating with Stockfish.
     * @throws InterruptedException If the thread is interrupted while waiting for UI updates.
     */
    public void startGame() throws IOException, InterruptedException {
        if (stockfish.startEngine()) {
            try {
                initializeStockfish();
                displayInitialBoard();
                playGameLoop();
            } finally {
                stockfish.stopEngine();
            }
        } else {
            //System.out.println("Failed to start Stockfish engine.");
        }
    }

    /**
     * Initializes the Stockfish engine by sending initial setup commands.
     *
     * @throws IOException If an I/O error occurs while sending commands or receiving responses from Stockfish.
     */
    private void initializeStockfish() throws IOException {
        stockfish.sendCommand("uci");
        stockfish.getResponse();
        stockfish.sendCommand("isready");
        stockfish.getResponse();
        stockfish.sendCommand("position startpos");
    }

    /**
     * Displays the initial board state in the ChessWindow UI.
     */
    private void displayInitialBoard() {
        Platform.runLater(() -> {
            try {
                chessWindow.displayChessPieces(-1, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isWhiteToMove = true;

    /**
     * The main game loop that alternates turns between Stockfish (White) and the random player (Black).
     *
     * @throws IOException If an I/O error occurs while communicating with the Stockfish engine.
     * @throws InterruptedException If the thread is interrupted while waiting for UI updates.
     */
    private void playGameLoop() throws IOException, InterruptedException {
        String boardStateBeforeLoop = chessWindow.getBoard().toString();

        while (true) {
            if (isWhiteToMove) {
                makeStockfishMove();
            } else {
                makeRandomMove();
            }

            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    chessWindow.displayChessPieces(-1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            latch.await();

            String boardStateAfterLoop = chessWindow.getBoard().toString();
            if (boardStateBeforeLoop.equals(boardStateAfterLoop)) {
                System.out.println("Game over detected. The board state is unchanged.");
                break;
            }

            double rating = this.stockfish.getMoveRating();
            System.out.println("Rating: " + rating);

            boardStateBeforeLoop = boardStateAfterLoop;
            isWhiteToMove = !isWhiteToMove;
            Thread.sleep(500);
        }

        System.out.println("Game over");
    }

    /**
     * Executes Stockfish's move as White and updates the game state.
     *
     * @throws IOException If an I/O error occurs while sending commands to Stockfish.
     * @throws InterruptedException If the thread is interrupted while waiting for UI updates.
     */
    private void makeStockfishMove() throws IOException, InterruptedException {
        try {
            stockfish.sendCommand("go movetime 1000");
            String bestMove = stockfish.getBestMove();

            if (bestMove == null || bestMove.isEmpty()) {
                //System.out.println("Stockfish could not find a move. Game over.");
                return;
            }

            updateMoveHistory(bestMove);

            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    chessWindow.movePiece(bestMove);
                    chessWindow.displayChessPieces(-1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            latch.await();

            chessWindow.getBoard().printBoardWithIndices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a random legal move for the Black player and updates the game state.
     *
     * @throws IOException If an I/O error occurs while interacting with the ChessBoard or Stockfish.
     * @throws InterruptedException If the thread is interrupted while waiting for UI updates.
     */
    private void makeRandomMove() throws IOException, InterruptedException {
        try {
            List<int[]> legalMoves = chessWindow.getBoard().getAllLegalMoves(ChessBoard.Player.BLACK);

            if (legalMoves.isEmpty()) {
                //System.out.println("Black has no legal moves. Game over.");
                return;
            }

            int[] randomMove = legalMoves.get(random.nextInt(legalMoves.size()));
            String from = chessWindow.getBoard().toChessNotation(randomMove[0], randomMove[1]);
            String to = chessWindow.getBoard().toChessNotation(randomMove[2], randomMove[3]);
            String randomMoveNotation = from + to;

            updateMoveHistory(randomMoveNotation);

            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    chessWindow.movePiece(randomMoveNotation);
                    chessWindow.displayChessPieces(-1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            latch.await();

            chessWindow.getBoard().printBoardWithIndices();
            stockfish.sendCommand("position startpos moves " + moveHistory.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the move history with the given move.
     *
     * @param move The move in standard chess notation to be appended to the history.
     */
    private void updateMoveHistory(String move) {
        if (moveHistory.length() > 0) {
            moveHistory.append(" ");
        }
        moveHistory.append(move);
    }

}
