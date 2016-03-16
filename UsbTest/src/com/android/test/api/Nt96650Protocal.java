package com.android.test.api;

import android.util.Log;

public class Nt96650Protocal {

	public final static int CMD_HEAD = 0x33CC;
	public final static int CMD_TAIL = 0x0D0A;
	public final static int RESP_HEAD = 0x6576;

	public final static int CMD_NULL = 0;
	public final static int CMD_KEY_CAMERA_LOCK = 0x01;
	public final static int CMD_KEY_MIC = 0x02;
	public final static int CMD_KEY_CAMERA = 0x03;
	public final static int CMD_KEY_PIP = 0x04;
	public final static int CMD_KEY_BACK_ANDROID = 0x05;
	public final static int CMD_KEY_SET_TWO_LEVEL_MENU = 0x06;
	public final static int CMD_KEY_REVIEW_TWO_LEVEL_MENU = 0x07;

	public final static int CMD_KEY_PHOTO = 0x08;
	public final static int CMD_KEY_GET_FILE = 0x09;
	public final static int CMD_KEY_UP = 0x10;
	public final static int CMD_KEY_DOWN = 0x11;
	public final static int CMD_KEY_OK = 0x12;
	public final static int CMD_KEY_BACK = 0x13;
	public final static int CMD_KEY_SYNC_DATE = 0x14;
	public final static int CMD_KEY_SYNC_CARINFO = 0x15;
	public final static int CMD_KEY_LINK = 0x16;
	public final static int CMD_KEY_BRIGHT = 0x17;
	public final static int CMD_KEY_POWER = 0x18;	
	public final static int CMD_KEY_TVOUT = 0x19;
	public final static int CMD_KEY_SETTING = 0x21;

	private final static int MAX_RX_LEN = 600;

	private static int rxstate = 0;
	private static int rxlen, rxlensave;
	private static int rxcrc;
	private static int rxindex = 0;
	private static int curCmd = CMD_NULL;
	static boolean rxreceived = false;
	static boolean lentwobyte = false;

	private static byte[] rawByteData = new byte[MAX_RX_LEN];

	private static byte[] sendCmdCommon(int cmd) {
		byte[] txbuf = new byte[9];
		int crc;
		curCmd = cmd;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x00;
		crc = Nt96650Util.GetCRC16(txbuf, 5);
		txbuf[5] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[6] = (byte) (crc >> 8);
		txbuf[7] = (byte) (CMD_TAIL >> 8);
		txbuf[8] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	// 01
	public static byte[] keyCameraLock() {
		return sendCmdCommon(CMD_KEY_CAMERA_LOCK);
	}

	public static int isKeyCameraLockOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_CAMERA_LOCK)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 02
	public static byte[] keyMic() {
		return sendCmdCommon(CMD_KEY_MIC);
	}

	public static int isKeyMicOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_MIC)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 03
	public static byte[] keyCamera() {
		return sendCmdCommon(CMD_KEY_CAMERA);
	}

	public static int isKeyCameraOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_CAMERA)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 04
	public static byte[] keyPIP() {
		return sendCmdCommon(CMD_KEY_PIP);
	}

	public static int isKeyPIPOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_PIP)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 05
	public static byte[] keyBackAndroidUI() {
		return sendCmdCommon(CMD_KEY_BACK_ANDROID);
	}

	public static int isKeyBackAndroidUIOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_BACK_ANDROID)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 06
	public static byte[] keySetTwoLevelMenu() {
		return sendCmdCommon(CMD_KEY_SET_TWO_LEVEL_MENU);
	}

	public static int isKeySetTwoLevelMenuOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_SET_TWO_LEVEL_MENU)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x07
	public static byte[] keyReviewTwoLevelMenu() {
		return sendCmdCommon(CMD_KEY_REVIEW_TWO_LEVEL_MENU);
	}

	public static int isKeyReviewTwoLevelMenuOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_REVIEW_TWO_LEVEL_MENU)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x08
	public static byte[] keyPhoto() {
		return sendCmdCommon(CMD_KEY_PHOTO);
	}

	public static int isKeyPhotoOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_PHOTO)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x09
	public static byte[] getPhotoFile(int index) {
		byte[] txbuf = new byte[11];
		int crc;
		curCmd = CMD_KEY_GET_FILE;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x02;
		txbuf[5] = (byte) (index >> 8);
		txbuf[6] = (byte) (index & 0xff);
		crc = Nt96650Util.GetCRC16(txbuf, 7);
		txbuf[7] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[8] = (byte) (crc >> 8);
		txbuf[9] = (byte) (CMD_TAIL >> 8);
		txbuf[10] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	// 0x10
	public static byte[] keyUp() {
		return sendCmdCommon(CMD_KEY_UP);
	}

	public static int isKeyUpOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_UP)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x11
	public static byte[] keyDown() {
		return sendCmdCommon(CMD_KEY_DOWN);
	}

	public static int isKeyDownOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_DOWN)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x12
	public static byte[] keyOk() {
		return sendCmdCommon(CMD_KEY_OK);
	}

	public static int isKeyOkOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_OK)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x13
	public static byte[] keyBack() {
		return sendCmdCommon(CMD_KEY_BACK);
	}

	public static int isKeyBackOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_BACK)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x14
	public static byte[] syncDate(int year, int month, int day, int hour,
			int minute, int second) {
		byte[] txbuf = new byte[16];
		int crc;
		curCmd = CMD_KEY_SYNC_DATE;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00; // len
		txbuf[4] = 0x07;
		txbuf[5] = (byte) ((year & 0xff00) >> 8);
		txbuf[6] = (byte) (year & 0xff);
		txbuf[7] = (byte) (month & 0xff);
		txbuf[8] = (byte) (day & 0xff);
		txbuf[9] = (byte) (hour & 0xff);
		txbuf[10] = (byte) (minute & 0xff);
		txbuf[11] = (byte) (second & 0xff);
		crc = Nt96650Util.GetCRC16(txbuf, 12);
		txbuf[12] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[13] = (byte) (crc >> 8);
		txbuf[14] = (byte) (CMD_TAIL >> 8);
		txbuf[15] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	public static int isSyncDateOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_SYNC_DATE)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x15
	public static byte[] syncCarInfo(byte[] buffer) {
		int i, len, crc;
		len = (buffer.length & 0xff);
		byte[] txbuf = new byte[len + 9];
		curCmd = CMD_KEY_SYNC_CARINFO;
		// if(len>21)
		// len=21;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = (byte) (len);
		for (i = 0; i < len; i++)
			txbuf[5 + i] = buffer[i];
		i = 5 + len;
		crc = Nt96650Util.GetCRC16(txbuf, i);
		txbuf[i++] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[i++] = (byte) (crc >> 8);
		txbuf[i++] = (byte) (CMD_TAIL >> 8);
		txbuf[i++] = (byte) (CMD_TAIL & 0x00ff);

		return txbuf;
	}

	public static int isSyncCarInfoOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_SYNC_CARINFO)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x16
	public static byte[] link() {
		return sendCmdCommon(CMD_KEY_LINK);
	}

	public static int isLinkOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_LINK)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}

	// 0x17
	public static byte[] keyBright(int bright) {
		// return sendCmdCommon(CMD_KEY_BRIGHT);
		byte[] txbuf = new byte[10];
		int crc;
		curCmd = CMD_KEY_BRIGHT;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x02;
		txbuf[5] = (byte) (bright & 0xff);
		crc = Nt96650Util.GetCRC16(txbuf, 6);
		txbuf[6] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[7] = (byte) (crc >> 8);
		txbuf[8] = (byte) (CMD_TAIL >> 8);
		txbuf[9] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	public static int iskeyBrightOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_BRIGHT)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}
	
	// 0x18
	public static byte[] keyPower(boolean on) {
		// return sendCmdCommon(CMD_KEY_BRIGHT);
		byte[] txbuf = new byte[10];
		int crc;
		curCmd = CMD_KEY_POWER;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x02;
		txbuf[5] = (byte) (on?1:0);
		crc = Nt96650Util.GetCRC16(txbuf, 6);
		txbuf[6] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[7] = (byte) (crc >> 8);
		txbuf[8] = (byte) (CMD_TAIL >> 8);
		txbuf[9] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	public static int iskeyPowerOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_POWER)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}	
	
	// 0x19
	public static byte[] keyTvout(boolean on) {
		// return sendCmdCommon(CMD_KEY_BRIGHT);
		byte[] txbuf = new byte[10];
		int crc;
		curCmd = CMD_KEY_TVOUT;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x02;
		txbuf[5] = (byte) (on?1:0);
		crc = Nt96650Util.GetCRC16(txbuf, 6);
		txbuf[6] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[7] = (byte) (crc >> 8);
		txbuf[8] = (byte) (CMD_TAIL >> 8);
		txbuf[9] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	public static int iskeyTvoutOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_TVOUT)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}	
	
	// 0x21
	public static byte[] keyUsb(int mode) {
		// return sendCmdCommon(CMD_KEY_BRIGHT);
		byte[] txbuf = new byte[11];
		int crc;
		curCmd = CMD_KEY_SETTING;
		txbuf[0] = (byte) (CMD_HEAD >> 8);
		txbuf[1] = (byte) (CMD_HEAD & 0x00ff);
		txbuf[2] = (byte) curCmd;
		txbuf[3] = 0x00;
		txbuf[4] = 0x02;
		txbuf[5] = 0x00;
		txbuf[6] = (byte) (mode & 0xff);
		crc = Nt96650Util.GetCRC16(txbuf, 7);
		txbuf[7] = (byte) (crc & 0x00ff); // 低字节在前
		txbuf[8] = (byte) (crc >> 8);
		txbuf[9] = (byte) (CMD_TAIL >> 8);
		txbuf[10] = (byte) (CMD_TAIL & 0x00ff);
		return txbuf;
	}

	public static int iskeyUsbOk(byte[] buffer) {
		if (buffer[0] != CMD_KEY_SETTING)
			return 0;
		if (buffer[3] == 0x00)
			return 0;
		return 1;
	}
	
	// 取到OK的包就返回，否则返回null,
	// 每次都只解析到一个有效的数据包后就退出了，不好，但没有发现问题，暂时这样
	public static byte[] getRxPacket(byte[] buffer) {
		int i;
		byte rxdata = 0;
		for (i = 0; i < buffer.length; i++) {
			rxdata = buffer[i];
			// Log.d("zyx", "rxstate: " + rxstate);
			switch (rxstate) {
			case 0:
				rxindex = 0;
				if (rxdata == (byte) (RESP_HEAD >> 8))
					rxstate = 1;
				break;
			case 1:
				if (rxdata == (byte) (RESP_HEAD & 0xff))
					rxstate = 2;
				else
					rxstate = 0;
				break;
			case 2: // cmd
				rxstate = 3;
				break;
			case 3: // len high byte
				rxlen = rxdata & 0xff;
				rxstate = 4;
				break;
			case 4:
				rxlen = (rxlen << 8) + (rxdata & 0xff);
				rxlensave = rxlen;
				if (rxlen == 0)
					rxstate = 6;
				else
					rxstate = 5;
				Log.d("zyx", "rxlen: " + rxlen);
				break;

			case 5: // data
				if (--rxlen == 0)
					rxstate = 6;
				break;
			case 6: // crc1
				rxcrc = Nt96650Util.GetCRC16(rawByteData, 5 + rxlensave);
				// Log.d("zyx",
				// "rxcrcL: "
				// + Integer.toString((rxcrc & 0xff) + 0x100, 16)
				// .substring(1)
				// + ", rxdata: "
				// + Integer.toString((rxdata & 0xff) + 0x100, 16)
				// .substring(1));
				if ((rxcrc & 0x00ff) == (rxdata & 0xff)) // 低在前
					rxstate = 7;
				else
					rxstate = 0;
				break;

			case 7: // crc2
				// Log.d("zyx",
				// "rxcrcH: "
				// + Integer.toString((rxcrc >> 8) + 0x100, 16)
				// .substring(1)
				// + ", rxdata: "
				// + Integer.toString((rxdata & 0xff) + 0x100, 16)
				// .substring(1));
				if ((rxcrc >> 8) == (rxdata & 0xff))
					rxstate = 8;
				else
					rxstate = 0;
				break;

			case 8:
				if (rxdata == ((Nt96650Protocal.CMD_TAIL >> 8) & 0xff))
					rxstate = 9;
				else
					rxstate = 0;
				break;
			case 9:
				if (rxdata == (Nt96650Protocal.CMD_TAIL & 0x00ff))
					rxreceived = true;
				rxstate = 0;
				break;

			}
			rawByteData[rxindex++] = rxdata;

			// 处理数据
			if (rxreceived) {
				rxreceived = false;
				return Nt96650Util.subBytes(rawByteData, 2, rxlensave + 3);

			} else if (rxindex >= MAX_RX_LEN)
				rxstate = 0;
		}

		return null;
	}

	// "-\n\r\r Programming Completed Sucessfully!\n\r-"+"                     "
	// public static boolean isFirmWareUpdateLastDataAck(ArrayList<Byte> data) {
	//
	// byte[] buf = new byte[data.size()];
	// for (int i = 0; i < buf.length; i++)
	// buf[i] = data.get(i);
	// String s = new String(buf, 0, buf.length);
	// int i = s.indexOf("Successfully");
	//
	// return i > 0 ? true : false;
	// }

}
