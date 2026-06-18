package kr.co.igns.framework.comm.model;

import lombok.Data;

import java.util.List;

/*
 * @ 프로젝트       : ESG
 * @ 패키지          : kr.co.igns.framework.comm.model
 * @ 파일명          : LocalVO.java
 * @ 기능명          :
 * @ 작성자          : 조동하
 * @ 최초생성일     : 2024. 03. 08.
 * @ 프로그램 설명  : 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경내용 - 수정자)
 *    [2023-09-07] 카카오 Local API VO 추가  - 소민환
 *    [2024-03-08] KIST 날씨 관련 기능 추가 - 조동하
 *    [2024-12-04] 세이프위즈 pro 이관 - 이지훈
 */

@Data
public class LocalVO {
    private List<Document> documents;
    
    @Data
    public static class Document {
    	/* 경도 */
        private double x; 
        /* 위도 */
        private double y; 
    }
}


