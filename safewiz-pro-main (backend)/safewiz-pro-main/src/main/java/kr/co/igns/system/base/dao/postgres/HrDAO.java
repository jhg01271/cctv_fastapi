package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrJbrpVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.OrgHistoryVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.UserManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HrDAO {
    List<HrVO> getHrList(SpSearchVO searchVo);
    HrVO getHrDetail(SpSearchVO searchVo);
    List<BaseMapVO> getCompIdByHr(String hrId);
    List<HrVO> getAffMgrCompIdByHr(String hrId);
    HrVO checkExceedMaxHrCount(HrVO vo);

    int insertHr(HrVO vo);

    int updateHr(HrVO vo);

    int deleteHr(HrVO vo);

    int deleteHrInfoCompMap(HrVO vo);

    HrVO getHrById(String hrId);
    String getHrOriginCompId(String hrId);

    //사용자 수정
    int updateUserHr(HrVO vo);
    //사용자 삭제
    int deleteUserHr(String hrId);

    //사업장 조회 : 수정시, insert/update 구분용
    List<HrVO> getCompHr(String hrId);
    //사업장 추가
    int addCompHr(HrVO vo);
    //사업장 수정
    int updateCompHr(HrVO vo);
    //사업장 삭제
    int deleteCompHr(HrVO vo);

    //조직 변경 이력 조회
    List<OrgHistoryVO> getOrgHistoryList(String hrId);
    //조직 변경 이력 추가
    int addOrgnHistoryHr(HrVO vo);
    //조직 변경 이력 수정
    int updateOrgnHistoryHr(HrVO vo);

    //인원 맵 조회
    List<BaseMapVO> getHrMap(BaseMapVO vo);
    List<HrVO> getHrMapDetail(BaseMapVO vo);
    //인원 추가
    int addHrMap(BaseMapVO vo);
    //인원 수정
    int updateHrMap(BaseMapVO vo);
    //인원 맵 데이터 수정(useyn만 업데이트)
    int updateUseYnHrMap(BaseMapVO vo);
    //인원 삭제
    int deleteHrMap(BaseMapVO vo);
    int hardDeleteHrMap(BaseMapVO vo);
    int deleteHrMapByGubun(BaseMapVO vo);

    // 직위관리
    List<HrJbrpVO> getHrJbrp(String compId);
    List<HrJbrpVO> getDatasetHrJbrp();
    void insertHrJbrp(HrJbrpVO vo);
    void updateHrJbrp(HrJbrpVO vo);
    void deleteHrJbrp(HrJbrpVO vo);

    // 비밀번호 초기화
    UserManageVO getUserInfo(UserManageVO vo);
    void resetPassword(UserManageVO vo);
}
