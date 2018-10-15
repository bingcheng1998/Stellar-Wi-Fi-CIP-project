package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 13:16
 *          类说明
 */
public class DeviceBrightnessReq extends Packet {
    public final static byte mFrameType=0x02;
    private  byte value;//0-100

    public DeviceBrightnessReq(byte value) {
        super(mFrameType,NEED_RESPONSE_CODE);
        this.value = value;
    }

    public DeviceBrightnessReq() {
        super(mFrameType,NEED_RESPONSE_CODE);
    }


    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        return new byte[]{value};
    }
}
