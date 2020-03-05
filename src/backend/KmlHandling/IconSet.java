package backend.KmlHandling;

import java.util.ArrayList;
import java.util.List;

public class IconSet {
    public List<String> iconList;
    public List<SingleIconPair> pairedIcons = new ArrayList<SingleIconPair>();

    public IconSet(List<String> iconList, List<String> availableCategories) {
        /**
        * IconSet is meant for storing list of paired categories and icon names
        * it can return an icon name for a given category or set one.
         */
        this.iconList = iconList;

        int nbOfAvailableCategories = availableCategories.size();
        for(int i = 0; i < nbOfAvailableCategories; i++) {
            this.pairedIcons.add(i, new SingleIconPair(availableCategories.get(i),""));
        }
    }

    public void chooseIconForCategory(int categoryIndex, String icon){
        iconList.remove(categoryIndex);
        iconList.add(categoryIndex, icon);
    }

    public String getIconForCategory(String inputCategory){
        for(int i = 0; i < pairedIcons.size(); i++){
            String catFromList = pairedIcons.get(i).getCategory();
            if(inputCategory.equals(catFromList)){
                return catFromList;
            }
        }
        return null;//todo: remember to handle this case. user should get an error prompt
    }

    /*todo we currently could access chooseIconForCategory from code but it should use
    either GUI or a preset file. it should be implemented
     */
}
