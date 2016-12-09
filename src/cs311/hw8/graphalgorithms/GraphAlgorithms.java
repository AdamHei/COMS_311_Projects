
package cs311.hw8.graphalgorithms;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;

import java.util.*;

/**
 * A set of useful graph property-finding and ordering algorithms
 */
public class GraphAlgorithms {

    public static <V, E extends IWeight> List<Edge<E>> ShortestPath(IGraph<V, E> g, String vertexStart, String vertexEnd) {
        Map<String, Boolean> marked = new HashMap<>();
        Map<String, Double> distTo = new HashMap<>();
        Queue<Vertex<V>> heap = new PriorityQueue<>((Vertex<V> v1, Vertex<V> v2) -> {
            if (distTo.get(v1.getVertexName()) < distTo.get(v2.getVertexName())) return -1;
            if (distTo.get(v1.getVertexName()) > distTo.get(v2.getVertexName())) return 1;
            return 0;
        });
        Map<Vertex<V>, Vertex<V>> predecessor = new HashMap<>();

        g.getVertices().forEach(vertex -> {
            distTo.put(vertex.getVertexName(), Double.POSITIVE_INFINITY);
            marked.put(vertex.getVertexName(), false);
        });
        distTo.put(vertexStart, 0.0);
        heap.add(g.getVertex(vertexStart));

        while (!heap.isEmpty()) {
            Vertex<V> vertex = heap.poll();
            marked.put(vertex.getVertexName(), true);
            for (Vertex<V> neighbor : g.getNeighbors(vertex.getVertexName())) {
                if (!marked.get(neighbor.getVertexName()) && !heap.contains(neighbor)) {
                    heap.add(neighbor);
                }
                double currentDistance = distTo.get(neighbor.getVertexName());
                double tempDistance = distTo.get(vertex.getVertexName()) + g.getEdgeData(vertex.getVertexName(), neighbor.getVertexName()).getWeight();
                if (tempDistance < currentDistance) {
                    heap.remove(neighbor);
                    distTo.put(neighbor.getVertexName(), tempDistance);
                    heap.add(neighbor);
                    predecessor.put(neighbor, vertex);
                }
            }
        }

        List<Edge<E>> shortestPath = new ArrayList<>();
        Vertex<V> u = g.getVertex(vertexEnd);
        while (predecessor.get(u) != null) {
            Vertex<V> pred = predecessor.get(u);
            shortestPath.add(0, g.getEdge(pred.getVertexName(), u.getVertexName()));
            u = pred;
        }

        return shortestPath;
    }

    /**
     * Runs and returns a valid topological sort on the given graph utilizing a depth-first search style.
     * Works on non-fully-connected graphs, though not fully useful
     *
     * @param g   The graph to run on
     * @param <V> The vertex data type
     * @param <E> The edge data type
     * @return A valid topological sort in the form of a list of vertices
     */
    public static <V, E> List<Vertex<V>> TopologicalSort(IGraph<V, E> g) {
        //If there is a cycle in the graph, there are no valid topological sorts
        if (hasCycle(g)) {
            return new ArrayList<>();
        }

        List<Vertex<V>> sortedList = new ArrayList<>();
        Map<String, Boolean> marked = new HashMap<>();
        g.getVertices().forEach(v -> marked.put(v.getVertexName(), false));

        g.getVertices().stream()
                .filter(vertex -> !marked.get(vertex.getVertexName()))
                .forEach(vertex -> visitVertex(g, vertex.getVertexName(), sortedList, marked));

        return sortedList;
    }

    //Helper method that recursively visits neighbors.
    //Upon callback, the most recently visited vertex
    //is added to the front of the running possible topo sort
    private static <V, E> void visitVertex(IGraph<V, E> graph, String vertex, List<Vertex<V>> list, Map<String, Boolean> marked) {
        if (!marked.get(vertex)) {
            marked.put(vertex, true);
            for (Vertex<V> neighbor : graph.getNeighbors(vertex)) {
                visitVertex(graph, neighbor.getVertexName(), list, marked);
            }
            list.add(0, graph.getVertex(vertex));
        }
    }

    /**
     * Recursively discovers all possible valid topological sorts for a given graph
     *
     * @param g   The graph to run on
     * @param <V> The Vertex data type
     * @param <E> The edge data type
     * @return A list of all possible valid topological sorts
     */
    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort(IGraph<V, E> g) {
        //If there is a cycle in the graph, there are no valid topological sorts
        if (hasCycle(g)) {
            return new ArrayList<>();
        }

        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Integer> inDegrees = new HashMap<>();

        g.getVertices().forEach(v -> {
            visited.put(v.getVertexName(), false);
            inDegrees.put(v.getVertexName(), 0);
        });

        //Initialization of in-degrees
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

    //Recursive helper method for AllTopoSorts
    //Done in a DFS style, simulates deconstructing the graph by reducing indegrees and backtracking
    //to add vertices to a running possible topological sort
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

    /**
     * Finds the minimum spanning tree of a given graph, utilizing the union-find strategy
     *
     * @param g   The graph to run on
     * @param <V> The vertex data type
     * @param <E> The edge data type
     * @return A minimum spanning tree of the given graph
     */
    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g) {
        //The number of edges in the graph must at least be the number of vertices - 1
        //If the graph is not connected, we are not guaranteed that
//        if (!isConnected(g)) {
//            return g;
//        }

        IGraph<V, E> mst = new Graph<>();
        mst.setUndirectedGraph();

        for (Vertex<V> vertex : g.getVertices()) {
            mst.addVertex(vertex.getVertexName(), vertex.getVertexData());
        }

        List<Edge<E>> allEdges = g.getEdges();

        Collections.sort(allEdges, new EdgeComparator<>());

        int edgeCounter = 0, i = 0;
        int numVertices = g.getVertices().size();

        Map<String, SubGraph<V>> subGraphMap = new HashMap<>();

        //Construct subTrees for each vertex
        for (Vertex<V> vertex : g.getVertices()) {
            SubGraph<V> subGraph = new SubGraph<>();
            subGraph.parent = vertex;
            subGraph.rank = 0;
            subGraphMap.put(vertex.getVertexName(), subGraph);
        }

        //Iteratively add new edges to the tree, ensuring no cycle is created
        //Ensures we only add as many edges as are necessary
        while (edgeCounter < numVertices - 1 && i < allEdges.size()) {
            Edge<E> nextEdge = allEdges.get(i);

            Vertex<V> fromRoot = findRootAndCollapse(subGraphMap, g.getVertex(nextEdge.getVertexName1()));
            Vertex<V> toRoot = findRootAndCollapse(subGraphMap, g.getVertex(nextEdge.getVertexName2()));

            //Vertices on either side of the edge are not in the same subtree
            if (!fromRoot.equals(toRoot)) {
                //Add them to our running tree and union their subtrees
                mst.addEdge(nextEdge.getVertexName1(), nextEdge.getVertexName2(), nextEdge.getEdgeData());
                edgeCounter++;
                union(subGraphMap, fromRoot, toRoot);
            }

            i++;
        }

        return mst;
    }

    //Helper method for recursively finding the root of a given subtree given a vertex in that subtree
    //while collapsing that subtree for faster future use
    private static <V> Vertex<V> findRootAndCollapse(Map<String, SubGraph<V>> subGraphs, Vertex<V> vertex) {
        String vertexName = vertex.getVertexName();

        if (!subGraphs.get(vertexName).parent.equals(vertex)) {
            subGraphs.get(vertexName).parent = findRootAndCollapse(subGraphs, subGraphs.get(vertexName).parent);
        }

        return subGraphs.get(vertexName).parent;
    }

    //Merge two subtrees together based on larger rank, i.e. levels
    private static <V> void union(Map<String, SubGraph<V>> subGraphs, Vertex<V> thing1, Vertex<V> thing2) {
        Vertex<V> firstRoot = findRootAndCollapse(subGraphs, thing1);
        Vertex<V> secondRoot = findRootAndCollapse(subGraphs, thing2);
        String firstName = firstRoot.getVertexName();
        String secondName = secondRoot.getVertexName();

        if (subGraphs.get(firstName).rank < subGraphs.get(secondName).rank) {
            subGraphs.get(firstName).parent = secondRoot;
        } else if (subGraphs.get(firstName).rank > subGraphs.get(secondName).rank) {
            subGraphs.get(secondName).parent = firstRoot;
        } else {
            subGraphs.get(secondName).parent = firstRoot;
            subGraphs.get(firstName).rank++;
        }
    }

    /**
     * @return Whether you can get from any given vertex to every other vertex
     */
    private static <V, E> boolean isConnected(IGraph<V, E> graph) {
        if (graph.getVertices().size() == 0) return true;

        Map<String, Boolean> marked = new HashMap<>();
        graph.getVertices().forEach(vertex -> marked.put(vertex.getVertexName(), false));

        List<String> names = new ArrayList<>();
        graph.getVertices().forEach(vertex -> names.add(vertex.getVertexName()));

        dfs(marked, names.get(0), graph);

        for (int i = 1; i < names.size(); i++) {
            if (!marked.get(names.get(i))) {
                return false;
            }
        }
        return true;
    }

    //Recursively mark every reachable vertex
    private static <V, E> void dfs(Map<String, Boolean> marked, String name, IGraph<V, E> graph) {
        if (!marked.get(name)) {
            marked.put(name, true);
            for (Vertex<V> neighbor : graph.getNeighbors(name)) {
                dfs(marked, neighbor.getVertexName(), graph);
            }
        }
    }

    /**
     * @return Whether the graph contains a cycle or not
     */
    private static <V, E> boolean hasCycle(IGraph<V, E> graph) {
        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Boolean> stack = new HashMap<>();

        graph.getVertices().forEach(vertex -> {
            visited.put(vertex.getVertexName(), false);
            stack.put(vertex.getVertexName(), false);
        });

        for (Vertex<V> vertex : graph.getVertices()) {
            if (hasCycleHelper(vertex.getVertexName(), visited, stack, graph)) {
                return true;
            }
        }

        return false;
    }

    //Helper for recursively checking whether the graph has a cycle or not
    private static <V, E> boolean hasCycleHelper(String name, Map<String, Boolean> visited, Map<String, Boolean> stack, IGraph<V, E> graph) {
        if (!visited.get(name)) {
            visited.put(name, true);
            stack.put(name, true);

            for (Vertex<V> neighbor : graph.getNeighbors(name)) {
                if (!visited.get(neighbor.getVertexName()) && hasCycleHelper(neighbor.getVertexName(), visited, stack, graph)) {
                    return true;
                } else if (stack.get(neighbor.getVertexName())) {
                    return true;
                }
            }
        }

        stack.put(name, false);
        return false;
    }

    //Inner class to help with union-find. Represents a simple tree
    private static class SubGraph<V> {
        Vertex<V> parent;
        int rank;
    }

    //Comparator for IWeight classes
    static class EdgeComparator<E extends IWeight> implements Comparator<Edge<E>> {
        @Override
        public int compare(Edge<E> o1, Edge<E> o2) {
            if (o1.getEdgeData().getWeight() < o2.getEdgeData().getWeight()) return -1;
            if (o1.getEdgeData().getWeight() > o2.getEdgeData().getWeight()) return 1;
            return 0;
        }
    }
}