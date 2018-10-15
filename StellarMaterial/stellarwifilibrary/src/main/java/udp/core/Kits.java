package udp.core;

import java.nio.ByteBuffer;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/10/12 14:27
 *          类说明
 */
public class Kits {
    public static byte[] ipTobytes(String ip) {
        if (ip == null || ip.length() == 0) return null;
        String[] nums = ip.split("\\.");
        if (nums == null || nums.length != 4) return null;
        byte[] address = new byte[4];
        for (int i = 0; i < nums.length; i++) {
            String num = nums[i];
            address[i] = (byte) Integer.parseInt(num);
        }
        return address;
    }

    public static byte[] hexDecode(String hexString) {
        if (hexString == null || hexString.length() == 0) return null;
        if (hexString.length() % 2 != 0) throw new IllegalArgumentException("hexString length error");
        ByteBuffer buffer = ByteBuffer.allocate(hexString.length() / 2);
        for (int i = 0; i < hexString.length(); i+=2) {
            String num = hexString.substring(i, i + 2);
            buffer.put((byte) Integer.parseInt(num,16));
        }
        return (byte[]) buffer.flip().array();
    }

    public static String bytetoHexString(byte b) {
        String hexString = Integer.toHexString(b & 0xFF);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public static String int2HexString(int i) {
        String hexString = Integer.toHexString(i);
        for (int k = 0; k < 4 - hexString.length(); k++) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public static String intToIpString(int ipAdress) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (ipAdress >> (i * 8) & 0xFF);
        }
        return PacketParserUtils.bytesToIp(bytes);
    }
}
