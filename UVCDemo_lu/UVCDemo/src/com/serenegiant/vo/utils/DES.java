package com.serenegiant.vo.utils;



import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

	/**
	 * DES算法,加密 　
	 * 
	 * @param data
	 *            待加密字符串 　
	 * @param key
	 *            加密私钥,长度不能够小于8位 　　
	 * @return 加密后的字节数组,一般结合Base64编码使用 　
	 * @throws CryptException
	 *             异常 　　
	 */
	public static String encode(String key, String data) throws Exception {
		return encode(key, data.getBytes());
	}

	/**
	 * DES算法,加密
	 * 
	 * @param data
	 *            待加密字符串 　
	 * @param key
	 *            加密私钥,长度不能够小于8位 　
	 * @return 加密后的字节数组,一般结合Base64编码使用 　
	 * @throws CryptException
	 *             异常
	 */
	public static String encode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data);
			return Base64.encode(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * @param data
	 *            待解密字符串 　　
	 * @param key
	 *            解密私钥,长度不能够小于8位 　　
	 * @return 解密后的字节数组 　　
	 * @throws Exception
	 *             异常 　　
	 */
	public static byte[] decode(String key, byte[] data) throws Exception {
		try {
			//SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 获取编码后的值 　　
	 * 
	 * @param key
	 * @param data
	 * @return 　　
	 * @throws Exception
	 */
	public static String decodeValue(String key, String data) {
		byte[] datas;
		String value = null;
		try {
			if (System.getProperty("os.name") != null
					&& (System.getProperty("os.name").equalsIgnoreCase("sunos") || System.getProperty("os.name")
							.equalsIgnoreCase("linux"))) {
				datas = decode(key, Base64.decode(data));
			} else {
				datas = decode(key, Base64.decode(data));
			}
			value = new String(datas);
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

}