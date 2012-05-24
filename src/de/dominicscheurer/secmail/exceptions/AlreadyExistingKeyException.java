package de.dominicscheurer.secmail.exceptions;

import de.dominicscheurer.secmail.applet.InstallationApplet;

/**
 * Encapsulates an exception which is thrown if the key
 * that shall be generated does already exist.
 * 
 * @author Dominic Scheurer
 */
public class AlreadyExistingKeyException extends Exception {
    private static final long serialVersionUID = 1L;

    private String msg;
    
    public AlreadyExistingKeyException() {
        msg = InstallationApplet.getInstance().getMessage("KeyExistingError");
    }

    // ESCA-JAVA0173:
    @SuppressWarnings("unused")
    private AlreadyExistingKeyException(String message) {
    }
    
    @Override
    public String getMessage() {
        return msg;
    }
}
