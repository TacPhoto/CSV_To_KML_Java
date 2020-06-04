package GUI;

import backend.CsvHandling.CsvReader;
import backend.CsvHandling.LastCategoryScanner;
import backend.KmlHandling.IconSet;
import backend.KmlHandling.OriginalKmlData;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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

    public void selectOutputPresetFile() {
        try {
            outputKMLPath = selectFile().getPath();
            presetToSavePathTextField.setText(outputPresetPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
    }

    public void selectOutPutKMLFile() {
        try {
            outputKMLPath = selectFile().getPath();
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
            csvPath = selectFile().getPath();
            csvPathTextField.setText(csvPath);
        } catch (Exception e) {
            //case when no file was selected. Ignore
        }
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

    public void loadDataFromCsv() throws Exception {
        csvPath = "example_test_files/short_valid_csv.csv";

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

    private void prepareIconSetBasedOnTable(){
        /** prepareIconSetBasedOnTable will clean iconSet up and initialize it again
         * then it will set proper categories basing on data from table
         */
        //todo: remove this method later. it's redundant
        //System.out.println(categoryCol.getCellObservableValue(0).getValue().toString());
        //System.out.println(iconCol.getCellObservableValue(0).getValue().toString());
        iconSet = new IconSet(iconList, categoriesList);
        iconSet.generateIconSetFromData();

        //TEST
        System.out.println(iconSet.getDebugIcon("big"));
    }

    private void updateIconsFromKML(){
        //does no need additional List cleanup
        iconList = FXCollections.observableArrayList(originalKmlData.getIconList());
    }

    private void prepareIconEditor(CsvReader csvReader) throws Exception {
        String sortedCsv = csvReader.getSortedCsvReadyString();

        ArrayList<String> testIconList = new ArrayList<String>();
        testIconList.add("Ione");
        testIconList.add("Itwo");

        LastCategoryScanner lastCategoryScanner = new LastCategoryScanner(sortedCsv, 3, true, true);

        ///TEST, should be done elsewhere
        originalKmlData = new OriginalKmlData("Z:\\GitHubLearning\\CSV_To_KML_Java\\example_test_files\\ShortExample.kml");
        kmlHeader = originalKmlData.getIconsHeader();
        ///

        categoriesList = FXCollections.observableArrayList(lastCategoryScanner.getLastCatList());
        updateIconsFromKML();

        iconCategoryTable.setEditable(true);
        categoryCol.setEditable(false);

        categoryCol.setCellValueFactory(new PropertyValueFactory<IconsFX, String>("category"));

        iconCol.setCellValueFactory(new PropertyValueFactory<IconsFX, String>("icon"));
        iconCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(iconList));

        iconCategoryTable.getItems().add(new IconsFX("capital", "default"));
        iconCategoryTable.getItems().add(new IconsFX("big", "default"));
        iconCategoryTable.getItems().add(new IconsFX("small", "default"));
        iconCategoryTable.refresh();

        System.out.println(categoriesList);
        System.out.println();
        prepareIconSetBasedOnTable(); //todo: add reading iconset preset from file
        System.out.println("test end");
    }
}
