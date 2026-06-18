//package kr.co.igns.framework.api.notification.entity;
//
//import java.util.UUID;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.PrePersist;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//import org.hibernate.annotations.GenericGenerator;
//
//import kr.co.igns.framework.utils.etc.BaseEntity;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Entity
//@DynamicInsert // insert 시 null 필드 제외
//@DynamicUpdate // update 시 null 필드 제외
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "tb_notification_map")
//public class NotificationEntity extends BaseEntity {
//
//	@Id
//	@Comment("id")
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//	@Column(name = "uid", columnDefinition = "uuid DEFAULT public.uuid_generate_v1()")
//	private String uid;
//
//	@Comment("유저Id")
//	@Column(name = "user_id")
//	private String userId;
//
//	@Comment("알림방법")
//	@Column(name = "method")
//	private String method;
//
//	@Comment("알림사용여부")
//	@Column(name = "agreed")
//	private Boolean agreed;
//
//	@PrePersist
//	public void generateId() {
//		if (this.uid == null) {
//			this.uid = UUID.randomUUID().toString();
//		}
//	}
//
//	@Builder
//	public NotificationEntity(String userId, String method, Boolean agreed, String createdBy, String updatedBy) {
//		this.userId = userId;
//		this.method = method;
//		this.agreed = agreed;
//		setCreatedBy(createdBy);
//		setUpdatedBy(updatedBy);
//
//	}
//
//}
