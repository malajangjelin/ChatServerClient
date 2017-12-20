import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ClientServerHandler implements ServerInterface {

    protected String name;
    protected Socket socket;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    public boolean connected;

    public ClientServerHandler(String name){
        this.name = name;
        this.socket = null;
        this.input  = null;
        this.output = null;
        this.connected = false;

        run();
    }

    @Override
    public void run(){
        // Authenticate a username to connect to the server
        authenticate();

        // Get initial response from the server
        new Thread(()->{
            try{
                while(connected){
                    // Receive Messages
                    Object[] data = (Object[]) input.readObject();
                    handleMessage(data);
                }
            }catch(IOException ex){
                if(connected){
                    ClientMain.frame.logout();
                    ClientMain.frame.displayMessage("Disconnected from the server.");
                    connected = false;
                }
            }catch(ClassNotFoundException e){
                ClientMain.frame.logout();
                ClientMain.frame.displayMessage("Disconnected from the server.\n" + e.getMessage());
                connected = false;
            }
            closeConnection();
        }).start();
    }

    // Handle Message
    @Override
    public synchronized void handleMessage(Object[] data){
        if(data[0].equals(Message.PRIVATE)){
            ClientFrame.clientList.addMessage((String)data[1], (String)data[2]);
        }
        else if(data[0].equals(Client.ADD)){
            ClientFrame.clientList.addClient((String)data[1], (String)data[2], (int)data[3]);
        }
        else if(data[0].equals(Client.REMOVE)){
            ClientFrame.clientList.removeClient((String)data[1]);
        }
    }

    // Authenticate login
    protected void authenticate(){
        try{
            socket = new Socket("localhost", 8000);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(new Object[]{ Connection.LOGIn, name });
            output.flush();
            input  = new ObjectInputStream(socket.getInputStream());

            // Receive authentication message from server
            Object[] data = (Object[])input.readObject();

            if(data[0].equals(Connection.LOGIn)){
                ClientMain.setTitle(name + " connected to server");
                ClientMain.frame.login();
                connected = true;
            }
            else if(data[0].equals(Message.ERROR)){
                ClientMain.frame.displayMessage("Error:\n" + data[1]);
                connected = false;
            }
        } catch(IOException ex){
            ClientMain.frame.displayMessage("Error:\n" + ex.getMessage());
            connected = false;
        } catch (ClassNotFoundException e){
            ClientMain.frame.displayMessage("Error:\n" + e.getMessage());
            connected = false;
        }
    }

    @Override
    public synchronized void sendData(Object[] data){
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
            if(socket != null) socket.close();
        } catch (IOException e){
            ClientMain.frame.displayMessage("Error:\n" + e.getMessage());
        }
    }

}