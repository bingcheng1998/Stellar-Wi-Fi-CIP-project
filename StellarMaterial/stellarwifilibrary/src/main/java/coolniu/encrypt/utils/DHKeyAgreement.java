package coolniu.encrypt.utils;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

// import javax.crypto.Cipher;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 * As part of SSL_DHE_RSA_WITH_DES_CBC_SHA,
 * this program executes the Diffie-Hellman key agreement protocol
 * between 2 parties.
 *
 * By default, preconfigured parameters (1024-bit prime modulus and base
 * generator used by SKIP) are used.
 */

public class DHKeyAgreement {

    /**
     * Key Algorithm
    */
    private static final String KEY_ALGORITHM = "DH";

    // The 1024 bit Diffie-Hellman modulus values used by SKIP
    private static final byte skip1024ModulusBytes[] = {
        (byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58,
        (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD,
        (byte)0x20, (byte)0xB4, (byte)0x9D, (byte)0xE4,
        (byte)0x91, (byte)0x07, (byte)0x36, (byte)0x6B,
        (byte)0x33, (byte)0x6C, (byte)0x38, (byte)0x0D,
        (byte)0x45, (byte)0x1D, (byte)0x0F, (byte)0x7C,
        (byte)0x88, (byte)0xB3, (byte)0x1C, (byte)0x7C,
        (byte)0x5B, (byte)0x2D, (byte)0x8E, (byte)0xF6,
        (byte)0xF3, (byte)0xC9, (byte)0x23, (byte)0xC0,
        (byte)0x43, (byte)0xF0, (byte)0xA5, (byte)0x5B,
        (byte)0x18, (byte)0x8D, (byte)0x8E, (byte)0xBB,
        (byte)0x55, (byte)0x8C, (byte)0xB8, (byte)0x5D,
        (byte)0x38, (byte)0xD3, (byte)0x34, (byte)0xFD,
        (byte)0x7C, (byte)0x17, (byte)0x57, (byte)0x43,
        (byte)0xA3, (byte)0x1D, (byte)0x18, (byte)0x6C,
        (byte)0xDE, (byte)0x33, (byte)0x21, (byte)0x2C,
        (byte)0xB5, (byte)0x2A, (byte)0xFF, (byte)0x3C,
        (byte)0xE1, (byte)0xB1, (byte)0x29, (byte)0x40,
        (byte)0x18, (byte)0x11, (byte)0x8D, (byte)0x7C,
        (byte)0x84, (byte)0xA7, (byte)0x0A, (byte)0x72,
        (byte)0xD6, (byte)0x86, (byte)0xC4, (byte)0x03,
        (byte)0x19, (byte)0xC8, (byte)0x07, (byte)0x29,
        (byte)0x7A, (byte)0xCA, (byte)0x95, (byte)0x0C,
        (byte)0xD9, (byte)0x96, (byte)0x9F, (byte)0xAB,
        (byte)0xD0, (byte)0x0A, (byte)0x50, (byte)0x9B,
        (byte)0x02, (byte)0x46, (byte)0xD3, (byte)0x08,
        (byte)0x3D, (byte)0x66, (byte)0xA4, (byte)0x5D,
        (byte)0x41, (byte)0x9F, (byte)0x9C, (byte)0x7C,
        (byte)0xBD, (byte)0x89, (byte)0x4B, (byte)0x22,
        (byte)0x19, (byte)0x26, (byte)0xBA, (byte)0xAB,
        (byte)0xA2, (byte)0x5E, (byte)0xC3, (byte)0x55,
        (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
    };

    // The SKIP 1024 bit modulus
    private static final BigInteger skip1024Modulus = new BigInteger(1, skip1024ModulusBytes);

    // The base used with the SKIP 1024 bit modulus
    private static final BigInteger skip1024Base = BigInteger.valueOf(2);

    private KeyPair kPair;
    private KeyAgreement keyAgree;
    private DHParameterSpec dhParamSpec;

    /*
    * Server side to send out key and verify Diffie-Hellman key agreement protocol
    */
    public DHKeyAgreement(boolean bGenerate) throws Exception {
        
        if (bGenerate) {
            // Some central authority creates new DH parameters
            AlgorithmParameterGenerator paramGen = 
                AlgorithmParameterGenerator.getInstance(KEY_ALGORITHM);
            paramGen.init(1024);
            AlgorithmParameters params = paramGen.generateParameters();
            dhParamSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
        } else {
            // use some pre-generated, default DH parameters
            dhParamSpec = new DHParameterSpec(skip1024Modulus, skip1024Base);
        }

        init();
    }

    /*
    * Client side to response and verify Diffie-Hellman key agreement protocol
    */
    public DHKeyAgreement(BigInteger p, BigInteger g) throws Exception {
        dhParamSpec = new DHParameterSpec(p, g);
        init();
    }

    public DHKeyAgreement(byte[] p, byte[] g) throws Exception {
    	// Translates the sign-magnitude representation of a BigInteger into a BigInteger.
    	//  The sign is represented as an integer signum value: -1 for negative, 0 for zero, or 1 for positive.
        dhParamSpec = new DHParameterSpec(new BigInteger(1, p), new BigInteger(1, g));
        init();
    }

    public DHKeyAgreement(byte[] peerPublicKeyEncode) throws Exception {
        PublicKey peerPubKey = retrievePublicKey(peerPublicKeyEncode);

        // User peer's public key to initialize self objects.
        dhParamSpec = ((DHPublicKey) peerPubKey).getParams();
        init();

        // accept peer public key
        keyAgree.doPhase(peerPubKey, true);
    }

    /*
    * Initialize the key pair and KeyAgreement object
    */
    private void init() throws Exception {
        // Creates own DH key pair using the DH parameters
        KeyPairGenerator kPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        kPairGen.initialize(dhParamSpec);
        kPair = kPairGen.generateKeyPair();

        // Initializes DH KeyAgreement object
        keyAgree = KeyAgreement.getInstance(KEY_ALGORITHM);
        keyAgree.init(kPair.getPrivate());
    }

    /*
    * Return the prime modulus p.
    */
    public BigInteger getP() {
        return dhParamSpec.getP();
    }
    
    public byte[] getPArray() {
    	return bigIntToArray(getP());
    }

    /**
     * Return the base generator g.
     */
    public BigInteger getG() {
        return dhParamSpec.getG();
    }

    public byte[] getGArray() {
    	return bigIntToArray(getG());
    }

    /**
     * Return the base generator g.
     */
    public BigInteger getY() {
        return ((DHPublicKey)kPair.getPublic()).getY();
    }
    
    public byte[] getYArray() {
    	return bigIntToArray(getY());
    }    
    
    /**
     * toByteArray() will returns a byte[] containing the two's-complement 
     * representation of this BigInteger. 
     * So there are two cases: 
     * 		left-most byte is 0x00 or not. If it is 0x00 we can safely drop it.
     * @param value
     * @return
     */
    private byte[] bigIntToArray(BigInteger value) {
    	byte[] array = value.toByteArray();
    	if (array[0] == 0) {
    	    byte[] tmp = new byte[array.length - 1];
    	    System.arraycopy(array, 1, tmp, 0, tmp.length);
    	    array = tmp;
    	}
    	return array;
    }

    /*
    * Encodes self public key
    */
    public byte[] getPublicKey() {
        return kPair.getPublic().getEncoded();
    }

    /*
    * Uses peer's public key for the first (and only) phase
    * of current version of the DH protocol.
    */
    public void acceptPeerPublicKey(byte[] peerPubKeyEnc) throws Exception {
        PublicKey peerPubKey = retrievePublicKey(peerPubKeyEnc);
        keyAgree.doPhase(peerPubKey, true);
    }

    /*
    * Uses peer's public value Y for the first (and only) phase
    * of current version of the DH protocol.
    */
    public void acceptPeerY(byte[] peerY) throws Exception {
        acceptPeerY(new BigInteger(1, peerY));
    }

    /*
    * Uses peer's public value Y for the first (and only) phase
    * of current version of the DH protocol.
    */
    public void acceptPeerY(BigInteger peerY) throws Exception {
        PublicKey peerPubKey = retrievePublicKey(peerY);
        keyAgree.doPhase(peerPubKey, true);
    }

    /*
    * At this stage, both server and client have completed the DH key
    * agreement protocol.
    * Generate the (same) shared secret.
    */
    public byte[] generateSecret() throws Exception {
        return keyAgree.generateSecret();
    }

    /*
    * Instantiates a DH public key from the encoded key material
    */
    private PublicKey retrievePublicKey(byte[] pubKeyEnc) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyEnc);
        return keyFac.generatePublic(x509KeySpec);
    }

    /*
    * At this stage, self parameter must have been initialized.
    * Instantiates a DH public key from public Y and local P, G
    */
    private PublicKey retrievePublicKey(BigInteger Y) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance(KEY_ALGORITHM);
        DHPublicKeySpec publicKeySpec = new DHPublicKeySpec(Y, getP(), getG());
        return keyFac.generatePublic(publicKeySpec); 
    }
}