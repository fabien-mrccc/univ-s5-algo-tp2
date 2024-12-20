package Graph;

import java.util.*;

public class Graph {

    private final int upperBound;
    private int order;
    private int edgeCardinality;

    private final Map<Integer,List<NonDirectedEdge>> incidence;
    private final Map<Integer,List<DirectedEdge>> inIncidence;
    private final Map<Integer,List<DirectedEdge>> outIncidence;

    public Graph(int upperBound) {
        this.upperBound = upperBound;
        this.order = 0;
        this.edgeCardinality = 0;
        this.incidence = new HashMap<>();
        this.inIncidence = new HashMap<>();
        this.outIncidence = new HashMap<>();

        initializeIncidences();
    }

    /**
     * Initializes the incidence lists with null objects.
     */
    private void initializeIncidences() {
        for (int index=0; index < upperBound(); index++) {
            getIncidence().put(index, null);
            getInIncidence().put(index, null);
            getOutIncidence().put(index, null);
        }
    }

    /**
     * Checks if the given vertex is valid and active in the graph.
     *
     * @param vertex the vertex to check.
     * @return true if the vertex is within bounds and active, false otherwise.
     */
    public boolean isVertex(int vertex) {
        return isNotOutOfBounds(vertex) && isVertexActive(vertex);
    }

    /**
     * Ensures that the given vertex is present and active in the graph.
     * If the vertex is not active, it adds the vertex to the graph.
     *
     * @param vertex the vertex to ensure.
     */
    public void ensureVertex(int vertex) {
        if (isNotOutOfBounds(vertex) && !isVertexActive(vertex)) {
            addVertex(vertex);
        }
    }

    /**
     * Adds a new vertex to the graph, initializing its incidence, incoming, and outgoing lists.
     *
     * @param vertex the vertex to add.
     */
    private void addVertex(int vertex) {
        getIncidence().put(vertex, new LinkedList<>());
        getInIncidence().put(vertex, new LinkedList<>());
        getOutIncidence().put(vertex, new LinkedList<>());
        incrementOrder();
    }

    /**
     * Removes the specified vertex and all its associated edges from the graph.
     * Updates the edge cardinality and graph order accordingly.
     *
     * @param vertex the vertex to delete.
     */
    public void deleteVertex(int vertex) {
        int numberOfEdgesToRemove = getIncidence().get(vertex).size();

        removeEdges(convertIncidenceMap(getIncidence()), vertex);
        removeEdges(convertIncidenceMap(getInIncidence()), vertex);
        removeEdges(convertIncidenceMap(getOutIncidence()), vertex);

        getIncidence().remove(vertex);
        getInIncidence().remove(vertex);
        getOutIncidence().remove(vertex);
        decreaseEdgeCardinality(numberOfEdgesToRemove);
        decrementOrder();
    }

    /**
     * Converts a map of incidence lists from a specific edge type to a map of general edge lists.
     *
     * @param incidence the map of incidence lists with a specific edge type.
     * @param <T> the type of edges (subclass of Edge).
     * @return a new map where each list is of type List<Edge>.
     */
    private <T extends Edge> Map<Integer, List<Edge>> convertIncidenceMap(Map<Integer, List<T>> incidence) {
        Map<Integer, List<Edge>> incidenceAsList = new HashMap<>();

        incidence.forEach((key, value) -> {
            if (value != null) {
                incidenceAsList.put(key, new LinkedList<>(value));
            } else {
                incidenceAsList.put(key, null);
            }
        });
        return incidenceAsList;
    }

    /**
     * Removes all edges incident to the specified vertex from the incidence list
     * of the opposite vertex.
     *
     * @param incidence the map of incidence lists (vertex -> list of edges).
     * @param vertex the vertex whose edges are to be removed from the opposite vertex's incidence list.
     */
    private void removeEdges(Map<Integer, List<Edge>> incidence, int vertex) {
        List<Edge> edges = incidence.get(vertex);

        if (edges != null) {
            for (Edge edge : edges) {
                List<Edge> oppositeEdges = incidence.get(edge.oppositeExtremity(vertex));

                if (oppositeEdges != null) {
                    oppositeEdges.remove(edge);
                }
            }
        }
    }

    /**
     * Adds an edge to the graph, handling both directed and non-directed edges.
     * Updates the incidence lists and increments the edge cardinality.
     *
     * @param edge the edge to be added (directed or non-directed).
     */
    public void addEdge(Edge edge) {
        int source = edge.getSource();
        int destination = edge.getDestination();

        ensureVertex(source);
        ensureVertex(destination);

        boolean edgeAdded = false;

        if (edge instanceof DirectedEdge) {
            edgeAdded = addDirectedEdge((DirectedEdge) edge, source, destination);
        } else if (edge instanceof NonDirectedEdge) {
            edgeAdded = addNonDirectedEdge((NonDirectedEdge) edge, source, destination);
        }

        if (edgeAdded) {
            incrementEdgeCardinality();
        }
    }

    /**
     * Adds a directed edge to the graph if it is not already present.
     *
     * @param directedEdge the directed edge to add.
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @return true if the edge was added, false if it was already present.
     */
    private boolean addDirectedEdge(DirectedEdge directedEdge, int source, int destination) {
        if (!getOutIncidence().get(source).contains(directedEdge)) {
            getIncidence().get(source).add(directedEdge.getSupport());
            getIncidence().get(destination).add(directedEdge.getSupport());
            getOutIncidence().get(source).add(directedEdge);
            getInIncidence().get(destination).add(directedEdge);
            return true;
        }
        return false;
    }

    /**
     * Adds a non-directed edge to the graph if it is not already present.
     *
     * @param nonDirectedEdge the non-directed edge to add.
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @return true if the edge was added, false if it was already present.
     */
    private boolean addNonDirectedEdge(NonDirectedEdge nonDirectedEdge, int source, int destination) {
        if (!getIncidence().get(source).contains(nonDirectedEdge)) {
            DirectedEdge directedEdgeFromSource = new DirectedEdge(nonDirectedEdge, false);
            DirectedEdge directedEdgeFromDestination = new DirectedEdge(nonDirectedEdge, true);

            getIncidence().get(source).add(nonDirectedEdge);
            getIncidence().get(destination).add(nonDirectedEdge);
            getOutIncidence().get(source).add(directedEdgeFromSource);
            getInIncidence().get(destination).add(directedEdgeFromSource);
            getOutIncidence().get(destination).add(directedEdgeFromDestination);
            getInIncidence().get(source).add(directedEdgeFromDestination);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of directed edges leaving the specified vertex.
     *
     * @param vertex the vertex whose outgoing edges are to be retrieved.
     * @return a list of outgoing directed edges from the vertex.
     */
    public ArrayList<DirectedEdge> outEdges(int vertex) {
        if (!isVertex(vertex)) {
            return null;
        }
        return new ArrayList<>(getOutIncidence().get(vertex));
    }

    /**
     * Checks if the given vertex is within the valid bounds of the graph.
     *
     * @param vertex the vertex to check.
     * @return true if the vertex is within bounds, false otherwise.
     */
    private boolean isNotOutOfBounds(int vertex) {
        return vertex >= 0 && vertex < upperBound();
    }

    /**
     * Checks if the given vertex is active by verifying that its incidence lists are not null.
     *
     * @param vertex the vertex to check.
     * @return true if the vertex is active (i.e., its incidence lists are not null), false otherwise.
     */
    private boolean isVertexActive(int vertex) {
        return getIncidence().get(vertex) != null
                && getInIncidence().get(vertex) != null
                && getOutIncidence().get(vertex) != null;
    }

    public int order() {
        return order;
    }

    private int upperBound() {
        return upperBound;
    }

    private Map<Integer,List<NonDirectedEdge>> getIncidence() {
        return incidence;
    }

    private Map<Integer,List<DirectedEdge>> getInIncidence() {
        return inIncidence;
    }

    private Map<Integer,List<DirectedEdge>> getOutIncidence() {
        return outIncidence;
    }

    private void incrementEdgeCardinality() {
        edgeCardinality++;
    }

    private void incrementOrder() {
        order++;
    }

    private void decrementOrder() {
        order--;
    }

    private void decreaseEdgeCardinality(int quantity) {
        edgeCardinality -= quantity;
    }

    public int edgeCardinality() {
        return edgeCardinality;
    }

    public int getUpperBound() { return upperBound;}
}
