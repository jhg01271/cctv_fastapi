package kr.co.igns.business.comp.master.dao.postgres;
import kr.co.igns.business.comp.master.model.CompCodeVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.factory.dao.postgres;
 * ※ 파일명 : CompanyCodeDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 7. 8.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface CompCodeDAO {
    int insert(CompCodeVO vo);

    int update(CompCodeVO vo);

    int updateDelYn(Long compCodeSeq);

    int delete(Long compCodeSeq);

    int searchCount(SpSearchVO searchVo);
    
    List<CompCodeVO> search(SpSearchVO searchVo);

    CompCodeVO findOne(Long compCodeSeq);
    
    List<CompCodeVO> findByUpCode(@Param("compId") String compId, @Param("code") String compCode);



}
