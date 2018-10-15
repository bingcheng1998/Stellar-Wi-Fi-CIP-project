package com.sansi.stellar.tftp;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016-3-14 下午4:38:44 
 * 类说明 
 */
public class IOUtils {
//	public static InputStream printStream(InputStream in) throws IOException {
//		ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
//		copyStream(in, arrayOutputStream, true);
//		L.i(arrayOutputStream.toString());
//		return new ByteArrayInputStream(arrayOutputStream.toByteArray());
//	}
	
	public static void copyStream(InputStream in, OutputStream out, boolean closeInStream) {
		byte[]buffer= new byte[0];
		try {
			buffer = new byte[4096];
			int len=-1;
			while((len=in.read(buffer))>0){
                out.write(buffer, 0, len);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(closeInStream){
			close(in);
		}
		buffer=null;
	}


	public static void close(Closeable c){
		if (c == null) return;
		try {
			c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static byte[]asBytes(InputStream in){
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		copyStream(in,out,false);
		return out.toByteArray();
	}
}