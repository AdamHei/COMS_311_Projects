package cs311.hw8;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AnotherPlayground {
    public static void main(String[] args) {
        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(osmMap.LOCAL_FILE);

        OSMMap.Location location = new OSMMap.Location(42.0226047, -93.6466534);

        System.out.println(osmMap.ClosestRoad(location));

        osmMap.map.getNeighbors(osmMap.ClosestRoad(location)).forEach(neighbor -> System.out.println(neighbor.getVertexData().latitude));
    }
}
