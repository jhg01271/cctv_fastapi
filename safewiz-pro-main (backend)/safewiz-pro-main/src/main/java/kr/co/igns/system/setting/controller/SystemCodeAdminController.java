package kr.co.igns.system.setting.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyResponseTrainingVO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.setting.service.SystemCodeService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "System Code Admin")
@RestController
@RequiredArgsConstructor
public class SystemCodeAdminController {
    private final MessageService messageService;
    private final SystemCodeService systemCodeService;

    @Operation(summary = "[디테일 조회]", description = "디테일 코드를 조회한다")
    @GetMapping(value = "/system/setting/systemCodeAdmin/{majorCd}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findDetail(@ModelAttribute @Valid SystemCodeVO vo) throws Exception {

        List<SystemMinorCodeVO> list = systemCodeService.findDetail(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }


    @Operation(summary = "[디테일 조회(page)]", description = "디테일 코드를 조회한다")
    @GetMapping(value = "/system/setting/systemCodeAdmin/getSystemCodeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findDetail(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        int totalCount = systemCodeService.searchCountDetail(searchVo);
        List<SystemMinorCodeVO> list = systemCodeService.findDetailPage(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
    // @Operation(summary = "[수정]", description = "id를 이용해 한건의 정보를 수정 한다.")
    // @PutMapping(value = "/admin/base/system/systemCode/{codeSeq}")
    // public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> modify(@RequestBody @Valid SystemCodeVO reqVo, @PathVariable Long codeSeq) throws Exception {
    // 	reqVo.setCodeSeq(codeSeq);
    // 	BaseVO rtnDto = systemCodeService.modify(reqVo);

    // 	HashMap<String, Object> result = new HashMap<String, Object>();
    // 	result.put("result", rtnDto);
    // 	return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    // }

    @Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/masterCode/{codeSeq}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> removeMaster(@RequestBody SystemCodeVO vo) throws Exception {

        String rtnDto = systemCodeService.removeMasterCode(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[삭제]", description = "id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/detailCode/{codeSeq}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> removeDetail(@RequestBody SystemMinorCodeVO vo) throws Exception {

        String rtnDto = systemCodeService.removeDetailCode(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[마스터 등록]", description = "입력 정보를 이용해 등록 한다.")
    @PostMapping(value = "/system/setting/systemCodeAdmin/upsertMasterCode")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> upsertMasterCode(@RequestBody @Valid SystemCodeVO reqVo) throws Exception {

        BaseVO rtnDto = systemCodeService.upsertMasterCode(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[디테일 등록]", description = "입력 정보를 이용해 등록 한다.")
    @PostMapping(value = "/system/setting/systemCodeAdmin/upsertDetailCode")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> upsertDetailCode(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") SystemMinorCodeVO reqVo)
            throws Exception {

        BaseVO rtnDto = systemCodeService.upsertDetailCode(files, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[커스텀 디테일 수정]", description = "입력 정보를 이용해 수정 한다.")
    @PostMapping(value = "/system/setting/systemCodeAdmin/updateEsgDetailCode")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateEsgDetailCode(@RequestBody @Valid List<SystemMinorCodeVO> reqVo) throws Exception {

        String rtnDto = systemCodeService.updateEsgDetailCode(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/system/setting/systemCodeAdmin/search")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        int totalCount = systemCodeService.searchCount(searchVo);
        List<SystemCodeVO> list = systemCodeService.search(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[커스텀 디테일 조회(esg 스키마)]", description = "커스텀 디테일 코드(esg 스키마)를 조회한다")
    @GetMapping(value = "/system/setting/systemCodeCustom/getSystemCodeDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findEsgCommonDetail(@ModelAttribute @Valid SystemCodeVO vo) throws Exception {

        List<SystemMinorCodeVO> list = systemCodeService.findEsgCommonDetail(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[커스텀 디테일 저장]", description = "입력 정보를 이용해 저장 한다.")
    @PostMapping(value = "/system/setting/systemCodeCustom/saveEsgDetailCode")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveEsgDetailCode(@RequestBody @Valid List<SystemMinorCodeVO> reqVo) throws Exception {

        BaseVO rtnDto = systemCodeService.saveEsgDetailCode(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[커스텀 디테일 삭제(완전 삭제)]", description = "id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/systemCodeCustom/deleteEsgDetailCode")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEsgDetailCode(@RequestBody List<SystemMinorCodeVO> reqVo) throws Exception {

        BaseVO rtnDto = systemCodeService.deleteEsgDetailCode(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }
}
