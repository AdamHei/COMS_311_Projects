
package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Vertex;

import java.util.*;


public class GraphAlgorithms {
    public static <V, E> List<Vertex<V>> TopologicalSort(IGraph<V, E> g) {
        List<Vertex<V>> sortedList = new ArrayList<>();
        Map<String, Boolean> marked = new HashMap<>();
        g.getVertices().forEach(v -> marked.put(v.getVertexName(), false));

        g.getVertices().stream()
                .filter(vertex -> !marked.get(vertex.getVertexName()))
                .forEach(vertex -> visitVertex(g, vertex.getVertexName(), sortedList, marked));

        return sortedList;
    }

    private static <V, E> void visitVertex(IGraph<V, E> graph, String vertex, List<Vertex<V>> list, Map<String, Boolean> marked) {
        if (!marked.get(vertex)) {
            marked.put(vertex, true);
            for (Vertex<V> neighbor : graph.getNeighbors(vertex)) {
                visitVertex(graph, neighbor.getVertexName(), list, marked);
            }
            list.add(0, graph.getVertex(vertex));
        }
    }

    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort(IGraph<V, E> g) {
        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Integer> inDegrees = new HashMap<>();

        g.getVertices().forEach(v -> {
            visited.put(v.getVertexName(), false);
            inDegrees.put(v.getVertexName(), 0);
        });

        g.getVertices()
                .forEach(vVertex -> g.getNeighbors(vVertex.getVertexName())
                        .forEach(neighbor -> {
                            inDegrees.put(neighbor.getVertexName(), inDegrees.get(neighbor.getVertexName()) + 1);
                        }));

        List<Vertex<V>> sorted = new ArrayList<>();
        List<List<Vertex<V>>> masterList = new ArrayList<>();

        allTopoSortsHelper(masterList, g, visited, sorted, inDegrees);
        return masterList;
    }

    private static <V, E> void allTopoSortsHelper(List<List<Vertex<V>>> masterList, IGraph<V, E> graph, Map<String, Boolean> visited, List<Vertex<V>> sorted, Map<String, Integer> inDegrees) {
        boolean allFound = false;

        for (Vertex<V> vertex : graph.getVertices()) {
            if (inDegrees.get(vertex.getVertexName()) == 0 && !visited.get(vertex.getVertexName())) {
                for (Vertex<V> neighbor : graph.getNeighbors(vertex.getVertexName())) {
                    inDegrees.put(neighbor.getVertexName(), inDegrees.get(neighbor.getVertexName()) - 1);
                }

                sorted.add(vertex);
                visited.put(vertex.getVertexName(), true);

                allTopoSortsHelper(masterList, graph, visited, sorted, inDegrees);

                visited.put(vertex.getVertexName(), false);
                sorted.remove(sorted.size() - 1);
                for (Vertex<V> neighbor : graph.getNeighbors(vertex.getVertexName())) {
                    inDegrees.put(neighbor.getVertexName(), inDegrees.get(neighbor.getVertexName()) + 1);
                }
                allFound = true;
            }
        }

        if (!allFound) {
            masterList.add(new ArrayList<>(sorted));
        }
    }

    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g) {
        return null; // Dummy return - replace this.
    }
}