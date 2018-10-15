package udp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static udp.core.PacketParserUtils.packetProviders;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 10:54
 *          类说明
 */
public class PacketDecoder {
    private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);
    public static final byte mFrameStart = (byte) 0x35;
    public static final byte mFrameEnd = (byte) 0x53;

    protected Packet decode(byte[] data,byte[]ipAdress,int port) throws Exception {
        final  byte[]__rawData=data;
        if(Connection.DEBUG) {
            logger.info("<=待解码数据:{}", Utils.byte2hex(data, true));
        }
        if (data == null || data.length < 16) return null;//1 帧头 + 2 长度 + 8 mac + 1 应答码 + 1 帧类型 + *（帧内容） + 2 校验 + 1 帧尾
        if(data[0]!=mFrameStart || data[data.length-1]!=mFrameEnd)return null;
        final short len = (short) (data[1] << 8 | data[2]);
        if (len != data.length - 2) {
            logger.warn("<=数据包提供长度{}与实际长度{}不等.",len,data.length-2);
            return null;
        }
        short crc16 = (short) (((data[data.length - 3] & 0xFF) << 8) | (data[data.length - 2] & 0xFF));
        if (PacketParserUtils.genCrc16(data, 1, data.length - 4) != crc16) {
            logger.warn("<=校验不通过{}.", crc16);
            return null;
        }
        byte[]mac= Arrays.copyOfRange(data,3,11);
        byte code=data[11];
        byte frgType=data[12];
        int conentLen=len-2-8-1-1-2;
        byte[]content=new byte[0];
        if(conentLen>0){
            content=Arrays.copyOfRange(data,13,13+conentLen);
        }
        PacketProvider packetProvider = packetProviders.get(frgType);
        if (packetProvider != null) {
            Packet packet = packetProvider.parsePacket(content);
            packet.setPort(port);
            packet.setIpaddress(ipAdress);
            packet.setFragType(frgType);
            packet.setCode(code);
            packet.setMac(Utils.byte2hex(mac));
            return  packet;
        }else {
            logger.warn("帧类型:{}没有Provider.",Kits.bytetoHexString(frgType));
            return new Packet(port, ipAdress, frgType, code, Utils.byte2hex(mac));
        }

    }
}
