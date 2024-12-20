package Tests;

import Graph.Graph;
import Graph.NonDirectedEdge;
import Graph.DirectedEdge;
import Tests.Utilities.TestExecutionException;
import Tests.Utilities.TestPrinter;

public class GraphTest {

    public static void main(String[] args) throws TestExecutionException {
        Graph graph = new Graph(10);
        NonDirectedEdge nonDirectedEdge = new NonDirectedEdge(0, 1, 20);
        DirectedEdge directedEdge = new DirectedEdge(nonDirectedEdge, false);

        TestPrinter.result(graph.isVertex(-1), false);
        TestPrinter.result(graph.isVertex(0), false);
        TestPrinter.result(graph.outEdges(0) == null, true);

        graph.ensureVertex(0);
        TestPrinter.result(graph.isVertex(0), true);
        TestPrinter.result(graph.order(), 1);

        TestPrinter.result(graph.outEdges(0).size(), 0);
        TestPrinter.result(graph.edgeCardinality(), 0);

        graph.addEdge(directedEdge);
        TestPrinter.result(graph.outEdges(0).size(), 1);
        TestPrinter.result(graph.outEdges(0).contains(directedEdge), true);
        TestPrinter.result(graph.edgeCardinality(), 1);

        graph.addEdge(nonDirectedEdge);
        TestPrinter.result(graph.edgeCardinality(), 1);

        TestPrinter.result(graph.order(), 2);

        graph.deleteVertex(0);
        TestPrinter.result(graph.edgeCardinality(), 0);
        TestPrinter.result(graph.outEdges(0) == null, true);
        TestPrinter.result(graph.order(), 1);

        graph.deleteVertex(1);
        TestPrinter.result(graph.order(), 0);
    }
}
