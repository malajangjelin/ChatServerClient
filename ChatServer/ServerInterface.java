import java.net.ServerSocket;

/**
 * Server/Client chat messenger protocol.
 * Enums are used as the first index in an Object[](data) to determine how to handle the message.
 */
public interface ServerInterface {

    /**
     * data[0] = enum Connection
     * data[1] = (String)name of client
     */
    public static enum Connection { LOGIn };

    /**
     * data[0] = enum Client
     * data[1] = (String)name of client
     * data[2] = (ADD ONLY) client ip
     * data[3] = (ADD ONLY) client port
     */
    public static enum Client { ADD, REMOVE };

    /**
     * data[0] = enum Message
     * data[1] = (String)client name message is going to
     * data[2] = (String)message
     */
    public static enum Message { PRIVATE, ERROR }

    // Runnable
    public void run();

    // Handle Messages
    public void handleMessage(Object[] data);

    // Send the Object[] to the client.
    public void sendData(Object[] data);

    // Cleanup and close the streams and sockets.
    public void closeConnection();

}