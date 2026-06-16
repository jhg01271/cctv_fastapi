package kr.co.igns.business.notice.model;

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
 *
 */

@Data
public class NoticeVO extends BaseVO {
	@Schema(description = "고객사 ID")
	private String clntId;

	@Schema(description = "공지사항 제목")
	private String subject;

	@Schema(description = "작성일자 (YYYYMMDD 형식)")
	private String writeDt;

	@Schema(description = "문서 번호")
	private String docNo;

	@Schema(description = "작성자 이름")
	private String userNm;

	@Schema(description = "푸시 메세지 발송자 ID")
	private String userId;
}
