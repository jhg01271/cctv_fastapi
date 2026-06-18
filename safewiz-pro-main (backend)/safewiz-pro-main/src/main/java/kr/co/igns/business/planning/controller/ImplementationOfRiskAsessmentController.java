package kr.co.igns.business.planning.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.igns.business.planning.model.*;
import kr.co.igns.business.utils.model.UtilsVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.service.ImplementationOfRiskAseessmentService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : ImplementationOfRiskAsessmentController
 * ※ 기능명 : 위험성 평가 > 위험성평가표 > 위험성평가 이행
 * ※ 작성자 : 윤다영
 * ※ 최초생성일 : 2024. 11. 12.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ImplementationOfRiskAsessment", description = "위험성 평가 이행")
@RestController
@RequiredArgsConstructor
public class ImplementationOfRiskAsessmentController {
    private final MessageService messageService;
    private final ImplementationOfRiskAseessmentService implementationOfRiskAseessmentService;

    @Operation(summary = "[위험성평가 이행 목록 조회]", description = "위험성평가 이행 목록을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskImplList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskImplList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<RiskAssessmentPlanVO> list = implementationOfRiskAseessmentService.getRiskImplList(searchVo);

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
    
    @Operation(summary = "[위험성평가 이행 상세 조회]", description = "위험성평가 이행 상세 조회한다")
    @GetMapping(value = "/safewizpro/planning/getRiskAssessmentDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentDetail(
            @ModelAttribute @Valid ClassificationOfHazardsVO searchVo) throws Exception {
        List<ClassificationOfHazardsVO> list = implementationOfRiskAseessmentService.getRiskAssessmentDetail(searchVo);

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

	@Operation(summary = "[위험성평가 이행 상세 조회]", description = "위험성평가 이행 상세의 모든 데이터를 조회한다.")
	@GetMapping(value = "/safewizpro/planning/getRiskAssessmentDetailAll")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentDetailAll(
			@ModelAttribute @Valid ClassificationOfHazardsVO searchVo) throws Exception {
		List<ClassificationOfHazardsVO> list = implementationOfRiskAseessmentService.getRiskAssessmentDetailAll(searchVo);

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

    @Operation(summary = "[위험성평가 계획 상세 팝업 조회]", description = "위험성평가 계획 팝업에 대한 이행의 모든 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getRiskAssessmentDetailPopupAll")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getRiskAssessmentDetailPopupAll(
            @ModelAttribute @Valid ClassificationOfHazardsVO searchVo) throws Exception {
        List<ClassificationOfHazardsVO> list = implementationOfRiskAseessmentService.getRiskAssessmentDetailPopupAll(searchVo);

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

    @Operation(summary = "[위험성평가 계획 상세 팝업 - 이행 조회 시 이미 등록한 데이터 조회]", description = "위험성평가 계획 팝업에 대한 이행의 이미 등록한 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getReferenceRiskImplList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getReferenceRiskImplList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ImplementationOfRiskAseessmentVO> list = implementationOfRiskAseessmentService.selectReferenceRiskImplList(searchVo);

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

    @Operation(summary = "[위험성평가 이행 - 공정/물질설비 반영 시 제거 되는 데이터 조회]", description = "위험성평가 이행 - 공정/물질설비 반영 시 제거 되는 데이터 조회한다.")
    @GetMapping(value = "/safewizpro/planning/checkRemoveRiskImplList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> checkRemoveRiskImplList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ImplementationOfRiskAseessmentVO> list = implementationOfRiskAseessmentService.checkRemoveRiskImplList(searchVo);

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
    
    @Operation(summary = "[이행, 감소대책, 담당자 등록 및 수정]", description = "이행, 감소대책, 담당자를 등록한다")
    @PostMapping(value = "/safewizpro/planning/saveRiskImpl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveClassProcEquipMsds(
    		@RequestPart(value="reqVo") List<ClassificationOfHazardsVO> reqVo, @RequestPart(value="plan") RiskAssessmentPlanVO planVO)
            throws Exception {
    	
    	List<ClassificationOfHazardsVO> list = implementationOfRiskAseessmentService.saveRiskImpl(reqVo, planVO);
    	
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
    
    @Operation(summary = "[위험성평가 계획 상세 팝업 - 이행 등록 및 수정]", description = "위험성평가 계획 상세 팝업 이행을 등록한다")
    @PostMapping(value = "/safewizpro/planning/saveMainRiskImpl")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveMainRiskImpl(
            @RequestBody List<ClassificationOfHazardsVO> reqVo)
            throws Exception {

        List<ClassificationOfHazardsVO> list = implementationOfRiskAseessmentService.saveMainRiskImpl(reqVo);

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
    
    @Operation(summary = "[개선 이미지 등록]", description = "개선 이미지를 등록한다")
    @PostMapping(value = "/safewizpro/planning/saveRiskImplFiles")
    public ResponseEntity<SingleResponseDto<String>> saveRiskImplFiles(
    		@RequestPart(value="prevFiles", required = false) List<MultipartFile> prevFiles,
    		@RequestPart(value="afterFiles", required = false) List<MultipartFile> afterFiles,
    		@RequestPart(value="afterVo", required = false) List<ImplementationOfRiskAseessmentReductionVO> afterVo,
    		@RequestPart(value="prevVo", required = false) List<ImplementationOfRiskAseessmentReductionVO> prevVo,
    		@RequestPart(value="deletFiles", required = false) List<String> deleteFiles)
            throws Exception {
    	
        String result = implementationOfRiskAseessmentService.saveRiskImplImage(prevVo, afterVo, prevFiles, afterFiles, deleteFiles);

        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[위험성평가 이행 출력]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getRiskImplReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response
			, @RequestBody List<ClassificationOfHazardsVO> spSearchVO) throws Exception {
    	implementationOfRiskAseessmentService.getReport(request, response, spSearchVO);
	}

	@Operation(summary = "[위험성평가 이행 전체 출력]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getRiskImplReportAll")
	public void getReportAll(HttpServletRequest request, HttpServletResponse response
			, @RequestBody List<ClassificationOfHazardsVO> spSearchVO) throws Exception {
		implementationOfRiskAseessmentService.getReportAll(request, response, spSearchVO);
	}

	@Operation(summary = "[근로자 확인 팝업 - 근로자 일괄 삭제]", description = "현재 조직과 일치하지 않는 근로자들을 일괄 삭제한다")
	@PostMapping(value = "/safewizpro/planning/deleteWorkerApprovalInfoAll")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWorkerApprovalInfoAll(
			@RequestBody UtilsVO vo) throws Exception {
		UtilsVO list = implementationOfRiskAseessmentService.deleteWorkerApprovalInfoAll(vo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("list", list);

		if (list == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), result);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}
}
