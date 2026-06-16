package kr.co.igns.business.impl.model;

import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ContractorChecklistVO extends BaseVO {

    private String id;
    private String evalTypeId;

    private String compId;

    private String name;
    private String evalTypeNm;

    private String roleNm;

    private String useYn;

    private int ordSeq;

    private int weight;

    private List<ContractorChecklistDetailVO> detailDataList;
}
