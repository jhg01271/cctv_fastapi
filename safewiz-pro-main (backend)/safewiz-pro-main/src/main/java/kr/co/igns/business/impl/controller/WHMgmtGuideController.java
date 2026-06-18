package kr.co.igns.business.impl.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.WHMgmtGuideVO;
import kr.co.igns.business.impl.service.WHMgmtGuideService;
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
 * ※ 패키지 : ;
 * ※ 파일명 : Worker Health Management Guidelines
 * ※ 기능명 : 근로자 건강관리 지침
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 31.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "근로자 건강관리 지침")
@RestController
@RequiredArgsConstructor
public class WHMgmtGuideController {
    private final MessageService messageService;
    private final WHMgmtGuideService wHMgmtGuideService;

    @Operation(summary = "[근로자 건강관리 지침 조회]", description = "근로자 건강관리 지침을 조회한다")
    @GetMapping(value = "/safewizpro/impl/wHMGuide/getWHMgmtGuide")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWHMgmtGuide(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<WHMgmtGuideVO> list = wHMgmtGuideService.getWHMgmtGuide(searchVo);

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

    @Operation(summary = "[근로자 건강관리 지침 상세 조회]", description = "근로자 건강관리 지침 상세 조회한다")
    @GetMapping(value = "/safewizpro/impl/wHMGuide/getWHMgmtGuideDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWHMgmtGuideDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        WHMgmtGuideVO list = wHMgmtGuideService.getWHMgmtGuideDetail(searchVo);

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

    @Operation(summary = "[근로자 건강관리 지침 저장]", description = "근로자 건강관리 지침을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/wHMGuide/saveWHMgmtGuide")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveWHMgmtGuide(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") WHMgmtGuideVO voList)
            throws Exception {
        BaseVO rtnDto = wHMgmtGuideService.saveWHMgmtGuide(files, voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[근로자 건강관리 지침 저장 (불러오기) 유효성 검사]", description = "근로자 건강관리 지침을 저장 시 유효성을 검사한다.")
    @GetMapping(value = "/safewizpro/impl/wHMGuide/validateGuideByOrgn")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> validateGuideByOrgn(@ModelAttribute @Valid WHMgmtGuideVO vo) throws Exception {
        List<WHMgmtGuideVO> resultList = wHMgmtGuideService.validateGuideByOrgn(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", resultList);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[근로자 건강관리 지침 저장 (불러오기)]", description = "근로자 건강관리 지침을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/wHMGuide/saveWHMgmtGuideByOrgn")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveWHMgmtGuideByOrgn(@RequestBody List<WHMgmtGuideVO> voList) throws Exception {
        wHMgmtGuideService.saveWHMgmtGuideByOrgn(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
    }

    @Operation(summary = "[근로자 건강관리 지침 삭제]", description = "근로자 건강관리 지침을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/wHMGuide/deleteWHMgmtGuide")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWHMgmtGuide(@RequestBody List<WHMgmtGuideVO> list) throws Exception {
        wHMgmtGuideService.deleteWHMgmtGuide(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[근로자 건강관리 지침 상세 삭제]", description = "근로자 건강관리 지침 상세를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/wHMGuide/deleteWHMgmtGuideDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWHMgmtGuideDetail(@RequestBody WHMgmtGuideVO list) throws Exception {
        wHMgmtGuideService.deleteWHMgmtGuideDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[근로자 건강 상담일지 보고서]", description = "근로자 건강 상담일지 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/wHMGuide/getWHMgmtGuideReport")
    public void getWHMgmtGuideReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        wHMgmtGuideService.getWHMgmtGuideReport(request, response, spSearchVO);
    }

    @Operation(summary = "[근로자 보건 및 작업환경 개선요청 상담일지 보고서]", description = "근로자 건강관리 지침 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/wHMGuide/getWHMgmtGuideRequestReport")
    public void getTrainingResultReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        wHMgmtGuideService.getWHMgmtGuideRequestReport(request, response, spSearchVO);
    }
}
