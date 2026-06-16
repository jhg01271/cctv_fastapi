package kr.co.igns.business.participation.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.participation.model.HsePolicySuggestionVO;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.business.participation.service.HsePolicyService;
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
 * ※ 패키지 : kr.co.igns.business.participation.controller
 * ※ 파일명 : HsePolicy
 * ※ 기능명 : 안전보건경영방침
 * ※ 작성자 : 이지훈
 * ※ 최초생성일 : 2024. 10. 14.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "HsePolicy", description = "안전보건경영방침 관리")
@RestController
@RequiredArgsConstructor
public class HsePolicyController {
    private final MessageService messageService;
    private final HsePolicyService hsePolicyService;

    @Operation(summary = "[안전보건경영방침 확정항목 조회]", description = "안전보건경영방침을 조회한다")
    @GetMapping(value = "/safewizpro/participation/getConfirmedHsePolicyList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getConfirmedHsePolicyList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HsePolicyVO> list = hsePolicyService.getConfirmedHsePolicyList(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        if (list.size() > 0) {
            result.put("list", list);
        } else {
            result.put("list", null);
        }

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[안전보건경영방침 조회]", description = "안전보건경영방침을 조회한다")
    @GetMapping(value = "/safewizpro/participation/getHsePolicyList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HsePolicyVO> list = hsePolicyService.getHsePolicyList(searchVo);

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

    @Operation(summary = "[안전보건경영방침 상세 조회]", description = "안전보건경영방침 상세 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/getHsePolicyDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicyDetail(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        HsePolicyVO list = hsePolicyService.getHsePolicyDetail(spSearchVO);

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

    @Operation(summary = "[안전보건경영방침 예시 조회]", description = "안전보건경영방침 예시데이터를 조회한다")
    @GetMapping(value = "/safewizpro/participation/getSampleHsePolicy")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSampleHsePolicy(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        HsePolicyVO list = hsePolicyService.getSampleHsePolicy(searchVo);

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

    @Operation(summary = "[안전보건경영방침 저장]", description = "안전보건경영방침 데이터를 저장한다")
    @PostMapping(value = "/safewizpro/participation/saveHsePolicy")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHsePolicy(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") HsePolicyVO reqVO) throws Exception {
        BaseVO rtnDto = hsePolicyService.saveHsePolicy(files, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건경영방침 추가파일 저장]", description = "안전보건경영방침 추가파일 데이터를 저장한다")
    @PostMapping(value = "/safewizpro/participation/saveHsePolicyFiles")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHsePolicyFiles(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") HsePolicyVO reqVO) throws Exception {
        BaseVO rtnDto = hsePolicyService.saveHsePolicyFiles(files, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건경영방침 확정여부 수정]", description = "안전보건경영방침 확정여부를 저장한다")
    @PostMapping(value = "/safewizpro/participation/updateHsePolicyConfirmYn")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHsePolicyConfirmYn(
            @RequestBody HsePolicyVO reqVO) throws Exception {
        BaseVO rtnDto = hsePolicyService.updateHsePolicyConfirmYn(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }


    @Operation(summary = "[안전보건경영방침 삭제]", description = "안전보건경영방침 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/participation/deleteHsePolicy")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHsePolicy(
            @RequestBody List<HsePolicyVO> list
    ) throws Exception {
        hsePolicyService.deleteHsePolicy(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건경영방침 레포트 ]", description = "안전보건경영방침 근로자 의견 레포트를 제작한다.")
    @GetMapping(value = "/safewizpro/participation/getHsePolicyReport")
    public void getHsePolicyReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute @Valid SpSearchVO spSearchVO)
            throws Exception {
        hsePolicyService.getHsePolicyReport(request, response, spSearchVO);
    }

    // 근로자 의견 관련 API
    @Operation(summary = "[안전보건경영방침 근로자 의견 조회]", description = "안전보건경영방침 근로자 의견 데이터를 조회한다.")
    @GetMapping(value = "/safewizpro/participation/suggestion/getHsePolicySug")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHsePolicySug(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<HsePolicySuggestionVO> list = hsePolicyService.getHsePolicySug(spSearchVO);

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

    @Operation(summary = "[안전보건경영방침 근로자 의견 저장]", description = "안전보건경영방침 근로자 의견 데이터를 저장(수정)한다.")
    @PostMapping(value = "/safewizpro/participation/suggestion/saveHsePolicySug")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHsePolicySug(
            @RequestBody List<HsePolicySuggestionVO> list)
            throws Exception {
        BaseVO rtnDto = hsePolicyService.saveHsePolicySug(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[안전보건경영방침 근로자 의견 삭제]", description = "안전보건경영방침 근로자 의견 데이터를 삭제한다.")
    @DeleteMapping(value = "/safewizpro/participation/suggestion/deleteHsePolicySug")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHsePolicySug(
            @RequestBody List<HsePolicySuggestionVO> list)
            throws Exception {
        BaseVO rtnDto = hsePolicyService.deleteHsePolicySug(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

}
