package kr.co.igns.system.base.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.PartnerVO;
import kr.co.igns.system.base.model.WpVO;
import kr.co.igns.system.base.service.PartnerService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * ※ 파일명 : Partner
 * ※ 기능명 : 협력사 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Partner")
@RestController
@RequiredArgsConstructor
public class PartnerController {
    private final MessageService messageService;
    private final PartnerService partnerService;

    @Operation(summary = "[협력사 조회]", description = "협력사를 조회한다")
    @GetMapping(value = "/system/base/partner/getPartner")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPartner(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<PartnerVO> list = partnerService.getPartner(searchVo);

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

    @Operation(summary = "[협력사 상세 조회]", description = "협력사 상세정보를 조회한다")
    @GetMapping(value = "/system/base/partner/getPartnerDetail/{partCompId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPartnerDetail(@PathVariable String partCompId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(partCompId);
        PartnerVO list = partnerService.getPartnerDetail(searchVo);

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

    @Operation(summary = "[협력사 등록]", description = "협력사를 등록한다.")
    @PostMapping(value = "/system/base/partner/insertPartner", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertPartner(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                    @NotNull @RequestPart(value = "files2", required = false) List<MultipartFile> files2,
                                                                                    @RequestPart(value = "info")  PartnerVO reqVo)
            throws Exception {
        BaseVO rtnDto = partnerService.insertPartner(files,files2, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[협력사 수정]", description = "part_comp_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/partner/{partCompId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updatePartner(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                    @NotNull @RequestPart(value = "files2", required = false) List<MultipartFile> files2,
                                                                                    @RequestPart(value = "info")  PartnerVO reqVO,
                                                                                    @PathVariable String partCompId) throws Exception {
        reqVO.setPartCompId(partCompId);
        BaseVO rtnDto = partnerService.updatePartner(files,files2, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[협력사 삭제]", description = "part_comp_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/partner/{partCompId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePartner(@PathVariable String partCompId)
            throws Exception {
        PartnerVO param = new PartnerVO();
        param.setPartCompId(partCompId);
        BaseVO rtnDto = partnerService.deletePartner(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[협력사 일괄 삭제]", description = "part_comp_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/partner/deletePartners")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePartners(
            @RequestBody List<PartnerVO> list)
            throws Exception {
        partnerService.deletePartners(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }


    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/partner/downloadPartnerExcelTemplate/{id}")
    public void downloadHrExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        partnerService.downloadPartnerExcelTemplate(request, response, id);
    }

    @Operation(summary = "[조직 등록 - 엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/partner/insertPartnerExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertHrExcel(
            @RequestBody List<PartnerVO> list)
            throws Exception {
        List<PartnerVO> orgnList = partnerService.insertPartnerExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", orgnList);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    // 거래품목 관리 팝업 관련 API
    @Operation(summary = "[거래품목 조회]", description = "거래품목 데이터셋을 조회한다")
    @GetMapping(value = "/system/base/partner/getPartCompItemDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getPartCompItemDataset(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<PartnerVO> list = partnerService.getPartCompItemDataset(searchVo);

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
    @Operation(summary = "[거래품목 예시 조회]", description = "거래품목 예시 데이터셋을 조회한다")
    @GetMapping(value = "/system/base/partner/getSamplePartCompItemDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getSamplePartCompItemDataset(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<PartnerVO> list = partnerService.getSamplePartCompItemDataset(searchVo);

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

    @Operation(summary = "[거래품목 저장]", description = "거래품목을 등록한다.")
    @PostMapping(value = "/system/base/partner/savePartCompItemDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> savePartCompItemDataset(
            @RequestBody List<PartnerVO> voList)
            throws Exception {
       partnerService.savePartCompItemDataset(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[거래품목 삭제]", description = "거래품목을 삭제 한다.")
    @DeleteMapping(value = "/system/base/partner/deletePartCompItemDataset")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deletePartCompItemDataset(
            @RequestBody List<PartnerVO> voList)
            throws Exception {
        partnerService.deletePartCompItemDataset(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
}
