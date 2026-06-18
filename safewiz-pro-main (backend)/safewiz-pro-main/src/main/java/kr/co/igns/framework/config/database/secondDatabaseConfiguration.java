//package kr.co.igns.framework.config.database;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
///**
// * 
// * @ 프로젝트   	: igns
// * @ 패키지		: kr.co.igns.framework.config.database
// * @ 파일명		: secondDatabaseConfiguration.java
// * @ 기능명 		: 
// * @ 작성자 		: 김성준
// * @ 최초생성일 	: 2022. 4. 13.
// */
//
//@Configuration 
//@MapperScan(value="최상위 패키지 경로", basePackages="kr.co.igns.**.dao.db2", sqlSessionFactoryRef="secondSqlSessionFactory") //dao 위치확인
//@EnableTransactionManagement
//public class secondDatabaseConfiguration {
//	
//	// 데이터소스를 반환하는 빈을 선언합니다.
//    @Bean(name="secondDataSource")
//    @Qualifier("second")
//    @ConfigurationProperties(prefix="spring.datasource.hikari.second")
//    public DataSource secondDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//	//secondDB
//	@Bean(name="secondSqlSessionFactory")
//    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception{
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));        
//        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/db2/**/*Mapper.xml")); //mapper 위치 확인
//        
//        return sessionFactory.getObject();
//    }
//
//	@Bean(name = "secondSqlSessionTemplate") 
//	public SqlSessionTemplate secondSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory secondSqlSessionFactory) throws Exception { 
//		return new SqlSessionTemplate(secondSqlSessionFactory); 
//	}
//}