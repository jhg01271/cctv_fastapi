package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.SafetyChecklistDAO;
import kr.co.igns.business.impl.model.SafetyChecklistDetailVO;
import kr.co.igns.business.impl.model.SafetyChecklistHrVO;
import kr.co.igns.business.impl.model.SafetyChecklistItemVO;
import kr.co.igns.business.impl.model.SafetyChecklistVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SafetyChecklistService {
    private final SafetyChecklistDAO safetyChecklistDao;
    private final String targetType = "SCL";

    //선택된 설비유형의 안전점검표가 존재하는 여부 반환
    public boolean  hasSafetyChecklist(SpSearchVO searchVo) throws Exception {
        int count = safetyChecklistDao.hasSafetyChecklist(searchVo);
        return count > 0;
    }

    public List<SafetyChecklistVO> getSafetyChecklist(SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistVO> voList = safetyChecklistDao.getSafetyChecklist(searchVo);
        return voList;
    }

    public SafetyChecklistVO getSafetyChecklistDetail(SpSearchVO searchVo) throws Exception {
        SafetyChecklistVO vo = safetyChecklistDao.getSafetyChecklistDetail(searchVo);
        if(!"".equals(searchVo.getSearchText())){
            // searchText에 stdEqId 넣어서 조회할 경우
            searchVo.setWriteYear(vo.getWriteYear());
            searchVo.setCompId(vo.getCompId());
            searchVo.setDocNo(vo.getDocNo());
            searchVo.setDocType(vo.getDocType());
        }
        vo.setItemList(safetyChecklistDao.getSafetyChecklistItem(searchVo));
        vo.setDetailList(safetyChecklistDao.getSafetyChecklistDetailList(searchVo));
        vo.setHistoryList(safetyChecklistDao.getSafetyChecklistHistoryList(searchVo));
        return vo;
    }

    public List<SafetyChecklistItemVO> getSafetyChecklistItem(SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistItemVO> voList = safetyChecklistDao.getSafetyChecklistItem(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveSafetyChecklist(SafetyChecklistVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 안전점검표 추가
            vo.setDocType(targetType);
            vo.setCreatedBy(vo.getCreatedBy());
            safetyChecklistDao.insertSafetyChecklist(vo);
            // 점검 항목 추가 - 점검 사항은 점검 항목이 하나라도 추가된 후 만들 수 있음
            for (SafetyChecklistItemVO item : vo.getItemList()){
                String oldDocSeq = item.getDocSeq();    //복사일 경우, 사용
                item.setDocNo(vo.getDocNo());
                item.setDocType(targetType);
                item.setCreatedBy(vo.getCreatedBy());
                safetyChecklistDao.insertSafetyChecklistItem(item);

                //복사일 경우(doc_seq가 복사대상 값이 들어있는 경우), 점검사항을 같이 저장
                if(oldDocSeq != null && !oldDocSeq.isEmpty()){
                    //점검 사항 추가
                    for (SafetyChecklistDetailVO detail : vo.getDetailList()) {
                        //doc_seq =  check_item_seq 인 점검사항
                        if(oldDocSeq.equals(detail.getCheckItemSeq())) {
                            detail.setDocNo(vo.getDocNo());
                            detail.setCheckItemSeq(item.getDocSeq());   //새로만든 점검항목 doc_seq 연결
                            detail.setDocType(targetType);
                            detail.setCreatedBy(vo.getCreatedBy());
                            safetyChecklistDao.insertSafetyChecklistDetail(detail);
                        }
                    }
                }
            }
        } else {
            // 안전점검표 수정
            SafetyChecklistVO resultVO = safetyChecklistDao.getSafetyChecklistById(vo);
            if (resultVO == null)
                return null;
            resultVO = (SafetyChecklistVO) SpUtils.objectMap(vo, resultVO);

            safetyChecklistDao.updateSafetyChecklist(resultVO);

            // 점검 항목 추가/수정
            for (SafetyChecklistItemVO item : vo.getItemList()) {
                if (item.getCmd().equals("I")) {
                	item.setCreatedBy(vo.getCreatedBy());
                    safetyChecklistDao.insertSafetyChecklistItem(item);
                } else {
                    SafetyChecklistItemVO resultVO2 = safetyChecklistDao.getSafetyChecklistItemById(item);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (SafetyChecklistItemVO) SpUtils.objectMap(item, resultVO2);
                    
                    resultVO2.setUpdatedBy(vo.getUpdatedBy());
                    safetyChecklistDao.updateSafetyChecklistItem(resultVO2);
                }
            }
            // 점검 사항 추가/수정
            for (SafetyChecklistDetailVO detail : vo.getDetailList()) {
                if (detail.getCmd().equals("I")) {
                	detail.setCreatedBy(vo.getCreatedBy());
                    detail.setUseYn(detail.getItemUseYn());
                    safetyChecklistDao.insertSafetyChecklistDetail(detail);
                } else {
                    SafetyChecklistDetailVO resultVO2 = safetyChecklistDao.getSafetyChecklistDetailById(detail);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (SafetyChecklistDetailVO) SpUtils.objectMap(detail, resultVO2);

                    resultVO2.setUpdatedBy(vo.getUpdatedBy());
                    resultVO2.setUseYn(detail.getItemUseYn());
                    safetyChecklistDao.updateSafetyChecklistDetail(resultVO2);
                }
            }
        }

        // 점검주기 이력 테이블 up/in sert
        safetyChecklistDao.insertSafetyCheckListHistory(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteSafetyChecklist(List<SafetyChecklistVO> list) throws Exception {
        for (SafetyChecklistVO tmp : list) {
            safetyChecklistDao.deleteSafetyChecklist(tmp);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteSafetyChecklistDetail(SafetyChecklistVO vo) throws Exception {
        for (SafetyChecklistItemVO item : vo.getItemList()) {
            safetyChecklistDao.deleteSafetyChecklistItem(item);
        }
        for (SafetyChecklistDetailVO detail : vo.getDetailList()) {
            safetyChecklistDao.deleteSafetyChecklistDetail(detail);
        }
    }

    //안전점검 담당자 관리
    //안전점검 담당자 조회
    public List<SafetyChecklistHrVO> getSafetyCheckInspector(SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistHrVO> voList = safetyChecklistDao.getSafetyCheckInspector(searchVo);
        return voList;
    }

    //안전점검 담당자 불러오기
    public List<SafetyChecklistHrVO> getEqInspector(SpSearchVO searchVo) throws Exception {
        List<SafetyChecklistHrVO> voList = safetyChecklistDao.getEqInspector(searchVo);
        return voList;
    }

    //안전점검 담당자 저장
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveSafetyCheckInspector(List<SafetyChecklistHrVO> reqVo) throws Exception {
         for(SafetyChecklistHrVO vo : reqVo) {
             //현재 저장된 인원 리스트
             List<HrVO> dbHrList = safetyChecklistDao.getSafetyCheckInspectorByEq(vo);
             List<HrVO> deleteList = dbHrList.stream()
                     .filter(dbItem -> vo.getHrList().stream().noneMatch(dbItem2 -> dbItem2.getId().equals(dbItem.getId())))
                     .collect(Collectors.toList());
             //담당자 삭제
             for(HrVO hrVo : deleteList) {
                 safetyChecklistDao.deleteSafetyCheckInspector(hrVo);
             }
             //담당자 추가
             for(HrVO hrVo : vo.getHrList()) {
                 hrVo.setExtra1(vo.getEquipmentId());
                 safetyChecklistDao.insertSafetyCheckInspector(hrVo);
             }
         }
         return reqVo.get(0);
    }
}

