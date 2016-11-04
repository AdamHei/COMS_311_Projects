package cs311.hw7;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;

import java.util.List;

import static cs311.hw7.graphalgorithms.GraphAlgorithms.AllTopologicalSort;


public class Main {
    public static void main(String[] args) {
        IGraph<Integer, Integer> graph = new Graph<>(true);

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

        List<List<IGraph.Vertex<Integer>>> allSorts = AllTopologicalSort(graph);

        for (List<IGraph.Vertex<Integer>> sort : allSorts) {
            for (IGraph.Vertex<Integer> vertex : sort) {
                System.out.print(vertex.getVertexData() + " ");
            }
            System.out.println();
        }
    }
}
