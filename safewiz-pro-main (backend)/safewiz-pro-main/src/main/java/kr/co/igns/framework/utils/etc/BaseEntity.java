package kr.co.igns.framework.utils.etc;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	@Comment("등록일시")
	@CreationTimestamp
	@Column(insertable = true , updatable = false)
    private LocalDateTime createdAt;
	
	@Comment("등록자")
	@Column(insertable = true , updatable = false)
    private String createdBy;
	
	public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

	@Comment("수정일시")
	@UpdateTimestamp
	@Column(insertable = false , updatable = true)
    private LocalDateTime updatedAt;
	
	@Comment("수정자")
	@Column(insertable = false , updatable = true)
    private String updatedBy;
	
	public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}