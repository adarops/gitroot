package com.ilincar.camera;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.test.TestInterface;
import com.ilincar.ntmode.NT96655Manager;
import com.ilincar.utils.Utils;

/**
 * Created by zoufeng on 3/2/16.
 */
public class CameraFragment extends Fragment implements NT96655Manager.NITVOutChangeCallBack {

    private final String TAG = "CameraFragment";
    private CameraPreView mPreView;
    //private CameraDeviceCtrl mCameraDeviceCtrl;
    private CameraDeviceManagerImpl mCameraDeviceManagerImpl;

//	public CameraDeviceManagerImpl getCameraDeviceManagerImpl() {
//		return mCameraDeviceManagerImpl;
//	}

    private boolean mMaxUI = false;
   // private TestInterface mSwitchScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreView = new CameraPreView(this.getActivity());
        mPreView.setOnClickListener(onClick);
     //   mSwitchScreen = new TestInterface();
     //   NT96655Manager.getInstance().setNITVOutChangeCallBack(this);
    }

    @Override
    public void onTVOutChange() {
        if (mMaxUI) {
            Log.d("zoufeng", "lcmSwitchTo655");
        //    mSwitchScreen.lcmSwitchTo655();
        //    Utils.lcmSwitchTo655();
        } else {
            Log.d("zoufeng", "lcmSwitchTo35");
        //    mSwitchScreen.lcmSwitchTo35();
        //    Utils.lcmSwitchTo35();
        }
    }

    private void switchUISize(boolean maxUI) {
        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) mPreView
                .getLayoutParams();
        if (maxUI) {
            para.height = 480;
            para.width = 854;
            para.setMargins(0, 0, 0, 0);
            //	NT96655Manager.getInstance().setTVOutput(false);
            //NT96655Manager.getInstance().doTvOutPut(false);
        } else {
            para.height = 263;
            para.width = 405;
            para.setMargins(0, 72, 0, 0);
            //	NT96655Manager.getInstance().setTVOutput(true);
            //NT96655Manager.getInstance().doTvOutPut(true);
        }
        mPreView.setLayoutParams(para);
        mMaxUI = maxUI;
    }

    private OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mPreView != null) {
                switchUISize(!mMaxUI);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mPreView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 打开摄像头
        mPreView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreView.onPause();
    }
}
