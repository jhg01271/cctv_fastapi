package kr.co.igns.business.planning.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.ChemDetailVO;
import kr.co.igns.business.planning.model.ChemSearchVO;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.business.planning.service.MsdsService;
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
@Tag(name = "Msds", description = "MSDS 관리")
@RestController
@RequiredArgsConstructor
public class MsdsController {
    private final MessageService messageService;
    private final MsdsService msdsService;

    @Operation(summary = "[MSDS 조회]", description = "MSDS를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getMsds")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsds(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<MsdsVO> list = msdsService.getMsds(searchVo);

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

    @Operation(summary = "[MSDS 상세 조회]", description = "MSDS 상세정보를 조회한다")
    @GetMapping(value = "/safewizpro/planning/getMsdsDetail/{msdsId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMsdsDetail(
            @PathVariable String msdsId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(msdsId);
        MsdsVO list = msdsService.getMsdsDetail(searchVo);

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

    @Operation(summary = "[MSDS 등록]", description = "MSDS를 등록한다.")
    @PostMapping(value = "/safewizpro/planning/insertMsds")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertMsds(@NotNull @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles, @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                 @RequestPart(value = "info") MsdsVO reqVo)
            throws Exception {
        BaseVO rtnDto = msdsService.insertMsds(imageFiles, files, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[MSDS 수정]", description = "process_id 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/safewizpro/planning/updateMsds/{msdsId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateMsds(
			@NotNull @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles, @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info")  MsdsVO reqVO,
            @PathVariable String msdsId) throws Exception {
        reqVO.setWorkplaceId(msdsId);
        BaseVO rtnDto = msdsService.updateMsds(imageFiles, files, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[MSDS 삭제]", description = "process_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteMsds/{msdsId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMsds(@PathVariable String msdsId)
            throws Exception {
        MsdsVO param = new MsdsVO();
        param.setMsdsId(msdsId);
        BaseVO rtnDto = msdsService.deleteMsds(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[MSDS 일괄 삭제]", description = "process_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/planning/deleteMsdss")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteMsdss(
            @RequestBody List<MsdsVO> list)
            throws Exception {
        msdsService.deleteMsdss(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "화학물질목록", description = "안전보건공단에서 제공하는 화학물질 MSDS(물질안전보건자료)의 목록을 제공")
    @PostMapping(value="/safewizpro/planning/getChemData")
    public ResponseEntity<SingleResponseDto<String>> getChemData(@RequestBody SpSearchVO searchVo) throws Exception {
        String chemData = msdsService.getChemData(searchVo);
        if (chemData == null || chemData.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), chemData);
        }else if(chemData.equals("error")) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, false, messageService.getMessage("eMsdsApiError.code"),
                    messageService.getMessage("eMsdsApiError.msg"), chemData);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), chemData);
        }
    }
    
    @PostMapping(value="/safewizpro/planning/getChemList")
	@Operation(summary = "화학물질목록", description = "안전보건공단에서 제공하는 화학물질 MSDS(물질안전보건자료)의 목록을 제공")
	public ResponseEntity<SingleResponseDto<ChemSearchVO>> getChemList(@RequestBody SpSearchVO dto) throws Exception {
    	ChemSearchVO res = msdsService.getChemList(dto);
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
    
    @PostMapping(value = "/safewizpro/planning/getChemDetail01")
	@Operation(summary = "1. 화학제품과 회사에 관한 정보", description = "‘화학제품과 회사에 관한 정보’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail01(@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail01");
		HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("data", res);
		
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail02")
	@Operation(summary = "2. 유해성·위험성", description = "‘유해성·위험성’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail02(@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail02");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail03")
	@Operation(summary = "3. 구성성분의 명칭 및 함유량", description = "‘구성성분의 명칭 및 함유량’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail03(@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail03");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail04")
	@Operation(summary = "4. 응급조치요령", description = "‘응급조치요령’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail04( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail04");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail05")
	@Operation(summary = "5. 폭발·화재시 대처방법", description = "‘폭발·화재시 대처방법’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail05( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail05");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail06")
	@Operation(summary = "6. 누출사고시 대처방법", description = "누출사고시 대처방법’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail06( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail06");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail07")
	@Operation(summary = "7. 취급 및 저장방법", description = "취급 및 저장방법’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail07( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail07");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail08")
	@Operation(summary = "8. 노출방지 및 개인보호구", description = "‘노출방지 및 개인보호구’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail08( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail08");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail09")
	@Operation(summary = "9. 물리화학적 특성", description = "‘물리화학적 특성’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail09( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail09");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail10")
	@Operation(summary = "10. 안정성 및 반응성", description = "‘안정성 및 반응성’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail10( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail10");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail11")
	@Operation(summary = "11. 독성에 관한 정보", description = "‘독성에 관한 정보’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail11( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail11");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail12")
	@Operation(summary = "12. 환경에 미치는 영향", description = "환경에 미치는 영향’항목 정보를 제공")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail12( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail12");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail13")
	@Operation(summary = "13. 폐기시 주의사항", description = "‘폐기시 주의사항’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail13( @RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail13");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail14")
	@Operation(summary = "14. 운송에 필요한 정보", description = "‘운송에 필요한 정보’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail14(@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail14");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail15")
	@Operation(summary = "15. 법적 규제현황", description = "‘법적 규제현황’항목 정보를 제공  ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail15(@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail15");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	@PostMapping(value = "/safewizpro/planning/getChemDetail16")
	@Operation(summary = "16. 그 밖의 참고사항", description = "‘그 밖의 참고사항’항목 정보를 제공 ")
	public ResponseEntity<SingleResponseDto<ChemDetailVO>> getChemDetail16 (@RequestBody SpSearchVO dto) throws Exception {
		
		ChemDetailVO res = msdsService.getChemDetail(dto, "/chemdetail16");
		
		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), res);
	}
	
	 @Operation(summary = "[msds 관리대장 출력]", description = "시정조치 요구서+ 레포트를 출력한다.")
	    @PostMapping(value = "/safewizpro/planning/getMsdsReport")
	    public void getMsdsReport(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            @RequestBody SpSearchVO spSearchVO)
	            throws Exception {
		 		msdsService.getMsdsReport(request, response, spSearchVO);
	    }

}
