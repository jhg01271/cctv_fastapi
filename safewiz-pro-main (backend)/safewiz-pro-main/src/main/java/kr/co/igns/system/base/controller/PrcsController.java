package kr.co.igns.system.base.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.PrcsCnfmVO;
import kr.co.igns.system.base.model.PrcsVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import kr.co.igns.system.base.model.WpVO;
import kr.co.igns.system.base.service.PrcsService;
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
 * ※ 파일명 : process
 * ※ 기능명 : 공정
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Prcs")
@RestController
@RequiredArgsConstructor
public class PrcsController {
    private final MessageService messageService;
    private final PrcsService prcsService;

    @Operation(summary = "[공정 조회]", description = "공정을 조회한다")
    @GetMapping(value = "/system/base/prcs/getPrcs")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcs(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<PrcsVO> list = prcsService.getPrcs(searchVo);

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

    @Operation(summary = "[공정 상세 조회]", description = "공정 상세정보를 조회한다")
    @GetMapping(value = "/system/base/prcs/getPrcsDetail/{processId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcsDetail(
            @PathVariable String processId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(processId);
        PrcsVO list = prcsService.getPrcsDetail(searchVo);

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

    @Operation(summary = "[공정 등록]", description = "공정을 등록한다.")
    @PostMapping(value = "/system/base/prcs/insertPrcs")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertPrcs(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                 @RequestPart(value = "info") PrcsVO reqVo)
            throws Exception {
        BaseVO rtnDto = prcsService.insertPrcs(files,reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[공정 수정]", description = "process_id 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/prcs/{processId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updatePrcs(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") PrcsVO reqVO,
            @PathVariable String processId) throws Exception {
        reqVO.setProcessId(processId);
        BaseVO rtnDto = prcsService.updatePrcs(files,reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[공정 삭제]", description = "process_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/prcs/{processId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePrcs(@PathVariable String processId)
            throws Exception {
        PrcsVO param = new PrcsVO();
        param.setProcessId(processId);
        BaseVO rtnDto = prcsService.deletePrcs(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[공정 일괄 삭제]", description = "process_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/prcs/deletePrcss")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePrcss(
            @RequestBody List<PrcsVO> list)
            throws Exception {
        prcsService.deletePrcss(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    //region 공정별 작업 내용관련

    @Operation(summary = "[공정별 작업 내용 차수 리스트 조회]", description = "공정별 작업 내용 차수 리스트를 조회한다")
    @GetMapping(value = "/system/base/prcs/getPrcsRev/{processId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcsRev(
            @PathVariable String processId) throws Exception {
        List<PrcsCnfmVO> list = prcsService.getPrcsRevList(processId);

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

    @Operation(summary = "[공정별 확정 차수 작업 내용 조회]", description = "공정별 확정 차수 작업 내용을 조회한다")
    @GetMapping(value = "/system/base/prcs/getPrcsCnfmWork/{processId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcsCnfmWorkList(
            @PathVariable String processId) throws Exception {
        List<PrcsWorkVO> list = prcsService.getPrcsCnfmWorkList(processId);

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

    @Operation(summary = "[공정/차수별 작업 내용 리스트 조회]", description = "공정/차수별 작업 내용 리스트를 조회한다")
    @GetMapping(value = "/system/base/prcs/getPrcsWork")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPrcsWork(
            @ModelAttribute @Valid PrcsCnfmVO prcsCnfmVO) throws Exception {
        List<PrcsWorkVO> list = prcsService.getPrcsWorkList(prcsCnfmVO);

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

    @Operation(summary = "[공정별 차수 추가]", description = "공정별 차수를 추가한다.")
    @PostMapping(value = "/system/base/prcs/insertPrcsRev")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertPrcsRev(@RequestBody PrcsCnfmVO reqVo)
            throws Exception {
        BaseVO rtnDto = prcsService.insertPrcsRev(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[공정별 차수 수정]", description = "공정별 차수를 수정한다.")
    @PutMapping(value = "/system/base/prcs/updatePrcsRev")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updatePrcsRev(@RequestBody PrcsCnfmVO reqVo) throws Exception {
        BaseVO rtnDto = prcsService.updatePrcsRev(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[공정별 차수 확정/확정취소]", description = "공정별 차수를 확정/확정취소 한다.")
    @PutMapping(value = "/system/base/prcs/updatePrcsRevCnfm")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updatePrcsRevCnfm(@RequestBody PrcsCnfmVO reqVo) throws Exception {
        BaseVO rtnDto = prcsService.updatePrcsRevCnfm(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[공정/차수별 작업 내용 추가/수정]", description = "공정/차수별 작업 내용 추가/수정한다.")
    @PutMapping(value = "/system/base/prcs/updatePrcsWorks")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updatePrcsWorks(@RequestBody List<PrcsWorkVO> reqVo) throws Exception {
        List<PrcsWorkVO> rtnDto = prcsService.updatePrcsWorks(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/prcs/downloadPrcsExcelTemplate/{id}")
    public void downloadWpExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        prcsService.downloadPrcsExcelTemplate(request, response, id);
    }

    @Operation(summary = "[엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/prcs/insertPrcsExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertPrcsExcel(
            @RequestBody List<PrcsVO> list)
            throws Exception {
        prcsService.insertPrcsExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    //endregion
}
