package com.driving.touch.api;

import java.math.BigInteger;
import java.util.ArrayList;

//import com.mediatek.engineermode.io.EmGpio;

import android.content.Context;
import android.widget.Toast;

public class Nt96650Util {

	public static void showToast(Context context, CharSequence message) {
		
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
    public static final int byte2Int( byte b) {
        return (b & 0xFF);
    }
    
    public static byte[] int2Byte(int intValue){  
        byte[] b=new byte[4];  
        for(int i=0;i<4;i++){  
            b[i]=(byte)(intValue>>8*(3-i) & 0xFF);  
            System.out.print(Integer.toBinaryString(b[i])+" ");  
            System.out.println("test");  
            System.out.print((b[i]& 0xFF)+" ");  
        }  
        return b;  
     }  
    
	private static long bytes2long(byte[] b, int offset) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			temp = b[i + offset] & 0xff;
			res |= temp;
		}
		return res;
	}

	private static byte[] long2bytes(long num) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - (i * 8)));
		}
		return b;
	}
	

	public static String getHexString(byte[] b) {

		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public static String getHexArrayForCom(byte[] b) {

		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			result+=" ";
		}
		return result;
	}
	
	public static byte[] getByteArray(String hexString) {

		return new BigInteger(hexString, 16).toByteArray();
	}
	
	public static String ByteAarryList2String(ArrayList<Byte> data) {

		byte[] test = new byte[data.size()];
		for (int i = 0; i < data.size()-2; i++)
			test[i] = data.get(i+2);

		String str = new String(test);
		return str;
	}
	
	public static int stringToInt(String str) {
		return Integer.valueOf(str);
	}

	public static String intToString(int value) {
		Integer integer = new Integer(value);
		return integer.toString();
	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
	
	public static int GetCRC16 (byte[] bufData, int buflen)  
    {  
        int ret = 0;  
        int CRC = 0x0000ffff;  
        int POLYNOMIAL = 0x0000a001;  
        int i, j;  

        if (buflen == 0)  
        {  
            return ret;  
        }  
        for (i = 0; i < buflen; i++)  
        {  
            CRC ^= ((int)bufData[i] & 0x000000ff);
            for (j = 0; j < 8; j++)  
            {  
                if ((CRC & 0x00000001) != 0)
                {  
                    CRC >>= 1;  
                    CRC ^= POLYNOMIAL;  
                }  
                else  
                {  
                    CRC >>= 1;
                }  
            }  
            //System.out.println(Integer.toHexString(CRC));
        }  
          
        //System.out.println(Integer.toHexString(CRC));

        return CRC;  
    }  
		
	private static int UpdateCRC16(int crcIn, int data) {
		int crc = crcIn;
		int in = data | 0x100;
		do {
			crc <<= 1;
			in <<= 1;
			if ((in & 0x100) != 0)
				++crc;
			if ((crc & 0x10000) != 0)
				crc ^= 0x1021;
		} while ((in & 0x10000) == 0);
		return (crc & 0xffff);
	}

	public static int GetUpdateCRC16(byte[] data, int size) {
		int i = 0;
		int crc = 0;
		while (i < size) {
			crc = UpdateCRC16(crc, (data[i++] & 0xff));
		}
		crc = UpdateCRC16(crc, 0);
		crc = UpdateCRC16(crc, 0);
		return (crc & 0xffff);
	}

	public static int CalChecksum(byte[] data)
	{
	    int sum = 0;
	    for(int i=0; i<data.length; i++)
	        sum += (data[i++]) & 0xff;
	    	
	    return sum&0xff;
	}

//	public static void gpio_ctrl(int gpio_number, boolean on) {
//		EmGpio.gpioInit();
//		if (on) {
//			EmGpio.setGpioOutput(gpio_number);
//			EmGpio.setGpioDataHigh(gpio_number);
//		} else {
//			EmGpio.setGpioOutput(gpio_number);
//			EmGpio.setGpioDataLow(gpio_number);
//		}
//	}
	
}
