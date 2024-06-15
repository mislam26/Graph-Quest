package sol;

import src.NodeNameExistsException;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Interface for making methods generic between implementations of graph
 */
public interface IGraph {

    /**
     * Method to add a node to a graph
     *
     * @param descr name of node
     * @throws NodeNameExistsException if node with name already exists
     */
    void addNode(String descr) throws NodeNameExistsException;

    /**
     * Adds one-way edge from one node to the next
     *
     * @param descr1 string name of originating node
     * @param descr2 string name of destination node
     */
    void addDirectedEdge(String descr1, String descr2);

    /**
     * Adds bidirectional connection between two nodes
     *
     * @param descr1 string name of first node
     * @param descr2 string name of second node
     */
    void addUndirectedEdge (String descr1, String descr2);

    /**
     * Counts the number of nodes who have edges to themselves
     *
     * @return number of nodes with edges to themselves
     */
    int countSelfEdges();

    /**
     * Checks for whether a node has connections to all other nodes
     *
     * @param fromNodeLabel string of node to check for connections of
     * @return true if has connection to all other nodes, else false
     */
    boolean reachesAllOthers(String fromNodeLabel);

    /**
     * For obtaining a list of all neighbors to a specific node
     *
     * @param fromNode node to check for neighbors of
     * @return a list of node names that fromNode can reach
     */
    LinkedList<String> getNeighbors(String fromNode);

    /**
     * Gets the names of all nodes in graph
     *
     * @return a hashset of all node names from graph
     */
    HashSet<String> getAllNodes();
}
