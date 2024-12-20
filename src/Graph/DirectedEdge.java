package Graph;

public class DirectedEdge implements Edge {

	private final NonDirectedEdge support;
	private boolean reversed;
	
	public DirectedEdge(NonDirectedEdge e, boolean reversed) {
		this.support = e;
		setReversed(reversed);
	}

	@Override
	public int getSource() {
		return (reversed ? support.getDestination() : support.getSource());
	}

	@Override
	public int getDestination() {
		return (reversed ? support.getSource() : support.getDestination());
	}

	@Override
	public int oppositeExtremity(int vertex) {
		return getSupport().oppositeExtremity(vertex);
	}

	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}

	public NonDirectedEdge getSupport() {
		return support;
	}
}
