package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/21 10:01
 *          类说明
 */
public class Utils {
    public static String byte2hex(byte [] buffer){
        return byte2hex(buffer,false);
    }

    public static String byte2hexBlankSplit(byte [] buffer){
        return byte2hex(buffer,true);
    }

    public static byte[] shortTobytes(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s >> 8) & 0xFF);
        b[1] = (byte) (s & 0xFF);
        return b;

    }

    public static String shortToHexStringBlankSplit(short s){
        return byte2hex(shortTobytes(s),true);
    }

    public static String shortToHexString(short s){
        return byte2hex(shortTobytes(s),false);
    }



    public static String byte2hex(byte [] buffer, int offset, int length, boolean blankSplit){
        if (buffer == null || buffer.length < 1) return "";
        StringBuilder h = new StringBuilder();

        for(int i =offset; i < offset+length; i++){
            String hexString = Integer.toHexString(buffer[i] & 0xFF);
            if(hexString.length() == 1){
                hexString = "0" + hexString;
            }
            h.append(hexString);
            if(blankSplit)h.append(" ");
        }
        return h.toString();
    }
    public static String byte2hex(byte [] buffer, boolean blankSplit){
        if(buffer==null || buffer.length<1)return "";
        return byte2hex(buffer,0,buffer.length,blankSplit);
    }
}
