package Tests;

import Graph.NonDirectedEdge;
import Tests.Utilities.TestExecutionException;
import Tests.Utilities.TestPrinter;

public class NonDirectedEdgeTest {

    public static void main(String[] args) throws TestExecutionException {
        NonDirectedEdge nonDirectedEdge1 = new NonDirectedEdge(0, 1, 20);
        NonDirectedEdge nonDirectedEdge2 = new NonDirectedEdge(2, 3, 20);
        NonDirectedEdge nonDirectedEdge3 = new NonDirectedEdge(4, 5, 10);
        NonDirectedEdge nonDirectedEdge4 = new NonDirectedEdge(6, 7, 30);

        TestPrinter.result(nonDirectedEdge1.compareTo(nonDirectedEdge2), 0);
        TestPrinter.result(nonDirectedEdge1.compareTo(nonDirectedEdge3) > 0, true);
        TestPrinter.result(nonDirectedEdge1.compareTo(nonDirectedEdge4) < 0, true);

        TestPrinter.result(nonDirectedEdge1.oppositeExtremity(100), 100);
        TestPrinter.result(nonDirectedEdge1.oppositeExtremity(0), 1);
        TestPrinter.result(nonDirectedEdge1.oppositeExtremity(1), 0);
    }
}
