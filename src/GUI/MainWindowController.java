package GUI;

import backend.CsvHandling.CsvReader;
import backend.CsvHandling.CsvRecordToStringInitData;
import backend.CsvHandling.LastCategoryScanner;
import backend.KmlHandling.KmlWriter;
import backend.KmlHandling.OriginalKmlData;
import backend.Misc.IconSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public Text loadPresetFileText;
    @FXML
    public Label messageContentLabel;
    @FXML
    public TableView<IconsFX> iconCategoryTable;
    @FXML
    public TableColumn<IconsFX, String> categoryCol;
    @FXML
    public TableColumn<IconsFX, String> iconCol;

    public Stage primaryStage;

    LastCategoryScanner lastCategoryScanner;

    private String csvPath;
    private String presetPath;
    private String outputKMLPath;
    private String outputPresetPath;
    private String kmlHeader;
    private String iconPresetType = "";
    private String sortedCsv;

    private boolean wasDataLoaded = false;

    private OriginalKmlData originalKmlData;

    private Integer numberOfCategories;

    private List<String> lineList = new ArrayList<String>();
    ObservableList<String> categoriesList;
    ObservableList<String> iconList;

    private IconSet iconSet;

    private final static Logger LOGGER = Logger.getLogger(CsvReader.class.getName());

    public static void addControls(AnchorPane pane) {
    }

    public static void closeProgram() {
    }

    public void setStage(Stage primaryStage) {
        LOGGER.setLevel(Level.INFO);
        this.primaryStage = primaryStage;

        final ObservableList<String> iconSetPresetTypes = FXCollections.observableArrayList(
                "Created manually",
                "From preset file"
        );
        presetTypeSelectorCombo.setItems(iconSetPresetTypes);

        presetSelectorButton.setDisable(true); //enabled by updatePresetTypeSelectorValue() and by listener
        loadDataButton.setDisable(true); //enabled by selectPresetFile()
        savePresetButton.setDisable(true); //enabled by selectOutputPresetFile() and a listener
        processSaveMapButton.setDisable(true); //enabled by outputPathTextField listener and selectOutputKMLFile()

        iconPresetPathTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    if(StringUtils.isNotBlank(newValue)){
                        presetPath = newValue.trim();
                        loadDataButton.setDisable(false);
                    }else{
                        loadDataButton.setDisable(true);
                    }
                }catch(Exception e){
                    presetPath = oldValue.trim();
                }
            }
        });

        csvPathTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    if(StringUtils.isNotBlank(newValue)){
                        csvPath = newValue.trim();
                    }
                }catch(Exception e){
                    csvPath = oldValue.trim();
                }
            }
        });

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if(newText.matches("^[-+]?\\d+$")){
                return change;
            }
            return null;
        };

        numCategoriesTextField.setTextFormatter(
                new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

        numCategoriesTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable
                    , String oldValue
                    , String newValue) {
                try {
                    if (StringUtils.isNotBlank(newValue)) {
                        numberOfCategories = Integer.parseInt(newValue);
                    }
                } catch (Exception e) {
                    //already secured by textFormatter
                }
            }
        });

        presetToSavePathTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    if(StringUtils.isNotBlank(newValue)){
                        outputPresetPath = newValue;

                        if(iconCategoryTable != null) {
                            if(iconCategoryTable.getItems().get(0).getIcon() != null)
                            {
                                savePresetButton.setDisable(false);
                            }
                        }
                    }else{
                        savePresetButton.setDisable(true);
                    }
                }catch(Exception e){
                    outputPresetPath = oldValue;
                }
            }
        });

        outputPathTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    if(StringUtils.isNotBlank(newValue)){
                        outputKMLPath = newValue;

                        if(iconCategoryTable != null) {
                            if (iconCategoryTable.getItems().get(0).getIcon() != null) {
                                processSaveMapButton.setDisable(false);
                            }
                        }else{
                            processSaveMapButton.setDisable(true);
                        }
                    }
                }catch (Exception e){
                    outputKMLPath = oldValue;
                }
            }
        });

    }

    public void setMessageLabelText(String errorText){
        LOGGER.info(errorText);

        messageContentLabel.setTextFill(Color.RED);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if(errorText == null){
            errorText = dtf.format(now) + "  Unknown Error";
        }else{
            errorText = dtf.format(now) + "  " + errorText;
        }

        messageContentLabel.setText(errorText);
    }

    private File selectOpenFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

    private File selectOpenFile(String extension) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(extension.toUpperCase() + " file",extension.toLowerCase(), extension.toUpperCase());

        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);

        return fileChooser.showOpenDialog(primaryStage);
    }

    private File selectSaveFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showSaveDialog(primaryStage);
    }

    private File selectSaveFile(String extension) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(extension.toUpperCase() + " file",extension.toLowerCase(), extension.toUpperCase());

        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);

        return fileChooser.showSaveDialog(primaryStage);
    }

    public boolean selectOutputPresetFile() {
        try {
            outputPresetPath = selectSaveFile("kmlpreset").getPath();

            if(!outputPresetPath.toLowerCase().endsWith(".kmlpreset")){
                outputPresetPath = outputPresetPath + ".kmlpreset";
            }

            presetToSavePathTextField.setText(outputPresetPath);

            if(iconCategoryTable != null) {
                savePresetButton.setDisable(iconCategoryTable.getItems().get(0).getIcon() == null);
            }

            return true;
        } catch (Exception e) {
            //case when no file was selected.
            return false;
        }
    }

    public boolean selectOutputKMLFile() {
        try {
            outputKMLPath = selectSaveFile("kml").getPath();

            if(!outputKMLPath.toLowerCase().endsWith(".kml")){
                outputKMLPath = outputKMLPath + ".kml";
            }

            outputPathTextField.setText(outputKMLPath);

            if(iconCategoryTable != null) {
                if (iconCategoryTable.getItems().get(0).getIcon() != null) {
                    processSaveMapButton.setDisable(false);
                }
            }else{
                processSaveMapButton.setDisable(true);
            }

            return true;
        } catch (Exception e) {
            //case when no file was selected.
            return false;
        }
    }

    public void selectPresetFile() {
        try {
            presetPath = selectOpenFile().getPath();
            iconPresetPathTextField.setText(presetPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }

        loadDataButton.setDisable(false);
    }

    public void selectCsvFile() {
        try {
            csvPath = selectOpenFile("csv").getPath();
            csvPathTextField.setText(csvPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    private void refreshCsvFilePath(){
        csvPath = csvPathTextField.getText();
    }

    private boolean isFilePathValid(String path){
        File file = new File(path);

        return file.canRead() && file.exists();
    }

    private boolean isValidCSV(String path){
        if(!isFilePathValid(path)){
            return false;
        }

        return path.toLowerCase().endsWith(".csv");
    }

    private boolean isValidKML(String path){
        if(!isFilePathValid(path)){
            return false;
        }

        return path.toLowerCase().endsWith(".kml");
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

    public void savePresetFile() throws IOException {
        refreshIconSetPairedIcons();

        if(!outputPresetPath.toLowerCase().endsWith(".kmlpreset")){
            outputPresetPath = outputPresetPath + ".kmlpreset";
        }

        iconSet.saveIconSetPresetFile(outputPresetPath);

        messageContentLabel.setTextFill(Color.GREEN);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        messageContentLabel.setText(dtf.format(now) + " Preset successfully saved as " + outputPresetPath);
    }

    public Integer getNumberOfCategories() {
        return numberOfCategories;
    }

    public void setNumberOfCategories(Integer numberOfCategories) {
        this.numberOfCategories = numberOfCategories;
    }

    public void updatePresetTypeSelectorValue(){
        iconPresetType = (String) presetTypeSelectorCombo.getValue();
        presetSelectorButton.setDisable(false);

        if (((String)presetTypeSelectorCombo.getValue()).equals("Created manually")) //it's funny but this cast let's us avoid nullptr on .toString() so I placed it here just in case
            loadPresetFileText.setText("Load KML file");
        else
            loadPresetFileText.setText("Load Icon Preset");
    }

    public void loadDataFromCsvAndKML() throws Exception {
        if(presetTypeSelectorCombo.getValue() == null){
            setMessageLabelText("No preset type was selected");
            return;
        }
        String presetType = presetTypeSelectorCombo.getValue().toString();

        if(csvPath == null){
            setMessageLabelText("No CSV file was selected");
            return;
        }

        if(!isValidCSV(csvPath)){
            setMessageLabelText("Selected CSV file does not have .csv nor .CSV extension, file may be invalid.");
            return;
        }

        if(presetType.equals("From preset file") & (presetPath == null || presetType.equals(""))){
            setMessageLabelText("No Preset file was selected");
            return;
        }

        System.out.println("Number of categories given by user or from file: " + numberOfCategories); //DEBUG

        if (csvPath != null) {
            CsvReader csvReader = new CsvReader(csvPath);
            sortedCsv = csvReader.getSortedCsvReadyString(); //necessary for getLineList(), otherwise it will return nothing
            lineList = csvReader.getLineList();
            lastCategoryScanner = new LastCategoryScanner(sortedCsv, numberOfCategories, true, true);
            categoriesList = FXCollections.observableArrayList(lastCategoryScanner.getLastCatList());

            //parse kml header
            originalKmlData = new OriginalKmlData(presetPath);
            kmlHeader = originalKmlData.getIconsHeader();

            if (presetType.equals("Created manually")) {
                if(!isValidKML(presetPath)){
                    setMessageLabelText("KML file should have .kml or .KML extension if you want to set it manually");
                    return;
                }

                //get icon list
                List<String> iconList = originalKmlData.getIconList();
            }
            else if(presetType.equals("From preset file"))
            {
                //parse preset file as preset
                loadIconSetFromPresetFileToTheUI();
            }

            prepareIconEditor(csvReader);
        }
    }

    private void updateIconsFromKML(){
        //does not need additional List cleanup
        iconList = FXCollections.observableArrayList(originalKmlData.getIconList());
    }

    private void loadIconSetFromPresetFileToTheUI() throws Exception {
        /**
         * loadIconSetFromPresetFile(String iconSetPresetPath) cleans table view, then loads IconSet preset
         * file into the table and IconSet itself and refreshes the table afterwards
         */
        iconCategoryTable.getItems().clear(); //clear table view

        if(((String)presetTypeSelectorCombo.getValue()).equals("Created manually")) { //this cast let's us avoid nullptr on .toString() so I placed it here just in case
            iconSet = new IconSet(presetPath, categoriesList);
        }else{
            iconSet = new IconSet(presetPath, numberOfCategories);
        }

        kmlHeader = iconSet.kmlHeader;


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
            setMessageLabelText("CSV file is invalid");
            return;
        }

        //load or generate IconSet
        if(iconPresetType.equals("From preset file")){
            loadIconSetFromPresetFileToTheUI();
        }else {
            generateIconSetWithoutPresetFIle(); //default state, does not need user choice
        }


        wasDataLoaded = true;
    }

    private void refreshIconSetPairedIcons(){
        for(int i = 0; i < iconSet.nbOfAvailableCategories; i++){
            iconSet.setCategoryForIndex(i, categoryCol.getCellObservableValue(i).getValue().toString());
            iconSet.setIconForCategoryIndex(i, iconCol.getCellObservableValue(i).getValue().toString());
        }

    }

    public void saveKMLFile() throws IOException {
        refreshIconSetPairedIcons();
        //TODO: handle hasRating
        //TODO: handle maxRate
        //TODO: handle addLastCategory
        CsvRecordToStringInitData csvRecordToStringInitData = new CsvRecordToStringInitData(iconList, getNumberOfCategories(),5, lineList.get(1), false, true, iconSet);

        KmlWriter kmlWriter = new KmlWriter(lineList,
                kmlHeader,
                getNumberOfCategories(),
                outputKMLPath,
                csvRecordToStringInitData
                );

        kmlWriter.saveKML();
    }
}


//TODO: file selectors must show files (see preset file selector)
