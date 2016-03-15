package com.ilincar.ntmode;

/**
 * Created by zoufeng on 16/3/5.
 */
public interface NT96655Feature {

    /**
     * 释放方法，如串口释放
     */
    public void release();

    /**
     * 设置录制分辨率
     */
    public void setResolution(int mode);

    /**
     * 设置前后录制
     */
    public void setDoubleRecording(int mode);

    /**
     *设置循环录像时长
     */
    public void setLoopRecorderTime(int mode);

    /**
     * 设置录制视频时是否有声音
     */
    public void setRecorderSound(boolean enabled);

    /**
     * 设置移动侦测
     */
    public void setMotionDetection(int mode);

    /**
     * 设置 关，高，中，低
     */
    public void setGSensor(int mode);

    /**
     * 设置曝光补偿
     */
    public void setExposureCompensation(int mode);

    /**
     * 设置时间水印
     */
    public void setTimeWatermark(int mode);

    /**
     * 设置wdr  运动检测
     */
    public void setWdr(int mode);

    /**
     * 设置光源平衡
     */
    public void setLightBalancing(int mode);

    /**
     * 设置TV输出
     */
    public void setTVOutput(boolean enabled);

    /**
     *设置电视制式
     */
    public void setTVFormat(int mode);

    /**
     * 设置USB模式
     */
    public void setUSBMode(int mode);

    /**
     * 格式化SD卡
     */
    public void formatSdcard();

    /**
     * 恢复出厂设置
     */
    public void restoreFactory();

    /////////////////////////////////

    /**
     * 锁定录像
     */
    public void doLockCamera();

    /**
     * 切换是否录制声音
     */
    public void doSwitchMic();

    /**
     * 开启或者停止录像
     */
    public void doSwitchRecorder();

    /**
     * 画中画切换
     */
    public void doSwitchPicInPic();

    /**
     * 返回6735界面
     */
    public void doBackSystemUI();

    /**
     * 进入设置二级菜单
     */
    public void doInSettingMenu();

    /**
     * 进入回放界面
     */
    public void doInPlayBackMenu();

    /**
     * 拍照
     */
    public void doTakePicture();

    /**
     * 读取拍照文件
     */
    public void doGetTakePicture();

    /**
     * 方向键上
     */
    public void doKeyUp();

    /**
     * 方向键下
     */
    public void doKeyDown();

    /**
     * OK键
     */
    public void doKeyConfirm();

    /**
     * 返回上一级菜单
     */
    public void doBackMenu();

    /**
     * 同步时间日期
     */
    public void doSyncDate(int year, int month, int day, int hour, int minute, int second);

    /**
     * 同步车牌水印
     */
    public void doSyncCarInfo(byte[] buffer);

    /**
     * 握手命令
     */
    public void doLink();

    /**
     * 背光调整
     */
    public void doAdjustBright();

    /**
     * 关机
     */
    public void doPowerDown();

    /**
     * TV OUTPUT
     */
    public void doTvOutPut(boolean enabled);

    /**
     * 获取模块主界面相关状态
     */
    public void doGetMainUIState();

    /**
     * 菜单参数设置
     */
    public void doSettingsPara(int item, int value);

    /**
     * 菜单参数同步
     */
    public void doSyncSettings();
    
    /**
     * 同步设置菜单
     */
    public void doSyncSettingsMenu();
}
