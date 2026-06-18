package kr.co.igns.system.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class TokenKey implements Serializable {
    @Column(name="uid", length=36)
    private String uid;
    @Column(name="refreshToken", length=500)
    private String refreshToken;

}
