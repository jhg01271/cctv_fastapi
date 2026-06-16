package kr.co.igns.system.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ANONYMOUS("ROLE_ANONYMOUS"), MASTER("ROLE_MASTER"), MANAGER("ROLE_MANAGER"), USER("ROLE_USER");

    private final String key;
    
    public static Role fromKey(String key) {
        for (Role role : Role.values()) {
            if (role.getKey().equals("ROLE_" + key)) {
                return role;
            }else if (role.getKey().equals(key)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role key: " + key);
    }
}
 