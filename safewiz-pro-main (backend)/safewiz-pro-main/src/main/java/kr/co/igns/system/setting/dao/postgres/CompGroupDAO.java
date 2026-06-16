package kr.co.igns.system.setting.dao.postgres;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.GroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 :  kr.co.igns.comp.auth.postgres;
 * ※ 파일명 : GroupDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 7. 11.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface CompGroupDAO {
    int insert(GroupVO vo);

    int update(GroupVO vo);

    int delete(String grpCd);

    List<GroupVO> search(SpSearchVO searchVo);

    List<GroupVO> searchLists();

    GroupVO findOne(String grpCd);

    int searchCount(SpSearchVO searchVo);


}
