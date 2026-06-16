package kr.co.igns.system.setting.model;

import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class AuditVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7307093007312440342L;

	private Long 				seq;

	private String				referer;

	private String				url;

	private String				param;

	private String				userLocale;

	private String				localName;

	private String				os;

	private String				ipAddress;
	
	private String				browser;
	
	private String				userAgent;
	
	private String				sessionId;

	private String				userId;

}
