package kr.co.igns.business.alarm.controller;
import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.alarm.model.AlarmVO;
import kr.co.igns.business.alarm.model.TrainingPlanImplAlarmVO;
import kr.co.igns.business.alarm.service.AlarmService;
import kr.co.igns.business.support.model.TrainingPlanImplVO;
import kr.co.igns.business.support.model.TrainingResultReportVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.setting.model.AuthVO;
import kr.co.igns.system.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Alarm")
@RequestMapping("/safewizpro/alarm")
@RestController
@RequiredArgsConstructor
public class AlarmController {
	private final ResponseService responseService;
	private final MessageService messageService;
	private final AlarmService alarmService;

	//private static final Logger log = LogManager.getLogger("com.project");

	@Operation(summary = "[알림 조회]", description = "모든 푸시 알림을 조회한다")
	@GetMapping(value = "/getAlarm")
	public ResponseEntity<SingleResponseDto<List<AlarmVO>>> getAlarm() throws Throwable {
		List<AlarmVO> result = alarmService.getAlarm();

		if (result == null || result.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[읽은 알림 업데이트]", description = "읽은 알림을 readYn = 'Y'처리 한다")
	@PostMapping(value = "/updateReadAlarm")
	public ResponseEntity<SingleResponseDto<Map<String, Object>>> updateReadAlarm( @ModelAttribute @Valid AlarmVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());

		Map<String, Object> result = alarmService.updateReadAlarm(vo);
		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[비상 상황 발생 알림 발송]", description = "비상 상황 발생 알림을 발송한다.")
	@PostMapping(value = "/sendEmergencyMsg")
	public ResponseEntity<SingleResponseDto<AlarmVO>> sendEmergencyMsg() throws Throwable {
		AlarmVO result = alarmService.sendEmergencyMsg();

		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[안전보건 교육 실시 계획서 알림 발송]", description = "안전보건 교육 실시 계획서 알림을 발송한다.")
	@PostMapping(value = "/sendTrainingPlanImplMsg/{title}/{menuId}")
	public ResponseEntity<SingleResponseDto<AlarmVO>> sendTrainingPlanImplMsg(@RequestBody @Valid TrainingPlanImplVO vo,  @PathVariable String title, @PathVariable String menuId) throws Throwable {
		alarmService.sendTrainingPlanImplMsg(vo, title, menuId);

		AlarmVO result = new AlarmVO();
		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}


	@Operation(summary = "[알림 발송을 위한 token 여부 조회]", description = "알림 발송을 위한 token 여부를 조회한다.")
	@PostMapping(value = "/getSelectedTokenUserInfo")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSelectedTokenUserInfo( @RequestBody List<String> voList) throws Exception {
		List<HrVO> hrVoList = alarmService.getSelectedTokenUserInfo(voList);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", hrVoList);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
				messageService.getMessage("createSuccess.msg"), result);
	}


}
