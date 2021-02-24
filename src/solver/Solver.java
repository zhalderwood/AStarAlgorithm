package solver;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Solver {
    private int searchDepth = 0;
    private Puzzle previous, dequeuedNode;
    private NodeSearch bestNode;
    private Iterable<Puzzle> nodes;
    private Stack<Puzzle> puzzles;
    private Set<Puzzle> prevNodes = new HashSet<>(50);
    private PriorityQueue<NodeSearch> minimumPQ;


    // constructor
    public Solver(Puzzle node) {
        dequeuedNode = node;
        minimumPQ = new PriorityQueue<>(node.getSize() + 10);
        nodes = dequeuedNode.nextNodes();
        previous = null;
        bestNode = new NodeSearch(node, 0, null);
    }

    // A-Star Algorithm!
    // Creates next set of nodes and finds the best one by calling NodeSearch, then adds it to a priority queue
    public Puzzle nextSolution() {
        searchDepth++;
        nodes = dequeuedNode.nextNodes();
        for (Puzzle currentNode : nodes) {
            if (!currentNode.equals(previous) && !prevNodes.contains(currentNode)) {
                minimumPQ.add(new NodeSearch(currentNode, searchDepth, bestNode));
            }
        }
        prevNodes.add(previous);
        previous = dequeuedNode;
        bestNode = minimumPQ.poll();
        dequeuedNode = bestNode.current;
        return dequeuedNode;
    }

    // returns searchDepth
    public int searchDepth() {
        return searchDepth;
    }

    // checks to see which of two nodes is best candidate for next node, keeps the best one
    // f(n) = g(n) + h(n)
    private class NodeSearch implements Comparable<NodeSearch> {
        private final int priority;
        private final NodeSearch previous;
        private final Puzzle current;

        public NodeSearch(Puzzle current, int searchDepth, NodeSearch previous) {
            this.current = current;
            this.previous = previous;
            this.priority = searchDepth + current.getManhattan(); // best candidate calculation
        }

        @Override
        public int compareTo(NodeSearch that) {
            int cmp = this.priority - that.priority;
            return Integer.compare(cmp, 0);
        }
    }
}