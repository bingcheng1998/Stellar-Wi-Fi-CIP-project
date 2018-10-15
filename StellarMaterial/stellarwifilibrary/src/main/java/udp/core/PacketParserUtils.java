package udp.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import udp.packet.DeviceDelayQueryReq;
import udp.packet.DeviceDelayQueryRes;
import udp.packet.DeviceInfoReq;
import udp.packet.DeviceInfoRes;
import udp.packet.DeviceRgbReq;
import udp.packet.DeviceRgbRes;
import udp.packet.DeviceStatusReq;
import udp.packet.DeviceStatusRes;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 12:55
 *          类说明
 */
public class PacketParserUtils {
    private static final Logger logger = LoggerFactory.getLogger(PacketParserUtils.class);
    public  static final boolean DEBUG=Connection.DEBUG;
    public static final int SERVER_PRIVIDER=0x00;
    public static final int CLIENT_PRIVIDER=0x01;

    public static int privider_type=CLIENT_PRIVIDER;

    public static short genCrc16(byte[] buffer){
     return genCrc16(buffer,0,buffer.length);
    }

    public final static Map<Byte,PacketProvider> packetProviders=new HashMap<>();

    static {
        if (privider_type == CLIENT_PRIVIDER) {
            packetProviders.put(DeviceInfoReq.mFrameType, new DeviceInfoRes.DeviceInfoResProvider());
            packetProviders.put(DeviceRgbReq.mFrameType,new DeviceRgbRes.DeviceRgbResProvider());
            packetProviders.put(DeviceDelayQueryReq.mFrameType,new DeviceDelayQueryRes.DeviceDelayQueryResProvider());
            packetProviders.put(DeviceStatusReq.mFrameType,new DeviceStatusRes.DeviceStatusResProvider());
        } else if (privider_type == SERVER_PRIVIDER) {
        }
    }

    public static short genCrc16(byte[] buffer, int offset, int buffer_length) {
        byte c, treat, bcrc;
        short wcrc = 0;
        int i, j;
        for (i = offset; i < offset + buffer_length; i++) {
            c = buffer[i];
            for (j = 0; j < 8; j++) {
                treat = (byte) (c & 0x80);
                c <<= 1;
                bcrc = (byte) (wcrc >> 8 & 0x80);
                wcrc <<= 1;
                if (treat != bcrc) {
                    wcrc ^= 0x1021;
                }
            }
        }
        return wcrc;
    }

    private static void checkCrc16(byte[] tran) throws ProtocolErrorException {
        int len =tran.length;
        if (len > 0) {
            if (len < 3) throw new ProtocolErrorException("protocol date length error", null);
            short crc16 = (short) ((tran[len - 2] << 8) | tran[len - 1]);
            short calcCrc16 = genCrc16(tran, 0, len - 2);
            if (crc16 != calcCrc16)
                new ProtocolErrorException("protocol crc16 validation fails", null);

        }
    }

    public static String bytesToIp(byte[] bytes) {
        if(bytes.length!=4)new IllegalArgumentException("bytes.length="+bytes.length);
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<bytes.length;i++){
            sb.append((bytes[i]&0xFF));
            if(i<bytes.length-1){
                sb.append(".");
            }
        }
        return sb.toString();
    }



    public static String packetToString(Packet packet){
        return String.format("%s\n",packet.toString());
    }

}
