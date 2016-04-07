package com.serenegiant.ui;
/*
 * UVCCamera
 * library and sample to access to UVC web camera on non-rooted Android device
 *
 * Copyright (c) 2015 saki t_saki@serenegiant.com
 *
 * File name: MainActivity.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
 * Files in the jni/libjpeg, jni/libusb, jin/libuvc, jni/rapidjson folder may have a different license, see the respective files.
*/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.DeviceHelper;
import com.serenegiant.usb.IFrameCallback;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener;
import com.serenegiant.usb.USBMonitor.UsbControlBlock;
import com.serenegiant.usb.encode.MediaAudioEncoder;
import com.serenegiant.usb.encode.MediaEncoder;
import com.serenegiant.usb.encode.MediaMuxerWrapper;
import com.serenegiant.usb.encode.MediaVideoEncoder;
import com.serenegiant.usb.widget.CameraViewInterface;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.uvccamera.R;
import com.serenegiant.vo.utils.MyLog;
import com.serenegiant.vo.Debug;
import com.serenegiant.widget.WaitDialog;

public final class CaptureActivity extends Activity implements CameraDialog.CameraDialogParent, DeviceHelper.CameraDialogParent{
	private static final boolean DEBUG = true;	// TODO set false on release
	private static final boolean USER_OPRATOR = false;	// TODO 用户是否操作
	private static final String TAG = "MainActivity";
	private static Bitmap mBitmap;

    /**
     * preview resolution(width)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     */
    private static final int PREVIEW_WIDTH = 1600;//640
    /**
     * preview resolution(height)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     */
    private static final int PREVIEW_HEIGHT = 1200;//480
    /**
     * preview mode
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     * 0:YUYV, other:MJPEG
     */
    private static final int PREVIEW_MODE = UVCCamera.FRAME_FORMAT_MJPEG;

	/**
	 * for accessing USB
	 */
	private USBMonitor mUSBMonitor;
	/**
	 * Handler to execute camera releated methods sequentially on private thread
	 */
	private CameraHandler mHandler;
	/**
	 * for camera preview display
	 */
	private CameraViewInterface mUVCCameraView;
	/**
	 * for open&start / stop&close camera preview  不添加该开关，减少用户操作步骤
	 */
	private ToggleButton mCameraButton;
	/**
	 * button for start/stop recording
	 */
	private ImageButton mCaptureButton;
	
	private static Handler handler;
	
	private DeviceHelper helper;
	
	private WaitDialog waitDialog;
	private String delResult;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DEBUG) Log.v(TAG, "onCreate:");
		setContentView(R.layout.activity_main);

		///////////////////////////////////////////// 【☆】 ////////////////////////////////////
		mCameraButton = (ToggleButton)findViewById(R.id.camera_button);
		if (USER_OPRATOR) {
			mCameraButton.setOnClickListener(mOnClickListener);
		}else {
			mCameraButton.setVisibility(View.GONE);
		}
		
		///////////////////////////////////////////// 【☆】 ////////////////////////////////////

		mCaptureButton = (ImageButton)findViewById(R.id.capture_button);
		mCaptureButton.setOnClickListener(mOnClickListener);

		final View view = findViewById(R.id.camera_view);
		view.setOnLongClickListener(mOnLongClickListener);
		mUVCCameraView = (CameraViewInterface)view;
		mUVCCameraView.setAspectRatio(PREVIEW_WIDTH / (float)PREVIEW_HEIGHT);//TODO

		mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
		mHandler = CameraHandler.createHandler(this, mUVCCameraView);
		
		if (mHandler.isCameraOpened()) {
			mCaptureButton.setVisibility(View.VISIBLE);
		}else {
			mCaptureButton.setVisibility(View.INVISIBLE);
			try {
//				mUSBMonitor.register();
				helper = new DeviceHelper(CaptureActivity.this);
//				helper.requestDevices();
			} catch (Exception e) {
				Toast.makeText(CaptureActivity.this, "请检查USB外部设备是否连接！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (helper == null) {
				Toast.makeText(CaptureActivity.this, "请检查USB外部设备是否连接！", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		waitDialog = new WaitDialog(this);
		handler = new Handler(){

			/**
			 * 0x11  “扫描中”对话框显示并进行图片处理     0x100 图片处理完成进行跳转   0x101 “扫描中”对话框消失  
			 * */
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x11) {
					waitDialog.showDialog();
					new MyAsyncTask().execute("");
				}else if (msg.what == 0x100) {
					//TODO  jump
					Toast.makeText(CaptureActivity.this, delResult +"-------", 1).show();
				}else if (msg.what == 0x101) {
					if (waitDialog != null) {
						waitDialog.dismiss();
					}
				}
			}
			
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		if (DEBUG) Log.v(TAG, "onResume:");
		/**
		 * 如果想让用户自己选择开启摄像头请采用【1】并注掉【2】，并且相关界面也要简单处理一下，修改一下【☆】处
		 * */
		////////////////////////////////////////// 【1】  /////////////////////////////////////////
//		mUSBMonitor.register();
		//////////////////////////////////////////【1】  /////////////////////////////////////////
		
		
		//////////////////////////////////////////【2】  /////////////////////////////////////////
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mUSBMonitor.register();
				helper.requestDevices();
			}
		}, 1000);// 延迟操作  解决post和destroy后摄像头不显示的问题
		mUSBMonitor.unregister();
		//////////////////////////////////////////【2】  /////////////////////////////////////////
				if (mUVCCameraView != null)
					mUVCCameraView.onResume();
	}

	@Override
	public void onPause() {
		if (DEBUG) Log.v(TAG, "onPause:");
//		mHandler.stopRecording();
//		mHandler.stopPreview();
    	mHandler.closeCamera();
    	if (mUSBMonitor != null) {
	        mUSBMonitor.destroy();
        }
    	                                                                            
		if (mUVCCameraView != null)
			mUVCCameraView.onPause();
		mCameraButton.setChecked(false);
		mCaptureButton.setVisibility(View.INVISIBLE);
		mUSBMonitor.unregister();
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		if (DEBUG) Log.v(TAG, "onDestroy:");
        if (mHandler != null) {
	        mHandler = null;
        }
        if (mUSBMonitor != null) {
	        mUSBMonitor.destroy();
	        mUSBMonitor = null;
        }
        mUVCCameraView = null;
        mCameraButton = null;
        mCaptureButton = null;
		super.onDestroy();
	}
	

	/**
	 * event handler when click camera / capture button
	 */
	private final OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(final View view) {
			switch (view.getId()) {
			case R.id.camera_button:
				if (!mHandler.isCameraOpened()) {
					CameraDialog.showDialog(CaptureActivity.this);
				} else {
					mHandler.closeCamera();
					mCaptureButton.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.capture_button:// 录像
				if (mHandler.isCameraOpened()) {
					if (!mHandler.isRecording()) {
						mCaptureButton.setColorFilter(0xffff0000);	// turn red
						mHandler.startRecording();
					} else {
						mCaptureButton.setColorFilter(0);	// return to default color
						mHandler.stopRecording();
					}
				}
				break;
			}
		}
	};

	/**
	 * capture still image when you long click on preview image(not on buttons)
	 */
	private final OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(final View view) {
			switch (view.getId()) {
			case R.id.camera_view:// 拍照
				if (mHandler.isCameraOpened()) {
					mHandler.captureStill();
					return true;
				}
			}
			return false;
		}
	};

	private Surface mSurface;
	private void startPreview() {
		final SurfaceTexture st = mUVCCameraView.getSurfaceTexture();
		if (mSurface != null) {
			mSurface.release();
		}
		
		if (st != null) {
			mSurface = new Surface(st);
			mHandler.startPreview(mSurface);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mCaptureButton.setVisibility(View.VISIBLE);
				}
			});
		}
		
	}

	private final OnDeviceConnectListener mOnDeviceConnectListener = new OnDeviceConnectListener() {
		@Override
		public void onAttach(final UsbDevice device) {
			Toast.makeText(CaptureActivity.this, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onConnect(final UsbDevice device, final UsbControlBlock ctrlBlock, final boolean createNew) {
			if (DEBUG) Log.v(TAG, "onConnect:");
			mHandler.openCamera(ctrlBlock);
			startPreview();
		}

		@Override
		public void onDisconnect(final UsbDevice device, final UsbControlBlock ctrlBlock) {
			if (DEBUG) Log.v(TAG, "onDisconnect:");
			if (mHandler != null) {
				mHandler.closeCamera();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mCaptureButton.setVisibility(View.INVISIBLE);
						mCameraButton.setChecked(false);
					}
				});
			}
		}
		@Override
		public void onDettach(final UsbDevice device) {
			Toast.makeText(CaptureActivity.this, "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
		}
	};

	/**
	 * to access from CameraDialog
	 * @return
	 */
	@Override
	public USBMonitor getUSBMonitor() {
		return mUSBMonitor;
	}

	/**
	 * Handler class to execute camera releated methods sequentially on private thread
	 */
	private static final class CameraHandler extends Handler {
		private static final int MSG_OPEN = 0;
		private static final int MSG_CLOSE = 1;
		private static final int MSG_PREVIEW_START = 2;
		private static final int MSG_PREVIEW_STOP = 3;
		private static final int MSG_CAPTURE_STILL = 4;
		private static final int MSG_CAPTURE_START = 5;
		private static final int MSG_CAPTURE_STOP = 6;
		private static final int MSG_MEDIA_UPDATE = 7;
		private static final int MSG_RELEASE = 9;

		private final WeakReference<CameraThread> mWeakThread;

		public static final CameraHandler createHandler(final CaptureActivity parent, final CameraViewInterface cameraView) {
			final CameraThread thread = new CameraThread(parent, cameraView);
			thread.start();
			return thread.getHandler();
		}

		private CameraHandler(final CameraThread thread) {
			mWeakThread = new WeakReference<CameraThread>(thread);
		}

		public boolean isCameraOpened() {
			final CameraThread thread = mWeakThread.get();
			return thread != null ? thread.isCameraOpened() : false;
		}

		public boolean isRecording() {
			final CameraThread thread = mWeakThread.get();
			return thread != null ? thread.isRecording() :false;
		}

		public void openCamera(final UsbControlBlock ctrlBlock) {
			sendMessage(obtainMessage(MSG_OPEN, ctrlBlock));
		}

		public void closeCamera() {
			stopPreview();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					sendEmptyMessage(MSG_CLOSE);
					
				}
			}, 1000);
			
		}

		public void startPreview(final Surface sureface) {
			if (sureface != null)
				sendMessage(obtainMessage(MSG_PREVIEW_START, sureface));
		}

		public void stopPreview() {
			stopRecording();
			final CameraThread thread = mWeakThread.get();
			if (thread == null) return;
			synchronized (thread.mSync) {
				sendEmptyMessage(MSG_PREVIEW_STOP);
				// wait for actually preview stopped to avoid releasing Surface/SurfaceTexture
				// while preview is still running.
				// therefore this method will take a time to execute
				try {
					thread.mSync.wait();
				} catch (final InterruptedException e) {
				}
			}
		}

		public void captureStill() {
			sendEmptyMessage(MSG_CAPTURE_STILL);
		}

		public void startRecording() {
			sendEmptyMessage(MSG_CAPTURE_START);
		}

		public void stopRecording() {
			sendEmptyMessage(MSG_CAPTURE_STOP);
		}

		@Override
		public void handleMessage(final Message msg) {
			final CameraThread thread = mWeakThread.get();
			if (thread == null) return;
			switch (msg.what) {
			case MSG_OPEN:
				thread.handleOpen((UsbControlBlock)msg.obj);
				break;
			case MSG_CLOSE:
				thread.handleClose();
				break;
			case MSG_PREVIEW_START:
				thread.handleStartPreview((Surface)msg.obj);
				break;
			case MSG_PREVIEW_STOP:
				thread.handleStopPreview();
				break;
			case MSG_CAPTURE_STILL:
				thread.handleCaptureStill();
				break;
			case MSG_CAPTURE_START:
				thread.handleStartRecording();
				break;
			case MSG_CAPTURE_STOP:
				thread.handleStopRecording();
				break;
			case MSG_MEDIA_UPDATE:
				thread.handleUpdateMedia((String)msg.obj);
				break;
			case MSG_RELEASE:
				thread.handleRelease();
				break;
			default:
				throw new RuntimeException("unsupported message:what=" + msg.what);
			}
		}


		private static final class CameraThread extends Thread {
			private static final String TAG_THREAD = "CameraThread";
			private final Object mSync = new Object();
			private final WeakReference<CaptureActivity> mWeakParent;
			private final WeakReference<CameraViewInterface> mWeakCameraView;
			private boolean mIsRecording;
			/**
			 * shutter sound
			 */
			private SoundPool mSoundPool;
			private int mSoundId;
			private CameraHandler mHandler;
			/**
			 * for accessing UVC camera
			 */
			private UVCCamera mUVCCamera;
			/**
			 * muxer for audio/video recording
			 */
			private MediaMuxerWrapper mMuxer;
			/**
			 * for video recording
			 */
			private MediaVideoEncoder mVideoEncoder;

			private CameraThread(final CaptureActivity parent, final CameraViewInterface cameraView) {
				super("CameraThread");
				mWeakParent = new WeakReference<CaptureActivity>(parent);
				mWeakCameraView = new WeakReference<CameraViewInterface>(cameraView);
				loadSutterSound(parent);
			}

			@Override
			protected void finalize() throws Throwable {
				Log.i(TAG, "CameraThread#finalize");
				super.finalize();
			}

			public CameraHandler getHandler() {
				if (DEBUG) Log.v(TAG_THREAD, "getHandler:");
				synchronized (mSync) {
					if (mHandler == null)
					try {
						mSync.wait();
					} catch (final InterruptedException e) {
					}
				}
				return mHandler;
			}

			public boolean isCameraOpened() {
				return mUVCCamera != null;
			}

			public boolean isRecording() {
				return (mUVCCamera != null) && (mMuxer != null);
			}

			public void handleOpen(final UsbControlBlock ctrlBlock) {
				if (DEBUG) Log.v(TAG_THREAD, "handleOpen:");
				handleClose();
				mUVCCamera = new UVCCamera();
				mUVCCamera.open(ctrlBlock);
				if (DEBUG) Log.i(TAG, "supportedSize:" + mUVCCamera.getSupportedSize());
				if (Debug.isOpen) {
					MyLog.log("OutCamera Support Size: ", mUVCCamera.getSupportedSize());
					Toast.makeText(mWeakParent.get(), "OutCamera Support Size: " 
							+ mUVCCamera.getSupportedSize(), Toast.LENGTH_LONG).show();
				}
			}

			public void handleClose() {
				if (DEBUG) Log.v(TAG_THREAD, "handleClose:");
//				handleStopRecording();
				if (mUVCCamera != null) {
					mUVCCamera.stopPreview();
					mUVCCamera.destroy();
					mUVCCamera = null;
				}
			}

			public void handleStartPreview(final Surface surface) {
				if (DEBUG) Log.v(TAG_THREAD, "handleStartPreview:");
				if (mUVCCamera == null) return;
				try {
					mUVCCamera.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_MODE);
				} catch (final IllegalArgumentException e) {
					try {
						// fallback to YUV mode
						mUVCCamera.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT, UVCCamera.DEFAULT_PREVIEW_MODE);
					} catch (final IllegalArgumentException e1) {
						handleClose();
					}
				}
				if (mUVCCamera != null) {
					mUVCCamera.setPreviewDisplay(surface);
					mUVCCamera.startPreview();
				}
			}

			public void handleStopPreview() {
				if (DEBUG) Log.v(TAG_THREAD, "handleStopPreview:");
				if (mUVCCamera != null) {
					mUVCCamera.stopPreview();
				}
				synchronized (mSync) {
					mSync.notifyAll();
				}
			}

			public void handleCaptureStill() {
				if (DEBUG) Log.v(TAG_THREAD, "handleCaptureStill:");
				final CaptureActivity parent = mWeakParent.get();
				if (parent == null) return;
				mSoundPool.play(mSoundId, 0.2f, 0.2f, 0, 0, 1.0f);	// play shutter sound
				final Bitmap bitmap = mWeakCameraView.get().captureStillImage();
				mBitmap = bitmap;
				handler.sendEmptyMessage(0x11);// TODO 如果不要保存拍照图片将“try”部分相关的代码注释
				try {
					// get buffered output stream for saving a captured still image as a file on external storage.
					// the file name is came from current time.
					// You should use extension name as same as CompressFormat when calling Bitmap#compress.
					final File outputFile = MediaMuxerWrapper.getCaptureFile(Environment.DIRECTORY_DCIM, ".png");
					final BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile));
					try {
						try {
							bitmap.compress(CompressFormat.PNG, 100, os);
							os.flush();
							mHandler.sendMessage(mHandler.obtainMessage(MSG_MEDIA_UPDATE, outputFile.getPath()));
						} catch (final IOException e) {
						}
					} finally {
						os.close();
					}
				} catch (final FileNotFoundException e) {
				} catch (final IOException e) {
				}
			}

			public void handleStartRecording() {
				if (DEBUG) Log.v(TAG_THREAD, "handleStartRecording:");
				try {
					if ((mUVCCamera == null) || (mMuxer != null)) return;
					mMuxer = new MediaMuxerWrapper(".mp4");	// if you record audio only, ".m4a" is also OK.
					// for video capturing using MediaVideoEncoder
					mVideoEncoder = new MediaVideoEncoder(mMuxer, mMediaEncoderListener);
					if (true) {
						// for audio capturing
						new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
					}
					mMuxer.prepare();
					mMuxer.startRecording();
					mUVCCamera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_NV21);
				} catch (final IOException e) {
					Log.e(TAG, "startCapture:", e);
				}
			}

			public void handleStopRecording() {
				if (DEBUG) Log.v(TAG_THREAD, "handleStopRecording:mMuxer=" + mMuxer);
				mVideoEncoder = null;
				if (mMuxer != null) {
					mMuxer.stopRecording();
					mMuxer = null;
					// you should not wait here
				}
				if (mUVCCamera != null)
					mUVCCamera.setFrameCallback(null, 0);
			}

			public void handleUpdateMedia(final String path) {
				if (DEBUG) Log.v(TAG_THREAD, "handleUpdateMedia:path=" + path);
				final CaptureActivity parent = mWeakParent.get();
				if (parent != null && parent.getApplicationContext() != null) {
					try {
						if (DEBUG) Log.i(TAG, "MediaScannerConnection#scanFile");
						MediaScannerConnection.scanFile(parent.getApplicationContext(), new String[]{ path }, null, null);
					} catch (final Exception e) {
						Log.e(TAG, "handleUpdateMedia:", e);
					}
					if (parent.isDestroyed())
						handleRelease();
				} else {
					Log.w(TAG, "MainActivity already destroyed");
					// give up to add this movice to MediaStore now.
					// Seeing this movie on Gallery app etc. will take a lot of time.
					handleRelease();
				}
			}

			public void handleRelease() {
				if (DEBUG) Log.v(TAG_THREAD, "handleRelease:");
 				handleClose();
				if (!mIsRecording)
					Looper.myLooper().quit();
			}

			private final IFrameCallback mIFrameCallback = new IFrameCallback() {
				@Override
				public void onFrame(final ByteBuffer frame) {
					if (mVideoEncoder != null) {
						mVideoEncoder.frameAvailableSoon();
						mVideoEncoder.encode(frame);
					}
				}
			};

			private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
				@Override
				public void onPrepared(final MediaEncoder encoder) {
					if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);
					mIsRecording = true;
				}

				@Override
				public void onStopped(final MediaEncoder encoder) {
					if (DEBUG) Log.v(TAG_THREAD, "onStopped:encoder=" + encoder);
					if (encoder instanceof MediaVideoEncoder)
					try {
						mIsRecording = false;
						final CaptureActivity parent = mWeakParent.get();
						final String path = encoder.getOutputPath();
						if (!TextUtils.isEmpty(path)) {
							mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_MEDIA_UPDATE, path), 1000);
						} else {
							if (parent == null || parent.isDestroyed()) {
								handleRelease();
							}
						}
					} catch (final Exception e) {
						Log.e(TAG, "onPrepared:", e);
					}
				}
			};

			/**
			 * prepare and load shutter sound for still image capturing
			 */
			@SuppressWarnings("deprecation")
			private void loadSutterSound(final Context context) {
		    	// get system stream type using refrection
		        int streamType;
		        try {
		            final Class<?> audioSystemClass = Class.forName("android.media.AudioSystem");
		            final Field sseField = audioSystemClass.getDeclaredField("STREAM_SYSTEM_ENFORCED");
		            streamType = sseField.getInt(null);
		        } catch (final Exception e) {
		        	streamType = AudioManager.STREAM_SYSTEM;	// set appropriate according to your app policy
		        }
		        if (mSoundPool != null) {
		        	try {
		        		mSoundPool.release();
		        	} catch (final Exception e) {
		        	}
		        	mSoundPool = null;
		        }
		        // load sutter sound from resource
			    mSoundPool = new SoundPool(2, streamType, 0);
			    mSoundId = mSoundPool.load(context, R.raw.camera_click, 1);
			}

			@Override
			public void run() {
				Looper.prepare();
				synchronized (mSync) {
					mHandler = new CameraHandler(this);
					mSync.notifyAll();
				}
				Looper.loop();
				synchronized (mSync) {
					mHandler = null;
					mSoundPool.release();
					mSoundPool = null;
					mSync.notifyAll();
				}
			}
		}
	}

	public static Bitmap getStillBitmap() {
		return mBitmap;
	}
	
	/**扫描处理结果*/
	public String getDelResult() {
		return delResult;
	}

	public void setDelResult(String delResult) {
		this.delResult = delResult;
	}

	class MyAsyncTask extends AsyncTask<Object, Integer, String> {

		@Override
		protected String doInBackground(Object... params) {
			String result = null;
			try {
				// TODO
				result = "demo";
			} catch (Exception e) {
				handler.sendEmptyMessage(0x101);//返回异常处理结果请求
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			delResult = result;
			handler.sendEmptyMessage(0x100);//返回处理结果请求
		}

	}
}
