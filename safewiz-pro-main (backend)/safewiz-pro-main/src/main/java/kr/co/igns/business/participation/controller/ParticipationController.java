package kr.co.igns.business.participation.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.participation.model.ActPlanVO;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.business.participation.model.ParticipationVo;
import kr.co.igns.business.participation.service.ParticipationService;
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
import org.springframework.lang.Nullable;
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
@Tag(name = "참여", description = "MSDS 관리")
@RestController
@RequiredArgsConstructor
public class ParticipationController {
    private final MessageService messageService;
    private final ParticipationService participationService;

    @Operation(summary = "[조회]", description = "협의 및 참여 데이터를 조회한다")
    @GetMapping(value = "/safewizpro/participation/getCommittee/{type}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyList(
            @ModelAttribute @Valid SpSearchVO searchVo, @PathVariable String type) throws Exception {
       searchVo.setSearchCd(type);
                List<ParticipationVo> list = participationService.searchList(searchVo);

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
    @Operation(summary = "[상세 조회]", description = "협의 및 참여 상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/getCommitteeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCommitteeDetail(
         @ModelAttribute @Valid SpSearchVO reqVo) throws Exception {
        ParticipationVo list = participationService.search(reqVo);

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

    @Operation(summary = "[등록]", description = "협의 및 참여 데이터를 등록한다.")
    @PostMapping(value = "/safewizpro/participation/insertCommittee")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertCommittee(@RequestBody @Valid ParticipationVo reqVo) throws Exception {
        BaseVO rtnDto = participationService.create(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[삭제]", description = "협의 및 참여 데이터를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/participation/deleteCommittee")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCommittee(@RequestBody @Valid List<ParticipationVo> reqVo) throws Exception {
        BaseVO rtnDto = participationService.delete(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[부서 상황 출력(특정년도, 특정조직 전체)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/participation/getCommitteeReport")
	public void getReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
        participationService.getReport(request, response, vo);
    }

    @Operation(summary = "[산업안전보건위원회 출력(특정년도)]", description = "출력물 다운로드")
	@PostMapping(value = "/safewizpro/participation/getOhsCommitteeReport")
	public void getOhsCommitteeReport(HttpServletRequest request, HttpServletResponse response, @Nullable @RequestBody SpSearchVO vo) throws Exception {
        participationService.getOhsCommitteeReport(request, response, vo);
    }
    
}
