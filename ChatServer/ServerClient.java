import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ServerClient implements ServerInterface{

    protected String name;
    protected int port;
    protected String ip;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected boolean connected;

    public ServerClient(ObjectOutputStream output, ObjectInputStream input, String name, int port, String ip){
        this.connected = true;
        this.output = output;
        this.input = input;
        this.name = name;
        this.port = port;
        this.ip = ip;

        run();
    }

    @Override
    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object[] data = (Object[]) input.readObject();

                        // Handle the message
                        ServerClient.this.handleMessage(data);

                    } catch (IOException e) {
                        break;
                    } catch (ClassNotFoundException e) {
                        ServerMain.frame.appendMessage("Object class not found error: \n" + e.getMessage() + "\n", Color.WHITE);
                        ServerMain.frame.appendMessage("" + ServerFrame.getDate() + "\n\n", Color.WHITE);
                        break;
                    }
                }
                // Close the connection and remove the thread from the thread pool
                ServerClient.this.closeConnection();
            }
        }).start();
    }

    // Handle Messages
    @Override
    public void handleMessage(Object[] data){
        if(data[0].equals(Message.PRIVATE)){
            // Send a private message
            ServerMain.frame.server.sendMessage(name, (String)data[1], (String)data[2]);
        }
    }

    @Override
    public void sendData(Object[] data){
        try{
            output.writeObject(data);
            output.flush();
        }catch(IOException e){
            ServerMain.frame.appendMessage("Error: Sending message. " + e.getMessage() + "\n", Color.WHITE);
        }
    }

    @Override
    public void closeConnection(){
        try{
            if(output != null) output.close();
            if(input != null) input.close();
        } catch (IOException e){
            ServerMain.frame.appendMessage("Error: Unable to close client. " + '\n' + e.getMessage() + "\n", Color.WHITE);
            ServerMain.frame.appendMessage("" + ServerFrame.getDate() + "\n\n", Color.WHITE);
        }

        // Remove client from all other clients
        ServerFrame.server.removeClient(name);

        // Remove Client from the thread pool
        ServerFrame.server.removeClientThread(name);

        // Add server message
        ServerMain.frame.appendMessage(name + " disconnected.\n", Color.WHITE);
        ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);
    }

    public String getName(){ return name; }

    public int getPort(){ return port; }

    public String getIP(){ return ip; }
}