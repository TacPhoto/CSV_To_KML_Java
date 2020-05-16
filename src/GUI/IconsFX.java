package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IconsFX {

    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty icon = new SimpleStringProperty();

    public IconsFX(String category, String icon) {
        setCategory(category);
        setIcon(icon);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getIcon() {
        return icon.get();
    }

    public StringProperty iconProperty() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon.set(icon);
    }
}
