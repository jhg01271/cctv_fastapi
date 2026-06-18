package kr.co.igns.business.impl.service;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.business.impl.dao.postgres.SafetyInspectionLogDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.task.dao.postgres.TaskDAO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.task.service.TaskService;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.base.service.EquipService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SafetyInspectionLogService {
    private final SafetyInspectionLogDAO safetyInspectionLogDao;
    private final TaskDAO taskDao;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final EquipService equipService;
    private final TaskService taskService;
    private final String targetType = "SIL";

    // 설비별 점검일지 목록 조회
    public List<SafetyInspectionLogVO> getSafetyInspectionLogList(SpSearchVO vo) throws Exception {
        List<SafetyInspectionLogVO> resultList = new ArrayList<>();
        vo.setHrId(SecurityUtil.getCurrentHrId());
        resultList = safetyInspectionLogDao.getSafetyInspectionLogList(vo);
        return resultList;
    }

    public SafetyInspectionLogVO getSafetyInspectionLog(SafetyInspectionLogSearchVO searchVo) throws Exception {
        SafetyInspectionLogVO vo = safetyInspectionLogDao.getSafetyInspectionLog(searchVo);
        if (vo == null) {
            vo = new SafetyInspectionLogVO();
            vo.setEquipmentId(searchVo.getEquipmentId());
            vo.setStdEqId(searchVo.getStdEqId());
            vo.setCheckDt(searchVo.getCheckDt());

            //조회 데이터가 없을때 설비 최신 정보 반환
            searchVo.setSearchText(searchVo.getEquipmentId());
            EquipVO eq = equipService.getEquipDetail(searchVo);
            vo.setSetupLocation(eq.getSetupLocation());
            vo.setMfSpec(eq.getMfSpec());
            vo.setOrgnList(eq.getOrgnList());
        } else {
            searchVo.setEquipmentId(vo.getEquipmentId());
            searchVo.setStdEqId(vo.getStdEqId());
            searchVo.setCheckDt(vo.getCheckDt());
        }
        vo.setDetailList(safetyInspectionLogDao.getSafetyInspectionLogDetail(searchVo));
        return vo;
    }

    public SafetyInspectionLogVO getEquipInfo(SafetyInspectionLogSearchVO searchVo) throws Exception {
        SafetyInspectionLogVO result = safetyInspectionLogDao.getEquipInfo(searchVo);
        return result;
    }

     public List<EquipVO> getSafetyInspectionStdEqList(SpSearchVO searchVo) throws Exception {
        List<EquipVO> resultList = safetyInspectionLogDao.getSafetyInspectionStdEqList(searchVo);
        return resultList;
    }

    public List<String> getSafetyInspectionLogDates(SafetyInspectionLogSearchVO searchVo) throws Exception {
        List<String> result = safetyInspectionLogDao.getSafetyInspectionLogDates(searchVo);
        return result;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveSafetyInspectionLog(SafetyInspectionLogVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 안전점검일지 추가
            vo.setDocType(targetType);
            vo.setCreatedBy(vo.getCreatedBy());
            safetyInspectionLogDao.insertSafetyInspectionLog(vo);
            // 안전점검일지:점검사항 추가
            for (SafetyInspectionLogDetailVO item : vo.getDetailList()) {
                item.setWriteYear(vo.getWriteYear());
                item.setCheckDt(vo.getCheckDt());
                item.setDocNo(vo.getDocNo());
                item.setDocType(targetType);
                item.setCreatedBy(vo.getCreatedBy());
                safetyInspectionLogDao.insertSafetyInspectionLogDetail(item);
            }
        } else {
            // 안전점검일지 수정
            SafetyInspectionLogVO resultVO = safetyInspectionLogDao.getSafetyInspectionLogById(vo);
            if (resultVO == null)
                return null;
            resultVO = (SafetyInspectionLogVO) SpUtils.objectMap(vo, resultVO);

            safetyInspectionLogDao.updateSafetyInspectionLog(resultVO);

            // 안전점검일지:점검사항 수정
            for (SafetyInspectionLogDetailVO item : vo.getDetailList()) {
                SafetyInspectionLogDetailVO resultVO2 = safetyInspectionLogDao.getSafetyInspectionLogDetailById(item);
                if (resultVO2 == null)
                    return null;
                resultVO2 = (SafetyInspectionLogDetailVO) SpUtils.objectMap(item, resultVO2);

                safetyInspectionLogDao.updateSafetyInspectionLogDetail(resultVO2);
            }
        }

        // 점검을 하지 않았다가 했을 때 TASK 테이블 업데이트
        if (!vo.getDetailList().isEmpty()) {
            boolean hasAllN = vo.getDetailList().stream()
                    .anyMatch(item -> "N".equals(item.getAcceptableYn()) && "N".equals(item.getNonCompliantYn()));
            if (!hasAllN) {
                vo.setCompleted(true);
            } else {
                vo.setCompleted(false);
            }
            // 등록완료 혹은 점검완료 시 task 테이블 업데이트함
            taskService.updateSafetyCheckListTask(vo);
        }


        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteSafetyInspectionLog(SafetyInspectionLogVO vo) throws Exception {
        safetyInspectionLogDao.deleteSafetyInspectionLog(vo);
        safetyInspectionLogDao.deleteSafetyInspectionLogDetail(vo);
        // 평가 Task cancel
        TaskVO reqVo = new TaskVO();
        reqVo.setCompId(vo.getCompId());
        String reqInfoKey = vo.getDocType() + "^&" + vo.getWriteYear() + "^&" + vo.getStdEqId() + "^&" + vo.getEquipmentId() + "^&" + vo.getCheckDt();
        reqVo.setReqInfoKey(reqInfoKey);
        taskDao.cancelSafetyCheckListTask(reqVo);

        // 서명정보 삭제
        TaskVO deleteVO = new TaskVO();
        deleteVO.setTargetType(vo.getDocType());
        deleteVO.setTargetId(vo.getWriteYear() + vo.getStdEqId() + vo.getEquipmentId() + vo.getCheckDt());
        taskDao.deleteApprovalInfo(deleteVO);

        // 문서등록 TASK cancel
        SafetyInspectionLogVO originPK = taskDao.originSafetyCheckListTask(vo);
        String reqInfoKey2 = originPK.getDocType() + "^&" + originPK.getWriteYear() + "^&" + originPK.getDocNo() + "^&" + vo.getStdEqId() + "^&" + vo.getEquipmentId();
        reqVo.setReqInfoKey(reqInfoKey2);
        taskDao.cancelSafetyCheckListTask(reqVo);


        return vo;
    }


    public List<JasperPrint> getSafetyInspectionLogReportList(HttpServletRequest request, HttpServletResponse response,
                                                              SpSearchVO spSearchVO) throws Exception {
        List<SpSearchVO> checkedObjList = new ArrayList<>();
        for (SpSearchVO originalParam : spSearchVO.getCheckedObjList()) {
            SafetyInspectionLogSearchVO vo = new SafetyInspectionLogSearchVO();
            vo.setWriteYear(spSearchVO.getWriteYear());
            vo.setStdEqId(originalParam.getSearchText());
            vo.setEquipmentId(originalParam.getSearchText2());
            vo.setCompId(SecurityUtil.getCurrentCompId());

            List<String> checkDtList = safetyInspectionLogDao.getSafetyInspectionLogDates(vo); // 점검일자 조회

            for (String date : checkDtList) {
                SpSearchVO newParam = new SpSearchVO();
                newParam.setSearchText(originalParam.getSearchText());
                newParam.setSearchText2(originalParam.getSearchText2());
                newParam.setSearchText3(date); // 날짜 설정
                checkedObjList.add(newParam);
            }
        }
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전점검표";
        spSearchVO.setPrintAll(true);
        spSearchVO.setCheckedObjList(checkedObjList);
        List<JasperPrint> reportList = getSafetyInspectionLogReport(request, response, spSearchVO);
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        return reportList;
    }

    public List<JasperPrint> getSafetyInspectionLogReport(HttpServletRequest request, HttpServletResponse response,
                                                          SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String fileNm = "";

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        int index = 0;
        for (SpSearchVO param : spSearchVO.getCheckedObjList()) {
            SafetyInspectionLogSearchVO searchVO = new SafetyInspectionLogSearchVO();
            searchVO.setStdEqId(param.getSearchText());
            searchVO.setEquipmentId(param.getSearchText2());
            searchVO.setCheckDt(param.getSearchText3());
            searchVO.setCompId(SecurityUtil.getCurrentCompId());
            SafetyInspectionLogVO vo = safetyInspectionLogDao.getSafetyInspectionLog(searchVO);
            String title = "안전점검표";
            if (spSearchVO.getCheckedObjList().size() > 1) {
                fileNm = "(" + vo.getWriteYear() + ")" + "안전점검표_" + vo.getStdEqNm();
            } else
                fileNm = "(" + vo.getWriteYear() + ")" + "안전점검표_" + vo.getStdEqNm() + "_" + DateUtils.formatDate(vo.getCheckDt());
            ;
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            // 표지 리포트
            if (index == 0) {
                JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
                reportList.add(JasperFrontReport);
                page = page + JasperFrontReport.getPages().size();
                localPage = localPage + JasperFrontReport.getPages().size();
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/impl/safetyInspectionLog/safetyInspectionLog.jrxml");
            reportVO.setType(searchVO.getType());


            Map<String, Object> params = new HashMap<String, Object>();

            List<SafetyInspectionLogDetailVO> detailList = safetyInspectionLogDao.getSafetyInspectionLogDetail(searchVO);
            vo.setDetailList(detailList);


            params.put("logo", logo);
            params.put("title", title);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            params.put("subTitle", vo.getTitle());
            params.put("equipmentNm", vo.getEquipmentNm());
            params.put("stdEqNm", vo.getStdEqNm());

            params.put("mfSpec", vo.getMfSpec());
            params.put("setupLocation", vo.getSetupLocation());
            params.put("orgnNm", vo.getOrgnNm());
            params.put("nonComplianceAction", vo.getNonComplianceAction());
//            params.put("checkDt", SpUtils.formatDate1(vo.getCheckDt()));
            params.put("checkDt", DateUtils.formatDate(vo.getCheckDt()));
            params.put("hrOrgnNm", vo.getHrOrgnNm());

            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(vo.getDocType());
            signParam.setTargetId(vo.getWriteYear() + vo.getStdEqId() + vo.getEquipmentId() + vo.getCheckDt());
            signParam.setType("change");

            // 2. 서명 데이터 매핑 -
            List<UtilsVO> signList = utilsService.getApprovalInfo(signParam);
            if (signList.size() >= 1) {
                InputStream signStream = utilsService.convertToInputStream(signList.get(0).getSignature());
                params.put("signature", signStream);
                params.put("hrNm", signList.get(0).getHrNm());
                params.put("orgnNmSign", signList.get(0).getOrgnNm());
            } else {
                params.put("hrNm", " ");
            }

            // 점검항목
            List<Map<String, Object>> datasource = new ArrayList<>();

            //점검 항목이 없을때, 빈값 생성

            if (vo.getDetailList().isEmpty()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("compId", vo.getCompId());
                data.put("seq", 0);
                data.put("checkItem", "");
                data.put("checkList", "");
                data.put("acceptableYn", "");
                data.put("nonCompliantYn", "");
                datasource.add(data);
            }
            int mainSeq = 0;//점검사항 정렬 index
            String checkItem = "";
            for (SafetyInspectionLogDetailVO detail : vo.getDetailList()) {
                if (checkItem.isEmpty()) {
                    checkItem = detail.getCheckItem();
                }
                if (!detail.getCheckItem().equals(checkItem)) {
                    checkItem = detail.getCheckItem();
                    mainSeq++;
                }
                if (detail != null) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("compId", vo.getCompId());
                    data.put("seq", detail.getOrdSeq());
                    data.put("checkItem", detail.getCheckItem());
                    data.put("checkList", detail.getCheckList());
                    data.put("acceptableYn", detail.getAcceptableYn());
                    data.put("nonCompliantYn", detail.getNonCompliantYn());
                    data.put("mainSeq", mainSeq);
                    datasource.add(data);
                }
            }

            params.put("dataList", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
            index++;
        }
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }
}
