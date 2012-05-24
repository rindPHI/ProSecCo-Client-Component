package de.dominicscheurer.secmail.applet;

/**
 * Interface to implement for objects which want to register
 * as feedback recipients with a FeedbackGivingPanel.
 * 
 * @author Dominic Scheurer
 */
public interface FeedbackRecipient<T> {
    void receiveFeedback(T feedback);
}
