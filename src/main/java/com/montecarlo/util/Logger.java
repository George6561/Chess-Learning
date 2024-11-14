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
 * Class: Logger
 *
 * The Logger class is responsible for managing and recording log entries during the execution
 * of the Monte Carlo simulations. It provides methods for logging information, warnings,
 * and errors to both the console and a log file. Each log entry is timestamped to allow 
 * for traceability of events within the simulation process.
 *
 * Key functionalities include:
 * - Logging information, warnings, and errors.
 * - Writing logs to the console and appending them to a persistent log file.
 * - Providing timestamped log entries for better traceability.
 *
 * Usage:
 * - Use `logInfo(String message)` for general information.
 * - Use `logWarning(String message)` for warnings.
 * - Use `logError(String message)` for errors.
 *
 * Dependencies:
 * - Java I/O classes (`FileWriter`, `PrintWriter`).
 * - Java time classes (`LocalDateTime`, `DateTimeFormatter`) for timestamping log entries.
 */
package com.montecarlo.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class for managing and recording log entries during the execution of the Monte Carlo simulations.
 * Provides methods for logging information, warnings, and errors to both the console and a log file.
 * 
 * Author: George Miller
 */
public class Logger {

    private static final String LOG_FILE = "simulation_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message with INFO level.
     *
     * @param message The message to log.
     */
    public static void logInfo(String message) {
        log("INFO", message);
    }

    /**
     * Logs a message with WARNING level.
     *
     * @param message The message to log.
     */
    public static void logWarning(String message) {
        log("WARNING", message);
    }

    /**
     * Logs a message with ERROR level.
     *
     * @param message The message to log.
     */
    public static void logError(String message) {
        log("ERROR", message);
    }

    /**
     * Internal method for logging a message with a specified level.
     *
     * @param level   The level of the log (INFO, WARNING, ERROR).
     * @param message The message to log.
     */
    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);

        // Print to console
        System.out.println(logEntry);

        // Write to log file
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }
}
