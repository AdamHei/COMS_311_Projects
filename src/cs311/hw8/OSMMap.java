package cs311.hw8;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;
import cs311.hw8.graphalgorithms.IWeight;
import org.jetbrains.annotations.Contract;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cs311.hw8.graphalgorithms.GraphAlgorithms.ShortestPath;

public class OSMMap {

    private IGraph<NodeData, EdgeData> map;
    private final static String LOCAL_FILE = "C:/Users/Adam/Desktop/AmesMap.txt";

    /**
     * Print the approximate number of miles of roadway in Ames using AmesMap.txt
     * ~Total Edge length / 2
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String mapFileName = args[0], routeFileName = args[1];

        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(mapFileName);

        System.out.println("Total distance " + osmMap.TotalDistance());
        System.out.println();

        List<String> routePairs = Files.readAllLines(Paths.get(routeFileName));
        List<String> locationIDs = new ArrayList<>();
        for (String pair: routePairs){
            String[] latAndLon = pair.split(" ");
            double lat = Double.parseDouble(latAndLon[0]), lon = Double.parseDouble(latAndLon[1]);
            Location location = new Location(lat, lon);
            String id = osmMap.ClosestRoad(location);

            NodeData data = osmMap.map.getVertexData(id);
            System.out.println("Closest is: " + data.latitude + ", " + data.longitude);
            locationIDs.add(id);
        }
//        System.out.println("There are roughly " + (osmMap.map.getEdges().size() / 2) + " street in Ames");
        List<String> streetNames = osmMap.StreetRoute(locationIDs);
        streetNames.forEach(System.out::println);
    }

    /**
     * Default Constructor that creates an empty map
     */
    public OSMMap() {
        refreshMap();
    }

    private void refreshMap() {
        map = new Graph<>();
        map.setDirectedGraph();
    }

    /**
     * @param filename The filename of the XML map to load
     */
    public void LoadMap(String filename) throws IOException, SAXException, ParserConfigurationException {
        refreshMap();
        Document doc = buildDoc(filename);
        initNodes(doc);
        initStreets(doc);
    }

    private void initNodes(Document doc) {
        NodeList nodes = doc.getElementsByTagName("node");

        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            Element element = (Element) nodes.item(i);

            String id = element.getAttribute("id");
            double latitude = Double.parseDouble(element.getAttribute("lat"));
            double longitude = Double.parseDouble(element.getAttribute("lon"));

            map.addVertex(id, new NodeData(latitude, longitude));
        }
    }

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

    private void addStreet(String from, String to, String name) {
        NodeData fromData = map.getVertexData(from);
        NodeData toData = map.getVertexData(to);

        double distance = distance(fromData.latitude, fromData.longitude, toData.latitude, toData.longitude);

//        System.out.println("Adding street from " + from + " to " + to);
        map.addEdge(from, to, new EdgeData(name, distance));
    }


    /**
     * Distance formula courtesy of GeoDataSource at http://www.geodatasource.com/developers/java
     *
     * @return The distance between two coordinate pairs in miles
     */
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    @Contract(pure = true)
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    @Contract(pure = true)
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    private Document buildDoc(String f) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new File(f));
        doc.normalize();
        return doc;
    }

    public double TotalDistance() {
        final double[] total = {0.0};
        map.getEdges().forEach(edge -> total[0] += edge.getEdgeData().distance);
        return total[0] / 2;
    }

    public String ClosestRoad(Location location) {
        double minDistance = Double.POSITIVE_INFINITY;
        String closestId = "";

        for (Vertex<NodeData> node : map.getVertices()) {
            Location temp = new Location(node.getVertexData().latitude, node.getVertexData().longitude);
            double distance = distance(location.getLatitude(), location.getLongitude(), temp.getLatitude(), temp.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                closestId = node.getVertexName();
            }
        }

        return closestId;
    }

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

    public List<String> StreetRoute(List<String> vertexIds) {
        Set<String> streetNames = new HashSet<>();
        List<String> orderedNames = new ArrayList<>();

        for (int i = 0; i < vertexIds.size() - 1; i++) {
            List<Edge<EdgeData>> subPath = ShortestPath(map, vertexIds.get(i), vertexIds.get(i + 1));
            System.out.println("Checking route from " + vertexIds.get(i) + " to " + vertexIds.get(i + 1) + "\n");
            subPath.stream().filter(street -> !streetNames.contains(street.getEdgeData().streetName))
                    .forEach(street -> {
                        orderedNames.add(street.getEdgeData().streetName);
                        streetNames.add(street.getEdgeData().streetName);
            });
        }

        return orderedNames;
    }

    public static class Location {
        double latitude, longitude;

        Location(double lat, double lon) {
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

    private class NodeData {
        double latitude, longitude;

        NodeData(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }
    }

    private class EdgeData implements IWeight {
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
