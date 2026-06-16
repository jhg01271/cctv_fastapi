package kr.co.igns.framework.config.swagger;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.framework.config.swagger
 * ※ 파일명 : Swagger2Config.java
 * ※ 기능명 :
 * ※ 작성자 : 
 * ※ 최초생성일 : 2024. 9. 19.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * SecuritySchems "Bearer " 로 변경
 * </pre>
 */
@Configuration
public class Swagger2Config {
	
	@Bean
	public GroupedOpenApi openapi_safewiz_setting() {
		return GroupedOpenApi.builder().group("0. SAFEWIZPRO 시스템관리").pathsToMatch("/system/setting/**").build();
	}
	
	@Bean
	public GroupedOpenApi openapi_safewiz_base() {
		return GroupedOpenApi.builder().group("1. SAFEWIZPRO 기준관리").pathsToMatch("/system/base/**").build();
	}
	
	@Bean
	public GroupedOpenApi openapi_safewiz_business() {
		return GroupedOpenApi.builder().group("2. SAFEWIZPRO 서비스").pathsToMatch("/safewizpro/**").build();
	}

	@Bean
	public List<GroupedOpenApi> apis() {
		return Arrays.asList(openapi_safewiz_setting(), openapi_safewiz_base(), openapi_safewiz_business());
	}

	@Bean
	public OpenAPI customOpenAPI() {
		SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
				.bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER).name("Authorization");
		SecurityRequirement schRequirement = new SecurityRequirement().addList("Bearer ");

		return new OpenAPI()
				.info(new Info().title("ILJOO-GNS SAFEWIZ PRO REST API").description("(주)일주지앤에에스 SAFEWIZ-PRO REST API 정보입니다.")
						.version("v0.1.1"))
				.components(new Components().addSecuritySchemes("Bearer ", securityScheme))
				.security(Arrays.asList(schRequirement))
				.servers(Arrays.asList(
						new Server().url("http://localhost:9000").description("로컬 서버"),
	                    new Server().url("https://fems.i-gns.co.kr").description("운영 서버"),	                    
	                    new Server().url("https://meta.i-gns.co.kr").description("메타 서버")	                    
	            ));

	}
}
