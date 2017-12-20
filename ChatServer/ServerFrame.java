import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ServerFrame extends JFrame {

    protected JTextPane textPane;
    public static Server server;

    public ServerFrame(){
        // Text Pane to display messages
        this.textPane = new JTextPane();
        textPane.setBackground(Color.BLACK);
        textPane.setFont(new Font("Dialog", Font.PLAIN, 16));
        textPane.setDoubleBuffered(true);
        textPane.setEditorKit(new WrapEditorKit());
        textPane.setEditable(false);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        // Add the components to the frame
        add(scrollPane);

        // Server
        this.server = new Server();
    }

    // Append Text To JTextPane
    public void appendMessage(String message, Color color){
        textPane.setEditable(true);
        SimpleAttributeSet attribute = new SimpleAttributeSet();
        StyleConstants.setForeground(attribute, color);

        int length = textPane.getDocument().getLength();
        textPane.setCaretPosition(length);
        textPane.setCharacterAttributes(attribute, false);
        textPane.replaceSelection(message);
        textPane.setEditable(false);
    }

    // Wrap Editor Kit forces text to wrap to the next line
    public class WrapEditorKit extends StyledEditorKit {
        ViewFactory defaultFactory = new WrapColumnFactory();

        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

        // Wrap Column Factory
        private class WrapColumnFactory implements ViewFactory {
            public View create(Element elem) {
                String kind = elem.getName();
                if (kind != null) {
                    if (kind.equals(AbstractDocument.ContentElementName)) {
                        return new WrapLabelView(elem);
                    } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                        return new ParagraphView(elem);
                    } else if (kind.equals(AbstractDocument.SectionElementName)) {
                        return new BoxView(elem, View.Y_AXIS);
                    } else if (kind.equals(StyleConstants.ComponentElementName)) {
                        return new ComponentView(elem);
                    } else if (kind.equals(StyleConstants.IconElementName)) {
                        return new IconView(elem);
                    }
                }
                return new LabelView(elem);
            }
        }

        // Wrap Label View
        private class WrapLabelView extends LabelView {
            public WrapLabelView(Element elem){ super(elem); }
            public float getMinimumSpan(int axis){
                switch (axis) {
                    case View.X_AXIS:
                        return 0;
                    case View.Y_AXIS:
                        return super.getMinimumSpan(axis);
                    default:
                        throw new IllegalArgumentException("Invalid axis: " + axis);
                }
            }
        }
    }

    // Format the date and time
    public static String getDate(){
        Format formatter = new SimpleDateFormat("hh:mm a EEEE MMMM d, YYYY");
        return formatter.format(new Date());
    }

}