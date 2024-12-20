import Graph.*;
import RandomTrees.AldousBroder;
import RandomTrees.MinimumWeightSpanningTree;
import RandomTrees.RandomEdgeInsertion;
import RandomTrees.RandomWalkTree;
import Tests.GraphGenerators.Complete;
import Tests.GraphGenerators.ErdosRenyi;
import Tests.GraphGenerators.Grid;
import Tests.GraphGenerators.Lollipop;
import Utilities.TreeAnalyzers.Labyrinth;
import Utilities.TreeAnalyzers.RootedTree;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Main {

    static Grid grid;

    public static void main(String[] argv) throws Throwable {

        int randomTreeIndex = getRandomTreeIndex(argv);
        int graphIndex = getGraphIndex(argv);
        Graph graph = chooseFromGraphFamily(graphIndex);
        ArrayList<NonDirectedEdge> randomTree = null;

        int noOfSamples = 10;
        Stats stats = new Stats(noOfSamples);
        for (int i = 0; i < noOfSamples; i++) {
            randomTree = genTree(graph, randomTreeIndex);
            stats.update(randomTree);
        }
        stats.print();

        if (grid != null) showGrid(grid, randomTree);
    }

    /**
     * Gets the random tree index from the command line arguments.
     * Defaults to 1 if no argument is provided.
     *
     * @param argv the command line arguments
     * @return the graph index
     */
    private static int getRandomTreeIndex(String[] argv) {
        if (argv.length > 0) {
            return Integer.parseInt(argv[0]);
        }
        return 1;
    }

    /**
     * Gets the graph index from the command line arguments.
     * Defaults to 1 if no argument is provided.
     *
     * @param argv the command line arguments
     * @return the graph index
     */
    private static int getGraphIndex(String[] argv) {
        if (argv.length > 1) {
            return Integer.parseInt(argv[1]);
        }
        return 1;
    }

    /**
     * Selects a graph from a family of graphs.
     * The function can be modified to choose different types of graphs.
     *
     * @return the selected graph
     */
    private static Graph chooseFromGraphFamily(int index) {
        Graph graph;
        switch (index) {
            case 1:
                System.out.println("-------Grid mode------");
                grid = new Grid(1920 / 11, 1080 / 11);
                graph = grid.getGraph();
                break;
            case 2:
                System.out.println("-------Complete Graph mode------");
                graph = new Complete(400).getGraph();
                break;
            case 3:
                System.out.println("-------ErdosRenyi mode------");
                graph = new ErdosRenyi(1_000, 100).getGraph();
                break;
            case 4:
                System.out.println("-------Lollipop mode------");
                graph = new Lollipop(1_000).getGraph();
                break;
            default:
                return chooseFromGraphFamily(1);
        }
        return graph;
    }

    /**
     * Generates a random tree based on the selected method.
     *
     * @param graph the graph from which to generate the tree
     * @param randomTreeIndex the index that determines which method to use (e.g., 1 for MST, 2 for Random Walk)
     * @return the generated tree as a list of undirected edges
     */
    public static ArrayList<NonDirectedEdge> genTree(Graph graph, int randomTreeIndex) {
        ArrayList<Edge> randomDirectedEdgeTree = selectRandomTreeMethod(graph, randomTreeIndex);
        return convertToNonDirectedEdges(randomDirectedEdgeTree);
    }

    /**
     * Selects the random tree generation method based on the provided index.
     *
     * @param graph the graph from which to generate the tree
     * @param randomTreeIndex the method index (1 for MST, 2 for Random Walk)
     * @return a list of directed edges from the selected tree generation method
     */
    private static ArrayList<Edge> selectRandomTreeMethod(Graph graph, int randomTreeIndex) {
        ArrayList<Edge> randomDirectedEdgeTree;

        switch (randomTreeIndex) {
            case 1:
                randomDirectedEdgeTree = MinimumWeightSpanningTree.generateRandomTree(graph);
                break;
            case 2:
                randomDirectedEdgeTree = RandomWalkTree.generateRandomTree(graph, true);
                break;
            case 3:
                randomDirectedEdgeTree = RandomEdgeInsertion.generateRandomTree(graph);
                break;
            case 4:
                randomDirectedEdgeTree = AldousBroder.generateRandomTree(graph);
                break;
            default:
                return selectRandomTreeMethod(graph, 1);
        }
        return randomDirectedEdgeTree;
    }

    /**
     * Converts a list of directed edges into a list of undirected edges.
     *
     * @param directedEdges the list of directed edges
     * @return a list of undirected edges
     */
    private static ArrayList<NonDirectedEdge> convertToNonDirectedEdges(ArrayList<Edge> directedEdges) {
        ArrayList<NonDirectedEdge> randomTree = new ArrayList<>();

        for (Edge edge : directedEdges) {
            if (edge instanceof DirectedEdge) {
                randomTree.add(((DirectedEdge) edge).getSupport());
            } else if (edge instanceof NonDirectedEdge) {
                randomTree.add((NonDirectedEdge) edge);
            }
        }
        return randomTree;
    }

    /**
     * Helper class to store and compute statistics for random trees.
     */
    private static class Stats {
        private final int  nbrOfSamples;
        private int diameterSum = 0;
        private double eccentricitySum = 0;
        private long wienerSum = 0;
        private final int[] degreesSum = {0, 0, 0, 0, 0};
        private final long startingTime;

        public Stats(int noOfSamples) {
            this.nbrOfSamples = noOfSamples;
            this.startingTime = System.nanoTime();
        }

        /**
         * Prints the accumulated statistics.
         */
        public void print() {
            long delay = System.nanoTime() - startingTime;

            System.out.println("On " + nbrOfSamples + " samples:");
            System.out.println("Average eccentricity: " + (eccentricitySum / nbrOfSamples));
            System.out.println("Average Wiener index: " + (wienerSum / nbrOfSamples));
            System.out.println("Average diameter: " + (diameterSum / nbrOfSamples));
            System.out.println("Average number of leaves: " + (degreesSum[1] / nbrOfSamples));
            System.out.println("Average number of degree 2 vertices: " + (degreesSum[2] / nbrOfSamples));
            System.out.println("Average computation time: " + delay / (nbrOfSamples * 1_000_000L) + "ms");
        }

        /**
         * Updates the statistics with data from a new random tree.
         *
         * @param randomTree the generated random tree to analyze
         */
        public void update(ArrayList<NonDirectedEdge> randomTree) {
            RootedTree rooted = new RootedTree(randomTree, 0);
            diameterSum += rooted.getDiameter();
            eccentricitySum += rooted.getAverageEccentricity();
            wienerSum += rooted.getWienerIndex();

            int[] degrees = rooted.getDegreeDistribution(4);
            for (int j = 1; j < 5; j++) {
                degreesSum[j] += degrees[j];
            }
        }
    }

    /**
     * Displays the grid with the generated tree on a GUI window.
     * Optionally saves the resulting image to disk.
     *
     * @param grid the grid to display
     * @param randomTree the random tree to overlay on the grid
     */
    private static void showGrid(Grid grid, ArrayList<NonDirectedEdge> randomTree) throws Throwable {
        RootedTree rooted = new RootedTree(randomTree, 0);

        JFrame window = new JFrame("Solution");
        final Labyrinth labyrinth = new Labyrinth(grid, rooted);

        labyrinth.setStyleBalanced();
        labyrinth.setShapeSmoothSmallNodes();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(labyrinth);
        window.pack();
        window.setLocationRelativeTo(null);

        // Add edges to the labyrinth
        for (final NonDirectedEdge e : randomTree) {
            labyrinth.addEdge(e);
        }
        labyrinth.drawLabyrinth();

        window.setVisible(true);

        // Save the labyrinth visualization as an image
        try {
            labyrinth.saveImage("resources/random.png");
        } catch (IOException e) {
            throw e.getCause();
        }
    }
}
