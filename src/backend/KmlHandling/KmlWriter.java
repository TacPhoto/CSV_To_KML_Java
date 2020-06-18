package backend.KmlHandling;

import backend.CsvHandling.CsvRecordToString;
import backend.CsvHandling.CsvRecordToStringInitData;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class KmlWriter {
    public List<String> lineList; //each line has data used to write a single record. First line is a csv header
    public String kmlHeader;
    public String outputPath;
    public int categoriesAmount;
    public int foldersAmount;     //categoriesAmount - 1 because last category is not used to make folders but to assign icon
    private int currentIndent;    //stores current indentation level. It should not ba affected by base indent in a single record.
                                  //It should be only affected by Folder indentation level;
    private CsvRecordToStringInitData csvRecordToStringInitData;
    private CsvRecordToString csvRecordToString;

    public KmlWriter(List<String> lineList, String kmlHeader, int categoriesAmount, String outputPath, CsvRecordToStringInitData csvRecordToStringInitData) {
        this.lineList = lineList;
        this.kmlHeader = kmlHeader;
        this.outputPath = outputPath;
        this.categoriesAmount = categoriesAmount;
        this.foldersAmount = categoriesAmount - 1;
        this.currentIndent = 0;
        this.csvRecordToStringInitData = csvRecordToStringInitData;
        this.csvRecordToString = new CsvRecordToString(csvRecordToStringInitData);
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

    @NotNull
    private String makeIndent(int indentsNum){
        //todo: when functionality is working, it should no longer create a string but directly write into a file
        StringBuilder indent = new StringBuilder("");

        for(int i = 0; i < indentsNum; i++)
            indent.append("\t");

        return indent.toString();
    }

    @NotNull
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

    @NotNull
    private String makeFolder(String folderName){
        return makeIndent(currentIndent++) + "<Folder>\n"
                + makeIndent(currentIndent) + "<name>" + folderName + "</name>\n";
    }

    private String closeFolder(){
        return makeIndent(--currentIndent) + "</Folder>\n";
    }

    @NotNull
    private String recordWithLine(String record){
        /**
         * Returns a single data record with proper indentation
         */
        /*
        We can add indentation line by line using various ways but as far as I know,
        using Reader is faster then using .split() or a Scanner
         */
        StringBuilder finalRecord = new StringBuilder("");

        try (BufferedReader reader = new BufferedReader(new StringReader(record))) {
            String currentLine = reader.readLine();

            while(currentLine != null) {
                finalRecord.append(makeIndent(currentIndent));
                finalRecord.append(currentLine);
                finalRecord.append("\n");

                currentLine = reader.readLine();
            }
        } catch (IOException e) { //should never happen
            System.out.println("recordWithLine has encountered IO error\n" +
                    "check if input string is valid and passed correctly");
            e.printStackTrace();
        }
        return finalRecord.toString();
    }

    @NotNull
    private String getFolderNameFromPathAt(String path, int index){
        /**
         * Returns specific folder name from a path basing on its index
         * in hierarchy
         * Can be used to extract text between X semicolon and X+1 semicolon
         */

        int counter = 0;
        StringBuilder folderName = new StringBuilder("");

        for(int i = 0; i < path.length(); i++) {
            char character = path.charAt(i);
            if (character == ';') {
                counter++;
                if (counter == index)
                    break;
            }
            if (counter == index - 1 && character != ';')
                folderName.append(character);
        }

        return folderName.toString();
    }

    private int getNumberOfDifferencesBetweenPaths(String firstPath, String secondPath){
        /**
        * Returns number of different folders in Paths. It can be used for folder creation
         * and closing.
         * It should work fine because the program uses already sorted database and differences
         * go without huge variation.
         */
        int differencesNum = 0;

        if(firstPath.equals(secondPath))
            return 0;

        for(int i = 0; i < foldersAmount; i++)
            if( getFolderNameFromPathAt(firstPath, i).equals(getFolderNameFromPathAt(secondPath, i)) )
                differencesNum++;

        return differencesNum;
    }

    private String[] getDifferencesBetweenPaths(String firstPath, String secondPath){
        /**
         * Returns String[] which contains folder names from secondPath which don't match firstPath
         */
        int nbOfDifferences = getNumberOfDifferencesBetweenPaths(firstPath, secondPath);
        String[] differencesBetweenPaths = new String[nbOfDifferences];

        int counter = 0;
        for(int i = 0; i < foldersAmount; i++)
            if( !getFolderNameFromPathAt(firstPath, i).equals(getFolderNameFromPathAt(secondPath, i)) ) {
                differencesBetweenPaths[counter] = getFolderNameFromPathAt(secondPath, i);
                counter++;
            }
        return differencesBetweenPaths;
    }

    private String writeAllDataRecords(){
        /**
         * Saves all data records to file in a form of CSV file that lacks only a header
         */
        //todo: current implementation returns a String. It should write to the file
        StringBuilder result = new StringBuilder();

        //open first folder
        String lineSplit[] = lineList.get(1).split(";");
        for(int i = 0; i < foldersAmount; i++){
            result.append(makeFolder(lineSplit[i]));
        }

        //write all records
        for(int i = 1; i < lineList.size(); i++){ //starting from 1 because first line is a csv header
            boolean skipLast = (i == lineList.size()-1);

            String currentLine = getPathForLine(
                    lineList.get(i)
                    .replaceAll("\n","")
                    .replaceAll("\t","")
                    .replaceAll(" ","")
            );
            String nextLine = null;
            if(!skipLast) {
                nextLine = getPathForLine(recordWithLine(
                        lineList.get(i + 1))
                        .replaceAll("\n","")
                        .replaceAll("\t","")
                        .replaceAll(" ","")
                );
            }

            //write a record
            csvRecordToString.setLine(lineList.get(i));
            result.append(csvRecordToString.getRecord());

            //check if needs folder change
            int NumberOfDifferencesBetweenPaths = skipLast ? 0 : getNumberOfDifferencesBetweenPaths(currentLine, nextLine);
            if(NumberOfDifferencesBetweenPaths != 0){
                //close folder
                for(int j = 0; j < NumberOfDifferencesBetweenPaths; j++){
                    result.append(closeFolder());
                }
                //open folder
                String[] differences = getDifferencesBetweenPaths(currentLine, nextLine);
                for(int j = differences.length - 1; j > 0; j--){
                    result.append(makeFolder(differences[j]));
                }

            }
        }

        //close last folder
        for(int i = 0; i < foldersAmount; i++){
            result.append(closeFolder());
        }


        return result.toString();
    }

    public void debugTest(){
        /**
         * Debug method used for testing class during development
         * This method should not be used on production
         * Beware of overriding class variables by the method!!!
         */
        /*
        String line1 = "geo;cities;pl;warsaw;pl";
        String line2 = "geo;cities;rus;moscow;rus";

        System.out.println("PRINT DEBUG PATH");
        foldersAmount = 3;
        System.out.println(getPathForLine(line1));

        System.out.print("\nThird element from path: ");
        System.out.print(getFolderNameFromPathAt(line1, 3));
        System.out.print("\nSecond element from path: ");
        System.out.print(getFolderNameFromPathAt(line1, 2));
        System.out.print("\nFirst element from path: ");
        System.out.print(getFolderNameFromPathAt(line1, 1));

        String testRecord = "\t<Placemark>place\n\t\t<second>test";
        System.out.println("\n\nFinal record:");
        System.out.print(recordWithLine(testRecord));

        System.out.println("\n\nPath 1: " + line1 );
        System.out.println("Path 2: " + line2);
        System.out.println("Number of different folders: " +
                getNumberOfDifferencesBetweenPaths(line1, line2));
        */

        //System.out.println(kmlHeader);
        //System.out.println();

        System.out.println(writeAllDataRecords());

    }
}

//TODO: when everything is ready, switch from keeping data int Strings to writing them directly to file (if possible)