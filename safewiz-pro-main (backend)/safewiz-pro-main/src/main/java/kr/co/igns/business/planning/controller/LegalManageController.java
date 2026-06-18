package kr.co.igns.business.planning.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.business.planning.model.LegalSearchVO;
import kr.co.igns.business.planning.service.LegalManageService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.planning.controller
 * ※ 파일명 : LegalManage
 * ※ 기능명 : 법규관리
 * ※ 작성자 : 김동건
 * ※ 최초생성일 : 2024. 10. 21.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "LegalManage", description = "법규 관리")
@RestController
@RequiredArgsConstructor
public class LegalManageController {
    private final MessageService messageService;
    private final LegalManageService legalManageService;

    @Operation(summary = "[법규관리 조회]", description = "법규관리를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalManageList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalManageList(
            @ModelAttribute @Valid LegalManageVO vo) throws Exception {
        List<LegalManageVO> list = legalManageService.getLegalManageList(vo);

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
    
    @Operation(summary = "[법규관리 popup 조회]", description = "법규관리 popup을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalManageTypeList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalManageTypeList(
            @ModelAttribute @Valid LegalManageVO vo) throws Exception {
        List<LegalManageVO> list = legalManageService.getLegalManageTypeList(vo);

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
    
    @Operation(summary = "[법규 관리 데이터 예시 조회]", description = "법규 관리 데이터 예시를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getDatasetLegalManageType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetLegalManageType(SpSearchVO vo) throws Exception {
        List<LegalManageVO> list = legalManageService.getDatasetLegalManageType(vo);

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
    
    @Operation(summary = "[안전보건법령 목록]", description = "안전보건공단에서 제공하는 안전보건법령의 목록을 제공")
    @PostMapping(value="/safewizpro/planning/getLegalList")
    public ResponseEntity<SingleResponseDto<LegalSearchVO>> getLegalList(@RequestBody SpSearchVO vo) throws Exception {
    	LegalSearchVO res = legalManageService.getLegalList(vo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
    }
    
    @Operation(summary = "[법규검토서, 위험성평가 법규 popup 조회]", description = "법규 popup을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalManageListPopup")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalManageListPopup(
            @ModelAttribute @Valid LegalManageVO vo) throws Exception {
        List<LegalManageVO> list = legalManageService.getLegalManageListPopup(vo);

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
    
    @Operation(summary = "[법규관리 상세 마스터 조회]", description = "법규관리 상세 마스터 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalManageDetailMasterList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalManageDetailMasterList(
            @ModelAttribute @Valid LegalManageVO vo) throws Exception {
        List<LegalManageVO> list = legalManageService.getLegalManageDetailMasterList(vo);

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
    
    @Operation(summary = "[법규관리 상세 조회]", description = "법규관리 상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/planning/getLegalManageDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getLegalManageDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
    	List<LegalManageDetailVO> list = legalManageService.getLegalManageDetail(spSearchVO);

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
  

    @Operation(summary = "[법규관리 저장]", description = "법규관리 데이터를 저장한다")
    @PostMapping(value = "/safewizpro/planning/saveLegalManage")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveLegalManage(
    		@RequestPart(value = "data") List<Object> dataVo) throws Exception {  
    	
    	// master Data
    	Map<String, Object> masterDataVo = new HashMap<>();
    	LegalManageVO masterLegalVo = new LegalManageVO();
    	
    	masterDataVo = (Map<String, Object>) dataVo.get(0);
    	    	
    	masterLegalVo.setWriteYear((String) masterDataVo.get("writeYear"));
    	masterLegalVo.setDocType((String) masterDataVo.get("docType"));
    	masterLegalVo.setDocNo((String) masterDataVo.get("docNo"));
    	masterLegalVo.setCompId((String) masterDataVo.get("compId"));
    	masterLegalVo.setLegalId((String) masterDataVo.get("legalId"));
    	masterLegalVo.setLegalNm((String) masterDataVo.get("legalNm"));
    	masterLegalVo.setUseYn((String) masterDataVo.get("useYn"));
    	masterLegalVo.setDivFg((String) masterDataVo.get("divFg"));
    	masterLegalVo.setRevisionAt((String) masterDataVo.get("revisionAt"));
    	masterLegalVo.setCreatedAt((String) masterDataVo.get("createdAt"));
    	masterLegalVo.setUpdatedAt((String) masterDataVo.get("updateAt"));
    	masterLegalVo.setConfirmedYn((String) masterDataVo.get("confirmedYn"));
    	//masterLegalVo.setRevisionBy((String) masterDataVo.get("revisionBy"));
    	
    	
    	masterLegalVo.setCmd((String) masterDataVo.get("cmd"));
    	
    	LegalManageVO legalVo = (LegalManageVO) masterLegalVo; //.get("master");
    	
    	if(legalVo.getCmd().equals("I")) {
    		String docNo = legalManageService.getDocNo(legalVo);
        	
        	legalVo.setDocNo(docNo);
    	}  	
    	
    	LegalManageVO list = legalManageService.saveLegalManage(legalVo);
    	   
    	//Detail Data
    	List<Map<String, Object>> detailDataListVo = new ArrayList<>();
    	detailDataListVo = (List<Map<String, Object>>) dataVo.get(1);
    	
    	List<Map<String, Object>> listDetail = legalManageService.saveLegalManageDetail(detailDataListVo,list);
    	
    	
		 HashMap<String, Object> result = new HashMap<String, Object>();
	     result.put("result", list);
	     return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
	             messageService.getMessage("updateSuccess.msg"), result);

    }
   
    @Operation(summary = "[법규 차수 수정]", description = "법규 관리의 차수를 수정한다.")
    @PutMapping(value = "/safewizpro/planning/confirmLegalManage")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> confirmLegalManage(
            @RequestBody @Valid LegalManageVO reqVO) throws Exception {
        BaseVO rtnDto = legalManageService.confirmLegalManage(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규 관리 데이터 등록]", description = "법규 관리 데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/planning/saveLegalManageType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHrJbrp(
            @RequestBody List<LegalManageVO> reqVO)
            throws Exception {
        BaseVO rtnDto = legalManageService.saveLegalManageType(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규관리 삭제]", description = "법규관리 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/planning/deleteLegalManage")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalManage(
            @RequestBody List<LegalManageVO> list
    ) throws Exception {
    	      legalManageService.deleteLegalManage(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규 관리 항목 삭제]", description = "법규 관리 항목 데이터를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteLegalManageType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalManageType(
            @RequestBody List<LegalManageVO> list)
            throws Exception {
    	legalManageService.deleteLegalManageType(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    @Operation(summary = "[법규관리 상세 삭제]", description = "법규관리 상세 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/planning/deleteLegalManageDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteLegalManageDetail(
            @RequestBody List<LegalManageDetailVO> list
    ) throws Exception {
    	legalManageService.deleteLegalManageDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
    
    
    @Operation(summary = "[조항 조회]", description = "조항을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getLegalNm")
    public ResponseEntity<SingleResponseDto<Integer>> getLegalNm(
    		@ModelAttribute @Valid LegalManageVO vo) throws Exception {
    	Integer list = legalManageService.getLegalNm(vo);
        
        if (list == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), list);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), list);
        }
    }
    
    @Operation(summary = "[법규의 구분을 조회]", description = "법규의 구분을 조회한다")
    @GetMapping(value = "/safewizpro/planning/getValidLegalDivFg")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getValidLegalDivFg(
    		@ModelAttribute @Valid LegalManageVO vo) throws Exception {
    	List<LegalManageVO> list = legalManageService.getValidLegalDivFg(vo);
        
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
    
    
    @Operation(summary = "[법규관리 레포트 ]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getLegalManageReport")
	public void getLegalManageReport(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestPart(value = "data") List<Object> dataVo
			) throws Exception {
		
    	legalManageService.getLegalManageReport(request, response, dataVo);
	}
    
    @Operation(summary = "[법규관리 레포트 Card ]", description = "Card 출력물 다운로드")
	@PostMapping(value = "/safewizpro/planning/getLegalManageReportCard")
	public void getLegalManageReportCard(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestBody @Valid SpSearchVO spSearchVO
			) throws Exception {
		
    	legalManageService.getLegalManageReportCard(request, response, spSearchVO);
	}
    
    

}
