package Client.Utils;

import Client.MainConfiguration;
import Client.Controllers.MenuController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class StageConfiguration {

    private static StageConfiguration stageConfiguration = new StageConfiguration();

    private Stage loginGUIStage;
    private Scene loginGUIScene;

    private Stage clientGUIStage;
    private Scene clientGUIScene;

    private Stage aboutStage;
    private Scene aboutScene;

    private Stage errorStage;
    private Scene errorScene;
    private Label errorMessage;

    private Stage exitStage;
    private Scene exitScene;
    private boolean exit;

    // init all stages
    private StageConfiguration(){
        initError();
        initLoginGUI();
        initExit();
        initAbout();
    }

    public static StageConfiguration getInstance() {
        return StageConfiguration.stageConfiguration;
    }

    private Stage setStageXY(Stage stage){
        stage.setX(clientGUIStage.getX() + clientGUIStage.getWidth() / 2.5);
        stage.setY(clientGUIStage.getY() + clientGUIStage.getHeight() / 3);
        return stage;
    }

    // init login stage
    private void initLoginGUI(){
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/LoginGUI.fxml"));
        try {
           Parent loginGUIRoot = (Parent) fxmlLoader.load();
            loginGUIStage = new Stage();
            loginGUIStage.setTitle("Login");
            loginGUIScene = new Scene(loginGUIRoot, 508, 360);
            loginGUIScene.getStylesheets().add(getClass().getClassLoader().getResource("Client/Res/css/main.css").toExternalForm());
            loginGUIStage.setScene(loginGUIScene);
            loginGUIStage.setResizable(false);
        }catch (Exception e) {
            showErrorStage();
            e.printStackTrace();
            System.exit(0);
        }
    }

    // initialized after login
    public void initClientGUI(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/ClientGUI.fxml"));
            Parent clientGUIRoot = (Parent) fxmlLoader.load();
            clientGUIStage = new Stage();
            // listener to handle requests for closing the window
            clientGUIStage.setOnCloseRequest(e -> {
                e.consume();
                // display confirm exit message
                showExitStage();
                // if Yes is clicked
                if (exit) {
                    try {
                        // close socket and IO streams
                        MainConfiguration.getStreamConfiguration().stopStream();
                        Platform.exit();
                        System.exit(0);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        System.exit(0);

                    }

                }
            });
            clientGUIStage.setTitle("Project");
            clientGUIScene = new Scene(clientGUIRoot);
            clientGUIScene.getStylesheets().add(getClass().getClassLoader().getResource("Client/Res/css/main.css").toExternalForm());
            // change menu slider height on ClientGUIController new scene height
            clientGUIScene.heightProperty().addListener(((observableValue, oldSceneHeight, newSceneHeight) -> MenuController.staticmenuFxmlVBox.setPrefHeight((Double) newSceneHeight - 65)));
            clientGUIStage.setScene(clientGUIScene);
        }catch(Exception e){
            errorStage.showAndWait();
            e.printStackTrace();
            System.exit(0);
        }
    }

    // init menu view
    // used to initialize the drawer
    public VBox initMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Menu.fxml"));
            loader.setController(new MenuController());
            return loader.load();
        }catch (Exception e){
            showErrorStage();
            e.printStackTrace();
        }
        return null;
    }

    // init about stage
    private void initAbout(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/About.fxml"));
        try {
            Parent aboutRoot = (Parent) fxmlLoader.load();
            aboutStage = new Stage();
            aboutStage.initStyle(StageStyle.UNDECORATED);
            aboutStage.setResizable(false);
            aboutStage.setTitle("About");
            aboutScene = new Scene(aboutRoot);
            aboutScene.getStylesheets().add(getClass().getClassLoader().getResource("Client/Res/css/main.css").toExternalForm());
            aboutStage.setScene(aboutScene);
            aboutStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            showErrorStage();
            e.printStackTrace();
        }

    }

    // init error stage
    private void initError(){
        errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.setTitle("Error");
        errorMessage = new Label("An error has occurred. Please try again");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> errorStage.close());
        VBox vbox = new VBox(40);
        vbox.setPrefSize(350,250);
        vbox.getChildren().addAll(errorMessage,closeButton);
        vbox.setAlignment(Pos.CENTER);
        errorScene = new Scene(vbox);
        errorStage.setScene(errorScene);
    }

    // init exit stage
    private void initExit(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Exit.fxml"));
        try {
            Parent exitRoot = (Parent) fxmlLoader.load();
            exitStage = new Stage();
            exitStage.initModality(Modality.APPLICATION_MODAL);
            exitStage.initStyle(StageStyle.UNDECORATED);
            exitStage.setResizable(false);
            exitStage.setTitle("Exit");
            exitScene = new Scene(exitRoot);
            exitScene.getStylesheets().add(getClass().getClassLoader().getResource("Client/Res/css/main.css").toExternalForm());
            exitStage.setScene(exitScene);
            exitStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            showErrorStage();
            e.printStackTrace();
        }

    }

    public void showExitStage(){
        //setStageXY(exitStage).showAndWait();
        exitStage.setX(clientGUIStage.getX() + clientGUIStage.getWidth() / 2.5);
        exitStage.setY(clientGUIStage.getY() + clientGUIStage.getHeight() / 3);
        exitStage.showAndWait();
    }

    // generic error stage
    public void showErrorStage(){
        this.errorMessage.setText("An error occurred. Please restart the client and try again...");
        if(clientGUIStage == null){
            errorStage.showAndWait();
            return;
        }
        setStageXY(errorStage).showAndWait();
    }

    // specific error stage to display errorMessage
    public void showErrorStage(String errorMessage){
        this.errorMessage.setText(errorMessage);
        if(clientGUIStage == null){
            errorStage.showAndWait();
            return;
        }
        setStageXY(errorStage).showAndWait();
    }

    public void showAboutStage(){
        setStageXY(aboutStage).showAndWait();
    }

    // getters/setters

    public Stage getLoginGUIStage() {
        return loginGUIStage;
    }

    public void setLoginGUIStage(Stage loginGUIStage) {
        this.loginGUIStage = loginGUIStage;
    }

    public Scene getLoginGUIScene() {
        return loginGUIScene;
    }

    public void setLoginGUIScene(Scene loginGUIScene) {
        this.loginGUIScene = loginGUIScene;
    }

    public Stage getClientGUIStage() {
        return clientGUIStage;
    }

    public void setClientGUIStage(Stage clientGUIStage) {
        this.clientGUIStage = clientGUIStage;
    }

    public Scene getClientGUIScene() {
        return clientGUIScene;
    }

    public void setClientGUIScene(Scene clientGUIScene) {
        this.clientGUIScene = clientGUIScene;
    }

    public Stage getAboutStage() {
        return aboutStage;
    }

    public void setAboutStage(Stage aboutStage) {
        this.aboutStage = aboutStage;
    }

    public Scene getAboutScene() {
        return aboutScene;
    }

    public void setAboutScene(Scene aboutScene) {
        this.aboutScene = aboutScene;
    }

    public Stage getErrorStage() {
        return errorStage;
    }

    public void setErrorStage(Stage errorStage) {
        this.errorStage = errorStage;
    }

    public Scene getErrorScene() {
        return errorScene;
    }

    public void setErrorScene(Scene errorScene) {
        this.errorScene = errorScene;
    }

    public Label getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Label errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Stage getExitStage() {
        return exitStage;
    }

    public void setExitStage(Stage exitStage) {
        this.exitStage = exitStage;
    }

    public Scene getExitScene() {
        return exitScene;
    }

    public void setExitScene(Scene exitScene) {
        this.exitScene = exitScene;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public Boolean getExit() { return this.exit; }

    public void setExit(Boolean value) { this.exit = value; }
}
