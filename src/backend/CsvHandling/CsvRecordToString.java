package backend.CsvHandling;

import backend.Misc.IconSet;

import java.util.List;

public class CsvRecordToString {
    public List<String> iconList; //iconList from OriginalKmlData
    int categoriesAmount, maxRate;
    String line, record;
    public boolean hasRating, addLastCategory;
    private String lastCategory;
    private IconSet iconSet;

    public CsvRecordToString(List<String> iconList, int categoriesAmount, int maxRate, String line, boolean hasRating, boolean addLastCategory, IconSet iconSet) {
        this.iconSet = iconSet;
        this.iconList = iconList;
        this.categoriesAmount = categoriesAmount;
        this.maxRate = maxRate;
        this.line = line;
        this.hasRating = hasRating;
        this.addLastCategory = addLastCategory;
        this.record = getPinData();
    }

    public CsvRecordToString(CsvRecordToStringInitData initData){
        this.iconSet = initData.iconSet;
        this.iconList = initData.iconList;
        this.categoriesAmount = initData.categoriesAmount;
        this.maxRate = initData.maxRate;
        this.line = initData.line;
        this.hasRating = initData.hasRating;
        this.addLastCategory = initData.addLastCategory;
        this.record = getPinData();
    }

    public void setLine(String line) {
        this.line = line;
    }

    private String getPinData(){
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

        trimStringArray(lineSplit);

        String latitude = lineSplit[categoriesAmount];
        String longitude = lineSplit[categoriesAmount + 1];
        String name = lineSplit[categoriesAmount + 2];
        String rating = hasRating ? lineSplit[lineSplit.length - 1] + "//" + maxRate : ""; //todo: allow to input number instead of checking number of characters
/*
        if(categoriesAmount < 2)
            lastCategory = categoriesAmount == 0 ? "" : lineSplit[categoriesAmount - 1];
        else
            lastCategory =  addLastCategory ? lineSplit[categoriesAmount - 1] : lineSplit[categoriesAmount - 2];
*/
        if (categoriesAmount > 0)
            lastCategory =  addLastCategory ? lineSplit[categoriesAmount - 1] : ""; //tolerates 0 categories variant
                if(categoriesAmount > 2 && !addLastCategory)
                    lastCategory = lineSplit[categoriesAmount - 2];
        else
            lastCategory = "";

        StringBuilder descriptionBuilder = new StringBuilder();

        descriptionBuilder.append(lineSplit[categoriesAmount + 3]).append(", ").append( //build 1st line of the description
                lastCategory).append(", ").append(rating);

        int loopEnd = hasRating ? lineSplit.length - 1 : lineSplit.length;
        for (int i  = categoriesAmount + 4; i < loopEnd; i++){ //build rest of the description
            descriptionBuilder.append(lineSplit[i]).append("\n");
        }

        String description = descriptionBuilder.toString();

        ////Make a paste ready Placemark data string:
        /**
        *Indentation below may not match the one from the comment above
         *but it is correct. It's based on kml format
         */

        StringBuilder placemarkBuilder = new StringBuilder();

        placemarkBuilder.append("\t<Placemark>\n");
        placemarkBuilder.append("\t\t<name>").append(name).append("</name>\n");
        placemarkBuilder.append("\t\t<description>").append(description).append("</description>\n");
        placemarkBuilder.append("\t\t<LookAt>\n");
        placemarkBuilder.append("\t\t\t<longitude>").append(longitude).append("</longitude>\n").append("\t\t\t<latitude>").append(latitude).append("</latitude>\n");
        placemarkBuilder.append("\t\t\t<altitude>0</altitude>\n"+
                "\t\t\t<heading>7</heading>\n"+
                "\t\t\t<tilt>0</tilt>\n"+
                "\t\t\t<range>1108953.793528179</range>\n"+
                "\t\t\t<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\n");
        placemarkBuilder.append("\t\t</LookAt>\n");
        placemarkBuilder.append("\t\t<styleUrl>#").append(getIconName(lastCategory)).append("</styleUrl>\n");
        placemarkBuilder.append("\t\t<Point>\n");
        placemarkBuilder.append("\t\t\t<gx:drawOrder>1</gx:drawOrder>\n" + "\t\t\t<coordinates>").append(longitude).append(",").append(latitude).append(",0</coordinates>\n");
        placemarkBuilder.append("\t\t</Point>\n");
        placemarkBuilder.append("\t</Placemark>\n");

        String placemark = placemarkBuilder.toString();

        return placemark;
    }

    private void trimStringArray(String[] lineSplit) {
        for(int i = 0; i < lineSplit.length; i++){
            lineSplit[i] = lineSplit[i].trim();
        }
    }

    private String getIconName(String lastCategory) {
        /**
        * getIconName returns a proper icon name from icon preset
         * it's associated with lastCategory
         */
        return this.iconSet.getIconForCategory(lastCategory);
    }

    public String getRecord(){
        return getPinData();
    }

    public String getLastCategory(){
        return lastCategory;
    }
}
