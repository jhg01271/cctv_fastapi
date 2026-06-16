package kr.co.igns.system.setting.dao.postgres;


import kr.co.igns.system.setting.model.GroupMemberDeleteDto;
import kr.co.igns.system.setting.model.GroupMemberVO;
import kr.co.igns.system.setting.model.GroupVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : fems-back-end
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
public interface GroupDAO {
    int insert(GroupVO vo);

    int update(GroupVO vo);

    int delete(GroupVO vo);

    int deleteMember(GroupMemberDeleteDto searchVO);

    int deleteMemberMenuByGroup(GroupMemberDeleteDto searchVO);

    List<GroupVO> search(SpSearchVO searchVo);

    List<HrVO> searchMember(SpSearchVO searchVo);

    int insertMember(GroupMemberVO vo);

    List<GroupVO> searchLists();

    GroupVO findOne(GroupVO vo);

    int searchCount(SpSearchVO searchVo);

    // 데이터셋 API
    // 그룹목록 데이터셋 조회
    List<GroupVO> getDatasetGroupList(SpSearchVO vo);

    // 그룹 데이터셋 저장(복사)
    void insertGroupDataset(GroupVO vo);

    // 그룹에 속한 메뉴 데이터셋 저장(복사)
    void insertGroupMenuDataset(GroupVO vo);

    GroupVO findUserGroup(GroupVO vo);
}
