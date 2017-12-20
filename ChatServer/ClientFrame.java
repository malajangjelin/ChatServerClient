import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ClientFrame extends JFrame implements MouseListener {

    protected JPanel loginPanel;
    protected JTextField nameTF;
    protected JButton connectBtn;
    protected JPanel westPanel;
    protected JPanel eastPanel;
    public static ClientServerHandler server;
    public static ClientList clientList;

    public ClientFrame(){
        this.setLayout(new BorderLayout());

        // Username Label
        JLabel nameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 14));

        // Username Text Field
        nameTF = new JTextField();
        nameTF.setHorizontalAlignment(JTextField.CENTER);
        nameTF.setPreferredSize(new Dimension(190, 25));

        // Connect Button
        connectBtn = new JButton("Connect to server");
        connectBtn.setPreferredSize(new Dimension(140, 25));
        connectBtn.addMouseListener(this);

        // Login Panel
        this.loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(Box.createRigidArea(new Dimension(200,200)));
        loginPanel.add(nameLabel);
        loginPanel.add(nameTF);
        loginPanel.add(connectBtn);
        loginPanel.add(Box.createRigidArea(new Dimension(200,400)));

        // List of connected clients
        clientList = new ClientList();
        clientList.setVisible(false);

        // Empty JPanel to pad the layout
        JPanel padPanel = new JPanel();
        padPanel.setBackground(Color.WHITE);
        padPanel.setPreferredSize(new Dimension(100, 100));

        // West and East JPanels are for layout padding
        this.westPanel = getPadJPanel();
        this.eastPanel = getPadJPanel();

        // Add the components to the frame
        add(loginPanel, BorderLayout.CENTER);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        add(clientList, BorderLayout.NORTH);
    }

    protected JPanel getPadJPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(100, 100));
        return panel;
    }

    // Hide the login and show the list of clients
    public void login(){
        loginPanel.setVisible(false);
        westPanel.setVisible(false);
        eastPanel.setVisible(false);
        clientList.setVisible(true);
    }

    public void logout(){
        ClientMain.setTitle("Chat Client: Not connected to server");
        loginPanel.setVisible(true);
        westPanel.setVisible(true);
        eastPanel.setVisible(true);
        clientList.setVisible(false);
    }

    // Display a Dialog Message
    public void displayMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void mousePressed(MouseEvent e){
        Object source = e.getSource();

        // Validate that a username has been entered
        if(source.equals(connectBtn)){
            String name = nameTF.getText().trim();
            if(name.length() < 1){
                displayMessage("A username must be entered!");
            }
            else{
                server = new ClientServerHandler(name);
                nameTF.setText("");
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){}
}