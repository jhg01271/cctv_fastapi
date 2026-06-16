package kr.co.igns.business.comp.company.controller;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.company.model.CompVO;
import kr.co.igns.business.comp.company.service.CompanyMyService;
import kr.co.igns.business.comp.company.service.CompanyService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.controller
 * ※ 파일명 : CompanyController.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 15.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Tag(name = "Client(고객)", description = "고객 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompanyMyController {

	private final MessageService messageService;
	private final CompanyService companyService;
    private final CompanyMyService companyMyService;



    @Operation(summary = "[목록 조회]", description = "목록을 조회한다")
    @GetMapping(value = "/comp/company/site/search/my")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        int totalCount = companyMyService.searchCount(searchVo);
        List<CompVO> list = companyMyService.search(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", totalCount);
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[트리 조회]", description = "목록으로 트리를 조회한다")
    @GetMapping(value = "/comp/company/site/tree/my")
    public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> treeComp(@ModelAttribute @Valid SpSearchVO searchVo) throws Exception {

        ArrayNode list = companyMyService.tree(searchVo);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        if (list == null || list.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
    
    
    // 내 사업장 한건 조회 추가
//    @GetMapping(value = "/comp/company/site/{compId}")

}
