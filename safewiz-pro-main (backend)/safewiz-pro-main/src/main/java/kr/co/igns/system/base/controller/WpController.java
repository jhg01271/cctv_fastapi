package kr.co.igns.system.base.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.PrcsVO;
import kr.co.igns.system.base.model.WpVO;
import kr.co.igns.system.base.service.WpService;
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
 * ※ 파일명 : workplace
 * ※ 기능명 : 작업장
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Wp")
@RestController
@RequiredArgsConstructor
public class WpController {
    private final MessageService messageService;
    private final WpService wpService;

    @Operation(summary = "[작업장 조회]", description = "작업장을 조회한다")
    @GetMapping(value = "/system/base/wp/getWp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWp(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<WpVO> list = wpService.getWp(searchVo);

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

    @Operation(summary = "[작업장 상세 조회]", description = "작업장 상세정보를 조회한다")
    @GetMapping(value = "/system/base/wp/getWpDetail/{workplaceId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getWpDetail(
            @PathVariable String workplaceId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(workplaceId);
        WpVO list = wpService.getWpDetail(searchVo);

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

    @Operation(summary = "[작업장 등록]", description = "작업장을 등록한다.")
    @PostMapping(value = "/system/base/wp/insertWp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertWp(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") WpVO reqVo)
            throws Exception {
        BaseVO rtnDto = wpService.insertWp(files, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 수정]", description = "workplace_id 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/wp/{workplaceId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateWp(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info")  WpVO reqVO,
            @PathVariable String workplaceId) throws Exception {
        reqVO.setWorkplaceId(workplaceId);
        BaseVO rtnDto = wpService.updateWp(files, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 삭제]", description = "workplace_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/wp/{workplaceId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWp(@PathVariable String workplaceId)
            throws Exception {
        WpVO param = new WpVO();
        param.setWorkplaceId(workplaceId);
        BaseVO rtnDto = wpService.deleteWp(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[작업장 일괄 삭제]", description = "workplace_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/wp/deleteWps")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteWps(
            @RequestBody List<WpVO> list)
            throws Exception {
        wpService.deleteWps(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/wp/downloadWpExcelTemplate/{id}")
    public void downloadWpExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        wpService.downloadWpExcelTemplate(request, response, id);
    }

    @Operation(summary = "[엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/wp/insertWpExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertWpExcel(
            @RequestBody List<WpVO> list)
            throws Exception {
        wpService.insertWpExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
}
