package kr.co.igns.business.orgstatus.service;

import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jfree.util.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.dataset.dao.postgres.DatasetDAO;
import kr.co.igns.business.dataset.model.TypeofbusinessVO;
import kr.co.igns.business.orgstatus.dao.postgres.EmployeesAndStakeholdersDAO;
import kr.co.igns.business.orgstatus.model.EmployeesAndStakeholdersVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class EmployeesAndStakeholdersService {

    private final EmployeesAndStakeholdersDAO employeesAndStakeholdersDAO;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final UtilsDAO utilsDao;

    public List<EmployeesAndStakeholdersVO> searchEmployeesStakeholders(EmployeesAndStakeholdersVO reqVo) throws Exception {
        List<EmployeesAndStakeholdersVO> voList = employeesAndStakeholdersDAO.searchEmployeesStakeholders(reqVo);
        return voList;
    }

    public List<EmployeesAndStakeholdersVO> searchYear() {
        List<EmployeesAndStakeholdersVO> voList = employeesAndStakeholdersDAO.searchYear();
        return voList;
    }

    public EmployeesAndStakeholdersVO addEmployeesStakeholders(EmployeesAndStakeholdersVO reqVo) throws Exception {
        employeesAndStakeholdersDAO.addEmployeesStakeholders(reqVo);

        return reqVo;
    }


    @Transactional(rollbackFor = Throwable.class)
    public void deleteEmployeesStakeholdersList(List<EmployeesAndStakeholdersVO> reqVo) throws Exception {
        for (EmployeesAndStakeholdersVO item : reqVo) {
            employeesAndStakeholdersDAO.deleteEmployeesStakeholdersList(item);
        }
    }

    //detail 조회
    public List<EmployeesAndStakeholdersVO> searchEmployeesStakeholdersDetail(EmployeesAndStakeholdersVO reqVo) throws Exception {
        List<EmployeesAndStakeholdersVO> voList = employeesAndStakeholdersDAO.searchEmployeesStakeholdersDetail(reqVo);
        return voList;
    }

    public List<UtilsVO> getApprovalInfo(UtilsVO utilsVO) throws Exception {
        List<UtilsVO> voList = utilsDao.getApprovalInfo(utilsVO);
        return voList;
    }

    //detail 저장
    @Transactional(rollbackFor = Throwable.class)
    public void addEmployeesStakeholdersDetail(List<EmployeesAndStakeholdersVO> reqVo) throws Exception {

        UtilsVO signParam = new UtilsVO();

        String docNo = "";
        for (EmployeesAndStakeholdersVO item : reqVo) {
            item.setCreatedBy(SecurityUtil.getCurrentMemberId());
            //minCmd check
            if (item.getMainCmd().equals("I") && item.getDocNo().equals("")) {
                employeesAndStakeholdersDAO.addEmployeesStakeholders(item);
                docNo = item.getDocNo();

                signParam.setTargetType(item.getDocType());
                signParam.setTargetId(item.getWriteYear() + docNo);
                signParam.setHrId(item.getCreatedBy());
                signParam.setParam("writer");
                signParam.setUserId(item.getCreatedBy());
                utilsDao.insertApprovalInfo(signParam);
            } else {
                employeesAndStakeholdersDAO.modifyEmployeesStakeholders(item);
            }

            //detailcmd 구분자가 I 일경우 신규 생성
            if (item.getDetailCmd().equals("I")) {
                if (item.getDocNo().equals("")) {
                    item.setDocNo(docNo);
                }
                employeesAndStakeholdersDAO.addEmployeesStakeholdersDetail(item);
            } else {
                employeesAndStakeholdersDAO.modifyEmployeesStakeholdersDetail(item);
            }
        }
    }

    //서명자 저장
    @Transactional(rollbackFor = Throwable.class)
    public void insertApprovalInfo(List<UtilsVO> reqVo) throws Exception {
        for (UtilsVO item : reqVo) {
            if (!item.getHrId().isEmpty()) {
                item.setUserId(SecurityUtil.getCurrentMemberId());
                utilsDao.insertApprovalInfo(item);
            }
        }
    }

    //서명자 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteApprovalInfo(List<UtilsVO> reqVo) throws Exception {
        for (UtilsVO item : reqVo) {
            utilsDao.deleteApprovalInfo(item);
        }
    }

    //서명 입력
    @Transactional(rollbackFor = Throwable.class)
    public void updateApprovalInfoSign(UtilsVO reqVo) throws Exception {
        reqVo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDao.updateApprovalInfoSign(reqVo);
    }

    //서명 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void updateApprovalInfoSignCancel(UtilsVO reqVo) throws Exception {
        reqVo.setUserId(SecurityUtil.getCurrentMemberId());
        utilsDao.updateApprovalInfoSignCancel(reqVo);
    }

    //detail 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteEmployeesStakeholdersDetail(List<EmployeesAndStakeholdersVO> reqVo) throws Exception {
        for (EmployeesAndStakeholdersVO item : reqVo) {
            employeesAndStakeholdersDAO.deleteEmployeesStakeholdersDetail(item);
        }
    }

    //**********************레포트 관련******************************

    // 일괄출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int localPage = spSearchVO.getLocalPage();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        List<SpSearchVO> checkedObjList = new ArrayList<>();

        List<JasperPrint> jaspers = new ArrayList<>();
        checkedObjList = employeesAndStakeholdersDAO.getAllReport(spSearchVO);
        if (!checkedObjList.isEmpty()) {
            SpSearchVO reportParam = new SpSearchVO();
            reportParam.setPrintAll(true);
            reportParam.setCheckedObjList(checkedObjList);
            reportParam.setPage(page);
            reportParam.setSubPage(subPage);
            reportParam.setLocalPage(localPage);
            jaspers = getReport(request, response, reportParam);
            reportList.addAll(jaspers);
        }
//        for (JasperPrint report : jaspers) {
//            page += report.getPages().size();
//            localPage += report.getPages().size();
//        }
        return reportList;
    }

    // 전체 예산 레포트
    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        /***********************/
        /****** 공통 정보 조회 ******/
        /***********************/

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        String title = "이해관계자의 요구사항과" + "\n" + "기대 분석표";
        if (!spSearchVO.isPrintAll()) {
            JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
            reportList.add(JasperFrontReport);
            page = page + JasperFrontReport.getPages().size();
            localPage = localPage + JasperFrontReport.getPages().size();
        }
        /*************************************/
        /****** Jasper Export File Setup *****/
        /*************************************/
        String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";
        fileNm += "이해관계자의 요구사항과 기대 분석표";
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();

            // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)

            // 출력 생성용 Jasper 파일 위치
            reportVO.setJrxmlPath("report/orgnStatus/employees/employees.jrxml");

            // 출력  파일 형식 지정
            reportVO.setType(vo.getType());


            /**********************************/
            /****** 출력물 공통 Parameter 입력 *****/
            /**********************************/

            Map<String, Object> params = new HashMap<String, Object>();

            params.put("year", vo.getWriteYear());
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);


            /*************************************/
            /****** 출력물 Custom Parameter 입력 *****/
            /*************************************/
            params.put("year", vo.getWriteYear());

            //출력물 용 데이터 조회
            EmployeesAndStakeholdersVO reportData = employeesAndStakeholdersDAO.getReportData(vo);
            //조직명 입력
            params.put("orgnNm", reportData.getOrgnNm());
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + reportData.getOrgnNm();
            }

            //gridData 조회
            List<EmployeesAndStakeholdersVO> resultList = employeesAndStakeholdersDAO.getReportGridData(vo);
            //gridData 입력
            params.put("gridData", new JRBeanCollectionDataSource(resultList));

            //서명 조회
            UtilsVO signParam = new UtilsVO();
            signParam.setDocType(vo.getDocType());
            signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());

            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, signParam.getTargetId());
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            /**********************/
            /****** 출력물 출력 *******/
            /**********************/

            // 출력 파일 명 설정
            reportVO.setFileName(fileNm);
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }

}
