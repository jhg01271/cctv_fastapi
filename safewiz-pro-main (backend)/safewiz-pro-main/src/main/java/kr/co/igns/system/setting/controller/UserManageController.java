package kr.co.igns.system.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.setting.model.CompVO;
import kr.co.igns.system.setting.model.UserManageVO;
import kr.co.igns.system.setting.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : User
 * ※ 기능명 : 사용자 관리
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 8. 26.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "User")
@RestController
@RequiredArgsConstructor
public class UserManageController {
    private final MessageService messageService;
    private final UserManageService userService;
    @Operation(summary = "[사용자 조회]", description = "사용자를 조회한다")
    @GetMapping(value = "/system/setting/user/getUser")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getUser(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        searchVo.initialize();
        int totalCount = userService.searchCount(searchVo);
        List<UserManageVO> list = userService.getUser(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);

        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[사용자 상세 조회]", description = "사용자 상세정보를 조회한다")
    @GetMapping(value = "/system/setting/user/getUserDetail/{userId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getUserDetail(
            @PathVariable String userId) throws Exception {
        UserManageVO list = userService.getUserDetail(userId);

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

    @Operation(summary = "[사용자 등록]", description = "사용자를 등록한다.")
    @PostMapping(value = "/system/setting/user/insertUser")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertUser(@RequestBody @Valid UserManageVO reqVo) throws Exception {
        BaseVO rtnDto = userService.insertUser(reqVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 수정]", description = "use_cd를 이용해 한건의 정보를 수정 한다.")
    @PutMapping(value = "/system/setting/user/{userId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateUser(@RequestBody @Valid UserManageVO reqVO, @PathVariable String userId) throws Exception {
        reqVO.setUserId(userId);
        BaseVO rtnDto = userService.updateUser(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 삭제]", description = "use_cd를 이용해 한건의 정보를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/user/{userId}")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteUser(@PathVariable String userId, @RequestBody UserManageVO reqVO) throws Exception {
        BaseVO rtnDto = userService.deleteUser(reqVO);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[사용자 일괄 삭제]", description = "use_cd list에 해당되는 사용자를 삭제 한다.")
    @DeleteMapping(value = "/system/setting/user/deleteUsers")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteUsers(@RequestBody List<UserManageVO> list) throws Exception {
        userService.deleteUsers(list);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
    }

    @Operation(summary = "[인원별 사업장 매핑]", description = "인원별 사업장 매핑 테이블 데이터를 저장한다.")
    @PostMapping(value = "/system/setting/user/insertHrInfoCompMap")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> insertHrInfoCompMap(@RequestBody @Valid UserManageVO vo) throws Exception {
        List<UserManageVO> rtnDto = userService.insertHrInfoCompMap(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }

    @Operation(summary = "[인원별 사업장 매핑]", description = "인원별 사업장 매핑 테이블 데이터를 수정한다.")
    @PostMapping(value = "/system/setting/user/updateHrInfoCompMap")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateHrInfoCompMap(@RequestBody @Valid UserManageVO vo) throws Exception {
        List<UserManageVO> rtnDto = userService.updateHrInfoCompMap(vo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", rtnDto);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
    }
}
