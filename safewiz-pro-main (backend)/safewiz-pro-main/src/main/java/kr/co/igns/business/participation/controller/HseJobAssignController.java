package kr.co.igns.business.participation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.HseJobAssignVO;
import kr.co.igns.business.participation.service.HseJobAssignService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
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
 * ※ 파일명 : job_assignment
 * ※ 기능명 : HSE 업무분장
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 18.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "HSE 업무분장")
@RestController
@RequiredArgsConstructor
public class HseJobAssignController {
    private final MessageService messageService;
    private final HseJobAssignService hseJobAssignService;

    @Operation(summary = "[HSE 업무분장 조회]", description = "HSE 업무분장을 조회한다")
    @GetMapping(value = "/safewizpro/participation/JobAssign/getJobAssign")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJobAssign(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HseJobAssignVO> list = hseJobAssignService.getJobAssign(searchVo);

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

    @Operation(summary = "[HSE 업무분장 상세 조회]", description = "HSE 업무분장을 조회한다")
    @GetMapping(value = "/safewizpro/participation/JobAssign/getJobAssignDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getJobAssignDetail(@ModelAttribute SpSearchVO searchVo) throws Exception {
        HseJobAssignVO list = hseJobAssignService.getJobAssignDetail(searchVo);

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

    @Operation(summary = "[MY HSE 업무분장 조회]", description = "MY HSE 업무분장을 조회한다")
    @GetMapping(value = "/safewizpro/participation/JobAssign/getMyJobAssign")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getMyJobAssign(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HseJobAssignVO> list = hseJobAssignService.getMyJobAssign(searchVo);

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

    @Operation(summary = "[HSE 업무분장 저장]", description = "HSE 업무분장을 저장한다.")
    @PostMapping(value = "/safewizpro/participation/JobAssign/saveJobAssign")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveJobAssign(
            @RequestBody List<HseJobAssignVO> voList)
            throws Exception {
        BaseVO rtnDto = hseJobAssignService.saveJobAssign(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[HSE 업무분장 삭제]", description = "job_assign_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/JobAssign/deleteJobAssign")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobAssign(@RequestBody HseJobAssignVO reqVo) throws Exception {
        BaseVO rtnDto = hseJobAssignService.deleteJobAssign(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[HSE 업무분장 일괄 삭제]", description = "job_assign_id list에 해당되는 사용자를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/participation/JobAssign/deleteJobAssigns")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteJobAssigns(@RequestBody List<HseJobAssignVO> list) throws Exception {
        hseJobAssignService.deleteJobAssigns(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

}
