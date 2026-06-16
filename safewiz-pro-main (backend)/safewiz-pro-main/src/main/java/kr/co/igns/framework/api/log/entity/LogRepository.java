package kr.co.igns.framework.api.log.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long>, JpaSpecificationExecutor<LogEntity> {
	
    @Modifying
    @Query("DELETE FROM LogEntity WHERE logDate < ?1")
    int deleteInBulkByLogDate(String datetime);
    
    @Query("SELECT logLevel, count(logLevel) AS level_cnt FROM LogEntity " +
	       "WHERE logDate BETWEEN ?1 AND ?2 GROUP BY logLevel")
    List<Object[]> getLogPie(String startDatetime, String endDatetime);
    
    @Query("SELECT logLevel, COUNT(logLevel) AS level_cnt, TO_DATE(logDate, 'YYYY-MM-DD') FROM LogEntity " + 
			"WHERE logDate BETWEEN ?1 AND ?2 GROUP BY logLevel, TO_DATE(logDate, 'YYYY-MM-DD') ORDER BY TO_DATE(logDate, 'YYYY-MM-DD') ASC")
     List<Object[]> getLogBar(String startDatetime, String endDatetime);
}
