package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Kits;
import udp.core.Packet;
import udp.core.Utils;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 15:33
 *          类说明
 */
public class DeviceRgbSimpleReq extends Packet {
    public final static byte mFrameType=0x03;
    private byte mode;
    private short time;
    private int rgbw;//rgbw 整形每8位表示 r g b w
    private final byte seq=0;


    public DeviceRgbSimpleReq() {
        super(mFrameType, NEED_RESPONSE_CODE);
    }


    public DeviceRgbSimpleReq(DeviceRgbSimpleReq rgbReq) {
        super(rgbReq);
        this.mode = rgbReq.mode;
        this.time = rgbReq.time;
        this.rgbw = rgbReq.rgbw;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public short getTime() {
        return time;
    }

    public void setTime(short time) {
        this.time = time;
    }

    public int getRgbw() {
        return rgbw;
    }

    public void setRgbw(int rgbw) {
        this.rgbw = rgbw;
    }



    @Override
    public byte[] getData() {
        ByteBuffer buffer=ByteBuffer.allocate(8);
        buffer.put(seq).put(mode).putShort(time).putInt(rgbw);
        return (byte[]) buffer.flip().array();
    }



    @Override
    public String getChildString() {
        StringBuilder sb=new StringBuilder();
        sb.append("seq=").append(Kits.bytetoHexString(seq))
                .append(", mode=").append(Kits.bytetoHexString(mode))
                .append(",time=").append(Utils.shortToHexString(time))
                .append(",rgbw=").append(Integer.toHexString(rgbw));
        return sb.toString();
    }
}
