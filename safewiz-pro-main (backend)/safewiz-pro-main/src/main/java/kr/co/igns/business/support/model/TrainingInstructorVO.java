package kr.co.igns.business.support.model;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TrainingInstructorVO extends BaseVO {
    @Schema(description = "강사 번호")
    private String InstructorId;
    @Schema(description = "강사", example = "John Doe")
    private String InstructorNm;
    @Schema(description = "직책/직위")
    private String jbrp;
    @Schema(description = "직책/직위")
    private String jbrpNm;
    @Schema(description = "성별")
    private String sexDiv;
    @Schema(description = "생년월일")
    private String birthDt;
    @Schema(description = "이메일주소")
    private String email;
    @Schema(description = "폰번호")
    private String phone;
    @Schema(description = "우편번호")
    private String zipCd;
    @Schema(description = "주소1" )
    private String addrs1;
    @Schema(description = "주소2" )
    private String addrs2;
    @Schema(description = "비고")
    private String remark;
    @Schema(description = "사용 여부", example = "Y")
    private YesNo useYn;
}
