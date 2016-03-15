package com.ilincar.drivingrecorder2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class FirstActivity extends Activity {

	private Intent intentService;
	private MainService mMainService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intentService = new Intent(this, MainService.class);
		startService(intentService);
		bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMainService != null){
			Log.d("zoufeng","MainService.DisplayMode.MAX");
			mMainService.setDisplayMode(MainService.DisplayMode.MAX);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mMainService != null)
			mMainService.setDisplayMode(MainService.DisplayMode.MINI);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unbindService(mServiceConnection);
	}

    //最小化界面
    private void finishUI(){
        this.finish();
    }
    
    public MainService.CallBackHideInterface onHideBack = new MainService.CallBackHideInterface(){
        @Override
        public void onHide() {
            finishUI();
        }
    };
    
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			// CLog.d("mServiceConnection-onServiceDisconnected");
			
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mMainService = ((MainService.MainServiceBinder) service)
					.getService();
			mMainService.setMiniUICallBack(onHideBack);
			mMainService.setDisplayMode(MainService.DisplayMode.MAX);
		}
	};
}
