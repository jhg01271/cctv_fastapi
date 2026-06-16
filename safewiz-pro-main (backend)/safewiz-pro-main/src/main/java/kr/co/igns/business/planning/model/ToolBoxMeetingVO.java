package kr.co.igns.business.planning.model;

import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToolBoxMeetingVO extends BaseVO {
	private int cnt;
//	private String cmd;
	private String tbmYear;
	private String tbmMonth;
	private String writeYear;
//	private String docType;
//	private String docNo;
//	private String compId;
	private String tbmDate;
	private String tbmTime;
	private String tbmTimeStt;
	private String tbmTimeEnd;
	private String sameDateYn;
	private String workNm;
	private String workDesc;
	private String workPlace;
	private String riskYn;
	private String checkResult;
	private String workEndMeeting;
	private String useYn;
	private String leader;
	private String attend;
	
//	private String docSeq;
	private String risk;
	private String measures;
	
	private String potentialRiskSeq;
	
	private String actionYn;
	private String action;
	
	private String hrNm;
	private String orgnNm;
	private String jbrpNm;
	
	private String attend1;
	private String attend2;
	private String attend3;
	private InputStream attendSign1;
	private InputStream attendSign2;
	private InputStream attendSign3;
	private String attendSignTime1;
	private String attendSignTime2;
	private String attendSignTime3;
	
	private String userId;


	//위험성평가 선택
	private String riskWriteYear;
	private String riskDocType;
	private String riskDocNo;
	private String riskNm;
	private String riskAllowanceStandards;
	private String cpemWriteYear;
	private String cpemDocNo;

	//공정 선택
	private String processId;
	private String processNm;
	private String revNo;
	
//	private String subPage;
//	private Integer page;
//	private Integer localPage;
}