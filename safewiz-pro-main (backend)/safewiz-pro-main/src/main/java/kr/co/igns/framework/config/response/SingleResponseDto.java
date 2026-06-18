package kr.co.igns.framework.config.response;

import lombok.Getter;

@Getter
public class SingleResponseDto<T> {

	private boolean success;
	private String code;
	private String msg;
	private T data;

	public SingleResponseDto(int statusCode, boolean success, String code, String message, T data) {
		if (statusCode == 200) {
			this.success = success;
		} else {
			this.success = false;
		}
		this.code = code;
		this.msg = message;
		this.data = data;
	}
}
