package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {
    String csvPath;
    String presetPath;
    String outputKMLPath;
    String outputPresetPath;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);

        MainWindowController controller = (MainWindowController) loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setOnCloseRequest((e) -> {MainWindowController.closeProgram();});

        controller.setPaths(csvPath, presetPath, outputKMLPath, outputPresetPath);



        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
