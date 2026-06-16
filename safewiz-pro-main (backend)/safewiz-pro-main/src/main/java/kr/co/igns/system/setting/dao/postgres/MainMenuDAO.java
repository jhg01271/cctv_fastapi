package kr.co.igns.system.setting.dao.postgres;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.MainMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 :  kr.co.igns.base.system.dao.postgres;
 * ※ 파일명 : MainMenuDAO.java
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
public interface MainMenuDAO {
    int insert(MainMenuVO vo);

    int update(MainMenuVO vo);

    int delete(String menuId);

    List<MainMenuVO> search(SpSearchVO searchVo);
    List<MainMenuVO> searchMaster(SpSearchVO searchVo);
    List<MainMenuVO> searchFavorites(SpSearchVO searchVo);

    List<MainMenuVO> searchAdmin(SpSearchVO searchVo);

    List<MainMenuVO> searchAdminOrderByName(SpSearchVO searchVo);

    MainMenuVO findOne(String menuId);

    List<String> findUserGroup(SpSearchVO searchVo);

    int searchCount(SpSearchVO searchVo);

    String getAffCompIdByUserId(SpSearchVO searchVo);
    String getAffHrIdByUserId(SpSearchVO searchVo);

}
