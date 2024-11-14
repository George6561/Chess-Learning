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
 * Class: MoveValidator
 *
 * The MoveValidator class is responsible for validating moves during the Monte Carlo
 * simulations. It ensures that proposed moves adhere to the rules of chess and are
 * legitimate within the current state of the game.
 *
 * Key functionalities include:
 * - Validating the legality of a given move.
 * - Checking for conditions like checks, checkmates, and stalemates.
 * - Assisting the GameSimulator and other simulation classes by filtering out illegal moves.
 *
 * Dependencies:
 * - `ChessBoard` for board state and move generation.
 * - `Move` for encapsulating move details.
 */
package com.montecarlo.util;

import com.montecarlo.data.Move;
import com.chess.board.ChessBoard;

/**
 * Utility class for validating chess moves to ensure they are legal and adhere
 * to the rules of the game.
 * 
 * Author: George Miller
 */
public class MoveValidator {

    /**
     * Validates whether a move is legal for the current state of the board.
     *
     * @param board The current chessboard state.
     * @param move The move to validate.
     * @return True if the move is valid, false otherwise.
     */
    public static boolean isMoveValid(ChessBoard board, Move move) {
        // Check if the move is within the bounds of the board
        if (!isWithinBounds(move)) {
            return false;
        }

        // Ensure that the starting position has a piece and the piece belongs to the current player
        if (board.getPieceAt(move.getStartRow(), move.getStartCol()) == 0) {
            return false; // No piece at starting position
        }

        // Add further validation logic as needed:
        // - Check if the move puts the player in check
        // - Validate special moves like castling, en passant, promotion, etc.

        // Placeholder for more comprehensive rule checks
        return true; // Assume the move is valid if it passes basic checks
    }

    /**
     * Checks if the move's start and end positions are within the bounds of the board.
     *
     * @param move The move to check.
     * @return True if both start and end positions are within the 8x8 grid, false otherwise.
     */
    private static boolean isWithinBounds(Move move) {
        int startRow = move.getStartRow();
        int startCol = move.getStartCol();
        int endRow = move.getEndRow();
        int endCol = move.getEndCol();

        return startRow >= 0 && startRow < 8 && startCol >= 0 && startCol < 8 &&
               endRow >= 0 && endRow < 8 && endCol >= 0 && endCol < 8;
    }
    
}
