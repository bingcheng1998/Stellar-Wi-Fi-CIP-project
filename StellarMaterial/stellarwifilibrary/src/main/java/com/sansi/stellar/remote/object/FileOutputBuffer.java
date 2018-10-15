package com.sansi.stellar.remote.object;

import com.sansi.stellar.tftp.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/8/14 14:27
 *          类说明
 */
public class FileOutputBuffer {
    RandomAccessFile accessFile;
    int offset = 0;
    int packageSize = 2048;

    public FileOutputBuffer(File file,int packageSize ) throws FileNotFoundException {
        this.accessFile = new RandomAccessFile(file, "rw");
        this.packageSize = packageSize;
        this.offset=0;
    }

    public void appandPackage(byte[ ] datas) throws IOException {
        this.accessFile.write(datas);
        this.offset += datas.length;
    }

    public int getOffset() {
        return offset;
    }



    public void close(){
        IOUtils.close(accessFile);
    }
}
