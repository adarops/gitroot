package com.ilincar.ntmode;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by zoufeng on 16/3/5.
 */
public class NT96655Tools {

    public static final int byte2Int(byte b){
        return (b & 0xff);
    }

    public static byte[] int2Byte(int value){
        byte[] b = new byte[4];
        for(int i = 0; i < 4 ; i++){
            b[i] = (byte)(value>>8*(3-i) & 0xff);
        }
        return b;
    }

    public static int hexToint(String b){
        return Integer.parseInt(b,16);
    }

    public static int hexsToint(String[] b){
        String str = "";
        for(int i = 0 ; i < b.length; i++){
            str += b[i];
        }
        if(str.equals("")){
           return 0;
        }
        return Integer.parseInt(str,16);
    }

    public static long bytes2long(byte[] b, int offset){
        long temp = 0;
        long res = 0;
        for(int i = 0; i < 8; i++){
            res <<=8 ;
            temp = b[i + offset] & 0xff;
            res |= temp;
        }
        return res;
    }

    public static byte[] long2bytes(long num){
        byte[] b = new byte[8];
        for(int i = 0; i< 8; i++){
            b[i] = (byte) (num >>> (56 - (i * 8)));
        }
        return b;
    }

    public static String getHexString(byte[] b){
        String result = "";
        for(int i = 0; i < b.length; i++){
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String getStringByByte(byte b){
    	String result = "";
    	result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    	return result;
    }
    
    public static String getHexArrayForCom(byte[] b){
        String result = "";
        for(int i = 0; i < b.length; i++){
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            result += " ";
        }
        return result;
    }

    public static byte[] getByteArray(String hexString){
        return new BigInteger(hexString, 16).toByteArray();
    }

    public static String ByteArrayList2String(ArrayList<Byte> data){
        byte[] temp = new byte[data.size()];
        for(int i = 0; i < data.size() - 2; i++) {
            temp[i] = data.get(i + 2);
        }
        String str = new String(temp);
        return str;
    }

    public static int stringToInt(String str){
        return Integer.valueOf(str);
    }

    public static String intToString(int value){
        Integer integer = new Integer(value);
        return integer.toString();
    }

    public static byte[] subBytes(byte[] src, int begin, int count){
        byte[] b = new byte[count];
        for(int i = begin; i < begin + count ; i++)
            b[i-begin] = src[i];
        return b;
    }

    public static byte[] getBytesByByteArray(ArrayList<byte[]> arrays){
    	byte[] b = new byte[1024];
    	int k = 0;
    	for(int i = 0; i < arrays.size(); i++){
    		byte[] by = arrays.get(i);
    		for(int j = 0; j < by.length; j++){
    			b[k] = by[j];
    			k++;
    		}
    	}
    	byte[] out = new byte[k];
    	for(int i = 0; i < k; i++){
    		out[i] = b[i];
    	}
    	
    	return out;
    }
    
    public static int getCRC16(byte[] bufData, int buflen){
        int ret = 0;
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;

        if(buflen == 0)
            return ret;

        for(i = 0; i < buflen; i++){
            CRC ^= ((int)bufData[i] & 0x000000ff);
            for(j = 0; j < 8; j++){
                if((CRC & 0x00000001) != 0){
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                }else{
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }

    public static int updateCRC16(int crcIn, int data){
        int crc = crcIn;
        int in = data | 0x100;
        do{
            crc <<= 1;
            in <<= 1;
            if((in & 0x100) != 0){
                ++crc;
            }
            if((crc & 0x10000) != 0){
                crc ^= 0x1021;
            }
        }while ((in & 0x10000) == 0);
        return (crc & 0xffff);
    }

    public static int getUpdateCRC16(byte[] data, int size){
        int i = 0;
        int crc = 0;
        while(i < size){
            crc = updateCRC16(crc, (data[i++] & 0xff));
        }
        crc = updateCRC16(crc, 0);
        crc = updateCRC16(crc, 0);
        return (crc & 0xffff);
    }

    public static int calCheckSum(byte[] data){
        int sum = 0;
        for(int i = 0; i < data.length; i++){
            sum += (data[i++]) & 0xff;
        }
        return sum & 0xff;
    }
}
