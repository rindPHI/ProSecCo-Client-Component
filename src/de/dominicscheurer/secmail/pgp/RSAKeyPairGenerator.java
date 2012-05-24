package de.dominicscheurer.secmail.pgp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import de.dominicscheurer.secmail.exceptions.AlreadyExistingKeyException;

/**
 * Class to create a pair of RSA keys for PGP usage.
 * 
 * @author Dominic Scheurer
 */
public class RSAKeyPairGenerator {
    private static final int KEY_LENGTH = 2048;
    private static final String KEY_FOLDER = "/.secmail";
    private static final boolean ARMOR = true;

    private RSAKeyPairGenerator() {
    }

    /**
     * Saves a key pair to some output streams.
     * 
     * @param secretOut
     *            Output stream to save the privateKey
     * @param publicOut
     *            Output stream to save the publicKey
     * @param publicKey
     *            The public key
     * @param privateKey
     *            The private Key
     * @param identity
     *            The identifier for which the key pair was created for (e.g.
     *            "Barrack Obama")
     * @param passPhrase
     *            A pass phrase to protect the private key with
     * @throws IOException
     * @throws PGPException
     */
    private static void exportKeyPair(
            OutputStream secretOut,
            OutputStream publicOut,
            PublicKey publicKey,
            PrivateKey privateKey,
            String identity,
            char[] passPhrase)
            throws IOException,
            PGPException {
        if (ARMOR) {
            secretOut = new ArmoredOutputStream(secretOut);
        }

        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder()
                .build()
                .get(HashAlgorithmTags.SHA1);
        @SuppressWarnings("deprecation")
        PGPKeyPair keyPair = new PGPKeyPair(
                PGPPublicKey.RSA_GENERAL,
                publicKey,
                privateKey,
                new Date());
        PGPSecretKey secretKey = new PGPSecretKey(
                PGPSignature.DEFAULT_CERTIFICATION,
                keyPair,
                identity,
                sha1Calc,
                null,
                null,
                new JcaPGPContentSignerBuilder(keyPair
                        .getPublicKey()
                        .getAlgorithm(), HashAlgorithmTags.SHA1),
                new JcePBESecretKeyEncryptorBuilder(
                        PGPEncryptedData.CAST5,
                        sha1Calc).setProvider("BC").build(passPhrase));
        
        Arrays.fill(passPhrase, '\0'); // Zero-out password

        secretKey.encode(secretOut);

        secretOut.close();

        if (ARMOR) {
            publicOut = new ArmoredOutputStream(publicOut);
        }

        PGPPublicKey key = secretKey.getPublicKey();

        key.encode(publicOut);

        publicOut.close();
    }

    /**
     * Removes / Replaces certain characters in identity string for the purpose
     * of file name generation.
     * 
     * @param identityString
     *            The identifier to normalize.
     * @return A normalized Version of the identity String, not containing empty
     *         characters and other non-alphanumeric characters except for "_".
     */
    private static String normalizeIdentity(String identityString) {
        return identityString
                .replaceAll(" ", "_")
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase();
    }

    /**
     * Generates a PGP key pair for a given identity with a given pass phrase
     * for the private key. Saves the keys to a default location and uses the
     * identifier to assign names to the keys.
     * 
     * @param identity
     *            The owner of the keys.
     * @param passPhrase
     *            The pass phrase to protect the private key with.
     * @return Path to public key.
     * 
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws IOException
     * @throws PGPException
     * @throws AlreadyExistingKeyException 
     */
    public static String generateKeyPair(String identity, char[] passPhrase)
            throws NoSuchAlgorithmException,
            NoSuchProviderException,
            IOException,
            PGPException, AlreadyExistingKeyException {
        // Pre-compute paths
        String identityNormalized = normalizeIdentity(identity);
        String homeDir = System.getProperty("user.home");
        String secKeyDestination = homeDir + KEY_FOLDER + "/"
                + identityNormalized + "_secret.asc";
        String pubKeyDestination = homeDir + KEY_FOLDER + "/"
                + identityNormalized + "_pub.asc";

        // Create key folder if necessary
        File keyFolder = new File(homeDir + KEY_FOLDER + "/");
        if (!keyFolder.exists()) {
            keyFolder.mkdir();
        }

        if (new File(secKeyDestination).exists()
                || new File(pubKeyDestination).exists()) {
            // At least one of the files exists => Don't create anything
            throw new AlreadyExistingKeyException();
        }

        // Create key pair
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(KEY_LENGTH);
        KeyPair kp = kpg.generateKeyPair();

        // Output key pair
        FileOutputStream secretOut = new FileOutputStream(secKeyDestination);
        FileOutputStream publicOut = new FileOutputStream(pubKeyDestination);

        exportKeyPair(
                secretOut,
                publicOut,
                kp.getPublic(),
                kp.getPrivate(),
                identity,
                passPhrase);
        
        Arrays.fill(passPhrase, '\0'); // Zero-out password

        // Success
        return pubKeyDestination;
    }

//    public static void main(String[] args)
//            throws NoSuchAlgorithmException,
//            NoSuchProviderException,
//            IOException,
//            PGPException {
//        System.out.println(generateKeyPair("Dominic Scheurer", "passphrase"));
//    }
}