package kr.co.igns.business.alarm.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 기능명 :
 * ※ 작성자 : 심지성
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 * [2024-11-08] 파일 목록 추가
 */

@Data
public class TrainingPlanImplAlarmVO extends BaseVO {
	@Schema(description = "사용자 ID")
	private String userId;

	@Schema(description = "알람 발송일 (YYYYMMDD)")
	private String alarmDt;

	@Schema(description = "알람 번호")
	private int alarmSeq;

	@Schema(description = "알람 제목")
	private String fcmTitle;

	@Schema(description = "알람 내용")
	private String fcmBody;

	@Schema(description = "알람 데이터 [이미지 등등] (json)")
	private String fcmData;

	@Schema(description = "메뉴 ID")
	private String menuId;

	@Schema(description = "작성년도 (YYYY 형식)")
	private String writeYear;
	
	@Schema(description = "작성일자 (YYYYMMDD 형식)")
	private String writeDt;

	@Schema(description = "문서 번호")
	private String docType;
	
	@Schema(description = "문서 번호")
	private String docNo;

	@Schema(description = "읽음 여부")
	private String readYn;

	@Schema(description = "읽은 시각")
	private String readAt;

	@Schema(description = "발송일")
	private String createdAt;

	@Schema(description = "발송자")
	private String createdBy;

	@Schema(description = "수정일")
	private String updatedAt;

	@Schema(description = "수정자")
	private String updatedBy;

	@Schema(description = "라우터명")
	private String routerNm;

	@Schema(description = "삭제 주기")
	private int deleteCycle;

	@Schema(description = "읽지 않은 알람 카운트")
	private int count;
	
	

}

