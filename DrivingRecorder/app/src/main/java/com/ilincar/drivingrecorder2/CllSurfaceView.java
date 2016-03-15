package com.ilincar.drivingrecorder2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ilincar.camera.CameraDeviceCtrlEx;

public class CllSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	SurfaceHolder mSurfaceHolder;
	// private SurfaceBackListener mListener;
	private Context mContext;
//	private CameraDeviceCtrl mCameraDeviceCtrl;
	private boolean mSurfaceCreated  = false;
	
	public CllSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//init();

	//	if(mSurfaceCreated) requestLayout();
	//	CameraDeviceCtrlEx.getInstance().openCamera();
		CameraDeviceCtrlEx.getInstance().surfaceCreated(holder);
        if(CameraDeviceCtrlEx.getInstance().getPreviewSize() == null)
            requestLayout();
        
   //     CameraDeviceCtrlEx.getInstance().openCamera();
        
        mSurfaceCreated = true;

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.d("zoufeng","this is surfaceChanged");
		requestLayout();
		CameraDeviceCtrlEx.getInstance().surfaceChanged(holder, format, width, height);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		CameraDeviceCtrlEx.getInstance().surfaceDestroyed();
		Log.d("zoufeng","this is surfaceDestroyed");
	}

//	public CameraDeviceCtrl getCameraDeviceCtrl() {
//		return mCameraDeviceCtrl;
//	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int heigth = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, heigth);
		CameraDeviceCtrlEx.getInstance().setPreviewSize(width, heigth);
	}
	/*
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		int previewWidth = width;
		int previewHeight = height;

		if (CameraDeviceCtrlEx.getInstance().getPreviewSize() != null) {
			previewWidth = CameraDeviceCtrlEx.getInstance().getPreviewSize().width;
			previewHeight = CameraDeviceCtrlEx.getInstance().getPreviewSize().height;
		}

		if (width * previewHeight > height * previewWidth) {
			final int scaledChildWidht = previewWidth * height / previewHeight;
			this.layout((width - scaledChildWidht) / 2, 0,
					(width + scaledChildWidht) / 2, height);
		} else {
			final int scaledChildHeight = previewHeight * width / previewWidth;
			this.layout(0, (height - scaledChildHeight) / 2, width,
					(height + scaledChildHeight) / 2);
		}
	}*/
}
