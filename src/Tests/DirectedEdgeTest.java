package Tests;

import Graph.DirectedEdge;
import Graph.NonDirectedEdge;
import Tests.Utilities.TestExecutionException;
import Tests.Utilities.TestPrinter;

public class DirectedEdgeTest {

    public static void main(String[] args) throws TestExecutionException {
        NonDirectedEdge nonDirectedEdge = new NonDirectedEdge(0, 1, 20);
        DirectedEdge directedEdge = new DirectedEdge(nonDirectedEdge, false);

        TestPrinter.result(directedEdge.getSource(), 0);
        TestPrinter.result(directedEdge.getDestination(), 1);
        directedEdge.setReversed(true);
        TestPrinter.result(directedEdge.getSource(), 1);
        TestPrinter.result(directedEdge.getDestination(), 0);
    }
}
