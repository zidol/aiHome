package kr.co.aihome.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 *
 * @since 2021.12.22
 * 암복호화 클래스
 * 
 * pbszUserKey : 암복호화하는 데 사용하는 비밀키
 * pbszIV : 초기화 벡터키로, 비밀키와 마찬가지로 16비트로 구성해야한다.
 */
@Component
public class Seed {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	
	private static String PBSZUSERKEY;
	@Value("${crypto.pbszUserKey}")
	public void setPbszUserKey(String value) {
		PBSZUSERKEY = value;
    }
	
	private static String PBSZIV;
	@Value("${crypto.pbszIV}")
	public void setPbszIV(String value) {
		PBSZIV = value;
    }
	public static String encrypt(String rawMessage) {
		Encoder encoder = Base64.getEncoder();
		byte[] message = rawMessage.getBytes(UTF_8);
		byte[] encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(PBSZUSERKEY.getBytes(), PBSZIV.getBytes(), message, 0, message.length);
		return new String(encoder.encode(encryptedMessage), UTF_8);
	}

	public static String decrypt(String encryptedMessage) {
		Decoder decoder = Base64.getDecoder();
		byte[] message = decoder.decode(encryptedMessage);
		byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(PBSZUSERKEY.getBytes(), PBSZIV.getBytes(), message, 0, message.length);
		return new String(decryptedMessage, UTF_8);
	}
}
