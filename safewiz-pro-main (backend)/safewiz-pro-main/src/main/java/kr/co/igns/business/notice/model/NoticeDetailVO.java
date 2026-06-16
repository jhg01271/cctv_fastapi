package kr.co.igns.business.notice.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
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
public class NoticeDetailVO extends BaseVO {

	private static final long serialVersionUID = -2417117617959565367L;
	
	@Schema(description = "클라이언트 ID")
	private String clntId;

	@Schema(description = "작성일자 (YYYYMMDD 형식)")
	private String writeDt;

	@Schema(description = "문서 번호")
	private String docNo;

	@Schema(description = "키 문서 번호")
	private String keyDocNo;

	@Schema(description = "공지사항 제목")
	private String subject;

	@Schema(description = "공지사항 내용")
	private String content;

	@Schema(description = "작성자 ID")
	private String hrId;

	@Schema(description = "작성자 이름")
	private String hrNm; // 작성자 정보 객체
	
	@Schema(description = "작성자 직책")
	private String jobTitle;

	@Schema(description = "나스에 저장된 프로필 이미지")
	private String orgName;
	
	@Schema(description = "나스에 저장된 프로필 이미지")
	private String profileImageId;

	@Schema(description = "공지사항 유형")
	private String noticeType;

	@Schema(description = "시작일 (YYYYMMDD 형식)")
	private String dispStDate;

	@Schema(description = "종료일 (YYYYMMDD 형식)")
	private String dispEdDate;
}
