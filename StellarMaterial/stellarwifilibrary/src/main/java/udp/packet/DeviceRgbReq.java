package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Kits;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 15:33
 *          类说明
 */
public class DeviceRgbReq extends PacketSegment {
    public final static byte mFrameType=0x03;

    RgbEx rgbEx;


    public DeviceRgbReq() {
        super(mFrameType, NEED_RESPONSE_CODE);
    }

    public DeviceRgbReq(byte seq, RgbEx rgbEx) {
        super(mFrameType, NEED_RESPONSE_CODE);
        this.seq = seq;
        this.rgbEx = rgbEx;
        this.segOffsetBuff=new SegOffsetBuff(64,rgbEx.toBytes());
    }

    public  DeviceRgbReq(DeviceRgbReq rgbReq){
        super(rgbReq);
        this.rgbEx=rgbReq.rgbEx;
    }



    public RgbEx getRgbEx() {
        return rgbEx;
    }

    public void setRgbEx(RgbEx rgbEx) {
        this.rgbEx = rgbEx;
        this.segOffsetBuff=new SegOffsetBuff(64,rgbEx.toBytes());
    }

    @Override
    public PacketSegment newSegment() {
        return new DeviceRgbReq(this);
    }

    @Override
    public byte[] getData() {
        prepareSegment();
        ByteBuffer buffer=ByteBuffer.allocate(1+segment.length);
        buffer.put(seq).put(segment);
        return (byte[]) buffer.flip().array();
    }



    @Override
    public String getChildString() {
        StringBuilder sb=new StringBuilder();
        sb.append("seq=").append(Kits.bytetoHexString(seq))
                .append(", rgbEx={").append(rgbEx).append("}");
        return sb.toString();
    }
}
