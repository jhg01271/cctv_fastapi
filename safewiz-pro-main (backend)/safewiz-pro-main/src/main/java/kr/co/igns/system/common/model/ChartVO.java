package kr.co.igns.system.common.model;
import kr.co.igns.system.common.code.YesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.common.dto
 * ※ 파일명 : FemsSearchDto.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 5. 9.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChartVO {
	private String name;
	private BigDecimal value;
	private String xAxis;
	private YesNo defColYn;
}
