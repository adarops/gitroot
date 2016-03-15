package com.ilincar.ntmode;

import android.util.Log;

/**
 * Created by zoufeng on 16/3/5.
 */
public class NT96655Util {
    //
    public static final int SETTINGS_RESOLUTION = 0x01;

    public static final int SETTINGS_DOUBLE_RECORDING = 0x02;

    public static final int SETTINGS_RECORDER_TIME = 0x03;

    public static final int SETTINGS_RECORDER_SOUND = 0x04;

    public static final int SETTINGS_MOTION_DETECTION = 0x05;

    public static final int SETTINGS_G_SENSOR = 0x06;

    public static final int SETTINGS_EXPOSURE_COMPENSATION = 0x07;

    public static final int SETTINGS_TIME_WATERMARK = 0x08;

    public static final int SETTINGS_WDR = 0x09;

    public static final int SETTINGS_LIGHT_BALANCING = 0x10;

    public static final int SETTINGS_TV_OUTPUT = 0x11;

    public static final int SETTINGS_TV_FORMAT = 0x12;

    public static final int SETTINGS_USB_MODE = 0x13;

    public static final int SETTINGS_SDCARD_FORMAT = 0x14;

    public static final int SETTINGS_RESTORE_FACTORY = 0x15;
    /**
     * 设置当前录制的分辨率
     */
    public static final int RESOLUTION_1080FHD = 0x01;
    public static final int RESOLUTION_720P = 0x02;
    public static final int RESOLUTION_WVGA = 0x03;
    public static final int RESOLUTION_VGA = 0x04;


    /**
     * 设置是否开启前后录制
     */
    public static final int CLOSE_DOUBLE_RECORDING = 0x00;
    public static final int OPEN_DOUBLE_RECORDING = 0x01;

    /**
     * 设置循环录影时长
     * 关，1分钟，2分钟，3分钟 分别对话 00, 01, 02, 03
     */
    public static final int LOOP_RECORDER_TIME_CLOSE = 0x00;
    public static final int LOOP_RECORDER_TIME_ONE = 0x01;
    public static final int LOOP_RECORDER_TIME_TWO = 0x02;
    public static final int LOOP_RECORDER_TIME_THREE = 0x03;

    /**
     * 设置是否开始录制声音
     */
    public static final int CLOSE_RECORDER_SOUND = 0x00;
    public static final int OPEN_RECORDER_SOUND = 0x01;

    /**
     * 设置移动侦测
     * 关，高，中，低 分别对应00, 01, 02, 03
     */
    public static final int MOTION_DETECTION_CLOSE = 0x00;
    public static final int MOTION_DETECTION_HIGH = 0x01;
    public static final int MOTION_DETECTION_NORMAL = 0x02;
    public static final int MOTION_DETECTION_LOW = 0x03;

    /**
     * 设置重力感应设置
     * 关，高，中，低 分别对应00, 01, 02, 03
     */
    public static final int G_SENSOR_CLOSE = 0x00;
    public static final int G_SENSOR_HIGH = 0x01;
    public static final int G_SENSOR_NORMAL = 0x02;
    public static final int G_SENSOR_LOW = 0x03;

    /**
     * 设置曝光补偿
     * ＋2/3, +1/3, +0.0, -1/3, -2/3, -1.0, -4/3, -5/3, -2.0
     * 分别对应1-9
     */
    public static final int EXPOSURE_COMPENSATION_1 = 0x01;
    public static final int EXPOSURE_COMPENSATION_2 = 0x02;
    public static final int EXPOSURE_COMPENSATION_3 = 0x03;
    public static final int EXPOSURE_COMPENSATION_4 = 0x04;
    public static final int EXPOSURE_COMPENSATION_5 = 0x05;
    public static final int EXPOSURE_COMPENSATION_6 = 0x06;
    public static final int EXPOSURE_COMPENSATION_7 = 0x07;
    public static final int EXPOSURE_COMPENSATION_8 = 0x08;
    public static final int EXPOSURE_COMPENSATION_9 = 0x09;

    /**
     * 设置时间水印
     */
    public static final int TIME_WATERMARK_CLOSE = 0x00;
    public static final int TIME_WATERMARK_OPEN = 0x01;

    /**
     * 设置WDR是否开启
     */
    public static final int WDR_CLOSE = 0x00;
    public static final int WDR_OPEN = 0x01;

    /**
     * 设置光源平衡
     */
    public static final int LIGHT_BALANCING_50HZ = 0x01;
    public static final int LIGHT_BALANCING_60HZ = 0x02;

    /**
     * TV输出
     */
    public static final int TV_OUTPUT_CLOSE = 0x00;
    public static final int TV_OUTPUT_OPEN = 0x01;

    /**
     * 电视制式
     */
    public static final int TV_FORMAT_NTSC = 0x01;
    public static final int TV_FORMAT_PAL = 0x02;

    /**
     * 设置USB模式
     */
    public static final int USB_MODE_CHARGING = 0x01;
    public static final int USB_MODE_STORE = 0x02;
    public static final int USB_MODE_CAMERA = 0x03;

    /**
     * 格式化SD卡
     */
    public static final int SDCARD_FORMAT_CANCLE = 0x00;
    public static final int SDCARD_FORMAT_CONFIRM = 0x01;

    /**
     * 恢复出厂设置
     */
    public static final int RESTORE_FACTORY_SETTING_CANCLE = 0x00;
    public static final int RESTORE_FACTORY_SETTING_CONFIRM = 0x01;


    ////////////////////////
    public static final int CMD_NULL = 0;

    //锁定当前录像
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

    //方向键 下
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

    //同步当前模块的各项数据
    public static final int CMD_KEY_SYNC_MENU = 0x22;


    private static final int CMD_HEAD = 0x33CC;
    private static final int CMD_TAIL = 0x0D0A;
    private static final int RESP_HEAD = 0x6576;


    public static byte[] getCommond(int cmd){
        byte[] txbuf = new byte[9];
        int crc;
        txbuf[0] = (byte) (CMD_HEAD >> 8);
        txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
        txbuf[2] = (byte) cmd;
        txbuf[3] = (byte) 0x00;
        txbuf[4] = (byte) 0x00;
        crc = NT96655Tools.getCRC16(txbuf, 5);
        txbuf[5] = (byte) (crc & 0x00ff);
        txbuf[6] = (byte) (crc >> 8);
        txbuf[7] = (byte) (CMD_TAIL >> 8);
        txbuf[8] = (byte) (CMD_TAIL & 0x00ff);
        return txbuf;
    }

    
    
    //把buffer里的内容转换成byte[] 然后写入到串口
    public static byte[] getSyncCarInfoCommond(byte[] buffer){
        int i , len, crc;
        len = (buffer.length & 0xff);
        byte[] txbuf = new byte[len + 9];

        txbuf[0] = (byte) (CMD_HEAD >> 8);
        txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
        txbuf[2] = (byte) CMD_KEY_SSYNC_CARINFO;
        txbuf[3] = (byte) 0x00;
        txbuf[4] = (byte) len;
        for(i = 0; i < len; i++){
            txbuf[5 + i] =buffer[i];
        }
        i = 5 + len;
        crc = NT96655Tools.getCRC16(txbuf, i);
        txbuf[i++] = (byte) (crc & 0x00ff);
        txbuf[i++] = (byte) (crc >> 8);
        txbuf[i++] = (byte) (CMD_TAIL >> 8);
        txbuf[i++] = (byte) (CMD_TAIL & 0x00ff);
        return txbuf;
    }

    public static byte[] getAjustBrightCommond(int bright){
        byte[] txbuf = new byte[10];
        int crc;
        txbuf[0] = (byte) (CMD_HEAD >> 8);
        txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
        txbuf[2] = (byte) CMD_KEY_BRIGHT;
        txbuf[3] = (byte) 0x00;
        txbuf[4] = (byte) 0x02;
        txbuf[5] = (byte) (byte) (bright & 0xff);
        crc = NT96655Tools.getCRC16(txbuf, 6);
        txbuf[6] = (byte) (crc & 0x00ff);
        txbuf[7] = (byte) (crc >> 8);
        txbuf[8] = (byte) (CMD_TAIL >> 8);
        txbuf[9] = (byte) (CMD_TAIL & 0x00ff);
        return txbuf;
    }

    public static byte[] getSyncDataCommond(int year, int month, int day, int hour, int minute, int second){
        byte[] txbuf = new byte[16];
        return txbuf;
    }

    public static byte[] getPhotoFile(int index){
        byte[] txbuf = new byte[11];
        int crc;

        txbuf[0] = (byte) (CMD_HEAD >> 8);
        txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
        txbuf[2] = (byte) CMD_KEY_GET_FILE;
        txbuf[3] = (byte) 0x00;
        txbuf[4] = (byte) 0x02;
        txbuf[5] = (byte) (index >> 8);
        txbuf[6] = (byte) (index & 0xff);
        crc = NT96655Tools.getCRC16(txbuf, 6);
        txbuf[7] = (byte) (crc & 0x00ff);
        txbuf[8] = (byte) (crc >> 8);
        txbuf[9] = (byte) (CMD_TAIL >> 8);
        txbuf[10] = (byte) (CMD_TAIL & 0x00ff);
        return txbuf;
    }

    public static byte[] getPowerDown(boolean down){
        byte[] txbuf = new byte[10];
        return txbuf;
    }

    public static byte[] getSettingsCommond(int item, int state){
        byte[] txbuf = new byte[11];
        int crc;
        txbuf[0] = (byte) (CMD_HEAD >> 8);
        txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
        txbuf[2] = (byte) CMD_KEY_SETTINGS;
        txbuf[3] = (byte) 0x00;
        txbuf[4] = (byte) 0x02;
        txbuf[5] = (byte) item;
        txbuf[6] = (byte) state;
        crc = NT96655Tools.getCRC16(txbuf, 5);
        txbuf[7] = (byte) (crc & 0x00ff);
        txbuf[8] = (byte) (crc >> 8);
        txbuf[9] = (byte) (CMD_TAIL >> 8);
        txbuf[10] = (byte) (CMD_TAIL & 0x00ff);
        return txbuf;
    }

    public static byte[] getTVOutPutCommond(boolean enabled){
		byte[] txbuf = new byte[9];
		int crc;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) CMD_KEY_TVOUT;
		txbuf[3] = 0x00;
		txbuf[4] = (byte) (enabled ? 1:0);
		crc = NT96655Tools.getCRC16(txbuf, 6);
		txbuf[5] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[6] = (byte) (crc >> 8);
		txbuf[7] = (byte) (CMD_TAIL >> 8);
		txbuf[8] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
    }

    public static final void receive(byte protocal){

    }


}
