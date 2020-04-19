package backend.CsvHandling;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvReader {
    private BufferedReader reader;
    private List<String> lineList = new ArrayList<String>();
    private final static Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

    public CsvReader(String csvPath) throws IOException {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("CSV reader initialization. Path used: " + csvPath);
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "utf-8"));
    }

    private void csvToStringList() throws IOException {
        LOGGER.info("READING CSV FILE LINE BY LINE");
        try {
            String line;
            while((line = reader.readLine()) != null)
                lineList.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("READING CSV FILE LINE BY LINE FINISHED");
    }

    private void sortStringList(){
        /**
         *Returns a given list without changing the position of a first record.
         *Important when the list is made of a csv file with header.
         *DO NOT USE ON LISTS WITHOUT HEADERS
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
       /**
        * getLineList() is public but needs readRawCsv() to be executed first to give us a proper output
        */
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
