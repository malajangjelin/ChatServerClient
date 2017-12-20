import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by angjelinmalaj on 11/26/17.
 */


public class ClientButton extends JButton implements MouseListener {

    protected String name;
    protected String ip;
    protected int port;

    public ClientButton(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;

        this.setText(name);
        this.setPreferredSize(new Dimension(350, 25));
        addMouseListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e){
        this.setEnabled(false);
        ClientFrame.clientList.addWindow(name, ip, port);
    }
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
}
