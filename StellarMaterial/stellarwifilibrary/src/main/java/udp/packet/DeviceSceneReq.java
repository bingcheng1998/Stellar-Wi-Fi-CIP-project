package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 21:49
 *          类说明
 */
public class DeviceSceneReq extends Packet {
    public final static byte mFrameType=0x05;
    byte modeId;
    short changeTime;
    public DeviceSceneReq(){
        super(mFrameType,NEED_RESPONSE_CODE);
    }

    public DeviceSceneReq(byte modeId, short changeTime) {
        super(mFrameType,NEED_RESPONSE_CODE);
        this.modeId = modeId;
        this.changeTime = changeTime;
    }

    public byte getModeId() {
        return modeId;
    }

    public void setModeId(byte modeId) {
        this.modeId = modeId;
    }

    public short getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(short changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(3);
        buffer.put(modeId).putShort(changeTime);
        return (byte[]) buffer.flip().array();
    }
}
