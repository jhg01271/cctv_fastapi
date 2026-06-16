package kr.co.igns.business.planning.dao.postgres;

import kr.co.igns.business.planning.model.*;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface LegalReviewDAO {
    List<LegalReviewVO> getLegalReviewList(SpSearchVO vo);
    
    List<LegalReviewVO> getLegalReviewDetailMasterList(SpSearchVO vo);
    
    List<LegalReviewDetailVO> getLegalReviewDetailList(SpSearchVO vo);

    List<LegalReviewDetailOrgnVO> getLegalReviewDetailOrgnList(LegalReviewDetailVO vo);

    String getDocNo(LegalReviewVO searchVo);
    
    void insertLegalReview(LegalReviewVO reqVo);
    void updateLegalReview(LegalReviewVO reqVo);
    
    void insertLegalReviewDetail(Map<String, Object> reqVo);
    void updateLegalReviewDetail(Map<String, Object> reqVo);

    int insertLegalReviewDetailOrgn(LegalReviewDetailOrgnVO vo);
    int deleteLegalReviewDetailOrgn(LegalReviewDetailOrgnVO vo);

    void deleteLegalReview(LegalReviewVO reqVo);
    void deleteLegalReviewDetail(LegalReviewDetailVO reqVo);
    
    
 // 출력물 용 데이터 조회
    LegalReviewVO getReportData(LegalReviewVO searchVo);
 	List<LegalReviewDetailVO>  getReportGridData(LegalReviewVO searchVo);
 	
 	
 	List<LegalReviewDetailVO>  getReportGridDataAll(SpSearchVO searchVo);
 	
    // 일괄출력용
    List<SpSearchVO> getAllReport(SpSearchVO spSearchVO);
}
