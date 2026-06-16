package kr.co.igns.system.setting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ClientVO extends BaseVO {
    @Schema(description = "고객번호", example = "202408200001")
    private String clntId;
    @Schema(description = "고객번호(저장용)", example = "202408200001")
    private String clntIdTemp;
    @Schema(description = "고객명", example = "(주)일주")
    private String clntNm;
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
    @Schema(description = "설명", example = "")
    private String desc;
    @Schema(description = "사용유무", example = "N")
    private String useYn;

}
