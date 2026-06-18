//package kr.co.igns.framework.api.file.entity;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface FileRepository extends JpaRepository<FileEntity, Long>, JpaSpecificationExecutor<FileEntity> {
//
//	List<FileEntity> findByTargetIdAndTargetType(String id , String type);
//	List<FileEntity> findByTargetIdAndTargetTypeAndCompId(String targetId , String targetType, String compId);
//
//	List<FileEntity> findByTargetIdAndTargetTypeIn(String id, List<String> type);
//
//	int countByTargetIdAndTargetType(String id , String type);
//
//	FileEntity findByFileId(Long id);
//	int countByCompIdAndTargetTypeAndTargetIdAndFileNm(String compId, String targetType, String TargetId, String fileNm);
//
//	@Query(value = "select * " +
//		"			  from tb_sys_file " +
//		"		 	 where comp_id = :compId " +
//	    " 			   and target_type = :targetType " +
//	    "			   and target_id = :targetId " +
//		"			   and del_yn != 'Y'", nativeQuery = true)
//	List<FileEntity> findByCompIdAndTargetTypeAndTargetId(@Param("compId")String compId, @Param("targetType")String targetType, @Param("targetId")String targetId);
//
//
//
//
//}
