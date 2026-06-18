package kr.co.igns.business.improvement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class NearMissReportVO extends BaseVO {
	
	@Schema(description = "아차사고 보고서 ID", example="작성 연/월/일 + 순서 | YYYYMMDD0001")
    private String reportId;
	
	@Schema(description = "년도(Key)", example = "2024")
    private String writeYear;

	@Schema(description = "문서구분(Key)", example = "")
    private String docType;
	
	@Schema(description = "문서번호(Key)", example = "00001")
    private String docNo;
	
	@Schema(description = "사업장 ID", example = "")
    private String compId;
	
	@Schema(description = "작성자 ID", example = "")
    private String hrId;
	
	@Schema(description = "작성자 명", example = "")
    private String hrNm;
	
	@Schema(description = "작성자 조직 ID", example = "")
    private String creatOrgnId;
	
	@Schema(description = "작성자 조직 명", example = "")
    private String creatOrgnNm;
	
	@Schema(description = "조치자 조직 ID", example = "")
    private String actionOrgnId;
	
	@Schema(description = "조치자 조직 명", example = "")
    private String actionOrgnNm;
	
	@Schema(description = "아차사고 내용", example = "")
    private String accidentContent;
	
	@Schema(description = "아차사고 개선대책 내용", example = "")
    private String accidentImproved;
	
	@Schema(description = "유해위험발굴 내용", example = "")
    private String dangerContent;
	
	@Schema(description = "유해위험발굴 개선대책 내용", example = "")
    private String dangerImproved;
	
	@Schema(description = "조치결과 내용", example = "")
    private String actionContent;
	
	@Schema(description = "작성일자", example = "")
    private String writeDt;
	
	@Schema(description = "접수일자", example = "")
    private String receiptDt;
	
	@Schema(description = "조치일자", example = "")
    private String actionDt;
	
	@Schema(description = "사용여부", example = "")
    private String useYn;
	
	@Schema(description = "생성일자", example = "")
    private String createdAt;
	
	@Schema(description = "생성자 ID", example = "")
    private String createdBy;
	
	@Schema(description = "수정일자", example = "")
    private String updatedAt;
	
	@Schema(description = "수정자 ID", example = "")
    private String updatedBy;
	
	@Schema(description = "조직 헤드의 ID", example = "조치 조직 선택시 자동으로 해당 조지조직의 HEAD를 사인 컴포넌트의 조치조직 조직장에 값을 삽입하기 위함")
    private String orgnHeadId;

	@Schema(description = "조직 헤드의 NM", example = "조치 조직 선택시 자동으로 해당 조지조직의 HEAD를 사인 컴포넌트의 조치조직 조직장에 값을 삽입하기 위함")
    private String orgnHeadNm;

}
