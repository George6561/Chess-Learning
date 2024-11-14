package com.montecarlo.engine;

import com.montecarlo.data.GameData;
import com.montecarlo.data.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: MonteCarloNode
 *
 * This class represents a node in the Monte Carlo Tree Search (MCTS) algorithm.
 * Each node holds information about the move that led to it, its visit count,
 * win count, and child nodes. It helps in evaluating and exploring potential game moves.
 *
 * Key functionalities include:
 * - Storing move data and tracking visit and win statistics.
 * - Managing child nodes and their connections to parent nodes.
 * - Calculating the UCB1 (Upper Confidence Bound) value to prioritize exploration.
 *
 * Dependencies:
 * - `Move` for representing moves associated with each node.
 */
public class MonteCarloNode {

    private Move move;
    private GameData gameData; // New field for associated GameData
    private MonteCarloNode parent;
    private List<MonteCarloNode> children;
    private int visitCount;
    private double winCount;

    /**
     * Constructor to initialize the MonteCarloNode with Move, GameData, and Parent Node.
     *
     * @param move The move associated with this node.
     * @param gameData The game state associated with this move.
     * @param parent The parent node in the tree, or null if this is the root.
     */
    public MonteCarloNode(Move move, GameData gameData, MonteCarloNode parent) {
        this.move = move;
        this.gameData = gameData; // Initialize gameData
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visitCount = 0;
        this.winCount = 0.0;
    }

    /**
     * Constructor to initialize the MonteCarloNode with only Move and Parent Node.
     *
     * @param move The move associated with this node.
     * @param parent The parent node in the tree, or null if this is the root.
     */
    public MonteCarloNode(Move move, MonteCarloNode parent) {
        this(move, null, parent); // Call the other constructor with gameData as null
    }

    /**
     * Adds a child node to this node.
     *
     * @param child The child node to be added.
     */
    public void addChild(MonteCarloNode child) {
        this.children.add(child);
    }

    /**
     * Increments the visit count of this node.
     */
    public void incrementVisit() {
        this.visitCount++;
    }

    /**
     * Adds a win count to this node.
     *
     * @param wins The number of wins to be added.
     */
    public void addWin(double wins) {
        this.winCount += wins;
    }

    /**
     * Calculates the UCB1 value for this node to prioritize exploration.
     *
     * @param totalSimulations The total number of simulations run by the parent node.
     * @return The UCB1 value.
     */
    public double calculateUCB1(int totalSimulations) {
        if (visitCount == 0) {
            return Double.MAX_VALUE; // Prioritize unvisited nodes.
        }
        return (winCount / visitCount) + Math.sqrt(2 * Math.log(totalSimulations) / visitCount);
    }

    /**
     * Checks if the node is fully expanded (i.e., all possible moves are explored).
     *
     * @return True if the node is fully expanded, false otherwise.
     */
    public boolean isFullyExpanded() {
        return !children.isEmpty() && children.stream().allMatch(child -> child.getVisitCount() > 0);
    }

    /**
     * Getters and setters for encapsulated fields.
     */
    public Move getMove() {
        return move;
    }

    public GameData getGameData() {
        return gameData;
    }

    public MonteCarloNode getParent() {
        return parent;
    }

    public List<MonteCarloNode> getChildren() {
        return children;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getWinCount() {
        return winCount;
    }

    /**
     * Finds the most visited child node for move selection after the simulation phase.
     *
     * @return The child node with the highest visit count.
     */
    public MonteCarloNode getMostVisitedChild() {
        return children.stream().max((node1, node2) -> Integer.compare(node1.getVisitCount(), node2.getVisitCount()))
                .orElse(null);
    }
}
