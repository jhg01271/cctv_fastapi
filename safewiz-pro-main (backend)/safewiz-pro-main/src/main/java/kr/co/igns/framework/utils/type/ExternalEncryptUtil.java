package kr.co.igns.framework.utils.type;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

public class ExternalEncryptUtil {
	static final byte[] SALTS = new byte[]{25, 89, 23, 65, 25, 89, 23, 65};
	static final byte[] ivBytes = new byte[]{25, 89, 23, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	static final int ITERATION = 1000;
	
	@Value("${external-encrypt.key}")
	private String KEY;
	
	@Value("${external-encrypt.secret-key-factory}")
	private String SECRET_KEY;
	
	@Value("${external-encrypt.cipher}")
	private String CIPHER;

	public String multiEncode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY);
		PBEKeySpec spec = new PBEKeySpec((new String(Base64.getDecoder().decode(KEY))).toCharArray(), SALTS, 1000, 256);
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = null;
		cipher = Cipher.getInstance(CIPHER);
		cipher.init(1, secret, ivSpec);
		return Base64.getEncoder().encodeToString(cipher.doFinal(textBytes));
	}

	public String multiDecode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY);
		PBEKeySpec spec = new PBEKeySpec((new String(Base64.getDecoder().decode(KEY))).toCharArray(), SALTS, 1000, 256);
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		byte[] textBytes = Base64.getDecoder().decode(str);
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(2, secret, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}
}