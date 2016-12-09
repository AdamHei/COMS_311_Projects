package cs311.hw8;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;
import cs311.hw8.graphalgorithms.IWeight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cs311.hw8.graphalgorithms.GraphAlgorithms.Kruscal;
import static cs311.hw8.graphalgorithms.GraphAlgorithms.ShortestPath;

public class OSMMap {

    public IGraph<NodeData, EdgeData> map;
    public final String LOCAL_FILE = "C:/Users/Adam/Desktop/AmesMap.txt";

    public static void main1(String[] args) {
        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(osmMap.LOCAL_FILE);

        osmMap.PipeDream();
    }

    /**
     * Print the approximate number of miles of roadway in Ames using AmesMap.txt
     * ~Total Edge length / 2
     */
    public static void main2(String[] args) {
        String mapFileName = args[0];

        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(mapFileName);

        System.out.println(osmMap.TotalDistance());
    }

    /**
     * Print the streets along the route from each vertex
     *
     * @param args The filenames of the map and route files to parse
     */
    public static void main(String[] args) {
        String mapFileName = args[0], routeFileName = args[1];

        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(mapFileName);

        List<String> routePairs;
        try {
            routePairs = Files.readAllLines(Paths.get(routeFileName));
        } catch (IOException e) {
            System.out.println(routeFileName + " is an invalid filename");
            return;
        }

        //Build an array of Locations along the route
        List<String> shortestRouteVertexIds = new ArrayList<>();
        Location[] locations = new Location[routePairs.size()];
        for (int i = 0; i < routePairs.size(); i++) {
            String pair = routePairs.get(i);
            String[] latAndLon = pair.split(" ");
            double lat = Double.parseDouble(latAndLon[0]), lon = Double.parseDouble(latAndLon[1]);
            locations[i] = new Location(lat, lon);
        }

        //Add the vertex ids along the shortest route between every consecutive pairs of vertices
        for (int j = 0; j < locations.length - 1; j++) {
            List<String> subRoute = osmMap.ShortestRoute(locations[j], locations[j + 1]);
            //Don't want consecutive same vertices
            if (j > 0) subRoute.remove(0);
            shortestRouteVertexIds.addAll(subRoute);
        }

        List<String> streetNames = osmMap.StreetRoute(shortestRouteVertexIds);
        streetNames.forEach(System.out::println);
    }

    /**
     * Default Constructor that creates an empty map
     */
    public OSMMap() {
        refreshMap();
    }

    //Helper method to build a fresh map
    private void refreshMap() {
        map = new Graph<>();
        map.setDirectedGraph();
    }

    /**
     * Attempts to initialize the map-graph from the given XML file
     *
     * @param filename The filename of the XML map to load
     */
    public void LoadMap(String filename) {
        refreshMap();
        Optional<Document> doc = buildDoc(filename);

        if (doc.isPresent()) {
            initNodes(doc.get());
            initStreets(doc.get());
        }
    }

    //Attempts to build a Document object from the given filename
    //Uses optionals to avoid null-checks
    private Optional<Document> buildDoc(String filename) {
        if (filename.length() == 0) return Optional.empty();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));
            doc.normalize();
            return Optional.of(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    //Finds all node tags in the XML document and constructs vertices with the latitude and longitude in the element
    private void initNodes(Document doc) {
        NodeList nodes = doc.getElementsByTagName("node");

        //Most inefficient getLength() method ever
        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            //Every node should be an element
            Element element = (Element) nodes.item(i);
            String id = element.getAttribute("id");
            double latitude = Double.parseDouble(element.getAttribute("lat"));
            double longitude = Double.parseDouble(element.getAttribute("lon"));

            map.addVertex(id, new NodeData(latitude, longitude));
        }
    }

    //Similar to initNodes(), constructs edges in the graph that have "highway" and "name" attributes
    //Accounts for one-way edges as well
    private void initStreets(Document doc) {
        NodeList edges = doc.getElementsByTagName("way");

        int edgesLength = edges.getLength();
        for (int i = 0; i < edgesLength; i++) {
            Node edge = edges.item(i);

            NodeList tags = ((Element) edge).getElementsByTagName("tag");
            String name = null, highway = null;
            boolean isOneWay = false;

            int tagLength = tags.getLength();
            for (int j = 0; j < tagLength; j++) {
                Element element = (Element) tags.item(j);
                String k = element.getAttribute("k");
                String v = element.getAttribute("v");

                if (k.equals("highway")) {
                    highway = v;
                } else if (k.equals("name")) {
                    name = v;
                } else if (k.equals("oneway") && v.equals("yes")) {
                    isOneWay = true;
                }
            }

            if (name == null || highway == null) continue;

            List<String> neighbors = new ArrayList<>();
            NodeList neighborNodes = ((Element) edge).getElementsByTagName("nd");

            int neighborNodeLength = neighborNodes.getLength();
            for (int j = 0; j < neighborNodeLength; j++) {
                Element element = (Element) neighborNodes.item(j);
                neighbors.add(element.getAttribute("ref"));
            }

            for (int j = 0; j < neighbors.size() - 1; j++) {
                String from = neighbors.get(j);
                String to = neighbors.get(j + 1);

                addStreet(from, to, name);

                if (!isOneWay) {
                    addStreet(to, from, name);
                }
            }
        }
    }

    //Helper method for adding an edge with the given name and computed distance in the graph
    private void addStreet(String from, String to, String name) {
        NodeData fromData = map.getVertexData(from);
        NodeData toData = map.getVertexData(to);

        double distance = distance(fromData.latitude, fromData.longitude, toData.latitude, toData.longitude);

        map.addEdge(from, to, new EdgeData(name, distance));
    }


    /**
     * Distance formula courtesy of GeoDataSource at http://www.geodatasource.com/developers/java
     *
     * @return The distance between two coordinate pairs in miles
     */
    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**
     * @return Half of all edge distances
     */
    public double TotalDistance() {
        final double[] total = {0.0};
        map.getEdges().forEach(edge -> total[0] += edge.getEdgeData().distance);
        return total[0] / 2;
    }

    /**
     * Finds the vertex closest to the given location with neighbors utilizing the distance() formula
     *
     * @param location The source location
     * @return The id of the vertex closest to the given location
     */
    public String ClosestRoad(Location location) {
        double minDistance = Double.POSITIVE_INFINITY;
        String closestId = "";

        for (Vertex<NodeData> node : map.getVertices()) {
            Location tempLocation = new Location(node.getVertexData().latitude, node.getVertexData().longitude);
            double tempDistance = distance(location.getLatitude(), location.getLongitude(), tempLocation.getLatitude(), tempLocation.getLongitude());
            if (tempDistance < minDistance && map.getNeighbors(node.getVertexName()).size() > 0) {
                minDistance = tempDistance;
                closestId = node.getVertexName();
            }
        }

        return closestId;
    }

    /**
     * Finds the ordered list of vertex ids along the shortest path from one location to the other
     *
     * @param fromLocation The starting location
     * @param toLocation   The target location
     * @return The list of vertex ids along the shortest path
     */
    public List<String> ShortestRoute(Location fromLocation, Location toLocation) {
        String fromId = ClosestRoad(fromLocation);
        String toId = ClosestRoad(toLocation);

        List<Edge<EdgeData>> path = ShortestPath(map, fromId, toId);
        List<String> pathIds = new ArrayList<>();

        if (path.size() > 0) {
            pathIds.add(path.get(0).getVertexName1());
            pathIds.addAll(path.stream()
                    .map(Edge::getVertexName2)
                    .collect(Collectors.toList()));
        }
        return pathIds;
    }

    /**
     * The ordered list of street names along the route that hits every vertex given
     *
     * @param vertexIds The vertices to visit
     * @return The sequentially-duplicate-free path along the vertices
     */
    public List<String> StreetRoute(List<String> vertexIds) {
        List<String> orderedNames = new ArrayList<>();

        for (int i = 0; i < vertexIds.size() - 1; i++) {
            Edge<EdgeData> edge;
            try {
                edge = map.getEdge(vertexIds.get(i), vertexIds.get(i + 1));
            } catch (IGraph.NoSuchEdgeException e) {
                System.out.println("There was no direct edge between " + vertexIds.get(i) + " and " + vertexIds.get(i + 1));
                return new ArrayList<>();
            }
            if (orderedNames.size() == 0 || orderedNames.size() > 0 && !orderedNames.get(orderedNames.size() - 1).equals(edge.getEdgeData().streetName)) {
                orderedNames.add(edge.getEdgeData().streetName);
            }
        }

        return orderedNames;
    }

    public void PipeDream() {
        IGraph<NodeData, EdgeData> mst = Kruscal(map);
        final double[] weightSum = {0.0};
        mst.getEdges().forEach(edge -> weightSum[0] += edge.getEdgeData().getWeight());
        System.out.println(weightSum[0]);
    }

    public List<Vertex<NodeData>> ApproximateTSP(List<String> vertexIds) {
        IGraph<NodeData, EdgeData> graph = new Graph<>();
        graph.setUndirectedGraph();

        vertexIds.forEach(vertex -> graph.addVertex(vertex, map.getVertexData(vertex)));

        for (int i = 0; i < vertexIds.size() - 1; i++) {
            String first = vertexIds.get(i);
            for (int j = i + 1; j < vertexIds.size(); j++) {
                String second = vertexIds.get(j);
                List<Edge<EdgeData>> path = ShortestPath(map, first, second);
                final double[] distance = {0.0};
                path.forEach(edge -> distance[0] += edge.getEdgeData().getWeight());
                graph.addEdge(first, second, new EdgeData(first + second, distance[0]));
            }
        }

        IGraph<NodeData, EdgeData> mst = Kruscal(graph);

        //Might be empty
        Vertex<NodeData> start = mst.getVertices().get(0);
        List<Vertex<NodeData>> tspPath = new ArrayList<>();

        preorderTraversal(graph, start, tspPath);

        //TODO Go back to start?
//        tspPath.add(0, tspPath.get(tspPath.size() - 1));

        return tspPath;

//        tspPath.forEach(vertex -> System.out.println(graph.getVertexData(vertex.getVertexName()).latitude + ", " + graph.getVertexData(vertex.getVertexName()).longitude));
    }

    private void preorderTraversal(IGraph<NodeData, EdgeData> graph, Vertex<NodeData> vertex, List<Vertex<NodeData>> tspPath){
        tspPath.add(0, vertex);
        for (Vertex<NodeData> neighbor: graph.getNeighbors(vertex.getVertexName())){
            if (!tspPath.contains(neighbor)){
                preorderTraversal(graph, neighbor, tspPath);
            }
        }
    }

    public static class Location {
        double latitude, longitude;

        public Location(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    /**
     * Generic Vertex data class that stores coordinates
     */
    //TODO make private
    public class NodeData {
        public double latitude, longitude;

        NodeData(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }
    }

    /**
     * Generic Edge data class that represents street name and length of street
     */
    public class EdgeData implements IWeight {
        double distance;
        String streetName;

        EdgeData(String s, double d) {
            streetName = s;
            distance = d;
        }

        @Override
        public double getWeight() {
            return distance;
        }
    }
}
