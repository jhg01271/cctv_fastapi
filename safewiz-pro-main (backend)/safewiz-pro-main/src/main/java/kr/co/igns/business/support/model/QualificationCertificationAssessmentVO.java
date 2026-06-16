package kr.co.igns.business.support.model;

import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import java.util.List;

@Data
public class QualificationCertificationAssessmentVO extends BaseVO{
        private String hrId;
        private String writeDate;
        private String remark;
        private String writerId;
        private String writerNm;
        private String writerOrgn;
        private String workingAt;
        private String hrNm;
        private String jbrpNm;
        private String orgnNm;
        private String hrType;
        private String passYn;
        private int column1;
        private int column2;
        private int column3;
        private int column4;
        private int column5;
        private String columnEtc1;
        private String columnEtc2;
        private String useYn;
        private UtilsVO writer;
        private UtilsVO reviewer;
        private UtilsVO approver;
        private String logoId;
        private String approvalStatus;
}
