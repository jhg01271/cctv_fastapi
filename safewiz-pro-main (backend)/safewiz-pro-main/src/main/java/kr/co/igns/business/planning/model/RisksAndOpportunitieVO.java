package kr.co.igns.business.planning.model;

import java.io.InputStream;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class RisksAndOpportunitieVO extends BaseVO {
	
	//main
	@Schema(description = "사업장ID", example = "202408200001")
    private String compId;
	
	@Schema(description = "작성년도", example = "2024")
    private String writeYear;
	
	@Schema(description = "작성년도(param용)", example = "2024")
	private String searchText;
	
	@Schema(description = "문서타입", example = "RAO")
    private String docType;
	
	@Schema(description = "문서번호", example = "202401010001")
    private String docNo;
	
	@Schema(description = "조직", example = "202401010001")
    private String orgnId;
	
	@Schema(description = "조직명", example = "대외사업부")
    private String orgnNm;
	
	@Schema(description = "사용여부", example = "Y")
    private String useYn;
	
	@Schema(description = "리스크건수", example = "3")
    private String riskDetailCount;
	
	@Schema(description = "기회건수", example = "3")
    private String oppDetailCount;
	
	@Schema(description = "참여자명수", example = "4")
    private String parDetailCount;
	
	@Schema(description = "등록일자", example = "20240101")
    private String createdAt;
	
	@Schema(description = "등록자", example = "userId")
    private String createdBy;

	@Schema(description = "수정일자", example = "20240101")
    private String updatedAt;

	@Schema(description = "수정자", example = "userId")
    private String updatedBy;
	
	//risk
	@Schema(description = "문서시퀀스", example = "00001")
    private String riskDocSeq;
	
	@Schema(description = "조직구분(이해관계자)", example = "202408200001")
    private String riskOrgn;
	
	@Schema(description = "리스크 내용", example = "리스크 내용")
    private String riskDesc;
	
	@Schema(description = "현재 가능성", example = "1")
    private String presentPos;
	
	@Schema(description = "현재 심각성", example = "1")
    private String presentSev;
	
	@Schema(description = "현재등급", example = "S")
    private String presentLevel;
	
	@Schema(description = "조치후 등급", example = "S")
    private String measureLevel;
	
	@Schema(description = "조치계획내용", example = "조치계획내용")
    private String riskMeasureDesc;
	
	@Schema(description = "조치예정일", example = "20240820")
    private String riskMeasureStDt;
	
	@Schema(description = "조치완료일", example = "20240820")
    private String riskMeasureEdDt;
	
	@Schema(description = "조치후 가능성", example = "1")
    private String measurePos;
	
	@Schema(description = "조치후 심각성", example = "1")
    private String measureSev;
	
	@Schema(description = "사용여부", example = "Y")
    private String riskUseYn;
	
	//opp
	@Schema(description = "문서시퀀스", example = "00001")
    private String oppDocSeq;
	
	@Schema(description = "조직구분(이해관계자)", example = "202408200001")
    private String oppOrgn;
	
	@Schema(description = "기회내용", example = "기회내용")
    private String oppDesc;
	
	@Schema(description = "관심도", example = "1")
    private String oppConcern;
	
	@Schema(description = "중요도", example = "1")
    private String oppImportance;
	
	@Schema(description = "영향도", example = "1")
	private String oppInfluence;
	
	@Schema(description = "기회등급", example = "S")
	private String oppLevel;
	
	@Schema(description = "조치계획내용", example = "조치계획내용")
    private String oppMeasureDesc;
	
	@Schema(description = "조치예정일", example = "20240820")
    private String oppMeasureStDt;
	
	@Schema(description = "조치완료일", example = "20240820")
    private String oppMeasureEdDt;
	
	@Schema(description = "조치결과 효과성평가", example = "조치결과 효과성평가")
	private String measureResultEffect;
	
	@Schema(description = "사용여부", example = "Y")
    private String oppUseYn;
	
	//par
	@Schema(description = "문서시퀀스", example = "00001")
    private String parDocSeq;
	
	@Schema(description = "담당업무", example = "담당업무")
	private String duties;
	
	@Schema(description = "참여자 성명", example = "성명")
	private String hrId;
	private String hrNm;
	private String leader;
	
	@Schema(description = "사용여부", example = "Y")
    private String parUseYn;
	
	@Schema(description = "서명상태", example = "Y")
	private String approvalStatus;
	
	//저장 플래그
	@Schema(description = "저장플래그", example = "")
    private String type;
	
	// 리포트 출력용
	private String no;
	private String signature;
	private InputStream signatureImg;
	
	//평가기준표 api용
	@Schema(description = "내용1", example = "")
	private String content1;
	@Schema(description = "내용2", example = "")
	private String content2;
	@Schema(description = "내용3", example = "")
	private String content3;
	@Schema(description = "내용4", example = "")
	private String content4;
	@Schema(description = "detail메뉴", example = "P,S,RL...")
	private String detailType;
	@Schema(description = "detail시퀀스", example = "00001...")
	private String detailSeq;
	
	private int index;
	private String noCheckPrint;
}
