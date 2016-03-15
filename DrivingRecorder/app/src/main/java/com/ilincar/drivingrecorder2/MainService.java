package com.ilincar.drivingrecorder2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.closeli.camera.Closeli;
import com.ilincar.camera.CameraDeviceCtrlEx;
import com.ilincar.ntmode.NT96655Manager;
import com.ilincar.ntmode.NT96655Manager.NIUIStateCallBack;
import com.ilincar.ntmode.NT96655Util;
import com.ilincar.utils.NTData;
import com.ilincar.utils.Res;

public class MainService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class MainServiceBinder extends Binder {
		MainService getService() {
			return MainService.this;
		}
	}

	private final IBinder mBinder = new MainServiceBinder();

	private Context mContext = null;
	private WindowManager mWindowManager;
	private View mRecordView;

	private ImageButton mImgBackBtn;
	private ImageButton mTglSoundBtn;
	private ImageButton mTglLockBtn;
	private ImageButton mImgSettingBtn;
	private ImageButton mImgSwitchBtn;
	private ImageButton mImgPlayBtn;
	private ImageButton mImgRecorderBtn;
	private ImageButton mImgTakePictureBtn;

	private ImageView mImgSDCardView;
	private ImageView mImgTimeLongView;
	private ImageView mImgSportView;
	private ImageView mImgWdrView;
	private ImageView mImgRelotionView;

	private boolean bShowMax = false;

	private boolean isSDCardEnabled = false;
	private boolean isSoundRecorder = false;
	private CllSurfaceView mSurfaceView;

	private LinearLayout mToppane;
	private LinearLayout mLeftpane;
	private LinearLayout mRightpane;
	private LinearLayout mBottompane;
	private LinearLayout control_pane;
	private TextView mTextDateTime;
	private ToggleButton yun_video;

	private IntentFilter intentFilter;

	private boolean maxUi = false;

	public enum DisplayMode {
		MAX, MINI,
	};

	private DisplayMode mDisplayMode;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this.getApplicationContext();
		mWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mRecordView = inflater.inflate(R.layout.service_main, null);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
				mContext.getResources().getDisplayMetrics().widthPixels,
				mContext.getResources().getDisplayMetrics().heightPixels,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mRecordView, layoutParams);
		mDisplayMode = DisplayMode.MAX;
		initUi(mRecordView);
		login();
		intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	private void initUi(View view) {
		mSurfaceView = (CllSurfaceView) view.findViewById(R.id.camera_preview);
		mSurfaceView.setOnClickListener(onClickListener);
		mTglSoundBtn = (ImageButton) view.findViewById(R.id.toggle_sound_btn);
		mTglLockBtn = (ImageButton) view.findViewById(R.id.toggle_lock_btn);
		mImgBackBtn = (ImageButton) view.findViewById(R.id.image_back_btn);
		mImgSettingBtn = (ImageButton) view
				.findViewById(R.id.image_setting_btn);
		mImgSwitchBtn = (ImageButton) view.findViewById(R.id.image_switch_btn);
		mImgPlayBtn = (ImageButton) view.findViewById(R.id.image_play_btn);
		mImgRecorderBtn = (ImageButton) view
				.findViewById(R.id.image_recorder_btn);
		mImgTakePictureBtn = (ImageButton) view
				.findViewById(R.id.image_picture_btn);
		mImgSDCardView = (ImageView) view.findViewById(R.id.iamge_sd_card);
		mImgTimeLongView = (ImageView) view.findViewById(R.id.image_time_long);
		mImgSportView = (ImageView) view.findViewById(R.id.image_sport);
		mImgWdrView = (ImageView) view.findViewById(R.id.image_wdr);
		mImgRelotionView = (ImageView) view.findViewById(R.id.image_relotion);
		yun_video = (ToggleButton) view.findViewById(R.id.yun_video);
		mTglSoundBtn.setOnClickListener(onClickListener);
		mTglLockBtn.setOnClickListener(onClickListener);
		mImgBackBtn.setOnClickListener(onClickListener);
		mImgSettingBtn.setOnClickListener(onClickListener);
		mImgSwitchBtn.setOnClickListener(onClickListener);
		mImgPlayBtn.setOnClickListener(onClickListener);
		mImgRecorderBtn.setOnClickListener(onClickListener);
		mImgTakePictureBtn.setOnClickListener(onClickListener);
		yun_video.setOnCheckedChangeListener(onCheckedChangeListener);
		/*
		 * private LinearLayout mToppane; private LinearLayout mLeftpane;
		 * private LinearLayout mRightpane; private LinearLayout mBottompane;
		 * private LinearLayout control_pane; private TextView mTextDateTime;
		 */
		mToppane = (LinearLayout) view.findViewById(R.id.toppane);
		mLeftpane = (LinearLayout) view.findViewById(R.id.leftpane);
		mRightpane = (LinearLayout) view.findViewById(R.id.rightpane);
		mBottompane = (LinearLayout) view.findViewById(R.id.bottompane);
		control_pane = (LinearLayout) view.findViewById(R.id.controlpane);
		mTextDateTime = (TextView) view.findViewById(R.id.date_time);

		NT96655Manager.getInstance().setNIUIStateCallBack(uiCallBack);
		// 需要根据当前TV状态,更改TV out
	//	NT96655Manager.getInstance().setTVOutput(true);		//现在更改为默认tvout
		NT96655Manager.getInstance().doGetMainUIState();
		CameraDeviceCtrlEx.getInstance().openCamera();
	}

	private void updateUI(int[] items) {
		//[0] sd卡， [1]循环录像状态  [2,3]录像时间  [4]移动侦测 [5]wdr 状态   [6]当前分辨率 [7] 当前声音
		isSDCardEnabled = items[NTData.MAIN_ITEM_SDCARD] == 0 ? false : true;
		mImgSDCardView.setBackground(getResources().getDrawable(
				Res.ICON_SDCARD[items[NTData.MAIN_ITEM_SDCARD]]));
		mImgTimeLongView.setBackground(getResources().getDrawable(
				Res.ICON_LOOP_RECORDER[items[NTData.MAIN_ITEM_LOOP_RECORDER]]));
		mImgSportView
				.setBackground(getResources()
						.getDrawable(
								Res.ICON_MOTION_DETECTION[items[NTData.MAIN_ITEM_MOTION_DETECTION]]));
		mImgWdrView.setBackground(getResources().getDrawable(
				Res.ICON_WDR[items[NTData.MAIN_ITEM_WDR]]));
		mImgRelotionView.setBackground(getResources().getDrawable(
				Res.ICON_RESOLUTION[items[NTData.MAIN_ITEM_RESOLUTION]]));
		isSoundRecorder = items[NTData.MAIN_ITEM_SOUND] == 0 ? true : false;
		mTglSoundBtn.setBackground(getResources().getDrawable(
				items[NTData.MAIN_ITEM_SOUND]));
	}

	private NIUIStateCallBack uiCallBack = new NIUIStateCallBack() {

		@Override
		public void onBackMainUIData(int[] items) {
			// TODO Auto-generated method stub
			// 00 08 01 01 00 00 03 01 00 01
			// txbuf = 65 76 20 00 08 01 01 00 00 03 01 00 01 4a 71 0d 0a
			updateUI(items);
			// 获取SD卡状态 00 SD卡异常 01 SD卡正常
			int state = items[NTData.MAIN_ITEM_SDCARD];
			// 获取循环录像状态 00 循环录影为关 01 循环录影为1分钟 02 循环录影为2分钟 03 循环录影为3分钟
			state = items[NTData.MAIN_ITEM_LOOP_RECORDER];
			// 获取录像时间 00没有录影 录影时间为0
			// XX XX实际录影时间，高字节在前，低字节在后，例如，录影时间为 00:04:35 则发送01 13
			state = items[NTData.MAIN_ITEM_RECORDER_TIME];
			// 获取移动侦测状态 00 移动侦测当前为高 01 移动侦测单前为中 02 移动侦测单前为低 03 移动侦测单前为关
			state = items[NTData.MAIN_ITEM_MOTION_DETECTION];
			// 获取WDR状态 WDR当前为关 WDR为开
			state = items[NTData.MAIN_ITEM_WDR];
			// 获取当前分辨率 00 当前分辨率为1080FHD 01当前分辨率为720P 02当前分辨率为WVGA 03当前分辨率为VGA
			state = items[NTData.MAIN_ITEM_RESOLUTION];
			// 获取当前是否录制声音 当前声音mic设置为关 01前声音mic设置为关
			state = items[NTData.MAIN_ITEM_SOUND];
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// CameraDeviceCtrlEx.getInstance().openCamera();
		// boolean show = intent.getBooleanExtra("visiable", false);
		if (!intent.getBooleanExtra("ui_visiable", false)) {
			setDisplayMode(DisplayMode.MINI);
		}
		return START_STICKY;
	}

	// 界面最小化接口
	private CallBackHideInterface mHideCallBack = null;

	public interface CallBackHideInterface {
		void onHide();
	}

	public void setMiniUICallBack(CallBackHideInterface callBack) {
		this.mHideCallBack = callBack;
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.image_back_btn:
				setDisplayMode(DisplayMode.MINI);
				mHideCallBack.onHide();
				break;
			case R.id.image_setting_btn: {
				setDisplayMode(DisplayMode.MINI); // 首先设置界面最小化
				Intent i = new Intent(MainService.this, SettingsActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(i);
				break;
			}
			case R.id.image_switch_btn:
				NT96655Manager.getInstance().doSwitchPicInPic();
				break;
			case R.id.image_play_btn:
				// USB更改为存储模式
				NT96655Manager.getInstance().setUSBMode(
						NT96655Util.USB_MODE_STORE);
				break;
			case R.id.image_recorder_btn:
				NT96655Manager.getInstance().doSwitchRecorder();
				break;
			case R.id.image_picture_btn:
				NT96655Manager.getInstance().doTakePicture();
				break;
			case R.id.toggle_sound_btn:
				// NT96655Manager.getInstance().setRecorderSound(!isSoundRecorder);
				break;
			case R.id.toggle_lock_btn:
				break;
			case R.id.camera_preview:
				switchMaxUI(!bShowMax);
				Log.d("zoufeng", "this is R.id.camera_preview clicked...... ");
				break;
			}
		}
	};
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				if (MyApplication.LOGIN_STATE)
					CameraDeviceCtrlEx.getInstance().startUpload();
				else {
					yun_video.setChecked(false);
					Toast.makeText(mContext,
							getResources().getString(R.string.net_error), 0)
							.show();
				}
			} else {
				CameraDeviceCtrlEx.getInstance().stopUpload();
			}
		}
	};

	private void switchMaxUI(boolean bshow) {
		RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mSurfaceView
				.getLayoutParams();
		if (bshow) {
			para.height = 480;
			para.width = 854;
			para.setMargins(0, 0, 0, 0);
		} else {
			para.height = 263;
			para.width = 405;
			para.setMargins(0, 72, 0, 0);
		}
		mSurfaceView.setLayoutParams(para);
		bShowMax = bshow;
	}

	/*	*/
	public void setDisplayMode(boolean maxUi) {

	}

	public void setDisplayMode(DisplayMode dm) {
		ViewGroup.LayoutParams lp = mRecordView.getLayoutParams();
		if (dm == DisplayMode.MAX) {
			lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
			lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
			Log.d("zoufeng", "lp.width = " + lp.width + "; lp.height  = "
					+ lp.height);
		} else {
			lp.width = 1;
			lp.height = 1;
		}
		mWindowManager.updateViewLayout(mRecordView, lp);
	}

	private static final String TEST_ACCOUNT = "15999501864";
	private static final String TEST_PASSWORD = "12345678a";

	private void login() {
		// showLoading();
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				return Closeli.getInstance().login(TEST_ACCOUNT, TEST_PASSWORD);
			}

			@Override
			protected void onPostExecute(final Boolean result) {
				MyApplication.LOGIN_STATE = result;
				Log.d("MyApplicationliujh", "login_result:" + result);
			}
		}.execute();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				if (!MyApplication.LOGIN_STATE)
					login();
			}
		}
	};

	public void onDestroy() {
		Closeli.getInstance().uninit();
		unregisterReceiver(broadcastReceiver);
	};
}
