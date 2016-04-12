package com.movon.fcw;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Adas;
import android.hardware.Camera.AdasDetectionListener;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;

import java.util.List;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.view.ViewGroup.LayoutParams; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Math;

import com.roadrover.carinfo.ICarListener;
import com.roadrover.carinfo.ICarService;
// import com.roadrover.common.CarDataParser;




import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.graphics.YuvImage;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;


public class MDAS2 extends Activity implements TextureView.SurfaceTextureListener,Camera.AdasDetectionListener{
	final int PREVIEW_WIDTH = 640;
	final int PREVIEW_HEIGHT = 480; 
	final int DISP_WIDTH = 854;
	final int DISP_HEIGHT = 480;
	
	final int NUM_BUFFERS = 5;
	final int BUFFER_SIZE = PREVIEW_WIDTH * PREVIEW_HEIGHT*3/2;
    private DrawOnTop mDraw;
    private Thread mThread;
    
    static final private String TAG = "MDAS";
    private boolean mFirstSetParam;// 
    private TextureView mTextureView;
    public SharedSettingItem mParam;
    public SharedPreferences mPref;
    public SharedPreferences.Editor mPrefEditor;

	AlertDialog.Builder mSettingDlg;
	DialogInterface mPopupDlg = null;

	private float mLastMotionX = 0;
	private float mLastMotionY = 0;
	
	private int mTouchSlop;
	
	private boolean mHasPerformedLongPress;
	private CheckForLongPress mPendingCheckForLongPress;
	
	private Handler mLCHandler = null;
	
	Intent mSettingIntent = null;
	
    TextView gps_speed;

    LocationManager locationManager;
    
    int speed_kph = 100;
    int speed_mph = 0;
    int mCanData = 0;
    int speed_select = 0;
    int ttc_select = 0;    
    int preview_width = PREVIEW_WIDTH;
    int preview_height = PREVIEW_HEIGHT;
    Camera mCamera;
    public boolean mbJPGSave = false;
    public int frameCount = 0;
    public long max_time=0;
    public long sum_time=0;
    int disp_width=DISP_WIDTH;
    int disp_height=DISP_HEIGHT;
    
    public static final int	ADAS_CAN_DATA_SPEED = 0;//CAN data and speed
    public static final int	ADAS_FRAME_WIDTH =1; //
    public static final int	ADAS_FRAME_HEIGHT=2;
    public static final int	ADAS_APP_DISP_WIDTH=3;
    public static final int ADAS_APP_DISP_HEIGHT=4;
    public static final int	ADAS_CAMERA_FRAME=5;
    public static final int	ADAS_CAMERA_HEIGHT=6;
    public static final int	ADAS_TTC=7;
    public static final int	ADAS_VANISH_LINE=8;
    public static final int ADAS_CAMERA_CENTER_OFFSET=9;
    public static final int	ADAS_CAR_WIDTH=10;
    public static final int	ADAS_CAMERA_2_WHEEL=11;
    public static final int	ADAS_CAM_2_BUMPER=12;
    public static final int	ADAS_LDW_SENSITY_LEFT=13;
    public static final int	ADAS_LDW_SENSITY_RIGHT=14;
    public static final int	ADAS_HOOD_LINE=15;
    public static final int	ADAS_DISTANCE=16;
    public static final int	ADAS_LDW_VELOCITY=17;//g_ldw_velocity
    public static final int	ADAS_FCW_ACTIVITY_VELOCITY=18;//g_fcw_activity_velocity
    public static final int	ADAS_VEHICLE_CENTER_LINE=19;
    
    public static final int MSG_ID_ADAS_DATA = 12;
    public static final int MSG_ID_CAN_DATA = 10;
    private final Handler mHandler = new Handler () {
    	
    	public void handleMessage ( Message msg ) {
    		super.handleMessage(msg);
    		if ( msg.what == 0 ) {
    			mDraw.update();
    			if (mParam.getSpeedType()){
    				gps_speed.setText(speed_mph + " Miles/h");
    			}
    			else{
    				gps_speed.setText(speed_kph + " Km/h");
    			}
    			if ( mCamera != null ) 
    	     		mCamera.setAdasParameters(ADAS_CAN_DATA_SPEED, speed_kph | (mCanData << 16));
    		}
    		else if ( msg.what == 1 ) {
    			Toast toast = Toast.makeText(getApplicationContext(),
  					   "Turn ON GPS first!!!", Toast.LENGTH_LONG);
  				toast.setGravity(Gravity.CENTER, 0, 0);
  				toast.show();
    			finish();
    		}
    		else if ( msg.what == 2 ) {

    		}
    		else if ( msg.what == 3 ) {
    			mDraw.update();
    		}
    		else if(msg.what == MSG_ID_CAN_DATA)
    		{
    			int state = msg.arg1;
    			if(state == 1)
    			{
    				mCanData = 0x01;
    				mCamera.setAdasParameters(ADAS_CAN_DATA_SPEED,speed_kph | (0x01<<16));
    			}
    			else if(state == 2)
    			{
    				mCanData = 0x02;
    				mCamera.setAdasParameters(ADAS_CAN_DATA_SPEED,speed_kph | (0x02<<16));
    			}
    			else
    			{
    				mCanData = 0x0;
    				mCamera.setAdasParameters(ADAS_CAN_DATA_SPEED,speed_kph | (0x0<<16));
    			}
    			
    		}
    		else if(msg.what == MSG_ID_ADAS_DATA)
    		{
    			mDraw.setAdasResult((Adas)msg.obj);
    			//Toast.makeText(getApplicationContext(),
  				//	   "Adas message is called", Toast.LENGTH_SHORT).show();
    		}
    		else {
    			Toast toast = Toast.makeText(getApplicationContext(),
 					   "Wrong message is clicked", Toast.LENGTH_LONG);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
    		}
    	}  		
   };
   
//   public static class CarLight
//   {
//       public int directionLamp;// 饔у릲��0 �놂펽1藥�쉬�묕펽2�녘쉬��//       
//       public int widthLamp;// 鹽뷴퍜��//       
//       public int emergencyLamp;// 榮㎪���펷�뚪뿪竊�//       
//       public int frontforkLamp;// �띺쎗��//       
//       public int farLamp;// 瓦쒎뀎��//       
//       public int nearLamp;// 瓦묈뀎��//       
//       public int dayLamp;// �ι뿴烏뚩쉐��//       
//       public int stopLamp;// �배쉐��//       
//       public int backLamp;// �믦쉐��//       public int backforkLamp;// �롩쎗��//   }
   
 
//   CarDataParser.CarLight mCarLight;
   private ICarService mService = null;
    
   private ServiceConnection osc = new ServiceConnection()
   {
       
       @Override
       public void onServiceConnected(ComponentName name, IBinder service)
       {
           // TODO Auto-generated method stub
           mService = ICarService.Stub.asInterface(service);
           try
           {
               mService.requestRRUpdates(0, mCarListener);
               mHandler.post(new Runnable()
               {
                   public void run()
                   {
                       // TODO Auto-generated method stub
                       //getCarDataParser();
                   }
               });
           }
           catch (RemoteException e)
           {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
       
       @Override
       public void onServiceDisconnected(ComponentName name)
       {
           // TODO Auto-generated method stub
           mService = null;
       }
       
   };
   
   private ICarListener mCarListener = new ICarListener.Stub()
   {
       @Override
       public int onDeviceParamChanged(int device, int paramid, byte[] value)
           throws RemoteException
       {
           if (null == value)
               return -1;
           parseCarLightStatus(value);
           return 0;
       }

	@Override
	public int onCommonParamChange(int arg0, int arg1, int arg2, byte[] arg3)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int onDeviceInfo(int arg0, int arg1, byte[] arg2)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
};

 private void parseCarLightStatus(byte[] data)
   {
       if (null == data)
           return;
//       if (mCarLight == null)
//           mCarLight = new CarDataParser.CarLight();
//       int direction = mCarLight.directionLamp;
//       if (0 == CarDataParser.parseLightdata(data, mCarLight))
//       {
//    	   if(mCarLight.directionLamp != direction)
//    	   {
//    		   Message msg = new Message();
//    		   msg.what = 10;
//    		   msg.arg1 = mCarLight.directionLamp;
//    		   mHandler.sendMessage(msg);
//    	   }
//       }
   }

  
   public int getSpeed ( ) { return speed_kph; }
   
   private BackPressCloseHandler backPressCloseHandler;
  
	
   boolean chkGpsService() {
	   String gps = android.provider.Settings.Secure.getString(getContentResolver(),android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	   if ( !(gps.matches(".*gps.*") && gps.matches(".*network.*")) ) {
		   return false;
   }else{ 
		   return true;
	   }
   }   	 
	 
	 LocationListener locationListener = new LocationListener() {
     	public void onLocationChanged(Location location) {
     		speed_kph = (int)(location.getSpeed()*3.6);
     		speed_mph = (int)(Math.round(100*(speed_kph) / 1.6))/100;
     		if ( mHandler != null ) mHandler.sendEmptyMessage(0);     		
     	}
     	public void onStatusChanged(String provider, int status, Bundle extras) { }
     	public void onProviderEnabled(String provider) { }
     	public void onProviderDisabled(String provider) { }
     };
  
	 public void loadGps() {
		 String context = Context.LOCATION_SERVICE;
		 locationManager = (LocationManager)getSystemService(context);
		 Criteria criteria = new Criteria();
		 criteria.setAccuracy(Criteria.ACCURACY_FINE);
		 criteria.setPowerRequirement(Criteria.POWER_LOW);
		 criteria.setAltitudeRequired(false);
		 criteria.setBearingRequired(false);
		 criteria.setSpeedRequired(false); 
		 criteria.setCostAllowed(true);
		 		 
		 boolean result = chkGpsService();
		 if(result){
			String provider = locationManager.getBestProvider(criteria, true);	 
		    locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
		 }
		 else {
			 //mHandler.sendEmptyMessage(1);
		 }
	 }

	private void setParameters()
	{
		//mParam.setDispWidth( mPref.getInt( "DispWidth", disp_width ) );
		//mParam.setDispHeight( mPref.getInt( "DispHeight", disp_height ) );

		mParam.setDispWidth( disp_width );
		mParam.setDispHeight( disp_height );
		
		mParam.setSpeedType( mPref.getBoolean( "SpeedType", false) );
		mParam.setVanishLine( mPref.getInt( "VanishLine", disp_height/2 ) );
		mParam.setHoodLine( mPref.getInt( "HoodLine", disp_height-20 ) );
		mParam.setVehicleWidth( mPref.getInt( "VehicleWidth", 180 ) );
		mParam.setCameraHeight( mPref.getInt( "CameraHeight", 150 ) );
		mParam.setWheelLength( mPref.getInt( "WheelLength", 100 ) );
		mParam.setCameraCenter( mPref.getInt( "CameraCenter", 0 ) );
		mParam.setBumperLength( mPref.getInt( "BumperLength", 150 ) );
		mParam.setSensitivityLeft( mPref.getInt( "SensitivityLeft", 0 ) );
		mParam.setSensitivityRight( mPref.getInt( "SensitivityRight", 0 ) );
		mParam.setFCWSensitivity( mPref.getFloat( "FCWSensitivity", 1.2f ) );
		mParam.setNeedflush(mPref.getBoolean( "needflush", false));
	}
	
	private void putParameters()
	{		
		
		mPrefEditor.putInt(	"DispWidth", mParam.getDispWidth() );  
		mPrefEditor.putInt( "DispHeight", mParam.getDispHeight() );		

		// false: Km/h, true: Mp/h
		mPrefEditor.putBoolean(	"SpeedType", mParam.getSpeedType() );  
		mPrefEditor.putInt( "VanishLine", mParam.getVanishLine() );
		mPrefEditor.putInt( "HoodLine", mParam.getHoodLine() );
		mPrefEditor.putInt( "VehicleWidth", mParam.getVehicleWidth() );
		mPrefEditor.putInt( "CameraHeight", mParam.getCameraHeight() );
		mPrefEditor.putInt( "WheelLength", mParam.getWheelLength() );
		mPrefEditor.putInt( "CameraCenter", mParam.getCameraCenter() );
		mPrefEditor.putInt( "BumperLength", mParam.getBumperLength() );
		mPrefEditor.putInt( "SensitivityLeft", mParam.getSensitivityLeft() );
		mPrefEditor.putInt( "SensitivityRight", mParam.getSensitivityRight() );
		mPrefEditor.putFloat( "FCWSensitivity", mParam.getFCWSensitivity() );
		mPrefEditor.putBoolean(	"needflush", mParam.getNeedflush() ); 
		mPrefEditor.commit();
	}
	
	private void initParameters()
	{
		setParameters();

		if( mPref.getBoolean( "first", true ) ){
			
			mPrefEditor.putBoolean( "first", false );
			putParameters();
		}
	}

	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        LayoutParams param;
   	    param = new LayoutParams(LayoutParams.WRAP_CONTENT
   	    						, LayoutParams.WRAP_CONTENT);
   	    setContentView(R.layout.mdas_view);
   	    mTextureView = (TextureView)findViewById(R.id.preview_content);
   	    // Create our Preview view and set it as the content of our activity.
   	    Log.d(TAG,"onCreate start");
   	    mTextureView.setSurfaceTextureListener(this);
        
		mPref		= getSharedPreferences( "calib", MODE_PRIVATE );
		mPrefEditor = mPref.edit();
        mParam		= new SharedSettingItem();
                        
        mFirstSetParam = true;
        
		mLCHandler = new Handler();
		mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		
		mSettingIntent = new Intent(MDAS2.this, Settings.class);
		mDraw = new DrawOnTop(this);
		addContentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT
											, LayoutParams.WRAP_CONTENT));
		Point display_size = new Point();
		getWindowManager().getDefaultDisplay().getSize(display_size);
		
		int width = DISP_WIDTH;
		int height = DISP_HEIGHT;
		
		preview_width = PREVIEW_WIDTH;
		preview_height = PREVIEW_HEIGHT;
		
		Log.d(TAG,"oncreate1111111112222");
		gps_speed  = new TextView(this);
		gps_speed.setX(width*160/200);
		gps_speed.setY(height*14/200);
		gps_speed.setText(speed_kph + " Km/h");
		gps_speed.setTextColor(Color.YELLOW);
		gps_speed.setTextSize(width*3/200);
		
		AbsoluteLayout layout = new AbsoluteLayout(this);

		layout.setLayoutParams(param);
		layout.addView(gps_speed,param);
			
		addContentView(layout,new LayoutParams(width,height));
		backPressCloseHandler = new BackPressCloseHandler(this);
		Log.d(TAG,"oncreate111111111333");
		startActivity(new Intent(this, SplashScreen.class));
		loadGps();
		
		mSettingDlg = new AlertDialog.Builder(MDAS2.this);
		mSettingDlg.setTitle("Setting")
		.setMessage("To the parameter setting Press the yes button.\n"
		+"Starts automatically after 5 seconds when unselected.")
		.setPositiveButton(
			"Calibration", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					
					if(mFirstSetParam)	mFirstSetParam = false;
					mSettingIntent.putExtra("param", mParam);
					startActivity(mSettingIntent);
				}
			}).setNegativeButton("MDAS2", null);
		mSettingDlg.setCancelable(false);
		mSettingDlg.show();
		
		Log.d("MDAS2","bindserver start");
		
		if (mService == null)
        {
//            Intent intent = new Intent();
//            intent.setClassName("com.roadrover.carinfo", "com.roadrover.carinfo.CarService");
//            bindService(intent, osc, 0);
        }
        
	
		Log.d("MDAS2","bindserver end");
		
	
    }
  
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
	}
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setParameters();
		Log.d(TAG,"onResume start###########");
		if(mPref.getBoolean("needflush", false))
		{
			Log.d(TAG,"needflush=true");
			mPrefEditor.putBoolean(	"needflush",
					false );  
			mPrefEditor.commit();
			
			preview_width = PREVIEW_WIDTH;
			preview_height = PREVIEW_HEIGHT;
					
			int camera_height = mPref.getInt( "CameraHeight", 150 );
			int ttc = (int)(mPref.getFloat( "FCWSensitivity", 1.2f )*10);
			int vanish_line = mPref.getInt( "VanishLine", disp_height/2 )*preview_height/disp_height;
			if(mCamera != null)
			{
				Log.d(TAG,"onResume setAdasParameters");
				mCamera.setAdasParameters(ADAS_FRAME_WIDTH, PREVIEW_WIDTH);
				mCamera.setAdasParameters(ADAS_FRAME_HEIGHT, PREVIEW_HEIGHT);
				mCamera.setAdasParameters(ADAS_CAMERA_CENTER_OFFSET, mPref.getInt( "CameraCenter", 0 ));
				mCamera.setAdasParameters(ADAS_CAMERA_HEIGHT, camera_height);
				mCamera.setAdasParameters(ADAS_TTC, ttc);
				mCamera.setAdasParameters(ADAS_VANISH_LINE, vanish_line);
				mCamera.setAdasParameters (ADAS_CAR_WIDTH,mPref.getInt( "VehicleWidth", 180 ));
				mCamera.setAdasParameters (ADAS_CAMERA_2_WHEEL,mPref.getInt( "WheelLength", 100 ));
				mCamera.setAdasParameters (ADAS_CAM_2_BUMPER,mPref.getInt( "BumperLength", 150 ));
				mCamera.setAdasParameters (ADAS_LDW_SENSITY_LEFT,mPref.getInt( "SensitivityLeft", 0 ));
				mCamera.setAdasParameters (ADAS_LDW_SENSITY_RIGHT,mPref.getInt( "SensitivityRight", 0 ));
				mCamera.setAdasParameters (ADAS_HOOD_LINE,mPref.getInt( "HoodLine", disp_height-20 )*preview_height/disp_height);
				mCamera.setAdasParameters (ADAS_VEHICLE_CENTER_LINE,PREVIEW_WIDTH/2);
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				String imei = telephonyManager.getDeviceId();
				WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
				WifiInfo info = wifiMan.getConnectionInfo();  
				String mac = info.getMacAddress();
				mCamera.setAdasDeviceInfo(imei + "." + mac);								
			}
			
			
		}
		
		mDraw.setWillNotDraw(false);
		this.gps_speed.setVisibility(View.VISIBLE);
		Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mDraw.setWillNotDraw(true);
		this.gps_speed.setVisibility(View.GONE);
		
//		Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
	}

    @Override
    public boolean onTouchEvent (MotionEvent event) {

    	switch( event.getAction() ){
    	case MotionEvent.ACTION_DOWN:
    		mLastMotionX = event.getX();
    		mLastMotionY = event.getY();

    		mHasPerformedLongPress = false;

    		postCheckForLongClick(0);
    		break;
    	case MotionEvent.ACTION_MOVE:
    		final float x = event.getX();
    		final float y = event.getY();
    		final int deltaX = Math.abs((int)(mLastMotionX - x));
    		final int deltaY = Math.abs((int)(mLastMotionY - y));

    		if(deltaX >= mTouchSlop || deltaY >= mTouchSlop){
    			if( !mHasPerformedLongPress ){
    				removeLongPressCallback();
    			}
    		}
    		break;
    	case MotionEvent.ACTION_CANCEL:
    		if( !mHasPerformedLongPress ){
    			removeLongPressCallback();
    		}
    		break;  	
    	case MotionEvent.ACTION_UP:
    		if(!mHasPerformedLongPress){
    			removeLongPressCallback();
    			
    			performOneClick();
    		}
    		break;
    	default:	break;
    	}
    	return false;
    }
    
    class CheckForLongPress implements Runnable{
    	public void run(){
    		if( performLongClick() ){
    			mHasPerformedLongPress = true;
    		}
    	}
    }
    
    private void postCheckForLongClick(int delayOffset){
    	mHasPerformedLongPress = false;
    	
    	if(mPendingCheckForLongPress == null){
    		mPendingCheckForLongPress = new CheckForLongPress();
    	}
    	mLCHandler.postDelayed(mPendingCheckForLongPress,
    			ViewConfiguration.getLongPressTimeout() - delayOffset );
    }
    
    private void removeLongPressCallback(){
    	if(mPendingCheckForLongPress != null){
    		mLCHandler.removeCallbacks(mPendingCheckForLongPress);
    	}
    }
    
    public boolean performLongClick(){
//    	Toast.makeText(this, "Long click OK~~~!!", Toast.LENGTH_SHORT).show();
		mSettingDlg.setTitle("Setting")
		.setMessage("To the parameter setting Press the yes button.")
		.setPositiveButton(
			"Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					
					if(mFirstSetParam)	mFirstSetParam = false;
					mSettingIntent.putExtra("param", mParam);
					startActivity(mSettingIntent);
				}
		}).setNegativeButton("No", null);
		mSettingDlg.show();
    	return true;
    }
    private void performOneClick(){
//    	Toast.makeText(this, "One click OK~~~!!", Toast.LENGTH_SHORT).show();
    }
    
    
   @Override    
   public boolean onKeyDown(int keyCode, KeyEvent event) {
    	AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
    	switch (keyCode) {
    	case KeyEvent.KEYCODE_VOLUME_UP :
    		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    		return true;
    	case KeyEvent.KEYCODE_VOLUME_DOWN: 
    		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    		return true;
    	case KeyEvent.KEYCODE_BACK:
    		locationManager.removeUpdates(locationListener);
    		backPressCloseHandler.onBackPressed();
    		
    	}
    	return false;    
    }
   
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
    	switch (keyCode) {
    	case KeyEvent.KEYCODE_VOLUME_UP :
    		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    		return true;
    	case KeyEvent.KEYCODE_VOLUME_DOWN:
    		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    		return true;
    	}
    	return false;
    }
        
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int w,
			int h) {
		// TODO Auto-generated method stub
		int cameraId=0; 
        mCamera = Camera.open(cameraId);
        if ( mCamera == null )
        {
        	Log.e(TAG,"Camera pointer is null\n");
        	return;
        }
        Camera.Parameters parameters = (mCamera!=null)? mCamera.getParameters() : null;
    	parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
    	//parameters.setPreviewFpsRange(25000, 25000);
    	parameters.setPreviewFrameRate(25);//25
    	parameters.setExposureCompensation(0);
    	//parameters.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(parameters);
        mCamera.setAdasDetectionListener(this);
        initParameters();
		Log.d(TAG,"onSurfaceTextureAvailable111111111");
		int camera_height = mPref.getInt( "CameraHeight", 150 );
		int ttc = (int)(mPref.getFloat( "FCWSensitivity", 1.2f )*10);
		int vanish_line = mPref.getInt( "VanishLine", disp_height/2)*preview_height/disp_height; //default value for vanish line value is height/2		
		mCamera.setAdasParameters(ADAS_FRAME_WIDTH, PREVIEW_WIDTH);
		mCamera.setAdasParameters(ADAS_FRAME_HEIGHT, PREVIEW_HEIGHT);
		mCamera.setAdasParameters(ADAS_CAMERA_CENTER_OFFSET, mPref.getInt( "CameraCenter", 0 ));
		mCamera.setAdasParameters(ADAS_CAMERA_HEIGHT, camera_height);
		mCamera.setAdasParameters(ADAS_TTC, ttc);
		mCamera.setAdasParameters(ADAS_VANISH_LINE, vanish_line);
		mCamera.setAdasParameters (ADAS_CAR_WIDTH,mPref.getInt( "VehicleWidth", 180 ));
		mCamera.setAdasParameters (ADAS_CAMERA_2_WHEEL,mPref.getInt( "WheelLength", 100 ));
		mCamera.setAdasParameters (ADAS_CAM_2_BUMPER,mPref.getInt( "BumperLength", 150 ));
		mCamera.setAdasParameters (ADAS_LDW_SENSITY_LEFT,mPref.getInt( "SensitivityLeft", 0 ));
		mCamera.setAdasParameters (ADAS_LDW_SENSITY_RIGHT,mPref.getInt( "SensitivityRight", 0 ));
		mCamera.setAdasParameters (ADAS_HOOD_LINE,mPref.getInt( "HoodLine", disp_height-20 )*preview_height/disp_height);
		mCamera.setAdasParameters (ADAS_VEHICLE_CENTER_LINE,PREVIEW_WIDTH/2);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = wifiMan.getConnectionInfo();  
		String mac = info.getMacAddress();
		mCamera.setAdasDeviceInfo(imei + "." + mac);
		
        disp_width = w;
        disp_height = h;
        Toast.makeText(this,"w = " + w + "h=" + h, Toast.LENGTH_LONG).show();
        try {
           mCamera.setPreviewTexture(surface);
           mCamera.startPreview();
           mCamera.startAdasDetection();
        } catch (IOException exception) {
           mCamera.release();
           mCamera = null;
           return;
           // TODO: add more exception handling logic here
        }
        
        
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
		Log.e(TAG,"onSurfaceTextureDestroyed start 1");
		mCamera.stopAdasDetection();
		mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        Log.e(TAG,"onSurfaceTextureDestroyed end 1");
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int w,
			int h) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "width : " + w + ", height : " + h, Toast.LENGTH_LONG);
    	
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAdasDetection(Adas adas, Camera camera) {
		// TODO Auto-generated method stub
		//for ( int i=0; i < 9; i++) Log.d("sx1","sx1["+i+"]="+adas.ldw.left_lane.sx1[i]);
		Message message = Message.obtain(null, MSG_ID_ADAS_DATA, adas); 
		mHandler.sendMessage(message);
	}

}

class DrawOnTop extends View {
	public MDAS2 mFCWActivity;
	public static final int RESULT_VEHICLE_MAX = 16;
	public static final int RESULT_LANEPT_MAX = 9;
	public Adas mAdas = null;
	
	public int disp_width;
	public int disp_height;
	
	static int ALARM_LDW_DEPARTURE			= 0x00000001;
    static int ALARM_FCW_COLLISION_CAUTION	= 0x00000002;
    static int ALARM_FCW_COLLISION_ALERT	= 0x00000004;
    static int ALARM_FCW_COLLISION_SERIOUS	= 0x00000008;
    static int ALARM_FCW_FORWARD_NEAR		= 0x00000010;
    static int LDW_DEPARTURE_LEFT			= 0x00010000;
    static int LDW_DEPARTURE_RIGHT			= 0x00020000;
    static int FCW_COLLISION_CAUTION		= 0x00010000;
    static int FCW_COLLISION_DANGER			= 0x00020000;
    static int LDW_CAL_UPDATE_RESULT        = 0x00000001;
    static int LDW_CAL_APPLIED              = 0x00000010;
    
	static final String TAG= "Draw_ADAS";
	private  SoundPool sound_pool;
    private  int       sound_beep_basic;
    private  int       sound_beep_loud1;
    private  int       sound_beep_loud2;
    private  Vibrator  vibrator;
    private  AudioManager am;
    Bitmap   image;
    Bitmap   scaledImage;  
    int distance;
    
    public DrawOnTop(MDAS2 context) {
    	super(context);
    	mFCWActivity = context;	
 		am         = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
 		sound_pool = new SoundPool(5,AudioManager.STREAM_SYSTEM,0);
 		sound_beep_basic = sound_pool.load(getContext(), R.raw.basic,1); 		
 		sound_beep_loud1 = sound_pool.load(getContext(), R.raw.loud1,1);
 		sound_beep_loud2 = sound_pool.load(getContext(), R.raw.loud2,1);
 		vibrator   = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
      
    public void setAdasResult( Adas adas ) {
    	mAdas = adas;
    	if ( (mAdas.autocal_flags & LDW_CAL_UPDATE_RESULT) == LDW_CAL_UPDATE_RESULT ) {
    		//Toast.makeText(mFCWActivity, "LDW_CAL_UPDATE_RESULT : " + mAdas.autocal_vanish_line, Toast.LENGTH_SHORT).show();
    		if ( (mAdas.autocal_flags & LDW_CAL_APPLIED) == LDW_CAL_APPLIED ) {
    			Toast.makeText(mFCWActivity, "LDW_CAL_APPLIED : " + mAdas.autocal_vanish_line, Toast.LENGTH_SHORT).show();
    		}
    	}
    	invalidate();
    }
   
    private Bitmap getBitmapFromIndex(int digit)
    {
    	if ( digit ==0 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_0);
    	if ( digit ==1 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_1);
    	if ( digit ==2 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_2);
    	if ( digit ==3 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_3);
    	if ( digit ==4 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_4);
    	if ( digit ==5 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_5);
    	if ( digit ==6 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_6);
    	if ( digit ==7 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_7);
    	if ( digit ==8 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_8);
    	if ( digit ==9 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_9);
    	
    	
    	return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_0);
    }
    
    private Bitmap getBitmapFromIndexRed(int digit)
    {
    	if ( digit ==0 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_0);
    	if ( digit ==1 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_1);
    	if ( digit ==2 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_2);
    	if ( digit ==3 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_3);
    	if ( digit ==4 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_4);
    	if ( digit ==5 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_5);
    	if ( digit ==6 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_6);
    	if ( digit ==7 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_7);
    	if ( digit ==8 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_8);
    	if ( digit ==9 ) return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_9);
    	
    	
    	return BitmapFactory.decodeResource(getContext().getResources(), R.raw.digit_red_0);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	
//    	Toast.makeText(mFCWActivity, "onDraw()", Toast.LENGTH_SHORT).show();
    	 Paint paint = new Paint(); 
    	 paint.setStyle(Paint.Style.FILL); 
         paint.setColor(Color.WHITE);
         paint.setStrokeWidth(3);
         paint.setTextSize(32);
         if((mAdas == null) || (mFCWActivity.mCamera == null))
        	 return;
        
        
         paint.setAntiAlias(true);
         paint.setColor(Color.RED); 
     	disp_width = mFCWActivity.disp_width;
  		disp_height = mFCWActivity.disp_height;
  		//Log.d(TAG,"vanish_line=" + mAdas.vanish_line);
         canvas.drawRect(disp_width/3,mFCWActivity.mPref.getInt( "VanishLine", disp_height/2 ),disp_width*2/3,mFCWActivity.mPref.getInt( "VanishLine", disp_height/2 )+4,paint);
        
         {
	         if ((mAdas.ldw.resultFlags & ALARM_LDW_DEPARTURE) == ALARM_LDW_DEPARTURE) {
	        	 if ( am.getStreamVolume(AudioManager.STREAM_SYSTEM) != 0) {
	        		 sound_pool.play(sound_beep_basic, 1f, 1f, 0, 0, 1f);       				 
	        	 }
	        	 else {        				 
	        		 vibrator.vibrate(1000);        				 
	        	 }
	         }
	         
	         if ( mAdas.ldw.left_lane.reliability == 0 )             paint.setColor(Color.YELLOW);
	         else if ( ( mAdas.ldw.resultFlags & LDW_DEPARTURE_LEFT) == LDW_DEPARTURE_LEFT ) paint.setColor(Color.RED);
	         else                                     paint.setColor(Color.GREEN);
	         
	        
	         for ( int i=0; i < mAdas.ldw.left_lane.count1; i++) {
	        	 canvas.drawLine(mAdas.ldw.left_lane.sx1[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.left_lane.sy1[i]*disp_height/mFCWActivity.preview_height,
	        			 mAdas.ldw.left_lane.ex1[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.left_lane.ey1[i]*disp_height/mFCWActivity.preview_height,paint);	
	         }
	         for ( int i=0; i < mAdas.ldw.left_lane.count2; i++) {
	        	 canvas.drawLine(mAdas.ldw.left_lane.sx2[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.left_lane.sy2[i]*disp_height/mFCWActivity.preview_height,
	        			 mAdas.ldw.left_lane.ex2[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.left_lane.ey2[i]*disp_height/mFCWActivity.preview_height,paint);	
	         }
	         
	         if ( mAdas.ldw.right_lane.reliability == 0 ) paint.setColor(Color.YELLOW);
	         else if ( (mAdas.ldw.resultFlags & LDW_DEPARTURE_RIGHT) == LDW_DEPARTURE_RIGHT ) paint.setColor(Color.RED);
	         else                                     paint.setColor(Color.GREEN);         
	         
	         for ( int i=0; i < mAdas.ldw.right_lane.count1; i++) {
	        	 canvas.drawLine(mAdas.ldw.right_lane.sx1[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.right_lane.sy1[i]*disp_height/mFCWActivity.preview_height,
	        			 mAdas.ldw.right_lane.ex1[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.right_lane.ey1[i]*disp_height/mFCWActivity.preview_height,paint);	
	         }
	         for ( int i=0; i < mAdas.ldw.right_lane.count2; i++) {
	        	 canvas.drawLine(mAdas.ldw.right_lane.sx2[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.right_lane.sy2[i]*disp_height/mFCWActivity.preview_height,
	        			 mAdas.ldw.right_lane.ex2[i]*disp_width/mFCWActivity.preview_width,mAdas.ldw.right_lane.ey2[i]*disp_height/mFCWActivity.preview_height,paint);	
	         }
         }
         
         if ( (mAdas.fcw.resultFlags & ALARM_FCW_COLLISION_SERIOUS) == ALARM_FCW_COLLISION_SERIOUS ) {
 			if ( am.getStreamVolume(AudioManager.STREAM_SYSTEM) != 0) {
 				sound_pool.play(sound_beep_loud1, 1f, 1f, 0, 0, 1f);
 			}
 			else {
 				vibrator.vibrate(1000);
 			}
 			Toast.makeText(mFCWActivity, "Alert FCW Collison Serious", Toast.LENGTH_LONG).show();
 		}
 		else if ( (mAdas.fcw.resultFlags & ALARM_FCW_COLLISION_ALERT) == ALARM_FCW_COLLISION_ALERT ) {
 			if ( am.getStreamVolume(AudioManager.STREAM_SYSTEM) != 0) {
 				sound_pool.play(sound_beep_loud2, 1f, 1f, 0, 0, 1f);
 			}
 			else {
 				vibrator.vibrate(1000);
 			}
 			Toast.makeText(mFCWActivity, "Alert FCW Collision Alert", Toast.LENGTH_LONG).show();
 		}
 		else if ( (mAdas.fcw.resultFlags & ALARM_FCW_FORWARD_NEAR) == ALARM_FCW_FORWARD_NEAR ) {
 			if ( am.getStreamVolume(AudioManager.STREAM_SYSTEM) != 0) {
 				sound_pool.play(sound_beep_loud2, 1f, 1f, 0, 0, 1f);
 			}
 			else {
 				vibrator.vibrate(1000);
 			}
 			Toast.makeText(mFCWActivity, "Alert FCW Forward Near", Toast.LENGTH_LONG).show();
 		}
 		else if ( (mAdas.fcw.resultFlags & ALARM_FCW_COLLISION_CAUTION) == ALARM_FCW_COLLISION_CAUTION ) {
 			if ( am.getStreamVolume(AudioManager.STREAM_SYSTEM) != 0) {
 				sound_pool.play(sound_beep_loud2, 1f, 1f, 0, 0, 1f);
 			}
 			else {
 				vibrator.vibrate(1000);
 			}
 			Toast.makeText(mFCWActivity, "Alert FCW Collision Caution", Toast.LENGTH_LONG).show();
 		}
         
         for ( int i=0; i <  mAdas.fcw.count; i++ ) {
        	 if ( mAdas.fcw.w[i] > 0 ) {
        		 
        		 if ( ((mAdas.fcw.resultFlags & FCW_COLLISION_CAUTION) == FCW_COLLISION_CAUTION) && (i==mAdas.fcw.forwardVehicle)) {
        			 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.car_red);
        		 }
        		 else {
        			 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.car_green);
        		 }
        		 scaledImage = Bitmap.createScaledBitmap(image,mAdas.fcw.w[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.h[i]*disp_height/mFCWActivity.preview_height, true);
		         canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);
		         Log.d("distance", "distance = " + mAdas.fcw.distance[i]);
		         distance = mAdas.fcw.distance[i]/100;
		         
		         
		         if ( ((mAdas.fcw.resultFlags & FCW_COLLISION_CAUTION) == FCW_COLLISION_CAUTION) && (i==mAdas.fcw.forwardVehicle) ) {
		        	 
		        	 if (distance >= 10) {
		        		 image = getBitmapFromIndexRed((distance/10)%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);
		        		 image = getBitmapFromIndexRed(distance%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width + 32,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);	        	 
		        	 }
		        	 else if (distance < 10) {
		        		 image = getBitmapFromIndexRed(distance%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width + 32,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);		        	 
		        	 }
		         }
		         else {
		        	 if (distance >= 10) {
		        		 image = getBitmapFromIndex((distance/10)%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);
		        		 image = getBitmapFromIndex(distance%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width + 32,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);	        	 
		        	 }
		        	 else if (distance < 10) {
		        		 image = getBitmapFromIndex(distance%10);
		        		 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
		        		 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height, null);		        	 
		        	 }
		         }
		         
		         if (i== mAdas.fcw.forwardVehicle) {
		        	 int TTC = mAdas.fcw.forwardVehicleTTC;     
			         if ( (mAdas.fcw.resultFlags & FCW_COLLISION_CAUTION) == FCW_COLLISION_CAUTION ) {
			        	 image = getBitmapFromIndexRed((TTC/10)%10);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.red_point);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width+32,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = getBitmapFromIndexRed(TTC%10);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width+64,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.red_s);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width+96,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			         }
			         else if ( TTC != 0 ) {
			        	 image = getBitmapFromIndex((TTC/10)%10);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.green_point);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width+32,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = getBitmapFromIndex(TTC%10);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width + 64,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			        	 image = BitmapFactory.decodeResource(getContext().getResources(), R.raw.green_s);
			        	 scaledImage = Bitmap.createScaledBitmap(image,32,32, true);
			        	 canvas.drawBitmap(scaledImage,mAdas.fcw.x[i]*disp_width/mFCWActivity.preview_width+96,mAdas.fcw.y[i]*disp_height/mFCWActivity.preview_height-32, null);
			         }
		         }
		         
        	 }        	 
         }
         
         super.onDraw(canvas); 
         
    }
    
    public void update ( ) {
    	invalidate();
    }
} 

class BackPressCloseHandler {
	private long backKeyPressedTime = 0;
	private Toast toast;
	private Activity activity;

	public BackPressCloseHandler(Activity context) {
		this.activity = context;
	}

	public void onBackPressed() {

		if (System.currentTimeMillis() > (backKeyPressedTime + 2000)) {
			backKeyPressedTime = System.currentTimeMillis();
			showGuide();
			return;
		}
		if (System.currentTimeMillis() <= (backKeyPressedTime + 2000)) {
			activity.finish();
			toast.cancel();
		}
	}

	private void showGuide() {
		toast = Toast.makeText(activity, "press back key one more time to finish",
				Toast.LENGTH_SHORT);
		toast.show();
	}
}

