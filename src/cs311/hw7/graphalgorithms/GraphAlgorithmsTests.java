package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;
import org.junit.Test;

import java.util.*;

import static cs311.hw7.graphalgorithms.GraphAlgorithms.Kruscal;
import static cs311.hw7.graphalgorithms.GraphAlgorithms.TopologicalSort;
import static org.junit.Assert.assertEquals;


public class GraphAlgorithmsTests {

    private IGraph<Integer, Integer> graph;
    private List<IGraph.Vertex<Integer>> sorted;

    private void init100Nodes() {
        graph = new Graph<>(true);
        for (int i = 0; i < 101; i++) {
            graph.addVertex(i + "", i);
        }
    }

    @Test
    public void linearTopoTest() {
        init100Nodes();
        for (int i = 0; i < 100; i++) {
            graph.addEdge(i + "", (i + 1) + "");
        }

        sorted = TopologicalSort(graph);

        for (int i = 0; i < 101; i++) {
            assertEquals(Integer.valueOf(i), sorted.get(i).getVertexData());
        }
    }

    @Test
    public void allToOne() {
        init100Nodes();
        for (int i = 0; i < 101; i++) {
            graph.addEdge(i + "", 50 + "");
        }

        sorted = TopologicalSort(graph);
        assertEquals(Integer.valueOf(50), sorted.get(sorted.size() - 1).getVertexData());
    }

    @Test
    public void sizeOne() {
        graph = new Graph<>(true);
        graph.addVertex("1", 1);

        sorted = TopologicalSort(graph);
        assertEquals(Integer.valueOf(1), sorted.get(0).getVertexData());
    }

    @Test
    public void regularTopo() {
        graph = new Graph<>(true);
        graph.addVertex("1", 1);
        graph.addVertex("2", 2);
        graph.addVertex("3", 3);
        graph.addVertex("18", 18);
        graph.addVertex("19", 19);
        graph.addVertex("20", 20);

        graph.addEdge("1", "18");
        graph.addEdge("3", "18");
        graph.addEdge("18", "2");
        graph.addEdge("19", "3");
        graph.addEdge("20", "1");
        graph.addEdge("20", "2");
        graph.addEdge("3", "20");

        sorted = TopologicalSort(graph);

        assertEquals(Integer.valueOf(19), sorted.get(0).getVertexData());
        assertEquals(Integer.valueOf(3), sorted.get(1).getVertexData());
        assertEquals(Integer.valueOf(20), sorted.get(2).getVertexData());
        assertEquals(Integer.valueOf(1), sorted.get(3).getVertexData());
        assertEquals(Integer.valueOf(18), sorted.get(4).getVertexData());
        assertEquals(Integer.valueOf(2), sorted.get(5).getVertexData());
    }

//    @Test
//    public void KruskalsSimpleTest() {
//        IGraph<Integer, EdgeWeight> myGraph = new Graph<>(false);
//
//        for (int i = 1; i < 5; i++) {
//            myGraph.addVertex(i + "", i);
//        }
//
//        myGraph.addEdge("1", "2", new EdgeWeight(1));
//        myGraph.addEdge("1", "4", new EdgeWeight(2));
//        myGraph.addEdge("1", "3", new EdgeWeight(3));
//        myGraph.addEdge("2", "3", new EdgeWeight(4));
//        myGraph.addEdge("2", "4", new EdgeWeight(5));
//        myGraph.addEdge("4", "3", new EdgeWeight(6));
//
//        IGraph<Integer, EdgeWeight> mst = Kruscal(myGraph);
//
//        for ()
//    }

    public static class EdgeWeight implements IWeight{
        int weight;
        public EdgeWeight(int w){
            weight = w;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }
}
