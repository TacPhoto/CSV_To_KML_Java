package backend.Misc;

public class SingleIconPair {
    public String category;
    public String icon;

    public SingleIconPair(String category, String icon) {
        this.category = category;
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isValid(){
        return ( icon != null && category != null);
    }
}
