package Client.Controllers;

import Client.MainConfiguration;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginGUIController implements Initializable{
    @FXML
    private VBox vbox;
    @FXML
    private TextField fieldUserName;
    @FXML
    private TextField fieldHostname;
    @FXML
    private TextField fieldPort;
    @FXML
    private Button buttonJoin;

    // called after FXMLLoader loads the Scene's FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // username 10 chars max
        textFieldLimit(this.fieldUserName, 10);

        // IP 15 chars max
        textFieldLimit(this.fieldHostname, 15);

        // Port 5 chars max
        textFieldLimit(this.fieldPort, 5);

        // give focus at some point after the scene has been fully loaded
        Platform.runLater(()-> vbox.requestFocus());

        // set listener to trigger "Join" button on Enter
        vbox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                this.buttonJoin.fire();
            }
        });
    }

    // check the field content length dynamically
    private void textFieldLimit(TextField textField, int limit){
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue()){
                if(textField.getText().length() > limit){
                    textField.setText(textField.getText().substring(0,limit));
                }
            }
        });
    }


    @FXML
    void join(ActionEvent event) {
        String name;
        int port;
        String host;

        // parse input
        try{
            name = fieldUserName.getText();
            port = Integer.parseInt(fieldPort.getText());
            host = this.fieldHostname.getText();

            if(name.isEmpty()) {
                MainConfiguration.getStageConfiguration().showErrorStage("Invalid username.");
                return;
            }
            if(host.isEmpty()) {
                MainConfiguration.getStageConfiguration().showErrorStage("Invalid host address.");
                return;
            }
            if(port <= 0 || port > 65535) {
                MainConfiguration.getStageConfiguration().showErrorStage("Invalid port number.");
                return;
            }
        }catch(Exception e){
            MainConfiguration.getStageConfiguration().showErrorStage("Invalid input. Please try again.");
            e.printStackTrace();
            return;
        }
        try {
            initStreamConfiguration(name, host, port);
            // init main ClientGUIController stage
            MainConfiguration.getStageConfiguration().initClientGUI();
            MainConfiguration.getStageConfiguration().getClientGUIStage().show();
            MainConfiguration.getStageConfiguration().getLoginGUIStage().close();

        }catch(Exception ex){
            MainConfiguration.getStageConfiguration().showErrorStage();
            ex.printStackTrace();
            System.exit(1);
        }
    }

    // init socket and socket IO streams
    private void initStreamConfiguration(String name, String ip, int port) throws IOException {
        MainConfiguration.getStreamConfiguration().setName(name);
        MainConfiguration.getStreamConfiguration().setHostAddr(ip);
        MainConfiguration.getStreamConfiguration().setPortAddr(port);
        MainConfiguration.getStreamConfiguration().initSocket();
        MainConfiguration.getStreamConfiguration().initStream();
    }
}
