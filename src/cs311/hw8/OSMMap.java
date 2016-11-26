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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cs311.hw8.graphalgorithms.GraphAlgorithms.ShortestPath;

public class OSMMap {

    //TODO Make private
    IGraph<NodeData, EdgeData> map;
    final String LOCAL_FILE = "C:/Users/Adam/Desktop/AmesMap.txt";

    /**
     * Print the approximate number of miles of roadway in Ames using AmesMap.txt
     * ~Total Edge length / 2
     */
    public static void main(String[] args) {
        String mapFileName = args[0];

        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(mapFileName);

        System.out.println("Total distance " + osmMap.TotalDistance());
    }

    /**
     * Print the streets along the route from each vertex
     * @param args The filenames of the map and route files to parse
     */
    public static void main3(String[] args) {
        String mapFileName = args[0], routeFileName = args[1];

        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(mapFileName);

        List<String> routePairs = null;
        try {
            routePairs = Files.readAllLines(Paths.get(routeFileName));
        } catch (IOException e) {
            System.out.println("Invalid filename");
            System.exit(1);
        }

        List<String> locationIDs = new ArrayList<>();
        for (String pair : routePairs) {
            //Parse coordinates and find closest node
            String[] latAndLon = pair.split(" ");
            double lat = Double.parseDouble(latAndLon[0]), lon = Double.parseDouble(latAndLon[1]);
            Location location = new Location(lat, lon);

            String id = osmMap.ClosestRoad(location);
            locationIDs.add(id);
        }

        List<String> streetNames = osmMap.StreetRoute(locationIDs);
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
        }
        return Optional.empty();
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
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return dist;
    }

    @Contract(pure = true)
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    @Contract(pure = true)
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
     * Finds the vertex closest to the given location utilizing the distance() formula
     * @param location The source location
     * @return The id of the vertex closest to the given location
     */
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

    /**
     * Finds the ordered list of vertex ids along the shortest path from one location to the other
     * @param fromLocation The starting location
     * @param toLocation The target location
     * @return The list of vertex ids along the shortest path
     */
    //TODO What if either location has no neighbors?
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
     * The ordered list of street names along the shortest route that hits every vertex given
     * @param vertexIds The vertices to visit
     * @return The sequentially-duplicate-free path along the vertices
     */
    public List<String> StreetRoute(List<String> vertexIds) {
        List<String> orderedNames = new ArrayList<>();

        for (int i = 0; i < vertexIds.size() - 1; i++) {
            List<Edge<EdgeData>> subPath = ShortestPath(map, vertexIds.get(i), vertexIds.get(i + 1));
            subPath = cleanList(subPath);

            if (subPath.size() > 0 && orderedNames.size() > 0 && subPath.get(0).getEdgeData().streetName.equals(orderedNames.get(orderedNames.size() - 1))) {
                //ShortestPath() accounts for duplicates but we have to here
                subPath.remove(0);
            }
            subPath.forEach(street -> orderedNames.add(street.getEdgeData().streetName));
        }
        return orderedNames;
    }

    //Helper method to remove duplicates... Perhaps could be done better
    private List<Edge<EdgeData>> cleanList(List<Edge<EdgeData>> list) {
        List<Edge<EdgeData>> cleaned = new ArrayList<>();
        if (list.size() > 0){
            cleaned.add(list.get(0));
        }

        for (Edge<EdgeData> edge : list) {
            if (!cleaned.get(cleaned.size() - 1).getEdgeData().streetName.equals(edge.getEdgeData().streetName)) {
                cleaned.add(edge);
            }
        }

        return cleaned;
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

    /**
     * Generic Vertex data class that stores coordinates
     */
    //TODO Make private
    public class NodeData {
        double latitude, longitude;

        NodeData(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }
    }

    /**
     * Generic Edge data class that represents street name and length of street
     */
    //TODO Make Private
    class EdgeData implements IWeight {
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



    //TODO Delete me
    public String closestVertexIDwithNeighbors(String id){
        List<String> allVertices = new ArrayList<>();
        map.getVertices().forEach(nodeDataVertex -> allVertices.add(nodeDataVertex.getVertexName()));

        String closest = "";
        double distance = Double.POSITIVE_INFINITY;

        double idLat = map.getVertexData(id).latitude;
        double idLon = map.getVertexData(id).longitude;

        for (String location: allVertices){
            NodeData temp = map.getVertexData(location);
            double tempDist = distance(idLat, idLon, temp.latitude, temp.longitude);
            if (map.getNeighbors(location).size() > 0 && tempDist < distance) {
                closest = location;
                distance = tempDist;
            }
        }
        if (closest.length() == 0) System.out.println("Bruh it didn't work");

        return closest;
    }
}
