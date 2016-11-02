package cs311.hw7.graph;

import java.util.*;

public class Graph<V, E> implements IGraph<V, E> {

    private Map<String, List<Edge<E>>> nameToNeighbors = new HashMap<>();
    private Map<String, Vertex<V>> nameToVertex = new HashMap<>();
    private boolean isDirected;

    public Graph(boolean directed){
        isDirected = directed;
    }

    @Override
    public void setDirectedGraph() {
        isDirected = true;
    }

    @Override
    public void setUndirectedGraph() {
        for (String vertex: nameToNeighbors.keySet()){
            for (Edge<E> edge: nameToNeighbors.get(vertex)){
                putEdgeNoException(edge.getVertexName2(), vertex, edge.getEdgeData());
            }
        }
        isDirected = false;
    }

    private void putEdgeNoException(String vertex1, String vertex2, E edgeData){
        List<Edge<E>> edges = nameToNeighbors.get(vertex1);
        for (Edge<E> edge : edges) {
            if (edge.getVertexName2().equals(vertex2) && edge.getEdgeData().equals(edgeData)) {
                return;
            }
        }
        edges.add(new Edge<>(vertex1, vertex2, edgeData));
    }

    @Override
    public boolean isDirectedGraph() {
        return isDirected;
    }

    @Override
    public void addVertex(String vertexName) throws DuplicateVertexException {
        putVertex(vertexName, null);
    }

    @Override
    public void addVertex(String vertexName, V vertexData) throws DuplicateVertexException {
        putVertex(vertexName, vertexData);
    }

    private void putVertex(String vertexName, V vertexData){
        if (containsVertex(vertexName)) throw new DuplicateVertexException();

        Vertex<V> v = new Vertex<>(vertexName, vertexData);
        nameToVertex.put(vertexName, v);
        nameToNeighbors.put(vertexName, new ArrayList<>());
    }

    private boolean containsVertex(String vertexName){
        return nameToVertex.keySet().contains(vertexName);
    }

    @Override
    public void addEdge(String vertex1, String vertex2) throws DuplicateEdgeException, NoSuchVertexException {
        putEdge(vertex1, vertex2, null);
    }

    @Override
    public void addEdge(String vertex1, String vertex2, E edgeData) throws DuplicateEdgeException, NoSuchVertexException {
        putEdge(vertex1, vertex2, edgeData);
    }

    private void putEdge(String vertex1, String vertex2, E edgeData){
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        for (Edge<E> edge: nameToNeighbors.get(vertex1)){
            if (edge.getVertexName2().equals(vertex2)){
                throw new DuplicateEdgeException();
            }
        }
        if (!isDirected){
            for (Edge<E> edge: nameToNeighbors.get(vertex2)){
                if (edge.getVertexName2().equals(vertex1)){
                    throw new DuplicateEdgeException();
                }
            }
        }

        List<Edge<E>> v1Edges = nameToNeighbors.get(vertex1);
        v1Edges.add(new Edge<>(vertex1, vertex2, edgeData));

        if (!isDirected){
            List<Edge<E>> v2Edges = nameToNeighbors.get(vertex2);
            v2Edges.add(new Edge<>(vertex2, vertex1, edgeData));
        }
    }

    @Override
    public V getVertexData(String vertexName) throws NoSuchVertexException {
        if (!containsVertex(vertexName)) throw new NoSuchVertexException();

        return nameToVertex.get(vertexName).getVertexData();
    }

    @Override
    public void setVertexData(String vertexName, V vertexData) throws NoSuchVertexException {
        if (!containsVertex(vertexName)) throw new NoSuchVertexException();

        nameToVertex.put(vertexName, new Vertex<V>(vertexName, vertexData));
    }

    @Override
    public E getEdgeData(String vertex1, String vertex2) throws NoSuchVertexException, NoSuchEdgeException {
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        for (Edge<E> edge: nameToNeighbors.get(vertex1)){
            if (edge.getVertexName2().equals(vertex2)){
                return edge.getEdgeData();
            }
        }

        return null;
    }

    @Override
    public void setEdgeData(String vertex1, String vertex2, E edgeData) throws NoSuchVertexException, NoSuchEdgeException {
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        boolean isSet = setEdge(vertex1, vertex2, edgeData);
        if (!isSet) throw new NoSuchEdgeException();

        if (!isDirected){
            isSet = setEdge(vertex2, vertex1, edgeData);
            if (!isSet) throw new NoSuchEdgeException();
        }
    }

    private boolean setEdge(String first, String second, E edgeData){
        List<Edge<E>> edges = nameToNeighbors.get(first);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getVertexName2().equals(second)){
                Edge<E> edge = new Edge<>(first, second, edgeData);
                edges.set(i, edge);
                return true;
            }
        }
        return false;
    }

    @Override
    public Vertex<V> getVertex(String VertexName) {
        return nameToVertex.get(VertexName);
    }

    @Override
    public Edge<E> getEdge(String vertexName1, String vertexName2) {
        for (Edge<E> edge: nameToNeighbors.get(vertexName1)){
            if (edge.getVertexName2().equals(vertexName2)){
                return edge;
            }
        }
        return null;
    }

    @Override
    public List<Vertex<V>> getVertices() {
        return new ArrayList<>(nameToVertex.values());
    }

    @Override
    public List<Edge<E>> getEdges() {
        List<Edge<E>> edges = new ArrayList<>();
        nameToNeighbors.values().forEach(list -> list.forEach(edges::add));

        return edges;
    }

    @Override
    public List<Vertex<V>> getNeighbors(String vertex) {
        List<Vertex<V>> neighbors = new ArrayList<>();
        nameToNeighbors.get(vertex).forEach(
                neighborName ->
                        neighbors.add(nameToVertex.get(neighborName.getVertexName2()))
        );

        return neighbors;
    }

}
