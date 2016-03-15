package com.ilincar.camera;

import android.hardware.Camera;

import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by zoufeng on 3/2/16.
 */
public class CameraDeviceCtrl {

    private final String TAG = "CameraDeviceCtrl";
    private Camera mCamera;

    private Camera.Size mPreviewSize;
    private List<Camera.Size> mSupportedPreviewSize;
    private int mCameraCurrentlyLocked;
    private int mCurrentCamera = 0;     //l2000s上面根据当前档位来设置当前Camera
    public CameraDeviceCtrl(){
    	
    }

    public void openCamera(){
        mCamera = Camera.open(mCurrentCamera);
        mCameraCurrentlyLocked = mCurrentCamera;
        mSupportedPreviewSize = mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void surfaceCreated(SurfaceHolder holder){
        if(mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        //reset previewSize and startPreview
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public void setPreviewSize(int w, int h){
        if(mSupportedPreviewSize != null){
            mPreviewSize = getOptionPreviewSize(mSupportedPreviewSize, w, h);
        }
        if(mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setParameters(parameters);
        }
    }

    public Camera.Size getPreviewSize(){
        return mPreviewSize;
    }

    public void surfaceDestroyed(){
        if(mCamera != null)
            mCamera.stopPreview();
    }

    public void release(){
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    public Camera.Size getOptionPreviewSize(List<Camera.Size> sizes, int w, int h){
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if( sizes == null )
            return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;
        for(Camera.Size size : sizes){
            double ratio = (double) size.width / size.height;
            if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if(Math.abs(size.height - targetHeight) < minDiff){
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if(optimalSize == null){
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes){
                if(Math.abs(size.height - targetHeight) < minDiff){
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }
}
