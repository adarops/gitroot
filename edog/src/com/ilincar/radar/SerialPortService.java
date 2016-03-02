package com.ilincar.radar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortService extends Service {
	CommandReceiver cmdReceiver;

	// protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	public boolean threadFlag = true;
	private String func;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		cmdReceiver = new CommandReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(RadarUtil.SERIALPORT_SERVICE_RECEIVE);

		registerReceiver(cmdReceiver, filter);
		func = intent.getStringExtra("uartfunction");

		int dev = intent.getIntExtra("uartdevice", -1);
		String s = "/dev/ttyMT" + String.valueOf(dev);
		int br = intent.getIntExtra("baudrate", -1);
		doJob(s, br);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mReadThread != null)
			mReadThread.interrupt();
		// mApplication.closeSerialPort();
		if(mSerialPort!=null){
			mSerialPort.close();
			mSerialPort = null;
		}

		this.unregisterReceiver(cmdReceiver);
		threadFlag = false;
		boolean retry = true;
		while (retry) {
			try {
				// mReadThread.join(); 加后退出后重启动不行
				retry = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		GpioDisable();
	}

	private void GpioEnable() {
		RadarUtil.gpio_crtl(3, true);		
	}

	private void GpioDisable() {
		RadarUtil.gpio_crtl(3, false);
	}
	
	public void doJob(String path, int br) {
		if (mSerialPort != null)
			return;
		// mApplication = (Application) getApplication(); //
		try {
			// mSerialPort = mApplication.getSerialPort();
			this.mSerialPort = new SerialPort(new File(path), br, 0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			this.mReadThread = new ReadThread();
			mReadThread.start();

		} catch (SecurityException e) {
			// DisplayError(R.string.error_security);
			Log.e("<zyx>", "SecurityException");
		} catch (IOException e) {
			// DisplayError(R.string.error_unknown);
			Log.e("<zyx>", "IOException");
		} catch (InvalidParameterException e) {
			// DisplayError(R.string.error_configuration);
			Log.e("<zyx>", "InvalidParameterException");
		}
		
		GpioEnable();
	}

	public void serialportSendBytes(byte[] buffer)
	{
		if(mOutputStream==null)
			return;
		try {
			mOutputStream.write(buffer);
			// String str=new String(buffer,0,500);
			// mOutputStream.write(str.getBytes());
			// mOutputStream.write("\r\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopService() {
		threadFlag = false;
		stopSelf();
	}

	public void BroadcastSerialPortRxData(byte[] buffer) {
		Intent intent = new Intent();
		if (func.equals("radar"))
			intent.putExtra("cmd", RadarUtil.CMD_RECEIVE_RADAR_DATA);

		intent.putExtra("databuf", buffer);
		intent.setAction(RadarUtil.SERIALPORT_ACTIVITY_RECEIVE);
		sendBroadcast(intent);
	}

	private class CommandReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(RadarUtil.SERIALPORT_SERVICE_RECEIVE)) {
				int cmd = intent.getIntExtra("cmd", -1);
				if (cmd == RadarUtil.CMD_STOP_SERVICE) {
					stopService();
				}

				if (cmd== RadarUtil.CMD_SEND_RADAR_DATA) {
					// byte command = intent.getByteExtra("command", (byte) 0);
					// int value = intent.getIntExtra("value", 0);
					// String str = intent.getStringExtra("str");
					// sendCmd(command,value);
					byte[] buffer = intent.getByteArrayExtra("databuf");
					// serialportSendString(command,str);
					serialportSendBytes(buffer);
				}

				if (cmd == RadarUtil.CMD_FINISH) {
					stopService();
				}
			}
		}
	}

	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				while (!isInterrupted()) {
					int size;
					try {
						byte[] buffer = new byte[500];
						if (mInputStream == null)
							return;
						
						Log.i("zyx", "before read");
						size = mInputStream.read(buffer);		// wait forever
						Log.i("zyx", "recv size: " + String.valueOf(size));
						Log.i("zyx", "recv: " + String.valueOf(buffer[0]) + String.valueOf(buffer[1]));
						if (size > 0) {
							// onDataReceived(buffer, size); 	//
							// String s=new String(buffer); 	//这个后面添加0
							// String s=new String(buffer,0,size);
							byte[] buf = new byte[size];
							for (int i = 0; i < size; i++)
								buf[i] = buffer[i];
							BroadcastSerialPortRxData(buf);
							Thread.sleep(1);
						}
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			} catch (InterruptedException e) {
				// thread was interrupted during sleep or wait
				e.printStackTrace();
			}
		}
	}
}
