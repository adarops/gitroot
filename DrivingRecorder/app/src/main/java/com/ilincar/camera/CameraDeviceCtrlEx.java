package com.ilincar.camera;

import java.io.IOException;
import java.util.List;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.closeli.camera.Closeli;
import com.closeli.camera.MediaConfig;
import com.ilincar.drivingrecorder2.MyApplication;

/**
 * Created by zoufeng on 3/2/16.
 */
public class CameraDeviceCtrlEx {

    private final String TAG = "CameraDeviceCtrl";
    private Camera mCamera;

    private Camera.Size mPreviewSize;
    private List<Camera.Size> mSupportedPreviewSize;
    private int mCameraCurrentlyLocked;
    private int mCurrentCamera = 1; // l2000s上面根据当前档位来设置当前Camera
    private static final int VIDEO_WIDTH = 480;
    private static final int VIDEO_HEIGHT = 320;
    private static CameraDeviceCtrlEx instance = null;

    public static CameraDeviceCtrlEx getInstance() {
        if (instance == null) {
            instance = new CameraDeviceCtrlEx();
        }
        return instance;
    }

    private CameraDeviceCtrlEx() {

    }

    public void openCamera() {
        if (mCamera == null) {
            mCamera = Camera.open(mCurrentCamera);
            mCameraCurrentlyLocked = mCurrentCamera;
            mSupportedPreviewSize = mCamera.getParameters()
                    .getSupportedPreviewSizes();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // reset previewSize and startPreview
        Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        parameters.setPreviewSize(480, 320);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public void setPreviewSize(int w, int h) {
        if (mSupportedPreviewSize != null) {
            mPreviewSize = getOptionPreviewSize(mSupportedPreviewSize, w, h);
        }
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            //	parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setPreviewSize(480, 320);
            parameters.setPreviewFormat(ImageFormat.YV12);
            mCamera.setParameters(parameters);
        }
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    public void surfaceDestroyed() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void release() {
        if (mCamera != null) {
            stopUpload();
            mCamera.release();
            mCamera = null;
        }
    }

    public Camera.Size getOptionPreviewSize(List<Camera.Size> sizes, int w,
                                            int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    /**
     * 开启视频流上传
     */
    public void startUpload() {
        MediaConfig config = new MediaConfig();
        config.setWidth(VIDEO_WIDTH);
        config.setHeight(VIDEO_HEIGHT);
        Closeli.getInstance().setMediaConfig(config);
        Closeli.getInstance().preUploadData(MyApplication.getApplication());
        mCamera.setPreviewCallback(mPreviewCallback);
    }

    /**
     * 关闭视频流上传
     */
    public void stopUpload() {
        try {
            mCamera.setPreviewCallback(null);
            Closeli.getInstance().stopUploadData();
        } catch (Exception e) {

        }
    }

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Closeli.getInstance().uploadData(data);
        }
    };
}
