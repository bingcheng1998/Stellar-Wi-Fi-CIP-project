package udp.packet;

import udp.core.Packet;
import udp.core.PacketProvider;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 22:16
 *          类说明
 */
public class DeviceDelayQueryRes extends Packet {
    byte birVal; //0~100
    short changetime;//ms
    int delay;//范围为 0~0xFFFFFFFF,以 s 单位。0-立即调光，0xFFFFFFFF-取消延迟调光

    public DeviceDelayQueryRes() {
        super(DeviceDelayQueryReq.mFrameType,NEED_RESPONSE_CODE);
    }

    public DeviceDelayQueryRes(byte birVal, short changetime, int delay) {
        super(DeviceDelayQueryReq.mFrameType,NEED_RESPONSE_CODE);
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
    public static  class DeviceDelayQueryResProvider implements PacketProvider {

        @Override
        public Packet parsePacket(byte[] data) throws Exception {
            DeviceDelayQueryRes delayQueryRes=new DeviceDelayQueryRes();
            delayQueryRes.setBirVal(data[0]);
            delayQueryRes.setChangetime((short) (((data[1] & 0xFF) << 8) | (data[2] & 0xFF)));
            int n = 0;
            for (int i = 3; i < 7; i++) {
                int shiftLeft = (3 - (i - 3)) * 8;
                n = n | ((data[i] & 0xFF) << shiftLeft);
            }
            delayQueryRes.setDelay(n);
            return delayQueryRes;
        }
    }
}
