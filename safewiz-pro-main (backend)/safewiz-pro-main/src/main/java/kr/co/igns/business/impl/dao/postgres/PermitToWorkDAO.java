package kr.co.igns.business.impl.dao.postgres;

import kr.co.igns.business.impl.model.*;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermitToWorkDAO {
    List<PermitToWorkVO> getPermitToWork(SpSearchVO searchVo);
    PermitToWorkVO getPermitToWorkByDocNo(SpSearchVO searchVo);
    List<PermitToWorkDetailVO> getPermitToWorkDetail(SpSearchVO searchVo);
    List<PermitToWorkTypeVO> getPermitToWorkType(SpSearchVO searchVo);

    int insertPermitToWork(PermitToWorkVO vo);
    int insertPermitToWorkDetail(PermitToWorkDetailVO vo);
    int insertPermitToWorkType(PermitToWorkTypeVO vo);

    int updatePermitToWork(PermitToWorkVO vo);
    int updatePermitToWorkDetail(PermitToWorkDetailVO vo);
    int updatePermitToWorkType(PermitToWorkTypeVO vo);

    int deletePermitToWorkList(PermitToWorkVO vo);
    int deletePermitToWorkType(PermitToWorkTypeVO vo);
    int deletePermitToWork(PermitToWorkVO vo);
    int deletePermitToWorkDetail(PermitToWorkDetailVO vo);
    int deletePermitToWorkEqs(PermitToWorkVO vo);
    int deletePermitToEqByWorkDetail(PermitToWorkDetailVO vo);

    //점검사항 일지 초기화
    int initPermitToWorkDetail(PermitToWorkVO vo);

    PermitToWorkVO getPermitToWorkById(PermitToWorkVO vo);
    PermitToWorkDetailVO getPermitToWorkDetailById(PermitToWorkDetailVO vo);
    PermitToWorkTypeVO getPermitToWorkTypeById(PermitToWorkTypeVO vo);

    //인원 매핑
    List<HrVO> getPermitToWorkHr(SpSearchVO searchVo);
    int insertPermitToWorkHr(HrVO vo);
    int deletePermitToWorkHr(SpSearchVO searchVo);

    //필요안전기구 매핑
    List<SafetyEquipmentVO> getPermitToWorkEq(SpSearchVO searchVo);
    int insertPermitToWorkEq(SafetyEquipmentVO vo);
    int deletePermitToWorkEq(SafetyEquipmentVO vo);

    //#region 점검사항 관리 팝업
    List<SafetyWorkChecklistVO> getSafetyWorkChecklist(SpSearchVO searchVo);
    List<SafetyWorkChecklistVO> getSafetyWorkChecklistDetail(SafetyWorkChecklistVO vo);
    int insertSafetyWorkChecklist(SafetyWorkChecklistVO vo);
    int updateSafetyWorkChecklist(SafetyWorkChecklistVO vo);
    int insertSafetyWorkChecklistDetail(SafetyWorkChecklistVO vo);
    int updateSafetyWorkChecklistDetail(SafetyWorkChecklistVO vo);
    int deleteSafetyWorkChecklistDetail(SafetyWorkChecklistVO vo);
    SafetyWorkChecklistVO getSafetyWorkChecklistById(SafetyWorkChecklistVO vo);
    SafetyWorkChecklistVO getSafetyWorkChecklistDetailById(SafetyWorkChecklistVO vo);
    //#endregion

    //#region 안전기구 관리 팝업
    List<SafetyEquipmentVO> getSafetyEquipment(SpSearchVO searchVo);
    List<SafetyEquipmentVO> getSafetyEquipmentDetail(SafetyEquipmentVO vo);
    int insertSafetyEquipment(SafetyEquipmentVO vo);
    int updateSafetyEquipment(SafetyEquipmentVO vo);
    int insertSafetyEquipmentDetail(SafetyEquipmentVO vo);
    int updateSafetyEquipmentDetail(SafetyEquipmentVO vo);
    int deleteSafetyEquipmentDetail(SafetyEquipmentVO vo);
    SafetyEquipmentVO getSafetyEquipmentById(SafetyEquipmentVO vo);
    SafetyEquipmentVO getSafetyEquipmentDetailById(SafetyEquipmentVO vo);
    //#endregion
}
