package backend.KmlHandling;

import backend.CsvHandling.CsvReader;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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


    public OriginalKmlData(@NotNull String kmlPath) throws Exception {
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
                str.append(line).append('\n');
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

    public void removeIconSetPresetData() throws IOException {
        /**
         *If KML Header was extracted from IconSet preset file then it likely contains some additional data
         * this method will clean it up
         */
        StringBuilder finalStringBuilder= new StringBuilder("");

        String[] lines = this.IconsHeader.split("\n");

        for(String line : lines){
            if((line.contains("<") && line.contains(">") && !line.contains(";"))){
                finalStringBuilder.append(line).append("\n");
            }
        }


        this.IconsHeader = finalStringBuilder.toString();
    }

    private void listCleanup() {
        iconList = iconList.stream().distinct().collect(Collectors.toList());
    }

    public String getIconsHeader(String mapName) {
        /** Returns KML file header with icon data with original map name
        * changed to given String
         */
        Pattern pattern = Pattern.compile("<Document>\\n\\t<name>(.+?)</name>");

        return pattern.matcher(getIconsHeader())
                .replaceFirst("<Document>\\n\\t<name>" +
                        mapName+
                        "</name>\""
                );
    }

    public String getIconsHeader() {
        /** Returns unchanged KML file header with icon data.
        *
         */
        LOGGER.info("getIconHeader called");
        return IconsHeader;
    }

    public List<String> getIconList() {
        LOGGER.info("getIconList called");
        return iconList;
    }

}
