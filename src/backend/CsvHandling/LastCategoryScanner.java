package backend.CsvHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LastCategoryScanner {
    /**
     * LastCategoryScanner will go through csv file
     * will find the last available category in csv and save it into a list
     * this list can be used to create an Icon Preset later
     */
    public List<String> lastCatList = new ArrayList<String>(); //todo: it can be empty. such case has to be secured
    private String csv; //CsvReader.GetSortedCsvReadyString() should be passed here
    private int categoriesAmount;
    private boolean addLastCategory, hasHeader;

    public LastCategoryScanner(String csv, int categoriesAmount, boolean addLastCategory, boolean hasHeader) throws IOException {
        this.csv = csv;
        this.categoriesAmount = categoriesAmount;
        this.addLastCategory = addLastCategory;
        this.hasHeader = hasHeader;

        getLastCategories();
    }

    private String getPossibleCategoryFromLine(String line){
        String delimiter = ";"; // str.split works with Strings only
        String[] lineSplit = line.split(delimiter);

        String lastCategory =  addLastCategory ? lineSplit[categoriesAmount - 1] : ""; //tolerates 0 categories variant

        return lastCategory;
    }

    private void getLastCategories() throws IOException {
        BufferedReader bufReader = new BufferedReader(new StringReader(csv));

        //parse all LastCategories into a list
        int i = 0;
        String line=null;
        while( (line = bufReader.readLine()) != null )
        {
        if(!(hasHeader && (i == 0))) //skip header if there is one
            this.lastCatList.add(getPossibleCategoryFromLine(line));
        ++i;
        }

        //cleanup the list
        lastCatList = lastCatList.stream().distinct().collect(Collectors.toList());

        //hint for Garbage Collector to cleanup the heap
        //after list cleanup and messing with csv and kml files
        System.gc();
    }

    public List<String> getLastCatList(){
        return lastCatList;
    }
}
