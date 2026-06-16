package kr.co.igns.framework.config;

import kr.co.igns.system.setting.utils.interceptor.AuditInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config
 * @ 파일명		: webConfig.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Configuration
@Log4j2
public class webConfig implements WebMvcConfigurer {
    private final AuditInterceptor auditInterceptor;

    public webConfig(AuditInterceptor auditInterceptor) {
        this.auditInterceptor = auditInterceptor;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
    	
        return new WebMvcConfigurer() {
        	@Override
			public void addCorsMappings(CorsRegistry registry) {
        		log.info("WebMvcConfigurer...");
				registry.addMapping("/**")
					.allowedOrigins("*")
					.allowedMethods("HEAD" , "GET" ,"POST" , "PUT" , "DELETE" , "PATCH");
//						.allowedHeaders("Comp_id","comp_id","Comp_Id","CompId","COMPID","compid", "Authorization", "Content-Type", "X-Requested-With");

			}


            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(auditInterceptor)
                        .addPathPatterns("/**");
            }
        };
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
