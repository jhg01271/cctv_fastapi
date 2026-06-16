package kr.co.igns.system.setting.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import kr.co.igns.system.setting.service.CompService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Comp
 * ※ 기능명 : 사업장 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 26.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Comp")
@RestController
@RequiredArgsConstructor
public class CompController {
    private final MessageService messageService;
    private final CompService compService;

    @Operation(summary = "[사업장 조회]", description = "사업장을 조회한다")
    @GetMapping(value = "/system/setting/comp/getComp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getComp(@ModelAttribute @Valid SpSearchVO searchVo)
            throws Exception {

        searchVo.initialize();
        int totalCount = compService.searchCount(searchVo);
        List<CompVO> list = compService.getComp(searchVo);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[고객에 매핑된 사업장 조회]", description = "해당 고객에 매핑된 사업장을 조회한다")
    @GetMapping(value = "/system/setting/comp/getCompByClnt")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompByClnt(@ModelAttribute @Valid SpSearchVO searchVo)
            throws Exception {

        List<CompVO> list = compService.getCompByClnt(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[유저에 매핑된 사업장 조회]", description = "해당 고객에 매핑된 사업장을 조회한다")
    @GetMapping(value = "/system/setting/comp/getCompListByUserId")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompListByUserId(@ModelAttribute @Valid SpSearchVO searchVo)
            throws Exception {

        List<CompVO> list = compService.getCompListByUserId(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[인원에 매핑된 사업장 조회]", description = "해당 고객에 매핑된 사업장을 조회한다")
    @GetMapping(value = "/system/setting/comp/getCompByHr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompByHr(@ModelAttribute @Valid SpSearchVO searchVo)
            throws Exception {

        List<CompVO> list = compService.getCompByHr(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[사업장 상세 조회]", description = "사업장 상세정보를 조회한다")
    @GetMapping(value = "/system/setting/comp/getCompDetail/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompDetail(
            @PathVariable String compId) throws Exception {
        CompVO list = compService.getCompDetail(compId);

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

    @Operation(summary = "[사업장 등록]", description = "사업장을 등록한다.")
    @PostMapping(value = "/system/setting/comp/insertComp", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertComp(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                 @RequestPart(value = "info") CompVO reqVo)
            throws Exception {
        BaseVO rtnDto = compService.insertComp(files,reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[사업장 수정]", description = "comp_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/setting/comp/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateComp(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                 @RequestPart(value = "info") CompVO reqVO,
                                                                                 @PathVariable String compId) throws Exception {
        BaseVO rtnDto = compService.updateComp(files,reqVO,compId);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[사업장 삭제]", description = "comp_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/comp/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteComp(@RequestBody @Valid CompVO reqVO)
            throws Exception {
        BaseVO rtnDto = compService.deleteComp(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[사업장 일괄 삭제]", description = "comp_id list에 해당되는 고객사를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/comp/deleteComps")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteComps(@RequestBody List<CompVO> list)
            throws Exception {
        compService.deleteComps(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
}
