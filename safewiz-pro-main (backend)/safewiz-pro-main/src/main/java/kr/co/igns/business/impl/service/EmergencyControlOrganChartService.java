package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.EmergencyControlOrganChartDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyResponseDAO;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
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
public class EmergencyControlOrganChartService {
    private final EmergencyControlOrganChartDAO emergencyControlOrganChartDAO;
    private final SystemCodeDAO systemCodeDao;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<EmergencyControlOrganChartVO> getEmergencyOrgChartList(SpSearchVO searchVo) throws Exception {
        List<EmergencyControlOrganChartVO> voList = emergencyControlOrganChartDAO.getEmergencyOrgChartList(searchVo);
        return voList;
    }

    public List<EmergencyRoleVO> getEmergencyTypeList(SpSearchVO searchVo) throws Exception {
        List<EmergencyRoleVO> voList = emergencyControlOrganChartDAO.getEmergencyTypeList(searchVo);
        for (EmergencyRoleVO vo : voList) {
            vo.setRoles(emergencyControlOrganChartDAO.getEmergencyRoleList(vo));
        }
        return voList;
    }

    public List<EmergencyRoleVO> getEmergencyRoleList(EmergencyRoleVO vo) throws Exception {
        List<EmergencyRoleVO> voList = emergencyControlOrganChartDAO.getEmergencyRoleList(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO confirmEmergencyOrgChart(EmergencyControlOrganChartVO vo) throws Exception {
        emergencyControlOrganChartDAO.confirmEmergencyOrgChart(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEmergencyOrgChart(EmergencyControlOrganChartVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            emergencyControlOrganChartDAO.insertEmergencyOrgChart(vo);
        } else {
            emergencyControlOrganChartDAO.updateEmergencyOrgChart(vo);
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteEmergencyOrgChart(List<EmergencyControlOrganChartVO> voList) throws Exception {
        for (EmergencyControlOrganChartVO vo : voList) {
            emergencyControlOrganChartDAO.deleteEmergencyOrgChart(vo);
        }
        return voList.get(0);
    }

    public List<EmergencyRoleVO> getEmergencyType(SpSearchVO searchVo) throws Exception {
        List<EmergencyRoleVO> voList = emergencyControlOrganChartDAO.getEmergencyType(searchVo);
        return voList;
    }

    public List<EmergencyRoleVO> getEmergencyTypeRole(SpSearchVO searchVo) throws Exception {
        List<EmergencyRoleVO> voList = emergencyControlOrganChartDAO.getEmergencyRole(searchVo);
        return voList;
    }

    public List<EmergencyRoleVO> getEmergencyTypeDataset(SpSearchVO searchVo) throws Exception {
        List<EmergencyRoleVO> voList = emergencyControlOrganChartDAO.getEmergencyTypeDataset(searchVo);
        for (EmergencyRoleVO vo : voList) {
            vo.setRoles(emergencyControlOrganChartDAO.getEmergencyRoleDataset(vo));
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteEmergencyType(List<EmergencyRoleVO> voList) throws Exception {
        for (EmergencyRoleVO vo : voList) {
            if ("D".equals(vo.getCmd())) {
                // 하위데이터까지 전체 삭제
                emergencyControlOrganChartDAO.deleteEmergencyType(vo);
                emergencyControlOrganChartDAO.deleteAllEmergencyRole(vo);
            } else {
                // 하위데이터만 삭제
                for (EmergencyRoleVO role : vo.getRoles()) {
                    emergencyControlOrganChartDAO.deleteEmergencyRole(role);
                }
            }

        }
        return voList.get(0);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEmergencyType(List<EmergencyRoleVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (EmergencyRoleVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                vo.setCreatedBy(userId);
                emergencyControlOrganChartDAO.insertEmergencyType(vo);
            } else {
                vo.setUpdatedBy(userId);
                emergencyControlOrganChartDAO.updateEmergencyType(vo);
            }
            for (EmergencyRoleVO role : vo.getRoles()) {
                role.setTypeId(vo.getTypeId());
                if (role.getCmd().equals("I")) {
                    role.setCreatedBy(userId);
                    role.setCompId(vo.getCompId());
                    emergencyControlOrganChartDAO.insertEmergencyRole(role);
                } else {
                    role.setUpdatedBy(userId);
                    role.setCompId(vo.getCompId());
                    emergencyControlOrganChartDAO.updateEmergencyRole(role);
                }
            }
        }
        return voList.get(0);
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<SpSearchVO> checkedObjList = emergencyControlOrganChartDAO.getAllReport(spSearchVO);
        if (!checkedObjList.isEmpty()) {
            spSearchVO.setPrintAll(true);
            spSearchVO.setCheckedObjList(checkedObjList);
            reportList = getEmergencyOrgChartReport(request, response, spSearchVO);
        }
        return reportList;
    }


    public List<JasperPrint> getEmergencyOrgChartReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "비상통제조직도";
        String title = "비상통제 조직도";

        // 레포트 출력 타입이 새로형인지 가로형인지
        // 표지 리포트
        String typeMinorCd = "HOR";
        SystemCodeVO systemCodeReqVo = new SystemCodeVO();
        systemCodeReqVo.setMajorCd("OT01");
        List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);
        SystemMinorCodeVO selectedType = systemCodeList.stream()
                .filter(el -> "Y".equals(el.getExtra1()))
                .findFirst()
                .orElse(null); // 혹은 기본값을 설정 가능
        if (selectedType != null) {
            typeMinorCd = selectedType.getMinorCd();
        }
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport(selectedType.getExtra2(), spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            List<EmergencyControlOrganChartVO> chartList = getEmergencyOrgChartList(vo);
            EmergencyControlOrganChartVO chartData = chartList.get(0);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm = "(" + spSearchVO.getWriteYear() + ")" + "비상통제조직도" + "_" + chartData.getChartNm();
            }
            String jrxmlPath = "report/impl/emergencyControlOrganChart/";
            if ("HOR".equals(typeMinorCd)) {
                // 가로형
                jrxmlPath = jrxmlPath + "chart_hor.jrxml";
            } else if ("VER".equals(typeMinorCd)) {
                // 세로형
                jrxmlPath = jrxmlPath + "chart_VER.jrxml";
            }

            //파라미터 설정
            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath(jrxmlPath); // 레포트 파일 경로
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("page", page); // 페이지
            params.put("subPage", subPage); // 서브페이지
            params.put("localPage", localPage); // 로컬페이지

            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);

            // 데이터 바인딩
            params.put("chartId", chartData.getChartId());
            params.put("chartNm", chartData.getChartNm());
            if ("Y".equals(chartData.getConfirmYn())) {
                String confirmAt = DateUtils.formatDate(chartData.getConfirmAt());
                params.put("confirmAt", confirmAt);
            }
            String createdAt = DateUtils.formatDate(chartData.getCreatedAt());
            params.put("createdAt", createdAt);
            params.put("emergencyTypeNm", chartData.getEmergencyTypeNm());

            params.put("chartBlobData", utilsService.convertToInputStream(chartData.getChartBlobData()));
            reportVO.setParameter(params);

            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();


        }
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }
}
