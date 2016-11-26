package cs311.hw8;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Playground {
    public static void main(String[] args) {
        OSMMap osmMap = new OSMMap();
        osmMap.LoadMap(osmMap.LOCAL_FILE);

        String sbux1 = "972593630";
        String cafeDiem = "972627391";
        String groveCafe = "972633979";
        String theCafe = "1292348200";
        String bergiesEspresso = "1868149731";
        String stompingGrounds = "1933284322";
        String cafeBeaudelaire = "2400994656";
        String dunkinDonuts = "2400994660";
        String arcadia = "2523851119";
        String cafeMood = "2531586088";
        String vinylCafe = "3154706612";
        String caribouCoffee = "3641409028";
        String sbux2 = "4162641133";
        String sbux3 = "972593630";


        List<String> ids = Arrays.asList(sbux1, cafeDiem, groveCafe, theCafe, bergiesEspresso, stompingGrounds, cafeBeaudelaire, dunkinDonuts, arcadia, cafeMood, vinylCafe, caribouCoffee, sbux2, sbux3);
        List<String> idsWithNeighbors = new ArrayList<>();
        ids.forEach(id -> {
            String closestIdWithNeighbors = osmMap.closestVertexIDwithNeighbors(id);
            idsWithNeighbors.add(closestIdWithNeighbors);
        });

        idsWithNeighbors.forEach(location -> System.out.println(osmMap.map.getVertexData(location).latitude + ", " + osmMap.map.getVertexData(location).longitude));

        List<String> route = osmMap.StreetRoute(idsWithNeighbors);

        System.out.println();

        for (String local : route) {
            System.out.println(local);
        }
    }


}
