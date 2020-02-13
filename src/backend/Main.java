package backend;

import backend.CsvHandling.CsvReader;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        final Logger LOGGER = Logger.getLogger(Main.class.getName());
        LOGGER.setLevel(Level.INFO);

        CsvReader csvReader = new CsvReader("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\short_csv.csv");

        System.out.print(csvReader.getSortedCsvReadyString());

    }
}
