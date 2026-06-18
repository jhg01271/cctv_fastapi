package kr.co.igns.system.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

import java.security.Timestamp;

@Data
public class BaseMapVO extends BaseVO {
    @Schema(description = "target타입(시스템코드)", example = "orgn/workplace/process/equipment")
    private String targetType;
    @Schema(description = "targetID(target타입의 Key값)")
    private String targetId;
    @Schema(description = "id", example = "202408200001")
    private String id;
    @Schema(description = "nm")
    private String nm;
    @Schema(description = "구분", example = "시스템코드값 입력")
    private String gubun;
    @Schema(description = "사용유무", example = "Y")
    private YesNo useYn;

    public BaseMapVO() {
        // 기본 생성자
    }
    public BaseMapVO(String targetType, String targetId, String id, String gubun, String userNm){
        this.targetType= targetType;
        this.targetId = targetId;
        this.id = id;
        this.gubun = gubun;
        this.useYn = YesNo.Y;
        this.setCreatedBy(userNm);
        this.setUpdatedBy(userNm);
    }

    public BaseMapVO(String targetType, String targetId, String id, String userNm){
        this.targetType= targetType;
        this.targetId = targetId;
        this.id = id;
        this.useYn = YesNo.Y;
        this.setCreatedBy(userNm);
        this.setUpdatedBy(userNm);
    }

    //조회 vo 용도
    public BaseMapVO(String targetType, String targetId){
        this.targetType= targetType;
        this.targetId = targetId;
        this.useYn = YesNo.Y;
    }

    //useYn 설정
    public void setUseYnByString(String yn){
        this.useYn= YesNo.valueOf(yn);
    }

}
