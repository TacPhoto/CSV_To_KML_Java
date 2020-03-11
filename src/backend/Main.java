package backend;

import backend.CsvHandling.*;
import backend.KmlHandling.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        final Logger LOGGER = Logger.getLogger(Main.class.getName());
        LOGGER.setLevel(Level.INFO);

        CsvReader csvReader = new CsvReader("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\short_csv.csv");
        System.out.print(csvReader.getSortedCsvReadyString());
        csvReader.getSortedCsvReadyString();
        System.out.println(csvReader.getLineList());


        OriginalKmlData originalKmlData = new OriginalKmlData("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\ShortExample.kml");
        System.out.println(originalKmlData.getIconList());



    }
}
