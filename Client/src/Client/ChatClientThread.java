package Client;

import Client.Controllers.ClientGUIController;


import java.io.DataInputStream;
import java.net.SocketException;

public class ChatClientThread extends Thread
{

    private ClientGUIController client;
    private DataInputStream  streamIn;

    public ChatClientThread(ClientGUIController client) {
        this.client = client;
        streamIn  = MainConfiguration.getStreamConfiguration().getStreamIn();
        start();
    }


    public void run(){
        while (true){
        try {
            //Wait for data from stream and when data is accessed send it to ClientGUIController handle method to process it.
            client.handle(streamIn.readUTF());
        }
        catch(SocketException se){
            if(Thread.activeCount() > 1){
                client.handle("An error occurred. Please restart the client...");
                break;
            }
            se.printStackTrace();
        }
        catch(Exception e){
               MainConfiguration.getStreamConfiguration().stopStream();
               e.printStackTrace();
               break;
           }
       }
    }
}
