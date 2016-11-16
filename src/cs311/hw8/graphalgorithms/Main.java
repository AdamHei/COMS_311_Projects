package cs311.hw8.graphalgorithms;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;

import java.util.List;

/**
 * Created by Adam on 11/16/2016.
 */
public class Main {
    public static void main(String[] args) {
        IGraph<Integer, EdgeWeight> graph = new Graph<>();
        graph.setUndirectedGraph();

        for (int i = 1; i <= 5; i++) {
            graph.addVertex(i + "", i);
        }

        graph.addEdge("1", "2", new EdgeWeight(5));
        graph.addEdge("2", "3", new EdgeWeight(3));
        graph.addEdge("3", "4", new EdgeWeight(6));
        graph.addEdge("3", "5", new EdgeWeight(1));
        graph.addEdge("1", "5", new EdgeWeight(8));

        List<Edge<EdgeWeight>> shortestPath = GraphAlgorithms.ShortestPath(graph, "1", "5");
        for (Edge<EdgeWeight> edge: shortestPath){
            System.out.println(edge.getVertexName1() + " -> " + edge.getVertexName2() + "    " + edge.getEdgeData().getWeight());
        }
    }

    private static class EdgeWeight implements cs311.hw8.graphalgorithms.IWeight {
        int weight;
        EdgeWeight(int w){
            weight = w;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }
}
