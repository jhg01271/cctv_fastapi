package kr.co.igns.business.participation.service;

import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.participation.dao.postgres.SafetyOrganizationDAO;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.HrVO;
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
import kr.co.igns.business.participation.model.SafetyOrgnRoleVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SafetyOrganizationService {
    private final SafetyOrganizationDAO organizationDao;
    private final SystemCodeDAO systemCodeDao;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<SafetyOrgnRoleVO> getSafetyOrgList(SpSearchVO searchVo) throws Exception {
        List<SafetyOrgnRoleVO> voList = organizationDao.getSafetyOrgList(searchVo);
        return voList;
    }

    public List<SafetyOrgnRoleVO> getDataSetSafetyOrgList(SpSearchVO searchVo) throws Exception {
        List<SafetyOrgnRoleVO> voList = organizationDao.getDataSetSafetyOrgList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<SafetyOrgnRoleVO> saveSafetyOrgList(List<SafetyOrgnRoleVO> reqVo) throws Exception {
        //조직 생성
        //조직ID 자동 부여 (년월일순번)

        for(SafetyOrgnRoleVO vo : reqVo){
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                organizationDao.insertSafetyOrgList(vo);
            }else{
                organizationDao.updateSafetyOrgList(vo);

            }
        }

        return reqVo;
    }

    public List<SafetyOrgChartVO> getOrganizationChart(SpSearchVO searchVo) throws Exception {
        List<SafetyOrgChartVO> voList = organizationDao.getChartList(searchVo);
        return voList;
    }

    public SafetyOrgChartVO getOrganizationChartDetail(String chartId) throws Exception {
        SafetyOrgChartVO voList = organizationDao.getChartDetail(chartId);
        return voList;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertOrganizationChart(SafetyOrgChartVO reqVo) throws Exception {
        //조직 생성
        //조직ID 자동 부여 (년월일순번)
        organizationDao.insertOrganizationChart(reqVo);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateOrganizationChart(SafetyOrgChartVO reqVo) throws Exception {
        //조직 수정
        SafetyOrgChartVO  vo = organizationDao.getChartDetail(reqVo.getChartId());
        if (vo == null)
            return null;
        vo = (SafetyOrgChartVO) SpUtils.objectMap(reqVo, vo);

        organizationDao.updateOrganizationChart(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO confirmOrganizationChart(SafetyOrgChartVO reqVo) throws Exception {
        organizationDao.confirmOrganizationChart(reqVo);
        return reqVo;
    }

    public void deleteOrganizationChart(List<SafetyOrgChartVO> list) throws Exception {
        for (SafetyOrgChartVO tmp : list) {
            organizationDao.deleteOrganizationChart(tmp);
        }
    }
    public List<JasperPrint> getShOrgChartReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String fileNm = "안전보건 조직도";
        String title ="안전보건 조직도";

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
            SafetyOrgChartVO chartData = getOrganizationChartDetail(vo.getSearchText());
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm = "안전보건 조직도" + "_" + chartData.getChartNm();
            }
            String jrxmlPath = "report/participation/shOrgChart/";
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
                String confirmAt = DateUtils.formatDate(chartData.getConfirmDt());
                params.put("confirmAt", confirmAt);
            }
            String createdAt = DateUtils.formatDate(chartData.getCreatedAt());
            params.put("createdAt", createdAt);

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


    public List<HrVO> getSafetyOrgnListById(List<String> idList) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        String idDatas = "";
        for(String id : idList){
            if(idDatas.equals("")){
                idDatas = id;
            }else{
                idDatas += "," + id;
            }
        }
        searchVo.setCompId(SecurityUtil.getCurrentCompId());
        searchVo.setId(idDatas);
        List<HrVO> voList = organizationDao.getSafetyOrgnListById(searchVo);
        return voList;
    }
}
