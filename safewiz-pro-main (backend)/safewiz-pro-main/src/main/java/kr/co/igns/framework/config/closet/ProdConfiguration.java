package kr.co.igns.framework.config.closet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;

import kr.co.igns.framework.config.log.LogUtil;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.closet
 * @ 파일명		: ProdConfiguration.java
 * @ 기능명 		: spring.profiles.active에 따른 실행
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Configuration
@Profile("prod")
public class ProdConfiguration {
	@Bean
	public ClosetService devService() {
		String frame_version = org.springframework.core.SpringVersion.getVersion();
		LogUtil.ConsoleLogInfo(HttpStatus.OK, "[[ Spring Framework Version ]]", frame_version, null);
		LogUtil.ConsoleLogInfo(HttpStatus.OK, "[[ PROD ]]", "", null);
		return new ClosetService("prod");
	}
}
