package kr.co.igns.framework.config.response;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

	private static final Logger log = LogManager.getLogger("project.name");
	
	public static <T> ResponseEntity<SingleResponseDto<T>> SingleResponse(HttpStatus status, boolean success, String code , String message, T data) {
		if(status.value() > 399 && status.value() != 409) {
			log.error("[Message : " + message + "]");
		}
		SingleResponseDto<T> commonResponse = new SingleResponseDto<T>(status.value(), success, code , message, data);
	    return new ResponseEntity<>(commonResponse, HttpStatus.valueOf(status.value()));
	}
	 
//	public static <T> ResponseEntity<ListResponseDto<T>> ListResponse(HttpStatus status, boolean success, String code , String message, List<T> list) {
//		if(status.value() > 399 && status.value() != 409) {
//			log.error("[Message : " + message + "]");
//		}
//		ListResponseDto<T> commonResponse = new ListResponseDto<T>(status.value(), success, code ,message, list);
//	    return new ResponseEntity<>(commonResponse, HttpStatus.valueOf(status.value()));
//	}
}
