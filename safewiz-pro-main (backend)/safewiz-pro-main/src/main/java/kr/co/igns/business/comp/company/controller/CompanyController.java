package kr.co.igns.business.comp.company.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.comp.company.service.CompanyMyService;
import kr.co.igns.business.comp.company.service.CompanyService;
import kr.co.igns.framework.config.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.comp.company.controller
 * ※ 파일명 : CompanyController.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 8. 16.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 *
 * FIXME 내가 소속되지 않은 다른 사업장을 조회 가능? 관리자가 아닌데... 삭제 할 컨트롤러파일
 * </pre>
 */
@Tag(name = "Client(고객)", description = "고객 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompanyController {

	private final MessageService messageService;
	private final CompanyService companyService;
    private final CompanyMyService compService;

//	@Operation(summary = "[목록 조회]", description = "목록을 조회한다")
//	@GetMapping(value = "/comp/company/site/search")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> search(@ModelAttribute @Valid FemsSearchVO searchVo) throws Exception {
//
//		int totalCount = companyService.searchCount(searchVo);
//		List<CompanyVO> list = companyService.search(searchVo);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("totalCount", totalCount);
//		result.put("list", list);
//		if (list == null || list.isEmpty()) {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
//		} else {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//		}
//	}
//	
//	@Operation(summary = "[한건 조회]", description = "id를 이용해 한건의 정보를 조회 한다.")
//	@GetMapping(value = "/comp/company/site/{compId}")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> findOne(@PathVariable String compId) throws Exception {
//
//		BaseVO rtnDto = companyService.findOne(compId);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", rtnDto);
//
//		if (rtnDto == null) {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
//		} else {
//			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//		}
//	}
	

    

}
