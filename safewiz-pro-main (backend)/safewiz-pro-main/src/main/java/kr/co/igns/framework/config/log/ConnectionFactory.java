package kr.co.igns.framework.config.log;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.log
 * @ 파일명		: ConnectionFactory.java
 * @ 기능명 		: 로그 기록을 데이터베이스에 남기기 위한 클래스
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 11. 09.
 */

public class ConnectionFactory {
   
	private static interface Singleton {
        final ConnectionFactory INSTANCE = new ConnectionFactory();
    }

	@Value("${logdb.username}")
	private String DB_USER_NAME;

	@Value("${logdb.password}")
	private String DB_PASSWORD;
	
	@Value("${logdb.jdbc-url}")
	private String JDBC_URL;
	
	private final DataSource dataSource;
	 
    private ConnectionFactory() {
        Properties properties = new Properties();
        properties.setProperty("user", DB_USER_NAME);
        properties.setProperty("password", DB_PASSWORD);
 
        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(JDBC_URL, properties);
        new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);
        this.dataSource = new PoolingDataSource(pool);
    }
 
    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}