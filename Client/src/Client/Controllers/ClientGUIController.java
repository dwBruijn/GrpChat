package Client.Controllers;

import Client.MainConfiguration;
import Client.ChatClientThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;



public class ClientGUIController implements Initializable{


    @FXML
    private JFXHamburger buttonMenu;
    @FXML
    private JFXButton buttonSend;
    @FXML
    private ListView<HBox> chatList;
    @FXML
    private ListView<HBox> onlineList;
    @FXML
    private JFXTextField textField;
    @FXML
    private HBox header;
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane drawerAnchorPane;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label nameLabel;

    private HamburgerSlideCloseTransition transition;

    private String name;

    private DataOutputStream streamOut;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // buttonSend.setGraphic(new ImageView("Client/Resources/img/enterbutton2.png"));
        streamOut = MainConfiguration.getStreamConfiguration().getStreamOut();
        // get username
        name = MainConfiguration.getStreamConfiguration().getName();
        // display username
        nameLabel.setText(name);

        initFields();
        initOnlineList();
        new ChatClientThread(this);
    }

    @FXML
    void pressedSend(ActionEvent event) {
        // send non empty messages
        if(textField.getText() != null && !textField.getText().trim().isEmpty()){
            Send(textField.getText());
        }
        textField.clear();
    }

    private void Send(String message){
        try{
            streamOut.writeUTF(name+": "+message);
            streamOut.flush();

            ClientMessage(message);
        }catch(IOException e){
            MainConfiguration.getStreamConfiguration().stopStream();
            MainConfiguration.getStageConfiguration().showErrorStage("Failed to send message.");
        }catch (Exception e) {
            MainConfiguration.getStreamConfiguration().stopStream();
            MainConfiguration.getStageConfiguration().showErrorStage();
        }
    }

    // display sent message in the chat
    private void ClientMessage(String text){
        MainConfiguration.getMessageConfiguration().displayClientMessage(chatList, text);
    }

    // display received message in the chat
    private void ServerMessage(String text){
        MainConfiguration.getMessageConfiguration().displayServerMessage(chatList, text);
    }

    private void initFields() {
        // listener to close drawer if user clicks inside main chat window
        chatList.setOnMousePressed(e-> {
            if(drawer.isShown()){
                drawer.close();
                transition.setRate(-1);
                transition.play();
            }
        });
        // listener to close drawer if user clicks on TextField
        textField.setOnMousePressed(e-> {
            if(drawer.isShown()){
                drawer.close();
                transition.setRate(-1);
                transition.play();
            }
        });

        // listener to write characters in TextField if main chat window has the focus
        // triggered on all keys except ctrl, shift, alt, and capslk
        chatList.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.CONTROL && event.getCode() != KeyCode.SHIFT && event.getCode() != KeyCode.ALT
                    && event.getCode() != KeyCode.CAPS)
                textField.requestFocus();

        });

        // Textfield limit is 256 chars
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue()){
                if(textField.getText().length() >= 256){
                    textField.setText(textField.getText().substring(0,256));
                }
            }
        });

        // give drawer a higher Z-order than the main chat window
        drawer.toFront();
        // load drawer content
        drawer.setSidePane(MainConfiguration.getStageConfiguration().initMenu());
        // make sure drawer starts closed
        transition = new HamburgerSlideCloseTransition(buttonMenu);
        transition.setRate(-1);
        buttonMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            // reverse transition direction on each click
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(!drawer.isShown()){
                drawer.setPrefSize(onlineList.getWidth(), onlineList.getHeight());
                drawer.open();
            }
            else{
                drawer.close();
            }
        });

        // listener to handle key presses when the TextField has the focus
        textField.setOnKeyPressed(event -> {
            // if Enter is pressed
            if (event.getCode() == KeyCode.ENTER) {
                buttonSend.fire();
            }
        });

        Platform.runLater(()-> textField.requestFocus());
    }

    private void initOnlineList(){
        //You can convert this method to FXML.
        HBox hbox = new HBox();
        Label l = new Label();
        l.setText("Online");
        l.setTextFill(Color.WHITE);
        l.setFont(new Font(18));
        l.setMaxSize(500,500);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(l);
        onlineList.getItems().add(hbox);
    }

    // add connected user to online list
    private void addToOnlineList(String message){
        HBox hbox = new HBox();
        Label l = new Label();
        l.setText(message);
        l.setTextFill(Color.GHOSTWHITE);
        l.setFont(new Font(16));
        l.setMaxSize(500,500);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(l);
        onlineList.getItems().add(hbox);
    }

    // remove user from online list
    private void removeFromOnlineList(String message){
        int who = Integer.parseInt(message);
        onlineList.getItems().remove(who);
    }


    // This method gets handled by ChatClientThread when a new message is received.
    public void handle(String msg){

        if(msg.startsWith("//whoisonline")){
            String[] message = msg.split("-");
            //whoisonline gets called when the application loaded. That means there are multiple names received.
            //Skip whoisonline so method does not add an user named //whoisonline.
            for(String i : message){
                if(i.equals("//whoisonline")){continue;}
                Platform.runLater(() -> addToOnlineList(i));
            }
        } else if(msg.startsWith("//status-")){
            String[] message = msg.split("-");
            Platform.runLater(() -> addToOnlineList(message[1]));

        } else if(msg.startsWith("//statusdisconnect-")){
            String[] message = msg.split("-");
            Platform.runLater(() -> removeFromOnlineList(message[1]));
        } else {
            // display received message in chat
            Platform.runLater(()-> ServerMessage(msg));
        }
    }
}

