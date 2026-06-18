package kr.co.igns.framework.utils.etc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.http.HttpStatus;

import kr.co.igns.framework.config.log.LogUtil;

public class Aes256 {

	public static final String AES_KEY = "iljoogns004134112345678123456789";
	public static final String IV = "iljoogns12345678";
	
	/**
	 * 암호화
	 * 
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String encryptString(String text) {
		if (text == null) {
			text = "";
		}
		try {
			return new String(Base64.encodeBase64(cryptProcess(text.getBytes("UTF-8"), true))).replaceAll("\\n", "");
		} catch (UnsupportedEncodingException e) {
			return new String(Base64.encodeBase64(cryptProcess(text.getBytes(), true))).replaceAll("\\n", "");
		}
	}

	/**
	 * 복호화
	 * 
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String decryptString(String text) {
		if (text != null && text.length() > 0) {
			try {
				return new String(cryptProcess(Base64.decodeBase64(text), false), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return new String(cryptProcess(Base64.decodeBase64(text), false));
			}
		} else {
			return "";
		}
	}

	/**
	 * 암호화 / 복호화 노트
	 * 
	 * @param key
	 * @param iv
	 * @param text
	 * @param mode
	 * @return
	 */
private static byte[] cryptProcess(byte[] text, boolean mode) {
		
		byte[] resultString = null;
		ByteArrayInputStream input = null;
        ByteArrayOutputStream output = null;
		
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
			
			cipher.init(mode, new ParametersWithIV(new KeyParameter(AES_KEY.getBytes()), IV.getBytes()));
			
			input = new ByteArrayInputStream(text);
	        output = new ByteArrayOutputStream();
	        
	        int inputLen;
	        int outputLen;
	        
	        byte[] inputBuffer = new byte[4096];
	        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];
	        
	        while ((inputLen = input.read(inputBuffer)) > -1) {
	            outputLen = cipher.processBytes(inputBuffer, 0, inputLen, outputBuffer, 0);
	            if (outputLen > 0) {
	                output.write(outputBuffer, 0, outputLen);
	            }
	        }
	        
	        outputLen = cipher.doFinal(outputBuffer, 0);
	        
	        if (outputLen > 0) {
	            output.write(outputBuffer, 0, outputLen);
	        }
	        
			resultString = output.toByteArray();
			
			input.close();
			output.close();
			
		} catch (DataLengthException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "DataLengthException", e.getMessage(), null);
			return new byte[0];
		} catch (IllegalStateException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalStateException", e.getMessage(), null);
			return new byte[0];
		} catch (IOException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException", e.getMessage(), null);
			return new byte[0];
		} catch (InvalidCipherTextException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "InvalidCipherTextException", e.getMessage(), null);
			return new byte[0];
		} finally {
		    try {
		        if (input != null) {
		            input.close();
		        }
		        if (output != null) {
		            output.close();
		        }
		    } catch (IOException e) {
		        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException while closing resources", e.getMessage(), null);
		    }
		}
    	return resultString;
	}

	/**
	 * 파일 암호화
	 * 
	 * @param inputPath
	 * @param outputPath
	 */
	public static void encryptFile(String inputPath, String outputPath) {
		InputStream input = null;
		OutputStream output = null;
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
			
			cipher.init(true, new ParametersWithIV(new KeyParameter(AES_KEY.getBytes()), IV.getBytes()));
			
			input = new FileInputStream(inputPath);
	        output = new FileOutputStream(outputPath);
	        
	        int inputLen;
	        int outputLen;
	        
	        byte[] inputBuffer = new byte[4096];
	        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];
	        
	        while ((inputLen = input.read(inputBuffer)) > -1) {
	            outputLen = cipher.processBytes(inputBuffer, 0, inputLen, outputBuffer, 0);
	            if (outputLen > 0) {
	                output.write(outputBuffer, 0, outputLen);
	            }
	        }
	        
	        outputLen = cipher.doFinal(outputBuffer, 0);
	        
	        if (outputLen > 0) {
	            output.write(outputBuffer, 0, outputLen);
	        }
	        
	        input.close();
			output.close();
	    	
		} catch (DataLengthException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "DataLengthException", e.getMessage(), null);
		} catch (IllegalStateException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalStateException", e.getMessage(), null);
		} catch (IOException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException", e.getMessage(), null);
		} catch (InvalidCipherTextException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "InvalidCipherTextException", e.getMessage(), null);
		} finally {
		    try {
		        if (input != null) {
		            input.close();
		        }
		        if (output != null) {
		            output.close();
		        }
		    } catch (IOException e) {
		        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException while closing resources", e.getMessage(), null);
		    }
		}
	}

	public static String sha256(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		return bytesToHex(md.digest());
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();

	}

}