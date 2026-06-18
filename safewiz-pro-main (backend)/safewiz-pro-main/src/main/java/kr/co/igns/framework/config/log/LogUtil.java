package kr.co.igns.framework.config.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
		
	public static void ConsoleLogInfo(HttpStatus status, String title, String message, HttpServletRequest request) {
		log.info("[" + status.value() + "] [" + title + "] [" + message + "]");
		if (request != null) {
			log.info("[Request Method : " + request.getMethod() + "]");
			log.info("[Request URI : " + request.getRequestURI() + "]");
		}
	}
	
	public static void ConsoleLogError(HttpStatus status, String title, String message, HttpServletRequest request) {
		log.error("[" + status.value() + "] [" + title + "] [" + message + "]");
		if (request != null) {
			log.error("[Request Method : " + request.getMethod() + "]");
			log.error("[Request URI : " + request.getRequestURI() + "]");
		}
	}
	
	public static void ConsoleLogError(HttpStatus status, String title, Exception e, HttpServletRequest request) {
		log.error("[" + status.value() + "] [" + title + "] [" + e.getMessage() + "]", e);
		if (request != null) {
			log.error("[Request Method : " + request.getMethod() + "]");
			log.error("[Request URI : " + request.getRequestURI() + "]");
		}
	}
	
	public static void ConsoleLogWarn(HttpStatus status, String title, String message, HttpServletRequest request) {
		log.warn("[" + status.value() + "] [" + title + "] [" + message + "]");
		if (request != null) {
			log.warn("[Request Method : " + request.getMethod() + "]");
			log.warn("[Request URI : " + request.getRequestURI() + "]");
		}
	}
}
