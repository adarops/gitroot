package com.ilincar.utils;

public class NTData {
	/*
	 *     //锁定当前录像
    public static final int CMD_KEY_CAMERA_LOCK = 0x01;
    //麦克风打开或者关闭
    public static final int CMD_KEY_MIC = 0x02;
    //开始或者停止录像
    public static final int CMD_KEY_RECORDER = 0x03;
    //画中画切换
    public static final int CMD_KEY_PIP = 0x04;
    //返回6735界面
    public static final int CMD_KEY_BACK_SYSTEM = 0x05;
    //进入设置二级菜单
    public static final int CMD_KEY_SET_TWO_LEVEL_MENU = 0x06;
    //进入回放二级菜单
    public static final int CMD_KEY_REVIE_TWO_LEVEL_MENU = 0x07;
    //拍照
    public static final int CMD_KEY_TAKE_PHOTO = 0x08;
    //读取拍照文件
    public static final int CMD_KEY_GET_FILE = 0x09;
     //方向键 上
    public static final int CMD_KEY_UP = 0x10;
    //方向键 
    public static final int CMD_KEY_DOWN = 0x11;
    //OK键
    public static final int CMD_KEY_CONFIRM = 0x12;
    //返回上一级菜单
    public static final int CMD_KEY_BACK = 0x13;
    //同步时间日期
    public static final int CMD_KEY_SYNC_DATE = 0x14;
    //同步车牌水印
    public static final int CMD_KEY_SSYNC_CARINFO = 0x15;
    //握手命令
    public static final int CMD_KEY_LINK = 0x16;
    //背光调整
    public static final int CMD_KEY_BRIGHT = 0x17;
    //当前模块关机命令
    public static final int CMD_KEY_POWER_DOWN = 0x18;
    //TV OUT控制命令
    public static final int CMD_KEY_TVOUT = 0x19;
    //获取模块主界面下的相关状态
    public static final int CMD_KEY_MAIN_SETTINGS = 0x20;
    //设置菜单下，设置各项
    public static final int CMD_KEY_SETTINGS = 0x21;
    //同步当前模的各项数据
    public static final int CMD_KEY_SYNC_MENU = 0x22;
	 */
	///主界面相关
    ////[0] sd卡， [1]循环录像状态  [2,3]录像时间  [4]移动侦测 [5]wdr 状态   [6]当前分辨率 [7] 当前声音
	public static final int MAIN_ITEM_SDCARD = 0;
	public static final int MAIN_ITEM_LOOP_RECORDER = 1;
	public static final int MAIN_ITEM_RECORDER_TIME = 2;    //时间占两个字节
	public static final int MAIN_ITEM_MOTION_DETECTION = 4;
	public static final int MAIN_ITEM_WDR = 5;
	public static final int MAIN_ITEM_RESOLUTION = 6;
	public static final int MAIN_ITEM_SOUND = 7;
	
	///设置界面相关
	public static final int SET_ITEM_RESOLUTION = 0;
	public static final int SET_ITEM_DOUBLE_RECORDER = 1;
	public static final int SET_ITEM_LOOP_RECORDER = 2;
	public static final int SET_ITEM_SOUND = 3;
	public static final int SET_ITEM_MOTION_DETECTION = 4;
	public static final int SET_ITEM_GSENSOR = 5;
	public static final int SET_ITEM_EXPOSURE_COMPENSATION = 6;
	public static final int SET_ITEM_TIME_WATERMARK = 7;
	public static final int SET_ITEM_WDR = 8;
	public static final int SET_ITEM_LIGHT_BALANCING = 9;
	public static final int SET_ITEM_TV_OUTPUT = 10;
	public static final int SET_ITEM_TV_FORMAT = 11;
	public static final int SET_ITEM_USB_MODE = 12;
	
	
}
