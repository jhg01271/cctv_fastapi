package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Client
 * ※ 기능명 : 고객사 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 23.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Client")
@RestController
@RequiredArgsConstructor
public class ClientController {
    private final MessageService messageService;
    private final ClientService clientService;

    @Operation(summary = "[고객사 조회]", description = "고객사를 조회한다")
    @GetMapping(value = "/system/setting/client/getClient")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClient(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        int totalCount = clientService.searchCount(searchVo);
        List<ClientVO> list = clientService.getClient(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[고객사 상세 조회]", description = "고객사 상세정보를 조회한다")
    @GetMapping(value = "/system/setting/client/getClientDetail/{clntId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getClientDetail(
            @PathVariable String clntId) throws Exception {
        ClientVO list = clientService.getClientDetail(clntId);

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

    @Operation(summary = "[고객사 등록]", description = "고객사를 등록한다.")
    @PostMapping(value = "/system/setting/client/insertClient" , consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertClient(
        @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
		@RequestPart(value = "info") ClientVO dto)
        throws Exception {
                
        BaseVO rtnDto = clientService.insertClient(files, dto);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"),
                messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[고객사 수정]", description = "clnt_id를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/setting/client/{clntId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateClient( @NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                                                    @RequestPart(value = "info") ClientVO reqVO,
                                                                                    @PathVariable String clntId) throws Exception {
        reqVO.setClntIdTemp(clntId);
        BaseVO rtnDto = clientService.updateClient(files,reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[고객사 삭제]", description = "clnt_id를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/client/{clntId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteClient(@PathVariable String clntId)
            throws Exception {
        ClientVO param = new ClientVO();
        param.setClntIdTemp(clntId);
        BaseVO rtnDto = clientService.deleteClient(param);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[고객사 일괄 삭제]", description = "clnt_id list에 해당되는 고객사를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/client/deleteClients")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteClients(@RequestBody List<ClientVO> list)
            throws Exception {
        clientService.deleteClients(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"),
                messageService.getMessage("deleteSuccess.msg"), result);
    }
}
