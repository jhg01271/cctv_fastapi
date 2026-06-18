package kr.co.igns.business.support.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import java.util.List;

@Data

public class SAndHQualificationCertRegisterVO extends BaseVO{
    private String writeDate;
    private String hrId;
    private String hrNm;
    private String orgnNm;
    private String jbrpNm;
    private String useYn;
    private String regNm;
    private String regDt;
    private String regDetail;
    private String regCheckYn;
    private String cancleDt;
    private String cancleDetail;
    private String cancleCheckYn;
    private String logoId;
    private List<SafetyDutiesUserVO> memberList;
}
