package test;

import org.junit.Assert;
import org.junit.Test;

import sol.EdgeArrayGraph;
import sol.IGraph;
import sol.NodeEdgeGraph;
import src.NoRouteException;
import src.NodeNameExistsException;

import java.util.LinkedList;

import static sol.GraphUtils.getRoute;

public class GraphUtilsTest {
    // Assumes that graph will be empty, modifies it in-place
    private void addSimpleGraphNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("node 1");
        graph.addNode("node 2");
        graph.addNode("node 3");
        graph.addNode("node 4");
    }

    // Assumes that graph will have nodes from `addSimpleGraphNodes`,
    //     modifies it in-place
    private void addSimpleGraphEdges(IGraph graph) {
        graph.addDirectedEdge("node 1", "node 2");
        graph.addDirectedEdge("node 2", "node 3");
    }

    // Assumes that graph will be empty, modifies it in-place
    private void makeSimpleGraph(IGraph graph) throws NodeNameExistsException {
        addSimpleGraphNodes(graph);
        addSimpleGraphEdges(graph);
    }

    @Test
    public void testGetRouteSimple(){
        try {
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            String fromNode = "node 1";
            String toNode = "node 3";
            LinkedList<String> route = getRoute(simpleGraph, fromNode, toNode);

            LinkedList<String> expectedRoute = new LinkedList<>();
            expectedRoute.add("node 1");
            expectedRoute.add("node 2");
            expectedRoute.add("node 3");

            Assert.assertEquals(expectedRoute, route);
            Assert.assertTrue(checkValidRoute(simpleGraph, route, "node 1", "node 3", 3));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoRouteException e) {
            Assert.fail("getRoute did not find a route");
        }
    }

    @Test
    public void testGetRouteSimpleNoRoute(){
        try {
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            String fromNode = "node 1";
            String toNode = "node 4";
            Assert.assertThrows(
                    NoRouteException.class,
                    () -> getRoute(simpleGraph, fromNode, toNode));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCountSelfEdgesSimple() {
        try {
            IGraph basicGraph = new NodeEdgeGraph("a graph");
            basicGraph.addNode("node 1");
            basicGraph.addDirectedEdge("node 1", "node 1");
            Assert.assertEquals(1, basicGraph.countSelfEdges());
        } catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testReachesAllOthersSimple(){
        IGraph basicGraph = new NodeEdgeGraph("b graph");
        basicGraph.addUndirectedEdge("node 1", "node 2");
        Assert.assertTrue(basicGraph.reachesAllOthers("node 1"));
        Assert.assertTrue(basicGraph.reachesAllOthers("node 2"));
    }

    @Test
    public void testCreateArrayNode(){
        IGraph basicGraph = new EdgeArrayGraph("array graph");
        try {
            basicGraph.addNode("test");
            Assert.assertTrue(true);
        } catch (NodeNameExistsException e) {
            Assert.fail();
        }

    }

    @Test
    public void testCountSelfEdges() throws NodeNameExistsException{
        //Array Implementation
        IGraph graph = new EdgeArrayGraph("");
        Assert.assertEquals(0, graph.countSelfEdges());
        graph = createGraph1(true);
        Assert.assertEquals(0, graph.countSelfEdges());
        graph.addUndirectedEdge("1", "1");
        Assert.assertEquals(1, graph.countSelfEdges());
        graph.addUndirectedEdge("2", "2");
        Assert.assertEquals(2, graph.countSelfEdges());

        //Node Implementation
        graph = new NodeEdgeGraph("");
        Assert.assertEquals(0, graph.countSelfEdges());
        graph = createGraph1(false);
        Assert.assertEquals(0, graph.countSelfEdges());
        graph.addUndirectedEdge("1", "1");
        Assert.assertEquals(1, graph.countSelfEdges());
        graph.addUndirectedEdge("2", "2");
        Assert.assertEquals(2, graph.countSelfEdges());
    }

    @Test
    public void testReachesALlOthers() throws NodeNameExistsException {
        //Array Implementation
        IGraph graph = createGraph2(true);
        Assert.assertTrue(graph.reachesAllOthers("1"));
        Assert.assertTrue(graph.reachesAllOthers("2"));
        graph = createGraph3(true);
        graph.addUndirectedEdge("1", "4");
        Assert.assertTrue(graph.reachesAllOthers("1"));
        Assert.assertTrue(graph.reachesAllOthers("4"));
        Assert.assertFalse(graph.reachesAllOthers("2"));
        Assert.assertFalse(graph.reachesAllOthers("3"));

        //Node Implementation
        graph = createGraph2(false);
        Assert.assertTrue(graph.reachesAllOthers("1"));
        Assert.assertTrue(graph.reachesAllOthers("2"));
        graph = createGraph3(false);
        graph.addUndirectedEdge("1", "4");
        Assert.assertTrue(graph.reachesAllOthers("1"));
        Assert.assertTrue(graph.reachesAllOthers("4"));
        Assert.assertFalse(graph.reachesAllOthers("2"));
        Assert.assertFalse(graph.reachesAllOthers("3"));
    }

    @Test
    public void testGetRoute() throws NodeNameExistsException{
        //Array Implementation
        IGraph graph = createGraph4(true);
        try {
            getRoute(graph, "1", "4");
        } catch (NoRouteException e){
            Assert.assertTrue(true);
        }
        try {
            Assert.assertTrue(checkValidRoute(graph, getRoute(graph, "1", "3")
                    , "1", "3", 3));
        } catch (NoRouteException e) {
            Assert.fail();
        }

        //Node Implementation
        graph = createGraph4(false);
        try {
            getRoute(graph, "1", "4");
        } catch (NoRouteException e){
            Assert.assertTrue(true);
        }
        try {
            Assert.assertTrue(checkValidRoute(graph, getRoute(graph, "1", "3")
                    , "1", "3", 3));
        } catch (NoRouteException e) {
            Assert.fail();
        }
    }
    private boolean checkValidRoute(IGraph graph, LinkedList<String> route,String start, String end, int length) {
        if (route.size() != length) return false;
        if (!route.getFirst().equals(start) || !route.getLast().equals(end)) return false;
        for (int i = 0; i < route.size() - 1; i++) {
            String next = route.get(i + 1);
            if (!graph.getNeighbors(route.get(i)).contains(next)) {
                return false;
            }
        }
        return true;
    }

    private IGraph createGraph1(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addDirectedEdge("1", "2");
        return graph;
    }

    private IGraph createGraph2(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addUndirectedEdge("1", "2");
        return graph;
    }

    private IGraph createGraph3(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addDirectedEdge("1", "2");
        graph.addDirectedEdge("1", "3");
        graph.addDirectedEdge("4", "2");
        graph.addDirectedEdge("4", "3");
        graph.addUndirectedEdge("3", "2");
        return graph;
    }

    private IGraph createGraph4(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addNode("5");
        graph.addNode("6");
        graph.addNode("7");
        graph.addDirectedEdge("1", "2");
        graph.addDirectedEdge("2", "3");
        graph.addDirectedEdge("4", "3");
        graph.addDirectedEdge("1", "2");
        graph.addDirectedEdge("5", "6");
        graph.addDirectedEdge("7", "6");
        graph.addUndirectedEdge("4", "5");
        graph.addUndirectedEdge("4", "7");
        return graph;
    }

}
