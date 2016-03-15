package nt96650_serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.ilincar.ntmode.NT96655CallBack;
import com.ilincar.ntmode.NT96655Tools;

public class SerialManager {
	private static final String TAG = "SerialManager";

	private static SerialManager instance;

	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private boolean isRunning = false;
	
	private ArrayList<byte[]> buflist;
/*	private int allsize = 0;
	byte []outAllBufs = new byte[1024];*/
	private NT96655CallBack nt96655CallBack = null;

	public void setNt96655CallBack(NT96655CallBack callBack){
		nt96655CallBack = callBack;
	}

	public SerialManager() {
		// getSerialPort();
		buflist = new ArrayList<byte[]>();
		start();
	}

	public static SerialManager getInstance() {
		if (instance == null) {
			synchronized (SerialManager.class) {
				if (instance == null)
					instance = new SerialManager();
			}
		}
		return instance;
	}

	private SerialPort getSerialPort() throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			this.mSerialPort = new SerialPort(new File("/dev/ttyMT3"), 115200,
					0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		}
		return mSerialPort;
	}

	public void start() {
		if (!isRunning) {
			isRunning = true;
			try {
				getSerialPort();
			} catch (InvalidParameterException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SerialReadStart();
		}
	}

	public void stop() {
		isRunning = false;

		if (mReadThread != null)
			mReadThread.interrupt();

		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
		instance = null;
	}

	private void SerialReadStart() {
		this.mReadThread = new ReadThread();
		mReadThread.start();
	}

	public void sendBytes(byte[] buffer) {
		if (mOutputStream == null)
			return;
		try {
			synchronized (this) {
				mOutputStream.write(buffer);
				Log.d("zoufeng","sendBytes buffer = " + NT96655Tools.getHexString(buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();

			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[600];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					
					if( size > 0 ){
						byte[] buf = new byte[size];
						for (int i = 0; i < size; i++)
							buf[i] = buffer[i];
						
						buflist.add(buf);
					}
					byte[] output = NT96655Tools.getBytesByByteArray(buflist);
					if(nt96655CallBack != null){
						if(output.length > 2 &&  NT96655Tools.getStringByByte(output[output.length - 2]).equals("0d") && 
								NT96655Tools.getStringByByte(output[output.length - 1]).equals("0a")){
							nt96655CallBack.onGetN96655Data(NT96655Tools.getHexArrayForCom(output));
							buflist.clear();
						}
					}
				//	if (size > 0) {
				//		byte[] buf = new byte[size];
				//		for (int i = 0; i < size; i++)
				//			buf[i] = buffer[i];
			//		}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
}
