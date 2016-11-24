package cs311.hw8;

import cs311.hw8.OSMMap.Location;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cs311.hw8.OSMMap.LOCAL_FILE;

/**
 * Created by Adam on 11/23/2016.
 */
public class Playground {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(LOCAL_FILE);

        Location scubaShop = new Location(42.028297, -93.664099);
        Location tylersAppt = new Location(42.025460, -93.659164);
        Location amesWestSideStorage = new Location(42.036617, -93.681695);
        Location caseysNorth = new Location(42.056790, -93.643500);
        Location amesHigh = new Location(42.040529, -93.634627);
        Location byAmesHigh = new Location(42.042134, -93.631865);
        Location idkwhere = new Location(42.054168, -93.628094);
        Location stcecilia = new Location(42.048464, -93.630068);

        List<Location> locations = Arrays.asList(idkwhere, stcecilia);
        List<String> vertexIds = new ArrayList<>();
        locations.forEach(location -> vertexIds.add(osmMap.ClosestRoad(location)));

        vertexIds.forEach(id -> {
            System.out.println("Id: " + id + " has " + osmMap.map.getNeighbors(id).size() + " edges.");
        });
    }
}
