package kr.co.igns.system.base.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class PartnerVO extends BaseVO {
    @Schema(description = "협력사 ID", example = "년월일순번")
    private String partCompId;
    @Schema(description = "공사와의 거래품목(서비스 내용)", example = "시스템코드(C0007)")
    private String partCompItem;
    @Schema(description = "공사와의 거래품목(서비스 내용)", example = "202505200001")
    private String partCompItemId;
    @Schema(description = "공사와의 거래품목(서비스 내용)명")
    private String partCompItemNm;
    @Schema(description = "공사와의 거래품목 비고")
    private String remark;
    @Schema(description = "업체명")
    private String partCompNm;
    @Schema(description = "대표자")
    private String rpstNm;
    @Schema(description = "정렬순서")
    private int ordSeq;
    @Schema(description = "사업자등록번호")
    private String rgstNo;
    @Schema(description = "법인등록번호" )
    private String corpNo;
    @Schema(description = "업종코드")
    private String bizCd;
    @Schema(description = "업종명" )
    private String bizNm;
    @Schema(description = "이메일주소")
    private String email;
    @Schema(description = "전화번호")
    private String telNo;
    @Schema(description = "Fax")
    private String faxNo;
    @Schema(description = "우편번호")
    private String zipCd;
    @Schema(description = "주소1" )
    private String addrs1;
    @Schema(description = "주소2" )
    private String addrs2;
    @Schema(description = "국가코드" )
    private String natnCd;
    @Schema(description = "은행코드" )
    private String bankCd;
    @Schema(description = "주요사업" )
    private String desc;
    @Schema(description = "협력사 안전보건관리책임자" )
    private String partShHrId;
    @Schema(description = "협력사 안전보건관리책임자" )
    private String partShHrNm;
    @Schema(description = "협력사 안전보건관리책임자 이미지 ID" )
    private String partShHrImgId;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "등록일자", example = "YYYYMMDD" )
    private String regDt;

    //인원 Mapping - 상세 조회용(조회 분리)
    @Schema(description = "본사담당자(정)")
    private List<HrVO> HrListH;
    @Schema(description = "본사담당자(부)")
    private List<HrVO> HrListS;
    @Schema(description = "협력사담당자(정)")
    private List<HrVO> partnerHrListH;
    @Schema(description = "협력사담당자(부)")
    private List<HrVO> partnerHrListS;

    //region 카드 이미지 처리
    @Schema(description = "card의 logo")
    private String logoId;
//    @Schema(description = "card의 Thumbnail")
//    private String partnerThumbnailId;
    @Schema(description = "card의 정")
    private String primaryId;
    @Schema(description = "card의 부")
    private String secondaryId;
    //endregion
}
