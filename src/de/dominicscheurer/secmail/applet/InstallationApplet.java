package de.dominicscheurer.secmail.applet;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import org.bouncycastle.openpgp.PGPException;

import de.dominicscheurer.secmail.exceptions.AlreadyExistingKeyException;
import de.dominicscheurer.secmail.net.HttpsClient;
import de.dominicscheurer.secmail.pgp.RSAKeyPairGenerator;

// ESCA-JAVA0137:
/**
 * Handles the key creation and installation process for
 * secmail. 
 * 
 * @author Dominic Scheurer
 */
public class InstallationApplet extends JApplet {
    private static final String KEY_UPLOAD_PAGE = "https://dominic-scheurer.dyndns.org/index.html";

    private static final long serialVersionUID = 1L;
    
    private static InstallationApplet instance;
    
    private Locale currentLocale = Locale.getDefault();
    private String nickName = "predator", realName = "Alien A. Alienus";

    private ResourceBundle bundle;

    // Called when this applet is loaded into the browser.
    public void init() {
        instance = this;
        
        // Read parameters
        nickName = getParameter("nickName");
        realName = getParameter("realName");
        String localeParameter = getParameter("locale");
        currentLocale = localeParameter != null && !getParameter("locale").isEmpty() ?
                new Locale(localeParameter) : Locale.getDefault();
        

        initMessagesBundle();
                
        // Execute a job on the event-dispatching thread; creating this applet's
        // GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (InterruptedException e) {
            handleException(e);
        } catch (InvocationTargetException e) {
            handleException(e);
        }
    }

    private void createGUI() {
        // Create and set up the content pane.
        FeedbackGivingPanel<String[]> dataInputPanel = new DataInputPanel(nickName, realName);
        setContentPane(dataInputPanel);
        dataInputPanel.register(new FeedbackRecipient<String[]>() {
            @Override
            public void receiveFeedback(String[] feedback) {
                recvDataInputPanelFeedback(feedback[0], feedback[1]);
            }
        });
    }

    private void recvDataInputPanelFeedback(String identifier, String password) {
        try {
            setContentPane(new CenteredTextPanelPanel(
                    getMessage("KeysBeingGen")));
            validate();

            String publicKeyPath = RSAKeyPairGenerator.generateKeyPair(identifier, password);
            
            setContentPane(new CenteredTextPanelPanel(
                    getMessage("Uploading")));
            validate();
            
            HashMap<String, String> params = new HashMap<String, String>(2);
            params.put("nickName", nickName);
            
            HttpsClient.uploadFile(KEY_UPLOAD_PAGE, new File(publicKeyPath));
            
            setContentPane(new CenteredTextPanelPanel(
                    getMessage("Congratulations")));
            validate();

        } catch (NoSuchAlgorithmException e) {
            handleException(e);
        } catch (NoSuchProviderException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        } catch (PGPException e) {
            handleException(e);
        } catch (KeyManagementException e) {
            handleException(e);
        } catch (UnrecoverableKeyException e) {
            handleException(e);
        } catch (KeyStoreException e) {
            handleException(e);
        } catch (CertificateException e) {
            handleException(e);
        } catch (AlreadyExistingKeyException e) {
            handleException(e);
        } catch (URISyntaxException e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        setContentPane(new CenteredTextPanelPanel(
                getMessage("SorryError") +
                "\n" +
                e.getMessage(), Color.RED));
        validate();
    }

    public String  getMessage(String key) {
        return bundle.getString(key);
    }
    
    private void initMessagesBundle() {
        bundle = ResourceBundle.getBundle(
                "Messages",
                currentLocale);
        if (bundle == null) {
            handleException(new RuntimeException("Cannot read messages bundle file."));
        }
    }

    public static InstallationApplet getInstance() {
        return instance;
    }
}
