package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphAlgorithmsTest {

    @Test
    public void testTopologicalSort() {
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("H");
        g.addVertex("I");
        g.addVertex("J");
        g.addEdge("A", "B");
        g.addEdge("A", "D");
        g.addEdge("B", "D");
        g.addEdge("B", "E");
        g.addEdge("B", "C");
        g.addEdge("C", "F");
        g.addEdge("D", "E");
        g.addEdge("D", "G");
        g.addEdge("E", "C");
        g.addEdge("E", "F");
        g.addEdge("E", "G");
        g.addEdge("F", "G");
        g.addEdge("F", "H");
        g.addEdge("G", "I");
        g.addEdge("H", "G");
        g.addEdge("H", "J");
        g.addEdge("I", "J");

        List<IGraph.Vertex<Integer>> toposort = GraphAlgorithms.TopologicalSort(g);
        assertEquals(10, toposort.size());
        assertEquals("A", toposort.get(0).getVertexName());
        assertEquals("B", toposort.get(1).getVertexName());
        assertEquals("D", toposort.get(2).getVertexName());
        assertEquals("E", toposort.get(3).getVertexName());
        assertEquals("C", toposort.get(4).getVertexName());
        assertEquals("F", toposort.get(5).getVertexName());
        assertEquals("H", toposort.get(6).getVertexName());
        assertEquals("G", toposort.get(7).getVertexName());
        assertEquals("I", toposort.get(8).getVertexName());
        assertEquals("J", toposort.get(9).getVertexName());
    }

    @Test
    public void testAllTopologicalSort1() {
        //test graph with 1 toposort. Make sure it produces same result as
        //single toposort
        Graph<Integer, Integer> g = new Graph<>();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("H");
        g.addVertex("I");
        g.addVertex("J");
        g.addEdge("A", "B");
        g.addEdge("A", "D");
        g.addEdge("B", "D");
        g.addEdge("B", "E");
        g.addEdge("B", "C");
        g.addEdge("C", "F");
        g.addEdge("D", "E");
        g.addEdge("D", "G");
        g.addEdge("E", "C");
        g.addEdge("E", "F");
        g.addEdge("E", "G");
        g.addEdge("F", "G");
        g.addEdge("F", "H");
        g.addEdge("G", "I");
        g.addEdge("H", "G");
        g.addEdge("H", "J");
        g.addEdge("I", "J");

        List<List<IGraph.Vertex<Integer>>> alltoposort = GraphAlgorithms.AllTopologicalSort(g);
        assertEquals(1, alltoposort.size());
        assertEquals(alltoposort.get(0), GraphAlgorithms.TopologicalSort(g));
    }

    @Test
    public void testAllTopologicalSort2() {
        //test graph with multiple toposorts
        Graph<Integer, Integer> g2 = new Graph<>();
        g2.setDirectedGraph();
        g2.addVertex("A");
        g2.addVertex("B");
        g2.addVertex("C");
        g2.addVertex("D");
        g2.addVertex("E");
        g2.addEdge("A", "B");
        g2.addEdge("A", "C");
        g2.addEdge("A", "D");
        g2.addEdge("B", "C");
        g2.addEdge("B", "E");
        g2.addEdge("C", "E");
        g2.addEdge("D", "E");

        List<List<IGraph.Vertex<Integer>>> alltoposort2 = GraphAlgorithms.AllTopologicalSort(g2);
        assertEquals(3, alltoposort2.size());
        List<IGraph.Vertex<Integer>> valid1 = new ArrayList<>();
        valid1.add(g2.getVertex("A"));
        valid1.add(g2.getVertex("B"));
        valid1.add(g2.getVertex("C"));
        valid1.add(g2.getVertex("D"));
        valid1.add(g2.getVertex("E"));
        List<IGraph.Vertex<Integer>> valid2 = new ArrayList<>();
        valid2.add(g2.getVertex("A"));
        valid2.add(g2.getVertex("B"));
        valid2.add(g2.getVertex("D"));
        valid2.add(g2.getVertex("C"));
        valid2.add(g2.getVertex("E"));
        List<IGraph.Vertex<Integer>> valid3 = new ArrayList<>();
        valid3.add(g2.getVertex("A"));
        valid3.add(g2.getVertex("D"));
        valid3.add(g2.getVertex("B"));
        valid3.add(g2.getVertex("C"));
        valid3.add(g2.getVertex("E"));
        assertTrue(alltoposort2.contains(valid1));
        assertTrue(alltoposort2.contains(valid2));
        assertTrue(alltoposort2.contains(valid3));
    }

    @Test
    public void testAllTopologicalSort3() {
        //in a graph with no edges and n vetices, we should have n! valid sorts
        Graph<Integer, Integer> g3 = new Graph<>();
        g3.setDirectedGraph();
        g3.addVertex("1");
        assertEquals(1, GraphAlgorithms.AllTopologicalSort(g3).size());
        g3.addVertex("2");
        assertEquals(2, GraphAlgorithms.AllTopologicalSort(g3).size());
        g3.addVertex("3");
        assertEquals(6, GraphAlgorithms.AllTopologicalSort(g3).size());
        g3.addVertex("4");
        assertEquals(24, GraphAlgorithms.AllTopologicalSort(g3).size());
        g3.addVertex("5");
        assertEquals(120, GraphAlgorithms.AllTopologicalSort(g3).size());
    }

    @Test
    public void testKruscal() throws IGraph.NoSuchEdgeException {
        Graph<Integer, IntWeight> g1 = new Graph<>();
        g1.setUndirectedGraph();
        g1.addVertex("a");
        g1.addVertex("b");
        g1.addVertex("c");
        g1.addEdge("a", "b", new IntWeight(1));
        g1.addEdge("a", "c", new IntWeight(2));
        g1.addEdge("b", "c", new IntWeight(3));

        IGraph<Integer, IntWeight> mst1 = GraphAlgorithms.Kruscal(g1);
        assertEquals(4, mst1.getEdges().size());
        assertEquals(new IntWeight(1), mst1.getEdgeData("a", "b"));
        assertEquals(new IntWeight(2), mst1.getEdgeData("a", "c"));

        Graph<Integer, IntWeight> g2 = new Graph<>();
        g2.setUndirectedGraph();
        g2.addVertex("a");
        g2.addVertex("b");
        g2.addVertex("c");
        g2.addVertex("d");
        g2.addVertex("e");
        g2.addVertex("f");
        g2.addEdge("a", "b", new IntWeight(1));
        g2.addEdge("b", "c", new IntWeight(2));
        g2.addEdge("b", "e", new IntWeight(3));
        g2.addEdge("b", "d", new IntWeight(4));
        g2.addEdge("c", "e", new IntWeight(5));
        g2.addEdge("d", "e", new IntWeight(6));
        g2.addEdge("e", "f", new IntWeight(7));

        IGraph<Integer, IntWeight> mst2 = GraphAlgorithms.Kruscal(g2);
        assertEquals(10, mst2.getEdges().size());
        assertEquals(new IntWeight(1), mst2.getEdgeData("a", "b"));
        assertEquals(new IntWeight(2), mst2.getEdgeData("b", "c"));
        assertEquals(new IntWeight(3), mst2.getEdgeData("b", "e"));
        assertEquals(new IntWeight(4), mst2.getEdgeData("b", "d"));
        assertEquals(new IntWeight(7), mst2.getEdgeData("e", "f"));
    }

    public static class IntWeight implements IWeight {
        int w;

        public IntWeight(int w) {
            this.w = w;
        }

        public double getWeight() {
            return w;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            return w == ((IntWeight) obj).w;
        }
    }

}