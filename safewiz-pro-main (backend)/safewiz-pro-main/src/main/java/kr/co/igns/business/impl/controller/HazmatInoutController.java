package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.DatasetHazmatChecklistVO;
import kr.co.igns.business.impl.model.HazmatChecklistVO;
import kr.co.igns.business.impl.model.HazmatInoutDetailVO;
import kr.co.igns.business.impl.model.HazmatInoutVO;
import kr.co.igns.business.impl.service.HazmatChecklistService;
import kr.co.igns.business.impl.service.HazmatInoutService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Hazmat Inout
 * ※ 기능명 : 작업장 위험물/유해화학물질 입출고
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 11. 08.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "작업장 위험물/유해화학물질 입출고")
@RestController
@RequiredArgsConstructor
public class HazmatInoutController {
    private final MessageService messageService;
    private final HazmatInoutService hazmatInoutService;

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 조회]", description = "작업장 위험물/유해화학물질 입출고 건수를 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatInout/getHazmatInout")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazmatInout(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HazmatInoutVO> list = hazmatInoutService.getHazmatInout(searchVo);

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

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 내역 조회]", description = "작업장 위험물/유해화학물질 입출고 내역을 조회한다")
    @GetMapping(value = "/safewizpro/impl/hazmatInout/getHazmatInoutDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHazmatInoutDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        HazmatInoutVO list = hazmatInoutService.getHazmatInoutDetail(searchVo);

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

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 저장]", description = "작업장 위험물/유해화학물질 입출고 내역을 저장한다.")
    @PostMapping(value = "/safewizpro/impl/hazmatInout/saveHazmatInout")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHazmatInout(
            @RequestBody HazmatInoutVO voList)
            throws Exception {
        BaseVO rtnDto = hazmatInoutService.saveHazmatInout(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 삭제]", description = "작업장 위험물/유해화학물질 입출고 내역을 삭제한다.")
    @DeleteMapping(value = "/safewizpro/impl/hazmatInout/deleteHazmatInout")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHazmatInout(@RequestBody List<HazmatInoutVO> list) throws Exception {
        hazmatInoutService.deleteHazmatInout(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 내역 삭제]", description = "작업장 위험물/유해화학물질 입출고 내역을 삭제한다.")
    @DeleteMapping(value = "/safewizpro/impl/hazmatInout/deleteHazmatInoutDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHazmatInoutDetail(@RequestBody List<HazmatInoutDetailVO> list) throws Exception {
        hazmatInoutService.deleteHazmatInoutDetail(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 위험물/유해화학물질 입출고 보고서]", description = "작업장 위험물/유해화학물질 입출고 보고서를 제작한다.")
    @PostMapping(value = "/safewizpro/impl/hazmatInout/getHazmatInoutReport")
    public void getHazmatInoutReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
        spSearchVO.initialize();
        hazmatInoutService.getHazmatInoutReport(request, response, spSearchVO);
    }
}
