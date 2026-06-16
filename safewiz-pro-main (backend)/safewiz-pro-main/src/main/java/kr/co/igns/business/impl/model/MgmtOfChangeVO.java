package kr.co.igns.business.impl.model;

import java.util.List;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class MgmtOfChangeVO extends BaseVO{
    private String divEtc;
    private String content;
    private String reqOrgnId;
    private String reqOrgnNm;
    private String reqDt;
    private String changeDt;
    private String changeOrgnId;
    private String changeOrgnNm;
    private String reviewContent;
    private String reviewDt;
    private String result;
    private String resultContent;
    private String useYn;
    private String div;
    private String reviewHrNm;
    private String resultNm;
    private String changeOverview;
    private String changeHrNm;
}
