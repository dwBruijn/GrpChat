package Client.Controllers;



import Client.MainConfiguration;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ExitController {

    @FXML
    private JFXButton buttonYes;
    @FXML
    private JFXButton buttonNo;


    @FXML
    void confrimExit(ActionEvent event) {
        MainConfiguration.getStageConfiguration().setExit(true);
        MainConfiguration.getStageConfiguration().getExitStage().close();
    }

    @FXML
    void noExit(ActionEvent event) {
        MainConfiguration.getStageConfiguration().setExit(false);
        MainConfiguration.getStageConfiguration().getExitStage().close();
    }
}
