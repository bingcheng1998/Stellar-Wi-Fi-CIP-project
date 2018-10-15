package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/3 17:01
 *          类说明
 *          <p>
 *          帧类型 ：固定为 0x0B
 *          调光系数：范围为0~100，100 表示最亮。
 *          变化时间：范围为0~65535，以 ms 单位。
 *          注意：支持此条协议的程序版本，将不能再支持“设置调光系数”这条协议。
 */
public class DeviceBrightnessV2Req extends Packet {
    public final static byte mFrameType = 0x0B;
    byte value;//0-100
    short changetime;

    public DeviceBrightnessV2Req() {
        super(mFrameType, NEED_RESPONSE_CODE);
    }

    public DeviceBrightnessV2Req(byte value, short changetime) {
        super(mFrameType, NEED_RESPONSE_CODE);
        this.value = value;
        this.changetime = changetime;
    }


    public short getChangetime() {
        return changetime;
    }

    public void setChangetime(short changetime) {
        this.changetime = changetime;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(3);
        buffer.put(value);
        buffer.putShort(changetime);
        return (byte[]) buffer.flip().array();
    }
}
