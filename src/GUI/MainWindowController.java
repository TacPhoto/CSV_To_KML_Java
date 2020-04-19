package GUI;

import backend.CsvHandling.CsvReader;
import backend.KmlHandling.OriginalKmlData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
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
    public TableView iconCategoryTable;

    public Stage primaryStage;

    private String csvPath;
    private String presetPath;
    private String outputKMLPath;
    private String outputPresetPath;
    private String kmlHeader;

    private OriginalKmlData originalKmlData;

    private Integer numberOfCategories;

    private List<String> lineList = new ArrayList<String>();



    public static void addControls(AnchorPane pane) {
    }

    public static void closeProgram() {
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public File selectFile(){
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void selectOutputPresetFile(){
        try {
            outputKMLPath = selectFile().getPath();
            presetToSavePathTextField.setText(outputPresetPath);
        }catch(Exception e){
            //case when no file was selected. Ignore
        }
    }

    public void selectOutPutKMLFile(){
        try {
            outputKMLPath = selectFile().getPath();
            outputPathTextField.setText(outputKMLPath);
        }catch(Exception e){
            //case when no file was selected. Ignore
        }
    }

    public void selectPresetFile(){
        try {
            presetPath = selectFile().getPath();
            iconPresetPathTextField.setText(presetPath);
        }
        catch(Exception e){
            //case when no file was selected. Ignore
        }
    }

    public void selectCsvFile(){
        try{
        csvPath = selectFile().getPath();
            csvPathTextField.setText(csvPath);}
        catch(Exception e){
            //case when no file was selected. Ignore
        }
    }

    public void setPaths(String csvPath
            , String presetPath
            , String outputKMLPath
            , String outputPresetPath
            ){

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
                    , String newValue)
            {
                try{
                    if(StringUtils.isNotBlank(newValue))
                        numberOfCategories = Integer.parseInt(newValue);
                }
                catch (Exception e) {
                    //that's case when user tries to type nonInteger input
                    //todo: secure
                }
            }
        });
    }

    public void loadDataFromCsv() throws Exception {
        if(csvPath != null) {
            CsvReader csvReader = new CsvReader(csvPath);
            csvReader.getSortedCsvReadyString(); //necessary for getLineList(), otherwise it will return nothing
            lineList = csvReader.getLineList();

            if(false) {
                //todoo: check preset type, run either scan on kml or preset file

                //parse kml as preset
                originalKmlData = new OriginalKmlData(presetPath);
                kmlHeader = originalKmlData.getIconsHeader();

                //get icon list
                List<String> iconList = originalKmlData.getIconList();

            }
        }
        else
            System.out.println("NOT ENOUGH DATA TO PARSE CSV");
    }
}
