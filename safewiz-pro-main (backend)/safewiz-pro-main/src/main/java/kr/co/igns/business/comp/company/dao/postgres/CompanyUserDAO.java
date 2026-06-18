package kr.co.igns.business.comp.company.dao.postgres;
import kr.co.igns.business.comp.company.model.CompanyUserVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.comp.company.dao.postgres;
 * ※ 파일명 : CompanyUserDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 7. 18.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

// [2024-07-23] 패키지 변경 - 신택훈)
@Mapper
@Repository
public interface CompanyUserDAO {
	int insert(CompanyUserVO vo);
	
	int update(CompanyUserVO vo);
	
	int delete(Long memSeq);
	
	int deleteByUserId(String userId);
	
	int searchCount(SpSearchVO searchVo);
	
	List<CompanyUserVO> search(SpSearchVO searchVo);
	
	CompanyUserVO searchByCompIdAndUserId(SpSearchVO searchVo);
}
