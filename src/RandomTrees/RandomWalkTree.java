package RandomTrees;
import Graph.*;

import java.util.*;

public class RandomWalkTree {

    /**
     * Generates a random spanning tree for the given graph using a randomized traversal approach.
     *
     * @param graph       The input graph for which the random spanning tree is generated.
     * @param randomStart If true, starts from a random vertex; otherwise, starts from vertex 0.
     * @return A list of edges representing the random spanning tree.
     */
    public static ArrayList<Edge> generateRandomTree(Graph graph, boolean randomStart) {
        Random random = new Random();
        ArrayList<Edge> spanningTree = new ArrayList<>();
        boolean[] visited = new boolean[graph.order()];
        ArrayDeque<DirectedEdge> frontier = new ArrayDeque<>();

        initializeStartVertex(graph, randomStart, random, visited, frontier);

        while (!frontier.isEmpty()) {
            processFrontier(graph, frontier, visited, spanningTree, random);
        }

        return spanningTree;
    }

    /**
     * Initializes the start vertex for the spanning tree generation.
     *
     * @param graph      The graph containing the vertices and edges.
     * @param randomStart If true, selects a random start vertex; otherwise, starts from vertex 0.
     * @param random     The Random object.
     * @param visited    An array to mark the visited status of vertices.
     * @param frontier   The frontier to which outgoing edges of the start vertex are added.
     */
    private static void initializeStartVertex(Graph graph, boolean randomStart, Random random, boolean[] visited, ArrayDeque<DirectedEdge> frontier) {
        int startVertex = 0;
        if (randomStart) {
            startVertex = random.nextInt(graph.order());
        }
        visited[startVertex] = true;
        addOutgoingEdgesToFrontier(graph, startVertex, frontier, random);
    }

    /**
     * Processes the frontier to expand the spanning tree by adding a randomly selected edge.
     *
     * @param graph        The graph containing the edges.
     * @param frontier     The frontier of edges to be processed.
     * @param visited      An array indicating which vertices have been visited.
     * @param spanningTree The list of edges forming the spanning tree.
     * @param random       The Random object.
     */
    private static void processFrontier(Graph graph, ArrayDeque<DirectedEdge> frontier, boolean[] visited, ArrayList<Edge> spanningTree, Random random) {
        DirectedEdge directedEdge = frontier.poll();
        if (directedEdge == null) return;

        int target = directedEdge.getDestination();
        if (!visited[target]) {
            visited[target] = true;
            spanningTree.add(directedEdge.getSupport());
            addOutgoingEdgesToFrontier(graph, target, frontier, random);
        }
    }

    /**
     * Adds the outgoing edges of a given vertex to the frontier, in random order.
     *
     * @param graph    The graph containing the edges.
     * @param vertex   The vertex whose outgoing edges are to be added.
     * @param frontier The frontier to which the edges will be added.
     * @param random   The Random object.
     */
    private static void addOutgoingEdgesToFrontier(Graph graph, int vertex, ArrayDeque<DirectedEdge> frontier, Random random) {
        List<DirectedEdge> outgoingEdges = new ArrayList<>(graph.outEdges(vertex));
        Collections.shuffle(outgoingEdges, random);
        frontier.addAll(outgoingEdges);
    }
}
