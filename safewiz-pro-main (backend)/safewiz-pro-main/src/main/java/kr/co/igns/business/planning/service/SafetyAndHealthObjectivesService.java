package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.SafetyAndHealthObjectivesDAO;
import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.task.dao.postgres.TaskDAO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.model.OrganizationVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class SafetyAndHealthObjectivesService {

    private final SafetyAndHealthObjectivesDAO dao;
    private final UtilsService utilsService;
    private final UtilsDAO utilsDao;
    private final ReportService reportService;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final TaskDAO taskDao;

    // 메인 화면 조회
    public List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectives(vo);

        return voList;
    }

    // 상세 메인 화면 조회
    public List<SafetyAndHealthObjectivesVO> getMainSafetyAndHealthObjective(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = dao.getMainSafetyAndHealthObjective(vo);

        return voList;
    }

    // 상세 화면 조회
    public List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<SafetyAndHealthObjectivesVO> voList = dao.getSafetyAndHealthObjectivesDetail(vo);

        return voList;
    }

    // 조직조회
    public List<OrganizationVO> getOrganizationInSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo)
            throws Exception {
        List<OrganizationVO> voList = dao.getOrganizationInSafetyAndHealthObjectives(vo);

        return voList;
    }

    // 저장
    public void saveSafetyAndHealthObjectives(List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {

        boolean applyDocNo = false;
        String newDocNo = (String) dao.getDocNo(reqVo.get(0)); // 신규 문서번호

        for (SafetyAndHealthObjectivesVO item : reqVo) {

            String docNo = item.getDocNo();
            String docSeq = item.getDocSeq();

            if (docNo == null || docNo.isEmpty()) {
                item.setDocNo(newDocNo); // 새 문서번호(docNo) 정의

                // 메인 저장
                if (!applyDocNo) {
                    dao.insertSafetyAndHealthObjectives(item);
                    applyDocNo = true;
                }
 
                // 상세 저장
                dao.insertSafetyAndHealthObjectivesDetail(item);

            } else {
            	String reqInfoKey = "KPR" + "^" + item.getWriteYear() + "^" + item.getDocNo()+ "^" + item.getDocSeq();
            	item.setReqInfoKey(reqInfoKey);
            	
            	List<TaskVO> taskList = dao.getTaskList(item);
            	if(taskList.size() > 0) {
            		for(TaskVO vo : taskList) {
            			dao.updateTask(vo);
            			vo.setTaskDetailHrId(item.getHrId());
            			vo.setTaskUserChkDt(null);
            			vo.setTaskUserChk(null);
            			taskDao.insertTaskDetail(vo);
            			
            		}
            	}
            	
            	
            	
                // 문서 시퀀스(상세)가 없으면 신규 추가 있으면 업데이트
                dao.updateSafetyAndHealthObjectives(item);

                if (docSeq == null || docSeq.isEmpty()) {
                    // 상세 저장
                    dao.insertSafetyAndHealthObjectivesDetail(item);
                } else {
                    // 상세 수정
                    dao.updateSafetyAndHealthObjectivesDetail(item);
                }
            }
        }
    }

    // 저장
    public void saveSafetyAndHealthObjectivesTop(SafetyAndHealthObjectivesVO reqVo) throws Exception {
        dao.saveSafetyAndHealthObjectivesTop(reqVo);
    }

    // 메인 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void delSafetyAndHealthObjectives(List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {
        for (SafetyAndHealthObjectivesVO item : reqVo) {
            dao.delSafetyAndHealthObjectives(item);
        }
    }

    // 상세 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void delSafetyAndHealthObjectivesDetail(List<SafetyAndHealthObjectivesVO> reqVo) throws Exception {
        for (SafetyAndHealthObjectivesVO item : reqVo) {
            dao.delSafetyAndHealthObjectivesDetail(item);
        }
    }

    // 안전보건 목표 리포트
    public ReportVO getSafetyHealthObjectivesReport(HttpServletRequest request, HttpServletResponse response,
                                                    List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        String writeYear = spSearchVO.get(0).getWriteYear();
        int page = spSearchVO.get(0).getPage();
        int subPage = spSearchVO.get(0).getSubPage();
        int localPage = spSearchVO.get(0).getLocalPage();

        String title = "안전보건 목표";
        // 고객사 ID 조회
        spSearchVO.get(0).setClntId(SecurityUtil.getCurrentClntId());
        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(spSearchVO.get(0).getClntId());
        String clntNm = cVo.getClntNm();
        // 사업장 ID 조회
        spSearchVO.get(0).setCompId(SecurityUtil.getCurrentCompId());

        // 사업장 명칭 조회
        CompVO cpVO = new CompVO();
        cpVO.setClntId(spSearchVO.get(0).getClntId());
        cpVO.setCompId(spSearchVO.get(0).getCompId());
        CompVO cPo = compDao.getCompById(cpVO);

        String compNm = cPo.getCompNm();

        Map<String, Object> params = new HashMap<String, Object>();

        InputStream logo = utilsService.getClntLogo(spSearchVO.get(0).getClntId());
        params.put("logo", logo);
        params.put("compNm", compNm);

        SpSearchVO spVO = new SpSearchVO();
        spVO.setPage(page);
        spVO.setSubPage(subPage);
        spVO.setLocalPage(localPage);

        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        // 1. 서명 데이터 조회
        SpSearchVO signParam = new SpSearchVO();
        signParam.setDocType(spSearchVO.get(0).getDocType());
        signParam.setWriteYear(spSearchVO.get(0).getWriteYear());
        signParam.setDocNo(spSearchVO.get(0).getDocNo());
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        // 2. 메인 데이터 조회
        SafetyAndHealthObjectivesVO mainData = dao.getReportData(spSearchVO.get(0));

        params.put("orgnNm", mainData.getOrgnNm());
        params.put("revisedDt", mainData.getRevisedDt() != null && !mainData.getRevisedDt().isEmpty() ? DateUtils.formatDate(mainData.getRevisedDt()) : "");
        params.put("enactedDt", DateUtils.formatDate(mainData.getEnactedDt()));

        // 3. 상세 데이터 조회
        List<SafetyAndHealthObjectivesVO> detailData = new ArrayList<>();

        for (SafetyAndHealthObjectivesVO item : spSearchVO) {
            List<SafetyAndHealthObjectivesVO> data = dao.getReportGridData(item);

            detailData.addAll(data);
        }
        // 입력
        params.put("Dataset", new JRBeanCollectionDataSource(detailData));

        ReportVO reportVO = new ReportVO();

        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String fileNm = "";
        fileNm += '(' + writeYear + ')';
        fileNm += "안전보건 목표_";
        fileNm += mainData.getOrgnNm();
        // 출력 파일 명 설정
        reportVO.setFileName(fileNm);

        // 출력 생성용 Jasper 파일 위치
        reportVO.setJrxmlPath("report/planning/safetyAndHealthObjectives/safetyAndHealthObjectives.jrxml");

        // 출력 파일 형식 지정
        reportVO.setType(spSearchVO.get(0).getType());

        /**********************/
        /****** 출력물 출력 *******/
        /**********************/

        reportVO.setParameter(params);

        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (spSearchVO.get(0).isPrintAll())
            return reportVO;

        reportService.exportReportAll(request, response, reportList, spSearchVO.get(0).getType(), fileNm);

        return reportVO;
    }

    // 안전보건 목표 리포트 사용유무가 Y인것만 출력
    public ReportVO getSafetyHealthObjectivesReportUseYn(HttpServletRequest request, HttpServletResponse response,
                                                         List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        String writeYear = spSearchVO.get(0).getWriteYear();
        String title = "안전보건 목표";
        // 고객사 ID 조회
        spSearchVO.get(0).setClntId(SecurityUtil.getCurrentClntId());
        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(spSearchVO.get(0).getClntId());
        String clntNm = cVo.getClntNm();
        // 사업장 ID 조회
        spSearchVO.get(0).setCompId(SecurityUtil.getCurrentCompId());

        // 사업장 명칭 조회
        CompVO cpVO = new CompVO();
        cpVO.setClntId(spSearchVO.get(0).getClntId());
        cpVO.setCompId(spSearchVO.get(0).getCompId());
        CompVO cPo = compDao.getCompById(cpVO);

        String compNm = cPo.getCompNm();

        Map<String, Object> params = new HashMap<String, Object>();

        InputStream logo = utilsService.getClntLogo(spSearchVO.get(0).getClntId());
        params.put("logo", logo);
        params.put("compNm", compNm);

        int page = spSearchVO.get(0).getPage();
        int subPage = spSearchVO.get(0).getSubPage();
        int localPage = spSearchVO.get(0).getLocalPage();
        SpSearchVO spVO = new SpSearchVO();
        spVO.setPage(page);
        spVO.setSubPage(subPage);
        spVO.setLocalPage(localPage);

        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        // 1. 서명 데이터 조회
        SpSearchVO signParam = new SpSearchVO();
        signParam.setDocType(spSearchVO.get(0).getDocType());
        signParam.setWriteYear(spSearchVO.get(0).getWriteYear());
        signParam.setDocNo(spSearchVO.get(0).getDocNo());
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        // 2. 메인 데이터 조회
        SafetyAndHealthObjectivesVO mainData = dao.getReportData(spSearchVO.get(0));

        params.put("orgnNm", mainData.getOrgnNm());
        params.put("revisedDt", mainData.getRevisedDt() != null && !mainData.getRevisedDt().isEmpty() ? DateUtils.formatDate(mainData.getRevisedDt()) : "");
        params.put("enactedDt", DateUtils.formatDate(mainData.getEnactedDt()));
        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        // 3. 상세 데이터 조회
        List<SafetyAndHealthObjectivesVO> detailData = new ArrayList<>();

        for (SafetyAndHealthObjectivesVO item : spSearchVO) {
            List<SafetyAndHealthObjectivesVO> data = dao.getReportGridDataUseYn(item);

            detailData.addAll(data);
        }
        // 입력
        params.put("Dataset", new JRBeanCollectionDataSource(detailData));

        ReportVO reportVO = new ReportVO();

        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String fileNm = "";
        fileNm += '(' + writeYear + ')';
        fileNm += "안전보건 목표_";
        fileNm += mainData.getOrgnNm();

        // 출력 파일 명 설정
        reportVO.setFileName(fileNm);

        // 출력 생성용 Jasper 파일 위치
        reportVO.setJrxmlPath("report/planning/safetyAndHealthObjectives/safetyAndHealthObjectives.jrxml");

        // 출력 파일 형식 지정
        reportVO.setType(spSearchVO.get(0).getType());

        /**********************/
        /****** 출력물 출력 *******/
        /**********************/

        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);

        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (spSearchVO.get(0).isPrintAll())
            return reportVO;

        reportService.exportReportAll(request, response, reportList, spSearchVO.get(0).getType(), fileNm);

        return reportVO;
    }

    // 일괄 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 출력물 리스트
        SafetyAndHealthObjectivesVO param = new SafetyAndHealthObjectivesVO();
        param.setCompId(spSearchVO.getCompId());
        param.setWriteYear(spSearchVO.getWriteYear());
        List<SafetyAndHealthObjectivesVO> dataList = getSafetyAndHealthObjectives(param);
        if (!dataList.isEmpty()) {
            dataList.get(0).setPage(spSearchVO.getPage());
            dataList.get(0).setSubPage(spSearchVO.getSubPage());
            dataList.get(0).setLocalPage(spSearchVO.getLocalPage());
            dataList.get(0).setType(spSearchVO.getType());
            dataList.get(0).setPrintAll(true);

            reportList = getSafetyHealthObjectivesReportMain(request, response, dataList);
        }
        return reportList;
    }

    // 안전보건 목표 리포트 메인에서 출력시
    public List<JasperPrint> getSafetyHealthObjectivesReportMain(HttpServletRequest request, HttpServletResponse response,
                                                                 List<SafetyAndHealthObjectivesVO> spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 출력물 리스트

        /*
         * 인트로 페이지
         */
        String fileNm = spSearchVO.get(0).getFileNm(); // 파일이름
        String title = "안전보건 목표";
        int page = spSearchVO.get(0).getPage();
        int subPage = spSearchVO.get(0).getSubPage();
        int localPage = spSearchVO.get(0).getLocalPage();
        SpSearchVO spVO = new SpSearchVO();
        spVO.setPage(page);
        spVO.setSubPage(subPage);
        spVO.setLocalPage(localPage);

        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();
        for (SafetyAndHealthObjectivesVO printData : spSearchVO) {

            ReportVO reportvo = new ReportVO();
            reportvo.setJrxmlPath("report/planning/safetyAndHealthObjectives/safetyAndHealthObjectivesDetail.jrxml");

            Map<String, Object> params = new HashMap<String, Object>();
            // 1. 서명 데이터 조회
            SpSearchVO signParam = new SpSearchVO();
            signParam.setDocType(printData.getDocType());
            signParam.setWriteYear(printData.getWriteYear());
            signParam.setDocNo(printData.getDocNo());
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            // 2. 메인 데이터 조회
            SafetyAndHealthObjectivesVO mainData = dao.getReportData(printData);

            params.put("orgnNm", mainData.getOrgnNm());
            params.put("revisedDt", mainData.getRevisedDt() != null && !mainData.getRevisedDt().isEmpty() ? DateUtils.formatDate(mainData.getRevisedDt()) : "");
            params.put("enactedDt", DateUtils.formatDate(mainData.getEnactedDt()));

            // 3. 상세 데이터 조회
            List<SafetyAndHealthObjectivesVO> detailData = new ArrayList<>();

            List<SafetyAndHealthObjectivesVO> data = dao.getSafetyHealthObjectivesReportMain(printData);

            detailData.addAll(data);

            // 입력
            params.put("Dataset", new JRBeanCollectionDataSource(detailData));
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            reportvo.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportvo);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }

        // 통합 출력인 경우 reportList만 반환하고 종료

        if (spSearchVO.get(0).isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.get(0).getType(), fileNm);
        return reportList;

    }

}
