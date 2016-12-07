package cs311.hw8;

import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectedComponents {

    static Map<String, Boolean> visited = new HashMap<>();

    public static void main(String[] args) {
        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(osmMap.LOCAL_FILE);

        List<Vertex<OSMMap.NodeData>> vertices = new ArrayList<>();
        for (Vertex<OSMMap.NodeData> vertex : osmMap.map.getVertices()) {
            if (osmMap.map.getNeighbors(vertex.getVertexName()).size() > 0) {
                vertices.add(vertex);
                visited.put(vertex.getVertexName(), false);
            }
        }

        int connectedComponents = 1;
        dfs(osmMap, vertices.get(0));

        for (int i = 1; i < vertices.size(); i++) {
            if (!visited.get(vertices.get(i).getVertexName())){
                dfs(osmMap, vertices.get(i));
                connectedComponents++;
            }
        }

        System.out.println("Connected components: " + connectedComponents);
    }

    static void dfs(OSMMap osmMap, IGraph.Vertex<OSMMap.NodeData> node){
        if (!visited.containsKey(node.getVertexName())){
            return;
        }
        if (!visited.get(node.getVertexName())){
            visited.put(node.getVertexName(), true);
            for (Vertex<OSMMap.NodeData> vertex: osmMap.map.getNeighbors(node.getVertexName())){
                dfs(osmMap, vertex);
            }
        }
    }
}
