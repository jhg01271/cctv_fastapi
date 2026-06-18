package kr.co.igns.system.base.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.base.model.FavoritesVO;
import kr.co.igns.system.base.service.FavoritesService;
import kr.co.igns.system.base.service.HrService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : safewiz-back-end
 * ※ 패키지 : ;
 * ※ 파일명 : Favorites
 * ※ 기능명 : 즐겨찾기
 * ※ 작성자 : 김동건
 * ※ 최초생성일 : 2026. 1. 5.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Favorites")
@RestController
@RequiredArgsConstructor
public class FavoritesController {
    private final MessageService messageService;
    private final FavoritesService favoritesService;

    @Operation(summary = "[즐겨찾기 사이드바 조회]", description = "즐겨찾기 사이드바를 조회한다")
    @GetMapping(value = "/safewizpro/base/getFavoritesSide")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getFavoritesSide(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.initialize();
        ArrayNode list = favoritesService.getFavoritesSide(searchVo);
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

    @Operation(summary = "[즐겨찾기 popup 조회]", description = "즐겨찾기 popup을 조회한다")
    @GetMapping(value = "/safewizpro/base/getFavoritesList")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getFavoritesList(
            @ModelAttribute @Valid FavoritesVO vo) throws Exception {
        List<FavoritesVO> list = favoritesService.getFavoritesList(vo);

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

    @Operation(summary = "[즐겨찾기 예시불러오기 적용]", description = "즐겨찾기 예시불러오기를 적용한다")
    @PostMapping(value = "/safewizpro/base/saveFavoritesExample")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> saveFavoritesExample(
           @RequestBody List<FavoritesVO> voList) throws Exception {

        favoritesService.saveFavoritesExample(voList);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[즐겨찾기 폴더 저장]", description = "즐겨찾기 폴더를 저장한다")
    @PostMapping(value = "/safewizpro/base/addFavoritesFolder")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> addFavoritesFolder(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        favoritesService.addFavoritesFolder(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[즐겨찾기 저장]", description = "즐겨찾기 데이터를 저장한다")
    @PostMapping(value = "/safewizpro/base/addFavoritesMenu")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> addFavoritesMenu(
            @RequestPart(value = "data") List<Object> dataVo) throws Exception {

        FavoritesVO list = favoritesService.addFavoritesMenu(dataVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", list);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[즐겨찾기 메뉴 수정]", description = "즐겨찾기 메뉴를 수정한다")
    @PostMapping(value = "/safewizpro/base/updateFavoritesFolder")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateFavoritesFolder(
            @RequestPart(value = "data") List<Object> dataVo) throws Exception {
        favoritesService.updateFavoritesFolder(dataVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }

    @Operation(summary = "[즐겨찾기 순서 수정]", description = "즐겨찾기 순서를 수정한다")
    @PostMapping(value = "/safewizpro/base/updateFavoritesOdr")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> updateFavoritesOdr(
            @RequestPart(value = "data") List<Object> dataVo) throws Exception {

        favoritesService.updateFavoritesOdr(dataVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }


    @Operation(summary = "[즐겨찾기 삭제]", description = "즐겨찾기 데이터를 삭제한다")
    @DeleteMapping(value = "/safewizpro/base/deleteFavoritesMenu")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteFavoritesMenu(
            @RequestPart(value = "data") List<FavoritesVO> dataVo) throws Exception {
        favoritesService.deleteFavoritesMenu(dataVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", null);
        return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"),
                messageService.getMessage("updateSuccess.msg"), result);
    }


    @Operation(summary = "[예시불러오기 조회]", description = "예시불러오기 트리를 조회한다")
    @GetMapping(value = "/safewizpro/base/favoritesMenuExample")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> exampleMenu(
            @ModelAttribute @Valid SpSearchVO searchVo) throws Exception {
        searchVo.initialize();
        ArrayNode list = favoritesService.exampleMenu(searchVo);

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
}
