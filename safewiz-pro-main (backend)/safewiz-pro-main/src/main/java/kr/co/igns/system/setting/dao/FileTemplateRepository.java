package kr.co.igns.system.setting.dao;

import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.entity.SpFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileTemplateRepository extends JpaRepository<FileTemplateEntity, Long>, JpaSpecificationExecutor<FileTemplateEntity> {
	FileTemplateEntity findById(String id);
}
