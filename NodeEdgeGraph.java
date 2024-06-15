package sol;

import src.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Class for modeling a graph based on nodes
 */
public class NodeEdgeGraph implements IGraph{
    public String name;
    public HashMap<String, Node> allNodes;

    // ---------------------------------------------
    // the Node class
    static class Node {
        String descr;  // a descriptive name for the node
        LinkedList<Node> nextNodes; // the nodes that this node can get to

        public Node(String descr) {
            this.descr = descr;
            this.nextNodes = new LinkedList<>();
        }

        private void addEdge(Node toNode) {
            this.nextNodes.add(toNode);
        }
    }

    /**
     * Constructor for node based graph
     *
     * @param name name of graph
     */
    public NodeEdgeGraph(String name) {
        this.name = name;
        this.allNodes = new HashMap<String, Node>();
    }

    /**
     * Method to retrieve the Node object for a name
     *
     * @param label the name of the node to find
     * @return the Node that has label as its description
     */
    public Node getNode(String label) {
        return this.allNodes.get(label);
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
        if (this.allNodes.containsKey(descr))
            throw new NodeNameExistsException(descr);
        this.addNodeUnchecked(descr);
    }

    /**
     * An internal method to add a node without checking whether it exists.
     * This is useful for internally avoiding the exception handling when
     * we already know the node doesn't exist.
     *
     * @param descr the text description or label to associate with the node
     * @return the (new) node associated with the given description
     */
    private Node addNodeUnchecked(String descr) {
        Node newNode = new Node(descr);
        this.allNodes.put(descr, newNode);
        return newNode;
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
        Node node1 = this.allNodes.get(descr1);
        if (node1 == null) node1 = this.addNodeUnchecked(descr1);
        Node node2 = this.allNodes.get(descr2);
        if (node2 == null) node2 = this.addNodeUnchecked(descr2);
        if (!(node1.nextNodes.contains(node2))) {
            node1.addEdge(node2);
        }
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
        this.addDirectedEdge(descr1, descr2);
        this.addDirectedEdge(descr2, descr1);
    }

    /**
     * Method to count how many nodes have edges to themselves
     *
     * @return the number of nodes that have edges to themselves
     *
     * Runtime:
     *      O(N * M) Where N is the number of Nodes and M is the number of neighbors to the nodes
     *      Need to iterate once through the nodes checking for if they contain a link to themselves
     *      However each of the N calls also makes a call that's O(M)
     *      I'm not sure if that makes this O(N^2) as worst case M = N
     */
    public int countSelfEdges() {
        int count = 0;
        for (String nodeDesc : this.allNodes.keySet()) {
            if (this.getNeighbors(nodeDesc).contains(nodeDesc)) {
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
     *      O(N) Runtime in Node Implementation where N is the number of nodes in graph
     *      Must iterate through the list of nodes in graph
     *      Each has to be confirmed to be in the list of neighbors of the originating node
     */
    public boolean reachesAllOthers(String fromNodeLabel) {
        LinkedList<String> descList = this.getNeighbors(fromNodeLabel);
        HashSet<String> descSet = new HashSet<>();
        for (String string : descList) {
            descSet.add(string);
        }
        descSet.add(fromNodeLabel);
        for (String string : this.allNodes.keySet()) {
            if (!descSet.contains(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to return a list of all neighboring nodes as strings to a node
     *
     * @param fromNode node to check for outgoing connections from
     * @return a list of strings corresponding to the neighbors of fromNode
     */
    @Override
    public LinkedList<String> getNeighbors(String fromNode) {
        Node nodeObject = this.allNodes.get(fromNode);
        LinkedList<String> neighbors = new LinkedList<>();
        for (Node node : nodeObject.nextNodes) {
            neighbors.add(node.descr);
        }
        return neighbors;
    }

    /**
     * Method to return the name strings of all nodes in the graph
     *
     * @return a hashset of all node name strings
     */
    @Override
    public HashSet<String> getAllNodes() {
        HashSet<String> allNodesSet = new HashSet<>();
        for (String node : this.allNodes.keySet()) {
            allNodesSet.add(node);
        }
        return allNodesSet;
    }
}
