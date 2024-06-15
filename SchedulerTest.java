package test;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;

import sol.EdgeArrayGraph;
import sol.IGraph;
import sol.NodeEdgeGraph;
import sol.Scheduler;
import src.NoScheduleException;
import src.NodeNameExistsException;

import static sol.Scheduler.checkValidity;
import static sol.Scheduler.findSchedule;

public class SchedulerTest {
    // Assumes that graph will be empty, modifies it in-place
    private void addSimpleGraphNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("lab 1");
        graph.addNode("lab 2");
        graph.addNode("lab 3");
        graph.addNode("lab 4");
    }

    // Assumes that graph will have nodes from `addSimpleGraphNodes`,
    //     modifies it in-place
    private void addSimpleGraphEdges(IGraph graph) {
        graph.addUndirectedEdge("lab 1", "lab 2");
        graph.addUndirectedEdge("lab 2", "lab 3");
    }

    // Assumes that graph will be empty, modifies it in-place
    private void makeSimpleGraph(IGraph graph) throws NodeNameExistsException {
        addSimpleGraphNodes(graph);
        addSimpleGraphEdges(graph);
    }

    @Test
    public void testCheckValidityTrue() {
        try{
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("lab 1");
            set1.add("lab 3");
            set1.add("lab 4");
            proposedSchedule.add(set1);
            HashSet<String> set2 = new HashSet<>();
            set2.add("lab 2");
            proposedSchedule.add(set2);

            Assert.assertTrue(checkValidity(simpleGraph, proposedSchedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCheckValidityFalse() {
        try{
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("lab 3");
            set1.add("lab 4");
            proposedSchedule.add(set1);
            HashSet<String> set2 = new HashSet<>();
            set2.add("lab 1");
            set2.add("lab 2");
            proposedSchedule.add(set2);

            Assert.assertFalse(checkValidity(simpleGraph, proposedSchedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        }

    }

    @Test
    public void testFindScheduleValid(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(checkValidity(simpleGraph, schedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        }
    }

    @Test
    public void testFindScheduleInvalid(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            simpleGraph.addUndirectedEdge("lab 3", "lab 1"); // This edge should make it impossible to schedule
            Assert.assertThrows(
                    NoScheduleException.class,
                    () -> Scheduler.findSchedule(simpleGraph));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCheckValidity() throws NodeNameExistsException {
        //Array Implementation
        IGraph graph = createGraph3(true);
        ArrayList<HashSet<String>> propSched = new ArrayList<>();
        propSched.add(new HashSet<>());
        propSched.add(new HashSet<>());
        Assert.assertFalse(checkValidity(graph, propSched));
        propSched.get(0).add("1");
        propSched.get(0).add("3");
        Assert.assertFalse(checkValidity(graph, propSched));
        propSched.get(0).add("4");
        propSched.get(1).add("2");
        propSched.get(1).add("5");
        Assert.assertTrue(checkValidity(graph, propSched));
        propSched.get(0).add("5");
        Assert.assertFalse(checkValidity(graph, propSched));

        //Node Implementation
        graph = createGraph3(false);
        propSched = new ArrayList<>();
        propSched.add(new HashSet<>());
        propSched.add(new HashSet<>());
        Assert.assertFalse(checkValidity(graph, propSched));
        propSched.get(0).add("1");
        propSched.get(0).add("3");
        Assert.assertFalse(checkValidity(graph, propSched));
        propSched.get(0).add("4");
        propSched.get(1).add("2");
        propSched.get(1).add("5");
        Assert.assertTrue(checkValidity(graph, propSched));
        propSched.get(0).add("5");
        Assert.assertFalse(checkValidity(graph, propSched));
    }
    @Test
    public void testFindScheduleArray() throws NodeNameExistsException{
        IGraph graph = new EdgeArrayGraph("");
        try {
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
            graph = createGraph2(true);
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
            graph = createGraph3(true);
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
        } catch (NoScheduleException e) {
            Assert.fail();
        }
        try {
            findSchedule(createGraph1(true));
        } catch (NoScheduleException e) {
            Assert.assertTrue(true);
        }
        try {
            findSchedule((createGraph4(true)));
        } catch (NoScheduleException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testFindScheduleNode() throws NodeNameExistsException{
        IGraph graph = new NodeEdgeGraph("");
        try {
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
            graph = createGraph2(false);
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
            graph = createGraph3(false);
            Assert.assertTrue(checkValidity(graph, findSchedule(graph)));
        } catch (NoScheduleException e) {
            Assert.fail();
        }
        try {
            findSchedule(createGraph1(false));
        } catch (NoScheduleException e) {
            Assert.assertTrue(true);
        }
        try {
            findSchedule((createGraph4(false)));
        } catch (NoScheduleException e) {
            Assert.assertTrue(true);
        }
    }
    private IGraph createGraph1(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addUndirectedEdge("1", "2");
        graph.addUndirectedEdge("1", "3");
        graph.addUndirectedEdge("2", "3");
        return graph;
    }

    private IGraph createGraph2(boolean isArray) throws NodeNameExistsException{
        IGraph graph;
        if (isArray) graph = new EdgeArrayGraph("");
        else graph = new NodeEdgeGraph("");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addNode("5");
        graph.addNode("6");
        graph.addUndirectedEdge("1", "2");
        graph.addUndirectedEdge("1", "3");
        graph.addUndirectedEdge("2", "4");
        graph.addUndirectedEdge("3", "5");
        graph.addUndirectedEdge("5", "6");
        graph.addUndirectedEdge("4", "6");
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
        graph.addNode("5");
        graph.addUndirectedEdge("1", "2");
        graph.addUndirectedEdge("2", "3");
        graph.addUndirectedEdge("2", "4");
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
        graph.addUndirectedEdge("1", "2");
        graph.addUndirectedEdge("1", "3");
        graph.addUndirectedEdge("2", "4");
        graph.addUndirectedEdge("3", "5");
        graph.addUndirectedEdge("4", "5");
        return graph;
    }
}
