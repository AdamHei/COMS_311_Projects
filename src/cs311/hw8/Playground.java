package cs311.hw8;

import java.util.List;

public class Playground {
    public static void main(String[] args) {
        OSMMap map = new OSMMap();
        map.LoadMap(map.LOCAL_FILE);

        OSMMap.Location from = new OSMMap.Location(42.025251, -93.643702);
        OSMMap.Location to = new OSMMap.Location(42.027522, -93.643659);

//        System.out.println(map.map.getNeighbors(map.ClosestRoad(new OSMMap.Location(42.027769, -93.643702))));

        List<String> path = map.ShortestRoute(from, to);
        System.out.println(map.StreetRoute(path));

        for (String id: path){
            OSMMap.NodeData data = map.map.getVertexData(id);
            System.out.println(data.latitude + ", " + data.longitude);
        }
    }
}
