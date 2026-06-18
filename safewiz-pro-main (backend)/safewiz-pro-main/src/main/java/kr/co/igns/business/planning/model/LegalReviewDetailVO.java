package kr.co.igns.business.planning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Setter
@Data
public class LegalReviewDetailVO extends LegalReviewVO{
    @Schema(description = "작성년도", example = "")
    private String writeYear;

    @Schema(description = "문서 타입", example = "")
    private String docType;

    @Schema(description = "문서 번호", example = "")
    private String docNo;

    @Schema(description = "문서 시퀀스", example = "")
    private String docSeq;

    @Schema(description = "관련 법규 작성년도", example = "")
    private String legalWriteYear;

    @Schema(description = "관련 법규 문서 타입", example = "")
    private String legalDocType;

    @Schema(description = "관련 법규 문서 번호", example = "")
    private String legalDocNo;

    @Schema(description = "관련 법규 문서 시퀀스", example = "")
    private String legalDocSeq;
    
    @Schema(description = "관련 법규명", example = "")
    private String legalNm;
    
    @Schema(description = "관련 법규조항", example = "")
    private String legalArticleNm;
    
    @Schema(description = "제정/개정 일자", example = "")
    private String revisionAt;

    @Schema(description = "현행 법규 내용", example = "")
    private String currentlaws;

    @Schema(description = "해당 시설", example = "")
    private String facility;

    @Schema(description = "조직", example = "")
    private String orgnId;

    @Schema(description = "조직명", example = "")
    private String orgnNm;
    
    @Schema(description = "비고", example = "")
    private String remarkDc;
    
    @Schema(description = "사용 여부", example = "")
    private String useYn;
    
    @Schema(description = "cmd", example = "")
    private String cmd;
    
    @Schema(description = "checkYn", example = "")
    private String checkedYn;

    @Schema(description = "법규 검토서 사용 조직", example = "")
    private List<LegalReviewDetailOrgnVO> legalReviewOrgnList;

}
