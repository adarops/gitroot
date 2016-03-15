package com.ilincar.ntmode;

import nt96650_serialport.SerialManager;
import android.util.Log;

/**
 * Created by zoufeng on 16/3/5.
 */
public class NT96655Impl implements NT96655Feature{

    public void sendCommand(final byte[] txbuf) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SerialManager.getInstance().sendBytes(txbuf);
                Log.d("NT96655Manager","txbuf = " + NT96655Tools.getHexString(txbuf));
            }
        }).start();
    }

    /**
     * 释放方法，如串口释放
     */
    @Override
    public void release() {

    }

    /**
     * 设置录制分辨率
     *
     * @param mode
     */
    @Override
    public void setResolution(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_RESOLUTION, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置前后录制
     *
     * @param mode
     */
    @Override
    public void setDoubleRecording(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_DOUBLE_RECORDING, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置循环录像时长
     */
    @Override
    public void setLoopRecorderTime(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_RECORDER_TIME, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置录制视频时是否有声音
     */
    @Override
    public void setRecorderSound(boolean enabled) {
    	byte[] txbuf;
    	int falg = enabled ? NT96655Util.OPEN_RECORDER_SOUND : NT96655Util.CLOSE_RECORDER_SOUND ;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_RECORDER_SOUND, falg);
    	sendCommand(txbuf);
    }

    /**
     * 设置移动侦测
     */
    @Override
    public void setMotionDetection(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_MOTION_DETECTION, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置 关，高，中，低
     */
    @Override
    public void setGSensor(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_G_SENSOR, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置曝光补偿
     */
    @Override
    public void setExposureCompensation(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_EXPOSURE_COMPENSATION, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置时间水印
     */
    @Override
    public void setTimeWatermark(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_TIME_WATERMARK, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置wdr  运动检测
     */
    @Override
    public void setWdr(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_WDR, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置光源平衡
     */
    @Override
    public void setLightBalancing(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_LIGHT_BALANCING, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置TV输出
     */
    @Override
    public void setTVOutput(boolean enabled) {
    	byte[] txbuf;
    	int falg = enabled ? NT96655Util.TV_OUTPUT_OPEN : NT96655Util.TV_OUTPUT_CLOSE ;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_TV_OUTPUT, falg);
    	sendCommand(txbuf);
    }

    /**
     * 设置电视制式
     */
    @Override
    public void setTVFormat(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_TV_FORMAT, mode);
    	sendCommand(txbuf);
    }

    /**
     * 设置USB模式
     */
    @Override
    public void setUSBMode(int mode) {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_USB_MODE, mode);
    	sendCommand(txbuf);
    }

    /**
     * 格式化SD卡
     */
    @Override
    public void formatSdcard() {
    	byte[] txbuf;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_SDCARD_FORMAT, NT96655Util.SDCARD_FORMAT_CONFIRM);
    	sendCommand(txbuf);
    }

    /**
     * 恢复出厂设置
     */
    @Override
    public void restoreFactory() {
    	byte[] txbuf;
    //	int falg = enabled ? NT96655Util.RESTORE_FACTORY_SETTING_CONFIRM : NT96655Util.RESTORE_FACTORY_SETTING_CANCLE ;
    	txbuf = NT96655Util.getSettingsCommond(NT96655Util.SETTINGS_RESTORE_FACTORY, NT96655Util.RESTORE_FACTORY_SETTING_CONFIRM);
    	sendCommand(txbuf);
    }

    /**
     * 锁定录像
     */
    @Override
    public void doLockCamera() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_CAMERA_LOCK);
        sendCommand(txbuf);
    }

    /**
     * 切换是否录制声音
     */
    @Override
    public void doSwitchMic() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_MIC);
        sendCommand(txbuf);
    }

    /**
     * 开启或者停止录像
     */
    @Override
    public void doSwitchRecorder() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_RECORDER);
        sendCommand(txbuf);
    }

    /**
     * 画中画切换
     */
    @Override
    public void doSwitchPicInPic() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_PIP);
        sendCommand(txbuf);
    }

    /**
     * 返回6735界面
     */
    @Override
    public void doBackSystemUI() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_BACK_SYSTEM);
        sendCommand(txbuf);
    }

    /**
     * 进入设置二级菜单
     */
    @Override
    public void doInSettingMenu() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_SET_TWO_LEVEL_MENU);
        sendCommand(txbuf);
    }

    /**
     * 进入回放界面
     */
    @Override
    public void doInPlayBackMenu() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_REVIE_TWO_LEVEL_MENU);
        sendCommand(txbuf);
    }

    /**
     * 拍照
     */
    @Override
    public void doTakePicture() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_TAKE_PHOTO);
        sendCommand(txbuf);
    }

    /**
     * 读取拍照文件
     */
    @Override
    public void doGetTakePicture() {

    }

    /**
     * 方向键上
     */
    @Override
    public void doKeyUp() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_UP);
        sendCommand(txbuf);
    }

    /**
     * 方向键下
     */
    @Override
    public void doKeyDown() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_DOWN);
        sendCommand(txbuf);
    }

    /**
     * OK键
     */
    @Override
    public void doKeyConfirm() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_CONFIRM);
        sendCommand(txbuf);
    }

    /**
     * 返回上一级菜单
     */
    @Override
    public void doBackMenu() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_BACK);
        sendCommand(txbuf);
    }

    /**
     * 同步时间日期
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    @Override
    public void doSyncDate(int year, int month, int day, int hour, int minute, int second) {

    }

    /**
     * 同步车牌水印
     *
     * @param buffer
     */
    @Override
    public void doSyncCarInfo(byte[] buffer) {

    }

    /**
     * 握手命令
     */
    @Override
    public void doLink() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_LINK);
        sendCommand(txbuf);
    }

    /**
     * 背光调整
     */
    @Override
    public void doAdjustBright() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_BRIGHT);
        sendCommand(txbuf);
    }

    /**
     * 关机
     */
    @Override
    public void doPowerDown() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_POWER_DOWN);
        sendCommand(txbuf);
    }

    /**
     * TV OUTPUT
     */
    @Override
    public void doTvOutPut(boolean enabled) {
        byte[] txbuf;
        txbuf = NT96655Util.getTVOutPutCommond(enabled);
        Log.d("NT96655Manager", "NT96655Impl doTvOutPut txbuf = " + NT96655Tools.getHexString(txbuf));
        sendCommand(txbuf);
    }

    /**
     * 获取模块主界面相关状态
     */
    @Override
    public void doGetMainUIState() {
        byte[] txbuf;
        txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_MAIN_SETTINGS);
        Log.d("NT96655Manager", "NT96655Impl doTvOutPut txbuf = " + NT96655Tools.getHexString(txbuf));
        sendCommand(txbuf);
    }

    /**
     * 菜单参数设置
     */
    @Override
    public void doSettingsPara(int item, int value) {
        byte[] txbuf;
        txbuf = NT96655Util.getSettingsCommond(item, value);
        sendCommand(txbuf);
    }

    /**
     * 菜单参数同步
     */
    @Override
    public void doSyncSettings() {
    //	byte[] txbuf;
    //	txbuf = NT96655Util.getCommond(NT96655Util.CM);
    //	sendCommand(txbuf);
    }

	@Override
	public void doSyncSettingsMenu() {
		// TODO Auto-generated method stub
	    byte[] txbuf;
	    txbuf = NT96655Util.getCommond(NT96655Util.CMD_KEY_SYNC_MENU);
	    sendCommand(txbuf);
	}
}
