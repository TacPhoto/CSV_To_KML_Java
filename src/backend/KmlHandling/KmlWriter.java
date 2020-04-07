package backend.KmlHandling;

import java.util.List;

public class KmlWriter {
    public List<String> lineList; //each line has data used to write a single record. First line is a csv header
    public String kmlHeader;
    public String outputPath;
    public int categoriesAmount;
    public int foldersAmount;  //categoriesAmount - 1 because last category is not used to make folders but to assign icon
    private int currentIndent; //stores current indentation level

    public KmlWriter(List<String> lineList, String kmlHeader, int categoriesAmount, String outputPath) {
        this.lineList = lineList;
        this.kmlHeader = kmlHeader;
        this.outputPath = outputPath;
        this.categoriesAmount = categoriesAmount;
        this.foldersAmount = categoriesAmount - 1;
    }

    /**
     * this writer shall write us an kml file
     * it will begin by pasting header into kml then it should write records one by one
     * it will create proper categories in kml file by comparing current path to the record
     * inside kml file with the next one. If it's blank, it's the end of the file.
     * it will close folder in kml each time it finds a difference in path, starting from the end of path.
     * there should be no bugs because records list is already sorted
     *
     * indents in kml will be written sequentionally by a separate function
     * records will be written with CsvRecordToString
     * folderCheck-indent-CsvRecordToString-folderCheck-indent-Csv... and so on
     */

    private String makeIndent(int indentsNum){
        //todo: when functionality is working, it should no longer create a string but directly write into a file
        StringBuilder indent = new StringBuilder("");

        for(int i = 0; i < indentsNum; i++)
            indent.append("\t");

        return indent.toString();
    }

    private String getPathForLine(String line){
        int lastFolder = 0;
        int counter = 0;

        for(int i = 0; i < line.length(); i++) {
            lastFolder = i;

            if (line.charAt(i) == ';') {
                counter++;
                if( counter == foldersAmount)
                    break;
            }
        }

        String pathForLine = line.substring(0, lastFolder);

        return pathForLine;
    }

    //todo: implement folder creation
    //todo: implement folder closing
    //todo: implement comparing paths and running folder creation/closing operations based on the result
    //todo: implement record writting with proper indentation

/*
    private String createFolder(){ //don't do it for the first record to avoid bugs
        for(int i = foldersAmount - 1; i > 0; i--)
            //compare paths
            //for each difference close folders, add indents
            //for each difference open folders, add indents
    }

 */

    public void debugTest(){
        /**
         * Debug method used for testing class during development
         * This method should not be used on production
         * Beware of overriding class variables by the method!!!
         */
        String line1 = "geo;cities;pl;warsaw;pl";
        String line2 = "geo;cities;rus;moscow;rus";

        System.out.println("PRINT DEBUG PATH");
        foldersAmount = 3;
        System.out.println(getPathForLine(line1));
    }
}

//TODO; when everything is ready, switch from keeping data int Strings to writting them directly to file (if possible)