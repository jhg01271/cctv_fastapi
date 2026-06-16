package kr.co.igns.business.impl.model;

import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ContractorRegisterVO extends BaseVO {
    private String title;
    private String writeDt;
    private String partCompId;              //협력사 ID
    private String partCompNm;              //협력사 명
    private String partCompItem;            //품목 ID
    private String partCompItemNm;          //품목 명
    private String telNo;                   //전화번호
    private String hrNm;                    //대표자명
    private String zipCd;                   //우편번호
    private String addrs1;                  //주소
    private String addrs2;                  //상세주소
    private String remark;                  //비고
    private String useYn;                   //사용여부
    private Boolean isCompDirectlyWrite;
    private Boolean isHrDirectlyWrite;
    private List<HrVO> hrList;
    private String regDt;                   //등록일자
}
