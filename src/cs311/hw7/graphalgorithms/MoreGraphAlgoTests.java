package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Edge;
import cs311.hw7.graph.IGraph.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoreGraphAlgoTests<V, E extends IWeight> {

    private IGraph<V, EdgeWeight> before, after, inter;
    private List<List<Vertex<V>>> expected_all;

    @Before
    public void setUp() throws Exception {
        before = new Graph<>();
        inter = new Graph<>();

        expected_all = new ArrayList<>();

        List<Vertex<V>> list = new ArrayList<>();

        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);

        list = new ArrayList<>();
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);

        list = new ArrayList<>();
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        list.add(new Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("B", null));
        list.add(new Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        list.add(new Vertex<>("A", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("A", null));
        list.add(new Vertex<>("B", null));
        expected_all.add(list);


        list = new ArrayList<>();
        list.add(new Vertex<>("F", null));
        list.add(new Vertex<>("E", null));
        list.add(new Vertex<>("C", null));
        list.add(new Vertex<>("D", null));
        list.add(new Vertex<>("B", null));
        list.add(new Vertex<>("A", null));
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

        List<List<Vertex<V>>> list_res_all = GraphAlgorithms.AllTopologicalSort(before);

        assertEquals("List of Sorts does not match the size of expected List", expected_all.size(), list_res_all.size());

        for (List<Vertex<V>> item : list_res_all) {
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
        for (Edge<EdgeWeight> e : inter.getEdges()) {
            assertTrue("Edge not in Expected Graph", after.getEdges().contains(e));
        }
        for (Vertex<V> v : inter.getVertices()) {
            assertTrue("Vertex not in Expected Graph", after.getVertices().contains(v));
        }

        for (Edge<EdgeWeight> e : after.getEdges()) {
            assertTrue("Expected Edge not in Graph", inter.getEdges().contains(e));
        }
        for (Vertex<V> v : after.getVertices()) {
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
