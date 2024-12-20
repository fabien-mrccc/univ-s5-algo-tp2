package RandomTrees;

import Graph.Graph;
import Graph.NonDirectedEdge;
import Graph.DirectedEdge;
import Graph.Edge;
import Utilities.Searchers.UnionFind;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class MinimumWeightSpanningTree {

    /**
     * Generates a random spanning tree for the given graph.
     *
     * @param graph The input graph for which a random spanning tree is to be generated.
     * @return An ArrayList of Edges representing the random minimum weight spanning tree.
     */
    public static ArrayList<Edge> generateRandomTree(Graph graph) {
        ArrayList<NonDirectedEdge> edges = assignRandomWeightsToEdges(graph);
        return kruskal(edges);
    }

    /**
     * Assigns random weights between 0 and 1 to the edges of the graph and returns them as a list.
     *
     * @param graph The input graph whose edges will be assigned random weights.
     * @return A list of edges with random weights assigned.
     */
    private static ArrayList<NonDirectedEdge> assignRandomWeightsToEdges(Graph graph) {
        Random random = new Random();
        ArrayList<NonDirectedEdge> edges = new ArrayList<>();
        Set<NonDirectedEdge> edgeSet = new HashSet<>();

        for (int i = 0; i < graph.order(); i++) {
            for (DirectedEdge directedEdge : graph.outEdges(i)) {
                NonDirectedEdge nonDirectedEdge = directedEdge.getSupport();
                if (!edgeSet.contains(nonDirectedEdge)) {
                    edgeSet.add(nonDirectedEdge);
                    edges.add(new NonDirectedEdge(nonDirectedEdge.getSource(),
                            nonDirectedEdge.getDestination(),
                            random.nextDouble()));
                }
            }
        }
        return edges;
    }

    /**
     * Constructs a minimum spanning tree using Kruskal's algorithm.
     *
     * @param edges A sorted list of edges (by weight) to process for the spanning tree.
     * @return An ArrayList of Edges representing the minimum spanning tree.
     */
    private static ArrayList<Edge> kruskal(ArrayList<NonDirectedEdge> edges) {
        UnionFind unionFind = new UnionFind(edges.size());
        ArrayList<Edge> spanningTree = new ArrayList<>();

        PriorityQueue<NonDirectedEdge> edgeQueue = new PriorityQueue<>(edges);

        while (!edgeQueue.isEmpty()) {
            NonDirectedEdge edge = edgeQueue.poll();
            if (unionFind.union(edge.getSource(), edge.getDestination())) {
                spanningTree.add(edge);
            }
            if (spanningTree.size() == edges.size() - 1) {
                break;
            }
        }
        return spanningTree;
    }
}
