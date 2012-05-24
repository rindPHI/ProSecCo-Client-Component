package de.dominicscheurer.secmail.applet;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A panel including a text field with vertically and horizontally
 * centered contents.
 * 
 * @author Dominic Scheurer
 */
public class CenteredTextPanelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel lblPleaseWaitWhile;

    /**
     * Create the panel.
     */
    public CenteredTextPanelPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 0));
        add(getLblPleaseWaitWhile(), BorderLayout.CENTER);
    }
    
    public CenteredTextPanelPanel(String text) {
        this(text, Color.BLACK);
    }
    
    public CenteredTextPanelPanel(String text, Color color) {
        this();
        
        text = "<html>" + text + "</html>";
        text = text.replace("\n", "<br>");
        
        getLblPleaseWaitWhile().setText(text);
        getLblPleaseWaitWhile().setForeground(color);
    }

    private JLabel getLblPleaseWaitWhile() {
        if (lblPleaseWaitWhile == null) {
            lblPleaseWaitWhile = new JLabel("Please wait while your keys are being generated");
            lblPleaseWaitWhile.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return lblPleaseWaitWhile;
    }
}
