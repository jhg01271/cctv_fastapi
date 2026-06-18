package kr.co.igns.business.impl.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.igns.business.impl.dao.postgres.EmergencyControlOrganChartDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyControlTaskAsgmtDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyResponseDAO;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.impl.model.EmergencyControlTaskAsgmtVO;
import kr.co.igns.business.impl.model.EmergencyResponseVO;
import kr.co.igns.business.impl.model.EmergencyRoleVO;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.code.YesNo;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyControlTaskAsgmtService {
    private final EmergencyControlTaskAsgmtDAO emergencyControlTaskAsgmtDAO;
    private final UtilsService utilsService;
    private final ReportService reportService;

    public List<EmergencyControlTaskAsgmtVO> getEmergencyControlTaskAsgmtList(SpSearchVO searchVo) throws Exception {
        List<EmergencyControlTaskAsgmtVO> voList = emergencyControlTaskAsgmtDAO.getEmergencyControlTaskAsgmtList(searchVo);
        return voList;
    }

    public EmergencyControlTaskAsgmtVO getEmergencyControlTaskAsgmtDetail(SpSearchVO searchVo) throws Exception {
        List<EmergencyControlTaskAsgmtVO> voList = emergencyControlTaskAsgmtDAO.getEmergencyControlTaskAsgmtList(searchVo);

        voList.get(0).setRoleList(emergencyControlTaskAsgmtDAO.getEmergencyControlTaskAsgmtRoleList(voList.get(0)));

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> chartData = objectMapper.readValue(voList.get(0).getChartData(), new TypeReference<List<Map<String, Object>>>() {
        });

        for (EmergencyControlTaskAsgmtVO role : voList.get(0).getRoleList()) {
            List<String> validOrgnIdList = chartData.stream()
                    .filter(el -> el.get("isParent") != null && !(Boolean) el.get("isParent") && el.get("parentNode") != null && el.get("parentNode").equals(role.getRoleId()))
                    .map(el -> (String) el.get("id"))  // id를 String으로 맵핑
                    .collect(Collectors.toList());  // 결과를 List로 수집
            role.setOrgnIdList(validOrgnIdList);
            role.setHrList(emergencyControlTaskAsgmtDAO.getEmergencyControlTaskAsgmtHrList(role));
        }
        return voList.get(0);
    }

    public EmergencyControlTaskAsgmtVO getOrgnChartData(SpSearchVO searchVo) throws Exception {
        EmergencyControlTaskAsgmtVO vo = emergencyControlTaskAsgmtDAO.getOrgnChartData(searchVo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveEmergencyControlTaskAsgmt(EmergencyControlTaskAsgmtVO vo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(userId);
            emergencyControlTaskAsgmtDAO.insertEmergencyControlTaskAsgmt(vo);
        } else {
            vo.setUpdatedBy(userId);
            emergencyControlTaskAsgmtDAO.updateEmergencyControlTaskAsgmt(vo);
        }
        for (EmergencyControlTaskAsgmtVO role : vo.getRoleList()) {
            emergencyControlTaskAsgmtDAO.deleteEmergencyControlTaskAsgmtHr(role);
            // 디테일 데이터(업무분장)
            if (role.getCmd().equals("I")) {
                role.setWriteYear(vo.getWriteYear());
                role.setDocNo(vo.getDocNo());
                role.setCreatedBy(userId);
                emergencyControlTaskAsgmtDAO.insertEmergencyControlTaskAsgmtRole(role);
            } else {
                role.setWriteYear(vo.getWriteYear());
                role.setDocNo(vo.getDocNo());
                role.setUpdatedBy(userId);
                emergencyControlTaskAsgmtDAO.updateEmergencyControlTaskAsgmtRole(role);
            }
            for (EmergencyControlTaskAsgmtVO hr : role.getHrList()) {
                // 인원매핑정보
                hr.setWriteYear(vo.getWriteYear());
                hr.setDocNo(role.getDocNo());
                hr.setRoleId(role.getRoleId());
                hr.setCreatedBy(userId);
                emergencyControlTaskAsgmtDAO.insertEmergencyControlTaskAsgmtHr(hr);
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteEmergencyControlTaskAsgmt(List<EmergencyControlTaskAsgmtVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for (EmergencyControlTaskAsgmtVO vo : voList) {
            vo.setUpdatedBy(userId);
            emergencyControlTaskAsgmtDAO.deleteEmergencyControlTaskAsgmt(vo);
        }
        return voList.get(0);
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(emergencyControlTaskAsgmtDAO.getAllReport(spSearchVO));
        spSearchVO.setPrintAll(true);
        spSearchVO.setPage(page);
        spSearchVO.setSubPage(subPage);
        spSearchVO.setLocalPage(localPage);
        jaspers = getEmergencyControlTaskAsgmtReport(request, response, spSearchVO);
        reportList.addAll(jaspers);
        spSearchVO.setSubPage(spSearchVO.getSubPage());
        for (JasperPrint report : jaspers) {
            page += report.getPages().size();
            localPage += report.getPages().size();
        }
        return reportList;
    }

    public List<JasperPrint> getEmergencyControlTaskAsgmtReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String title = "비상통제 조직별 업무 분장";
        String fileNm = title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        Set<String> yearSet = new TreeSet<>(); // 파일명 저장을 위한 년도 변수
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/impl/emergencyControlTaskAsgmt/emergencyControlTaskAsgmt.jrxml");
            reportVO.setType(spSearchVO.getType());

            EmergencyControlTaskAsgmtVO result = getEmergencyControlTaskAsgmtDetail(vo); // 데이터
            if (!yearSet.contains(result.getWriteYear())) {  // 중복이 아닐 경우에만 추가
                yearSet.add(result.getWriteYear());
            }
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getTitle();
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("title", result.getTitle());
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            List<Map<String, Object>> datasource = new ArrayList<>();
            for (EmergencyControlTaskAsgmtVO role : result.getRoleList()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("roleNm", role.getRoleNm());
                data.put("task", role.getTask());
                StringBuilder hrNm = new StringBuilder();
                for (EmergencyControlTaskAsgmtVO hr : role.getHrList()) {
                    // hrNm 값이 있는 경우에만 추가
                    if (hr.getHrNm() != null) {
                        hrNm.append(hr.getHrNm().toString()).append("\n");
                    }
                }
                // 마지막 줄바꿈 제거
                if (hrNm.length() > 0) {
                    hrNm.setLength(hrNm.length() - 1);
                }
                data.put("hr", hrNm.toString());
                datasource.add(data);
            }

            params.put("roleList", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String year : yearSet) {
            joiner.add(year);
        }
        fileNm = "(" + joiner.toString() + ")" + fileNm;
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }
}
