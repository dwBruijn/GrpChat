package Client.Utils;

import Client.MainConfiguration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StreamConfiguration {

    private static StreamConfiguration streamConfiguration = new StreamConfiguration();

    private Socket socket;
    private String hostAddr;
    private int portAddr = 0;
    private String name;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;

    private StreamConfiguration() {}

    public static StreamConfiguration getInstance() {
        return StreamConfiguration.streamConfiguration;
    }


    public void initSocket() throws IOException {
        this.socket = new Socket(hostAddr, portAddr);
    }

    // open socket IO streams
    public void initStream() throws IOException{
        try{
            streamIn = new DataInputStream(socket.getInputStream());
            streamOut = new DataOutputStream(socket.getOutputStream());
            // send username to server
            streamOut.writeUTF(name);
            streamOut.flush();
        }
        catch(IOException e) {
            MainConfiguration.getStageConfiguration().showErrorStage("Failed to initialize streams.");
            System.exit(0);
        }

    }

    // close socket IO streams
    public void stopStream(){
        try{
            if(streamIn != null) streamIn.close();
            if(streamOut != null) streamOut.close();
            if(socket != null) socket.close();
        }catch(IOException e){
            MainConfiguration.getStageConfiguration().showErrorStage("An error occurred closing the application. Shutdown initiated.");
            System.exit(0);
        }

    }

    // getters/setters

    public DataInputStream getStreamIn() {
        return streamIn;
    }

    public void setStreamIn(DataInputStream streamIn) {
        this.streamIn = streamIn;
    }

    public DataOutputStream getStreamOut() {
        return streamOut;
    }

    public void setStreamOut(DataOutputStream streamOut) {
        this.streamOut = streamOut;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getHostAddr() {
        return hostAddr;
    }

    public void setHostAddr(String hostAddr) {
        this.hostAddr = hostAddr;
    }

    public int getPortAddr() {
        return portAddr;
    }

    public void setPortAddr(int portAddr) {
        this.portAddr = portAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
