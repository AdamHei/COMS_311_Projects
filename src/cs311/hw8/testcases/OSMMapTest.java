package cs311.hw8.testcases;

import cs311.hw8.OSMMap;
import cs311.hw8.graph.IGraph;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class OSMMapTest {
    // Change these paths to match your setup. For example, my text files are in the outer package directory
    private static final String routefile = "C:/Users/Adam/Desktop/routes2.txt";
    private static final String mapfile = "C:/Users/Adam/Desktop/AmesMap.txt";
    private static OSMMap osmMap;

    @BeforeClass
    public static void setup() {
        osmMap = new OSMMap();
        long starttime = System.nanoTime();
        osmMap.LoadMap(mapfile);
        long time = System.nanoTime() - starttime;
        System.out.println("Load took " + time / 1000000 + " ms");
    }

    @Test
    public void testLoad() {
        // You don't actually need to implement the OSMMap.getGraph() method.
        // I just did it for the sake of the test and will not include it in my submission.
        IGraph<OSMMap.NodeData, OSMMap.EdgeData> g = osmMap.map;
        assertTrue(g.isDirectedGraph());
        assertEquals(121648, g.getVertices().size());
        assertEquals(34785, g.getEdges().size());
    }

    @Test
    public void testClosestRoad() {
        String v = osmMap.ClosestRoad(new OSMMap.Location(42.0492620, -93.7442400));
        assertEquals("159018339", v);
    }

    @Test
    public void testAlmostClosestRoad() {
        String v = osmMap.ClosestRoad(new OSMMap.Location(42.0492621, -93.7442499));
        assertEquals("159018339", v);
    }

    @Test
    public void testNotSoClosestRoad() {
        String v = osmMap.ClosestRoad(new OSMMap.Location(42.0492600, -93.7442450));
        assertEquals("159018339", v);
    }

    @Test
    public void testmain3() {
        /*
        Here's a hack that makes sure the method prints the right thing.
        http://stackoverflow.com/a/8708357
        */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        String args[] = {mapfile, routefile};
//        OSMMap.main3(args);
        // On Windows, use \r\n, on *nix, change to \n. Sorry for not writing portable code...
        assertEquals("Marshall Avenue\r\n" +
                "Story Street\r\n" +
                "Crane Avenue\r\n" +
                "West Street\r\n" +
                "Union Drive\r\n" +
                "Bissell Road\r\n" +
                "Osborn Drive\r\n", baos.toString());
    }

    @Test
    public void testmain2() {
        /*
        Here's a hack that makes sure the method prints the right thing.
        http://stackoverflow.com/a/8708357
        */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        String[] args = {"C:/Users/Adam/Desktop/AmesMap.txt"};
        OSMMap.main2(args);
        assertEquals(763.012, Double.valueOf(baos.toString()), .1); // delta might be too strict
    }

    @Test
    public void testgetDistance() {
        assertEquals(0.0, osmMap.distance(42.990967, -71.463767, 42.990967, -71.463767));
        // Increase delta if needed
        assertEquals(5.52, osmMap.distance(42.910970, -71.463767, 42.990967, -71.463767), 0.1);
    }
}
