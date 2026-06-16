package kr.co.igns.system.setting.dao.postgres;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.AuthVO;
import kr.co.igns.system.setting.model.GroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 :  kr.co.igns.comp.auth.postgres;
 * ※ 파일명 : AuthDAO.java
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
public interface AuthDAO {
    int insert(AuthVO vo);

    int update(AuthVO vo);

    int delete(Long menuSeq);

    int deleteByGrpCd(String grpCd);
    int deleteGroupHrByGrpCd(GroupVO vo); // 인원 삭제
    int deleteGroupMenuByGrpCd(GroupVO vo); // 메뉴 삭제

    int deleteByGrpCdAndParam(AuthVO vo);

    List<AuthVO> search(SpSearchVO searchVo);

    List<AuthVO> searchByGroup(SpSearchVO searchVo);

    List<AuthVO> searchByUser(SpSearchVO searchVo);

    AuthVO findOne(Long menuSeq);

    int searchCount(SpSearchVO searchVo);

    int searchCountMenu(SpSearchVO searchVo);

    List<String> findUserGroup(SpSearchVO searchVo);
    //---- 그룹별 권한

    int removeGrpMap(AuthVO vo);

    int removeMemMapByParam(AuthVO vo);

    int removeGrpMapByParam(AuthVO vo);

    int insertGrpMap(AuthVO vo);

    int insertMemMap(AuthVO vo);
}
