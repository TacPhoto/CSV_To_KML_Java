package backend.Misc;

import javafx.collections.FXCollections;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IconSet {
    private final static Logger LOGGER = Logger.getLogger(IconSet.class.getName());
    public List<String> iconList;
    public ArrayList<SingleIconPair> pairedIcons = new ArrayList<SingleIconPair>();
    public int nbOfAvailableCategories;

    public IconSet(List<String> iconList, List<String> availableCategories) {
        /**
         * IconSet is meant for storing list of paired categories and icon names
         * it can return an icon name for a given category or set one.
         *
         * It always has to be initialized with proper data because it's used for validating
         * the preset file data before IconSet is overridden with it
         */
        LOGGER.setLevel(Level.INFO);

        this.iconList = iconList;

        nbOfAvailableCategories = availableCategories.size();
        for(int i = 0; i < nbOfAvailableCategories; i++) {
            this.pairedIcons.add(i, new SingleIconPair(availableCategories.get(i),""));
        }

        generateIconSetFromData();
    }

    public void setIconForCategoryIndex(int categoryIndex, String icon){
        pairedIcons.get(categoryIndex).setIcon(icon);
    }

    public String getIconForCategory(String inputCategory){
        for(int i = 0; i < pairedIcons.size(); i++){
            String catFromList = pairedIcons.get(i).getCategory();
            if(inputCategory.equals(catFromList)){
                return pairedIcons.get(i).getIcon();
            }
        }
        return null;//todo: remember to handle this case. user should get an error prompt
    }

    public String getDebugIcon(String inputCategory){
        //only for test purposes. all occurrences
        //of this method in code should be later
        //renamed to getIconForCategory
        return getIconForCategory(inputCategory);
    }

    public boolean isValid(){
        for(int i = 0; i < pairedIcons.size(); i++) {
            if(!pairedIcons.get(i).isValid())
                return false;
        }
        return true;
    }

    public void generateIconSetFromData(){
        for(int i = 0; i < nbOfAvailableCategories; i++){
            setIconForCategoryIndex(i, FXCollections.observableArrayList(iconList).get(i));

        }
    }

    private void cleanupIconSetData(){
        pairedIcons.clear();
    }

    public void saveIconSetPresetFile(String path) throws IOException {//path or FILE object
        final PrintWriter writer = new PrintWriter
                (new BufferedWriter(
                        new OutputStreamWriter
                                (new FileOutputStream(path), "utf-8")
                ));

        for (int i = 0; i < nbOfAvailableCategories; i++) {
            writer.write(pairedIcons.get(i).category + ';' + pairedIcons.get(i).icon);
        }
        writer.close();
    }

    private ArrayList<String> getStringListFromCsv(BufferedReader reader) {
        final ArrayList<String> lineList = new ArrayList<String>();

        try {
            String line;
            while ((line = reader.readLine()) != null)
                lineList.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineList;
    }

    private String getCategoryFromCsvLine(String line){
        return line.split(";")[0];
    }

    private String getIconFromCsvLine(String line){
        return line.split(";")[1];
    }

    private ArrayList<SingleIconPair> getCategoriesFromCsvList(List<String> lineList){
        ArrayList<SingleIconPair> tempPairedIcons = new ArrayList<SingleIconPair>();

        for(int i = 0; i < lineList.size(); i++) {
            String line = lineList.get(i);
            tempPairedIcons.add(i, new SingleIconPair(getCategoryFromCsvLine(line), getIconFromCsvLine(line)));
        }

        return tempPairedIcons;
    }

    public boolean isValidIconSetPresetFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        //todo: apply more dry-coding for reading and validating iconSetPreset files
        // they could avoid reading files for multiple time, single one would be enough
        // maybe create a separate temp object for such data from file and use it for validation
        // and copying data to the actual iconSet?
        LOGGER.info("IconSetPresetFile validation started");

        Path filePath = Paths.get(path);
        List<SingleIconPair> tempPairedIcons = new ArrayList<SingleIconPair>();
        ArrayList<String> lineList;

        if(!Files.exists(filePath)){
            LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail. File does not exist");
            return false;
        }

        if(!Files.isReadable(filePath)){
            LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail. File is not readable");
            return false;
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

        try {
            lineList = getStringListFromCsv(reader);
        }catch(Exception e){
            LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail on reading the file");
            LOGGER.log(Level.WARNING, "Exception log:\n");
            e.printStackTrace();

            return false;
        }

        if(!(nbOfAvailableCategories == lineList.size())) {
            LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail. Number of available categories does not match length of categories in the file");
            return false;
        }

        try{
        tempPairedIcons = getCategoriesFromCsvList(lineList);
        }catch (Exception e){
            LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail on extracting icon and category data from line list form of preset file");
            LOGGER.log(Level.WARNING, "Exception log:\n");
            e.printStackTrace();

            return false;
        }

        {
            boolean isValidCategoryData = false;
            for (int i = 0; i < nbOfAvailableCategories; i++) {
                for (int j = 0; j < pairedIcons.size(); j++) {
                    if (pairedIcons.get(j).category.equals(tempPairedIcons.get(i).category)) {
                        isValidCategoryData = true;
                        break;
                    }
                }
            }
            if (isValidCategoryData == false) {
                LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail, data from preset file does not match categories from csv database");
                return false;
            }
        }

        {
            boolean isValidIconData = false;
            for (int i = 0; i < nbOfAvailableCategories; i++) {
                for (int j = 0; j < pairedIcons.size(); j++) {
                    if (pairedIcons.get(j).icon.equals(tempPairedIcons.get(i).icon)) {
                        isValidIconData = true;
                        break;
                    }
                }
            }
            if (isValidIconData == false) {
                LOGGER.log(Level.WARNING, "IconSetPresetFile validation fail, data from preset file does not match icons from kml map");
                return false;
            }
        }

        LOGGER.info("IconSetPresetFile validation finished Passed");
        return true;
    }

    public void readIconSetPresetFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        if(!isValidIconSetPresetFile(path)){
            return;
        }

        cleanupIconSetData();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

        ArrayList<String> lineList = getStringListFromCsv(reader);
        nbOfAvailableCategories = lineList.size();
        pairedIcons = getCategoriesFromCsvList(lineList);
    }
    
    //todo we currently could access chooseIconForCategory from code but it should use
    // either GUI or a preset file. it should be implemented
}