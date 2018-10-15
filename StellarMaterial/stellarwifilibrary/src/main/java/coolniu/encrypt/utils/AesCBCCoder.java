package coolniu.encrypt.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES CBC Coder<br/>
 * secret key length:   256bit, default:    256 bit<br/>
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 */
public class AesCBCCoder {
	
    /**
     * Secret key algorithm
    */
    private static final String KEY_ALGORITHM = "AES";
    
    private static final String DEFAULT_CIPHER_ALGORITHM_E = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_CIPHER_ALGORITHM_D = "AES/CBC/NoPadding";

    /**
     * Generate random iv parameter
    */
    public static byte[] generateIV(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
    
    /**
     * Initialize secret key
     * 
     * @return byte[] secret key
     * @throws Exception
     */
    public static byte[] initSecretKey() {
        // get secret KeyGenerator object
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
        // Initialize generator to create expected length key
        // AES valid key length: 128/192/256
        // >= 256 need install the JCE Policy file 
        // (Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy File)
        kg.init(256);
        // generate secret key
        SecretKey  secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }
    
    /**
     * Convert Secret key
     * 
     * @param key   binary key
     * @return SecretKey
     */
    private static Key toKey(byte[] key){
        // generate key
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
    
    /**
     * Encrypt
     * 
     * @param data  unencrypt content
     * @param key   Secret key
     * @return byte[]   encrypted data
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, byte[] ivParam) throws Exception{
        return encrypt(data, key, ivParam, DEFAULT_CIPHER_ALGORITHM_E);
    }
    
    /**
     * Encrypt
     * 
     * @param data  unencrypt content
     * @param key   binary key
     * @return byte[]   encrypted data
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivParam) throws Exception{
        return encrypt(data, key, ivParam, DEFAULT_CIPHER_ALGORITHM_E);
    }    
    
    /**
     * Encrypt
     * 
     * @param data  unencrypt content
     * @param key   binary key
     * @param cipherAlgorithm   algorithm/mode/padding
     * @return byte[]   encrypted data
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivParam, String cipherAlgorithm) throws Exception{
        // restore key
        Key k = toKey(key);
        return encrypt(data, k, ivParam, cipherAlgorithm);
    }
    
    /**
     * Encrypt
     * 
     * @param data  unencrypt content
     * @param key   key
     * @param cipherAlgorithm   algorithm/mode/padding
     * @return byte[]   encrypted data
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, byte[] ivParam, String cipherAlgorithm) throws Exception{
        // instance cipher
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // need a iv for CBC mode
        IvParameterSpec iv = new IvParameterSpec(ivParam);
        // initialize with key and encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        // take action
        return cipher.doFinal(data);
    }
    
    /**
     * Decrypt
     * 
     * @param data  undecrypt content
     * @param key   binary key
     * @return byte[]   decrypted data
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] ivParam) throws Exception{
        return decrypt(data, key, ivParam, DEFAULT_CIPHER_ALGORITHM_D);
    }
    
    /**
     * Decrypt
     * 
     * @param data  undecrypt content
     * @param key   key
     * @return byte[]   decrypted data
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, byte[] ivParam) throws Exception{
        return decrypt(data, key, ivParam, DEFAULT_CIPHER_ALGORITHM_D);
    }
    
    /**
     * Decrypt
     * 
     * @param data  undecrypt content
     * @param key   binary key
     * @param cipherAlgorithm   algorithm/mode/padding
     * @return byte[]   decrypted data
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] ivParam, String cipherAlgorithm) throws Exception{
        // restore key
        Key k = toKey(key);
        return decrypt(data, k, ivParam, cipherAlgorithm);
    }

    /**
     * Decrypt
     * 
     * @param data  undecrypt content
     * @param key   secret key
     * @param cipherAlgorithm   algorithm/mode/padding
     * @return byte[]   decrypted data
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, byte[] ivParam, String cipherAlgorithm) throws Exception{
        // instance cipher
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // need a iv for CBC mode
        IvParameterSpec iv = new IvParameterSpec(ivParam);        
        // initialice with key and decrypt mode
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        // take action
        return cipher.doFinal(data);
    }
       
    public static void main(String[] args) throws Exception {
        byte[] key = initSecretKey();
        System.out.println("key: "+ Hex.toHexString(key));

        byte[] ivParam = (new String("cfbsdfgsdfxccvd1")).getBytes();
        System.out.println("iv: "+ Hex.toHexString(ivParam));
        
        Key k = toKey(key);
        
        String data ="AES Test for some long string and test again";
        System.out.println("Before: string:" + data);
        System.out.println("Before: byte[]:" + Hex.toHexString(data.getBytes()));
        System.out.println();
        byte[] encryptData = encrypt(data.getBytes(), k, ivParam);
        System.out.println("After: Length:" + encryptData.length);
        System.out.println("After: byte[]:" + Hex.toHexString(encryptData));
        System.out.println("After: hexStr:" + Hex.encodeHexStr(encryptData));
        System.out.println();
        byte[] decryptData = decrypt(encryptData, k, ivParam);
        System.out.println("Decrypted: byte[]:"+ Hex.toHexString(decryptData));
        System.out.println("Decrypted: string:"+ new String(decryptData));

        for (int i=0; i<5; i++) {
            System.out.println("Iv: " + Hex.toHexString(AesCBCCoder.generateIV(16)));
        }
    }
}