package kr.co.igns.business.comp.company.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.complex.model
 * ※ 파일명 : CompVO.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 14.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Data
public class CompVO extends BaseVO {

    private static final long serialVersionUID = -2417117617959565367L;


    @Schema(description = "멤버 순번", example = "1")
    private Long memSeq;

    @Schema(description = "FEMS 사업장 아이디", example = "사업장 아이디")
    private String compId;

    @Schema(description = "권한 순번", example = "1")
    private Long authSeq;

    @Schema(description = "사용자 아이디", example = "사용자 아이디")
    private String userId;

    @Schema(description = "FEMS 사업장 이름", example = "사업장 이름")
    private String compNm;

    @Schema(description = "FEMS 사업자 등록 번호", example = "사업자 등록 번호")
    private String rgstNo;

    @Schema(description = "FEMS 대표자", example = "대표자")
    private String rpstNm;

    @Schema(description = "FEMS 우편번호", example = "우편번호")
    private String zipCd;

    @Schema(description = "FEMS 주소", example = "주소")
    private String addrs1;

    @Schema(description = "FEMS 담당자", example = "담당자")
    private String chrgPrsn;

    @Schema(description = "FEMS 연락처", example = "연락처")
    private String telNo;

    @Schema(description = "FEMS 업종", example = "업종")
    private String bizNm;

    private String upCompId;

    private List<CompVO> children = new ArrayList<>();

}
