package backend.Misc;

import javafx.collections.FXCollections;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IconSet {
    public List<String> iconList;
    public List<SingleIconPair> pairedIcons = new ArrayList<SingleIconPair>();
    public int nbOfAvailableCategories;

    public IconSet(List<String> iconList, List<String> availableCategories) {
        /**
        * IconSet is meant for storing list of paired categories and icon names
        * it can return an icon name for a given category or set one.
         */
        this.iconList = iconList;

        nbOfAvailableCategories = availableCategories.size();
        for(int i = 0; i < nbOfAvailableCategories; i++) {
            this.pairedIcons.add(i, new SingleIconPair(availableCategories.get(i),""));
        }
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
            //iconSet.setIconForCategoryIndex(i, iconCol.getCellObservableValue(i).getValue());
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

    private List<String> getStringListFromCsv(BufferedReader reader) {
        final List<String> lineList = new ArrayList<String>();

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

    private List<SingleIconPair> getCategoriesFromCsvList(List<String> lineList){
        List<SingleIconPair> tempPairedIcons = new ArrayList<SingleIconPair>();

        for(int i = 0; i < lineList.size(); i++) {
            String line = lineList.get(i);
            tempPairedIcons.add(i, new SingleIconPair(getCategoryFromCsvLine(line), getIconFromCsvLine(line)));
        }

        return tempPairedIcons;
    }

    public void validateIconSetPresetFile(String path){
        //todo: implement
    }

    public void readIconSetPresetFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        cleanupIconSetData();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

        ArrayList<String> lineList = (ArrayList<String>) getStringListFromCsv(reader);
        nbOfAvailableCategories = lineList.size();
        pairedIcons = getCategoriesFromCsvList(lineList);
    }


    /*todo we currently could access chooseIconForCategory from code but it should use
    either GUI or a preset file. it should be implemented
     */
}
