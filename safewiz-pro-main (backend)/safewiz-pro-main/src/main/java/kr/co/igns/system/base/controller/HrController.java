package kr.co.igns.system.base.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.HrJbrpVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.OrgHistoryVO;
import kr.co.igns.system.base.service.HrService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.UserManageVO;
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
 * ※ 파일명 : Organization
 * ※ 기능명 : 조직 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Hr")
@RestController
@RequiredArgsConstructor
public class HrController {
    private final MessageService messageService;
    private final HrService hrService;

    @Operation(summary = "[인원 조회]", description = "인원을 조회한다\n복수 사업장은 추후반영")
    @GetMapping(value = "/system/base/hr/getHr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHr(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        List<HrVO> list = hrService.getHr(searchVo);

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

    @Operation(summary = "[인원 상세 조회]", description = "인원 상세정보를 조회한다.")
    @GetMapping(value = "/system/base/hr/getHrDetail/{hrId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHrDetail(
            @PathVariable String hrId) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setHrId(hrId);
        HrVO list = hrService.getHrDetail(searchVo);

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

    @Operation(summary = "[해당 인원의 사업장 최대 접속 인원 체크]", description = "해당 인원의 사업장 최대 접속 인원을 초과했는지 조회한다.")
    @GetMapping(value = "/system/base/hr/checkExceedMaxHrCount")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> checkExceedMaxHrCount(
            @ModelAttribute @Valid HrVO hrVO) throws Exception {
        boolean isCheckHrCount = hrService.checkExceedMaxHrCount(hrVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", isCheckHrCount);

        if (result == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[인원 등록 - 엑셀 업로드]", description = "엑셀 내용을 등록한다.")
    @PostMapping(value = "/system/base/hr/insertHrExcel")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertHrExcel(
            @RequestBody List<HrVO> list)
            throws Exception {
        List<HrVO> hrList = hrService.insertHrExcel(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", hrList);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[엑셀 양식 다운로드]", description = "엑셀 양식을 다운로드한다.")
    @GetMapping(value = "/system/base/hr/downloadHrExcelTemplate/{id}")
    public void downloadHrExcelTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        hrService.downloadHrExcelTemplate(request, response, id);
    }

    @Operation(summary = "[인원 등록]", description = "인원을 등록한다.")
    @PostMapping(value = "/system/base/hr/insertHr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertHr(
            @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "info") HrVO reqVo)
            throws Exception {
        BaseVO rtnDto = hrService.insertHr(files, reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[인원 수정]", description = "hr_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/base/hr/{hrId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHr(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                               @RequestPart(value = "info") HrVO reqVO,
                                                                               @PathVariable String hrId) throws Exception {
        reqVO.setHrId(hrId);
        BaseVO rtnDto = hrService.updateHr(files, reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[인원 삭제]", description = "hr_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/base/hr/{hrId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHr(@PathVariable String hrId)
            throws Exception {
        HrVO param = new HrVO();
        param.setHrId(hrId);
        BaseVO rtnDto = hrService.deleteHr(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[인원 일괄 삭제]", description = "hr_id list에 해당되는 조직을 삭제 한다.")
    @DeleteMapping(value = "/system/base/hr/deleteHrs")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHrs(
            @RequestBody List<HrVO> list)
            throws Exception {
        hrService.deleteHrs(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[인원별 조직 이력 조회]", description = "인원별 조직 이력을 조회한다.")
    @GetMapping(value = "/system/base/hr/getOrgHistory/{hrId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getOrgHistory(
            @PathVariable String hrId) throws Exception {
        List<OrgHistoryVO> list = hrService.getOrgHistoryList(hrId);

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

    @Operation(summary = "[직무데이터 조회]", description = "직무데이터를 조회한다.")
    @GetMapping(value = "/system/base/hr/getHrJbrp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHrJbrp(
    ) throws Exception {
        List<HrJbrpVO> list = hrService.getHrJbrp();

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
    
    @Operation(summary = "[소속 사업장 직무 팝업 데이터 조회]", description = "직무데이터를 조회한다.")
    @GetMapping(value = "/system/base/hr/getHrJbrpByCompId/{compId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getHrJbrpByCompId(@PathVariable String compId) throws Exception {
        List<HrJbrpVO> list = hrService.getHrJbrpByCompId(compId);

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

    // ------- 직위 관리
    @Operation(summary = "[직위데이터 예시 조회]", description = "직위데이터 예시를 조회한다.")
    @GetMapping(value = "/system/base/hr/getDatasetHrJbrp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getDatasetHrJbrp(
    ) throws Exception {
        List<HrJbrpVO> list = hrService.getDatasetHrJbrp();

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

    @Operation(summary = "[직위데이터 등록]", description = "직위데이터를 등록한다.")
    @PostMapping(value = "/system/base/hr/saveHrJbrp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveHrJbrp(
            @RequestBody List<HrJbrpVO> voList)
            throws Exception {
        BaseVO rtnDto = hrService.saveHrJbrp(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[직위데이터 삭제]", description = "직위데이터를 삭제 한다.")
    @DeleteMapping(value = "/system/base/hr/deleteHrJbrp")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteHrJbrp(
            @RequestBody List<HrJbrpVO> voList)
            throws Exception {
        hrService.deleteHrJbrp(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[인원 비밀번호 초기화]", description = "hr_id를 이용해 비밀번호를 초기화 한다.")
    @PutMapping(value = "/system/base/hr/resetPassword")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> resetPassword(
            @RequestBody UserManageVO vo)
            throws Exception {
        BaseVO rtnDto = hrService.resetPassword(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }
}
