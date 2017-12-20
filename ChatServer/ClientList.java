import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ClientList extends JPanel {

    protected JLabel noClientMsg;
    protected HashMap clients;
    protected HashMap windows;

    public ClientList(){
        this.setPreferredSize(new Dimension(400, 600));
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(Color.WHITE);
        this.clients = new HashMap();
        this.windows = new HashMap();

        // No Client Message is displayed when no other clients are loged in
        this.noClientMsg = new JLabel("No other clients are connected.");
        noClientMsg.setHorizontalAlignment(JLabel.CENTER);
        noClientMsg.setFont(new Font("Dialog", Font.BOLD, 14));

        add(noClientMsg);
    }

    public void addClient(String name, String ip, int port){
        // Hide the no clients message
        if(clients.size() < 1) remove(noClientMsg);

        // Create a new client button
        ClientButton client = new ClientButton(name, ip, port);

        // Add the client to the hash map
        clients.put(name, client);

        // Add the client to the JPanel
        add(client);

        System.out.println(clients.size());

        // Refresh the UI
        revalidate();
        repaint();
    }

    public void removeClient(String name){
        // Remove the client button
        ClientButton client = (ClientButton) clients.get(name);
        if(client != null){
            remove(client);

            // Remove the client from the hash map
            clients.remove(client);

            // show the no clients message
            if(clients.size() < 1){
                add(noClientMsg);
            }
        }

        // Dispose the client window
        ClientWindow window = (ClientWindow) windows.get(name);
        if(window != null){
            window.closeOperation(name);
        }

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Add window creates a new client message window
     * and gets added to a hash map.
     * @param name
     * @param ip
     * @param port
     */
    public void addWindow(String name, String ip, int port){
        // Create a new client window
        ClientWindow window = new ClientWindow(name, ip, port);

        // Add the window to the hash map
        windows.put(name, window);
    }

    /**
     * Remove client message window from the hash map
     * and then dispose the window.
     * @param name
     */
    public void removeWindow(String name){

        windows.remove(name);

        // If the client is still connected then re-enable the button
        ClientButton client = (ClientButton) clients.get(name);
        if(client != null){
            client.setEnabled(true);
        }
    }

    /**
     * Get the client window and add a message received from
     * the server.
     * @param name
     * @param message
     */
    public void addMessage(String name, String message){

        // Get the client window or create one
        ClientWindow client = (ClientWindow) windows.get(name);
        if(client != null){
            // Add the message to the client window
            client.addMessage(name, message);
        }
        else{
            // The client window does not exist so create a new window
            ClientButton clientBtn = (ClientButton) clients.get(name);
            if(clientBtn != null){
                clientBtn.setEnabled(false);
                addWindow(clientBtn.name, clientBtn.ip, clientBtn.port);
                client = (ClientWindow) windows.get(name);
                client.addMessage(name, message);
            }
        }
    }
}