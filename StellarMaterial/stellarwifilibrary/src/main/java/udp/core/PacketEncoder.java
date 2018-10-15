package udp.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 10:54
 *          类说明
 */
public class PacketEncoder {
    private static final Logger logger = LoggerFactory.getLogger(PacketEncoder.class);
    public static final byte mFrameStart = (byte) 0xA5;
    public static final byte mFrameEnd = (byte) 0x5A;

    protected byte[] encode(Packet packet)throws Exception {
        if(Connection.DEBUG) {
            logger.info("=>待编码对象为{}", packet);
        }
        checkMac(packet);
        byte[] data = packet.getData();//将帧packet的内容置为空，data是每个帧的除去帧类型的那部分
        int cLen = (data != null ? data.length : 0);
        int len = 2 + 8 + 1 + 1 + cLen + 2;//2 长度 + 8 mac+ 1 发送码 + 1帧类型 + *帧内容 + 2 crc16
        ByteBuffer buffer = ByteBuffer.allocate(len-2);
        buffer.putShort((short) len);
        String mac = packet.getMac();
        byte[] macbytes = Kits.hexDecode(mac);
        buffer.put(macbytes);
        buffer.put(packet.getCode());
        buffer.put(packet.getFragType());
        if (cLen > 0) {
            buffer.put(data);
        }
        byte[] rawDatas = (byte[]) buffer.flip().array();
        if(Connection.DEBUG) {
            logger.info("=>未加帧头帧尾校验的数据:" + Utils.byte2hex(rawDatas, true));
        }
        byte[] bytes = packageData(rawDatas);
        if(Connection.DEBUG) {
            logger.info("=>编码后数据为:" + Utils.byte2hex(bytes, true));
        }
        return bytes;
    }

    private void checkMac(Packet packet) {
        String mac = packet.getMac();
        if (mac == null || !mac.toLowerCase().matches("[0-f]{16}"))
            throw new IllegalArgumentException("mac 地址非法");
    }

    private byte[] packageData(byte[] data) {
        short crc = PacketParserUtils.genCrc16(data);
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 4);
        buffer.put(mFrameStart);
        buffer.put(data).putShort(crc).put(mFrameEnd);
        return (byte[]) buffer.flip().array();
    }
}
