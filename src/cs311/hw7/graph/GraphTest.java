package cs311.hw7.graph;

import cs311.hw7.graphalgorithms.GraphAlgorithms;
import cs311.hw7.graphalgorithms.IWeight;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest<V, E extends IWeight> {

    @Test
    public void testIsDirectedGraph() {
        Graph<Integer, Integer> g = new Graph<>();
        g.setDirectedGraph();
        assertTrue(g.isDirectedGraph());
        g.setUndirectedGraph();
        assertFalse(g.isDirectedGraph());
    }

    @Test
    public void testAddVertex() {
        //just tests for the right exceptions to be thrown
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("a");
        try {
            g.addVertex("a");
            fail("no exception thrown");
        } catch (IGraph.DuplicateVertexException e) {
            //success
        } catch (Exception e) {
            fail("wrong exception caught");
        }
    }

    @Test
    public void testAddEdge() {
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        g.addEdge("a", "b");

        try {
            g.addEdge("a", "d");
            fail("no exception thrown");
        } catch (IGraph.NoSuchVertexException e) {
            //success
        } catch (Exception e) {
            fail("wrong exception caught");
        }

        try {
            g.addEdge("a", "b");
            fail("no exception thrown");
        } catch (IGraph.DuplicateEdgeException e) {
            //success
        } catch (Exception e) {
            fail("wrong exception caught");
        }
    }

    @Test
    public void testGetVertexData() {
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("a");
        g.addVertex("b", 1);
        assertEquals(Integer.valueOf(1), g.getVertexData("b"));
        assertEquals(null, g.getVertexData("a"));
        g.setVertexData("a", 2);
        assertEquals(Integer.valueOf(2), g.getVertexData("a"));

        try {
            g.getVertexData("c");
            fail("No exveption thrown");
        } catch (IGraph.NoSuchVertexException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }
    }

    @Test
    public void testGetEdgeData() throws IGraph.NoSuchEdgeException {
        Graph<Integer, Integer> g = new Graph<>();
        g.setDirectedGraph();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        g.addEdge("a", "b", 1);
        g.addEdge("b", "c");

        assertEquals(Integer.valueOf(1), g.getEdgeData("a", "b"));
        assertEquals(null, g.getEdgeData("b", "c"));

        g.setEdgeData("b", "c", 2);
        assertEquals(Integer.valueOf(2), g.getEdgeData("b", "c"));

        try {
            g.getEdgeData("b", "a");
            fail("No exception thrown");
        } catch (IGraph.NoSuchEdgeException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }

        try {
            g.getEdgeData("a", "d");
            fail("No exception thrown");
        } catch (IGraph.NoSuchVertexException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }

        g.setUndirectedGraph();
        assertEquals(Integer.valueOf(1), g.getEdgeData("a", "b"));
        assertEquals(Integer.valueOf(1), g.getEdgeData("b", "a"));
    }

    @Test
    public void testGetVertex() {
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("a");
        g.addVertex("b", 1);
        assertEquals(new IGraph.Vertex<Integer>("b", 1), g.getVertex("b"));
        assertEquals(new IGraph.Vertex<Integer>("a", null), g.getVertex("a"));

        try {
            g.getVertex("c");
            fail("No exveption thrown");
        } catch (IGraph.NoSuchVertexException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }
    }

    @Test
    public void testGetEdge() {
        Graph<Integer, Integer> g = new Graph<>();
        g.setDirectedGraph();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        g.addEdge("a", "b", 1);
        g.addEdge("b", "c");

        assertEquals(new IGraph.Edge<>("a", "b", 1), g.getEdge("a", "b"));
        assertEquals(new IGraph.Edge<>("b", "c", null), g.getEdge("b", "c"));

        try {
            g.getEdgeData("b", "a");
            fail("No exception thrown");
        } catch (IGraph.NoSuchEdgeException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }

        try {
            g.getEdgeData("a", "d");
            fail("No exception thrown");
        } catch (IGraph.NoSuchVertexException e) {
            // success
        } catch (Exception e) {
            fail("wrong Exception thrown");
        }

        g.setUndirectedGraph();
        assertEquals(g.getEdge("b", "a").getEdgeData(), g.getEdge("a", "b").getEdgeData());
    }

    @Test
    public void testGetNeighbors() {
        Graph<Integer, Integer> g = new Graph<>();
        g.setDirectedGraph();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        g.addEdge("a", "b");
        g.addEdge("c", "a");

        List<IGraph.Vertex<Integer>> ret = g.getNeighbors("a");
        assertEquals(ret.size(), 1);
        assertTrue(ret.contains(new IGraph.Vertex<Integer>("b", null)));

        g.setUndirectedGraph();
        ret = g.getNeighbors("a");
        assertEquals(ret.size(), 2);
        assertTrue(ret.contains(new IGraph.Vertex<Integer>("b", null)));
        assertTrue(ret.contains(new IGraph.Vertex<Integer>("c", null)));
    }


    private final String KERMIT = "Kermit the Frog";
    private final String RALPH = "Ralph the Dog";

    @Test
    public void testAddVertices() {
        Graph<String, String> test = new Graph<String, String>();
        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("A");
        arr.add("B");
        arr.add("C");

        List<IGraph.Vertex<String>> vertices = test.getVertices();
        for (IGraph.Vertex<String> v : vertices) {
            if (!arr.remove(v.getVertexName())) {
                fail("Graph added an unwanted vertex.");
            }
        }

        if (arr.size() > 0) {
            fail("Graph is missing a vertex!");
        }
    }

    @Test
    public void testAddVerticesFailure() {
        Graph<String, String> test = new Graph<String, String>();
        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        try {
            test.addVertex("C");
        } catch (IGraph.DuplicateVertexException e) {
            // Test passed, return early.
            return;
        }

        // Catch statement missed, test failed.
        fail("Failed to throw DuplicateVertexException when adding duplicate vertex.");
    }

    @Test
    public void testNeighborsDirected() {
        Graph<String, String> test = new Graph<String, String>();
        test.setDirectedGraph();

        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        test.addEdge("A", "B");
        test.addEdge("A", "C");

        // Make sure we actually have 2 edges.
        assertEquals(2, test.getEdges().size());

        // Get the neighbors of A, this should be both B and C.
        List<IGraph.Vertex<String>> arr = test.getNeighbors("A");
        assertEquals("Expected 2 neighbors of A", 2, arr.size());

        // Vertices B and C should have no neighbors.
        arr = test.getNeighbors("B");
        assertEquals("Expected 0 neighbors of B", 0, arr.size());
        arr = test.getNeighbors("C");
        assertEquals("Expected 0 neighbors of C", 0, arr.size());
    }

    @Test
    public void testNeighborsUndirected() {
        Graph<String, String> test = new Graph<String, String>();
        test.setUndirectedGraph();

        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        test.addEdge("A", "B");
        test.addEdge("A", "C");

        // Make sure we actually have 2 edges.
        assertEquals(4, test.getEdges().size());

        // Get the neighbors of A, this should be both B and C.
        List<IGraph.Vertex<String>> arr = test.getNeighbors("A");
        assertEquals("Expected 2 neighbors of A", 2, arr.size());
        // B and C both have neighbors in A, but not each other.
        arr = test.getNeighbors("B");
        assertEquals("Expected 1 neighbors of B", 1, arr.size());
        arr = test.getNeighbors("C");
        assertEquals("Expected 1 neighbors of C", 1, arr.size());
    }

    @Test
    public void testDuplicateEdgesDirected() {
        Graph<String, String> test = new Graph<String, String>();
        test.setDirectedGraph();

        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        test.addEdge("A", "B");
        test.addEdge("A", "C");

        // This is okay because the graph is directed.
        test.addEdge("B", "A");
        assertEquals(3, test.getEdges().size());

        try {
            // This is not okay.
            test.addEdge("A", "C");
        } catch (IGraph.DuplicateEdgeException e) {
            // Test passed.
            return;
        }

        fail("Graph class did not throw DuplicateEdgeException where expected.");
    }

    @Test
    public void testDuplicateEdgesUndirected() {
        Graph<String, String> test = new Graph<String, String>();
        test.setUndirectedGraph();

        test.addVertex("A");
        test.addVertex("B");
        test.addVertex("C");

        test.addEdge("A", "B");
        test.addEdge("A", "C");

        try {
            // This is not okay for an undirected graph.
            test.addEdge("B", "A");
        } catch (IGraph.DuplicateEdgeException e) {
            // Test passed.
            return;
        }

        fail("Graph class did not throw DuplicateEdgeException where expected.");
    }

    @Test
    public void testAddEdgeWithInvalidVertices() {
        Graph<String, String> test = new Graph<String, String>();
        test.addVertex("A");
        test.addVertex("B");

        try {
            test.addEdge("A", "C");
        } catch (IGraph.NoSuchVertexException e) {
            // Test passed.
            return;
        }

        fail("Invalid edge added. Cannot add edge (A,C) with no such vertex C.");
    }

    @Test
    public void testVertexData() {
        Graph<String, String> test = new Graph<String, String>();
        test.addVertex("A", KERMIT);
        test.addVertex("B", RALPH);

        assertEquals(KERMIT, test.getVertexData("A"));
        assertEquals(RALPH, test.getVertexData("B"));
    }

    @Test
    public void testEdgeData() {
        Graph<String, String> test = new Graph<String, String>();
        test.setDirectedGraph();
        test.addVertex("A");
        test.addVertex("B");

        test.addEdge("A", "B", KERMIT);
        test.addEdge("B", "A", RALPH);

        try {
            assertEquals(KERMIT, test.getEdgeData("A", "B"));
            assertEquals(RALPH, test.getEdgeData("B", "A"));
        } catch (Exception e) {
            fail("This really shouldn't throw an exception.");
        }
    }

    @Test
    public void testEdgeDataUndirected() {
        Graph<String, String> test = new Graph<String, String>();
        test.setUndirectedGraph();
        test.addVertex("A");
        test.addVertex("B");
        test.addEdge("A", "B", KERMIT);

        try {
            assertEquals(KERMIT, test.getEdgeData("B", "A"));
        } catch (IGraph.NoSuchEdgeException e) {
            fail("Make sure that edge BA counts as edge AB in an undirected graph.");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testVertexDataFailure() {
        Graph<String, String> test = new Graph<String, String>();
        test.addVertex("A", KERMIT);
        test.addVertex("B", RALPH);

        try {
            test.getVertex("C");
        } catch (IGraph.NoSuchVertexException e) {
            // Test passed.
            return;
        }

        fail("Graph class failed to throw NoSuchVertexException.");
    }

    @Test
    public void testEdgeDataFailure() {
        Graph<String, String> test = new Graph<String, String>();
        test.setDirectedGraph();
        test.addVertex("A");
        test.addVertex("B");

        try {
            test.getEdgeData("A", "B");
        } catch (IGraph.NoSuchEdgeException e) {
            // Test passed.
            return;
        }

        fail("Graph class failed to throw NoSuchEdgeException.");
    }


    private IGraph<V, EdgeWeight> before, after, inter;
    private List<List<IGraph.Vertex<V>>> expected_all;

    @Before
    public void setUp() throws Exception {
        before = new Graph<>();
        inter = new Graph<>();

        expected_all = new ArrayList<>();

        List<IGraph.Vertex<V>> list = new ArrayList<>();

        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);

        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);

        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        list.add(new IGraph.Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("B", null));
        list.add(new IGraph.Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        list.add(new IGraph.Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("A", null));
        list.add(new IGraph.Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new IGraph.Vertex<>("F", null));
        list.add(new IGraph.Vertex<>("E", null));
        list.add(new IGraph.Vertex<>("C", null));
        list.add(new IGraph.Vertex<>("D", null));
        list.add(new IGraph.Vertex<>("B", null));
        list.add(new IGraph.Vertex<>("A", null));
        expected_all.add(list);
    }

    @After
    public void tearDown() throws Exception {

        before = new Graph<>();
        inter = new Graph<>();
        expected_all = new ArrayList<>();
        after = new Graph<>();
    }

    @Test
    public void testTopologicalSort() {
        before.setDirectedGraph();
        before.addVertex("A", null);
        before.addVertex("B", null);
        before.addVertex("C", null);
        before.addVertex("D", null);
        before.addVertex("E", null);
        before.addVertex("F", null);
        before.addEdge("F", "A", null);
        before.addEdge("E", "A", null);
        before.addEdge("F", "C", null);
        before.addEdge("E", "B", null);
        before.addEdge("D", "B", null);
        before.addEdge("C", "D", null);

        List<IGraph.Vertex<V>> list_res = GraphAlgorithms.TopologicalSort(before);

        assertEquals("Topological Sort List size does not match expected size", expected_all.get(0).size(), list_res.size());
        assertTrue("Topological Sort not in expected list", expected_all.contains(list_res));
    }

    @Test
    public void testAllTopologicalSort() {
        before.setDirectedGraph();
        before.addVertex("A", null);
        before.addVertex("B", null);
        before.addVertex("C", null);
        before.addVertex("D", null);
        before.addVertex("E", null);
        before.addVertex("F", null);
        before.addEdge("F", "A", null);
        before.addEdge("E", "A", null);
        before.addEdge("F", "C", null);
        before.addEdge("E", "B", null);
        before.addEdge("D", "B", null);
        before.addEdge("C", "D", null);

        List<List<IGraph.Vertex<V>>> list_res_all = GraphAlgorithms.AllTopologicalSort(before);

        assertEquals("List of Sorts does not match the size of expected List", expected_all.size(), list_res_all.size());

        for (List<IGraph.Vertex<V>> item : list_res_all) {
            assertTrue("Sort not in expected List", expected_all.contains(item));
        }

    }

    @Test
    public void testKruscal() {
        before = new Graph<>();
        before.setUndirectedGraph();
        before.addVertex("A", null);
        before.addVertex("B", null);
        before.addVertex("C", null);
        before.addVertex("D", null);
        before.addVertex("E", null);
        before.addVertex("F", null);
        before.addVertex("G", null);
        before.addEdge("A", "B", new EdgeWeight(7));
        before.addEdge("B", "C", new EdgeWeight(8));
        before.addEdge("C", "E", new EdgeWeight(5));
        before.addEdge("A", "D", new EdgeWeight(5));
        before.addEdge("D", "B", new EdgeWeight(9));
        before.addEdge("B", "E", new EdgeWeight(7));
        before.addEdge("D", "E", new EdgeWeight(15));
        before.addEdge("D", "F", new EdgeWeight(6));
        before.addEdge("E", "F", new EdgeWeight(8));
        before.addEdge("F", "G", new EdgeWeight(11));
        before.addEdge("E", "G", new EdgeWeight(9));

        inter = GraphAlgorithms.Kruscal(before);

        after = new Graph<>();
        after.setUndirectedGraph();
        after.addVertex("A", null);
        after.addVertex("B", null);
        after.addVertex("C", null);
        after.addVertex("D", null);
        after.addVertex("E", null);
        after.addVertex("F", null);
        after.addVertex("G", null);
        after.addEdge("A", "B", new EdgeWeight(7));
        after.addEdge("C", "E", new EdgeWeight(5));
        after.addEdge("A", "D", new EdgeWeight(5));
        after.addEdge("B", "E", new EdgeWeight(7));
        after.addEdge("D", "F", new EdgeWeight(6));
        after.addEdge("E", "G", new EdgeWeight(9));


        assertEquals("Number of Graph Edges do not match expected Graph", after.getEdges().size(), inter.getEdges().size());
        assertEquals("Number of Graph Verticies do not match expected Graph", after.getVertices().size(), inter.getVertices().size());
        for (IGraph.Edge<EdgeWeight> e : inter.getEdges()) {
            assertTrue("Edge not in Expected Graph", after.getEdges().contains(e));
        }
        for (IGraph.Vertex<V> v : inter.getVertices()) {
            assertTrue("Vertex not in Expected Graph", after.getVertices().contains(v));
        }

        for (IGraph.Edge<EdgeWeight> e : after.getEdges()) {
            assertTrue("Expected Edge not in Graph", inter.getEdges().contains(e));
        }
        for (IGraph.Vertex<V> v : after.getVertices()) {
            assertTrue("Expected Vertex not in Graph", inter.getVertices().contains(v));
        }

    }

    private static class EdgeWeight implements IWeight {
        double weight;

        EdgeWeight(double a_weight) {
            weight = a_weight;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return Double.toString(weight);
        }
    }
}