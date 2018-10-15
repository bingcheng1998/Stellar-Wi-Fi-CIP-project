package coolniu.encrypt.utils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA security coder component
 * 
 * @author Chris Liu
 * @version 1.0
 * @since 1.0
 */
public class RSACoder extends Coder {
    public static final String KEY_ALGORITHM = "RSA";

    // SHA1withDSA /  SHA1withRSA / SHA256withRSA / MD5withRSA
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";    

    // Predefined the RSA key
    private static String modulusString = "00d56047acf652298e3fcdbb8cecbc32214722aa1625f88480cf570cee373ada932b140c29b00dc44f6e59e7018dddca66b2f1c645dacb9d4a45459cfa8f7e33df";
    private static String exponentString = "18bc01730656bde47476f7cfbd3d8f9e15ede9c389814672dc161e349b08627fc885fe9d2442ae92f0214c7e97cf0b9a9fc876df4f53517ab63d710f997b2779";
    private static String publicExponentString = "010001";

    // Stellar provide client modulus
    //private static String clientModulusString = "DE6DC0A7B61E8B2E71C7A7715280837838E0E9852F510CB2A502B4F040EE3608A36524F1D9E42229A73BFF678DFA79425891A1770D4B8C2153D7826196727389F764EAA6072563185D4632FBE878D5F7440F77D36A4B7B596A9A38A5C3E9F3F9DC1BB472EAE93D5CC5FE7FF514B0140732FC666E1E3F0C05555E738236662883";
    //private static String clientPublicExponentString = "010001";

    private RSAPublicKey publicKey = null;
    private RSAPrivateKey privateKey = null;

    public static RSACoder getServerInstance(boolean bGenerate) throws Exception {
        RSACoder coder = new RSACoder();
        coder.initServerKey(bGenerate);
        return coder;
    }

    public static RSACoder getClientInstance() throws Exception {
        RSACoder coder = new RSACoder();
        coder.initPublicKey();
        return coder;
    }

    public static RSACoder getClientInstance(
        String strModulus, String strPubExponent) throws Exception {
        RSACoder coder = new RSACoder();
        coder.initPublicKey(strModulus, strPubExponent);
        return coder;        
    }

    public static RSACoder getClientInstance(
        BigInteger modulus, BigInteger pubExponent) throws Exception {
        RSACoder coder = new RSACoder();
        coder.initPublicKey(modulus, pubExponent);
        return coder;
    }

    private RSACoder() throws Exception {}

    /**
     * Get Private Key
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public PrivateKey getPrivateKey() throws Exception {
        return (PrivateKey) privateKey;
    }

    public byte[] getPrivateKeyEncoded() throws Exception {
        return privateKey.getEncoded();
    }

    /**
     * Get Public Key
     * 
     * @param keyMap
     * @return
     * @throws Exception
     */
    public PublicKey getPublicKey() throws Exception {
        return (PublicKey) publicKey;
    }

    public byte[] getPublicKeyEncoded() throws Exception {
        return publicKey.getEncoded();
    }

    public BigInteger getModulus() throws Exception {
        return publicKey.getModulus();
    }

    public BigInteger getPublicExponent() throws Exception {
        return publicKey.getPublicExponent();
    }

    /**
     * Server: initialize secret key
     * 
     * @throws Exception
     */
    private void initServerKey(boolean bGenerate) throws Exception {
        if (bGenerate) {
            KeyPairGenerator keyPairGen = 
                KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);

            KeyPair keyPair = keyPairGen.generateKeyPair();

            publicKey = (RSAPublicKey) keyPair.getPublic();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
        } else {
            // Load the key into BigIntegers
            BigInteger modulus = new BigInteger(modulusString, 16);
            BigInteger exponent = new BigInteger(exponentString, 16);
            BigInteger pubExponent = new BigInteger(publicExponentString, 16);

            // Create private and public key specs
            RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modulus, exponent);
            RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, pubExponent);

            // Create a key factory
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

            // Create the RSA private and public keys
            privateKey = (RSAPrivateKey)factory.generatePrivate(privateSpec);
            publicKey = (RSAPublicKey)factory.generatePublic(publicSpec);
        }
    }

    /**
     * Client: initialize public key with predefined value
     * @param strModulus    hex string format
     * @param strPubExponent hex string format
     * @throws Exception
     */
    private void initPublicKey() throws Exception {
        initPublicKey(modulusString, publicExponentString);
    }

    /**
     * Client: initialize public key
     * @param strModulus    hex string format
     * @param strPubExponent hex string format
     * @throws Exception
     */
    private void initPublicKey(
            String strModulus, String strPubExponent) throws Exception {
        // Load the key into BigIntegers
        BigInteger modulus = new BigInteger(strModulus, 16);
        BigInteger pubExponent = new BigInteger(publicExponentString, 16);
        initPublicKey(modulus, pubExponent);
    }

    /**
     * Client: initialize public key
     * 
     * @throws Exception
     */
    private void initPublicKey(
            BigInteger modulus, BigInteger pubExponent) throws Exception {
        // Create public key specs
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, pubExponent);
                        
        // Create a key factory
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
                        
        // Create the RSA private and public keys
        publicKey = (RSAPublicKey)factory.generatePublic(publicSpec);
    }

    /**
     * Generate the digital signature of the message by private key
     * 
     * @param data
     *            encrypt data
     * @param privateKey
     * 
     * @return
     * @throws Exception
     */
    public static byte[] sign(String content, 
                              byte[] privateKeyEnc, 
                              String encode) throws Exception {
        return sign(content.getBytes(encode), privateKeyEnc);
    }

    public static byte[] sign(byte[] data, 
                              byte[] privateKeyEnc) throws Exception {
        // Build PKCS8EncodedKeySpec object
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyEnc);

        // KEY_ALGORITHM with predefined key algotithm
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // get private key object
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        return sign(data, priKey);
    }

    public static byte[] sign(String content, 
                              PrivateKey privateKey, 
                              String encode) throws Exception {
        return sign(content.getBytes(encode), privateKey);
    }

    public static byte[] sign(byte[] data, 
                              Key key) throws Exception {
        return sign(data, (PrivateKey)key);
    }

    public static byte[] sign(byte[] data, 
                              PrivateKey privateKey) throws Exception {
        // use private key to sign the message
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return signature.sign();
    }

    /**
     * Verify digital signature
     * 
     * @param data
     *            encrypt data
     * @param publicKey
     *            
     * @param sign
     *            digital signature
     * 
     * @return true with verify successful and false with failed
     * @throws Exception
     * 
     */
    public static boolean verify(String content, 
                                 byte[] publicKeyEnc, 
                                 byte[] sign, 
                                 String encode) throws Exception {
        return verify(content.getBytes(encode), publicKeyEnc, sign);
    }

    public static boolean verify(byte[] data, 
                                 byte[] publicKeyEnc, 
                                 byte[] sign) throws Exception {
        // Build X509EncodedKeySpec object
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyEnc);

        // KEY_ALGORITHM with predefined key algotithm
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // get public key
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        return verify(data, pubKey, sign);
    }

    public static boolean verify(String content, 
                                 PublicKey publicKey, 
                                 byte[] sign,
                                 String encode) throws Exception {
        return verify(content.getBytes(encode), publicKey, sign);
    }

    // public static boolean verify(byte[] data, 
    //                              Key key, 
    //                              byte[] sign) throws Exception {
    //     return verify(data, (PublicKey)key, sign);
    // }

    public static boolean verify(byte[] data, 
                                 PublicKey publicKey, 
                                 byte[] sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        // verify signature
        return signature.verify(sign);
    }

    /**
     * Decrypt <br>
     * Use private key to decrypt
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] keyEnc) 
            throws Exception {
        // get private key
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyEnc);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = (PrivateKey)keyFactory.generatePrivate(pkcs8KeySpec);

        return decryptByKey(data, privateKey, keyFactory.getAlgorithm());
    }

    /**
     * Decrypt<br>
     * Use public key to decrypt
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] keyEnc) 
            throws Exception {
        // get public key
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyEnc);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = (PublicKey)keyFactory.generatePublic(x509KeySpec);

        return decryptByKey(data, publicKey, keyFactory.getAlgorithm());
    }

    /**
     * Decrypt<br>
     * Decrypt with input key
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByKey(byte[] data, PrivateKey key) throws Exception {
        return decryptByKey(data, (Key)key, KEY_ALGORITHM);
    }

    public static byte[] decryptByKey(byte[] data, PublicKey key) throws Exception {
        return decryptByKey(data, (Key)key, KEY_ALGORITHM);
    }

    public static byte[] decryptByKey(byte[] data, Key key) throws Exception {
        return decryptByKey(data, key, KEY_ALGORITHM);
    }

    public static byte[] decryptByKey(byte[] data, Key key, String algorithm) 
            throws Exception {
        // decrypt data
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    /**
     * Encrypt<br>
     * Use public key to encrypt data
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] keyEnc) 
            throws Exception {
        // get public key
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyEnc);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        return encryptByKey(data, publicKey, keyFactory.getAlgorithm());
    }

    /**
     * Encrypt<br>
     * User private key to encrypt data
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] keyEnc) 
            throws Exception {
        // Get private key
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyEnc);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        return encryptByKey(data, privateKey, keyFactory.getAlgorithm());
    }

    /**
     * Encrypt<br>
     * Encrypt data with input key
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByKey(byte[] data, PrivateKey key) throws Exception {
        return encryptByKey(data, (Key)key, KEY_ALGORITHM);
    }

    public static byte[] encryptByKey(byte[] data, PublicKey key) throws Exception {
        return encryptByKey(data, (Key)key, KEY_ALGORITHM);
    }

    public static byte[] encryptByKey(byte[] data, Key key) throws Exception {
        return encryptByKey(data, key, KEY_ALGORITHM);
    }

    public static byte[] encryptByKey(byte[] data, Key key, String algorithm) 
            throws Exception {
        // Encrypt data
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }
}
