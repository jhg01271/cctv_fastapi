package kr.co.igns.system.base.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.base.model.PrcsVO;
import kr.co.igns.system.base.service.EquipService;
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
 * ※ 파일명 : equipment
 * ※ 기능명 : 공정
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 24.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Equipment")
@RestController
@RequiredArgsConstructor
public class EquipController {
    private final MessageService messageService;
    private final EquipService equipService;

    @Operation(summary = "[설비 조회]", description = "설비를 조회한다")
    @GetMapping(value = "/system/base/equip/getEquip")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEquip(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EquipVO> list = equipService.getEquip(searchVo);

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

    @Operation(summary = "[설비 상세 조회]", description = "설비 상세정보를 조회한다")
    @GetMapping(value = "/system/base/equip/getEquipDetail/{equipmentId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEquipDetail(
            @PathVariable String equipmentId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setSearchText(equipmentId);
        EquipVO list = equipService.getEquipDetail(searchVo);

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

    @Operation(summary = "[설비 등록]", description = "설비를 등록한다.")
    @PostMapping(value = "/system/base/equip/insertEquip")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertEquip(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                  @RequestPart(value = "info") EquipVO reqVo)
            throws Exception {
        BaseVO rtnDto = equipService.insertEquip(files,reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[설비 수정]", description = "process_id 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/equip/{equipmentId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateEquip(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info")  EquipVO reqVO,
            @PathVariable String equipmentId) throws Exception {
        reqVO.setWorkplaceId(equipmentId);
        BaseVO rtnDto = equipService.updateEquip(files,reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[설비 삭제]", description = "process_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/equip/{equipmentId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEquip(@PathVariable String equipmentId)
            throws Exception {
        EquipVO param = new EquipVO();
        param.setEquipmentId(equipmentId);
        BaseVO rtnDto = equipService.deleteEquip(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[설비 일괄 삭제]", description = "process_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/equip/deleteEquips")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteEquips(
            @RequestBody List<EquipVO> list)
            throws Exception {
        equipService.deleteEquips(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[설비 유형 조회]", description = "설비 유형을 조회한다")
    @GetMapping(value = "/system/base/equip/std/getStdEquips")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getStdEquips(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EquipVO> list = equipService.getStdEquips(searchVo);

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


    @Operation(summary = "[설비 유형 저장]", description = "설비유형을 삽입/업데이트 한다.")
    @PutMapping(value = "/system/base/equip/std/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateEquip(
            @PathVariable String compId,
            @RequestBody List<TypeofEquipmentVO> list
            ) throws Exception {
        equipService.saveStdEquips(compId, list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
    @Operation(summary = "[설비 유형 삭제]", description = "설비유형을 삭제한다.")
    @DeleteMapping(value = "/system/base/equip/std/delete/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteStdEquips(
            @PathVariable String compId,
            @RequestBody List<TypeofEquipmentVO> list) throws Exception {
        equipService.deleteStdEquips(compId, list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }
    
    
    @Operation(summary = "[설비 유형별 설비 조회]", description = "설비 유형별 설비를 조회한다")
    @GetMapping(value = "/system/base/equip/getEquipmentByType")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEquipmentByType(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EquipVO> list = equipService.getEquipmentByType(searchVo);

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
    
    @Operation(summary = "[등록 설비 조회]", description = "등록된 설비 리스트만 조회한다")
    @GetMapping(value = "/system/base/equip/getStdEqList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getStdEqList(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<EquipVO> list = equipService.getStdEqList(searchVo);

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

    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/equip/downloadEquipExcelTemplate/{id}")
    public void downloadEquipExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        equipService.downloadEquipExcelTemplate(request, response, id);
    }

    @Operation(summary = "[엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/equip/insertEquipExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertEquipExcel(
            @RequestBody List<EquipVO> list)
            throws Exception {
        equipService.insertEquipExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
    
}
