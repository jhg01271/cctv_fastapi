package kr.co.igns.business.notice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class OutputVO extends BaseVO {

	@Schema(description = "설명")
	private String desc;
	
	@Schema(description = "라우터 경로")
	private String route;

	@Schema(description = "테이블 명")
	private String tableNm;

	@Schema(description = "출력가능 레포트 수(총합)")
	private int totalPrintableCnt = 0;

	@Schema(description = "출력가능 레포트 수")
	private int printableCnt = 0;
	
	@Schema(description = "키 값 모음")
	private List<OutputVO> params;

	@Schema(description = "디테일 코드")
	private String detailCd;
	
	@Schema(description = "아코디언내부 모음")
	private List<OutputVO> detailList;

	@Schema(description = "선택여부")
	private boolean checked;

	@Schema(description = "차트 ID 모음")
	private List<String> chartIdList;
}
