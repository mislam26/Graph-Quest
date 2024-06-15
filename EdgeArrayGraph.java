package sol;

import src.NodeNameExistsException;

import java.util.*;

/**
 * Class for modeling a graph based on an array of booleans representing connections
 */
public class EdgeArrayGraph implements IGraph {
    String name;
    ArrayList<ArrayList<Boolean>> graph;
    HashMap<String, Integer> stringToIndex;
    HashMap<Integer, String> indexToString;

    /**
     * Constructor for array based graph
     *
     * @param name name of graph
     */
    public EdgeArrayGraph(String name) {
        this.name = name;
        this.graph = new ArrayList<>();
        this.stringToIndex = new HashMap<String, Integer>();
        this.indexToString = new HashMap<Integer, String>();
    }

    /**
     * Method to add a new node with the given description. An exception will
     * be thrown if the description already names a node in the graph
     *
     * @param descr the text description or label to associate with the node
     * @throws NodeNameExistsException if that description is already
     * associated with a node in the graph
     */
    public void addNode(String descr) throws NodeNameExistsException {
        if (this.stringToIndex.containsKey(descr)) {
            throw new NodeNameExistsException();
        }
        this.addNodeUnchecked(descr);
    }

    /**
     * Internal method for adding a node without checking for exception. Used for instances where
     * it is known that the node does not already exist
     *
     * @param descr description of node to add
     */
    private void addNodeUnchecked(String descr) {
        this.addNewArray();
        int index = this.graph.get(0).size() - 1;
        this.stringToIndex.put(descr, index);
        this.indexToString.put(index, descr);
    }

    /**
     * Method to add a directed edge between the nodes associated with the given
     * descriptions. If descr1 and descr2 are not already
     * valid node labels in the graph, those nodes are also created.
     * If the edge already exists, no changes are made
     * (and no exceptions or warnings are raised)
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addDirectedEdge(String descr1, String descr2) {
        if (!this.stringToIndex.containsKey(descr1)) {
            this.addNodeUnchecked(descr1);
        }
        if (!this.stringToIndex.containsKey(descr2)) {
            this.addNodeUnchecked(descr2);
        }
        Integer index1 = this.stringToIndex.get(descr1);
        Integer index2 = this.stringToIndex.get(descr2);
        this.graph.get(index1).set(index2, true);
    }

    /**
     * Method to add an undirected edge between the nodes associated with the given
     * descriptions. This is equivalent to adding two directed edges, one from
     * descr1 to descr2, and another from descr2 to descr1.
     * If descr1 and descr2 are not already valid node labels in the graph,
     * those nodes are also created.
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addUndirectedEdge(String descr1, String descr2) {
        if (!this.stringToIndex.containsKey(descr1)) {
            this.addNodeUnchecked(descr1);
        }
        if (!this.stringToIndex.containsKey(descr2)) {
            this.addNodeUnchecked(descr2);
        }
        Integer index1 = this.stringToIndex.get(descr1);
        Integer index2 = this.stringToIndex.get(descr2);
        this.graph.get(index1).set(index2, true);
        this.graph.get(index2).set(index1, true);
    }

    /**
     * Method to count how many nodes have edges to themselves
     *
     * @return the number of nodes that have edges to themselves
     *
     * Runtime:
     *      O(N) Where N is the number of nodes
     *      Only necessary to check for being empty and for every [i][i] array value
     *      Will only ever check N array locations
     */
    public int countSelfEdges() {
        int count = 0;
        if (this.graph.isEmpty()) {
            return count;
        }
        for (int i = 0; i < this.graph.get(0).size(); i++) {
            if (this.graph.get(i).get(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method to check whether a given node has edges to every other node (with or without an edge to itself).
     * Assumes that fromNodeLabel is a valid node label in the graph.
     *
     * @param fromNodeLabel the node to check
     * @return true if fromNodeLabel has an edge to every other node, otherwise false
     *
     * Runtime:
     *      O(N) Where N is the number of nodes
     *      Iterates from array[0] to array[N] checking that all except for [i][i] are true
     */
    public boolean reachesAllOthers(String fromNodeLabel) {
        int index = this.stringToIndex.get(fromNodeLabel);
        for (int i = 0; i < this.graph.get(0).size(); i++) {
            if (i == index) {
                continue;
            }
            if (!this.graph.get(index).get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to get a list of all nodes in the graph that the chosen node has connections to
     *
     * @param node string name of node in graph to check for neighbors of
     * @return a list of strings corresponding to all neighbors of node
     */
    @Override
    public LinkedList<String> getNeighbors(String node) {
        int index = this.stringToIndex.get(node);
        LinkedList<String> neighbors = new LinkedList<>();
        for (int i = 0; i < this.graph.get(index).size(); i++) {
            if (this.graph.get(index).get(i)) {
                neighbors.add(this.indexToString.get(i));
            }
        }
        return neighbors;
    }

    /**
     * Method to return a hashset of the string names of all nodes in graph
     *
     * @return hashset of name strings
     */
    @Override
    public HashSet<String> getAllNodes() {
        HashSet<String> allNodesSet = new HashSet<>();
        for (String node : this.stringToIndex.keySet()) {
            allNodesSet.add(node);
        }
        return allNodesSet;
    }

    /**
     * Helper method to add and fill out arraylists with default of false
     * Used when adding a new node to the graph
     */
    private void addNewArray() {
        this.graph.add(new ArrayList<>());
        for (int i = 0; i < this.graph.size() - 1; i++) {
            this.graph.get(this.graph.size() - 1).add(false);
        }
        for (int i = 0; i < this.graph.size(); i++) {
            this.graph.get(i).add(false);
        }
     }
}
