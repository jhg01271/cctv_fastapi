package kr.co.igns.business.impl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.impl.model.FacilityEquipManageDetailVO;
import kr.co.igns.business.impl.model.FacilityEquipManageVO;
import kr.co.igns.business.impl.service.EmergencyResponseService;
import kr.co.igns.business.impl.service.FacilityEquipManageService;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : kr.co.igns.business.impl.controller
 * ※ 파일명 : FacilityEquipManage
 * ※ 기능명 : 시설 설비 관리 
 * ※ 작성자 : 이상걸
 * ※ 최초생성일 : 2024. 11. 32.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "FacilityEquipManage", description = "시설 설비 관리")
@RestController
@RequiredArgsConstructor
public class FacilityEquipManageController {
    private final MessageService messageService;
    private final FacilityEquipManageService facilityEquipManageService;

    @Operation(summary = "[시설 설비 관리 조회]", description = "시설 설비 관리 메뉴를 조회한다.")
    @GetMapping(value = "/safewizpro/impl/getFacilityEquipManage")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyResponse(
            @ModelAttribute @Valid SpSearchVO spSearchVO) throws Exception {
        List<FacilityEquipManageVO> list = facilityEquipManageService.getFacilityEquipManage(spSearchVO);

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
    
    @Operation(summary = "[시설 설비 관리 상세 조회]", description = "시설 설비 관리 메뉴를 상세 조회한다.")
    @GetMapping(value = "/safewizpro/impl/getFacilityEquipManageDetail")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getEmergencyResponseDetail(
            @ModelAttribute @Valid FacilityEquipManageVO spSearchVO) throws Exception {
        List<FacilityEquipManageVO> list = facilityEquipManageService.getFacilityEquipManageDetail(spSearchVO);

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
    
	@Operation(summary = "[시설설비 추가]", description = "시설설비 신규 데이터를 추가/변경한다")
	@PostMapping(value = "/safewizpro/impl/setFacilityEquipManage")
	public ResponseEntity<SingleResponseDto<FacilityEquipManageVO>> setFacilityEquipManage(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") FacilityEquipManageVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());
		FacilityEquipManageVO result = facilityEquipManageService.insertFacilityEquipManage(files, vo);
		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}
	
	@Operation(summary = "[시설설비 삭제]", description = "시설설비 신규 데이터를 삭제다")
	@PostMapping(value = "/safewizpro/impl/deleteFacilityEquipManage")
	public ResponseEntity<SingleResponseDto<List<FacilityEquipManageVO>>> deleteFacilityEquipManage(@RequestBody @Valid List<FacilityEquipManageVO> voList) throws Throwable {
		List<FacilityEquipManageVO> result = facilityEquipManageService.deleteFacilityEquipManage(voList);
		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}
}
