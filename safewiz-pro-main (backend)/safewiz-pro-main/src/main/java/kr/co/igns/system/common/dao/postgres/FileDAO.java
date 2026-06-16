package kr.co.igns.system.common.dao.postgres;

import kr.co.igns.system.common.model.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileDAO {
	
	FileVO getFileIdMax();
	List<FileVO> getFileList(@Param("targetId") String targetId, @Param("targetType") String targetType);
	List<FileVO> getFileListById(@Param("fileId") String fileId);
	void updateMoveFile(@Param("compId") String compId, @Param("fileId") String fileId, @Param("filePath") String filePath);
}
