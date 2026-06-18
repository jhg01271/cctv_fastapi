package kr.co.igns.business.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.ManageDocVO;
import kr.co.igns.business.support.service.ManageDocService;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Manager Document
 * ※ 기능명 : 문서 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 10. 31.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "문서 관리")
@RestController
@RequiredArgsConstructor
public class ManageDocController {
    private final MessageService messageService;
    private final ManageDocService manageDocService;

    @Operation(summary = "[문서 조회]", description = "문서를 조회한다")
    @GetMapping(value = "/safewizpro/support/manageDoc/getManageDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getManageDoc(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ManageDocVO> list = manageDocService.getManageDoc(searchVo);

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

    @Operation(summary = "[문서 상세 조회]", description = "문서 상세 조회한다")
    @GetMapping(value = "/safewizpro/support/manageDoc/getManageDocDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getManageDocDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<ManageDocVO> list = manageDocService.getManageDocDetail(searchVo);

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

    @Operation(summary = "[문서 저장]", description = "문서를 저장한다.")
    @PostMapping(value = "/safewizpro/support/manageDoc/saveManageDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveManageDoc(
            @RequestPart(value = "files", required = false)  List<MultipartFile> files,
            @RequestPart(value = "fileKeys") List<String> fileKeys,
            @RequestPart(value = "info") List<ManageDocVO> voList)
            throws Exception {

        // 파일 키에 따라 List<MultipartFile>을 Map으로 수동 매핑
        Map<String, List<MultipartFile>> filesMap = new HashMap<>();
        if(files != null) {
            for (int i = 0; i < files.size(); i++) {
                String key = fileKeys.get(i);
                filesMap.computeIfAbsent(key, k -> new ArrayList<>()).add(files.get(i));
            }
        }
        BaseVO rtnDto = manageDocService.saveManageDoc(filesMap,voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[문서 삭제]", description = "문서를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/manageDoc/deleteManageDoc")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteManageDoc(@RequestBody ManageDocVO reqVo) throws Exception {
        BaseVO rtnDto = manageDocService.deleteManageDoc(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[문서 일괄 삭제]", description = "문서 list를 삭제 한다.")
    @DeleteMapping(value = "/safewizpro/support/manageDoc/deleteManageDocs")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteManageDocs(@RequestBody List<ManageDocVO> list) throws Exception {
        manageDocService.deleteManageDocs(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

}
