package de.dominicscheurer.secmail.applet;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import de.dominicscheurer.secmail.model.IdentifierPasswordPair;

// ESCA-JAVA0100:
/**
 * Panel takes identifier and password for key pair.
 * 
 * @author Dominic Scheurer
 */
public class DataInputPanel extends FeedbackGivingPanel<IdentifierPasswordPair> {
    private static final long serialVersionUID = 1L;

    protected static final String SECURE_PWD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,100})";
    
    private JPanel panel;
    private JLabel lblKeyringIdentifier;
    private JLabel lblPassPhrase;
    private JComboBox txtIdentifier;
    private JPasswordField pwdPassword;
    private JTextArea txtrTheKeyIdentifier;
    private JTextArea txtrThePassPhrase;
    private JPanel panelBtnContainer;
    private JButton btnProceed;
    
    private String passPhraseDescrTxt = "";
    private String identDescrTxt = "";
    private String passPhraseLblTxt = "";
    private String keyringLblTxt = "";
    private String passwordInformationTxt;

    private String proceedBtnTxt;

    private String nickName;

    private String realName;
    private JTextArea lblErrorMessage;

    /**
     * Create the panel.
     */
    public DataInputPanel() {
        initialize();
    }
    
    public DataInputPanel(String nickName, String realName) {
        this.nickName = nickName;
        this.realName = realName;
        
        initialize();
    }

    public void initialize() {        
        this.passPhraseDescrTxt = InstallationApplet.getInstance().getMessage("KeyPwdDescr");
        this.identDescrTxt = InstallationApplet.getInstance().getMessage("KeyIdentDescr");
        this.passPhraseLblTxt = InstallationApplet.getInstance().getMessage("KeyPwdLbl");
        this.keyringLblTxt = InstallationApplet.getInstance().getMessage("KeyIdentLbl");
        this.proceedBtnTxt = InstallationApplet.getInstance().getMessage("ProceedBtnTxt");
        this.passwordInformationTxt = InstallationApplet.getInstance().getMessage("PasswordInformationTxt");
        
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(getPanel(), BorderLayout.NORTH);
        getContentPane().add(getLblErrorMessage(), BorderLayout.SOUTH);
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setBackground(Color.WHITE);
            GridBagLayout gbl_panel = new GridBagLayout();
            gbl_panel.columnWidths = new int[]{171, 353, 123, 0};
            gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
            gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
            gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
            panel.setLayout(gbl_panel);
            GridBagConstraints gbc_lblKeyringIdentifier = new GridBagConstraints();
            gbc_lblKeyringIdentifier.anchor = GridBagConstraints.EAST;
            gbc_lblKeyringIdentifier.insets = new Insets(0, 0, 5, 5);
            gbc_lblKeyringIdentifier.gridx = 0;
            gbc_lblKeyringIdentifier.gridy = 0;
            panel.add(getLblKeyringIdentifier(), gbc_lblKeyringIdentifier);
            GridBagConstraints gbc_txtIdentifier = new GridBagConstraints();
            gbc_txtIdentifier.insets = new Insets(0, 0, 5, 5);
            gbc_txtIdentifier.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtIdentifier.gridx = 1;
            gbc_txtIdentifier.gridy = 0;
            panel.add(getTxtIdentifier(), gbc_txtIdentifier);
            GridBagConstraints gbc_txtrTheKeyIdentifier = new GridBagConstraints();
            gbc_txtrTheKeyIdentifier.insets = new Insets(0, 0, 5, 5);
            gbc_txtrTheKeyIdentifier.fill = GridBagConstraints.BOTH;
            gbc_txtrTheKeyIdentifier.gridx = 1;
            gbc_txtrTheKeyIdentifier.gridy = 1;
            panel.add(getTxtrTheKeyIdentifier(), gbc_txtrTheKeyIdentifier);
            GridBagConstraints gbc_lblPassPhrase = new GridBagConstraints();
            gbc_lblPassPhrase.anchor = GridBagConstraints.EAST;
            gbc_lblPassPhrase.insets = new Insets(0, 0, 5, 5);
            gbc_lblPassPhrase.gridx = 0;
            gbc_lblPassPhrase.gridy = 3;
            panel.add(getLblPassPhrase(), gbc_lblPassPhrase);
            GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
            gbc_pwdPassword.insets = new Insets(0, 0, 5, 5);
            gbc_pwdPassword.fill = GridBagConstraints.HORIZONTAL;
            gbc_pwdPassword.gridx = 1;
            gbc_pwdPassword.gridy = 3;
            panel.add(getPwdPassword(), gbc_pwdPassword);
            GridBagConstraints gbc_txtrThePassPhrase = new GridBagConstraints();
            gbc_txtrThePassPhrase.insets = new Insets(0, 0, 5, 5);
            gbc_txtrThePassPhrase.fill = GridBagConstraints.BOTH;
            gbc_txtrThePassPhrase.gridx = 1;
            gbc_txtrThePassPhrase.gridy = 4;
            panel.add(getTxtrThePassPhrase(), gbc_txtrThePassPhrase);
            GridBagConstraints gbc_btnProceed = new GridBagConstraints();
            gbc_btnProceed.insets = new Insets(0, 0, 5, 5);
            gbc_btnProceed.fill = GridBagConstraints.BOTH;
            gbc_btnProceed.gridx = 1;
            gbc_btnProceed.gridy = 5;
            panel.add(getPanelBtnContainer(), gbc_btnProceed);
            getPanelBtnContainer().add(getBtnProceed());
        }
        return panel;
    }
    private JLabel getLblKeyringIdentifier() {
        if (lblKeyringIdentifier == null) {
            lblKeyringIdentifier = new JLabel(keyringLblTxt);
        }
        return lblKeyringIdentifier;
    }
    private JLabel getLblPassPhrase() {
        if (lblPassPhrase == null) {
            lblPassPhrase = new JLabel(passPhraseLblTxt);
        }
        return lblPassPhrase;
    }
    private JComboBox getTxtIdentifier() {
        if (txtIdentifier == null) {
            txtIdentifier = new JComboBox();
            
            if (!realName.isEmpty()) {
                txtIdentifier.addItem(realName);
            }
            
            if (!nickName.isEmpty()) {
                txtIdentifier.addItem(nickName);
            }            
        }
        
        return txtIdentifier;
    }
    private JPasswordField getPwdPassword() {
        if (pwdPassword == null) {
            pwdPassword = new JPasswordField();
        }
        return pwdPassword;
    }
    private JTextArea getTxtrTheKeyIdentifier() {
        if (txtrTheKeyIdentifier == null) {
            txtrTheKeyIdentifier = new JTextArea();
            txtrTheKeyIdentifier.setWrapStyleWord(true);
            txtrTheKeyIdentifier.setRows(2);
            txtrTheKeyIdentifier.setBackground(Color.WHITE);
            txtrTheKeyIdentifier.setEditable(false);
            txtrTheKeyIdentifier.setLineWrap(true);
            txtrTheKeyIdentifier.setText(passPhraseDescrTxt);
        }
        return txtrTheKeyIdentifier;
    }
    private JTextArea getTxtrThePassPhrase() {
        if (txtrThePassPhrase == null) {
            txtrThePassPhrase = new JTextArea();
            txtrThePassPhrase.setWrapStyleWord(true);
            txtrThePassPhrase.setText(identDescrTxt);
            txtrThePassPhrase.setLineWrap(true);
            txtrThePassPhrase.setEditable(false);
            txtrThePassPhrase.setBackground(Color.WHITE);
        }
        return txtrThePassPhrase;
    }
    private JPanel getPanelBtnContainer() {
        if (panelBtnContainer == null) {
            panelBtnContainer = new JPanel();
            panelBtnContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
            panelBtnContainer.setOpaque(false);
        }
        return panelBtnContainer;
    }
    private JButton getBtnProceed() {
        if (btnProceed == null) {
            btnProceed = new JButton();
            btnProceed.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    // First:
                    // Check, if entered values are correct
                    
                    CharBuffer charBuffer = CharBuffer.wrap(pwdPassword.getPassword());                    
                    Pattern pwdPattern = Pattern.compile(SECURE_PWD_REGEX);
                    Matcher pwdMatcher = pwdPattern.matcher(charBuffer);
                    
                    if (!((String)txtIdentifier.getSelectedItem()).isEmpty() &&
                        pwdMatcher.matches()) {
                        giveFeedback(
                                new IdentifierPasswordPair(
                                        ((String)txtIdentifier.getSelectedItem()),
                                        pwdPassword.getPassword()));
                    } else {
                        getLblErrorMessage().setText(passwordInformationTxt);
                    }
                }
            });
            btnProceed.setText(proceedBtnTxt);
        }
        return btnProceed;
    }
    private JTextArea getLblErrorMessage() {
        if (lblErrorMessage == null) {
            lblErrorMessage = new JTextArea("");
            lblErrorMessage.setFont(new Font("Dialog", Font.BOLD, 12));
            lblErrorMessage.setWrapStyleWord(true);
            lblErrorMessage.setLineWrap(true);
            lblErrorMessage.setEditable(false);
            lblErrorMessage.setForeground(Color.RED);
        }
        return lblErrorMessage;
    }
}
