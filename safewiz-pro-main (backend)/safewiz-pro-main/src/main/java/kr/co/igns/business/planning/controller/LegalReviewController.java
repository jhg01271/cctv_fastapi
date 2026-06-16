package kr.co.igns.business.planning.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.*;
import kr.co.igns.business.planning.service.LegalReviewService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller
 * ※ 파일명 : LegalManage
 * ※ 기능명 : 법규 검토서
 * ※ 작성자 : 김동건
 * ※ 최초생성일 : 2024. 10. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "LegalReview", description = "법규 검토서")
@RestController
@RequiredArgsConstructor
public class LegalReviewController {
    private final MessageService messageService;
    private final LegalReviewService legalReviewService;

    @Operation(summary = "[법규검토서 조회]", description = "법규검토서를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalReviewList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalReviewList(
            @ModelAttribute @Valid SpSearchVO vo) throws Exception {
        List<LegalReviewVO> list = legalReviewService.getLegalReviewList(vo);

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
    
    @Operation(summary = "[법규검토서 상세 마스터 조회]", description = "법규검토서 상세 마스터 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalReviewDetailMasterList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalReviewDetailMasterList(
            @ModelAttribute @Valid SpSearchVO vo) throws Exception {
        List<LegalReviewVO> list = legalReviewService.getLegalReviewDetailMasterList(vo);

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
    
    
    @Operation(summary = "[법규검토서 상세 조회]", description = "법규검토서 상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getLegalReviewDetailList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalReviewDetailList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
    	List<LegalReviewDetailVO> list = legalReviewService.getLegalReviewDetailList(spSearchVO);

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

    @Operation(summary = "[법규 검토서 - 준수평가표 조직 팝업 조회]", description = "법규검토서 상세 조직 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getLegalReviewDetailOrgnList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalReviewDetailOrgnList(
            @ModelAttribute @Valid LegalReviewDetailVO vo) throws Exception {
        List<LegalReviewDetailOrgnVO> list = legalReviewService.getLegalReviewDetailOrgnList(vo);

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

    @Operation(summary = "[법규검토서 저장]", description = "법규검토서 데이터를 저장한다")
    @PostMapping(value = "/safewizpro/planning/saveLegalReview")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveLegalReview(
    		@RequestPart(value = "data") List<Object> dataVo) throws Exception {  
    	
    	
    	// master Data
    	Map<String, Object> masterDataVo = new HashMap<>();
    	LegalReviewVO masterLegalVo = new LegalReviewVO();

    	masterDataVo = (Map<String, Object>) dataVo.get(0);
    	
    	masterLegalVo.setWriteYear((String) masterDataVo.get("writeYear"));
    	masterLegalVo.setDocType((String) masterDataVo.get("docType"));
    	masterLegalVo.setDocNo((String) masterDataVo.get("docNo"));
    	masterLegalVo.setCompId((String) masterDataVo.get("compId"));
        masterLegalVo.setLegalReviewNm((String) masterDataVo.get("legalReviewNm"));
    	masterLegalVo.setCreatedAt((String) masterDataVo.get("createdAt"));
    	masterLegalVo.setUpdatedAt((String) masterDataVo.get("updateAt"));
    	masterLegalVo.setCreatedBy((String) masterDataVo.get("createdBy"));
    	masterLegalVo.setUpdatedBy((String) masterDataVo.get("updatedBy"));
    	masterLegalVo.setUseYn((String) masterDataVo.get("useYn"));
    	masterLegalVo.setCmd((String) masterDataVo.get("cmd"));
    	
    	LegalReviewVO legalVo = (LegalReviewVO) masterLegalVo; 
    	
    	
    	if(legalVo.getCmd().equals("I")) {
    		String docNo = legalReviewService.getDocNo(legalVo);
        	
        	legalVo.setDocNo(docNo);
    	}  	
    	
    	LegalReviewVO list = legalReviewService.saveLegalReview(legalVo);
    	 	
    	//Detail Data
    	List<Map<String, Object>> detailDataListVo = new ArrayList<>();
    	detailDataListVo = (List<Map<String, Object>>) dataVo.get(1);
    	
    	List<Map<String, Object>> listDetail = legalReviewService.saveLegalReviewDetail(detailDataListVo,list);
    	
		 HashMap<String, Object> result = new HashMap<String, Object>();
	     result.put("result", list);
	     return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
	             messageService.getMessage("updateSuccess.msg"), result);

    }
    
    @Operation(summary = "[법규검토서 삭제]", description = "법규검토서 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/planning/deleteLegalReview")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalReview(
            @RequestBody List<LegalReviewVO> list
    ) throws Exception {
    	      legalReviewService.deleteLegalReview(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규검토서 상세 삭제]", description = "법규검토서 상세 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/planning/deleteLegalReviewDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalReviewDetail(
            @RequestBody List<LegalReviewDetailVO> list
    ) throws Exception {
    	legalReviewService.deleteLegalReviewDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규검토서 레포트]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getLegalReviewReport")
	public void getLegalReviewReport(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestBody List<LegalReviewDetailVO> dataVo
			) throws Exception {
		
    	legalReviewService.getLegalReviewReport(request, response, dataVo);
	}
    
    @Operation(summary = "[법규검토서 레포트 전체 ]", description = "출력물 전체 다운로드")
	@PostMapping(value = "/safewizpro/planning/getLegalReviewReportAll")
	public void getLegalReviewReportAll(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestBody SpSearchVO spSearchVO
			) throws Exception {
		
    	legalReviewService.getLegalReviewReportAll(request, response, spSearchVO);
	}

}
