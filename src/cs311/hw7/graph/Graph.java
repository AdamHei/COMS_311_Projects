package cs311.hw7.graph;

import java.util.*;

/**
 * Generic Graph implementation. Flexible between either directed or un-directed
 *
 * @param <V> Internal Vertex Data Type
 * @param <E> Internal Edge Data Type
 */
public class Graph<V, E> implements IGraph<V, E> {

    //Maps Vertex names to all outgoing edges
    private Map<String, List<Edge<E>>> nameToNeighbors = new HashMap<>();
    //Maps Vertex names to the actual vertex object
    private Map<String, Vertex<V>> nameToVertex = new HashMap<>();
    //Whether the graph is directed or not
    //Defaults to true, as the user must specify with set(Un)DirectedGraph()
    private boolean isDirected = true;

    /**
     * Default Constructor
     */
    public Graph() {
    }

    /**
     * All undirected graphs are already directed, so only a flag switch is needed
     */
    @Override
    public void setDirectedGraph() {
        isDirected = true;
    }

    /**
     * To make undirected, we check all edges and add their converse, i.e. add (y,x) if (x,y) exists.
     * We do this without adding duplicates.
     */
    @Override
    public void setUndirectedGraph() {
        for (String vertex : nameToNeighbors.keySet()) {
            for (Edge<E> edge : nameToNeighbors.get(vertex)) {
                putEdgeNoException(edge.getVertexName2(), vertex, edge.getEdgeData());
            }
        }
        isDirected = false;
    }

    //If the edge is present, simply return. Else, add the edge
    private void putEdgeNoException(String vertex1, String vertex2, E edgeData) {
        List<Edge<E>> edges = nameToNeighbors.get(vertex1);
        for (Edge<E> edge : edges) {
            if (edge.getVertexName2().equals(vertex2) && (edge.getEdgeData() == null || edge.getEdgeData().equals(edgeData))) {
                return;
            }
        }
        edges.add(new Edge<>(vertex1, vertex2, edgeData));
    }

    /**
     * Simple getter
     */
    @Override
    public boolean isDirectedGraph() {
        return isDirected;
    }

    /**
     * Adds a vertex with the given name and no internal data
     *
     * @param vertexName The unique name of the vertex.
     * @throws DuplicateVertexException Thrown if the vertex already exists
     */
    @Override
    public void addVertex(String vertexName) throws DuplicateVertexException {
        putVertex(vertexName, null);
    }

    /**
     * Similar to the above function, only with (hopefully) non-null data
     *
     * @param vertexName The name of the new vertex
     * @param vertexData It's internal data
     * @throws DuplicateVertexException Thrown if vertex already exists
     */
    @Override
    public void addVertex(String vertexName, V vertexData) throws DuplicateVertexException {
        putVertex(vertexName, vertexData);
    }

    //Helper function for addVertex() methods. Performs duplicate check and adds new vertices
    private void putVertex(String vertexName, V vertexData) {
        if (containsVertex(vertexName)) throw new DuplicateVertexException();

        Vertex<V> v = new Vertex<>(vertexName, vertexData);
        nameToVertex.put(vertexName, v);
        nameToNeighbors.put(vertexName, new ArrayList<>());
    }

    //Simple contains check used for duplicate Vertex checks
    private boolean containsVertex(String vertexName) {
        return nameToVertex.keySet().contains(vertexName);
    }

    /**
     * Similar to addVertex(), only for edges.
     *
     * @param vertex1 The first vertex in the edge.
     * @param vertex2 The second vertex in the edge.
     * @throws DuplicateEdgeException Thrown if edge already exists
     * @throws NoSuchVertexException  Thrown if either vertex is not in the graph
     */
    @Override
    public void addEdge(String vertex1, String vertex2) throws DuplicateEdgeException, NoSuchVertexException {
        putEdge(vertex1, vertex2, null);
    }

    /**
     * Similar to the above function, only with (hopefully) non-null data
     *
     * @param vertex1  The first vertex in the edge.
     * @param vertex2  The second vertex in the edge.
     * @param edgeData The generic edge data.
     * @throws DuplicateEdgeException Thrown if the edge already exists
     * @throws NoSuchVertexException  Thrown if either vertex is not in the graph
     */
    @Override
    public void addEdge(String vertex1, String vertex2, E edgeData) throws DuplicateEdgeException, NoSuchVertexException {
        putEdge(vertex1, vertex2, edgeData);
    }

    //Helper method for addEdge() functions. Adds vertices and accounts for un-directed adds to the graph
    private void putEdge(String vertex1, String vertex2, E edgeData) {
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        for (Edge<E> edge : nameToNeighbors.get(vertex1)) {
            if (edge.getVertexName2().equals(vertex2)) {
                throw new DuplicateEdgeException();
            }
        }
        if (!isDirected) {
            for (Edge<E> edge : nameToNeighbors.get(vertex2)) {
                if (edge.getVertexName2().equals(vertex1)) {
                    throw new DuplicateEdgeException();
                }
            }
        }

        List<Edge<E>> v1Edges = nameToNeighbors.get(vertex1);
        v1Edges.add(new Edge<>(vertex1, vertex2, edgeData));

        if (!isDirected) {
            List<Edge<E>> v2Edges = nameToNeighbors.get(vertex2);
            v2Edges.add(new Edge<>(vertex2, vertex1, edgeData));
        }
    }

    /**
     * Retrieves a vertex's internal data
     *
     * @param vertexName Name of vertex to get data for
     * @return Internal vertex data
     * @throws NoSuchVertexException Thrown if vertex is not in the graph
     */
    @Override
    public V getVertexData(String vertexName) throws NoSuchVertexException {
        if (!containsVertex(vertexName)) throw new NoSuchVertexException();
        return nameToVertex.get(vertexName).getVertexData();
    }

    /**
     * Overwrites or places a new vertex in the graph with updated data, as vertices are immutable
     *
     * @param vertexName The name of the vertex.
     * @param vertexData The generic vertex data.
     * @throws NoSuchVertexException Thrown if vertex is not in graph
     */
    @Override
    public void setVertexData(String vertexName, V vertexData) throws NoSuchVertexException {
        if (!containsVertex(vertexName)) throw new NoSuchVertexException();
        nameToVertex.put(vertexName, new Vertex<>(vertexName, vertexData));
    }

    /**
     * Retrieves the generic internal edge data
     *
     * @param vertex1 Vertex one of the edge.
     * @param vertex2 Vertex two of the edge.
     * @return The edge's data if present
     * @throws NoSuchVertexException Thrown if either vertex is not present
     * @throws NoSuchEdgeException   Thrown if no edge exists from vertex1 to 2
     */
    @Override
    public E getEdgeData(String vertex1, String vertex2) throws NoSuchVertexException, NoSuchEdgeException {
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        for (Edge<E> edge : nameToNeighbors.get(vertex1)) {
            if (edge.getVertexName2().equals(vertex2)) {
                return edge.getEdgeData();
            }
        }
        throw new NoSuchEdgeException();
    }

    /**
     * Overwrites an edge's internal data
     *
     * @param vertex1  Vertex one of the edge.
     * @param vertex2  Vertex two of the edge.
     * @param edgeData The generic edge data.
     * @throws NoSuchVertexException Thrown if either vertex is not present
     * @throws NoSuchEdgeException   Thrown if the edge is not already in the graph
     */
    @Override
    public void setEdgeData(String vertex1, String vertex2, E edgeData) throws NoSuchVertexException, NoSuchEdgeException {
        if (!(containsVertex(vertex1) && containsVertex(vertex2))) throw new NoSuchVertexException();

        boolean isSet = setEdge(vertex1, vertex2, edgeData);
        if (!isSet) throw new NoSuchEdgeException();

        if (!isDirected) {
            isSet = setEdge(vertex2, vertex1, edgeData);
            if (!isSet) throw new NoSuchEdgeException();
        }
    }

    //Helper method for setEdgeData() that returns the truthity of its ability to overwrite an edge
    private boolean setEdge(String first, String second, E edgeData) {
        List<Edge<E>> edges = nameToNeighbors.get(first);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getVertexName2().equals(second)) {
                Edge<E> edge = new Edge<>(first, second, edgeData);
                edges.set(i, edge);
                return true;
            }
        }
        return false;
    }

    /**
     * Simple retrieval for a vertex
     *
     * @param VertexName The name of the vertex.
     * @return The vertex associated with the name
     */
    @Override
    public Vertex<V> getVertex(String VertexName) throws NoSuchVertexException {
        if (!nameToVertex.containsKey(VertexName)) throw new NoSuchVertexException();
        return nameToVertex.get(VertexName);
    }

    /**
     * Simple edge retrieval with possible no such edge.
     * User assumes a directed edge from vertex1 to vertex2 exists
     *
     * @param vertexName1 Vertex one of edge.
     * @param vertexName2 Vertex two of edge.
     * @return The edge
     */
    @Override
    public Edge<E> getEdge(String vertexName1, String vertexName2) throws NoSuchEdgeException {
        for (Edge<E> edge : nameToNeighbors.get(vertexName1)) {
            if (edge.getVertexName2().equals(vertexName2)) {
                return edge;
            }
        }
        throw new NoSuchEdgeException();
    }

    /**
     * @return A copy of all vertices in the graph
     */
    @Override
    public List<Vertex<V>> getVertices() {
        return new ArrayList<>(nameToVertex.values());
    }

    /**
     * If directed, simply add all edges to a list
     * Else, add edges but check for duplicates
     *
     * IMPORTANT: INTERNALLY, UNDIRECTED GRAPHS REFLECT THE JAVADOC FOR setUndirectedGraph()
     * EDGE (X,Y) IS IN THE GRAPH IFF EDGE (Y,X) IS IN THE GRAPH
     * THUS, WE HAVE TO ENSURE WE RETURN ONLY THE RELEVANT EDGES
     *
     * @return A list of all edges in the graph
     */
    @Override
    public List<Edge<E>> getEdges() {
        List<Edge<E>> edges = new ArrayList<>();

        if (isDirected) {
            nameToNeighbors.values()
                    .forEach(list -> list.forEach(edges::add));
        } else {
            Set<NamePair> seen = new HashSet<>();
            for (List<Edge<E>> neighborList : nameToNeighbors.values()) {
                for (Edge<E> edge : neighborList) {
                    NamePair pair = new NamePair(edge.getVertexName1(), edge.getVertexName2());
                    if (!seen.contains(pair)) {
                        edges.add(edge);
                    }
                    seen.add(pair);
                }
            }
        }

        return edges;
    }

    //Used for getEdges() when graph is undirected to avoid an n^2 solution
    private class NamePair implements Map.Entry<String, String> {
        String vertex1, vertex2;

        NamePair(String v1, String v2) {
            vertex1 = v1;
            vertex2 = v2;
        }

        @Override
        public String getKey() {
            return vertex1;
        }

        @Override
        public String getValue() {
            return vertex2;
        }

        @Override
        public String setValue(String value) {
            return vertex2 = value;
        }

        @Override
        public int hashCode() {
            return vertex2.hashCode() + vertex1.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other.getClass().equals(NamePair.class)) {
                NamePair o = (NamePair) other;
                return o.vertex1.equals(vertex2) && o.vertex2.equals(vertex1) || vertex1.equals(o.vertex1) && vertex2.equals(o.vertex2);
            }
            return false;
        }
    }

    /**
     * @param vertex The vertex to return neighbors for.
     * @return A list of all vertices adjacent to the given vertex
     */
    @Override
    public List<Vertex<V>> getNeighbors(String vertex) {
        List<Vertex<V>> neighbors = new ArrayList<>();
        nameToNeighbors.get(vertex)
                .forEach(neighborName ->
                        neighbors.add(nameToVertex.get(neighborName.getVertexName2()))
                );
        return neighbors;
    }
}