package org.jeecg.common.util.rsa;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Random;

public class MD5Util {

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	/**
	 * 用私鑰解密成為銘文
	 */
	public static String getPwd(String Md5Pwd) throws Exception {
		// 将Base64编码后的私钥转换成PrivateKey对象
		PrivateKey privateKey = RSAUtil.string2PrivateKey(KeyManager.getPrivate_key());
		// 加密后的内容Base64解码
		byte[] base642Byte = RSAUtil.base642Byte(Md5Pwd);
		// 用私钥解密
		byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
		// 解密后的明文
		String password = new String(privateDecrypt);
		return password;
	}


	/**
	 * 加盐MD5加密
	 */
	public static String getSaltMD5(String password) {
		// 生成一个16位的随机数
		Random random = new Random();
		StringBuilder sBuilder = new StringBuilder(16);
		sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
		int len = sBuilder.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sBuilder.append("0");
			}
		}
		// 生成最终的加密盐
		String Salt = sBuilder.toString();
		password = md5Hex(password + Salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = Salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}

	/**
	 * 使用Apache的Hex类实现Hex(16进制字符串和)和字节数组的互转
	 */
	@SuppressWarnings("unused")
	private static String md5Hex(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new String(new Hex().encode(digest));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return "";
		}
	}

	/**
	 * 验证加盐后是否和原文一致
	 */
	public static boolean getSaltverifyMD5(String password, String md5str) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5str.charAt(i);
			cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
			cs2[i / 3] = md5str.charAt(i + 1);
		}
		String Salt = new String(cs2);
		return md5Hex(password + Salt).equals(String.valueOf(cs1));
	}


	public static void main(String[] args) throws Exception {

//
//        String inputPwd = "abc123";
//
//        String salt = MD5Util.getSaltMD5("abc123");
//
////        String spwd = "274c14d0a143a7527fb06b2e86bc9b04ab2f54fd0b92465c";
//
		try {
			String pwd = "cW+x70e6Ynte6lIl1WJMbVJw/4lKUg0Lg0KS4C6lPRrNrieEy3rtVurRVMBRah3VYBWXVu92iCRaRmldrZam/A0sE3fIJRsrf1alVrnIEeaIk/1IXn+Am6rVbJWtLqnjbTHfBpBQpiW+DHbR3vPCXfGbCA8q8wwLjAPqA+IMnlw=";
			String hh = getPwd(pwd);
			System.out.println(hh);
//            boolean flag = MD5Util.getSaltverifyMD5(inputPwd,salt);
//            System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
