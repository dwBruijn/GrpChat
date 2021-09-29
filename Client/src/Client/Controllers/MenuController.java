package Client.Controllers;


import Client.MainConfiguration;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;



public class MenuController implements Initializable{

    @FXML
    private JFXButton buttonSettings;

    @FXML
    private JFXButton buttonExit2;

    @FXML
    private JFXButton buttonAbout;

    @FXML
    private VBox menuFxmlVBox;


    @FXML
    private JFXButton buttonConnection;

    public static VBox staticmenuFxmlVBox;

    private boolean isSettingsOpen;
    private boolean isAboutOpen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticmenuFxmlVBox = menuFxmlVBox;
        isAboutOpen = false;
        isSettingsOpen = false;
    }

    @FXML
    void About(ActionEvent event) {

        if(!isAboutOpen){
            isAboutOpen = true;
            try {
                MainConfiguration.getStageConfiguration().showAboutStage();
                isAboutOpen = false;

            } catch (Exception e) {
                MainConfiguration.getStageConfiguration().showErrorStage();
                e.printStackTrace();
                System.exit(0);
            }
        }
    }


    @FXML
    void Exit(ActionEvent event) {
        MainConfiguration.getStageConfiguration().showExitStage();
        if(MainConfiguration.getStageConfiguration().isExit()) {
            MainConfiguration.getStreamConfiguration().stopStream();
                Platform.exit();
                System.exit(0);
        }
    }

}
