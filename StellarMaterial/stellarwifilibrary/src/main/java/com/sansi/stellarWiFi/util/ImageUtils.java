package com.sansi.stellarWiFi.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ImageUtils {
	public final static int DEFAULT_IMAGE_WIDTH=480;
	public final static int DEFAULT_IMAGE_HEIGHT=800;
	/**
	 * 将大图转换成小图
	 * */
	public static boolean getSmallImagFile(String infile,String outfile) throws IOException{
//		Bitmap bm=getSmallBitmap(infile);
//		FileOutputStream baos=new FileOutputStream(outfile);
//		boolean flag=bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return getSmallImagFile(infile, outfile, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
	}
	
	/**
	 * 将大图转换成小图
	 * */
	public static boolean getSmallImagFile(String infile,String outfile,int reqWidth, int reqHeight) throws IOException{
		Bitmap bm=getSmallBitmap(infile,reqWidth,reqHeight);
		FileOutputStream baos=new FileOutputStream(outfile);
		boolean flag=bm.compress(CompressFormat.JPEG, 100, baos);
		return flag;
	}
	/**
	 * 获取bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 将bitmap存入指定文件
	 * */
	public static void saveImageJPEG(String fileName, Bitmap bitmap)
			throws IOException {
		saveImageJPEG(fileName, bitmap, 100);
	}

	public static void saveImageJPEG(String fileName,
									 Bitmap bitmap, int quality) throws IOException {
		if (bitmap == null || fileName == null)
			return;
		FileOutputStream fos =new FileOutputStream(new File(fileName));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}
	
	/**
	 * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @throws IOException
	 */
	public static void saveImageJPEG(Context context, String fileName, Bitmap bitmap)
			throws IOException {
		saveImage(context, fileName, bitmap, 100,CompressFormat.JPEG);
	}

	public static void saveImagePNG(Context context, String fileName, Bitmap bitmap)
			throws IOException {
		saveImage(context, fileName, bitmap, 100,CompressFormat.PNG);
	}

	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, int quality,CompressFormat format) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(format, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}

	
	/**
	 * 根据指定高宽获取bitmap
	 * @param path 文件路径
	 * */
	public Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight,
            boolean isHighQuality) {
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);
        if (!isHighQuality) {
            options.inSampleSize += options.inSampleSize / 2 + 2;
        }
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 根据高宽计算缩放比
	 * */
	 private static int computeScale(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {
	            // Calculate ratios of height and width to requested height and
	            // width
	            final int heightRatio = Math.round((float)height / (float)reqHeight);
	            final int widthRatio = Math.round((float)width / (float)reqWidth);

	            // Choose the smallest ratio as inSampleSize value,
	            // this will guarantee a final image
	            // with both dimensions larger than or equal to the requested
	            // height and width.
	            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

	            // This offers some additional logic in case the image has a strange
	            // aspect ratio. For example, a panorama may have a much larger
	            // width than height. In these cases the total pixels might still
	            // end up being too large to fit comfortably in memory, so we should
	            // be more aggressive with sample down the image (=larger
	            // inSampleSize).

	            final float totalPixels = width * height;

	            // Anything more than 2x the requested pixels we'll sample down
	            // further
	            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

	            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
	                inSampleSize++;
	            }
	        }
	        return inSampleSize;
	    }
	 
		public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int rHeight = dm.heightPixels;
			int rWidth = dm.widthPixels;
			// float rHeight=dm.heightPixels/dm.density+0.5f;
			// float rWidth=dm.widthPixels/dm.density+0.5f;
			// int height=bitmap.getScaledHeight(dm);
			// int width = bitmap.getScaledWidth(dm);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			float zoomScale;
			/** 方式1 **/
			// if(rWidth/rHeight>width/height){//以高为准
			// zoomScale=((float) rHeight) / height;
			// }else{
			// //if(rWidth/rHeight<width/height)//以宽为准
			// zoomScale=((float) rWidth) / width;
			// }
			/** 方式2 **/
			// if(width*1.5 >= height) {//以宽为准
			// if(width >= rWidth)
			// zoomScale = ((float) rWidth) / width;
			// else
			// zoomScale = 1.0f;
			// }else {//以高为准
			// if(height >= rHeight)
			// zoomScale = ((float) rHeight) / height;
			// else
			// zoomScale = 1.0f;
			// }
			/** 方式3 **/
			if (width >= rWidth)
				zoomScale = ((float) rWidth) / width;
			else
				zoomScale = 1.0f;
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();
			// 缩放图片动作
			matrix.postScale(zoomScale, zoomScale);
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return resizedBitmap;
		}
	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}
	public static String getTempFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}
	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(Context ctx, String filePath,
			Bitmap bitmap, int quality) throws IOException {
		if (bitmap != null) {
			File file = new File(filePath.substring(0,
					filePath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			bitmap.compress(CompressFormat.JPEG, quality, bos);
			bos.flush();
			bos.close();
			if(ctx!=null){
				scanPhoto(ctx, filePath);
			}
		}
	}
	
	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(Context ctx, String dest,File src) throws IOException {
			File file = new File(dest.substring(0,dest.lastIndexOf(File.separator)));
			if (!file.exists()|| !file.isDirectory()) {
				file.mkdirs();
			}
			FileUtil.copy(src, new File(dest));
			if(ctx!=null){
				scanPhoto(ctx, dest);
			}
		}
	
	private static void scanPhoto(Context ctx, String imgFileName) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(imgFileName);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		ctx.sendBroadcast(mediaScanIntent);
	}
	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	

	
	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath,int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
			FileUtil.getSaveFilePath("td"),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	/**删除图片目录*/
	public static void clearAlbumDir(){
		FileUtil.deleteDirectory(getAlbumDir().getAbsolutePath());
	}
		/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "album";
	}
	@SuppressLint("SimpleDateFormat")
	public static File createImageFile() {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "tmp_" + timeStamp +UUID.randomUUID()+ ".jpg";
		
		File image = new File(getAlbumDir(), imageFileName);
		return image;
	}
	public static String  formatImagePath(String uri){
		if(TextUtils.isEmpty(uri))return "";
		if("nopicture".equals(uri))return "";
		if(uri.startsWith("http://")||uri.startsWith("https://"))return uri;
		return FileUtil.getFormatFilePath(uri);
	}
}
