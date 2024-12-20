package Utilities.Searchers;

import Graph.DirectedEdge;
import Graph.Graph;

import java.util.*;


public class BreadthFirstSearch {

	private final Graph graph;
	private final Queue<DirectedEdge> frontier;
	private final ArrayList<DirectedEdge> tree;
	private final BitSet reached;

	private BreadthFirstSearch (Graph graph) {
		this.graph = graph;
		this.frontier = new LinkedList<>();
		this.tree = new ArrayList<>();
		this.reached = new BitSet(graph.order());
	}

	/**
	 * Generates a spanning tree of the graph starting from the specified root vertex using breadth-first search (BFS).
	 * The tree consists of the edges traversed during BFS.
	 *
	 * @param graph The graph to traverse.
	 * @param root The root vertex from which BFS starts.
	 * @return A list of directed edges representing the BFS tree.
	 */
	public static ArrayList<DirectedEdge> generateTree(Graph graph, int root) {
		BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(graph);
		breadthFirstSearch.execute(root);
		return breadthFirstSearch.getTree();
	}

	/**
	 * Executes the breadth-first search algorithm starting from the specified vertex.
	 * It explores all reachable vertices and builds the BFS tree.
	 *
	 * @param startingVertex The vertex from which BFS starts.
	 */
	private void execute(int startingVertex) {
		getReached().set(startingVertex);
		push(startingVertex);
		while (!getFrontier().isEmpty()) {
			explore(getFrontier().poll());
		}
	}

	/**
	 * Adds all outgoing edges from the specified vertex to the frontier (queue).
	 *
	 * @param vertex The vertex whose outgoing edges are added to the frontier.
	 */
	private void push(int vertex) {
		for (DirectedEdge directedEdge : getGraph().outEdges(vertex)) getFrontier().offer(directedEdge);
	}

	/**
	 * Explores the next directed edge by marking its destination as reached
	 * and adding the edge to the BFS tree if the destination has not been visited.
	 *
	 * @param nextDirectedEdge The directed edge to explore.
	 */
	private void explore(DirectedEdge nextDirectedEdge) {
		if (getReached().get(nextDirectedEdge.getDestination())) return;
		getReached().set(nextDirectedEdge.getDestination());
		getTree().add(nextDirectedEdge);
		push(nextDirectedEdge.getDestination());
	}

	public Graph getGraph() {
		return graph;
	}

	private BitSet getReached() {
		return reached;
	}

	private ArrayList<DirectedEdge> getTree() {
		return tree;
	}

	private Queue<DirectedEdge> getFrontier() {
		return frontier;
	}
}
