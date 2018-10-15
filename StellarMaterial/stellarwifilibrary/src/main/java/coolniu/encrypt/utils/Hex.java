/**
 * reference apache commons 
 * <a href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
 * 
 * @author Aub
 * 
 */
package coolniu.encrypt.utils;

public class Hex {

    /**
     * Build lower-case character array as hex output.
     */
    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Build upper-case character array as hex output.
     */
    private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * Translate byte array to Hex character array
     * 
     * @param data 
     *            byte[]
     * @return Hex char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Translate byte array to Hex character array
     * 
     * @param data	
     *            byte[]
     * @param toLowerCase
     *            <code>true</code> with lower-case <code>false</code> with upper-case
     * @return Hex char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Translate byte array to Hex character array
     * 
     * @param data
     *            byte[]
     * @param toDigits
     *            define the output arrange char[]
     * @return Hex char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Translate byte array to Hex character array
     * 
     * @param data
     *            byte[]
     * @return Hex String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * Translate byte array to Hex character array
     * 
     * @param data
     *            byte[]
     * @param toLowerCase
     *            <code>true</code> with lower-case <code>false</code> with upper-case
     * @return Hex String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Translate byte array to Hex character array
     * 
     * @param data
     *            byte[]
     * @param toDigits
     *            define the output arrange char[]
     * @return Hex String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * Translate Hex character array to byte array
     * 
     * @param data
     *            Hex char[]
     * @return byte[]
     * @throws RuntimeException
     *             Throw Exception for invalid array length
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }
    
    public static byte[] hexStringToByteArray(String s) {
    	int len = s.length();
    	
        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] data = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Translate Hex character to integer
     * 
     * @param ch
     *            Hex char
     * @param index
     *            char position in hex char array
     * @return integer
     * @throws RuntimeException
     *             thorw Exception if ch is not a valid hex char
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /*
    * Converts a byte to hex digit and writes to the supplied buffer
    */
    protected static void byte2hex(byte b, StringBuffer buf, char[] toDigits) {
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(toDigits[high]);
        buf.append(toDigits[low]);
    }

    /*
    * Converts a byte array to hex string
    */
    public static String toHexString(byte[] block) {
    	return toHexString(block, false);
    }

    public static String toHexString(byte[] block, boolean toLowerCase) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        String srcStr = "Testing String";
        String encodeStr = encodeHexStr(srcStr.getBytes());
        String decodeStr = new String(decodeHex(encodeStr.toCharArray()));
        System.out.println("Before:" + srcStr);
        System.out.println("After:" + encodeStr);
        System.out.println("Decode:" + decodeStr);
    }

}