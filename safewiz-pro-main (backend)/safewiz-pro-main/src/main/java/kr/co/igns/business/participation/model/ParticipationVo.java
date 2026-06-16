package kr.co.igns.business.participation.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipationVo extends BaseVO{

    private String commId;
    private String commNm;
    private String commDt;
    private String commAt;
    private String commDiv;
    private String commAgenda;
    private String location;
    private String remark;
    private String useYn;
    private String type;
    private String count;
    private String writeYear;
    private String docType;
    private String docNo;
    private String targetId;
    private Integer percent;
    private String companyCount;
    private String employeeCount;
    private String partnerCount;

    private List<SignatureDto> companyMember = new ArrayList<SignatureDto>();
    private List<SignatureDto> employeeMember  = new ArrayList<SignatureDto>();;
    private List<SignatureDto> partnerMember  = new ArrayList<SignatureDto>();;

    // 결재 관련
    private SignatureDto writer;
    private SignatureDto reviewer;
    private SignatureDto approver;

    //내용
    private List<ContentDto> contents  = new ArrayList<ContentDto>();;
    
    //해임서
    private String dismissalTitle;
    private String dismissalSubtitle;
}
