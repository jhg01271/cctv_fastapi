package kr.co.igns.business.comp.company.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.model
 * ※ 파일명 : CompanyVO.java
 * ※ 기능명 :
 * ※ 작성자 : 신택훈
 * ※ 최초생성일 : 2024. 7. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

//[2024-07-23] 패키지 변경 - 신택훈

@Data
public class CompanyUserVO extends BaseVO {

	private static final long serialVersionUID = -2417117617959565367L;


	@Schema(description = "멤버 순번", example = "1")
	private Long memSeq;

	@Schema(description = "FEMS 사업장 아이디", example = "사업장 아이디")
	private String compId;

	@Schema(description = "권한 순번", example = "1")
	private Long grpCd;
	
	@Schema(description = "권한 이름", example = "1")
	private String grpNm;

	@Schema(description = "사용자 아이디", example = "사용자 아이디")
	private String userId;

	@Schema(description = "FEMS 사업장 이름", example = "사업장 이름")
	private String compNm;

	@Schema(description = "FEMS 사업자 등록 번호", example = "사업자 등록 번호")
	private String rgstNo;

	private String upCompId;

	private String cmd;

	private List<CompanyUserVO> children = new ArrayList<>();
	
}
