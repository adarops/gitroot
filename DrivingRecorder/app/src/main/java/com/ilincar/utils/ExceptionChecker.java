package com.ilincar.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;

/**
 * Created by zoufeng on 15/11/9.
 */
public class ExceptionChecker {
    private final String TAG = "ExceptionChecker";

    private Context mContext;
    private SensorManager mSensorManager =  null;
    private Sensor mSensor;

    private boolean mHasSensor = false;

    private int mDuration = 1000;   //两次成功异常的间隔
    private long mLastTime = 0l;    //最后一次异常产生的时间

    private float mShold = 10.0f;

    private float mLastX = 0.0f, mLastY = 0.0f, mLastZ = 0.0f;

    public ExceptionChecker(Context context){
        mContext = context;
        start();
    }

    ///接口回调
    public interface OnCollisionListener{
        void onCollision();
    }
    private OnCollisionListener mCallBack = null;
    public void setOnCollisionListener(OnCollisionListener callBack){
        mCallBack = callBack;
    }
    ///end

    private void start(){
        //从数据中获取mShold 的值
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mHasSensor = (mSensor != null);
        if(mHasSensor){
            mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void dispose(){
        mSensorManager.unregisterListener(sensorEventListener);
        mSensorManager = null;
        mSensor = null;
        mHasSensor = false;
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(!mHasSensor || (mCallBack == null))
                return;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float absX = Math.abs(x);
            float absY = Math.abs(y);
            float absZ = Math.abs(z);
            boolean toSave = false;
            if(absX > mShold){
                if(Math.abs(x - mLastX) > absX){
                    toSave = true;
                }
            }
            if(absY > mShold){
                if(Math.abs(x - mLastY) > absY){
                    toSave = true;
                }
            }
            if(absZ > mShold){
                if(Math.abs(z - mLastZ) > absZ){
                    toSave = true;
                }
            }
            mLastX = x; mLastY = y; mLastZ = z;

            if(toSave){
                long currentMils = SystemClock.uptimeMillis(); //获取当前系统时间
                if((currentMils - mLastTime) > mDuration){
                    mLastTime = currentMils;
                    mCallBack.onCollision();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
