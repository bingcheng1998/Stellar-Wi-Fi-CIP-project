package com.sansi.stellarWiFi.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class FileUtil {
	private static String BASE_PATH="SmartHome/files";
	private static String USER_Dir="users";
	
	public static String getSaveFilePath() {
			return getSaveFilePath(BASE_PATH);
	}
	
	public static String getSaveFilePath(String dir ){
			return getRootFilePath() + dir+File.separator;
	}
	
	public static String getUsersDir(){
		return  getSaveFilePath()+USER_Dir;
	}
	public static String getPersonSaveDir(String username){
		return getSaveFilePath()+USER_Dir+File.separator+username;
	}
	
	public static String getAndCreatePersonSaveDir(String username){
		String path=getPersonSaveDir(username);
		createDirectory(path);
		return path;
	}

	

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		String path=Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+UUID.randomUUID().toString();
		boolean success=createDirectory(path);
		if(success)deleteDirectory(path);
		return success;
	}
	
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
	
	
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	
	public static boolean createDirectory(String filePath){
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()&&file.isDirectory()){
			return true;
		}
		return file.mkdirs();

	}
	
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			L.e("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				L.d("delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		file.delete();
		L.d("delete filePath: " + file.getAbsolutePath());
		return true;
	}
	
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}

	public static void delete(File tmpFile) {
		if(tmpFile!=null&&tmpFile.exists()){
			tmpFile.delete();
		}
		
	}
	public  static String FILE_URI_PRIX="file://";
	/**
	 * 文件路径格式化为uri格式
	 * */
	 public static String getFormatFilePath(String path) {
	        if (TextUtils.isEmpty(path)) {
	            return "";
	        }
	        if (path.startsWith("file://")) {
	            return path;
	        }
	        return "file://" + path;
	    }
	 
	public static void copy(String source, String target)throws FileNotFoundException {
		InputStream in = null;
		OutputStream out = null;
		try {
			File s = new File(source);
			File t = new File(target);
			in = new FileInputStream(s);
			out = new FileOutputStream(t);
			CopyStream(in, out);
		} finally {
			close(in);
			close(out);

		}
	}
	
	public static void copy(File src, File det)throws FileNotFoundException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(det);
			CopyStream(in, out);
		} finally {
			close(in);
			close(out);

		}
	}
		public static void copyStream(InputStream is, OutputStream os,
				boolean closeStream) throws IOException {
			try {
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (IOException e) {
				throw e;
			} finally {
				if (closeStream) {
					close(is);
					close(os);
				}
			}
		}

		public static void close(Closeable closeable) {
			try {
				if(closeable!=null)
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str.trim()));
	}




}
