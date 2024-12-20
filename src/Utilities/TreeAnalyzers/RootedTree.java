package Utilities.TreeAnalyzers;

import Graph.DirectedEdge;
import Graph.NonDirectedEdge;
import Graph.Graph;
import Utilities.Searchers.BreadthFirstSearch;

import java.util.*;

public class RootedTree {

	// to write recursive algorithms without recursion
	private final ArrayList<RootedTreeNode> inverseBfsOrder;
	private final ArrayList<RootedTreeNode> bfsOrder;

	private final RootedTreeNode[] rootedTreeNodes;
	private RootedTreeNode root;
	private final int order;

	// Building the tree from list of arcs.
	// We want the center of the tree as root.
	// 1) createTree: Gets a tree encoded in the RootedTreeNode structure.
	//    This is done by using bfs algorithm on the graph of nonDirectedEdges.
	//    From the bfs list of arcs, creates each node and attach it to father
	//    Stores each node in an array indexed by vertices.
	// 2) Computes the height of every node, in inverse bfs order.
	// 3) rerootTree: Moves root toward center.
	//    the two highest sons must have almost the same height.
	//    it detects if it is balanced,
	//    and if not moves the root to the highest son (swapRootWith)
	// 4) resetBfsOrdering : recomputes bfs and inverse bfs order.
	//    this time, a bfs on the node structure is enough
	// 5) Computes height, size and depth of every node.
	public RootedTree(ArrayList<NonDirectedEdge> nonDirectedEdges, int root) {
		this.order = nonDirectedEdges.size() + 1;
		this.bfsOrder = new ArrayList<>(getOrder());
		this.inverseBfsOrder = new ArrayList<>(getOrder());
		this.rootedTreeNodes = new RootedTreeNode[getOrder()];
		this.root = new RootedTreeNode(root);

		Graph graph = new Graph(getOrder());
		for (NonDirectedEdge e : nonDirectedEdges) {
			graph.addEdge(e);
		}

		createTree(root, BreadthFirstSearch.generateTree(graph, root));

		rerootTree();
		computeAllHeights();
		computeAllSizes();
		computeAllDepths();
	}

	/**
	 * Creates a tree starting from the given root node by processing a sorted list of directed edges.
	 * It populates the BFS order and inverse BFS order of rootedTreeNodes.
	 *
	 * @param root                The root node for the tree.
	 * @param sortedDirectedEdges A sorted list of directed edges used to build the tree.
	 */
	private void createTree(int root, ArrayList<DirectedEdge> sortedDirectedEdges) {
		setNode(root, new RootedTreeNode(root));
		getBfsOrder().add(getNode(root));
		for (DirectedEdge directedEdge : sortedDirectedEdges) {
			createNode(directedEdge);
			getBfsOrder().add(getNode(directedEdge.getDestination()));
		}
		getInverseBfsOrder().addAll(getBfsOrder());
		Collections.reverse(getInverseBfsOrder());
	}

	/**
	 * Creates a new node for the destination vertex of the directed edge and adds it as a child
	 * to the node corresponding to the source vertex of the edge.
	 *
	 * @param directedEdge The directed edge that defines the parent-child relationship between rootedTreeNodes.
	 */
	private void createNode(DirectedEdge directedEdge) {
		int son = directedEdge.getDestination();
		int father = directedEdge.getSource();
		setNode(son, new RootedTreeNode(son));
		getNode(father).getSons().add(getNode(son));
	}

	/**
	 * Reroots the tree by repeatedly swapping the root with the child node having the maximum height
	 * until the tree is balanced. After the rerooting, it resets the BFS ordering.
	 */
	private void rerootTree() {
		computeAllHeights();
		while (isUnbalanced())
			swapRootWith(getRootNode().maxHeightSon());
		resetBfsOrdering();
	}

	/**
	 * Checks if the tree is unbalanced by comparing the height of the root node with its second maximum height.
	 * The tree is considered unbalanced if the difference is greater than 2.
	 *
	 * @return True if the tree is unbalanced, false otherwise.
	 */
	private boolean isUnbalanced() {
		return getRootNode().getHeight() > getRootNode().secondMaxHeight() + 2;
	}

	/**
	 * Swaps the current root node with the given child node (son).
	 * The current root is removed from its list of children and the height of the new root and old root are recalculated.
	 * The new root (son) becomes the root of the tree.
	 *
	 * @param son The child node to swap with the current root.
	 */
	private void swapRootWith(RootedTreeNode son) {
		getRootNode().getSons().remove(son);
		getRootNode().setHeight();
		son.setHeight(Math.max(getRootNode().getHeight() + 1, son.getHeight()));
		son.getSons().add(getRootNode());
		setRootNode(son);
	}

	/**
	 * Resets the BFS ordering of the tree by performing a breadth-first traversal starting from the root node.
	 * The BFS order is stored in `bfsOrder`, and the reverse BFS order is stored in `inverseBfsOrder`.
	 */
	private void resetBfsOrdering() {
		Queue<RootedTreeNode> stack = new LinkedList<>();
		stack.offer(getRootNode());
		getBfsOrder().clear();
		getInverseBfsOrder().clear();
		RootedTreeNode current;
		while (!stack.isEmpty()) {
			current = stack.poll();
			for (RootedTreeNode son : current.getSons()) stack.offer(son);
			getBfsOrder().add(current);
			getInverseBfsOrder().add(current);
		}
		Collections.reverse(getInverseBfsOrder());
	}

	/**
	 * Computes the height for all rootedTreeNodes by iterating through the inverse BFS order.
	 */
	private void computeAllHeights() {
		for (RootedTreeNode n : getInverseBfsOrder()) n.setHeight();
	}

	/**
	 * Computes the size for all rootedTreeNodes by iterating through the inverse BFS order.
	 */
	private void computeAllSizes() {
		for (RootedTreeNode n : getInverseBfsOrder()) n.setSize();
	}

	/**
	 * Computes the depth for all rootedTreeNodes, starting from the root node with depth 0,
	 * and then setting the depth of its descendants by iterating through the BFS order.
	 */
	private void computeAllDepths() {
		getRootNode().setDepth(0);
		for (RootedTreeNode n : getBfsOrder()) n.setSonsDepth();
	}

	/**
	 * Calculates the degree distribution of the tree.
	 * The degree of a node is the number of its neighbors, which for a tree is
	 * the number of its direct children (sons). The root node's degree is
	 * counted differently, as it has no parent.
	 * The method returns the number of rootedTreeNodes for each degree in the range [0, maxDegree].
	 *
	 * @param maxDegree The maximum degree to consider for the distribution.
	 * @return An array where the index represents the degree, and the value at each index
	 *         is the count of rootedTreeNodes with that degree. The array is of size maxDegree + 1.
	 */
	public int[] getDegreeDistribution(int maxDegree) {
		int maxIndex = Math.min(maxDegree, getOrder() - 1);
		int[] degrees = new int[1 + maxIndex];
		for (int i = 0; i <= maxIndex; i++) degrees[i] = 0;
		int degree;
		for (RootedTreeNode n : getBfsOrder()) {
			degree = n.getSons().size() + (n == getRootNode() ? 0 : 1);
			if (degree <= maxIndex)
				degrees[degree]++;
		}
		return degrees;
	}

	/**
	 * Calculates the Wiener index of the tree.
	 * The Wiener index is a topological index of a graph, defined as the sum of the shortest path lengths
	 * between all pairs of rootedTreeNodes. In the case of a tree, it is computed as the sum of the products of the sizes
	 * of the subtrees of each node (excluding the root node), multiplied by the size of the remaining part of the tree.
	 *
	 * @return The Wiener index of the tree, which is a long value representing the sum of distances between pairs of rootedTreeNodes.
	 */
	public long getWienerIndex() {
		long count = 0;
		for (RootedTreeNode n : getBfsOrder()) {
			if (n == getRootNode()) continue;
			count = count + (long) n.getSize() * (getOrder() - n.getSize());
		}
		return count;
	}

	/**
	 * Prints various statistical properties of the graph/tree, including order, diameter, radius,
	 * Wiener index, distance from center to centroid, and average eccentricity.
	 */
	public void printStats() {
		System.out.println("Order: " + getOrder());
		System.out.println("Diameter: " + getDiameter());
		System.out.println("Radius: " + getRadius());
		System.out.println("Wiener index: " + getWienerIndex());
		System.out.println("Center to centroid: "
				+ getDistanceFromCenterToCentroid());
		System.out.println("Average eccentricity: "
				+ getAverageEccentricity());
	}

	/**
	 * Prints the details of the node identified by the given vertex index.
	 *
	 * @param vertex The index of the vertex whose node details are to be printed.
	 */
	public void printNode(int vertex) {
		getNode(vertex).print();
	}

	/**
	 * Prints the details of all rootedTreeNodes in the tree, following the BFS order.
	 */
	public void printTree() {
		for (RootedTreeNode n : getBfsOrder()) n.print();
	}

	/**
	 * Calculates the diameter of the tree.
	 * The diameter is the longest path between any two rootedTreeNodes in the tree.
	 * It is approximated by the sum of the height of the root node and its second maximum height.
	 *
	 * @return The diameter of the tree.
	 */
	public int getDiameter() {
		return getRootNode().getHeight() + getRootNode().secondMaxHeight() + 1;
	}

	/**
	 * Calculates the radius of the tree.
	 * The radius is defined as the shortest distance from the center of the tree to any node.
	 * It is approximated by the height of the root node.
	 *
	 * @return The radius of the tree.
	 */
	public int getRadius() {
		return getRootNode().getHeight();
	}

	/**
	 * Calculates the average eccentricity of all rootedTreeNodes in the tree.
	 * Eccentricity of a node is defined as the greatest distance from the node to any other node in the tree.
	 * In this case, it is approximated by the depth of each node in the BFS order.
	 *
	 * @return The average eccentricity of all rootedTreeNodes in the tree.
	 */
	public double getAverageEccentricity() {
		int sumEccentricity = 0;
		for (RootedTreeNode n : getBfsOrder())
			sumEccentricity = sumEccentricity + n.getDepth();
		return (double) sumEccentricity / (double) getOrder();
	}

	/**
	 * Calculates the distance from the center of the tree to the centroid.
	 * The distance is defined as the depth of the centroid node in the tree.
	 *
	 * @return The depth of the centroid node, which approximates the distance from the center to the centroid.
	 */
	public int getDistanceFromCenterToCentroid() {
		return getCentroidNode().getDepth();
	}

	/**
	 * Finds the centroid node of the tree.
	 * The centroid is the node such that if it is chosen as the root,
	 * no subtree of the tree will have more than half of the total number of rootedTreeNodes.
	 * This is achieved by iteratively selecting the child node with the largest size until the condition is satisfied.
	 *
	 * @return The centroid node of the tree.
	 */
	private RootedTreeNode getCentroidNode() {
		RootedTreeNode centroid = getRootNode();
		while (centroid.maxSizeSon().getSize() * 2 > getOrder())
			centroid = centroid.maxSizeSon();
		return centroid;
	}

	private ArrayList<RootedTreeNode> getInverseBfsOrder() {
		return inverseBfsOrder;
	}

	private ArrayList<RootedTreeNode> getBfsOrder() {
		return bfsOrder;
	}

	private RootedTreeNode getNode(int value) {
		return rootedTreeNodes[value];
	}

	private void setNode(int value, RootedTreeNode node) {
		rootedTreeNodes[value] = node;
	}

	private int getOrder() {
		return order;
	}

	private RootedTreeNode getRootNode() {
		return root;
	}

	private void setRootNode(RootedTreeNode value) {
		this.root = value;
	}

	// RootedTreeNode accessors

	public int getRoot() { return getRootNode().getVertex(); }

	public int getHeight(int vertex) {
		return getNode(vertex).getHeight();
	}

	public int getDepth(int vertex) {
		return getNode(vertex).getDepth();
	}

	public int getSubtreeSize(int vertex) {
		return getNode(vertex).getSize();
	}

	public int getCentroid() {
		return getCentroidNode().getVertex();
	}
}
