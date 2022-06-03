package kr.co.aihome.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

@Component
public class AriaEncodeUtil {

	/*
	 * Aria 암호화
	 */
	public static String ariaEncrypt(String str) throws InvalidKeyException, UnsupportedEncodingException {
		
		if (str == null || str.equals(""))
			return "";
		String privateKey = "pfiGR7M6HLBFuVy";
		byte[] p;
		byte[] c;
		AriaEngine instance = new AriaEngine(256, privateKey);
		p = new byte[str.getBytes().length];
		p = str.getBytes();
		int len = str.getBytes().length;
		if ((len % 16) != 0) {
			len = (len / 16 + 1) * 16;
		}
		c = new byte[len];
		System.arraycopy(p, 0, c, 0, p.length);
		instance.encrypt(p, c, p.length);
		return byteArrayToHex(c).toUpperCase();
	}

	/*
	 * Aria 복호화
	 */
	public static String ariaDecrypt(String strHex) throws InvalidKeyException, UnsupportedEncodingException {
		String originalData = strHex;
		if (strHex == null || strHex.equals(""))
			return "";
		StringBuffer buf = null;
		try {
			String privateKey = "pfiGR7M6HLBFuVy";
			byte[] p;
			byte[] c;
			AriaEngine instance = new AriaEngine(256, privateKey);
			c = hexToByteArray(strHex);
			p = new byte[c.length];
			instance.decrypt(c, p, p.length);
			buf = new StringBuffer();
			buf.append(new String(p));
			return buf.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "NOUSER";
		}
	}
	
	// hex to byte[] 
		public static byte[] hexToByteArray(String hex) { 
			if (hex == null || hex.length() == 0) { 
				return null; 
			} 

			byte[] ba = new byte[hex.length() / 2]; 
			for (int i = 0; i < ba.length; i++) { 
				ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16); 
			} 
			return ba; 
		} 

		// byte[] to hex 
		public static String byteArrayToHex(byte[] ba) { 
			if (ba == null || ba.length == 0) { 
				return null; 
			} 

			StringBuffer sb = new StringBuffer(ba.length * 2); 
			String hexNumber; 
			for (int x = 0; x < ba.length; x++) { 
				hexNumber = "0" + Integer.toHexString(0xff & ba[x]); 

				sb.append(hexNumber.substring(hexNumber.length() - 2)); 
			} 
			return sb.toString(); 

		}
}
