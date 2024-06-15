package sol;

import src.NoRouteException;

import java.util.*;

/**
 * Class containing route methods for use with graphs
 */
public class GraphUtils {

    /**
     * Empty Constructor
     */
    public GraphUtils() {}

//    /**
//     * Method to use breadth-first-search to check whether there is a path
//     *     from one node to another in a graph. Assumes that both fromNodeLabel
//     *     and toNodeLabel are valid node labels in theGraph.
//     *
//     * @param theGraph the graph to traverse
//     * @param fromNodeLabel name of the node from which to start searching
//     * @param toNodeLabel   name of the node we want to reach
//     * @return boolean indicating whether such a route exists
//     */
//    public static boolean hasRoute(NodeEdgeGraph theGraph, String fromNodeLabel, String toNodeLabel) {
//        NodeEdgeGraph.Node fromNode = theGraph.getNode(fromNodeLabel);
//        NodeEdgeGraph.Node toNode = theGraph.getNode(toNodeLabel);
//        // set up and initialize data structures
//        HashSet<NodeEdgeGraph.Node> visited = new HashSet<>();
//        LinkedList<NodeEdgeGraph.Node> toCheck = new LinkedList<>();
//        toCheck.add(fromNode);
//
//        // process nodes to search for toNode
//        while (!toCheck.isEmpty()) {
//            NodeEdgeGraph.Node checkNode = toCheck.pop();
//            if (checkNode.equals(toNode)) {
//                return true;
//            } else if (!visited.contains(checkNode)) {
//                visited.add(checkNode);
//                toCheck.addAll(checkNode.nextNodes); //need method returning all edges
//            }
//        }
//        return false;
//    }

    /**
     * Method to produce a sequence of nodes that constitutes a shortest path
     *     from fromNodeLabel to toNodeLabel. Assumes that both fromNodeLabel
     *     and toNodeLabel are valid node labels in theGraph.
     * Throws a NoRouteException if no such path exists
     *
     * @param theGraph the graph to traverse
     * @param fromNodeLabel the node from which to start searching
     * @param toNodeLabel   the node we want to reach
     * @return List of nodes in order of the path
     * @throws NoRouteException if no such path exists
     */
    public static LinkedList<String> getRoute(IGraph theGraph, String fromNodeLabel, String toNodeLabel)
            throws NoRouteException {
        HashSet<String> visited = new HashSet<>();
        Queue<String> toCheck = new LinkedList<>();
        HashMap<String, String> cameFrom = new HashMap<>();
        toCheck.add(fromNodeLabel);

        while (!toCheck.isEmpty()) {
            String checkNode = toCheck.remove();
            visited.add(checkNode);
            if (checkNode.equals(toNodeLabel)) {
                LinkedList<String> route = constructRoute(toNodeLabel, cameFrom);
                route.addFirst(fromNodeLabel);
                return route;
            } else {
                for (String neighbor : theGraph.getNeighbors(checkNode)) {
                    if ((!visited.contains(neighbor))) {
                        toCheck.add(neighbor);
                        if (!cameFrom.containsKey(neighbor)) {
                            cameFrom.put(neighbor, checkNode);
                        }
                    }
                }
            }
        }
        if (!visited.contains(toNodeLabel)) {
            throw new NoRouteException();
        }
        return new LinkedList<>();
    }

    /**
     * Helper method to reconstruct the route to the target node
     *
     * @param endNode Target node that the route ended on
     * @param prevMap Map of nodes to the node they were originally found via
     * @return a list in order from the starting node of the route to the endNode
     */
    private static LinkedList<String> constructRoute(String endNode, HashMap<String, String> prevMap) {
        LinkedList<String> route = new LinkedList<>();
        String currentNode = endNode;
        while (prevMap.containsKey(currentNode)) {
            route.addFirst(currentNode);
            currentNode = prevMap.get(currentNode);
        }
        return route;
    }
}