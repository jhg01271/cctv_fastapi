package kr.co.igns.framework.config.response;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.response
 * @ 파일명		: ResponseService.java
 * @ 기능명 		: 응답 서비스
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Service
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(200, "성공하였습니다.");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
    
    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // byte[] 결과를 처리하는 메소드
    public <T> byteArrayResult<T> getByteArrayResult(byte[] data) {
    	
    	byteArrayResult<T> result = new byteArrayResult<>();
    	result.setData(data);
    	setSuccessResult(result);
    	result.setMsg("teste");
    	return result;
    }
    
    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }
    
    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
    
    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    
    // map 결과를 처리하는 메소드
    public <T> MapResult<T, T> getMapResult(Map<T, T> map) {
    	MapResult<T, T> result = new MapResult<T, T>();
        result.setMap(map);
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());        
        return result;
    }
    
    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    																											}
    
    // map list 결과를 처리하는 메소드
    public CommonResult getMapListResult(Map<String, Object> resultList) {
		MapResult<String, Object> result = new MapResult<String, Object>();
        result.setMap(resultList);
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
        
        return result;
	}
}