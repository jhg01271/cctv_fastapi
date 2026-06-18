package kr.co.igns.framework.utils.type;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	private static final Logger log = LogManager.getLogger("com.project");
	
	public static String getRuleFileKeyName(URI uri) {
		String keyName = "";
		String temp = uri.toString().substring(uri.toString().lastIndexOf("/"));
		String replace1 = uri.toString().replaceFirst(temp, temp.substring(1));
		keyName = replace1.substring(replace1.lastIndexOf("/") + 1);
		return keyName;
	}

	public static String getGUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getRandomStr(int nSize) {
		try {
			StringBuilder temp = new StringBuilder();
			Random rnd = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < nSize; ++i) {
				int rIndex = rnd.nextInt(3);
				switch (rIndex) {
					case 0 :
						temp.append((char) (rnd.nextInt(26) + 97));
						break;
					case 1 :
						temp.append((char) (rnd.nextInt(26) + 65));
						break;
					case 2 :
						temp.append(rnd.nextInt(10));
						break;
				}
			}
			return temp.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public static String ifNullToEmpty(String obj) {
		String result = "";
		return obj != null ? obj : result;
	}

	public static String objectIfNullToEmpty(Object obj) {
		String result = "";
		if (obj != null) {
			result = String.valueOf(obj);
			if ("null".equals(result)) {
				result = "";
			}

			return result;
		} else {
			return result;
		}
	}

	public static boolean isEmptyChk(Object s) {
		if (s == null) {
			return true;
		} else if (s instanceof String && ((String) s).trim().length() == 0) {
			return true;
		} else if (s instanceof Map) {
			return ((Map<?, ?>) s).isEmpty();
		} else if (s instanceof List) {
			return ((List<?>) s).isEmpty();
		} else if (s instanceof Object[]) {
			return ((Object[]) s).length == 0;
		} else {
			return false;
		}
	}

	public static String nullToStr(String s) {
		return isEmptyChk(s) ? "" : s.trim();
	}

	public static String nullToStr(String org, String converted) {
		return isEmptyChk(org) ? converted : org.trim();
	}

	public static String nullToStr(Object org, String converted) {
		return isEmptyChk(org) ? converted : org.toString();
	}

	public static int nullToStr(String org, int converted) {
		return isEmptyChk(org) ? converted : Integer.parseInt(org);
	}

	public static int nullToStr(Object org, int converted) {
		return isEmptyChk(org) ? converted : Integer.parseInt(org.toString());
	}
}
