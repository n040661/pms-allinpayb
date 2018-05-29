package com.dominator.utils.encode;


import com.dominator.utils.system.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * 3DES加密工具类
 * 
 * @author  
 */
public class Des3Utils {
	//private static final Logger logger = Logger.getLogger(Des3Utils.class);
	// 密钥
	private final static String secretKey = "secretKeyforjydssecretKeyforjyds";
	// 向量
	private final static String iv = Constants.DES3_PASSWORD;// "1qaz2wsx"
	// 加解密统一使用的编码方式
	private final static String encoding = Constants.DES3_ENCODE;//"utf-8"

	/**
	 * 3DES加密
	 * 
	 * @param plainText 普通文本
	 * @return
	 * @throws Exception 
	 */
	public static String encode(String plainText){
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
			return Base64.encode(encryptData);
		} catch (Exception e) {
			System.out.println("3DES加密错误:"+e.getMessage());
			return null;
		}
	}


	/**
	 * 3DES解密
	 * 
	 * @param encryptText 加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText){
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
			byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
			return new String(decryptData, encoding);
		} catch (Exception e) {
			System.out.println("3DES解密:"+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 加密响应
	 *
	 * @param data
	 * @return
	 */
	public static String encResponse(Object data) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String json = gson.toJson(data);
		// 先不加密
		String encStr = json;
		return encStr;
	}


	public static void main(String[] args) {
		String encode = Des3Utils.encode("z123456");
		System.out.println(encode);
		String decode=Des3Utils.decode("TBshQE1jUDc");
		System.out.println(decode);
	}
	

}
