package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class HrVO extends BaseVO {
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "사용자 비밀번호")
    private String userPs;
    @Schema(description = "사용자명")
    private String userNm;
    @Schema(description = "인원 ID")
    private String hrId;
    @Schema(description = "성명")
    private String hrNm;
    @Schema(description = "성별")
    private String sexDiv;
    @Schema(description = "생년월일")
    private String birthDt;
    @Schema(description = "사원번호")
    private String sabun;
    @Schema(description = "직책/직위")
    private String jbrp;
    @Schema(description = "직책/직위")
    private String jbrpNm;
    @Schema(description = "위험성평가 담당")
    private String riskAssRole;
    @Schema(description = "위험성평가 담당명")
    private String riskAssRoleNm;
    @Schema(description = "조직번호")
    private String orgnId;
    @Schema(description = "조직명")
    private String orgnNm;
    @Schema(description = "협력사번호")
    private String partCompId;
    @Schema(description = "협력사명")
    private String partCompNm;
    @Schema(description = "입사일")
    private String workingAt;
    @Schema(description = "퇴사일")
    private String fireAt;
    @Schema(description = "이메일주소")
    private String email;
    @Schema(description = "폰번호")
    private String phone;
    @Schema(description = "서명")
    private String signature;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사업장 List(조회용,저장용)")
    private List<BaseMapVO> compList;
    @Schema(description = "사용유무", example = "N")
    private YesNo useYn;
    @Schema(description = "정렬순서")
    private int ordSeq;

    @Schema(description = "직위 정렬순서")
    private int jbrpSeq;
    @Schema(description = "우편번호")
    private String zipCd;
    @Schema(description = "주소1" )
    private String addrs1;
    @Schema(description = "주소2" )
    private String addrs2;

    private String role;

    //인원별 조직 이력
    @Schema(description = "조직 변경 시작일")
    private String stDate;
    @Schema(description = "조직 변경 종료일")
    private String edDate;
    @Schema(description = "변경 전 조직 ID")
    private String oldOrgnId;

    //협력사 인원 매핑 조회용
    @Schema(description = "인원 매핑테이블 구분")
    private String gubun;

    //인원별 사업장
    @Schema(description = "사업장 ID")
    private String compId;    
    @Schema(description = "사업장 명")
    private String compNm;
    
    @Schema(description = "소속 사업장의 사업장 ID")
    private String affCompId;
    @Schema(description = "소속 사업장의 사업장 명")
    private String affCompNm;
    @Schema(description = "관리 사업장의 사업장 ID")
    private String mgrCompId;
    @Schema(description = "관리 사업장의 사업장 명")
    private String mgrCompNm;
    @Schema(description = "인원의 소속 사업장 'Y', 관리 사업장 'N'")
    private String orgnCompYn;

    @Schema(description = "target_id")
    private String targetId;

    //region 카드 이미지 처리
    @Schema(description = "card의 logo")
    private String logoId;

    //baseSelectPopup 데이터 가공용 처리
    @Schema(description = "custom name")
    private String customHrNm;
    
    private String clntId;

    @Schema(description = "소속 사업장의 총 인원")
    private int hrCount;

    @Schema(description = "소속 사업장의 접속 가능 인원")
    private int maxHrCount;

//    @Schema(description = "card의 Thumbnail")
//    private String thumbnailId;
//    @Schema(description = "card의 정")
//    private String primaryId;
//    @Schema(description = "card의 부")
//    private String secondaryId;
    //endregion

}
