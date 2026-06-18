package kr.co.igns.framework.utils.type;

import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncodeUtils {
	private static final Logger log = LogManager.getLogger("com.project");

	public static String encodeDownloadFileName(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "";
	}
}
