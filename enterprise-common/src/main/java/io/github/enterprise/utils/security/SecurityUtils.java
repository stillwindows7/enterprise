package io.github.enterprise.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sheldon on 2017年12月13日
 *
 */
public class SecurityUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
	
	/**
	 * 生成 uuid 字符串
	 * 
	 * @return 返回 uuid 字符串
	 */
	public static String generateUUID() {
		final String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
	
	/**
	 * 传入文本内容，返回 SHA-256 串
	 * 
	 * @param strText	要加密的字符串
	 * @return	返回加密字符串
	 */
	public static String sha256(final String strText) {
		return sha(strText, "SHA-256");
	}
	
	/**
	 * 传入文本内容，返回 SHA-512
	 * 
	 * @param strText	需要加密的内容
	 * @return	返回加密字符串
	 */
	public static String sha512(final String strText) {
		return sha(strText, "SHA-512");
	}
	
	/**
	 * 字符串 sha 加密
	 * 
	 * @param strText	要加密的字符串
	 * @param strType	加密类型
	 * @return
	 */
	private static String sha(final String strText, final String strType) {
		String strResult = null;	// 返回值
		
		if (StringUtils.isNotBlank(strText)) {
			try {
				// SHA 加密开始
				// 创建加密对象，并传入加密类型
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 类型结果
				byte[] byteBuffer = messageDigest.digest();
				
				// 将 byte 转换为 string
				StringBuffer strHexString = new StringBuffer();
				// 遍历 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回结果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException  e) {
				logger.error("no such algorithm", e);
			}
		}
		
		return strResult;
	}
}
