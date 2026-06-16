package kr.co.igns.system.setting.dao;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.orgstatus.model.EmployeesAndStakeholdersVO;
import kr.co.igns.system.common.entity.SpFileEntity;

@Repository
public interface FemsFileRepository extends JpaRepository<SpFileEntity, Long>, JpaSpecificationExecutor<SpFileEntity> {

	List<SpFileEntity> findByTargetIdAndTargetType(String id, String type);

	List<SpFileEntity> findByTargetIdAndTargetTypeAndCompId(String targetId, String targetType, String compId);

	List<SpFileEntity> findByTargetIdAndTargetTypeIn(String id, List<String> type);

	int countByTargetIdAndTargetType(String id, String type);

	SpFileEntity findByFileId(String id);

	int countByCompIdAndTargetTypeAndTargetIdAndFileNm(String compId, String targetType, String TargetId, String fileNm);

	List<SpFileEntity> findByTargetTypeAndTargetIdAndDelYn(@Param("targetType") String targetType, @Param("targetId") String targetId, @Param("delYn") String delYn);

}
