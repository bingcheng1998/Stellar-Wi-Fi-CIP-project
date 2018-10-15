package udp.packet;

import java.nio.ByteBuffer;

public class SegOffsetBuff {
    final int segSize;
    ByteBuffer buffer;

    public SegOffsetBuff(int segSize, byte[] data) {
        this.segSize = segSize;
        this.buffer = ByteBuffer.wrap(data).asReadOnlyBuffer();
    }

    public SegOffsetBuff(SegOffsetBuff segOffsetBuff){
        this.segSize=segOffsetBuff.segSize;
        this.buffer=segOffsetBuff.buffer.duplicate();
    }

    public boolean hasNext() {
        return buffer.hasRemaining();
    }

    public int offset() {
        int position = buffer.position();
        return position / segSize;
    }

    public byte[] readSegment() {
        int len = buffer.remaining() > segSize ? segSize : buffer.remaining();
        byte[] seg = new byte[len];
        buffer.get(seg);
        return seg;
    }

    public int totalByteLen(){
        return buffer.capacity();
    }

    public int segmentCount(){
        return (int) Math.ceil(buffer.capacity()*1.0/segSize);
    }
}