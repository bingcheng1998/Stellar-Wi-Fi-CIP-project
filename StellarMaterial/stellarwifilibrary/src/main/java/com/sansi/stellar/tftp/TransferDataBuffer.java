package com.sansi.stellar.tftp;

import java.nio.ByteBuffer;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/7/17 14:05
 *          类说明
 */
public class TransferDataBuffer {
    public static final int blockSize=512;
    ByteBuffer buffer;
    String filename;


    public TransferDataBuffer(byte[] data, String filename) {
        this.buffer = ByteBuffer.wrap(data);
        this.filename = filename;
    }

    public int totalBlock(){
        return (int) Math.ceil(buffer.limit() * 1.0 / blockSize);
    }

    public byte[] block(int seq){
        if (seq >= totalBlock()) throw new IllegalArgumentException();
        buffer.mark();
        buffer.position(seq * blockSize);
        byte[] data;
        if (buffer.remaining() >= blockSize) {
            data = new byte[blockSize];
        } else {
            data = new byte[buffer.remaining()];
        }
        buffer.get(data);
        buffer.reset();
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public int length(){
        return buffer.limit();
    }
}
