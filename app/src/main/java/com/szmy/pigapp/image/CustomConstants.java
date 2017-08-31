package com.szmy.pigapp.image;

/**
 * 自定义常量
 * @author qing
 *
 */
public class CustomConstants
{
	public static final String APPLICATION_NAME = "myPigApp";
	public static final String APPPicLICATION_NAME = "myPic";
	//单次最多发送图片数
	public static final int MAX_IMAGE_SIZE = 4;
	//首选项:临时图片
	public static final String PREF_TEMP_IMAGES = "pref_temp_images"; //多张图片
	public static final String PREF_TEMP_IMAGES1 = "pref_temp_images1";//单张图片1
	public static final String PREF_TEMP_IMAGES2 = "pref_temp_images2";//单张图片2
	public static final String PREF_TEMP_IMAGES3 = "pref_temp_images3";//单张图片2
	public static final String PREF_TEMP_IMAGES4 = "pref_temp_images4";//单张图片2
	public static final String PREF_TEMP_IMAGES5 = "pref_temp_images5";//单张图片2

	public static final int TAKE_PICTURE = 0x000000; //
	public static final int TAKE_PHOTO_ = 0x0000010;//相机
	
	public static final int TAKE_PHOTO = 0x000001;//相机
	public static final int TAKE_PHOTO2 = 0x000002;//相机
	public static final int TAKE_PHOTO3 = 0x000003;//相机
	public static final int TAKE_PHOTO4 = 0x000004;//相机
	public static final int TAKE_PHOTO5 = 0x000005;//相机
	public static final int TAKE_PICTURE1 = 0x0000011;//相册
	public static final int TAKE_PICTURE2 = 0x0000021;//相册
	public static final int TAKE_PICTURE3 = 0x0000031;//相册
	public static final int TAKE_PICTURE4 = 0x0000041;//相册
	public static final int TAKE_PICTURE5 = 0x0000051;//相册
	public static final int CORP_TAKE_PICTURE1 = 0x000001111;//裁剪第一张图片
	public static final int CORP_TAKE_PICTURE2 = 0x000002222;//裁剪第二张图片
	public static final int CORP_TAKE_PICTURE3 = 0x000003333;//裁剪第3张图片
	public static final int CORP_TAKE_PICTURE4 = 0x000004444;//裁剪第4张图片
	public static final int CORP_TAKE_PICTURE5 = 0x000005555;//裁剪第5张图片

	 public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
	 public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
	 public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
	 public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
	 
	//相册中图片对象集合
		public static final String EXTRA_IMAGE_LIST = "image_list";
		//相册名称
		public static final String EXTRA_BUCKET_NAME = "buck_name";
		//可添加的图片数量
		public static final String EXTRA_CAN_ADD_IMAGE_SIZE = "can_add_image_size";
		//当前选择的照片位置
		public static final String EXTRA_CURRENT_IMG_POSITION = "current_img_position";
}
