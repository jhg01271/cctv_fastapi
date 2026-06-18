package kr.co.igns.system.user.model;

import lombok.Data;

@Data
public class VerificationVO {
    private String certificationToken;
    private String certificationCode;
}
