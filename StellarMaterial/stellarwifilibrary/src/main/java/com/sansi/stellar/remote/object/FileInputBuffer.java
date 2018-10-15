package com.sansi.stellar.remote.object;

import com.sansi.stellar.tftp.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/8/14 13:45
 *          类说明
 */
public class FileInputBuffer {
    RandomAccessFile file;
    int packageSize = 2048;
    int offset = 0;

    public FileInputBuffer(File file, int packageSize) throws FileNotFoundException {
        this.packageSize = packageSize;
        this.file = new RandomAccessFile(file, "r");
    }


    public byte[] nextPackage() {
        try {
            int remainBytes = (int) (file.length() -offset);
            if (remainBytes > 0) {
                int len = Math.min(packageSize, remainBytes);
                byte[] buffer = new byte[len];
                file.readFully(buffer);
                offset+=len;
                return buffer;
            } else {
                return new byte[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public int getOffset() {
        return offset;
    }

    public int packageTotal() {
        try {
            int total = (int) Math.ceil(file.length() * 1.0f / packageSize);
            return total;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean hasRemaining() {
        return offset < length();
    }

    public int length() {
        try {
            return (int) file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void close(){
        IOUtils.close(file);
    }
}
