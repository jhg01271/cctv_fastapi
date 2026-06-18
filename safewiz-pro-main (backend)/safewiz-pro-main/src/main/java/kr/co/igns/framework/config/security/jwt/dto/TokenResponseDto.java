package kr.co.igns.framework.config.security.jwt.dto;

import lombok.Data;

@Data
public class TokenResponseDto {
    private String userId;
    private String uid;
    private String role;
    private String name;
}
