package de.dominicscheurer.secmail.model;

import java.util.Arrays;

/**
 * Encapsulates an identifier-password pair, ensuring that the
 * password is overwritten with zero bytes after termination
 * of the program.
 * 
 * @author Dominic Scheurer
 */
public class IdentifierPasswordPair {
    private String identifier;
    private char[] password;
    
    public IdentifierPasswordPair(String identifier, char[] password) {
        this.identifier = identifier;
        this.password = password.clone();
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public char[] getPassword() {
        return password.clone();
    }

    public void setPassword(char[] password) {
        this.password = password.clone();
    }    
    
    @Override
    protected void finalize() throws Throwable {        
        Arrays.fill(password, '\0'); // Zero-out password

        super.finalize();
    }
}
