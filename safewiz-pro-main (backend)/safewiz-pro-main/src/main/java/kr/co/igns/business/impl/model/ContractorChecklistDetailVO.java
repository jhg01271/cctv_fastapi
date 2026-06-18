package kr.co.igns.business.impl.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ContractorChecklistDetailVO extends BaseVO {

    private String targetId;
    private String evalTypeId;

    private int ordSeq;
    
    private String useYn;
    
    private String content;
    private String evalDetailNm;

    private int weight;

    private String divA;

    private String divB;

    private String divC;

    private String divD;

    private String divE;

    private String id;
    private String evalDetailId;

    private int point;
}
