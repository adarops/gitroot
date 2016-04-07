package com.serenegiant.vo;

import android.os.Environment;

/**
 * 静态常量聚合类，本APP固定的字段和网络请求地址
 * @see Constant
 * */
public class Constant {
	
	/**
	 * 拍照图片保存路径 
	 * */
	public final static String PIC_PATH = Environment.DIRECTORY_DCIM + "/USBCameraPic";
	
	/***************************************** 接口地址 *****************************************/
	/** 接口基地址*/
	public final static String BASE_URL = "http://www.baidu.com/";
	
}
