package GUI;

import backend.CsvHandling.CsvReader;
import backend.CsvHandling.LastCategoryScanner;
import backend.Misc.IconSet;
import backend.KmlHandling.OriginalKmlData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.platform.commons.util.StringUtils;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindowController {
    @FXML
    public MenuBar Menu;
    @FXML
    public MenuItem menuOpenTerminalChoice;
    @FXML
    public MenuItem menuCloseChoice;
    @FXML
    public MenuItem menuAboutChoice;
    @FXML
    public MenuItem menuLangChoice;
    @FXML
    public TextField csvPathTextField;
    @FXML
    public TextField iconPresetPathTextField;
    @FXML
    public ComboBox presetTypeSelectorCombo;
    @FXML
    public Button presetSelectorButton;
    @FXML
    public Button csvSelectorButton;
    @FXML
    public TextField numCategoriesTextField;
    @FXML
    public Button loadDataButton;
    @FXML
    public Button processSaveMapButton;
    @FXML
    public TextField outputPathTextField;
    @FXML
    public Button outputPathSelectorButton;
    @FXML
    public RadioButton generateKmzRadio;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField presetToSavePathTextField;
    @FXML
    public Button savePresetButton;
    @FXML
    public Button presetToSavePathSelectorButton;
    @FXML
    public Text messageMessageText;
    @FXML
    public Label messageContentLabel;
    @FXML
    public TableView<IconsFX> iconCategoryTable;
    @FXML
    public TableColumn<IconsFX, String> categoryCol;
    @FXML
    public TableColumn<IconsFX, String> iconCol;

    public Stage primaryStage;

    private String csvPath;
    private String presetPath;
    private String outputKMLPath;
    private String outputPresetPath;
    private String kmlHeader;
    private String iconPresetType = "";

    private boolean wasDataLoaded = false;

    private OriginalKmlData originalKmlData;

    private Integer numberOfCategories;

    private List<String> lineList = new ArrayList<String>();
    ObservableList<String> categoriesList;
    ObservableList<String> iconList;

    private IconSet iconSet;

    public static void addControls(AnchorPane pane) {
    }

    public static void closeProgram() {
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        final ObservableList<String> iconSetPresetTypes = FXCollections.observableArrayList(
                "Created manually",
                "From preset file"
        );
        presetTypeSelectorCombo.setItems(iconSetPresetTypes);
    }

    public File selectFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

    public File selectFile(String extension) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter= new FileChooser.ExtensionFilter(extension.toUpperCase() + " file",extension.toLowerCase(), extension.toUpperCase());

        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);

        return fileChooser.showOpenDialog(primaryStage);
    }

    public void selectOutputPresetFile() {
        try {
            outputKMLPath = selectFile().getPath();
            presetToSavePathTextField.setText(outputPresetPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    public void selectOutputKMLFile() {
        try {
            outputKMLPath = selectFile("kml").getPath();
            outputPathTextField.setText(outputKMLPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    public void selectPresetFile() {
        try {
            presetPath = selectFile().getPath();
            iconPresetPathTextField.setText(presetPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    public void selectCsvFile() {
        try {
            csvPath = selectFile("csv").getPath();
            csvPathTextField.setText(csvPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    public void refreshCsvFilePath(){
        csvPath = csvPathTextField.getText();
    }

    public boolean isFilePathValid(String path){
        File file = new File(path);

        if(file.canRead() && file.exists()){
            return true;
        }

        return false;
    }

    public boolean isValidCSV(String path){
        if(!isFilePathValid(path)){
            return false;
        }

        if(!path.toLowerCase().endsWith(".csv")){
            return false;
        }

        return true;
    }

    public boolean isValidKML(String path){
        if(!isFilePathValid(path)){
            return false;
        }

        if(!path.toLowerCase().endsWith(".kml")){
            return false;
        }

        return true;
    }

    public void setPaths(String csvPath
            , String presetPath
            , String outputKMLPath
            , String outputPresetPath
    ) {

        this.csvPath = csvPath;
        this.presetPath = presetPath;
        this.outputKMLPath = outputKMLPath;
        this.outputPresetPath = outputPresetPath;

    }

    public Integer getNumberOfCategories() {
        return numberOfCategories;
    }

    public void setNumberOfCategories(Integer numberOfCategories) {
        numberOfCategories = numberOfCategories;
    }

    public void setListenerForNumOfCategories() {
        numCategoriesTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable
                    , String oldValue
                    , String newValue) {
                try {
                    if (StringUtils.isNotBlank(newValue))
                        numberOfCategories = Integer.parseInt(newValue);
                } catch (Exception e) {
                    //that's case when user tries to type nonInteger input
                    //todo: secure
                }
            }
        });
    }

    public void updatePresetTypeSelectorValue(){
        iconPresetType = (String) presetTypeSelectorCombo.getValue();
    }

    public void loadDataFromCsv() throws Exception {
        csvPath = "example_test_files/short_valid_csv.csv";

        System.out.println("Number of categories given by user or from file: " + numberOfCategories); //DEBUG

        if (csvPath != null) {
            CsvReader csvReader = new CsvReader(csvPath);
            csvReader.getSortedCsvReadyString(); //necessary for getLineList(), otherwise it will return nothing
            lineList = csvReader.getLineList();

            if (false) {
                //todo: check preset type, run either scan on kml or preset file

                //parse kml as preset
                originalKmlData = new OriginalKmlData(presetPath);
                kmlHeader = originalKmlData.getIconsHeader();

                //get icon list
                List<String> iconList = originalKmlData.getIconList();

                prepareIconEditor(csvReader);
            }

            //test
            prepareIconEditor(csvReader);
        } else
            System.out.println("NOT ENOUGH DATA TO PARSE CSV");

    }

    private void updateIconsFromKML(){
        //does no need additional List cleanup
        iconList = FXCollections.observableArrayList(originalKmlData.getIconList());
    }

    private void loadIconSetFromPresetFile(String iconSetPresetPath) throws Exception {
        /**
         * loadIconSetFromPresetFile(String iconSetPresetPath) cleans table view, then loads IconSet preset
         * file into the table and IconSet itself and refreshes the table afterwards
         */
        iconSet = new IconSet(iconSetPresetPath, categoriesList);
        kmlHeader = iconSet.kmlHeader;

        iconCategoryTable.getItems().clear(); //clear table view

        for (int i = 0; i < iconSet.size(); i++){
            String category = categoriesList.get(i);
            iconCategoryTable.getItems().add(
                    (new IconsFX(
                            category,
                            iconSet.getIconForCategory(category)
                    )
                    )
            );
        }

        iconCategoryTable.refresh();
    }

    private void generateIconSetWithoutPresetFIle() {
        /**
         * generateIconSetWithoutPresetFIle() cleans table view, generates basic TableView
         * using default icon value. It also loads kmlHeader into IconSet It will refresh table afterwards
         */
        iconSet = new IconSet(iconList, categoriesList);
        iconSet.setKmlHeader(kmlHeader); //set's KML header for IconSet

        iconCategoryTable.getItems().clear(); //clear table view

        for (int i = 0; i < iconSet.size(); i++) {
            String category = categoriesList.get(i);
            iconCategoryTable.getItems().add(
                    (new IconsFX(
                            category,
                            iconSet.getFirstIcon() //first icon from IconSet is used like a default value
                    )
                    )
            );
        }
        iconCategoryTable.refresh();
    }

    private void prepareIconEditor(CsvReader csvReader) throws Exception {
        /**
         * This method loads necessary data and let's user customize category-icon pairs before further processing
         * it needs csv file chosen by user and either IconSet preset file or a KML file for header
         */
        String sortedCsv = csvReader.getSortedCsvReadyString();

        numberOfCategories = 3; //todo: REMOVE THIS LINE LATER. IT IS HERE JUST SO TESTING IS QUICKER
        LastCategoryScanner lastCategoryScanner = new LastCategoryScanner(sortedCsv, numberOfCategories, true, true);

        ///TEST, should be done elsewhere basing on user choice
        originalKmlData = new OriginalKmlData("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\ShortExample.kml");
        kmlHeader = originalKmlData.getIconsHeader();
        ///

        categoriesList = FXCollections.observableArrayList(lastCategoryScanner.getLastCatList());
        updateIconsFromKML();

        //set edibility flags for table view columns
        iconCategoryTable.setEditable(true);
        categoryCol.setEditable(false);

        //set cell editors for table view columns
        categoryCol.setCellValueFactory(new PropertyValueFactory<IconsFX, String>("category"));
        iconCol.setCellValueFactory(new PropertyValueFactory<IconsFX, String>("icon"));
        iconCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(iconList));

        refreshCsvFilePath();
        if(!isValidCSV(csvPath)){
            return; //todo: put some error here
        }

        //load or generate IconSet
        if(iconPresetType.equals("From preset file")){
            loadIconSetFromPresetFile("example_test_files/testPreset.txt");
        }else {
            generateIconSetWithoutPresetFIle(); //default state, does not need user choice
        }

        //test;
        iconSet.saveIconSetPresetFile("example_test_files/testPreset2.txt");

        wasDataLoaded = true;
    }
}
//todo: handle all cases when user wants to change icon preset type or csv etc