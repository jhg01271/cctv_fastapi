package kr.co.igns.business.improvement.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import lombok.Data;

@Data
public class IncidentReportVO extends BaseVO {
	
	@Schema(description = "재해발생 보고서 ID", example="작성 연/월/일 + 순서 | YYYYMMDD0001")
    private String reportId;
	
	@Schema(description = "년도(Key)", example = "2024")
    private String writeYear;

	@Schema(description = "문서구분(Key)", example = "")
    private String docType;
	
	@Schema(description = "문서번호(Key)", example = "00001")
    private String docNo;
	
	@Schema(description = "사업장 ID", example = "")
    private String compId;
	
	@Schema(description = "사고자 ID", example = "")
    private String incidentPersonId;
	
	@Schema(description = "사고자 명", example = "")
    private String incidentPersonNm;
	
	@Schema(description = "목격자 ID", example = "")
    private String witnessPersonId;
	
	@Schema(description = "목격자 명", example = "")
    private String witnessPersonNm;
	
	@Schema(description = "사고발생일자", example = "YYYYMMDD")
    private String incidentDt;
	
	@Schema(description = "사고발생시간", example = "")
    private String incidentTm;
	
	@Schema(description = "사고발생장소", example = "")
    private String incidentLocation;
	
	@Schema(description = "사고정도 및 상병명", example = "")
    private String incidentExtent;
	
	@Schema(description = "사업장 의견", example = "")
    private String compOpinion;
	
	@Schema(description = "사고처리 구분", example = "W:산재, I:공상")
    private String incidentType;
	
	@Schema(description = "사고이력", example = "")
    private String incidentHis;
		
	//@Schema(description = "사용여부", example = "")
    //private String useyn;
	
	@Schema(description = "생성일자", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정일자", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;

	@Schema(description = "사고조직 ID", example = "")
    private String orgnId;
	
	@Schema(description = "사고조직 명", example = "")
    private String orgnNm;
	
	@Schema(description = "입원일", example = "")
    private String hospIn;
	
	@Schema(description = "통원일", example = "")
    private String hospOut;
	
	@Schema(description = "사고경위", example = "")
    private String incidnetDetail;
	
	@Schema(description = "병원명", example = "")
    private String hospitalNm;
	
	@Schema(description = "병원전화", example = "")
    private String hospitalTel;

	@Schema(description = "병원위치", example = "")
    private String hospitalPl;
	
	@Schema(description = "입원시작", example = "")
    private String hospInfrDt;
	
	@Schema(description = "입원종료", example = "")
    private String hospIntoDt;
	
	@Schema(description = "통원시작", example = "")
    private String hospOutfrDt;
	
	@Schema(description = "통원종료", example = "")
    private String hospOuttoDt;
	
	@Schema(description = "사고경위", example = "")
    private String incidentDetail;

	@Schema(description = "예방대책", example = "")
    private String preventiveMeasures;
	
	@Schema(description = "관리감독자의견", example = "")
    private String supervisorsOpinion;
	
	@Schema(description = "직책", example = "")
    private String jbrpNm;
	
	@Schema(description = "생년월일", example = "")
    private String birthDt;
	
	@Schema(description = "전화번호", example = "")
    private String phone;
	
	@Schema(description = "입사일", example = "")
    private String workingAt;
	
	@Schema(description = "주소", example = "")
    private String addrs;
	
	@Schema(description = "본사/협력사구분", example = "")
    private String partComp;
	
	@Schema(description = "직책", example = "")
    private String wjbrpNm;
	
	@Schema(description = "생년월일", example = "")
    private String wbirthDt;
	
	@Schema(description = "전화번호", example = "")
    private String wphone;
	
	@Schema(description = "입사일", example = "")
    private String wworkingAt;
	
	@Schema(description = "주소", example = "")
    private String waddrs;
	
	@Schema(description = "본사/협력사구분", example = "")
    private String wpartComp;
	
	@Schema(description = "사고자/목격자구분", example = "")
    private String statementType;
	
	@Schema(description = "작성일", example = "")
    private String writeDt;
	
	@Schema(description = "진술내역", example = "")
    private String statementContent;
	
	@Schema(description = "목격자조직 ID", example = "")
    private String worgnId;
	
	@Schema(description = "목격자조직 명", example = "")
    private String worgnNm;
	
	@Schema(description = "문서순번", example = "")
    private String docSeq;
	
	@Schema(description = "사용유무", example = "")
    private String useYn;
	
	@Schema(description = "사고자 사번", example = "")
    private String sabun;

	@Schema(description = "목격자 사번", example = "")
    private String wsabun;	
	
	@Schema(description = "사고처리 구분", example = "")
    private String incidentTypenm;	
	
	@Schema(description = "첨부파일 ID", example = "")
    private String fileId;
	
	@Schema(description = "첨부파일 ID", example = "")
    private String targetId;
	
	@Schema(description = "진술서 고정문구_2", example = "")
    private String wStaticComment;
	
	@Schema(description = "진술서 고정문구_1", example = "")
    private String StaticComment;
	
	@Schema(description = "사고자 이미지 Id", example = "")
    private String ifileId;
	
	@Schema(description = "목격자 이미지 Id", example = "")
    private String wfileId;
	
	@Schema(description = "예방대책 및 관리자의견", example = "")
    private String incidentPmSo;
	
	@Schema(description = "서명 사고자/목격자 분류", example = "incidentsign")
	private String type;
	
	
}

