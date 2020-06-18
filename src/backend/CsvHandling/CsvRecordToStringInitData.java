package backend.CsvHandling;

import backend.Misc.IconSet;

import java.util.List;

public class CsvRecordToStringInitData {
    public List<String> iconList; //iconList from OriginalKmlData
    int categoriesAmount, maxRate;
    public String line;
    public boolean hasRating, addLastCategory;
    public IconSet iconSet;

    public CsvRecordToStringInitData(List<String> iconList, int categoriesAmount, int maxRate, String line, boolean hasRating, boolean addLastCategory, IconSet iconSet) {
        this.iconSet = iconSet;
        this.iconList = iconList;
        this.categoriesAmount = categoriesAmount;
        this.maxRate = maxRate;
        this.line = line;
        this.hasRating = hasRating;
        this.addLastCategory = addLastCategory;
    }
}
