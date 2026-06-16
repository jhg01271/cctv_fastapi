package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.Data;

import java.util.List;

@Data
public class CompVO extends BaseVO {
    @Schema(description = "순서", example = "")
    private int ordSeq;
    @Schema(description = "사업장명", example = "")
    private String compNm;
    @Schema(description = "대표자", example = "김일주")
    private String rpstNm;
    @Schema(description = "사업자등록번호", example = "111-11-11111")
    private String rgstNo;
    @Schema(description = "법인등록번호", example = "")
    private String corpNo;
    @Schema(description = "업종코드", example = "")
    private String bizCd;
    @Schema(description = "업종", example = "")
    private String bizNm;
    @Schema(description = "공사코드", example = "")
    private String constClassCd;
    @Schema(description = "공사명", example = "")
    private String constClassNm;
    @Schema(description = "이메일주소", example = "")
    private String email;
    @Schema(description = "전화번호", example = "")
    private String telNo;
    @Schema(description = "Fax", example = "")
    private String faxNo;
    @Schema(description = "우편번호", example = "")
    private String zipCd;
    @Schema(description = "주소1", example = "")
    private String addrs1;
    @Schema(description = "주소2", example = "")
    private String addrs2;
    @Schema(description = "국가코드", example = "")
    private String natnCd;
    @Schema(description = "은행코드", example = "")
    private String bankCd;
    @Schema(description = "계좌번호", example = "")
    private String bankNo;
    @Schema(description = "회사설명", example = "")
    private String desc;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "최대 접속 인원", example = "9999")
    private int maxHrCount;

    //매핑 고객사
    @Schema(description = "고객사 리스트")
    private List<SpSearchVO> clntList;
}
