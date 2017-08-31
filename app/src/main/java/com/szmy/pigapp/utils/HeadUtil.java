package com.szmy.pigapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HeadUtil {


	// 处理图片
	public static void setViewByRequest(Intent data,Context mContext,ImageView headImage) {
		ContentResolver resolver = mContext.getContentResolver();
		Uri originalUri = data.getData();
		try {
			// 使用ContentProvider通过URI获取原始图片
			Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
					originalUri);
			setImage(headImage, photo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static  void setImage(ImageView iv_image, Bitmap photo) {
		if (photo != null) {
			// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
//				Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() * 4 / 5,
//						photo.getHeight() * 4 / 5);
			photo= FileUtil.rotateBitmapByDegree(photo,FileUtil.readPictureDegree(FileUtil.IMG_PATH+"/head.jpg"));
			Bitmap smallBitmap = zoomBitmap(photo, 200,
					200);
			// 释放原始图片占用的内存，防止out of memory异常发生
			photo.recycle();
			iv_image.setImageBitmap(smallBitmap);
			// myBitmap = smallBitmap;
			iv_image.setVisibility(View.VISIBLE);
			savePhotoToSDCard(FileUtil.IMG_PATH+"/", "head.jpg", smallBitmap);


		}
	}
	/** Save image to the SD card **/

	public static void savePhotoToSDCard(String path, String photoName,
										 Bitmap photoBitmap) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File photoFile = new File(path, photoName); // 在指定路径下创建文件
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
											int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
					outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}



	public static File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			storageDir = new File(FileUtil.IMG_PATH+"/");
			;
			if (!storageDir.exists()) {
				storageDir.mkdirs();
			}
		} else {
		}
		return storageDir;
	}






	private static BitmapFactory.Options setBitmapOption(String file,
														 int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;

		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;
		if (outWidth > outHeight) {
			width = 200;
			height = 200;
		}
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}
		System.out.println(opt.inSampleSize);
		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	public static void setImage(String path, Bitmap photo) {
		if (photo != null) {

			photo = FileUtil.rotateBitmapByDegree(photo,
					FileUtil.readPictureDegree(path));
			// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
			Bitmap smallBitmap;
			if (photo.getWidth() > photo.getHeight()) {
				smallBitmap = zoomBitmap(photo, 200, 200);
			} else
				smallBitmap = zoomBitmap(photo, 200, 200);
			// 释放原始图片占用的内存，防止out of memory异常发生
			photo.recycle();

			savePhotoToSDCard(path, smallBitmap);

		}
	}

	/** 缩放Bitmap图片 **/

	private static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;

	}

	/** Save image to the SD card **/

	public static void savePhotoToSDCard(String path, Bitmap photoBitmap) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File photoFile = new File(path); // 在指定路径下创建文件
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 90,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
