package de.dominicscheurer.secmail.applet;

import java.util.ArrayList;

import javax.swing.JApplet;

// ESCA-JAVA0011:
/**
 * Simple JApplet which allows instantiating objects to register as feedback
 * recipients.
 * 
 * @author Dominic Scheurer
 */
public abstract class FeedbackGivingPanel<T> extends JApplet {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<FeedbackRecipient<T>> recipients = new ArrayList<FeedbackRecipient<T>>(
            2);

    public void register(FeedbackRecipient<T> recipient) {
        this.recipients.add(recipient);
    }

    protected void giveFeedback(final T feedback) {
        new Thread() {            
            @Override
            public void run() {
                for (FeedbackRecipient<T> recipient : recipients) {
                    recipient.receiveFeedback(feedback);
                }
            }
        }.start();
    }
}
