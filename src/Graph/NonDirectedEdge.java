package Graph;

public class NonDirectedEdge implements Comparable<NonDirectedEdge>, Edge {

	private final int source;
	private final int destination;
	private final double weight;
	
	public NonDirectedEdge(int source, int destination, double weight) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	@Override
	public int getSource() {
		return source;
	}

	@Override
	public int getDestination() {
		return destination;
	}

	@Override
	public int oppositeExtremity(int vertex) {
		if (vertex == getSource()) {
			return getDestination();
		}
		else if (vertex == getDestination()) {
			return getSource();
		}
		else {
			return vertex;
		}
	}

	@Override
	public int compareTo(NonDirectedEdge e) {
		return Double.compare(this.weight, e.weight);
	}
}
