package kr.co.igns.business.dataset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.business.dataset.model.TypeofbusinessVO;
import kr.co.igns.business.dataset.service.DatasetService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;
/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Comp
 * ※ 기능명 : 코드 조회
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 26.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Dataset")
@RestController
@RequiredArgsConstructor
public class DatasetController {
    private final MessageService messageService;
    private final DatasetService datasetService;

    // 업종코드 조회
    @Operation(summary = "[업종코드 조회]", description = "업종코드를 조회한다")
    @GetMapping(value = "/safewizpro/dataset/getTypeofbusiness")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTypeofbusiness(@Valid SpSearchVO searchVo) throws Exception {
        // 페이지와 페이지 사이즈를 전달해 데이터 조회
        List<TypeofbusinessVO> list = datasetService.getTypeofbusiness(searchVo);

        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true,
                    messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true,
                    messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    // 설비유형 조회
    @Operation(summary = "[설비유형 조회]", description = "설비유형을 조회한다")
    @GetMapping(value = "/safewizpro/dataset/getTypeofEquipment")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getTypeofEquipment(
            @RequestParam(value = "page", defaultValue = "1") int page,        // 페이지 번호
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, // 페이지당 아이템 수,
            @RequestParam(value = "compId") String compId
    ) throws Exception {

        // 페이지와 페이지 사이즈를 전달해 데이터 조회
        List<TypeofEquipmentVO> list = datasetService.getTypeofEquipment(page, pageSize, compId);

        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true,
                    messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true,
                    messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }
}
