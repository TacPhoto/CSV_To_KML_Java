package backend;

import backend.CsvHandling.*;
import backend.KmlHandling.*;
import backend.Misc.IconSet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws Exception {
        final Logger LOGGER = Logger.getLogger(Main.class.getName());
        LOGGER.setLevel(Level.INFO);

        List<String> lineList = new ArrayList<String>();
        String kmlHeader;
        String outputPath = null;
        int categoriesAmount = 3;

        CsvReader csvReader = new CsvReader("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\short_valid_csv.csv");
        String sortedCsv = csvReader.getSortedCsvReadyString(); //necessary for getLineList(), otherwise it will return nothing
        lineList = csvReader.getLineList();

        OriginalKmlData originalKmlData = new OriginalKmlData("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\ShortExample.kml");
        kmlHeader = originalKmlData.getIconsHeader();
        List<String> iconList = originalKmlData.getIconList();

        //test
        List<String> lastCategories = new ArrayList<String>();
        LastCategoryScanner lastCategoryScanner = new LastCategoryScanner(sortedCsv, 3, true, true);
        lastCategories = lastCategoryScanner.getLastCatList();

        IconSet iconSet = new IconSet("example_test_files/testPreset.txt", lastCategories);
        //IconSet iconSet = new IconSet(iconList, lastCategories);

        CsvRecordToStringInitData csvRecordToStringInitData = new CsvRecordToStringInitData(iconList, 3,5, lineList.get(1), false, true, iconSet);

        KmlWriter kmlWriter = new KmlWriter(lineList, kmlHeader, categoriesAmount, "example_test_files/testKML.kml", csvRecordToStringInitData);

        kmlWriter.debugTest();
    }
}
