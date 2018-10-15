package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 21:58
 *          类说明
 */
public class DeviceDelayReq extends Packet {
    public final static byte mFrameType=0x0C;
    byte birVal; //0~100
    short changetime;//ms
    int delay;//范围为 0~0xFFFFFFFF,以 s 单位。0-立即调光，0xFFFFFFFF-取消延迟调光

    public DeviceDelayReq() {
        super(mFrameType,NEED_RESPONSE_CODE);
    }

    public DeviceDelayReq(byte birVal, short changetime, int delay) {
        super(mFrameType,NEED_RESPONSE_CODE);
        this.birVal = birVal;
        this.changetime = changetime;
        this.delay = delay;
    }



    public byte getBirVal() {
        return birVal;
    }

    public void setBirVal(byte birVal) {
        this.birVal = birVal;
    }

    public short getChangetime() {
        return changetime;
    }

    public void setChangetime(short changetime) {
        this.changetime = changetime;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(7);
        buffer.put(birVal).putShort(changetime).putInt(delay);
        return (byte[]) buffer.flip().array();
    }
}
