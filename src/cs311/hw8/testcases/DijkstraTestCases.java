package cs311.hw8.testcases;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graphalgorithms.IWeight;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static cs311.hw8.graphalgorithms.GraphAlgorithms.ShortestPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DijkstraTestCases {

    private IGraph<Integer, EdgeWeight> graph;

    @Test
    public void noReach() {
        graph = new Graph<>();
        graph.setUndirectedGraph();
        initNNodes(20);
        Random rand = new Random();

        for (int i = 0; i < 20; i++) {
            int left = rand.nextInt(20) + 1;
            int right = rand.nextInt(20) + 1;
            if (left != 17 && right != 17) {
                try {
                    graph.addEdge(left + "", right + "", new EdgeWeight(rand.nextInt()));
                } catch (IGraph.DuplicateEdgeException e) {
                    //Everything is fine
                    i--;
                }
            }
        }

        for (int i = 1; i <= 20; i++) {
            List<Edge<EdgeWeight>> path = ShortestPath(graph, "17", i + "");
            assertTrue(path.size() == 0);
        }
    }

    @Test
    public void simpleTest() {
        graph = new Graph<>();
        graph.setUndirectedGraph();
        initNNodes(4);

        graph.addEdge("1", "2", new EdgeWeight(5));
        graph.addEdge("2", "3", new EdgeWeight(500));
        graph.addEdge("3", "4", new EdgeWeight(20));
        graph.addEdge("2", "4", new EdgeWeight(1));

        List<Edge<EdgeWeight>> path = ShortestPath(graph, "1", "4");
        assertTrue(path.get(0).getEdgeData().getWeight() == 5.0);
        assertEquals(path.get(0).getVertexName1(), "1");
        assertEquals(path.get(0).getVertexName2(), "2");
        assertTrue(path.get(1).getEdgeData().getWeight() == 1.0);
        assertEquals(path.get(1).getVertexName1(), "2");
        assertEquals(path.get(1).getVertexName2(), "4");

        path = ShortestPath(graph, "3", "1");
        assertTrue(path.get(0).getEdgeData().getWeight() == 20.0);
        assertEquals(path.get(0).getVertexName1(), "3");
        assertEquals(path.get(0).getVertexName2(), "4");
        assertTrue(path.get(1).getEdgeData().getWeight() == 1.0);
        assertEquals(path.get(1).getVertexName1(), "4");
        assertEquals(path.get(1).getVertexName2(), "2");
        assertTrue(path.get(2).getEdgeData().getWeight() == 5.0);
        assertEquals(path.get(2).getVertexName1(), "2");
        assertEquals(path.get(2).getVertexName2(), "1");
    }

    private void initNNodes(int n) {
        for (int i = 1; i <= n; i++) {
            graph.addVertex(i + "", i);
        }
    }

    private static class EdgeWeight implements IWeight {
        int weight;

        EdgeWeight(int w) {
            weight = w;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }
}
