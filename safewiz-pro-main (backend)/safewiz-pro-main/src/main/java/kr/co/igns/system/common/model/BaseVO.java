package kr.co.igns.system.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.common.dto
 * ※ 파일명 : FemsDto.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 5. 9.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuppressWarnings("serial")
public class BaseVO implements Serializable {

	@Default
	private String cmd = "U";

	@Default
	private String cmdArray = "";
	private Long seq;
	private int resultCnt;
	private String createdAt;
	private String createdBy;
	private String updatedAt;
	private String updatedBy;
	private String role;
	private String compId;
	private String clntId;
	/* 20241017 이지훈 추가*/
	private String docSeq;
	private String writeYear;
	private String docType;
	private String docNo;
	private String docDetailSeq;
	private String id;
	private String nm;
	private String targetId;

	private boolean printAll;
	private String type;
	private boolean checkedPrint; // 체크된 항목들만 출력할 때 True, 체크없이 모든 데이터 출력할 때 false
	private int page;
	private int subPage;
	private int localPage;
	private String extra1;

	//파일
	@Schema(description = "파일 정보 리스트")
	private List<FileVO> files;  // 파일 정보 리스트 추가
	private String fileId;	//조회, 저장(file upload)
	private String fileId2;
	private List<String> deleteFiles;	//저장(file delete)
	private List<String> deleteFiles2;

	@Schema(description = "결재 상태")
	private String approvalStatus;

	private String currentUserHrId;
}
