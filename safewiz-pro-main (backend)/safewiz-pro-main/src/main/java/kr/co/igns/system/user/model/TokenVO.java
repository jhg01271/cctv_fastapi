package kr.co.igns.system.user.model;

import kr.co.igns.system.common.code.YesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenVO {
    private String uid;
    private String refreshToken;
    private String expirationPeriod;
    private String delYn;
    private String ip;


    private String userId;
    private String token;
    private String tokenNm;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
}
