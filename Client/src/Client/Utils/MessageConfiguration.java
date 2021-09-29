package Client.Utils;

import ChatBubble.BubbleSpec;
import ChatBubble.BubbledLabel;
import Client.MainConfiguration;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import com.jfoenix.effects.JFXDepthManager;



public class MessageConfiguration {

    private static MessageConfiguration messageConfiguration = new MessageConfiguration();

    private MessageConfiguration() {}

    public static MessageConfiguration getInstance() {
        return MessageConfiguration.messageConfiguration;
    }


    private HBox createBubble(ListView listView, String text, String color, Pos pos, BubbleSpec bubbleSpec){
        HBox hBox = new HBox();
        BubbledLabel b1 = new BubbledLabel();
        b1.setText(text);
        b1.setFont(new Font(14));
        b1.setMaxSize(350, 500);
        b1.setCursor(Cursor.TEXT);
        b1.setStyle("-fx-background-color: "+color);
        b1.setWrapText(true);
        hBox.setMaxWidth(listView.getWidth() - 20);
        hBox.setAlignment(pos);
        b1.setBubbleSpec(bubbleSpec);
        hBox.getChildren().add(b1);
        JFXDepthManager.setDepth(hBox, 2);
        return hBox;
    }

    // add sent message to listview
    public void displayClientMessage(ListView listView, String text){
        String name = MainConfiguration.getStreamConfiguration().getName();
        if(text.trim().isEmpty()) {
            return;
        }
        HBox hBox = createBubble(listView, name + ": "+ text , "lightgreen", Pos.TOP_RIGHT, BubbleSpec.FACE_RIGHT_CENTER);
        listView.getItems().add(hBox);
        listView.scrollTo(listView.getItems().lastIndexOf(hBox));
    }

    // add reveived message to listview
    public void displayServerMessage(ListView listView, String text){
        if(text.trim().isEmpty() || text.split(":")[1].trim().isEmpty()) {
            return;
        }
        HBox hBox = createBubble(listView, " "+ text , "white", Pos.TOP_LEFT, BubbleSpec.FACE_LEFT_CENTER);
        listView.getItems().add(hBox);
        listView.scrollTo(listView.getItems().lastIndexOf(hBox));
    }
}
