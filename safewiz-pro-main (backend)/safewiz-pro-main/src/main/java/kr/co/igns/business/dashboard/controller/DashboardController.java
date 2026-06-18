package kr.co.igns.business.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.dashboard.model.ExternalDashboardVO;
import kr.co.igns.business.dashboard.model.DashboardVO;
import kr.co.igns.business.dashboard.service.DashboardService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Dashboard
 * ※ 기능명 : 대시보드
 * ※ 작성자 : 강지원, 박주형
 * ※ 최초생성일 : 2024. 12. 03.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final MessageService messageService;
    private final DashboardService dashboardService;

    // 대시보드 조회
    @Operation(summary = "[대시보드 조회]", description = "대시보드 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/dashboard/getDashboard")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDashboard(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        DashboardVO list = dashboardService.getDashboard(searchVo);

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

    // 청주산단 통합관제 모니터링 대시보드 연동 API
    @Operation(summary = "[사업장 목록 조회]", description = "사업장 목록 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/getComp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompDashboard() throws Exception {
        List<ExternalDashboardVO.comp> list = dashboardService.getCompDashboard();

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

    @Operation(summary = "[특정 사업장 대시보드 데이터 조회]", description = "특정 사업장 대시보드 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/getDashboard")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDashboardExternal(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        if(searchVo.getWriteYear() == null || searchVo.getWriteYear().isEmpty() || searchVo.getCompId() == null || searchVo.getCompId().isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("400"),
                    messageService.getMessage("잘못된 요청입니다. 파라미터를 확인하세요."), null);
        }
        // 유효 작성년도 체크
        boolean isValidYear = dashboardService.isValidYear(searchVo.getWriteYear());
        if(!isValidYear) {
            return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("400"),
                    messageService.getMessage("유효하지 않은 연도입니다. writeYear를 확인하세요."), null);
        }

        // 유효 사업장 체크
        boolean isValidComp = dashboardService.isValidComp(searchVo.getCompId());
        if(!isValidComp) {
            return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("400"),
                    messageService.getMessage("존재하지 않는 사업장입니다. compId를 확인하세요."), null);
        }
        ExternalDashboardVO.dashboard data = dashboardService.getDashboardExternal(searchVo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", data);

        if (data == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
}
