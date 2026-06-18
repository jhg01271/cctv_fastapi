package kr.co.igns.business.comp.company.dao.postgres;
import kr.co.igns.business.comp.company.model.CompVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.complex.dao.postgres;
 * ※ 파일명 : CompDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 14.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface CompanyMyDAO {
    int searchCount(SpSearchVO searchVo);

    List<CompVO> search(SpSearchVO searchVo);
}
