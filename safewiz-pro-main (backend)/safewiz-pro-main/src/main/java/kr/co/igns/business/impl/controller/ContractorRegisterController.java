package kr.co.igns.business.impl.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.impl.service.ContractorInvestigationFormService;
import kr.co.igns.business.impl.service.ContractorRegisterService;
import kr.co.igns.business.impl.service.EmergencyControlOrganChartService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : ContractorRegister
 * ※ 기능명 : 협력업체 등록대장
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 11. 28.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "ContractorRegister", description = "협력업체 등록대장")
@RestController
@RequiredArgsConstructor
public class ContractorRegisterController {
    private final MessageService messageService;
    private final ContractorRegisterService contractorRegisterService;

    @Operation(summary = "[협력업체 등록대장 조회]", description = "협력업체 등록대장 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/register/getConstractorRegisterList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getConstractorRegisterList(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<ContractorRegisterVO> list = contractorRegisterService.getConstractorRegisterList(spSearchVO);

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

    @Operation(summary = "[협력업체 등록대장 출력]", description = "협력업체 등록대장을 출력한다.")
    @PostMapping(value = "/safewizpro/impl/register/getConstractorRegisterReport")
    public void getConstractorRegisterReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SpSearchVO spSearchVO)
            throws Exception {
            contractorRegisterService.getConstractorRegisterReport(request, response, spSearchVO);
    }
}
