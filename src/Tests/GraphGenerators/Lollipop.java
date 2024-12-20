package Tests.GraphGenerators;
import Graph.*;

import java.util.ArrayList;
import java.util.Collections;

public class Lollipop {

	private final Graph graph;

	public Lollipop(int order) {
		graph = new Graph(order);
		ArrayList<Integer> permutation = generateShuffledPermutation(order);
		int t = order / 3;
		addEdgesForLollipopGraph(permutation, t, order);
	}

	/**
	 * Generates a shuffled permutation of vertex indices.
	 *
	 * @param order The number of vertices.
	 * @return A shuffled list of vertex indices.
	 */
	private ArrayList<Integer> generateShuffledPermutation(int order) {
		ArrayList<Integer> permutation = new ArrayList<>(order);
		for (int i = 0; i < order; i++) {
			permutation.add(i);
		}
		Collections.shuffle(permutation);
		return permutation;
	}

	/**
	 * Adds edges to the graph to form a Lollipop structure.
	 *
	 * @param permutation The shuffled list of vertices.
	 * @param t The number of vertices for the initial part of the graph.
	 * @param order The total number of vertices in the graph.
	 */
	private void addEdgesForLollipopGraph(ArrayList<Integer> permutation, int t, int order) {
		for (int i = 0; i < t; i++) {
			getGraph().addEdge(new NonDirectedEdge(permutation.get(i), permutation.get(i + 1), 0));
		}
		for (int i = t; i < order; i++) {
			for (int j = i + 1; j < order; j++) {
				getGraph().addEdge(new NonDirectedEdge(permutation.get(i), permutation.get(j), 0));
			}
		}
	}

	public Graph getGraph() {
		return graph;
	}
}

