package kr.co.igns.business.improvement.contoller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.firebase.database.annotations.NotNull;
import kr.co.igns.system.common.model.BaseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryInspectionVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryRequestVO;
import kr.co.igns.business.improvement.model.SafetyMgmtOfHazardousMachineryVO;
import kr.co.igns.business.improvement.service.SafetyMgmtOfHazardousMachineryService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "위험기계 및 기구 안전관리", description = "위험기계 및 기구 안전관리")
@RestController
@RequestMapping(value = "/safewizpro/improvement")
@RequiredArgsConstructor
public class SafetyMgmtOfHazardousMachineryController {
	private final SafetyMgmtOfHazardousMachineryService safetyMgmtService;
	private final MessageService messageService;
	
	@PostMapping(value = "/saveSafetyMgmt")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSafetyMgmt(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
																					   @RequestPart(value = "fileKeys", required = false) List<String> fileKeys,
																					   @RequestPart(value = "info") SafetyMgmtOfHazardousMachineryRequestVO vo) throws Exception {

		// 파일 키에 따라 List<MultipartFile>을 Map으로 수동 매핑
		Map<String, List<MultipartFile>> filesMap = new HashMap<>();
		if(files != null) {
			for (int i = 0; i < files.size(); i++) {
				String key = fileKeys.get(i);
				filesMap.computeIfAbsent(key, k -> new ArrayList<>()).add(files.get(i));
			}
		}
		safetyMgmtService.saveSafetyMgmt(filesMap, vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", vo);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createdSuccess.code"),
                messageService.getMessage("readSuccess.msg"), result);
	}
	
	@GetMapping(value = "/getSafetyMgmts")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyMgmts(
				@ModelAttribute SpSearchVO searchVo
			) {
		
		System.out.println(searchVo);
		List<SafetyMgmtOfHazardousMachineryRequestVO> safetyMgmts = safetyMgmtService.getSafetyMgmts(searchVo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", safetyMgmts);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                messageService.getMessage("readSuccess.msg"), result);
	}
	
	@GetMapping(value = "/getSafetyMgmtDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSafetyMgmtDetail(
				@ModelAttribute SafetyMgmtOfHazardousMachineryVO vo
			) {
		SafetyMgmtOfHazardousMachineryRequestVO resVo = safetyMgmtService.getSafetyMgmtDetail(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", resVo);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
				messageService.getMessage("readSuccess.msg"), result);
	}
	
	@PostMapping(value = "/getHazardousMachineryReport")
	public void getHazardousMachineryReport(
				HttpServletRequest request, HttpServletResponse response,
				@RequestBody List<SafetyMgmtOfHazardousMachineryRequestVO> voList
			) throws Exception {
		safetyMgmtService.getHazardousMachineryReport(request, response, voList);
	}

	@PostMapping(value = "/getHazardousMachineryReportDetail")
	public void getHazardousMachineryReportDetail(
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<SafetyMgmtOfHazardousMachineryRequestVO> voList
	) throws Exception {
		safetyMgmtService.getHazardousMachineryReportDetail(request, response, voList);
	}
	
	// 설비 삭제 (useYn => N)
	@DeleteMapping(value = "/deleteSafetyMgmts")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyMgmts(
				@RequestBody List<SafetyMgmtOfHazardousMachineryVO> deleteList
			) throws Exception {
		
		safetyMgmtService.deleteSafetyMgmts(deleteList);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", deleteList);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
	}
	
	@DeleteMapping(value = "/deleteSafetyMgmtInspections")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteSafetyMgmtInspections(
				@RequestBody List<SafetyMgmtOfHazardousMachineryInspectionVO> deleteList
			) throws Exception {
		safetyMgmtService.deleteSafetyMgmtInspections(deleteList);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", deleteList);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
	}
}
