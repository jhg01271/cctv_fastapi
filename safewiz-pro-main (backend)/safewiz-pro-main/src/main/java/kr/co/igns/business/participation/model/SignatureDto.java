package kr.co.igns.business.participation.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class SignatureDto extends BaseVO{
    private String hrId;
    private String hrNm;
    private String orgnNm;
    private String jbrpNm;
    private String signature;
    private String targetId;
    private String targetType;
    private String param;
    private String logoId;
    private String partCompNm;
}
