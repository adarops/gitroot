package com.ilincar.drivingrecorder2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.closeli.camera.Closeli;
import com.ilincar.camera.CameraDeviceCtrlEx;
import com.ilincar.camera.CameraFragment;
import com.ilincar.ntmode.NT96655Manager;
import com.ilincar.ntmode.NT96655Manager.NIUIStateCallBack;
import com.ilincar.utils.NTData;
import com.ilincar.utils.Res;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IlincarMainActivity extends Activity implements NT96655Manager.NISDCardErrorCallBack{

    private final String TAG = "MainActivity";

    private final static int MAIN_UI_DATA_CHANGE = 1001;
    private final static int MSG_UPDATE_MAINUI = 1002;
    private final static int MSG_SWITCH_RECORDER = 1003;

    private Context mContext;
    private ImageButton mImgBackBtn;
    private ToggleButton mTglSoundBtn;
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
    private boolean isSDCardEnabled = false;
    private boolean isSoundRecorder = false;
    private boolean isRecording = false;

    private IntentFilter intentFilter;
    private ToggleButton yun_video;
    private boolean isUploading = false;


    private TextView mTextRecorderTime;
    private TextView mTextSystemTime;
    private int[] mMainUIData = null;

    private int mCurrentRecorderTime = 0, mRecorderTotalTime = 0;

    private static final int INTERVAL_UPDATE_UI = 10;//每间隔10秒就重新刷新一次界面,mCurrentCount为30数的计数器
    private int mCurrentCount = 0;
    private boolean mMotionDetectionEnabled = false;

    //private TestInterface mSwitchScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.getApplicationContext();

        FragmentManager fm = getFragmentManager();
        CameraFragment camfrag = (CameraFragment) fm
                .findFragmentById(R.id.camera_frag);
        // camfrag.set
        mTglSoundBtn = (ToggleButton) findViewById(R.id.toggle_sound_btn);
        mTglLockBtn = (ImageButton) findViewById(R.id.toggle_lock_btn);
        mTglSoundBtn.setOnCheckedChangeListener(onCheckedChangeListener);

        mTglLockBtn.setOnClickListener(onClickListener);

        mImgBackBtn = (ImageButton) findViewById(R.id.image_back_btn);
        mImgSettingBtn = (ImageButton) findViewById(R.id.image_setting_btn);
        mImgSwitchBtn = (ImageButton) findViewById(R.id.image_switch_btn);
        mImgPlayBtn = (ImageButton) findViewById(R.id.image_play_btn);
        mImgRecorderBtn = (ImageButton) findViewById(R.id.image_recorder_btn);
        mImgTakePictureBtn = (ImageButton) findViewById(R.id.image_picture_btn);
        mImgBackBtn.setOnClickListener(onClickListener);
        mImgSettingBtn.setOnClickListener(onClickListener);
        mImgSwitchBtn.setOnClickListener(onClickListener);
        mImgPlayBtn.setOnClickListener(onClickListener);

        //	mImgRecorderBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        mImgRecorderBtn.setOnClickListener(onClickListener);
        mImgTakePictureBtn.setOnClickListener(onClickListener);

        mImgSDCardView = (ImageView) findViewById(R.id.iamge_sd_card);
        mImgTimeLongView = (ImageView) findViewById(R.id.image_time_long);
        mImgSportView = (ImageView) findViewById(R.id.image_sport);
        mImgWdrView = (ImageView) findViewById(R.id.image_wdr);
        mImgRelotionView = (ImageView) findViewById(R.id.image_relotion);
        yun_video = (ToggleButton) findViewById(R.id.yun_video);
        yun_video.setOnCheckedChangeListener(onCheckedChangeListener);

        mTextRecorderTime = (TextView) findViewById(R.id.recorder_time);
        mTextSystemTime = (TextView) findViewById(R.id.date_time);

        // NT96650API.getInstance().start();

        // 需要根据当前TV状态,更改TV out
        NT96655Manager.getInstance().setNIUIStateCallBack(uiCallBack);
        NT96655Manager.getInstance().setNISDCardErrorCallBack(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

    //    mSwitchScreen = new TestInterface();
    //    mSwitchScreen.lcmSwitchTo655();

        //    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_MAINUI, 2000);
    }

    @Override
    public void onSDCardError() {
        new AlertDialog.Builder(IlincarMainActivity.this).setTitle("SD卡错误")
                .setMessage("SD卡发生错误，是否格式化SD卡").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NT96655Manager.getInstance().formatSdcard();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void updateUI() {
        if (mMainUIData == null)
            return;
        //[0] sd卡， [1]循环录像状态  [2,3]录像时间  [4]移动侦测 [5]wdr 状态   [6]当前分辨率 [7] 当前声音
        isSDCardEnabled = mMainUIData[NTData.MAIN_ITEM_SDCARD] == 0 ? false : true;
        mImgSDCardView.setBackground(getResources().getDrawable(
                Res.ICON_SDCARD[mMainUIData[NTData.MAIN_ITEM_SDCARD]]));

        mImgTimeLongView.setBackground(getResources().getDrawable(
                Res.ICON_LOOP_RECORDER[mMainUIData[NTData.MAIN_ITEM_LOOP_RECORDER] - 1 ]));

        mRecorderTotalTime = mMainUIData[NTData.MAIN_ITEM_LOOP_RECORDER] * 60;
        int showTime = mMainUIData[2];
        if(isRecording != (showTime == 0 ? false : true)){
            mImgRecorderBtn.setEnabled(true);
        }
        isRecording = showTime == 0 ? false : true;
        mCurrentRecorderTime = showTime;
        if (isRecording) {
            mImgRecorderBtn.setBackground(getResources().getDrawable(R.drawable.recorder_enabled_btn));
        } else {
            mImgRecorderBtn.setBackground(getResources().getDrawable(R.drawable.recorder_off_btn));
        }

        mMotionDetectionEnabled = mMainUIData[NTData.MAIN_ITEM_MOTION_DETECTION] != 3 ? true : false;

        mImgSportView
                .setBackground(getResources()
                        .getDrawable(
                                Res.ICON_MOTION_DETECTION[mMainUIData[NTData.MAIN_ITEM_MOTION_DETECTION]]));
        mImgSportView.setEnabled(!mMotionDetectionEnabled);

        mImgWdrView.setBackground(getResources().getDrawable(
                Res.ICON_WDR[mMainUIData[NTData.MAIN_ITEM_WDR]]));

        mImgRelotionView.setBackground(getResources().getDrawable(
                Res.ICON_RESOLUTION[mMainUIData[NTData.MAIN_ITEM_RESOLUTION]]));

        isSoundRecorder = mMainUIData[NTData.MAIN_ITEM_SOUND] == 0 ? false : true;
        mTglSoundBtn.setChecked(isSoundRecorder);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        NT96655Manager.getInstance().doGetMainUIState();
        mHandler.post(timeRun);
        if (isUploading) {
            yun_video.setChecked(true);
        }
    }

    private Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isRecording) {
                if (mCurrentRecorderTime >= mRecorderTotalTime)
                    mCurrentRecorderTime = 0;
                mTextRecorderTime.setText(String.format("%02d:%02d:%02d", mCurrentRecorderTime / 3600,
                        mCurrentRecorderTime / 60, mCurrentRecorderTime % 60));
                mCurrentRecorderTime++;
            }
            //每隔60秒同步一次主界面
            if(mCurrentCount > INTERVAL_UPDATE_UI) {
            //    NT96655Manager.getInstance().doGetMainUIState();
                mCurrentCount = 0;
            }
            mCurrentCount++;
            //设置系统时间
            long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            mTextSystemTime.setText(format.format(new Date(time)));
            mHandler.postDelayed(timeRun, 1000);
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MAIN_UI_DATA_CHANGE:
                    updateUI();
                    break;
                case MSG_UPDATE_MAINUI:
                //    NT96655Manager.getInstance().doGetMainUIState();
                    break;
                case MSG_SWITCH_RECORDER:
                    NT96655Manager.getInstance().doSwitchRecorder();
            }
        }
    };

    private NIUIStateCallBack uiCallBack = new NIUIStateCallBack() {

        @Override
        public void onBackMainUIData(int[] items) {
            // TODO Auto-generated method stub
            // 00 08 01 01 00 00 03 01 00 01
            // txbuf = 65 76 20 00 08 01 01 00 00 03 01 00 01 4a 71 0d 0a
            //	updateUI(items);
            mMainUIData = items;
            mHandler.sendEmptyMessage(MAIN_UI_DATA_CHANGE);
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

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            switch (buttonView.getId()) {
                case R.id.toggle_sound_btn:
                    if(isSoundRecorder != isChecked)
                        NT96655Manager.getInstance().setRecorderSound(isChecked);
                    isSoundRecorder = isChecked;
                    Drawable soundDrawable = isSoundRecorder ? getResources().getDrawable(R.drawable.icon_record_on)
                            : getResources().getDrawable(R.drawable.icon_record_off);
                    mTglSoundBtn.setBackground(soundDrawable);
                    break;
                case R.id.yun_video:
                    if (isChecked) {
                        if (MyApplication.LOGIN_STATE) {
                            CameraDeviceCtrlEx.getInstance().startUpload();
                            isUploading = true;
                        } else {
                            yun_video.setChecked(false);
                            isUploading = false;
                            login();
                            Toast.makeText(mContext,
                                    "正在尝试重新登录......", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        CameraDeviceCtrlEx.getInstance().stopUpload();
                    }
                    break;
            }
        }

    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.image_back_btn:
                    finish();
                    break;
                case R.id.image_setting_btn: {
                    if(isRecording){
                        ///进入设置后，需要将当前正在录制暂停
                        NT96655Manager.getInstance().doSwitchRecorder();
                    }
                    Intent i = new Intent(IlincarMainActivity.this,
                            SettingsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                    break;
                }
                case R.id.image_switch_btn:
                    NT96655Manager.getInstance().doSwitchPicInPic();
                    break;
                case R.id.image_play_btn:
                    // USB更改为存储模式
                    // mNT96655Manager.doInPlayBackMenu();
                    //	NT96655Manager.getInstance().setUSBMode(
                    //			NT96655Util.USB_MODE_STORE);
                    NT96655Manager.getInstance().formatSdcard();
                    break;
                case R.id.image_recorder_btn:
                    mImgRecorderBtn.setEnabled(false);
                    NT96655Manager.getInstance().doSwitchRecorder();
                    break;
                case R.id.image_picture_btn:
                    NT96655Manager.getInstance().doTakePicture();
                    break;
                case R.id.toggle_sound_btn:
                    NT96655Manager.getInstance().setRecorderSound(!isSoundRecorder);
                    break;
                case R.id.toggle_lock_btn:
                    NT96655Manager.getInstance().doLockCamera();
                    break;
                // case R.id.relative_camera_frag:
                // Log.d("zoufeng","relative_camera_frag clicked...... ");
                // break;
            }
        }
    };
    private static final String TEST_ACCOUNT = "15999501864";
    private static final String TEST_PASSWORD = "12345678a";

    private void login() {
    //    showLoading();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return Closeli.getInstance().login(TEST_ACCOUNT, TEST_PASSWORD);
            }

            @Override
            protected void onPostExecute(final Boolean result) {
      //          hideLoading();
                if(!result)
                    Toast.makeText(mContext,"登录失败",Toast.LENGTH_LONG).show();
                MyApplication.LOGIN_STATE = result;
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mHandler.removeCallbacks(timeRun);
        if (isUploading) {
       //     CameraDeviceCtrlEx.getInstance().stopUpload();
            isUploading = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Closeli.getInstance().uninit();
        unregisterReceiver(broadcastReceiver);
    }

    private void showLoading() {
        if (loadingProgressCircle == null) {
            loadingProgressCircle = ProgressDialog.show(this, null,
                    "Loading...", true, false);
        }
        loadingProgressCircle.show();
    }

    private void hideLoading() {
        if (loadingProgressCircle != null) {
            loadingProgressCircle.dismiss();
        }
    }

    private ProgressDialog loadingProgressCircle = null;
}
