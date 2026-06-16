package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HazmatChecklistDAO {
    List<HazmatChecklistVO> getHazmatChecklist(SpSearchVO searchVo);
    HazmatChecklistVO getHazmatChecklistByDocNo(HazmatChecklistVO searchVo);
    HazmatChecklistVO existHazmatChecklist(HazmatChecklistVO searchVo);
    List<HazmatChecklistDetailVO> getHazmatChecklistDetail(HazmatChecklistVO searchVo);
    List<MsdsVO> getMsdsByWorkplace(HazmatChecklistVO vo);

    int insertHazmatChecklist(HazmatChecklistVO vo);
    int insertHazmatChecklistDetail(HazmatChecklistDetailVO vo);

    int updateHazmatChecklist(HazmatChecklistVO vo);
    int updateHazmatChecklistDetail(HazmatChecklistDetailVO vo);

    int deleteHazmatChecklist(HazmatChecklistVO vo);
    int deleteHazmatChecklistDetail(HazmatChecklistVO vo);

    //점검사항 일지 초기화
    int initHazmatChecklistDetail(HazmatChecklistVO vo);

    HazmatChecklistVO getHazmatChecklistById(HazmatChecklistVO vo);
    HazmatChecklistDetailVO getHazmatChecklistDetailById(HazmatChecklistDetailVO vo);

    //마지막 점검사항 불러오기
    List<HazmatChecklistDetailVO> getLastChecklist(SpSearchVO searchVo);

    //#region 점검사항 관리 팝업
    List<DatasetHazmatChecklistVO> getDatasetHazmatChecklist(SpSearchVO searchVo);
    List<DatasetHazmatChecklistVO> getDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo);
    int insertDatasetHazmatChecklist(DatasetHazmatChecklistVO vo);
    int updateDatasetHazmatChecklist(DatasetHazmatChecklistVO vo);
    int insertDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo);
    int updateDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo);
    int deleteDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo);
    DatasetHazmatChecklistVO getDatasetHazmatChecklistById(DatasetHazmatChecklistVO vo);
    DatasetHazmatChecklistVO getDatasetHazmatChecklistDetailById(DatasetHazmatChecklistVO vo);

    //예시 불러오기
    List<DatasetHazmatChecklistVO> getBaseDatasetHazmatChecklist(SpSearchVO searchVo);
    List<DatasetHazmatChecklistVO> getBaseDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo);
    //#endregion
}

