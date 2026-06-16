package kr.co.igns.business.task.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.AnnualTrainingPlanVO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.task.service.TaskService;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 :
 * ※ 파일명 : TaskController.java
 * ※ 기능명 : Task 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2025. 1. 24.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

@Tag(name = "Task", description = "Task")
@RestController
@RequiredArgsConstructor
@RequestMapping("/safewizpro/task")
public class TaskController {
	private final MessageService messageService;
	private final TaskService taskService;

	@Operation(summary = "[Task(서명) 조회]", description = "서명에 대한 Task 조회한다")
	@GetMapping(value = "/getTask")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTask(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
		
		List<TaskVO> list = taskService.getTask(searchVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);
		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[Task(서명) 확인]", description = "Task 요청에 대해 확인상태로 수정한다.")
	@PostMapping(value = "/updateTaskChk")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateTaskChk(
			@RequestBody TaskVO vo)
			throws Exception {
		BaseVO rtnDto = taskService.updateTaskChk(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}
}
