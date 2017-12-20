import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ClientMain {
    public static ClientFrame frame;

    // Main
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater( new Runnable(){
            public void run(){
                frame = new ClientFrame();
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setTitle("Chat Client: Not connected to server");
                frame.setSize(400, 600);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setFocusable(true);
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
        if(frame.server != null){
            frame.server.connected = false;
            frame.server.closeConnection();
        }
        System.exit(0);
    }

    // Getters/Setters
    public static void setTitle(String title){ frame.setTitle(title); }
}