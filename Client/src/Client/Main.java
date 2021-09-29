package Client;



import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       MainConfiguration.getStageConfiguration().getLoginGUIStage().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
