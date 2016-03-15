
package com.android.test.api;

import java.util.ArrayList;


public class NT96650API {

	private Nt96650Engine nt96650;

	private UpdateListener listener = null;
	
//	private ArrayList<Byte> receiveByteBuffer = new ArrayList<Byte>();
	
	
	public interface UpdateListener {
		public abstract void Update(ArrayList<Byte> buf);
	}

	public NT96650API() {
		nt96650 = Nt96650Engine.getInstance();
//		nt96650.registerIncomingPacketListener(incomingPacketListener);
	}

	public void start() {
		nt96650.start();
	}

	public void stop() {
		nt96650.stop();
	}

	public synchronized void registerOnUpdateListener(UpdateListener listener) {
		this.listener = listener;
	}

	public void OnUpdateListener(ArrayList<Byte> buf) {
		if (listener != null) {
			listener.Update(buf);
		}
	}

//	private IncomingPacketListener incomingPacketListener = new IncomingPacketListener() {
//		public void IncomingPacketReceive(byte[] packet) {
//			receiveByteBuffer.clear();
//			for (int i = 0; i < packet.length; i++)
//				receiveByteBuffer.add(packet[i]);
//
//			OnUpdateListener(receiveByteBuffer);
//		}
//	};
	
	/* 返回值：  1 执行成功
	 *        0执行失败
	 *       -1 没有应答
	 */
	public int keyCameraLock(){
		return nt96650.keyCameraLock();
	}
	
	public int keyMic(){
		return nt96650.keyMic();
	}
	
	public int keyCamera() {
		return nt96650.keyCamera();
	}
	
	public int keyPIP(){
		return nt96650.keyPIP();
	}
	
	public int keyBackAndroidUI() {
		return nt96650.keyBackAndroidUI();
	}
	
	public int keySetTwoLevelMenu(){
		return nt96650.keySetTwoLevelMenu();
	}
	
	public int keyReviewTwoLevelMenu(){
		return nt96650.keyReviewTwoLevelMenu();
	}
	
	public int keyPhoto() {
		return nt96650.keyPhoto();
	}
	
	public byte[] getPhotoFile(int index) {
		return nt96650.getPhotoFile(index);
	}
	
	public int keyUp() {
		return nt96650.keyUp();
	}
	
	public int keyDown() {
		return nt96650.keyDown();
	}
	
	public int keyOk() {
		return nt96650.keyOk();
	}
	
	public int keyBack() {
		return nt96650.keyBack();
	}
	
	public int syncDate(int year, int month, int day, int hour, int minute, int second) {
		return nt96650.syncDate(year, month, day, hour, minute, second);
	}

	// 7个字符的车牌信息，粤B3M6ZF
	public int syncCarInfo(String str) {
		return nt96650.syncCarInfo(str.getBytes());
	}
	
	public int link() {
		return nt96650.link();
	}
	
	public int keyBright(int bright) {
		return nt96650.keyBright(bright);
	}	
	
	public int keyPower(boolean on) {
		return nt96650.keyPower(on);
	}	
	
	public int keyTvout(boolean on) {
		return nt96650.keyTvout(on);
	}	
	
	public int keyUsb(int mode) {
		return nt96650.keyUsb(mode);
	}
}
