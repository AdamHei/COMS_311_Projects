package cs311.hw8;

import java.util.List;

/**
 * Created by Adam on 12/4/2016.
 */
public class Playground {
    public static void main(String[] args) {
        OSMMap map = new OSMMap();
        map.LoadMap(map.LOCAL_FILE);

        OSMMap.Location from = new OSMMap.Location(42.045353, -93.644740);
        OSMMap.Location to = new OSMMap.Location(42.056241, -93.644975);

        List<String> path = map.ShortestRoute(from, to);

        for (String id: path){
            OSMMap.NodeData data = map.map.getVertexData(id);
            System.out.println(data.latitude + ", " + data.longitude);
        }
    }
}
