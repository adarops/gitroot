package com.ilincar.camera;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zoufeng on 3/2/16.
 */
public class CameraPreView extends ViewGroup implements SurfaceHolder.Callback {

    private final String TAG = "CameraPreView";

 //   private CameraDeviceCtrl mCameraDeviceCtrl;
    private boolean mSurfaceCreated  = false;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;

    public CameraPreView(Context context) {
        super(context);
  //      mCameraDeviceCtrl = new CameraDeviceCtrl();
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void onResume(){
    	CameraDeviceCtrlEx.getInstance().openCamera();
        if(mSurfaceCreated) requestLayout();
    }

    public void onPause(){
    	CameraDeviceCtrlEx.getInstance().release();
    }

    
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	CameraDeviceCtrlEx.getInstance().surfaceCreated(holder);
        if(CameraDeviceCtrlEx.getInstance().getPreviewSize() == null)
            requestLayout();
        mSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        requestLayout();
        CameraDeviceCtrlEx.getInstance().surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	CameraDeviceCtrlEx.getInstance().surfaceDestroyed();
    //	
    }

  //  public CameraDeviceCtrl getCameraDeviceCtrl(){
  //      return mCameraDeviceCtrl;
  //  }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int heigth = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, heigth);
        CameraDeviceCtrlEx.getInstance().setPreviewSize(width, heigth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getChildCount() > 0){
            final View child = getChildAt(0);
            final int width = r - l ;
            final int height = b - t;
            int previewWidth = width;
            int previewHeight = height;
            if(CameraDeviceCtrlEx.getInstance().getPreviewSize() != null){
                previewWidth = CameraDeviceCtrlEx.getInstance().getPreviewSize().width;
                previewHeight = CameraDeviceCtrlEx.getInstance().getPreviewSize().height;
            }

            if(width * previewHeight > height * previewWidth){
                final int scaledChildWidht = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidht) / 2, 0, (width + scaledChildWidht) / 2, height);
            }else{
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2);
            }
        }
    }

}
