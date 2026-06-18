package kr.co.igns.business.impl.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ContractorAssmtReportVO extends BaseVO {
    private String partCompId;
    private String partCompNm;
    private String assmtDt;
    private String assmtManagerHrId;
    private String assmtManagerHrNm;
    private String assmtContent;
    private int point = 0;
    private String remark;
    private String superiorContent;
    private String insufficientContent;
    private String useYn;
    private String assmtUseYn;

    private List<ContractorChecklistVO> detailDataList;
    private List<ContractorAssmtReportHrVO> assmtHrList;
}
