package com.android.test.api;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import nt96650_serialport.SerialManager;
import android.util.Log;

public class Nt96650Engine {
	private static final String TAG = "ObdEngine";

	private static Nt96650Engine instance = null;
	private SerialManager serialManager = null;

	private Object mReceivedPacketSemaphore = new Object();
	private Semaphore mSemaphore = new Semaphore(0);

	private byte[] receiveByteBuffer;// = new ArrayList<Byte>();
	// private ArrayList<Byte> receiveByteBuffer = new ArrayList<Byte>();
	private boolean isResp = false;
	private boolean mHaveData = false;
	private byte currentCmd;

	private int getFileStep;
	private int CMD_RETRY_CNT = 1;

	private enum LinkState {
		NULL, LINKING, LINKED
	};

	private LinkState linkState = LinkState.NULL;
	private boolean mLinked = false;

	// 应用层通过这个监听接口接收主动发送的数据包
	public interface IncomingPacketListener {
		public abstract void IncomingPacketReceive(byte[] packet);
	}

	public Nt96650Engine() {
	}
	
	public static Nt96650Engine getInstance(){
		if(instance == null){
			instance = new Nt96650Engine();			
		}
		return instance;
	}

	public void start() {
		Log.d(TAG, "--start()--");
		if (serialManager == null) {
			serialManager = SerialManager.getInstance();
		}
		serialManager.registerReceiveListener(receiveListener);
		obdProbe();
	}

	public void stop() {
		if (serialManager != null) {
			serialManager.stop();
			serialManager = null;
		}
	}

	public synchronized int keyCameraLock() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyCameraLock();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyCameraLockOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyMic() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyMic();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyMicOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyCamera() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyCamera();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyCameraOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyPIP() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyPIP();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyPIPOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyBackAndroidUI() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyBackAndroidUI();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyBackAndroidUIOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keySetTwoLevelMenu() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keySetTwoLevelMenu();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeySetTwoLevelMenuOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyReviewTwoLevelMenu() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyReviewTwoLevelMenu();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyReviewTwoLevelMenuOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyPhoto() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyPhoto();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyPhotoOk(rxbuf);
		else
			return -1;
	}

	public synchronized byte[] getPhotoFile(int index) {
		byte[] txbuf;
		if (!isConnected()) {
			return null;
		}
		txbuf = Nt96650Protocal.getPhotoFile(index);
		sendCmd(txbuf);
		return getRespData();
	}

	public synchronized int keyUp() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyUp();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyUpOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyDown() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyDown();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyDownOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyOk() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyOk();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyOkOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyBack() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyBack();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isKeyBackOk(rxbuf);
		else
			return -1;
	}

	public synchronized int syncDate(int year, int month, int day, int hour,
			int minute, int second) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal
				.syncDate(year, month, day, hour, minute, second);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isSyncDateOk(rxbuf);
		else
			return -1;
	}

	public synchronized int syncCarInfo(byte[] buffer) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.syncCarInfo(buffer);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isSyncCarInfoOk(rxbuf);
		else
			return -1;
	}

	public synchronized int link() {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.link();
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.isLinkOk(rxbuf);
		else
			return -1;
	}
	
	public synchronized int keyBright(int bright) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyBright(bright);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.iskeyBrightOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyPower(boolean on) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyPower(on);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.iskeyPowerOk(rxbuf);
		else
			return -1;
	}
	
	public synchronized int keyTvout(boolean on) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyTvout(on);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.iskeyTvoutOk(rxbuf);
		else
			return -1;
	}

	public synchronized int keyUsb(int mode) {
		byte[] txbuf;
		byte[] rxbuf;
		if (!isConnected()) {
			return -1;
		}
		txbuf = Nt96650Protocal.keyUsb(mode);
		sendCmd(txbuf);
		rxbuf = getRespData();
		if (rxbuf != null)
			return Nt96650Protocal.iskeyUsbOk(rxbuf);
		else
			return -1;
	}
	
	private boolean isConnecting() {
		if (linkState == LinkState.LINKING)
			return true;
		else
			return false;
	}

	private boolean isConnected() {
		return mLinked;
	}

	private void obdProbe() {
		if (isConnecting() || isConnected())
			return;
		linkState = LinkState.LINKING;

		new Thread(new Runnable() {
			public void run() {
				int i;
				// ArrayList<Byte> buffer;
				// receiveByteBuffer.clear();

/*				for (int j = 0; j < 2; j++) {
					for (i = 0; i < 5; i++) {
						byte[] txbuf = Nt96650Protocal.link();
						sendCmd(txbuf);
						// buffer = getRespData();
						if (isHaveData()) {
							mLinked = true;
							linkState = LinkState.LINKED;
							return;
						}
					}
				}*/
				mLinked = true;
				linkState = LinkState.LINKED;
//linkState = LinkState.NULL;
			}
		}).start();
	}

	private boolean isCmdAck(byte resp) {
		Log.d(TAG, "resp :" + resp + ", currentCmd:" + currentCmd);
		if (resp == currentCmd)
			return true;
		else {
			return false;
		}
	}

	private boolean isHaveData() {
		return mHaveData;
	}

	private void waitForData() {
		try {
			mSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private byte[] getRespData() {
		waitForData();

		// ArrayList<Byte> buffer = new ArrayList<Byte>();
		// if(mHaveData){
		// for(int i=0; i<receiveByteBuffer.size(); i++)
		// buffer.add(receiveByteBuffer.get(i));
		// }else
		// return null;

		if (mHaveData) {
			byte[] buffer = new byte[receiveByteBuffer.length];
			for (int i = 0; i < receiveByteBuffer.length; i++)
				buffer[i] = receiveByteBuffer[i];
			return buffer;
		} else
			return null;

	}

	private void setRespData(byte[] packet) {
		// receiveByteBuffer.clear();
		// for (int i = 0; i < packet.length; i++)
		// receiveByteBuffer.add(packet[i]);

		receiveByteBuffer = new byte[packet.length];
		for (int i = 0; i < packet.length; i++)
			receiveByteBuffer[i] = packet[i];
	}

	// 串口收到数据，去协议层解析, 解析到完整的包上传到应用接口
	private OnReceiveListener receiveListener = new OnReceiveListener() {
		public void onBytesReceive(byte[] buffer) {

			synchronized (mReceivedPacketSemaphore) {
				byte[] packet = null;
				Log.d(TAG,
						"recv data=====" + Nt96650Util.getHexString(buffer));

				packet = Nt96650Protocal.getRxPacket(buffer);
				if (packet != null) {
					if (isCmdAck(packet[0])) {// 一般命令
						Log.d(TAG, "resp :" + "==================ok");
						isResp = true;
						setRespData(packet);
					} else {

					}

					if (isResp) {
						mReceivedPacketSemaphore.notify();
					}
				}
			}
		}
	};

	private void sendCmd(final byte[] txbuf) {
		new Thread(new Runnable() {
			public void run() {
				int mLost = 0;
				int retry = CMD_RETRY_CNT;

				if (linkState == LinkState.LINKING)
					retry = 1;
				Log.d(TAG, " sendCmd start");
				Log.d(TAG, " txbuf====="+ Nt96650Util.getHexString(txbuf));
				
				while (mLost < retry) {
					synchronized (mReceivedPacketSemaphore) {
						Log.d(TAG, "serialManager="+serialManager);
						isResp = false;
						mHaveData = false;
						if (txbuf.length > 3)
							currentCmd = txbuf[2];

						if (serialManager != null) {
							serialManager.sendBytes(txbuf);
						}
						try {
							if (getFileStep > 0) {
								mReceivedPacketSemaphore.wait(10000);
							} else
								mReceivedPacketSemaphore.wait(300);
							if (isResp) {
								mHaveData = true;
								Log.d(TAG, "data Incoming");
								break;
							} else {
								mLost++;
								Log.d(TAG, "data Lost");
							}
						} catch (InterruptedException e) {
						}
					}
				}
				Log.d(TAG, " sendCmd end");
				mSemaphore.release();
			}
		}).start();
	}
}
