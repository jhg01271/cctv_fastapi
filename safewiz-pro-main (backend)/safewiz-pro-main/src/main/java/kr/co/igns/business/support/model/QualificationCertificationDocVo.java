package kr.co.igns.business.support.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class QualificationCertificationDocVo extends BaseVO{
    private String compId;      //사업장 ID
    private String itemId;      //평가항목 ID
    private String itemNm;      //평가항목명
    private String itemType;    //평가항목 타입
    private String criteriaId;  //평가기준 ID
    private String criteriaNm;  //평가기준명
    private int point;       //점수
    private int ordSeq;      //순번
    private String useYn;       //사용여부
    private String createdAt;   //생성일자
    private String createdBy;   //생성자
    private String updatedAt;   //수정일자
    private String updatedBy;   //수정자
}
