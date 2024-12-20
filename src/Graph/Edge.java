package Graph;

public interface Edge {

    /**
     * Returns the source vertex of the edge.
     *
     * @return the source vertex.
     */
    int getSource();

    /**
     * Returns the destination vertex of the edge.
     *
     * @return the destination vertex.
     */
    int getDestination();

    /**
     * Returns the opposite extremity (vertex) of the edge given one of its vertices.
     * If the provided vertex is the source, the destination is returned, and vice versa.
     * If the vertex is not a source or a destination, returns it.
     *
     * @param vertex the vertex whose opposite extremity is to be found.
     * @return the opposite vertex.
     */
    int oppositeExtremity(int vertex);
}
