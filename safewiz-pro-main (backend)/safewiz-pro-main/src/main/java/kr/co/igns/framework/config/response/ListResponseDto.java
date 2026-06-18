package kr.co.igns.framework.config.response;

import java.util.List;

import lombok.Getter;

@Getter
public class ListResponseDto<T> {

	private boolean success;
	private String code;
	private String msg;
	private List<T> list;

	public ListResponseDto(int statusCode, boolean success, String code, String message, List<T> list) {
		if (statusCode == 200) {
			this.success = success;
		} else {
			this.success = false;
		}
		this.code = code;
		this.msg = message;
		this.list = list;
	}
}
