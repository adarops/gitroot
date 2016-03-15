package nt96650_serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import com.driving.touch.api.OnReceiveListener;

import android.util.Log;

public class SerialManager {
	private static final String TAG = "SerialManager";

	private static SerialManager instance;

	private SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private boolean isRunning = false;

	private OnReceiveListener receiveListener = null;

	public SerialManager() {
		// getSerialPort();
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
		Log.v(TAG, "stop");
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
					if (size > 0) {
						byte[] buf = new byte[size];
						for (int i = 0; i < size; i++)
							buf[i] = buffer[i];
						notifyBytesReceive(buf);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	public void registerReceiveListener(OnReceiveListener listener) {
		receiveListener = listener;
	}

	private void notifyBytesReceive(byte[] buffer) {
		if (receiveListener != null) {
			receiveListener.onBytesReceive(buffer);
		}
	}

}
