package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ImprovementAndImplementationVO;
import kr.co.igns.business.planning.service.ImprovementAndImplementationService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "improvementAndImplementation", description = "개선 실행 및 결과")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ImprovementAndImplementationController {
	private final MessageService msgSvc;
	private final ImprovementAndImplementationService svc;
	
	@Operation(summary = "[개선 실행 및 결과 목록 조회]", description = "개선 실행 및 결과 목록을 조회한다")
    @GetMapping(value = "/safewizpro/planning/improvementandimplementation/searchData")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchData(@ModelAttribute @Valid ImprovementAndImplementationVO vo) throws Exception {
		List<ImprovementAndImplementationVO> list = svc.searchData(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"), msgSvc.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
        }
	}
	
	@Operation(summary = "[개선 실행 및 결과 상세조회]", description = "개선 실행 및 결과 상세내용을 조회한다")
    @GetMapping(value = "/safewizpro/planning/improvementandimplementation/searchDataDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> searchDataDetail(@ModelAttribute @Valid ImprovementAndImplementationVO vo) throws Exception {
		List<ImprovementAndImplementationVO> list = svc.searchDataDetail(vo);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("noResultFound.code"), msgSvc.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, msgSvc.getMessage("readSuccess.code"), msgSvc.getMessage("readSuccess.msg"), result);
        }
	}
	
	@Operation(summary = "[개선 실행 및 결과 출력]", description = "개선 실행 및 결과을 출력한다")
	@PostMapping(value = "/safewizpro/planning/improvementandimplementation/getReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO listVo) throws Exception {
		svc.getReport(request, response, listVo);
	}
}