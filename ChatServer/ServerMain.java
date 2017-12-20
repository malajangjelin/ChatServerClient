import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ServerMain {

    public static ServerFrame frame;

    // Main
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater( new Runnable(){
            public void run(){
                frame = new ServerFrame();
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setTitle("Chat Server: Server Stopped");
                frame.setSize(600, 400);
                frame.setFocusable(true);
                frame.setResizable(false);
                frame.setVisible(true);

                // Add a listener to the close button and perform custom close operation
                frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        closeOperation();
                    }
                });
            }
        });
    }

    // Close Operation
    public static void closeOperation(){
        // Make sure the connection streams are closed
        frame.server.closeConnection();

        System.exit(0);
    }

    // Getters/Setters
    public static void setTitle(String title){ frame.setTitle("Chat Server: " + title); }

}