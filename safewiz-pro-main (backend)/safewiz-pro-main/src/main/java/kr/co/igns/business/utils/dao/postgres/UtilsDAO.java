package kr.co.igns.business.utils.dao.postgres;

import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.PrcsWorkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UtilsDAO {

    String getLogoFileId(UtilsVO utilsVO);

    List<HrVO> getHrIncharge(@Param("targetType") String targetType,
                             @Param("targetId") String targetId,
                             @Param("gubun") String gubun);

    List<UtilsVO> searchCompList(String clntId);

    String getClntNm(UtilsVO utilsVO);

    String getCompNm(UtilsVO utilsVO);

    String getUserSignature(UtilsVO utilsVO);

    List<UtilsVO> getApprovalInfo(UtilsVO utilsVO);

    List<UtilsVO> getApprovalInfoTask(UtilsVO utilsVO);

    List<UtilsVO> getApprovalInfoSimple(UtilsVO utilsVO);
//    int checkDuplicateApprovalInfo(UtilsVO utilsVO);

    long insertApprovalInfo(UtilsVO utilsVO);

    int updateApprovalInfo(UtilsVO utilsVO);

    int updateApprovalInfoSign(UtilsVO utilsVO);

    int updateApprovalInfoSignCancel(UtilsVO utilsVO);

    int deleteApprovalInfo(UtilsVO utilsVO);

    int deleteApprovalInfoAll(UtilsVO utilsVO);

    int deleteApprovalInfoByType(UtilsVO utilsVO);

    int updateUserSignature(UtilsVO utilsVO);

    // 매핑정보 조회
    List<BaseMapVO> getWpMapByTargetIds(Map<String, Object> vo);
    List<BaseMapVO> getOrgnMapByTargetIds(Map<String, Object> vo);
    List<PrcsWorkVO> getPrcsWorkMapByTargetIds(Map<String, Object> vo);
    List<HrVO> getHrInchargeByTargetIds(Map<String, Object> vo);
}
