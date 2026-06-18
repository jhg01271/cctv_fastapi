package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import kr.co.igns.business.planning.model.ToolBoxMeetingSettingVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.igns.business.planning.model.ToolBoxMeetingVO;
import kr.co.igns.business.planning.service.ToolBoxMeetingService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotNull;

@Tag(name = "ToolBoxMeeting", description = "개선 실행 및 결과")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ToolBoxMeetingController {
	private final MessageService msgSvc;
	private final ToolBoxMeetingService svc;
	
	@Operation(summary = "[TBM 목록 조회]", description = "TBM 목록을 조회한다")
    @GetMapping(value = "/safewizpro/planning/toolboxmeeting/searchData")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchData(@ModelAttribute @Valid ToolBoxMeetingVO vo) throws Exception {
		List<ToolBoxMeetingVO> list = svc.searchData(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"), msgSvc.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
        }
	}
	
	@Operation(summary = "[TBM 상세조회]", description = "TBM 상세내용을 조회한다")
    @GetMapping(value = "/safewizpro/planning/toolboxmeeting/searchDataDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchDataDetail(@ModelAttribute @Valid ToolBoxMeetingVO vo) throws Exception {
		List<ToolBoxMeetingVO> list = svc.searchDataDetail(vo);
		List<ToolBoxMeetingVO> list2 = svc.searchPotenialRisk(vo);
		List<ToolBoxMeetingVO> list3 = svc.searchKeyRisk(vo);
		List<ToolBoxMeetingVO> list4 = svc.searchSafeCheck(vo);
		List<ToolBoxMeetingVO> list5 = svc.searchSelectedDatas(vo);
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        result.put("list2", list2);
        result.put("list3", list3);
        result.put("list4", list4);
		result.put("list5", list5);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"), msgSvc.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
        }
	}

	@Operation(summary = "[TBM 설정 조회]", description = "TBM 설정 사항을 조회한다.")
	@GetMapping(value = "/safewizpro/planning/toolboxmeeting/searchSetting")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchSetting(@ModelAttribute @Valid ToolBoxMeetingSettingVO vo) throws Exception {
		List<ToolBoxMeetingSettingVO> list = svc.searchSetting(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);

		if (list == null || list.isEmpty()) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"), msgSvc.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
		}
	}

	@Operation(summary = "[TBM 설정 저장]", description = "TBM 설정을 저장한다")
	@PostMapping(value = "/safewizpro/planning/toolboxmeeting/saveSetting")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveSetting(@RequestBody @Valid List<ToolBoxMeetingSettingVO> voList) throws Exception {
		try {
			List<ToolBoxMeetingSettingVO> rtnList = svc.saveSetting(voList);

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("result", rtnList);
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("createSuccess.code"), msgSvc.getMessage("createSuccess.msg"), result);
		} catch(Exception e) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, false, "", e.getMessage(), null);
		}
	}

	@Operation(summary = "[TBM 저장]", description = "TBM을 저장한다")
	@PostMapping(value = "/safewizpro/planning/toolboxmeeting/saveData")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveData(@NotNull @RequestPart("info") @Valid String infoJson, @RequestPart(value = "files", required = false)List<MultipartFile> files) throws Exception {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
	        HashMap<String, Object> param = mapper.readValue(infoJson, new TypeReference<HashMap<String, Object>>() {});
	        
			ToolBoxMeetingVO saveResult = svc.saveData(param, files);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("result", saveResult);
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("createSuccess.code"), msgSvc.getMessage("createSuccess.msg"), result);
		} catch(Exception e) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, false, "", e.getMessage(), null);
		}
	}
	
	@Operation(summary = "[TBM 삭제]", description = "TBM을 삭제한다")
	@DeleteMapping(value = "/safewizpro/planning/toolboxmeeting/deleteData")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteData(@RequestBody @Valid List<ToolBoxMeetingVO> vo) throws Exception {
		svc.deleteData(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", null);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("createSuccess.code"), msgSvc.getMessage("createSuccess.msg"), result);
	}
	
	@Operation(summary = "[TBM 출력]", description = "TBM을 출력한다")
	@PostMapping(value = "/safewizpro/planning/toolboxmeeting/getReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @RequestBody SpSearchVO vo) throws Exception {
		svc.getReport(request, response, vo);
	}
}