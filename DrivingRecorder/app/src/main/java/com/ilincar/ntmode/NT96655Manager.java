package com.ilincar.ntmode;

import nt96650_serialport.SerialManager;

import android.util.Log;

/**
 * Created by zoufeng on 16/3/5.
 */
public class NT96655Manager implements NT96655CallBack {

    private final String TAG = "NT96655Manager";
    private NT96655Feature nt96655Feature;

    public static final int COMMAND_MAIN_DATA = 20;
    public static final int COMMAND_SETTIGS_DATA = 22;
    public static final int COMMAND_SDCARD_ERROR = 23;
    public static final int COMMAND_SWITCH_RECORDER = 3;
    public static final int COMMAND_TV_OUT_SWITCH = 19;
    //  public static final

    private static NT96655Manager mNT96655Manager = null;

    public static NT96655Manager getInstance() {
        if (mNT96655Manager == null) {
            mNT96655Manager = new NT96655Manager();
        }
        return mNT96655Manager;
    }

    private NT96655Manager() {
        SerialManager.getInstance().setNt96655CallBack(this);
        nt96655Feature = new NT96655Impl();
    }

    private NIUIStateCallBack mNIUIStateCallBack;

    public void setNIUIStateCallBack(NIUIStateCallBack callback) {
        this.mNIUIStateCallBack = callback;
    }

    //MOTION_DETECTION
    public interface NIUIStateCallBack {
        void onBackMainUIData(int[] items);
    }

    private NISettingStateCallBack mNISettingStateCallBack;

    public void setNISettingStateCallBack(NISettingStateCallBack callback) {
        this.mNISettingStateCallBack = callback;
    }

    public interface NISettingStateCallBack {
        void onBackSettingData(int[] items);
    }

    private NISDCardErrorCallBack mNISDCardErrorCallBack;

    public void setNISDCardErrorCallBack(NISDCardErrorCallBack callBack) {
        this.mNISDCardErrorCallBack = callBack;
    }

    public interface NISDCardErrorCallBack {
        void onSDCardError();
    }

    private NITVOutChangeCallBack mTVOutChangeCallBack;

    public void setNITVOutChangeCallBack(NITVOutChangeCallBack callBack) {
        mTVOutChangeCallBack = callBack;
    }

    public interface NITVOutChangeCallBack {
        void onTVOutChange();
    }

    /**
     * 当发送0x20 获取状态指令后，回调接口
     *
     * @param txbuf
     */
    @Override
    public void onGetN96655Data(String txbuf) {
        Log.d("zoufeng", "this is receive data = " + txbuf);
        try {
            String[] fileds = txbuf.split(" ");
            int commond = Integer.parseInt(fileds[2]);

            int len = 0;
            if (fileds[4].equals("0d") || fileds[4].equals("0D")) {
                len = 13;
            } else {
                len = Integer.parseInt(fileds[4]);
            }
            int[] items = new int[len];
            Log.d("zoufeng", "this is get back commond id = " + commond);
            if (commond == COMMAND_MAIN_DATA) {
                for (int i = 0; i < len; i++) {
                    if (i == 2 || i == 3) {
                        String[] toHex = {fileds[5 + i], fileds[6 + i]};
                        int showTime = NT96655Tools.hexsToint(toHex);
                        items[i] = showTime; //录像时间 如果为0表录没有录像
                    } else {
                        items[i] = Integer.parseInt(fileds[5 + i]);
                    }
                }
                if (mNIUIStateCallBack != null) {
                    mNIUIStateCallBack.onBackMainUIData(items);
                }
                return;
            } else {
                for (int i = 0; i < len; i++) {
                    items[i] = Integer.parseInt(fileds[5 + i]);
                }
            }

            switch (commond) {
          /*  case COMMAND_MAIN_DATA:
                if (mNIUIStateCallBack != null) {
                    Log.d("zoufeng", "this is backdata size = " + items.length + "; value = " + items);
                    mNIUIStateCallBack.onBackMainUIData(items);
                }
                break;*/
                case COMMAND_SETTIGS_DATA:
                    if (mNISettingStateCallBack != null)
                        mNISettingStateCallBack.onBackSettingData(items);
                    break;
                case COMMAND_SDCARD_ERROR:
                    if (mNISDCardErrorCallBack != null)
                        mNISDCardErrorCallBack.onSDCardError();
                    break;
                case COMMAND_SWITCH_RECORDER:
                    //如果是切换录制状态后，则还需要向主界面更新状态
                    //doGetMainUIState();
                    break;
                case COMMAND_TV_OUT_SWITCH:
                    if (mTVOutChangeCallBack != null)
                        mTVOutChangeCallBack.onTVOutChange();
                    break;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 设置录制分辨率
     *
     * @param mode
     */
    public void setResolution(int mode) {
        nt96655Feature.setResolution(mode);
    }

    /**
     * 设置前后录制
     *
     * @param mode
     */
    public void setDoubleRecording(int mode) {
        nt96655Feature.setDoubleRecording(mode);
    }

    /**
     * 设置循环录像时长
     */
    public void setLoopRecorderTime(int mode) {
        nt96655Feature.setLoopRecorderTime(mode);
    }

    /**
     * 设置录制视频时是否有声音
     */
    public void setRecorderSound(boolean enabled) {
        nt96655Feature.setRecorderSound(enabled);
    }

    /**
     * 设置移动侦测
     */
    public void setMotionDetection(int mode) {
        nt96655Feature.setMotionDetection(mode);
    }

    /**
     * 设置 关，高，中，低
     */
    public void setGSensor(int mode) {
        nt96655Feature.setGSensor(mode);
    }

    /**
     * 设置曝光补偿
     */
    public void setExposureCompensation(int mode) {
        nt96655Feature.setExposureCompensation(mode);
    }

    /**
     * 设置时间水印
     */
    public void setTimeWatermark(int mode) {
        nt96655Feature.setTimeWatermark(mode);
    }

    /**
     * 设置wdr  运动检测
     */
    public void setWdr(int mode) {
        nt96655Feature.setWdr(mode);
    }

    /**
     * 设置光源平衡
     */
    public void setLightBalancing(int mode) {
        nt96655Feature.setLightBalancing(mode);
    }

    /**
     * 设置TV输出
     */
    public void setTVOutput(boolean enabled) {
        nt96655Feature.setTVOutput(enabled);
    }

    /**
     * 设置电视制式
     */
    public void setTVFormat(int mode) {
        nt96655Feature.setTVFormat(mode);
    }

    /**
     * 设置USB模式
     */
    public void setUSBMode(int mode) {
        nt96655Feature.setUSBMode(mode);
    }

    /**
     * 格式化SD卡
     */
    public void formatSdcard() {
        nt96655Feature.formatSdcard();
    }

    /**
     * 恢复出厂设置
     */
    public void restoreFactory() {
        nt96655Feature.restoreFactory();
    }

    /**
     * 锁定录像
     */
    public void doLockCamera() {
        nt96655Feature.doLockCamera();
    }

    /**
     * 切换是否录制声音
     */
    public void doSwitchMic() {
        nt96655Feature.doSwitchMic();
    }

    /**
     * 开启或者停止录像
     */
    public void doSwitchRecorder() {
        nt96655Feature.doSwitchRecorder();
    }

    /**
     * 画中画切换
     */
    public void doSwitchPicInPic() {
        nt96655Feature.doSwitchPicInPic();
    }

    /**
     * 返回6735界面
     */
    public void doBackSystemUI() {
        nt96655Feature.doBackSystemUI();
    }

    /**
     * 进入设置二级菜单
     */
    public void doInSettingMenu() {
        nt96655Feature.doInSettingMenu();
    }

    /**
     * 进入回放界面
     */
    public void doInPlayBackMenu() {
        nt96655Feature.doInPlayBackMenu();
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        nt96655Feature.doTakePicture();
    }

    /**
     * 读取拍照文件
     */
    public void doGetTakePicture() {
        nt96655Feature.doGetTakePicture();
    }

    /**
     * 方向键上
     */
    public void doKeyUp() {

    }

    /**
     * 方向键下
     */
    public void doKeyDown() {

    }

    /**
     * OK键
     */
    public void doKeyConfirm() {

    }

    /**
     * 返回上一级菜单
     */
    public void doBackMenu() {

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
    public void doSyncDate(int year, int month, int day, int hour, int minute, int second) {
        nt96655Feature.doSyncDate(year, month, day, hour, minute, second);
    }

    /**
     * 同步车牌水印
     *
     * @param buffer
     */
    public void doSyncCarInfo(byte[] buffer) {
        nt96655Feature.doSyncCarInfo(buffer);
    }

    /**
     * 握手命令
     */
    public void doLink() {
        nt96655Feature.doLink();
    }

    /**
     * 背光调整
     */
    public void doAdjustBright() {
        nt96655Feature.doAdjustBright();
    }

    /**
     * 关机
     */
    public void doPowerDown() {
        nt96655Feature.doPowerDown();
    }

    /**
     * TV OUTPUT
     * 此函数现在不可用
     */
    public void doTvOutPut(boolean enabled) {
        nt96655Feature.doTvOutPut(enabled);
    }

    /**
     * 获取模块主界面相关状态
     */
    public void doGetMainUIState() {
        nt96655Feature.doGetMainUIState();
    }

    /**
     * 菜单参数设置
     */
    public void doSettingsPara(int item, int value) {
        nt96655Feature.doSettingsPara(item, value);
    }

    /**
     * 菜单参数同步
     */
    //   public void doSyncSettings() {
    //   	nt96655Feature.doSyncSettings();
    //   }
    public void doSyncSettingsMenu() {
        nt96655Feature.doSyncSettingsMenu();
    }

}
