package RandomTrees;

import Graph.Graph;
import Graph.NonDirectedEdge;
import Graph.DirectedEdge;
import Graph.Edge;
import Utilities.Searchers.UnionFind;

import java.util.ArrayList;
import java.util.Random;

/**
 * Random edge insertion algorithm to generate a spanning tree.
 * This algorithm adds edges randomly while avoiding cycles, ensuring a spanning tree is formed.
 */
public class RandomEdgeInsertion {

    /**
     * Generates a random spanning tree for the given graph using random edge insertion.
     *
     * @param graph The input graph from which to generate the spanning tree.
     * @return An ArrayList of edges representing the random spanning tree.
     */
    public static ArrayList<Edge> generateRandomTree(Graph graph) {
        ArrayList<NonDirectedEdge> edges = getAllEdges(graph);
        return randomEdgeInsertion(graph, edges);
    }

    /**
     * Retrieves all the edges from the graph.
     *
     * @param graph The input graph.
     * @return A list of all edges (non-directed) in the graph.
     */
    private static ArrayList<NonDirectedEdge> getAllEdges(Graph graph) {
        ArrayList<NonDirectedEdge> edges = new ArrayList<>();
        for (int i = 0; i < graph.order(); i++) {
            for (DirectedEdge directedEdge : graph.outEdges(i)) {
                NonDirectedEdge nonDirectedEdge = directedEdge.getSupport();
                edges.add(nonDirectedEdge);
            }
        }
        return edges;
    }

    /**
     * Performs random edge insertion to form a spanning tree.
     * Adds edges randomly to a set F, ensuring no cycle is formed.
     *
     * @param graph The graph from which edges are chosen.
     * @param edges A list of all edges in the graph.
     * @return A list of edges forming the spanning tree.
     */
    private static ArrayList<Edge> randomEdgeInsertion(Graph graph, ArrayList<NonDirectedEdge> edges) {
        UnionFind unionFind = new UnionFind(graph.order());
        ArrayList<Edge> spanningTree = new ArrayList<>();
        Random random = new Random();

        while (spanningTree.size() < graph.order() - 1) {
            NonDirectedEdge edge = edges.get(random.nextInt(edges.size()));

            if (unionFind.union(edge.getSource(), edge.getDestination())) {
                spanningTree.add(edge);
            }
        }
        return spanningTree;
    }
}
