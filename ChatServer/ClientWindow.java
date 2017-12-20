import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ClientWindow extends JFrame implements MouseListener {

    protected JTextArea textArea;
    protected JTextField input;
    protected JButton sendBtn;
    protected String name;
    protected String ip;
    protected int port;

    public ClientWindow(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;

        this.setTitle(name + " IP: " + ip + " Port: " + port);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Add a listener to the close button and perform custom close operation
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                closeOperation(name);
            }
        });

        // Text Area
        textArea = new JTextArea(15, 50);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(Font.getFont(Font.SANS_SERIF));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Scroll Pane
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Input
        input = new JTextField(20);
        input.requestFocus();
        sendBtn = new JButton("Send Message");
        sendBtn.addMouseListener(this);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(input);
        inputPanel.add(sendBtn);

        // Add to container
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(true);
        container.add(scroller);
        container.add(inputPanel);

        this.getContentPane().add(BorderLayout.CENTER, container);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setResizable(false);
    }

    // Close Operation
    public void closeOperation(String name){
        ClientFrame.clientList.removeWindow(name);
        this.dispose();
    }

    public void addMessage(String name, String message){
        textArea.append(name + ":\n" + message + "\n\n");
    }

    @Override
    public void mousePressed(MouseEvent e){
        Object source = e.getSource();

        if(source.equals(sendBtn)){
            // Get the message
            String message = input.getText().trim();
            if(message.length() > 0){
                // Send the message to the other client
                ClientFrame.server.sendData(new Object[]{ ServerInterface.Message.PRIVATE, name, message });

                // Add the message
                addMessage(ClientFrame.server.name, message);

                // Reset the input field
                input.setText("");
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
}