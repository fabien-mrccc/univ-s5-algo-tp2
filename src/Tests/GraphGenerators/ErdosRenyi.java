package Tests.GraphGenerators;
import Graph.* ;
import Utilities.Searchers.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.Random;

public class ErdosRenyi {

	private Graph graph;
	private final int order;
	private final double edgeProbability;
	private final Random random;

	public ErdosRenyi(int order, float expectedAverageDegree) {
		this.random = new Random();
		this.edgeProbability = Math.max(1.5, expectedAverageDegree) / (order-1);
		this.order = order;
		while (!isConnected()) {
			generateGraph();
		}
	}

	/**
	 * Checks if the graph is connected by performing a breadth-first search.
	 * A graph is considered connected if there is a path from the starting node
	 * to all other nodes.
	 *
	 * @return true if the graph is connected, false otherwise.
	 */
	private boolean isConnected() {
		if (getGraph() == null) return false;
		ArrayList<DirectedEdge> tree = BreadthFirstSearch.generateTree(getGraph(), 0);
		return tree.size() == order() - 1;
	}

	/**
	 * Generates a random graph based on the specified order and edge probability.
	 * It creates a graph by adding non-directed edges between pairs of nodes
	 * with a probability defined by {@link #edgeProbability()}.
	 */
	private void generateGraph() {
		setGraph(new Graph(order()));

		for (int i = 0; i < order(); i++) {
			for (int j = i+1; j < order(); j++) {
				if (getRandom().nextDouble() < edgeProbability())
					getGraph().addEdge(new NonDirectedEdge(i, j, 0));
			}
		}
	}

	private void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return graph;
	}

	public int order() {
		return order;
	}

	private double edgeProbability() {
		return edgeProbability;
	}

	private Random getRandom() {
		return random;
	}
}
