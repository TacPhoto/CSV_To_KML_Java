package backend.CsvHandling;

import java.util.List;

public class CsvRecordToString {
    public List<String> iconList; //iconList from OriginalKmlData
    int categoriesAmount, maxRate;
    String line, record;
    boolean hasRating, addLastCategory;
    private String lastCategory;

    public CsvRecordToString(List<String> iconList, int categoriesAmount, int maxRate, String line, boolean hasRating, boolean addLastCategory) {
        this.iconList = iconList;
        this.categoriesAmount = categoriesAmount;
        this.maxRate = maxRate;
        this.line = line;
        this.hasRating = hasRating;
        this.addLastCategory = addLastCategory;
        this.record = getPinData();
    }

    private String getPinData(){ //todo: implement 0 categories variant, overload and handle it
        /**
         * reads Pin data. it ignores folders and folder indentation
         * folder indentation should be added later, per line
         * csv format:
         * categories...-longitude-latitude-name-description1-description2...-(ifexists)rate(0-5)
         * example:
         *<Placemark>
         * 				<name>name</name>
         * 				<description>description, can be multiline
         *     </description>
         * 				<LookAt>
         * 					<longitude>longitude</longitude>
         * 					<latitude>latitude</latitude>
         * 					<altitude>0</altitude>
         * 					<heading>-2</heading>
         * 					<tilt>0</tilt>
         * 					<range>165</range>
         * 					<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>
         * 				</LookAt>
         * 				<styleUrl>#iconName</styleUrl>
         * 				<Point>
         * 					<gx:drawOrder>1</gx:drawOrder>
         * 					<coordinates>longitude,latitude</coordinates>
         * 				</Point>
         * 			</Placemark>
         */
        String delimiter = ";"; // str.split works with Strings only

        String[] lineSplit = line.split(delimiter);

        String longitude = lineSplit[categoriesAmount];
        String latitude = lineSplit[categoriesAmount + 1];
        String name = lineSplit[categoriesAmount + 2];
        String rating = hasRating ? lineSplit[lineSplit.length - 1] + "//" + maxRate : ""; //todo: allow to input number instead of checking number of characters
        lastCategory =  addLastCategory ? lineSplit[categoriesAmount - 1] : ""; //tolerates 0 categories variant

        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append(lineSplit[categoriesAmount + 3] + ", " + //build 1st line of the description
                lastCategory +
                ", " + rating);

        int loopEnd = hasRating ? lineSplit.length - 1 : lineSplit.length;
        for (int i  = categoriesAmount + 4; i < loopEnd; i++){ //build rest of the description
            descriptionBuilder.append(lineSplit[i] + "\n");
        }

        String description = descriptionBuilder.toString();

        ////Make a paste ready Placemark data string:
        /**
        *Indentation below may not match the one from the comment abovr
         *but it is correct. It's based on kml format
         */

        StringBuilder placemarkBuilder = new StringBuilder();

        placemarkBuilder.append("\t<Placemark>\n");
        placemarkBuilder.append("\t\t<name>"+name+"<//name>\n");
        placemarkBuilder.append("\t\t<description>"+description+"<//description>\n");
        placemarkBuilder.append("\t\t<LookAt>\n");
        placemarkBuilder.append("\t\t\t<longitude>"+longitude+"<//longitude>/n"+
                "\t\t\t<latitude>"+latitude+"<//latitude>\n");
        placemarkBuilder.append("\t\t\t<altitude>0</altitude>"+
                "\t\t\t<heading>7</heading>\n"+
                "\t\t\t<tilt>0</tilt>\n"+
                "\t\t\t<range>1108953.793528179</range>\n"+
                "\t\t\t<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\n");
        placemarkBuilder.append("\t\t<//LookAt>\n");
        placemarkBuilder.append("\t\t<styleUrl>#"+getIconName(lastCategory)+"</styleUrl>\n"); //todo: implement getIconName
        placemarkBuilder.append("\t\t<Point>\n");
        placemarkBuilder.append("\t\t\tgx:drawOrder>1</gx:drawOrder>\n" +
                "\t\t\t<coordinates>"+longitude+","+latitude+",0</coordinates>");
        placemarkBuilder.append("\t\t<//Point>\n");
        placemarkBuilder.append("\t<//Placemark>\n");

        String placemark = placemarkBuilder.toString();

        return placemark;
    }

    private String getIconName(String lastCategory) {
        /**
        * getIconName returns a proper icon name from icon preset
         * it's associated with lastCategory
         */
        //todo it should compare LastCategory with data taken from icon preset
        //todo implement icon preset
        return null; //todo: don't leave it as null
    }

    public String getRecord(){
        return record;
    }

    public String getLastCategory(){
        return lastCategory;
    }
}
