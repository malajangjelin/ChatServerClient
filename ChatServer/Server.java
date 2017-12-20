import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Server implements ServerInterface {

    private int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    public  static ThreadPool threadPool;

    public Server(){
        this.port = 8000;
        this.socket = null;
        this.serverSocket = null;
        this.threadPool = new ThreadPool();

        run();
    }

    // Listen for new client connections
    @Override
    public void run(){
        new Thread(()->{
            try{
                // Create New Server Socket
                serverSocket = new ServerSocket(port);
                serverSocket.getInetAddress();

                // Server Started Message
                InetAddress inetAddress = InetAddress.getLocalHost();
                String address = "IP: " + inetAddress.getHostAddress() + " Port: " + port;
                ServerMain.frame.appendMessage("Server started at " + address + "\n", Color.WHITE);
                ServerMain.frame.appendMessage(ServerMain.frame.getDate() + "\n\n", Color.WHITE);
                ServerMain.setTitle(address);

                while(true){
                    // Create New Socket
                    socket = serverSocket.accept();

                    // Create New Output/Input Stream
                    output  = new ObjectOutputStream(socket.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(socket.getInputStream());

                    // Listen for messages
                    try{
                        Object[] data = (Object[]) input.readObject();

                        // Handle the message
                        handleMessage(data);

                    }catch(ClassNotFoundException e){
                        ServerMain.frame.appendMessage("Object class not found error: \n" + e.getMessage() + "\n", Color.WHITE);
                        ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);
                    }
                }
            }catch(IOException ex){
                ServerMain.frame.appendMessage("I/O Exception error: \n" + ex.getMessage() + "\n", Color.WHITE);
                ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);
            }
        }).start();
    }

    // Handle Messages
    @Override
    public synchronized void handleMessage(Object[] data){
        if(data[0].equals(Connection.LOGIn)){
            clientLogin((String)data[1]);
        }
    }

    protected void clientLogin(String name){
        // Get client IP and Port
        String ip = "" + socket.getInetAddress();
        ip = ip.substring(1);
        int port = socket.getPort();

        // Check if the client name is already in use
        ServerClient client = threadPool.getClient(name);
        if(client != null){
            ServerMain.frame.appendMessage("Client at IP: " + ip + " Port: " + port +
                    ", attempted to connect with a name (" + name + ") that is already logged in.\n", Color.WHITE);
            ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);

            // Send error message to client
            sendData(new Object[]{ Message.ERROR, "Unable to connect.\nUsername: " + name + " is already in use!" });
        }
        else{
            // Send message to client authenticating login
            sendData(new Object[]{ Connection.LOGIn });

            // Instantiate a new server client thread
            client = new ServerClient(output, input, name, port, ip);

            // Add new client thread to the thread pool to maintain the connection
            threadPool.put(name, client);

            // Send message to add client to all other clients
            threadPool.sendToAllMinusSender(name, new Object[]{ Client.ADD, name, ip, port });

            // Retrieve all connected clients for the new client
            threadPool.getConnectedClients(client);

            // Client connected successfully
            ServerMain.frame.appendMessage("Client (" + name + ") connected at IP: " + ip + " Port: " + port + "\n",Color.WHITE);
            ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);
        }
    }

    /**
     * Send new client connection messages.
     * @param data
     */
    @Override
    public synchronized void sendData(Object[] data){
        try{
            output.writeObject(data);
            //Thats where I print out
            output.flush();
        } catch (IOException e){
            ServerMain.frame.appendMessage("Send message error: " + e.getMessage() + "\n", Color.WHITE);
            ServerMain.frame.appendMessage(ServerFrame.getDate() + "\n\n", Color.WHITE);
        }
    }

    /**
     * Send a message to remove a client from all active clients list.
     */
    public void removeClient(String name){
        threadPool.sendToAllMinusSender(name, new Object[] { Client.REMOVE, name });
    }

    // Remove a client from the thread pool
    public void removeClientThread(String name){ threadPool.remove(name); }

    public synchronized void sendMessage(String fromClient, String toClient, String message){
        ServerClient clientThread = threadPool.getClient(toClient);
        clientThread.sendData(new Object[]{ Message.PRIVATE, fromClient, message });
    }

    /**
     * Close all client threads and clear the thread pool.
     */
    @Override
    public void closeConnection(){
        // Remove all threads in the pool
        threadPool.clear();

        try{
            if(output != null) output.close();
            if(input != null) input.close();
            if(socket != null) socket.close();
            if(serverSocket != null) serverSocket.close();
        } catch(IOException e){
            ServerMain.frame.appendMessage("Error: Unable to close server. " + '\n' + e.getMessage() + "\n", Color.WHITE);
            ServerMain.frame.appendMessage("" + ServerFrame.getDate() + "\n\n", Color.WHITE);
        }
    }

}