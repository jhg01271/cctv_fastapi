package kr.co.igns.business.comp.master.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.math.BigDecimal;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.factory.model
 * ※ 파일명 : CompanyPreferencesVO.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Data
public class CompanyPreferencesVO extends BaseVO {

	private static final long serialVersionUID = -2417117617959565367L;
	
	@Schema(description = "FEMS 사업장 아이디", example = "사업장 아이디")
	private String compId;
	
	@Schema(description = "FEMS 전력 임계치 값", example = "1000")
	private BigDecimal eCriVal;

	@Schema(description = "FEMS 용수 임계치 값", example = "1000")
	private BigDecimal wCriVal;

	@Schema(description = "FEMS 가스 임계치 값", example = "1000")
	private BigDecimal gCriVal;

	@Schema(description = "FEMS 전력사전 알림 여부", example = "N")
	private YesNo eBfAlertYn;
	
	@Schema(description = "FEMS 가스사전 알림 여부", example = "N")
	private YesNo gBfAlertYn;
	
	@Schema(description = "FEMS 용수사전 알림 여부", example = "N")
	private YesNo wBfAlertYn;
	
	@Schema(description = "FEMS 알림 수신 메일", example = "test@test.com")
	private String alertMails;
	
	@Schema(description = "FEMS 계측 테이블 명", example = "rt_data001")
	private String senseTbNm;

}
