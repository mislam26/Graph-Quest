package sol;

import java.util.*;

import src.NoScheduleException;

/**
 * Class containing methods for scheduling using graphs
 */
public class Scheduler {

    /**
     * Empty Constructor
     */
    public Scheduler() {}

    /**
     * Method which checks if a given allocation of labs adheres to
     *     the scheduling constraints of the graph. Assumes that
     *     all lab names in proposedAlloc are valid labels in theGraph.
     *
     * @param theGraph the graph to try to schedule
     * @param proposedAlloc the proposed allocation of labs between Kathi and Elijah
     * @return boolean indicating whether the proposed allocation is valid
     */
    public static boolean checkValidity(IGraph theGraph, ArrayList<HashSet<String>> proposedAlloc) {
        HashSet<String> allNodes = theGraph.getAllNodes();
        if (proposedAlloc.size() != 2) {
            return false;
        }
        HashSet<String> mergedSchedule = new HashSet<>();
        mergedSchedule.addAll(proposedAlloc.get(0));
        mergedSchedule.addAll(proposedAlloc.get(1));
        if (mergedSchedule.size() != proposedAlloc.get(0).size() + proposedAlloc.get(1).size()) {
            return false;
        }
        for (String node : allNodes) {
            if (!proposedAlloc.get(0).contains(node) && !proposedAlloc.get(1).contains(node)) {
                return false;
            }
        }
        for (HashSet schedule : proposedAlloc) {
            if (checkHasConflicts(theGraph, schedule)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to compute a valid split of the graph nodes
     *     without violating scheduling constraints,
     *     if such a split exists
     * Throws a NoScheduleException if no such split exists
     *
     * @param theGraph the graph to try to schedule
     * @return an ArrayList of HashSets of node labels that constitute a
     *         valid split of the graph
     * @throws NoScheduleException if no such split exists
     */
    public static ArrayList<HashSet<String>> findSchedule(IGraph theGraph)
            throws NoScheduleException {
        ArrayList<HashSet<String>> schedule = new ArrayList<>();
        schedule.add(new HashSet<>());
        schedule.add(new HashSet<>());
        int currentSchedule = 0;
        HashMap<String, Integer> scheduleMap = new HashMap();
        Queue<String> toCheck = new LinkedList<>();
        for (String node : theGraph.getAllNodes()) {
            if (schedule.get(0).contains(node) || schedule.get(1).contains(node)) {
                continue;
            }
            schedule.get(currentSchedule).add(node);
            currentSchedule = Math.abs(currentSchedule - 1);
            for (String firstNeighbor : theGraph.getNeighbors(node)) {
                toCheck.add(firstNeighbor);
                scheduleMap.put(firstNeighbor, currentSchedule);
            }
            while (!toCheck.isEmpty()) {
                String neighbor = toCheck.remove();
                if (schedule.get(0).contains(neighbor) || schedule.get(1).contains(neighbor)) {
                    continue;
                }
                for (String name : theGraph.getNeighbors(neighbor)) {
                    if (schedule.get(scheduleMap.get(neighbor)).contains(name)) {
                        throw new NoScheduleException();
                    }
                }
                schedule.get(scheduleMap.get(neighbor)).add(neighbor);
                for (String nextNeighbor : theGraph.getNeighbors(neighbor)) {
                    if (!schedule.get(0).contains(nextNeighbor) && !schedule.get(1).contains(nextNeighbor)) {
                        scheduleMap.put(nextNeighbor, Math.abs(scheduleMap.get(neighbor) - 1));
                        toCheck.add(nextNeighbor);
                    }
                }
            }
        }
        return schedule;
    }

    /**
     * Helper method for checking of any conflicts in a schedule
     *
     * @param graph graph to check conflicts of its schedule
     * @param schedule schedule of one person for the graph
     * @return true if no conflicts found within the schedule provided for the graph else false
     */
    private static boolean checkHasConflicts(IGraph graph, HashSet<String> schedule) {
        for (String node : schedule) {
            LinkedList<String> neighbors = graph.getNeighbors(node);
            for (String neighbor : neighbors) {
                if (schedule.contains(neighbor)) {
                    return true;
                }
            }
        }
        return false;
    }
}
