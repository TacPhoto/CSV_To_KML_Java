package backend.UnitTests;

import backend.KmlHandling.OriginalKmlData;

import static org.junit.jupiter.api.Assertions.*;

class OriginalKmlDataTest {
    private String path = new String("example_test_files\\ShortExample.kml");
    private String pathWithDoubles = new String("example_test_files\\ShortExampleWithDoubles.kml");
    OriginalKmlData originalKmlData = new OriginalKmlData(path);
    OriginalKmlData originalKmlDataWithDoubles = new OriginalKmlData(pathWithDoubles);

    String expectedList = new String("[msn_electronics, m_ylw, msn_dollar, msn_homegardenbusiness, msn_coffee, msn_info_circle]");
    String expectedHeader = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "<Document>\n" +
            "\t<name>ShortExample.kml</name>\n" +
            "\t<StyleMap id=\"msn_electronics\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#sn_electronics</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#sh_electronics</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n" +
            "\t<Style id=\"sh_electronics\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.4</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/electronics.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sh_coffee\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.4</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/coffee.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sn_homegardenbusiness\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.2</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sh_homegardenbusiness\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.4</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/homegardenbusiness.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sn_electronics\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.2</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/electronics.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<StyleMap id=\"m_ylw-pushpin\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#s_ylw-pushpin</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#s_ylw-pushpin_hl</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n" +
            "\t<StyleMap id=\"msn_dollar\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#sn_dollar</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#sh_dollar</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n" +
            "\t<Style id=\"sn_coffee\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.2</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/coffee.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<StyleMap id=\"msn_homegardenbusiness\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#sn_homegardenbusiness</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#sh_homegardenbusiness</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n" +
            "\t<Style id=\"sh_dollar\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.4</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/dollar.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<StyleMap id=\"msn_coffee\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#sn_coffee</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#sh_coffee</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n" +
            "\t<Style id=\"s_ylw-pushpin\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.1</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sh_info_circle\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.4</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/info_circle.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sn_dollar\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.2</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/dollar.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"s_ylw-pushpin_hl\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.3</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t</Style>\n" +
            "\t<Style id=\"sn_info_circle\">\n" +
            "\t\t<IconStyle>\n" +
            "\t\t\t<scale>1.2</scale>\n" +
            "\t\t\t<Icon>\n" +
            "\t\t\t\t<href>http://maps.google.com/mapfiles/kml/shapes/info_circle.png</href>\n" +
            "\t\t\t</Icon>\n" +
            "\t\t\t<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n" +
            "\t\t</IconStyle>\n" +
            "\t\t<BalloonStyle>\n" +
            "\t\t</BalloonStyle>\n" +
            "\t\t<ListStyle>\n" +
            "\t\t</ListStyle>\n" +
            "\t</Style>\n" +
            "\t<StyleMap id=\"msn_info_circle\">\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>normal</key>\n" +
            "\t\t\t<styleUrl>#sn_info_circle</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t\t<Pair>\n" +
            "\t\t\t<key>highlight</key>\n" +
            "\t\t\t<styleUrl>#sh_info_circle</styleUrl>\n" +
            "\t\t</Pair>\n" +
            "\t</StyleMap>\n");

    OriginalKmlDataTest() throws Exception {
    }

    @org.junit.jupiter.api.Test
    void OriginalKmlDataTest(){
        assertNotEquals(null, originalKmlData.getIconsHeader());
        assertNotEquals(null, originalKmlData.getIconList());
    }

    @org.junit.jupiter.api.Test
    void getIconsHeader() throws Exception {
        assertEquals(expectedHeader, originalKmlData.getIconsHeader());
    }

    @org.junit.jupiter.api.Test
    void parseIconIdTest() {
        assertEquals(expectedList, originalKmlData.getIconList().toString());
    }

    @org.junit.jupiter.api.Test
     void listCleanup() {
        assertEquals(expectedList, originalKmlDataWithDoubles.getIconList().toString(), "listCleanup does not get rid of doubles in a proper way");
    }

}