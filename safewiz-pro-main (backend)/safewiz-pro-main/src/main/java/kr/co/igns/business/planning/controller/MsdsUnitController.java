package kr.co.igns.business.planning.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ChemDetailVO;
import kr.co.igns.business.planning.model.ChemSearchVO;
import kr.co.igns.business.planning.model.MsdsUnitVO;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.business.planning.service.MsdsService;
import kr.co.igns.business.planning.service.MsdsUnitService;
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
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.safewiz.planning.controller
 * ※ 파일명 : Msds
 * ※ 기능명 : MSDS 관리
 * ※ 작성자 : 박성학
 * ※ 최초생성일 : 2024. 10. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Msds", description = "MSDS Unit 관리")
@RestController
@RequiredArgsConstructor
public class MsdsUnitController {
    private final MessageService messageService;
    private final MsdsUnitService msdsUnitService;

    @Operation(summary = "[MSDS Unit 조회]", description = "MSDS를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getMsdsUnit")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsds(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<MsdsUnitVO> list = msdsUnitService.getMsdsUnit(searchVo);

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

    @Operation(summary = "[MSDS Unit 조회]", description = "MSDS를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getMsdsUnitDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsdsUnitDataset(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<MsdsUnitVO> list = msdsUnitService.getMsdsUnitDataset(searchVo);

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

    @Operation(summary = "[MSDS 등록]", description = "MSDS를 등록한다.")
    @PostMapping(value = "/safewizpro/planning/insertMsdsUnit")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertMsdsUnit(@RequestBody List<MsdsUnitVO> reqVo)
            throws Exception {
        BaseVO rtnDto = msdsUnitService.insertMsdsUnit(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
//
    @Operation(summary = "[MSDS 수정]", description = "process_id 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/safewizpro/planning/updateMsdsUnit/{unitId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateMsds(
            @RequestPart(value = "info")  MsdsUnitVO reqVO) throws Exception {

        BaseVO rtnDto = msdsUnitService.updateMsdsUnit(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
//
    @Operation(summary = "[MSDS 삭제]", description = "process_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteMsdsUnit/{unitId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMsds(@RequestBody List<MsdsUnitVO> reqVo)
            throws Exception {
    	for(MsdsUnitVO vo : reqVo) {
    		msdsUnitService.deleteMsdsUnit(vo);
    	}

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", reqVo);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
//
//    @Operation(summary = "[MSDS 일괄 삭제]", description = "process_id list에 해당되는 조직을 삭제 한다.")
//    @DeleteMapping(value = "/safewizpro/planning/deleteMsdss")
//    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMsdss(
//            @RequestBody List<MsdsVO> list)
//            throws Exception {
//        msdsService.deleteMsdss(list);
//
//        HashMap<String, Object> result = new HashMap<String, Object>();
//        result.put("result", null);
//        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
//                messageService.getMessage("deleteSuccess.msg"), result);
//    }
//
//    @Operation(summary = "화학물질목록", description = "안전보건공단에서 제공하는 화학물질 MSDS(물질안전보건자료)의 목록을 제공")
//    @PostMapping(value="/safewizpro/planning/getChemData")
//    public ResponseEntity<SingleResponseDto<String>> getChemData(@RequestBody SpSearchVO searchVo) throws Exception {
//        String chemData = msdsService.getChemData(searchVo);
//        if (chemData == null || chemData.isEmpty()) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
//                    messageService.getMessage("noResultFound.msg"), chemData);
//        }else if(chemData.equals("error")) {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, false, messageService.getMessage("eMsdsApiError.code"),
//                    messageService.getMessage("eMsdsApiError.msg"), chemData);
//        } else {
//            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
//                    messageService.getMessage("readSuccess.msg"), chemData);
//        }
//    }
//    
//    @PostMapping(value="/safewizpro/planning/getChemList")
//	@Operation(summary = "화학물질목록", description = "안전보건공단에서 제공하는 화학물질 MSDS(물질안전보건자료)의 목록을 제공")
//	public ResponseEntity<SingleResponseDto<ChemSearchVO>> getChemList(@RequestBody SpSearchVO dto) throws Exception {
//    	ChemSearchVO res = msdsService.getChemList(dto);
//		
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
//	}
//  
//	 @Operation(summary = "[msds 관리대장 출력]", description = "시정조치 요구서+ 레포트를 출력한다.")
//	    @PostMapping(value = "/safewizpro/planning/getMsdsReport")
//	    public void getMsdsReport(
//	            HttpServletRequest request,
//	            HttpServletResponse response,
//	            @RequestBody SpSearchVO spSearchVO)
//	            throws Exception {
//		 		msdsService.getMsdsReport(request, response, spSearchVO);
//	    }

}
