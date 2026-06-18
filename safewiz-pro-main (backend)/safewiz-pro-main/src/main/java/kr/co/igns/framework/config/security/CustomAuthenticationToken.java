package kr.co.igns.framework.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Object additionalInfo;  // 추가 필드

    public CustomAuthenticationToken(Object principal, Object credentials, Object additionalInfo) {
        super(principal, credentials);
        this.additionalInfo = additionalInfo;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Object additionalInfo) {
        super(principal, credentials, authorities);
        this.additionalInfo = additionalInfo;
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    //키값으로 value 가져오는 함수
    //saveAuthentication에서 값 설정
    //TODO: 코드개선 필요
    public Object getAdditionalInfoBykey(String keyToFind){
        List<Map<String, Object>> getAdditionalInfo = (List<Map<String, Object>>)additionalInfo;
        Object findValue = getAdditionalInfo.stream()
                .filter(map -> keyToFind.equals(map.get("key")))
                .map(map -> map.get("value"))
                .findFirst()
                .orElse(null);
        return findValue;
    }
}