package udp.packet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/15 18:47
 *          类说明
 */
public class DeviceSetNameReq extends Packet {
    public final static int LIGHT_NAME_LENGHT=24;
    public final static byte mFrameType = 0x09;
    byte[] name;//24字节

    public DeviceSetNameReq() {
        super(mFrameType, NEED_RESPONSE_CODE);
    }

    public String getName() {
        if (name == null) return null;
        try {
            return new String(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public void setName(String name) {
        NameFormatUtils.checkNameFormat(name);
        try {
            this.name = name.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(LIGHT_NAME_LENGHT);
        buffer.put(name);
        return buffer.array();
    }

    @Override
    public String getChildString() {
        return "name='"+getName()+"'";
    }
}
