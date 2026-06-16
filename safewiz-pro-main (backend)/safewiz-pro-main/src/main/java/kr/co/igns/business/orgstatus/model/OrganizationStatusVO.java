package kr.co.igns.business.orgstatus.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;



/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.orgstatus.model
 * ※ 파일명 : OrganizationStatusVO.java
 * ※ 기능명 :
 * ※ 작성자 : 김성현
 * ※ 최초생성일 : 2024. 9. 04.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrganizationStatusVO extends BaseVO {

	private static final long serialVersionUID = -2417117617959565367L;
	
	@Schema(description = "부서 상황 코드", example = "202401010001")
	private String statusId;
	
	@Schema(description = "작성년도", example = "2024")
	private String writeYear;
	
	@Schema(description = "작성일자", example = "202401010001")
	private String writeDt;
	
	@Schema(description = "문서타입", example = "202401010001")
	private String docType;
	
	@Schema(description = "문서번호", example = "202401010001")
	private String docNo;
	
	@Schema(description = "조직 ID", example = "AAA01")
	private String orgnId;
	
	@Schema(description = "회사 ID", example = "AAA01")
	private String compId;
	
	@Schema(description = "상세 게시판 코드", example = "00001")
	private String docSeq;
	
	@Schema(description = "작성 서명자 ID", example = "AAA01")
	private String writer;
	
	@Schema(description = "작성 서명", example = "AAA01")
	private String signWriter;
	
	@Schema(description = "검토 서명자 ID", example = "AAA01")
	private String reviewer;
	
	@Schema(description = "검토 서명", example = "AAA01")
	private String signReviewer;
	
	@Schema(description = "승인 서명자 ID", example = "AAA01")
	private String approver;
	
	@Schema(description = "승인 서명", example = "AAA01")
	private String signApprover;
	
	@Schema(description = "사용여부", example = "Y")
	private String useYn;
	
	@Schema(description = "사용여부(임시)", example = "Y")
	private String useYnM;
	
	@Schema(description = "내/외부 구분 코드", example = "I")
	private String gubun;

	@Schema(description = "내/외부 구분 명칭", example = "I")
	private String gubunNm;
	
	@Schema(description = "업무 내용", example = "업무 내용")
	private String workDesc;
	
	@Schema(description = "이슈", example = "업무 내용")
	private String issue;
	
	@Schema(description = "리스크", example = "리스크")
	private String risk;
	
	@Schema(description = "기회", example = "기회")
	private String chance;
	
	@Schema(description = "결재키", example = "")
	private String approvalKey;
	
	@Schema(description = "결재상태", example = "I")
	private String approvalStatus;
	
	@Schema(description = "결재상태명", example = "기안")
	private String approvalStatusNm;
	
	@Schema(description = "업무 내용", example = "업무 내용")
	private String createdAt;
	
	@Schema(description = "작성자", example = "홍길동")
	private String createdBy;
	
	@Schema(description = "부서명", example = "제조 ICT")
	private String orgnNm;
	
	@Schema(description = "년도", example = "2099")
	private String years;
	
	@Schema(description = "상황건수", example = "5")
	private String cnt;
	
	@Schema(description = "체크", example = "Y")
	private String checked;
	

	// 출력물
//	@Schema(description = "중간 페이지", example = "Y")
//	private String subPage;
//
//	@Schema(description = "전체 페이지", example = "Y")
//	private Integer page;
//
//	@Schema(description = "내부 페이지", example = "Y")
//	private Integer localPage;
	
	@Schema(description = "전체 출력 유무", example = "Y")
	private boolean printAll = false;
	
	private List<OrganizationStatusVO> children = new ArrayList<>();
	// commit test
}
