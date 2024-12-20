package Utilities.Searchers;

import java.util.ArrayList;
import java.util.List;

public class UnionFind {
    private final List<Integer> parents;
    private final List<Integer> ranks;

    public UnionFind(int size) {
        parents = new ArrayList<>(size);
        ranks = new ArrayList<>(size);

        for (int index = 0; index < size; index++) {
            getParents().add(index);
            getRanks().add(0);
        }
    }

    /**
     * Finds the root of the set containing the given vertex with path compression.
     * Optimizes the Union-Find structure by making nodes point directly to the root.
     *
     * @param vertex The vertex to find the root for.
     * @return The root of the set containing the vertex.
     */
    public int find(int vertex) {
        if (!getParents().get(vertex).equals(vertex)) {
            getParents().set(vertex, find(getParents().get(vertex)));
        }
        return getParents().get(vertex);
    }

    /**
     * Unites the sets containing two vertices, using union by rank to keep the structure balanced.
     *
     * @param firstVertex  The first vertex to union.
     * @param secondVertex The second vertex to union.
     * @return True if the sets were successfully united (they were in different sets),
     *         false if the vertices are already in the same set.
     */
    public boolean union(int firstVertex, int secondVertex) {
        int rootToFirstVertex = find(firstVertex);
        int rootToSecondVertex = find(secondVertex);

        if (rootToFirstVertex == rootToSecondVertex) {
            return false;
        }

        if (getRanks().get(rootToFirstVertex) > getRanks().get(rootToSecondVertex)) {
            getParents().set(rootToSecondVertex, rootToFirstVertex);
        } else if (getRanks().get(rootToFirstVertex) < getRanks().get(rootToSecondVertex)) {
            getParents().set(rootToFirstVertex, rootToSecondVertex);
        } else {
            getParents().set(rootToSecondVertex, rootToFirstVertex);
            getRanks().set(rootToFirstVertex, getRanks().get(rootToFirstVertex) + 1);
        }
        return true;
    }

    private List<Integer> getRanks() {
        return ranks;
    }

    private List<Integer> getParents() {
        return parents;
    }
}
