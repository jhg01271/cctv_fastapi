package kr.co.igns.mobile.trainings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.mobile.trainings.model.TrainingsDetailVO;
import kr.co.igns.mobile.trainings.model.TrainingsSearchVO;
import kr.co.igns.mobile.trainings.model.TrainingsVO;
import kr.co.igns.mobile.trainings.service.V1TrainingsService;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.mobile.trainings.controller
 * ※ 파일명 : Committees
 * ※ 기능명 : 교육관리
 * ※ 작성자 : 소민환
 * ※ 최초생성일 : 2024. 11. 28.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "Trainings", description = "교육관리")
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class V1TrainingsController {
   private final MessageService messageService;
   private final V1TrainingsService v1TrainingsService;
   
   @Operation(summary = "[교육 결과 캘린더 count 조회]", description = "교육관리 캘린더 일정 개수를 조회하는 API")
   @GetMapping(value = "/trainings/calendar/count")
   public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingsCalendarCount(
           @ModelAttribute @Valid TrainingsSearchVO trainingsSearchVO) throws Exception {
	   Map<String, List<Map<String, Integer>>> data = v1TrainingsService.getTrainingsCalendarCount(trainingsSearchVO);
   	
       HashMap<String, Object> result = new HashMap<String, Object>();
       result.put("result", data);

       if (result == null || result.isEmpty()) {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                   messageService.getMessage("noResultFound.msg"), result);
       } else {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                   messageService.getMessage("readSuccess.msg"), result);
       }
   }
   
   @Operation(summary = "[교육 결과 목록 조회]", description = "지정된 기간내 사업장의 교육 결과를 조회하는 API")
   @GetMapping(value = "/trainings")
   public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainings(
           @ModelAttribute @Valid TrainingsSearchVO trainingsSearchVO) throws Exception {
       List<TrainingsVO> list = v1TrainingsService.getTrainings(trainingsSearchVO);
       
       HashMap<String, Object> result = new HashMap<String, Object>();
       result.put("list", list);

       if (list == null || list.isEmpty()) {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                   messageService.getMessage("noResultFound.msg"), result);
       } else {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                   messageService.getMessage("readSuccess.msg"), result);
       }
   }
   
   @Operation(summary = "[교육 결과 상세 조회]", description = "사업장의 특정 교육 결과의 상세 정보를 조회하는 API")
   @GetMapping(value = "/trainings/{id}")
   public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTrainingsDetail(
		   @PathVariable("id") String id ,@ModelAttribute @Valid TrainingsSearchVO trainingsSearchVO) throws Exception {
       TrainingsDetailVO vo = v1TrainingsService.getTrainingsDetail(id, trainingsSearchVO);
       
       HashMap<String, Object> result = new HashMap<String, Object>();
       result.put("result", vo);

       if (result == null || result.isEmpty()) {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                   messageService.getMessage("noResultFound.msg"), result);
       } else {
           return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                   messageService.getMessage("readSuccess.msg"), result);
       }
   }
}
