package backend.KmlHandling;

import backend.CsvHandling.CsvReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OriginalKmlData {
    private List<String> iconList;
    private String IconsHeader;
    private final static Logger LOGGER = Logger.getLogger(CsvReader.class.getName());


    public OriginalKmlData(String kmlPath) throws Exception {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Icon list constructor initialized");

        iconList = new ArrayList<String>();
        this.IconsHeader = readIconsHeader(kmlPath);
        LOGGER.info("Header string is ready");
        parseIconId();

        LOGGER.info("Icon list construction finished");

    }

    private String readIconsHeader(String path) throws IOException {
        /**
         * Reads icons header from kml file. It will be used both
         * for creating icon id list for placemark data
         * and for creating a final kml header.
         */
        LOGGER.info("READING KML HEADER. Path used: " + path);

        StringBuilder str = new StringBuilder();
        BufferedReader reader = Files.newBufferedReader(Paths.get(path));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("\t<Folder>"))
                    break;
                str.append(line + '\n');
            }
        } catch (IOException e) {
            LOGGER.info("!!!KML HEADER READER CAUGHT AN EXCEPTION!!!");
            e.printStackTrace();
        }

        LOGGER.info("KML READING FINISHED, now Reader will return a string.");
        return str.toString();
    }

    private void parseIconId() throws Exception {
        Pattern pattern = Pattern.compile("(?<=StyleMap id=\")\\w+");
        LOGGER.info("Regex pattern for icon list initialization");
        Matcher matcher = pattern.matcher(IconsHeader);

        LOGGER.info("Searching for icon list tags");
        while (matcher.find()) {
            iconList.add(matcher.group());
        }

        if (iconList.toString().equals("[]"))
            throw new Exception("No icon data found in header, KML file is invalid");

        listCleanup();
    }

    private void listCleanup() {
        iconList = iconList.stream().distinct().collect(Collectors.toList());
    }

    public String getIconsHeader() {
        LOGGER.info("getIconHeader called");
        return IconsHeader;
    }

    public List<String> getIconList() {
        LOGGER.info("getIconList called");
        return iconList;
    }

}
