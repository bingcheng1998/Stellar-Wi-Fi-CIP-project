package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 16:43
 *          类说明
 */
public class PacketSegment extends Packet implements Segmentable {
    protected SegOffsetBuff segOffsetBuff;
    protected byte seq;
    protected byte[] segment;
    protected boolean prepareSegmentCalled=false;

    public PacketSegment() {
    }

    public PacketSegment(byte fragType, byte code) {
        super(fragType, code);
    }

    public PacketSegment(int port, byte[] ipaddress) {
        super(port, ipaddress);
    }

    public PacketSegment(int port, byte[] ipaddress, byte fragType, byte code, String mac) {
        super(port, ipaddress, fragType, code, mac);
    }

    public PacketSegment(PacketSegment packet) {
        super(packet);
        this.segOffsetBuff=new SegOffsetBuff(packet.segOffsetBuff);
        this.seq=packet.seq;
    }
    public byte getSeq() {
        prepareSegment();
        return seq;
    }

    public void setSeq(byte seq) {
        this.seq = seq;
    }

    @Override
    public boolean hasNext() {
        return segOffsetBuff.hasNext();
    }

    @Override
    public PacketSegment next() {
        return new PacketSegment(this);
    }

    @Override
    public int segmentCount() {
        return segOffsetBuff.segmentCount();
    }

    @Override
    public PacketSegment newSegment() {
        return this;
    }

    protected synchronized void prepareSegment() {
        if (!prepareSegmentCalled) {
            prepareSegmentCalled=true;
            seq = (byte) (segOffsetBuff.offset() & 0xFF);
            segment = segOffsetBuff.readSegment();
        }
    }
}
