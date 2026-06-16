package kr.co.igns.system.user.model;

import lombok.Data;

@Data
public class PasswordVO {
    private String userId;
    private String password;
    private String newPassword;
}
