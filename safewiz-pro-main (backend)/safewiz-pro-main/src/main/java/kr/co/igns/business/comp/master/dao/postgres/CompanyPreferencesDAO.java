package kr.co.igns.business.comp.master.dao.postgres;
import kr.co.igns.business.comp.master.model.CompanyPreferencesVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.factory.dao.postgres
 * ※ 파일명 : CompanyPreferencesDAO.java
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
public interface CompanyPreferencesDAO {

	int searchCount(SpSearchVO searchVo);

	List<CompanyPreferencesVO> search(SpSearchVO searchVo);

	CompanyPreferencesVO findOne(String compId);
	
	int insert(CompanyPreferencesVO vo);

	int update(CompanyPreferencesVO vo);

	int updateDelYn(String compId);

	int delete(String compId);


}
