package cs311.hw8.testcases;

import cs311.hw8.OSMMap;
import cs311.hw8.OSMMap.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ShortestRouteTests {
    private OSMMap osmMap;
    private List<String> route, computedRoute;

    @Before
    public void setUp() {
        osmMap = new OSMMap();
        osmMap.LoadMap(osmMap.LOCAL_FILE);
    }

    @Test
    public void scubaShopToByAmesHigh() {
        route = Arrays.asList("Oakland Street", "North Hyland Avenue", "Pammel Drive", "University Boulevard", "Haber Road", "13th Street", "Summit Avenue", "Ridgewood Avenue", "20th Street", "Hayes Avenue");

        Location scubaShop = new Location(42.028297, -93.664099);
        Location byAmesHigh = new Location(42.042134, -93.631865);

        computedRoute = osmMap.StreetRoute(osmMap.ShortestRoute(scubaShop, byAmesHigh));
        assertEquals(route, computedRoute);
    }

    @Test
    public void stCeciliaToIDKWhere() {
        route = Arrays.asList("Hoover Avenue", "Wheeler Street", "Nixon Avenue");

        Location idkwhere = new Location(42.054168, -93.628094);
        Location stcecilia = new Location(42.048464, -93.630068);

        computedRoute = osmMap.StreetRoute(osmMap.ShortestRoute(stcecilia, idkwhere));
        assertEquals(route, computedRoute);
    }

    @Test
    public void selfLoop() {
        route = new ArrayList<>();

        Location idkwhere = new Location(42.054168, -93.628094);

        computedRoute = osmMap.StreetRoute(osmMap.ShortestRoute(idkwhere, idkwhere));
        assertEquals(route, computedRoute);
    }

    @Test
    public void straight() {
        route = Collections.singletonList("Lincoln Way");

        Location SDandLincoln = new Location(42.0229098, -93.6786571);
        Location lincolnandGrand = new Location(42.0227968, -93.6199806);

        computedRoute = osmMap.StreetRoute(osmMap.ShortestRoute(SDandLincoln, lincolnandGrand));
        assertEquals(route, computedRoute);
    }
}
