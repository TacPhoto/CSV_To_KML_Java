package backend;

import backend.CsvHandling.*;
import backend.KmlHandling.*;

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
        int categoriesAmount = 0;

        CsvReader csvReader = new CsvReader("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\short_valid_csv.csv");
        //System.out.print(csvReader.getSortedCsvReadyString());
        csvReader.getSortedCsvReadyString(); //necessary for getLineList(), otherwise it will return nothing
        lineList = csvReader.getLineList();
        //System.out.println(lineList);

        OriginalKmlData originalKmlData = new OriginalKmlData("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\ShortExample.kml");
        //System.out.println(originalKmlData.getIconList());
        kmlHeader = originalKmlData.getIconsHeader();
        List<String> iconList = originalKmlData.getIconList();

        //test
        List<String> lastCategories = new ArrayList<String>();
        lastCategories.add("first");
        lastCategories.add("second");
        lastCategories.add("third");

        IconSet iconSet = new IconSet(iconList, lastCategories);

        CsvRecordToString recordWriter = new CsvRecordToString(iconList, 3,5, lineList.get(1), false, true, iconSet);
        System.out.println(recordWriter.getRecord());

       KmlWriter kmlWriter = new KmlWriter(lineList, kmlHeader, categoriesAmount, outputPath);

       //kmlWriter.debugTest();
    }
}
