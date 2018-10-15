package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 21:37
 *          类说明
 */
public class DeviceCctReq extends Packet {
    public final static byte mFrameType=0x04;
    short cctVale;//2700~6500
    short changtime;

    public DeviceCctReq() {
        super(mFrameType,NEED_RESPONSE_CODE);
    }

    public DeviceCctReq(short cctVale, short changtime){
       super(mFrameType,NEED_RESPONSE_CODE);
        this.cctVale=cctVale;
        this.changtime=changtime;
    }

    public short getCctVale() {
        return cctVale;
    }

    public void setCctVale(short cctVale) {
        this.cctVale = cctVale;
    }

    public short getChangtime() {
        return changtime;
    }

    public void setChangtime(short changtime) {
        this.changtime = changtime;
    }

    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(4);
        buffer.putShort(cctVale).putShort(changtime);
        return (byte[]) buffer.flip().array();
    }
}
