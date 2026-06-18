package kr.co.igns.business.comp.company.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.comp.company.model
 * ※ 파일명 : CompanyVO.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Data
public class CompanyVO extends BaseVO {

	private static final long serialVersionUID = -2417117617959565367L;
	
	@Schema(description = "FEMS 사업장 아이디", example = "사업장 아이디")
	private String compId;
	
	@Schema(description = "FEMS 사업장 이름", example = "사업장 이름")
	private String compNm;

	@Schema(description = "FEMS 사업장 소개", example = "사업장 소개")
	private String compRmk;

	@Schema(description = "FEMS 사업자 등록 번호", example = "사업자 등록 번호")
	private String rgstNo;

	@Schema(description = "FEMS 법인 등록 번호", example = "법인 등록 번호")
	private String corpNo;

	@Schema(description = "FEMS 대표자", example = "대표자")
	private String rpstNm;
	
	@Schema(description = "FEMS 우편번호", example = "우편번호")
	private String zipCd;
	
	@Schema(description = "FEMS 주소", example = "주소")
	private String addrs1;
	
	@Schema(description = "FEMS 상세주소", example = "상세주소")
	private String addrs2;
	
	@Schema(description = "FEMS 담당자", example = "담당자")
	private String chrgPrsn;
	
	@Schema(description = "FEMS 연락처", example = "연락처")
	private String telNo;
	
	@Schema(description = "FEMS 업종", example = "업종")
	private String bizNm;
	
	@Schema(description = "FEMS 업종 코드", example = "업종 코드")
	private String bizCd;
	
	@Schema(description = "FEMS 주요 생산품", example = "주요 생산품")
	private String mainPrdt;
	
	@Schema(description = "FEMS 연간 생산량", example = "1000")
	private Integer prdAmt;
	
	@Schema(description = "FEMS 사업장 사진", example = "1")
	private Long photoFileId;
	
	// LocalDate -> String 으로 변경 
	@Schema(description = "FEMS 준공년월", example = "2024-01-01")
	private String cmpltDe;
	
	@Schema(description = "FEMS 연면적", example = "연면적")
	private String flArea;
	
	@Schema(description = "FEMS 매출 금액", example = "50000.123")
	private BigDecimal salesAmt;
	
	@Schema(description = "FEMS 상시  종업원 수", example = "100")
	private Integer fullEmplCnt;
	
	@Schema(description = "FEMS 일평균 근무 시간", example = "8")
	private Integer avrgWorkHr;
	
	@Schema(description = "FEMS 사업장 아이디", example = "사업장 아이디")
	private String upCompId;

	// [2024-07-11]  delYn 추가 - 신택훈)
	@Schema(description = "FEMS 삭제여부", example = "삭제여부")
	private YesNo delYn;
	
	// [2024-07-17]  attachId 추가 - 신택훈)
	@Schema(description = "FEMS 첨부파일", example = "uuid")
	private String attachId;

	// [2024-08-05] 초대키 추가 - 조동하
	@Schema(description = "사업장 초대키", example = "jwt")
	private String inviteKey;
	
	private List<CompanyVO> children = new ArrayList<>();
	
}
