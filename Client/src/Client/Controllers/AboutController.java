package Client.Controllers;



import Client.MainConfiguration;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class AboutController {

    @FXML
    private JFXButton buttonClose;


    @FXML
    void Close(ActionEvent event) {
        MainConfiguration.getStageConfiguration().getAboutStage().close();
    }


}
