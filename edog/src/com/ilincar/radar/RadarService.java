package com.ilincar.radar;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

//
public class RadarService extends Service {

	private static final String TAG = "RadarService";

	// sxp
	final int RADAR_SYNC_HEAD1 = 0xAA;
	final int RADAR_SYNC_HEAD2 = 0x55;

	final int RADAR_LASER = 0x01;

	final int RADAR_KBAND_L1 = 0x0A;
	final int RADAR_KBAND_L2 = 0x4A;
	final int RADAR_KBAND_L3 = 0xCA;

	final int RADAR_XBAND_L1 = 0x02;
	final int RADAR_XBAND_L2 = 0x42;
	final int RADAR_XBAND_L3 = 0xC2;

	final int RADAR_KaBAND_L1 = 0x1A;
	final int RADAR_KaBAND_L2 = 0x5A;
	final int RADAR_KaBAND_L3 = 0xDA;

	// RT-200
	// final int RADAR_SYNC_DATA = 0xAA;
	//
	// final int RADAR_LINK_HEAD2 = 0x55;
	// final int RADAR_LASER = 0x80;
	// final int RADAR_KBAND_L1 = 0x50;
	// final int RADAR_KBAND_L2 = 0x51;
	// final int RADAR_KBAND_L3 = 0x52;
	// final int RADAR_KBAND_L4 = 0x53;
	//
	// final int RADAR_XBAND_L1 = 0x40;
	// final int RADAR_XBAND_L2 = 0x41;
	// final int RADAR_XBAND_L3 = 0x42;
	// final int RADAR_XBAND_L4 = 0x43;
	//
	// final int RADAR_KaBAND_L1 = 0x58;
	// final int RADAR_KaBAND_L2 = 0x59;
	// final int RADAR_KaBAND_L3 = 0x5A;
	// final int RADAR_KaBAND_L4 = 0x5B;

	final int RX_WAIT_SYNC_HEAD1 = 0;
	final int RX_WAIT_SYNC_HEAD2 = 1;
	final int RX_WAIT_DATA_FIRST = 2;
	final int RX_WAIT_DATA_NEXT = 3;

	private int rxState;
	private int lastRxData;
	private boolean isRadarLinked = false;
	private long lastdatatime;
	private int linkCount, errorAlermCount;

	private String rawData = null;

	RadarReceiver receiver;

	private RadarSound mRadarSound;

	@Override
	public void onCreate() {
		super.onCreate();

		String br = this.getString(R.string.baudrate);
		Intent intent = new Intent();
		intent.putExtra("uartfunction", "radar");
		// intent.putExtra("uartdevice", 3);
		intent.putExtra("uartdevice", 0);
		intent.putExtra("baudrate", Integer.parseInt(br));
		intent.setClass(this, SerialPortService.class);
		startService(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		RadaDataInit();
		if (receiver == null) {
			receiver = new RadarReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(RadarUtil.SERIALPORT_ACTIVITY_RECEIVE);
			registerReceiver(receiver, filter);
		}
		mRadarSound = RadarSound.getInstance(this);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		isRadarLinked = false;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		if (mRadarSound != null) {
			mRadarSound.close();
			mRadarSound = null;
		}

		if (receiver != null) {
			unregisterReceiver(receiver);
		}

		Intent intent = new Intent();
		intent.setClass(this, SerialPortService.class);
		stopService(intent);

		// stopService(new
		// Intent("android_serialport_api.SerialPortService.class"));
	}

	private void SoundTest() {
		mRadarSound.play(mRadarSound.SOUND_RADAR_START);
		mRadarSound.play(mRadarSound.SOUND_RADAR_SECOND);
		mRadarSound.play(mRadarSound.SOUND_RADAR_ERROR);
		mRadarSound.play(mRadarSound.SOUND_RADAR_RFDIDI);
		mRadarSound.play(mRadarSound.SOUND_RADAR_LASER);

		mRadarSound.play(mRadarSound.SOUND_RADAR_K);
		mRadarSound.play(mRadarSound.SOUND_RADAR_KA);
		mRadarSound.play(mRadarSound.SOUND_RADAR_KU);
		mRadarSound.play(mRadarSound.SOUND_RADAR_X);
	}

	private void RadaDataInit() {
		rxState = RX_WAIT_DATA_FIRST;
		lastRxData = 0;
		// 启动定时器, 8S无连接就报警，没30S报已次
		errorAlermCount = 8; //
		MonitorLink();
	}

	public class RadarReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction()
					.equals(RadarUtil.SERIALPORT_ACTIVITY_RECEIVE)) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == RadarUtil.CMD_SYSTEM_EXIT) {
					System.exit(0);
				} else if (cmd == RadarUtil.CMD_FINISH) {
					// System.exit(0);
				} else if (cmd == RadarUtil.CMD_RECEIVE_RADAR_DATA) {
					byte[] buffer = bundle.getByteArray("databuf");
					onDataReceived(buffer);
				}
			}
		}
	}

	protected void onDataReceived(final byte[] buffer) {
		int rxData;
		for (int i = 0; i < buffer.length; i++) {
			rxData = (buffer[i] & 0xff);
			switch (rxState) {
			// 等待收AA 55连接确认信息
			// case RX_WAIT_SYNC_HEAD1:
			// if(rxData==RADAR_SYNC_HEAD1){
			// rxState=RX_WAIT_SYNC_HEAD2;
			// }
			// break;

			case RX_WAIT_SYNC_HEAD2:
				if (rxData == RADAR_SYNC_HEAD2) {
					rxState = RX_WAIT_DATA_FIRST;
					LinkedConfirm();
				}
				break;

			// 55 XX ���
			case RX_WAIT_DATA_FIRST:
				if (rxData == 0x55)
					rxState = RX_WAIT_DATA_NEXT;
				else if (rxData == RADAR_SYNC_HEAD1)
					rxState = RX_WAIT_SYNC_HEAD2; // 来了同步头
				break;

			case RX_WAIT_DATA_NEXT:
				// Log.v("收到数据", Integer.toHexString(rxData));

				RadaDataProcess(rxData);
				rxState = RX_WAIT_DATA_FIRST;
				break;
			}
		}
	}

	private void LinkedConfirm() {
		if (!isRadarLinked) {
			mRadarSound.play(mRadarSound.SOUND_RADAR_START);

		}
		isRadarLinked = true;
		// 复位定时器
		linkCount = 6;
	}

	// private int GetRadarType(int data) {
	// if (data == RADAR_LASER){
	// return 1;
	// }
	// else if(data == RADAR_KBAND_L1|| data == RADAR_KBAND_L2 || data ==
	// RADAR_KBAND_L3){
	// return 2;
	// }else if(data == RADAR_XBAND_L1 || data == RADAR_XBAND_L2|| data ==
	// RADAR_XBAND_L3){
	// return 3;
	// }else if(data == RADAR_KaBAND_L1 ||data == RADAR_KaBAND_L2 || data ==
	// RADAR_KaBAND_L3)
	// return 4;
	// else
	// return 0;
	// }

	// 在收到confirm前先收到了有效的数据,补一个确认语音
	// 两个相同的数据要间隔足够长时间才重新播报
	private void RadaDataProcess(int data) {
		long time;
		// int radartype;
		//
		// radartype=GetRadarType(data);
		// if (radartype==0)
		// return;

		LinkedConfirm();

		time = System.currentTimeMillis();

		// if (radartype == lastRxData) { //
		if (data == lastRxData) { //
			if (time - lastdatatime < 20000) {
				// Log.v(TAG, "重复数据，不播放");
				return;
			}
		}
		lastdatatime = time;
		lastRxData = data;

		Log.v("播放数据", Integer.toHexString(data));

		switch (data) {

		case RADAR_LASER:
			mRadarSound.play(mRadarSound.SOUND_RADAR_LASER);
			break;

		case RADAR_KBAND_L1:
			// break;
		case RADAR_KBAND_L2:
			// break;
		case RADAR_KBAND_L3:
			mRadarSound.play(mRadarSound.SOUND_RADAR_K);
			break;

		case RADAR_XBAND_L1:
			// break;
		case RADAR_XBAND_L2:
			// break;
		case RADAR_XBAND_L3:
			mRadarSound.play(mRadarSound.SOUND_RADAR_X);
			break;

		case RADAR_KaBAND_L1:
			// break;
		case RADAR_KaBAND_L2:
			// break;
		case RADAR_KaBAND_L3:
			mRadarSound.play(mRadarSound.SOUND_RADAR_KA);
			break;
		}
	}

	// 监控通讯连接
	private void MonitorLink() {
		timer.schedule(NoConfirmMsgAlarmTask, 0, 1000);
	}

	private Timer timer = new Timer(true);

	private TimerTask NoConfirmMsgAlarmTask = new TimerTask() {
		@Override
		public void run() {
			if (isRadarLinked) {
				if (linkCount > 0) {
					if (--linkCount == 0) {
						isRadarLinked = false;
						errorAlermCount = 0;
					}
				}
			} else {
				if (errorAlermCount == 0) {
					errorAlermCount = 30;
					mRadarSound.play(mRadarSound.SOUND_RADAR_ERROR);
				} else
					errorAlermCount--;
			}

		}
	};

}
