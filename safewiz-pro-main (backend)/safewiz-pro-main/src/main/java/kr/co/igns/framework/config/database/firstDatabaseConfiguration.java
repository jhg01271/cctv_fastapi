package kr.co.igns.framework.config.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.database
 * @ 파일명		: firstDatabaseConfiguration.java
 * @ 기능명 		: 
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Configuration 
@MapperScan(value="kr.co.igns", basePackages="kr.co.igns.**.dao.postgres", sqlSessionFactoryRef="sqlSessionFactory")  //dao 위치확인
@EnableTransactionManagement
public class firstDatabaseConfiguration {
	
	
	// 데이터소스를 반환하는 빈을 선언합니다. 메인으로 사용할 DB에 @Primary 어노테이션 사용 
    @Bean(name="firstDataSource")
    @Qualifier("first")
    @Primary 
    @ConfigurationProperties(prefix="spring.datasource.hikari.first")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().build();
    }

	//firstDB
	@Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("firstDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));        
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/postgres/**/*Mapper.xml"));  //mapper 위치확인
        
        return sessionFactory.getObject();
    }

	@Bean(name = "firstSqlSessionTemplate") 
	@Primary
	public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception { 
		return new SqlSessionTemplate(sqlSessionFactory); 
	}
	
    
    @Bean 
    public PlatformTransactionManager transactionManager() throws URISyntaxException, GeneralSecurityException, ParseException, IOException { 
    	return new DataSourceTransactionManager(firstDataSource()); 
	}

}
