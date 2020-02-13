package backend.CsvHandling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader; //this could never happen TODO: implement safety mechanism
import java.io.IOException;//this could never happen^
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvReader {
    private String csvPath;
    private Reader reader;
    private CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';');
    private List<String> lineList = new ArrayList<String>();

    private final static Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

    public CsvReader(String csvPath) throws IOException {
        this.csvPath = csvPath;
        this.reader = Files.newBufferedReader(Paths.get(csvPath));
        LOGGER.setLevel(Level.INFO);
    }

    private void csvToStringList() throws IOException {
        LOGGER.info("READING CSV FILE LINE BY LINE");
        try {
            int i = 0;
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(reader);
            for (CSVRecord record : records)
                lineList.add(record.get(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("READING CSV FILE LINE BY LINE FINISHED");
    }

    private void sortStringList(){
        /**
         Returns a given list without changing the position of a first record.
         Important when the list is made of a csv file with header.
         DO NOT USE ON LISTS WITHOUT HEADERS
         */
        String temp = lineList.get(0);
        lineList.remove(0);
        Collections.sort(lineList);
        lineList.add(0, temp);
    }

    private void readRawCsv() throws IOException {
        csvToStringList();
        sortStringList();
    }

    public List<String> getLineList() {
        return lineList;
    }

    public String getSortedCsvReadyString() throws IOException {
         /**
         * getSortedCsvReadyString() reads a csv file returns it as a sorted string.
         * If csv file begins with category/folder/subfolder columns then
         * it gets sorted by folders similarly to sorting it as a folder tree
         * but in an easier way.
         * It is important for easier folder creation inside KML file
         */
        readRawCsv();

        LOGGER.info("CREATING A SORTED STRING FOR A NEW CSV");

        String listString = "";

        for (String line : lineList)
            listString += line + "\n";

        LOGGER.info("SORTED STRING FOR A NEW CSV READY");

        return listString;
    }
}
