package kr.co.igns.framework.report.model;

import java.util.Map;

import lombok.Data;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 * 
 * @ 프로젝트      : esg
 * @ 패키지      : kr.co.igns.framework.report.model
 * @ 파일명      : ReportVO.java
 * @ 기능명       : 
 * @ 작성자       : 유현창
 * @ 최초생성일    : 2022. 5. 18
 * @ 프로그램 설명    :  jasper Report VO
 * 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *   [2022. 5. 18] 파라메타들 추가   - 유현창
 */
@Data
public class ReportVO {
	private JRDataSource dataSource = new JREmptyDataSource(); // jrxml 파일에 들어갈 데이터소스
	private Map<String, Object> parameter; // jrxml 파일에 들어갈 파라메타
	private String fileName;               // 파일명
	private String type;                   // 레포트 타입 ex) ppt, pdf, docx, web
	private String jrxmlPath;              // jrxml 파일 경로 classpath 기준  src/main/resource 밑부터 시작
	
	
}
