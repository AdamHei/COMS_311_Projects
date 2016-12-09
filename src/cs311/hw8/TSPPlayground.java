//package cs311.hw8;
//
//import cs311.hw8.graph.IGraph;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class TSPPlayground {
//    public static void main(String[] args) {
//        OSMMap map = new OSMMap();
//        map.LoadMap(map.LOCAL_FILE);
//
//        List<String> tspIds = new ArrayList<>();
//        OSMMap.Location l1 = new OSMMap.Location(42.012036, -93.678938);
//        OSMMap.Location l2 = new OSMMap.Location(42.034910, -93.673125);
//        OSMMap.Location l3 = new OSMMap.Location(42.056286, -93.647585);
//        OSMMap.Location l4 = new OSMMap.Location(42.036563, -93.608607);
//        OSMMap.Location l5 = new OSMMap.Location(42.006845, -93.610850);
//
//        List<OSMMap.Location> locations = Arrays.asList(l1, l2, l3, l4, l5);
//        locations.forEach(location -> tspIds.add(map.ClosestRoad(location)));
//
//        long start = System.nanoTime();
//        List<IGraph.Vertex<OSMMap.NodeData>> path = map.ApproximateTSP(tspIds);
//        System.out.println("That took " + (System.nanoTime() - start) / 1000000 + " milliseconds!");
//
//        path.forEach(p -> System.out.println(map.map.getVertexData(p.getVertexName()).latitude + ", " + map.map.getVertexData(p.getVertexName()).longitude));
//        System.out.println();
//        for (int i = 0; i < path.size() - 1; i++) {
//            OSMMap.Location from = new OSMMap.Location(path.get(i).getVertexData().latitude, path.get(i).getVertexData().longitude);
//            OSMMap.Location to = new OSMMap.Location(path.get(i + 1).getVertexData().latitude, path.get(i + 1).getVertexData().longitude);
//
//            List<String> subPath = map.ShortestRoute(from, to);
//            subPath.forEach(str -> System.out.println(map.map.getVertexData(str).latitude + ", " + map.map.getVertexData(str).longitude));
//        }
//    }
//}
