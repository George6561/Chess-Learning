package com.chess.stockfish;

import java.io.*;
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
 * Class: StockfishConnector
 *
 * This class provides an interface for communicating with the Stockfish chess engine.
 * It is used to start, stop, and interact with the engine to evaluate positions, calculate 
 * best moves, and perform other operations that enhance the chess-playing capabilities of 
 * the application.
 *
 * Key functionalities include:
 * - Starting and stopping the Stockfish engine.
 * - Sending commands to and receiving responses from the Stockfish engine.
 * - Retrieving the best move and other analytical information from the engine.
 * - Setting options for the engine such as analysis depth, number of threads, and debug mode.
 * - Querying legal moves and verifying the validity of a given move.
 * - Handling game state updates to keep Stockfish informed of the current position on the chessboard.
 *
 * Dependencies:
 * - The Stockfish engine executable, referenced by `ENGINE_SOURCE`. Ensure that the executable path is correctly specified.
 * - Java I/O classes (`BufferedReader`, `BufferedWriter`, etc.) for interacting with Stockfish.
 *
 * Usage:
 * - `startEngine()`: Starts the Stockfish engine process.
 * - `sendCommand(String command)`: Sends a specific UCI command to the engine.
 * - `getBestMove()`: Retrieves the best move suggested by Stockfish based on the current game state.
 * - `stopEngine()`: Stops the engine and releases all related resources.
 *
 * Notes:
 * - The engine operates using the Universal Chess Interface (UCI), which allows for clear command and response protocols.
 * - Some methods rely on a proper sequence of commands to ensure the engine is in the correct state before processing further instructions.
 * - Use `isEngineReady()` to ensure the engine is ready for a new command after initialization or heavy operations.
 */
public class StockfishConnector {

    private Process stockfish;
    private BufferedReader input;
    private BufferedWriter output;
    private static final String ENGINE_SOURCE = "stockfish/stockfish-windows-x86-64-avx2";

    public boolean startEngine() {
        try {
            stockfish = new ProcessBuilder(ENGINE_SOURCE).start();
            input = new BufferedReader(new InputStreamReader(stockfish.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(stockfish.getOutputStream()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendCommand(String command) throws IOException {
        output.write(command + "\n");
        output.flush();
    }

    public String getResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.equals("uciok") || line.startsWith("bestmove") || line.equals("readyok")) {
                break;
            }
        }
        return sb.toString();
    }

    public String getBestMove() throws IOException {
        String bestMove = null;
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.startsWith("bestmove")) {
                String[] parts = line.split(" ");
                bestMove = parts[1];
                break;
            }
        }
        return bestMove;
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            stockfish.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGameState(String moveHistory) throws IOException {
        if (moveHistory == null || moveHistory.isEmpty()) {
            sendCommand("position startpos");
        } else {
            sendCommand("position startpos moves " + moveHistory);
        }
    }

    public void calculateBestMove(int timeLimitMillis) throws IOException {
        sendCommand("go movetime " + timeLimitMillis);
    }

    public boolean isEngineReady() throws IOException {
        sendCommand("isready");
        String response = getResponse();
        return response.contains("readyok");
    }

    public void setAnalysisDepth(int depth) throws IOException {
        sendCommand("go depth " + depth);
    }

    public void setOption(String option, String value) throws IOException {
        sendCommand("setoption name " + option + " value " + value);
    }

    public List<String> getLegalMoves() throws IOException {
        sendCommand("d");
        List<String> legalMoves = new ArrayList<>();
        String line;
        while ((line = input.readLine()) != null) {
            if (line.startsWith("Legal moves:")) {
                String[] moves = line.replace("Legal moves: ", "").split(" ");
                for (String move : moves) {
                    legalMoves.add(move);
                }
                break;
            }
        }
        return legalMoves;
    }

    public void setDebugMode(boolean enable) throws IOException {
        sendCommand("setoption name Debug Log File value " + (enable ? "debug.log" : ""));
    }

    public String getAnalysisOutput() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.startsWith("bestmove")) {
                break;
            }
        }
        return sb.toString();
    }

    public void setThreads(int threads) throws IOException {
        sendCommand("setoption name Threads value " + threads);
    }

    public void clearHash() throws IOException {
        sendCommand("ucinewgame");
    }

    public boolean isValidMove(String move) throws IOException {
        List<String> legalMoves = getLegalMoves();
        return legalMoves.contains(move);
    }

    public int getMoveEvaluation() throws IOException {
        sendCommand("go depth 20"); // You can set the depth as required
        String line;
        while ((line = input.readLine()) != null) {
            if (line.contains("info depth")) {
                Integer evaluation = parseEvaluationFromLine(line);
                if (evaluation != null) {
                    return evaluation;
                }
            } else if (line.startsWith("bestmove")) {
                break; // Stop reading once we reach the "bestmove" line
            }
        }
        return 0; // Return 0 if no evaluation was found, indicating a draw
    }

    private Integer parseEvaluationFromLine(String line) {
        String[] parts = line.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("cp")) {
                try {
                    // Positive values indicate good for white, negative for black
                    return Integer.parseInt(parts[i + 1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Handle potential parsing issues
                }
            } else if (parts[i].equals("mate")) {
                try {
                    int mateIn = Integer.parseInt(parts[i + 1]);
                    // Positive value indicates mate in favor of white, negative indicates mate for black
                    return mateIn > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null; // Return null if no evaluation found in this line
    }

    public double getMoveEvaluation(String moveHistory, int depth) throws IOException {
    updateGameState(moveHistory);
    sendCommand("go depth " + depth);

    String line;
    while ((line = input.readLine()) != null) {
        if (line.contains("info depth " + depth)) {
            Integer evaluation = parseEvaluationFromLine(line);
            if (evaluation != null) {
                switch (evaluation) {
                    case Integer.MAX_VALUE:
                        return 1000; 
                    case Integer.MIN_VALUE:
                        return -1000; 
                    default:
                        // Convert centipawn evaluation to a string
                        return evaluation / 100.0;
                }
            }
        } else if (line.startsWith("bestmove")) {
            break;
        }
    }
    return 0.0;
}


}
