package kr.co.igns;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import kr.co.igns.framework.config.database.firstDatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ 프로젝트 : igns
 * @ 패키지 : com.web.framework.igns
 * @ 파일명 : IgnsApplication.java
 * @ 기능명 : 프레임워크 기본 설정
 * @ 작성자 : 김성준
 * @ 최초생성일 : 2020. 9. 10.
 */

@EnableCaching
@SpringBootApplication
@Import(firstDatabaseConfiguration.class)
@EnableScheduling // 스케줄러 사용
@Slf4j
public class IgnsApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(IgnsApplication.class);
	}

	public static void main(String[] args) {
		// properties 파일을 사용할 시 아래 주석을 해제하여 사용하세요.
		SpringApplication.run(IgnsApplication.class, args);

		// yml 파일을 사용할 시 아래 주석을 해제하여 사용하세요. (청주산단)
//		String separator = File.separator;
//		new SpringApplicationBuilder(IgnsApplication.class)
//				.properties(
//						"spring.config.location=" +
//								"classpath:" + separator + "application.yml" +
//								", optional:file:" + separator + "app" + separator + "application.yml")
//				.run(args);

		log.info("user.dir="+System.getProperty("user.dir"));
		log.info("Logger Implementation: {}", org.slf4j.LoggerFactory.getLogger("jdbc.sqlonly").getClass().getName());
		log.info("LOG4J2 CONFIG FILE: " + System.getProperty("log4j.configurationFile"));
		log.info("==================== Started SafeWiz Pro ====================");
	}

}
