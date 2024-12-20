package Tests.GraphGenerators;

import Graph.*;

public class Complete {

    private final Graph graph;

    public Complete(int order) {
        this.graph = new Graph(order);
        for (int i = 0; i < order; i++)
            for (int j = i + 1; j < order; j++)
                getGraph().addEdge(new NonDirectedEdge(i, j, 0));
    }

    public Graph getGraph() {
        return graph;
    }
}
