package com.utlis.lib;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES加密与解密工具类
 * @author ZDF
 * @date 2015年3月17日
 *
 */
public class AES {

	/** 密钥算法 */
	private static final String KEY_ALGORITHM = "AES";

	/** 密算法/工作模式/填充方式 */
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/** 字符集 */
	public static final String CHAR_SET = "UTF-8";


	/**
	 * 加密消息
	 * @author ZDF
	 * @date 2015年3月17日
	 * @param message
	 * 			明文消息
	 * @param key
	 * 			密钥，字符串长度必须为：16、24或32
	 * @return
	 * 			加密消息字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(String message, String key) throws Exception {
		byte[] data = null;
		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(CHAR_SET), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			data = cipher.doFinal(message.getBytes(CHAR_SET));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}

	/**
	 * 加密消息
	 * @author ZDF
	 * @date 2015年3月19日
	 * @param message
	 * 			明文消息
	 * @param key
	 * 			密钥，字符串长度必须为：16、24或32
	 * @return
	 * 			密文（BASE64编码）
	 * @throws Exception
	 */
	public static String encryptToBase64(String message, String key) throws Exception{
		byte[] data = encrypt(message, key);
		return Base64.encodeToString(data,Base64.DEFAULT);
	}

	/**
	 * 解密
	 * @author ZDF
	 * @date 2015年3月17日
	 * @param message
	 * 			待解密消息字节数组
	 * @param key
	 * 			密钥，字符串长度必须为：16、24或32
	 * @return
	 * 			解密符串
	 * @throws Exception
	 */
	public static String decrypt(byte[] message, String key) throws Exception {
		String result = null;
		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(CHAR_SET), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] data = cipher.doFinal(message);
			result = new String(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
