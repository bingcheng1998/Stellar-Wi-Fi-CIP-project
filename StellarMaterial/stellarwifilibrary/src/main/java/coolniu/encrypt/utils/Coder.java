package coolniu.encrypt.utils;

import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Basic Encrypt Component
 * Source: http://snowolf.iteye.com/blog/379860
 * @version 1.0
 * @since 1.0
 */
public abstract class Coder {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * MAC Algorithm list
     * 
     * <pre>
     * HmacMD5 
     * HmacSHA1 
     * HmacSHA256 
     * HmacSHA384 
     * HmacSHA512
     * </pre>
     */
    public static final String KEY_MAC = "HmacMD5";

    /**
     * BASE64 Decrypt
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key);
    }

    /**
     * BASE64 Encrypt
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBytes(key);
    }

    /**
     * MD5 Encrypt
     * 
     * @param data
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);

        return md5.digest();
    }

    /**
     * SHA Encrypt
     * 
     * @param data
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);

        return sha.digest();
    }

    /**
     * Initialize HMAC secret key
     * 
     * @return String
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC Encrypt
     * 
     * @param data
     * @param key
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String strKey) throws Exception {
        return encryptHMAC(data, decryptBASE64(strKey), KEY_MAC);
    }

    public static byte[] encryptHMAC(byte[] data, byte[] key, String name) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, name);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);

        return mac.doFinal(data);
    }
}
