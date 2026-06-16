package kr.co.igns.system.user.entity;

import kr.co.igns.system.common.code.YesNo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user_token", schema="esg")
public class TokenEntity {

    @EmbeddedId
    private TokenKey tokenKey;
    @Column
    private String expirationPeriod;
    @Column
    private String ip;
    @Comment("삭제여부")
    @Column(columnDefinition = "varchar(3) default 'N'")
    @NotNull
    private YesNo delYn;
}
