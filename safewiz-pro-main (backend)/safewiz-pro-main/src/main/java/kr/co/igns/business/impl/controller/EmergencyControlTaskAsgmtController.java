package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyControlTaskAsgmtVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
import kr.co.igns.business.impl.service.EmergencyControlTaskAsgmtService;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
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
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : EmergencyControlTaskAsgmt
 * ※ 기능명 : 비상통제 조직별 업무분장
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 6.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "EmergencyControlTaskAsgmt", description = "비상통제 조직별 업무분장")
@RestController
@RequiredArgsConstructor
public class EmergencyControlTaskAsgmtController {
    private final MessageService messageService;
    private final EmergencyControlTaskAsgmtService emergencyControlTaskAsgmtService;

    @Operation(summary = "[비상통제 조직별 업무분장 조회]", description = "비상통제 조직도 업무분장 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyControlTaskAsgmtList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<EmergencyControlTaskAsgmtVO> list = emergencyControlTaskAsgmtService.getEmergencyControlTaskAsgmtList(spSearchVO);

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

    @Operation(summary = "[비상통제 조직별 업무분장 상세 조회]", description = "비상통제 조직별 업무분장 상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyControlTaskAsgmtDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        EmergencyControlTaskAsgmtVO list = emergencyControlTaskAsgmtService.getEmergencyControlTaskAsgmtDetail(spSearchVO);

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

    @Operation(summary = "[비상통제 조직별 업무분장 조직별 역할 조회]", description = "비상통제 조직별 업무분장 조직별 역할을 조회한다.")
    @GetMapping(value = "/safewizpro/impl/asgmt/getOrgnChartData")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrgnChartData(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        EmergencyControlTaskAsgmtVO list = emergencyControlTaskAsgmtService.getOrgnChartData(spSearchVO);

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

    @Operation(summary = "[비상통제 조직별 업무분장 저장]", description = "비상통제 조직별 업무분장 데이터를 저장한다.")
    @PostMapping(value = "/safewizpro/impl/asgmt/saveEmergencyControlTaskAsgmt")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEmergencyControlTaskAsgmt(
            @RequestBody @Valid EmergencyControlTaskAsgmtVO vo)
            throws Exception {
        BaseVO rtnDto = emergencyControlTaskAsgmtService.saveEmergencyControlTaskAsgmt(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직별 업무분장 삭제]", description = "비상통제 조직별 업무분장을 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/impl/asgmt/deleteEmergencyControlTaskAsgmt")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEmergencyControlTaskAsgmt(@RequestBody List<EmergencyControlTaskAsgmtVO> list) throws Exception {
        BaseVO rtnDto = emergencyControlTaskAsgmtService.deleteEmergencyControlTaskAsgmt(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[비상통제 조직별 업무분장 레포트 출력]", description = "비상통제 조직별 업무분장 레포트를 출력한다.")
    @PostMapping(value = "/safewizpro/impl/asgmt/getEmergencyControlTaskAsgmtReport")
    public void getEmergencyControlTaskAsgmtReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            emergencyControlTaskAsgmtService.getEmergencyControlTaskAsgmtReport(request, response, spSearchVO);
    }
}
