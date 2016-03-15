package com.ilincar.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.ilincar.drivingrecorder2.R;



/**
 * Created by zoufeng on 15/11/16.
 */
public class Data {

    public final int AUDIO_BIT_RATE = 128000;
    public final int AUDIO_NUM_CHANNELS = 2;
    public final int AUDIO_SAMP_RATE = 48000;
    public final int VIDEO_FRAME_RATE = 30;

    private final long MAX_FILE_SIZE = 500 * 1024l * 1024l;  //视频文件最大为500M
    private final long MIN_MEDIA_FREE_SIZE = 800 * 1024l * 1024l;   //SD卡最小的剩余空间为800M
    private final int EXCEPTION_RECORDER_TIME = 10000;      //异常录制时间10秒

    public int mScreenWidth = 0;
    public int mScreenHeight = 0;
    
    //保存当前的上传文件的一些参数
    protected HashMap<String, String> mUploadParams = new HashMap<String, String>();
    
    private CllSize.PhotoSize mPhotoSize = new CllSize.PhotoSize();
    private final int [][]photoSize = {
            {800,   480},
            {1280,  720},
            {1920,  1080}
    };

    private CllSize.VideoSize mVideoSize = new CllSize.VideoSize();
    private final int [][]videoSize = {
            {1280,  720,    7200000},
            {1920,  1080,   18000000},
    };

    private CllSize.GsensorSize mGsensorSize = new CllSize.GsensorSize();
    private final float [][] gsensorSize = {
            {6.0f,  18.5f,  39.5f},
            {6.5f,  19.2f,  40.0f},
            {255.0f,255.0f, 255.0f},
    };
    

    ///设置相关的值
    private Context mContext = null;
    private SharedPreferences mSharePreferences = null;
    private final String KEY_VIDEO_SIZE = "kVideoSize";
    private final String KEY_VIDEO_DURATION = "kVideoDuration";
    private final String KEY_PHOTO_SIZE = "kPhotoSize";
    private final String KEY_AUTO_LOCK = "kAutoLock";
    private final String KEY_SOUND_ENABLE = "kSoundEnable";
    private int mCurrentVideoSize = 0;  //0: 720P     1: 1280P
    private int mCurrentVideoDuration = 0;  //0: 1分钟    1: 3分钟
    private int mCurrentPhotoSize = 0;   //0: 低    1: 普通     2: 高
    private int mCurrentAutoLock = 0;       //0: 关闭   1： 普通     2: 灵敏
    private boolean mCurrenSoundEnabled = false;
    ///end

    private static final Data instance = new Data();
    public static Data getInstance(){
        return instance;
    }

    public void load(Context context){
        mContext = context;
        Resources res = context.getResources();
        mScreenWidth = res.getDisplayMetrics().widthPixels;
        mScreenHeight = res.getDisplayMetrics().heightPixels;

        mSharePreferences = mContext.getSharedPreferences(Data.class.getName(), Activity.MODE_PRIVATE);
        mCurrentVideoSize = mSharePreferences.getInt(KEY_VIDEO_SIZE, context.getResources().getInteger(R.integer.def_video_clarity));
        mCurrentVideoDuration = mSharePreferences.getInt(KEY_VIDEO_DURATION, context.getResources().getInteger(R.integer.def_video_duration));
        mCurrentPhotoSize = mSharePreferences.getInt(KEY_PHOTO_SIZE, context.getResources().getInteger(R.integer.def_photo_clarity));
        mCurrentAutoLock = mSharePreferences.getInt(KEY_AUTO_LOCK, context.getResources().getInteger(R.integer.def_auto_lock));
        mCurrenSoundEnabled = mSharePreferences.getBoolean(KEY_SOUND_ENABLE, context.getResources().getBoolean(R.bool.def_sound_enable));
        setCurrentVideoSize(mCurrentVideoSize);
        setCurrentVideoDuration(mCurrentVideoDuration);
        setCreentPhotoSize(mCurrentPhotoSize);
        setCurrentAutoLock(mCurrentAutoLock);
        setSoundEnabled(mCurrenSoundEnabled);
    }

    //返回当前Radio对应的值
    public int getCurrentVideoSize()  { return  mCurrentVideoSize; };
    public int getCurrentVideoDuration() { return mCurrentVideoDuration; }
    public int getCurrentPhotoSize(){ return mCurrentPhotoSize;}
    public int getCurrentAutoLock() { return mCurrentAutoLock; };
    //
    public void setCurrentVideoSize(int value){
        mCurrentVideoSize = value;
        mVideoSize.width = videoSize[value][0];
        mVideoSize.height = videoSize[value][1];
        mVideoSize.bitsRate = videoSize[value][2];
    }
    public void setCurrentVideoDuration(int value){
        mCurrentVideoDuration = value;
    }
    public void setCreentPhotoSize(int value){
        mCurrentPhotoSize = value;
        mPhotoSize.width = photoSize[value][0];
        mPhotoSize.height = photoSize[value][1];
    }

    public void setCurrentAutoLock(int value){
        mCurrentAutoLock = value;
        mGsensorSize.x = gsensorSize[value][0];
        mGsensorSize.y = gsensorSize[value][1];
        mGsensorSize.z = gsensorSize[value][2];
    }

    public void setSoundEnabled(boolean enabled){
        mCurrenSoundEnabled = enabled;
    }

    public int getVideoDuration(){
        return (mCurrentVideoDuration + 1) * 60 * 1000;
    }
    public CllSize.VideoSize getVideoSize(){ return mVideoSize; }
    public CllSize.PhotoSize getPhotoSize(){
        return mPhotoSize;
    }
    public CllSize.GsensorSize getGsensorSize(){ return mGsensorSize; }
    public boolean getSoundEnabled(){
        return mCurrenSoundEnabled;
    }

    public void save(){
        SharedPreferences.Editor edit = mSharePreferences.edit();
        edit.putInt(KEY_VIDEO_SIZE, mCurrentVideoSize);
        edit.putInt(KEY_VIDEO_DURATION, mCurrentVideoDuration);
        edit.putInt(KEY_PHOTO_SIZE, mCurrentPhotoSize);
        edit.putInt(KEY_AUTO_LOCK, mCurrentAutoLock);
        edit.putBoolean(KEY_SOUND_ENABLE, mCurrenSoundEnabled);
        edit.commit();
    }

    public int getExceptionRecorderTime(){
        return EXCEPTION_RECORDER_TIME;
    }

    public long getMaxFileSize(){
        return MAX_FILE_SIZE;
    }

    public long getMinMediaFreeSize(){
        return MIN_MEDIA_FREE_SIZE;
    }
    
    public void setUploadPrarms(HashMap<String, String> params){
    	mUploadParams = params;
    }
    
    public HashMap<String, String> getUploadParams(){
    	return mUploadParams;
    }
    
}