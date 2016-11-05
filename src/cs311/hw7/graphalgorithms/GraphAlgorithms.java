
package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.Graph;
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
        IGraph<V, E> mst = new Graph<>(g.isDirectedGraph());
        for (Vertex<V> vertex : g.getVertices()) {
            mst.addVertex(vertex.getVertexName(), vertex.getVertexData());
        }

        List<IGraph.Edge<E>> allEdges = g.getEdges();

        Collections.sort(allEdges, new EdgeComparator<>());

        int edgeIndex = 0, i = 0;
        int numVertices = g.getVertices().size();

        Map<String, SubGraph<V>> subGraphMap = new HashMap<>();

        for (Vertex<V> vertex: g.getVertices()){
            SubGraph<V> subGraph = new SubGraph<>();
            subGraph.parent = vertex;
            subGraph.rank = 0;
            subGraphMap.put(vertex.getVertexName(), subGraph);
        }

        while (edgeIndex < numVertices - 1){
            IGraph.Edge<E> nextEdge = allEdges.get(i);

            Vertex<V> fromRoot = findRootAndCollapse(subGraphMap, g.getVertex(nextEdge.getVertexName1()));
            Vertex<V> toRoot = findRootAndCollapse(subGraphMap, g.getVertex(nextEdge.getVertexName2()));

            if (!fromRoot.equals(toRoot)){
                mst.addEdge(nextEdge.getVertexName1(), nextEdge.getVertexName2(), nextEdge.getEdgeData());
                edgeIndex++;
                union(subGraphMap, fromRoot, toRoot);
            }

            i++;
        }

        return mst;
    }

    private static <V> Vertex<V> findRootAndCollapse(Map<String, SubGraph<V>> subGraphs, Vertex<V> vertex){
        String vertexName = vertex.getVertexName();

        if (!subGraphs.get(vertexName).parent.equals(vertex)){
            subGraphs.get(vertexName).parent = findRootAndCollapse(subGraphs, subGraphs.get(vertexName).parent);
        }

        return subGraphs.get(vertexName).parent;
    }

    private static <V> void union(Map<String, SubGraph<V>> subGraphs, Vertex<V> thing1, Vertex<V> thing2){
        Vertex<V> firstRoot = findRootAndCollapse(subGraphs, thing1);
        Vertex<V> secondRoot = findRootAndCollapse(subGraphs, thing2);
        String firstName = firstRoot.getVertexName();
        String secondName = secondRoot.getVertexName();

        if (subGraphs.get(firstName).rank < subGraphs.get(secondName).rank){
            subGraphs.get(firstName).parent = secondRoot;
        }
        else if (subGraphs.get(firstName).rank > subGraphs.get(secondName).rank){
            subGraphs.get(secondName).parent = firstRoot;
        }
        else {
            subGraphs.get(secondName).parent = firstRoot;
            subGraphs.get(firstName).rank++;
        }
    }

    private static class SubGraph<V> {
        Vertex<V> parent;
        int rank;
    }

    private static class EdgeComparator<E extends IWeight> implements Comparator<IGraph.Edge<E>> {
        @Override
        public int compare(IGraph.Edge<E> o1, IGraph.Edge<E> o2) {
            if (o1.getEdgeData().getWeight() < o2.getEdgeData().getWeight()) return -1;
            if (o1.getEdgeData().getWeight() > o2.getEdgeData().getWeight()) return 1;
            return 0;
        }
    }
}