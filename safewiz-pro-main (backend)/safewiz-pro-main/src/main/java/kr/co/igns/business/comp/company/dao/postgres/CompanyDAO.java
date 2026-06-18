package kr.co.igns.business.comp.company.dao.postgres;
import kr.co.igns.business.comp.company.model.CompanyVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.dao.postgres
 * ※ 파일명 : CompanyDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface CompanyDAO {
	
	int insert(CompanyVO vo);
	
	int update(CompanyVO vo);
	
	int updateDelYn(String compId);
	
	int delete(String compId);
		
	int searchCount(SpSearchVO searchVo);
	
	List<CompanyVO> search(SpSearchVO searchVo);
	
	CompanyVO findOne(String compId);
	CompanyVO findByInviteKey(String inviteKey);
	
	// [2024-07-11] 자식 존재 여부 확인 메서드 추가 - 신택훈)
	int countChildren(String compId);
	
	// [2024-07-11] 다른 테이블 사용 여부 확인 메서드 추가 - 신택훈)
	int countUsage(String compId);
}
