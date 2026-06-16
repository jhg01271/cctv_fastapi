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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.RiskAssessmentVO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.planning.model.WorkersOpinionsOnHazardsVO;
import kr.co.igns.business.planning.service.RiskAssessmentService;
import kr.co.igns.business.planning.service.WorkersOpinionsOnHazardsService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : RiskAssessment
 * ※ 기능명 : 위험성 평가
 * ※ 작성자 : 박성학
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "RiskAssessment", description = "위험성 평가")
@RestController
@RequiredArgsConstructor
public class WorkersOpinionsOnHazardsController {
    private final MessageService messageService;
    private final WorkersOpinionsOnHazardsService workerOpinionsOnHazardsService;

    @Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견 조회]", description = "위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getWorkersOpinionsOnHazards")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWorkersOpinionsOnHazards(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<WorkersOpinionsOnHazardsVO> list = workerOpinionsOnHazardsService.getWorkersOpinionsOnHazards(searchVo);

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
    
    @Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견 상세 조회]", description = "위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견 상세를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getWorkersOpinionsOnHazardsDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWorkersOpinionsOnHazardsDetail(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
    	System.out.println("getWorkersOpinionsOnHazardsDetail :::::::::::::::::::::::::::::::::::::::::::");

	    if(searchVo.getHrId() == null){
		    String hrId = workerOpinionsOnHazardsService.getHrId(searchVo);
		    searchVo.setHrId(hrId);
		}
		List<WorkersOpinionsOnHazardsVO> list = workerOpinionsOnHazardsService.getWorkersOpinionsOnHazardsDetail(searchVo);

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
    
    @Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견 사용자 별 상세 조회]", description = "위험성평가 사전 준비자료 > 유해 위험 요인 근로자 의견 사용자별 상세를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getWorkerHr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWorkerHr(
            @ModelAttribute @Valid WorkersOpinionsOnHazardsVO searchVo) throws Exception {
        List<WorkersOpinionsOnHazardsVO> list = workerOpinionsOnHazardsService.getWorkerHr(searchVo);

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

	@Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견 상세 저장]", description = "위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견 상세데이터를 저장한다.")
	@PostMapping(value = "/safewizpro/planning/setWorkerOpinionsOnHazardsDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createWorkersOpinionsOnHazardsDetail(@RequestBody @Valid List<WorkersOpinionsOnHazardsVO> saveVo) throws Exception {
		List<WorkersOpinionsOnHazardsVO> rtnDto = workerOpinionsOnHazardsService.createWorkersOpinionsOnHazardsDetail(saveVo);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견 경험담 저장]", description = "위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견 경험담 데이터를 저장한다.")
	@PostMapping(value = "/safewizpro/planning/setWorkerOpinionsOnHazardsExp")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> createWorkersOpinionsOnHazardsExp(@RequestBody @Valid List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		List<WorkersOpinionsOnHazardsVO> rtnDto = workerOpinionsOnHazardsService.createWorkersOpinionsOnHazardsExp(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견유해 상세 삭제]", description = "위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견유해 상세데이터를 삭제한다.")
	@PostMapping(value = "/safewizpro/planning/deleteWorkerOpinonsOnHarzards")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWorkerOpinonsOnHarzards(@RequestBody @Valid List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		List<WorkersOpinionsOnHazardsVO> rtnDto = workerOpinionsOnHazardsService.changeWorkersOpinionsOnHazardsUseN(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>(); 
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}
	
	@Operation(summary = "[위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견유해 상세 삭제]", description = "위험성평가 사전 준비자료 > 유해 위험요인 근로자 의견유해 상세데이터를 삭제한다.")
	@PostMapping(value = "/safewizpro/planning/deleteWorkerOpinonsOnHarzardsDetail")
	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWorkerOpinonsOnHarzardsDetail(@RequestBody @Valid List<WorkersOpinionsOnHazardsVO> saveVoList) throws Exception {
		List<WorkersOpinionsOnHazardsVO> rtnDto = workerOpinionsOnHazardsService.deleteWorkerOpinonsOnHarzardsDetail(saveVoList);

		HashMap<String, Object> result = new HashMap<String, Object>(); 
		result.put("result", rtnDto);
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
	}

	@Operation(summary = "[유해 위험요인 근로자 의견유해 리포트 마스터에서 인원 선택]", description = "유해 위험요인 근로자 마스터 출력물 다운로드 ")
	@PostMapping(value = "/safewizpro/planning/getWorkerReport")
	public void getWorkerReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody List<WorkersOpinionsOnHazardsVO> vo) throws Exception {
		 
		workerOpinionsOnHazardsService.getWorkerReportList(request, response, vo);
	}
	
	@Operation(summary = "[유해 위험요인 근로자 의견유해 리포트 상세]", description = "유해 위험요인 근로자 상세 출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getWorkerReportDetail")
	public void getWorkerReportDetail(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody List<WorkersOpinionsOnHazardsVO> vo) throws Exception {
		 
		workerOpinionsOnHazardsService.getWorkerReport(request, response, vo);
	}
}
