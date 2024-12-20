package Tests.GraphGenerators;
import Graph.*;

import java.util.BitSet;

public class Grid {

    private final Graph graph;
    private final int width;
    private final int height;
    private final int maxVertex;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.maxVertex = width * height - 1;
        this.graph = new Graph(maxVertex() + 1);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < width - 1) {
                    getGraph().addEdge(new NonDirectedEdge(
                            vertexOfCoordinate(i, j),
                            vertexOfCoordinate(i + 1, j),
                            0.0
                    ));
                }
                if (j < height - 1) {
                    getGraph().addEdge(new NonDirectedEdge(
                            vertexOfCoordinate(i, j),
                            vertexOfCoordinate(i, j + 1),
                            0.0
                    ));
                }
            }
        }
    }

    /**
     * Converts a pair of coordinates (abscissa, ordinate) into the corresponding vertex index.
     *
     * @param abscissa The horizontal coordinate (x).
     * @param ordinate The vertical coordinate (y).
     * @return The vertex index corresponding to the given coordinates.
     */
    private int vertexOfCoordinate(int abscissa, int ordinate) {
        return ordinate * width() + abscissa;
    }

    /**
     * Draws a subgrid based on the given edges.
     * The grid is constructed by using horizontal and vertical edges to determine where to draw lines.
     *
     * @param edges An iterable collection of undirected edges representing the grid's structure.
     */
    public void drawSubgrid(Iterable<NonDirectedEdge> edges) {
        BitSet up = new BitSet(maxVertex());
        BitSet right = new BitSet(maxVertex());

        for (NonDirectedEdge e : edges) {
            if (isHorizontal(e))
                right.set(Math.min(e.getSource(), e.getDestination()));
            if (isVertical(e))
                up.set(Math.min(e.getSource(), e.getDestination()));
        }

        for (int j = 0; j < height(); j++) {
            drawLine(j, right);
            if (j < height() - 1) drawInterline(j, up);
        }
    }

    /**
     * Draws a horizontal line at the specified height using a BitSet to determine where horizontal edges are present.
     *
     * @param h The vertical position (height) where the horizontal line should be drawn.
     * @param right A BitSet where each bit represents whether there is a horizontal edge at the corresponding vertex.
     */
    private void drawLine(int h, BitSet right) {
        for (int i = 0; i < width() - 1; i++) {
            System.out.print("o");
            if (right.get(vertexOfCoordinate(i, h))) System.out.print("--");
            else System.out.print("  ");
        }
        System.out.println("o");
    }

    /**
     * Draws a vertical separator (interline) at the specified height using a BitSet to determine where vertical edges are present.
     *
     * @param h The vertical position (height) where the interline should be drawn.
     * @param up A BitSet where each bit represents whether there is a vertical edge at the corresponding vertex.
     */
    private void drawInterline(int h, BitSet up) {
        for (int i = 0; i < width(); i++) {
            if (up.get(vertexOfCoordinate(i, h))) System.out.print("|");
            else System.out.print(" ");
            if (i < width() - 1) System.out.print("  ");
        }
        System.out.println();
    }

    /**
     * Returns the horizontal (x) coordinate (abscissa) of the given vertex.
     *
     * @param vertex The vertex index.
     * @return The horizontal coordinate (abscissa) of the vertex.
     */
    public int abscissaOfVertex(int vertex) {
        return vertex % width();
    }

    /**
     * Returns the vertical (y) coordinate (ordinate) of the given vertex.
     *
     * @param vertex The vertex index.
     * @return The vertical coordinate (ordinate) of the vertex.
     */
    public int ordinateOfVertex(int vertex) {
        return vertex / width();
    }

    /**
     * Determines whether the given edge is horizontal.
     * A horizontal edge connects two vertices that differ by exactly 1 in their index.
     *
     * @param e The undirected edge to check.
     * @return True if the edge is horizontal, false otherwise.
     */
    public boolean isHorizontal(NonDirectedEdge e) {
        return Math.abs(e.getSource() - e.getDestination()) == 1;
    }

    /**
     * Determines whether the given edge is vertical.
     * A vertical edge connects two vertices that differ by the width of the grid.
     *
     * @param e The undirected edge to check.
     * @return True if the edge is vertical, false otherwise.
     */
    public boolean isVertical(NonDirectedEdge e) {
        return Math.abs(e.getSource() - e.getDestination()) == width();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Graph getGraph() {
        return graph;
    }

    private int maxVertex() {
        return maxVertex;
    }
}
