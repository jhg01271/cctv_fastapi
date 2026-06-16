package kr.co.igns.system.setting.controller;

import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.support.model.AnnualTrainingPlanVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.ClientCompVO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import kr.co.igns.system.setting.service.ClientCompService;
import kr.co.igns.system.setting.service.ClientService;
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
 * ※ 파일명 : ClientComp
 * ※ 기능명 : 고객별 사업장 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "고객별 사업장 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/setting/clientComp")
public class ClientCompController {
    private final MessageService messageService;
    private final ClientCompService clientCompService;

    @Operation(summary = "[고객사 조회]", description = "고객사를 조회한다")
    @GetMapping(value = "/getClient")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClient(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        List<ClientVO> list = clientCompService.getClient(searchVo);

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

    @Operation(summary = "[고객사별 사업장 조회]", description = "고객사별 사업장을 조회한다")
    @GetMapping(value = "/getCompByClient")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getCompByClient(@ModelAttribute @Valid SpSearchVO searchVo)
            throws Exception {
        List<ClientCompVO> list = clientCompService.getComp(searchVo);

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

    @Operation(summary = "[고객사별 사업장 추가]", description = "고객사별 사업장를 추가한다.")
    @PostMapping(value = "/saveCompByClient")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveCompByClient(@RequestBody @Valid List<ClientCompVO> reqVO) throws Exception {
        clientCompService.saveCompByClient(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", reqVO);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[고객사별 사업장 삭제]", description = "고객사별 사업장를 삭제 한다.")
    @DeleteMapping(value = "/deleteCompByClient")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteCompByClient(@RequestBody @Valid List<ClientCompVO> reqVO)
            throws Exception {
        clientCompService.deleteCompByClient(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", reqVO);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
}
