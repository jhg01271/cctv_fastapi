package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.SafetyAndHealthInfoSurveyDAO;
import kr.co.igns.business.planning.model.SafetyAndHealthInfoSurveyVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class SafetyAndHealthInfoSurveyService {
    private final SafetyAndHealthInfoSurveyDAO safetyAndHealthInfoSurveyDAO;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final ReportService reportService;
    private final UtilsService utilsService;

    public List<SafetyAndHealthInfoSurveyVO> getSafetyAndHealthInfoSurvey(@Valid SpSearchVO searchVo) {
        List<SafetyAndHealthInfoSurveyVO> voList = safetyAndHealthInfoSurveyDAO.getSafetyAndHealthInfoSurvey(searchVo);

        return voList;
    }

    public List<SafetyAndHealthInfoSurveyVO> getProcessDataDetail(@Valid SafetyAndHealthInfoSurveyVO searchVo) {
        List<SafetyAndHealthInfoSurveyVO> voList = safetyAndHealthInfoSurveyDAO.getProcessDataDetail(searchVo);

        return voList;
    }

    public List<SafetyAndHealthInfoSurveyVO> getSafetyAndHealthInfoSurveyDetail(@Valid SpSearchVO searchVo) {
        List<SafetyAndHealthInfoSurveyVO> voList = safetyAndHealthInfoSurveyDAO.getSafetyAndHealthInfoSurveyDetail(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public SafetyAndHealthInfoSurveyVO createSafetyAndHealthInfoSurvey(SafetyAndHealthInfoSurveyVO saveVo) throws Exception {
        if ("I".equals(saveVo.getCmd())) {
            // 신규 생성
            saveVo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            // 기존에 동일한 공정 + 차수가 있어도 저장 가능
            safetyAndHealthInfoSurveyDAO.insertSafetyAndHealthInfoSurvey(saveVo);
        } else if ("D".equals(saveVo.getCmd())) {
            // 해당 경우에는 기준정보 반영 시에 기존에 있던 detail data를 다 날린다.
            saveVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            safetyAndHealthInfoSurveyDAO.changeSafetyAndHealthInfoSurvey(saveVo);
            safetyAndHealthInfoSurveyDAO.deleteAllSafetyAndHealthInfoSurveyDetail(saveVo);
        } else {
            // 수정
            saveVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            safetyAndHealthInfoSurveyDAO.changeSafetyAndHealthInfoSurvey(saveVo);
        }

        return saveVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<SafetyAndHealthInfoSurveyVO> createSafetyAndHealthInfoSurveyDetail(List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
        for (SafetyAndHealthInfoSurveyVO saveVo : saveVoList) {
            if ("I".equals(saveVo.getCmd())) {
                saveVo.setDocType("SAHIS");
                saveVo.setCreatedBy(SecurityUtil.getCurrentMemberId());
                safetyAndHealthInfoSurveyDAO.insertSafetyAndHealthInfoSurveyDetail(saveVo);
            } else {
                saveVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                safetyAndHealthInfoSurveyDAO.changeSafetyAndHealthInfoSurveyDetail(saveVo);
            }

        }
        return saveVoList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public SafetyAndHealthInfoSurveyVO confirmSafetyAndHealthInfoSurvey(SafetyAndHealthInfoSurveyVO vo) throws Exception {
        safetyAndHealthInfoSurveyDAO.confirmSafetyAndHealthInfoSurvey(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<SafetyAndHealthInfoSurveyVO> deleteSafetyAndHealthInfoSurvey(List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
        for (SafetyAndHealthInfoSurveyVO saveVo : saveVoList) {
            safetyAndHealthInfoSurveyDAO.changeSafetyAndHealthInfoSurveyUseYn(saveVo);
        }
        return saveVoList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<SafetyAndHealthInfoSurveyVO> deleteSafetyAndHealthInfoSurveyDetail(List<SafetyAndHealthInfoSurveyVO> saveVoList) throws Exception {
        for (SafetyAndHealthInfoSurveyVO saveVo : saveVoList) {
            safetyAndHealthInfoSurveyDAO.deleteSafetyAndHealthInfoSurveyDetail(saveVo);
        }
        return saveVoList;
    }

    // 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(safetyAndHealthInfoSurveyDAO.getAllReport(spSearchVO));
        if(!spSearchVO.getCheckedObjList().isEmpty()) {
            spSearchVO.setPrintAll(true);
            jaspers = getReports(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    // 출력물 다수
    public List<JasperPrint> getReports(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        /*
         * 인트로 페이지
         */
        ReportVO introReportVO = new ReportVO();
        CompVO cpVO = new CompVO();
        SpSearchVO searchVO = new SpSearchVO();
        // 고객사 ID 조회
        searchVO.setClntId(SecurityUtil.getCurrentClntId());

        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(searchVO.getClntId());
        String clntNm = cVo.getClntNm();

        // 사업장 ID 조회
        searchVO.setCompId(SecurityUtil.getCurrentCompId());

        cpVO.setClntId(searchVO.getClntId());
        cpVO.setCompId(searchVO.getCompId());
        CompVO cPo = compDao.getCompById(cpVO);

        String fileNm = "안전보건정보 조사";
//		if (!voList.isEmpty()) {
//			// 공정명 가져오기
//			String processNm = safetyAndHealthInfoSurveyDAO.getProcessName(voList.get(0).getProcessId());
//			if(voList.size() == 1) {
//				fileNm ="("+voList.get(0).getWriteYear()+")"+"안전보건정보 조사_"+processNm;
//			}else {
//				fileNm ="("+voList.get(0).getWriteYear()+")"+"안전보건정보 조사";
//			}
//		}
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, "안전보건정보 조사");
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();

        /*
         * 데이터 페이지
         */
        // 선택한 데이터 전부 출력
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            //출력물 용 데이터 조회
            SafetyAndHealthInfoSurveyVO reportData = safetyAndHealthInfoSurveyDAO.getReportData(vo);
            ReportVO reportVO = new ReportVO();

            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/riskAssessment/safetyAndHealthInfoSurvey/safetyAndHealthInfoSurveyMany.jrxml");
            reportVO.setType("pdf");

            // 파라미터 세팅
            Map<String, Object> params = new HashMap<>();

            //조직명 입력
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            params.put("title", reportData.getProcessNm());
            params.put("processNm", reportData.getProcessNm());
            params.put("product", reportData.getProduct());
            params.put("workerCnt", reportData.getWorkerCnt());
            params.put("material", reportData.getMaterial());
            params.put("bizNm", "안전보건정보 조사");
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전보건정보 조사_" + reportData.getProcessNm();
            } else {
                fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전보건정보 조사";
            }

            // 고객사 로고
            InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);
            //gridData 조회
            List<SafetyAndHealthInfoSurveyVO> resultList = safetyAndHealthInfoSurveyDAO.getReportGridData(vo);
            resultList = resultList.stream()
                    .filter(el -> {
                        boolean isEquipEmpty = el.getEquipContent() == null || el.getEquipContent().trim().isEmpty();
                        boolean isMsdsEmpty = el.getMsdsContent() == null || el.getMsdsContent().trim().isEmpty();
                        // 둘 다 비어있으면 false(필터링)
                        return !(isEquipEmpty && isMsdsEmpty);
                    })
                    .collect(Collectors.toList());
            System.out.println("### resultList "+ resultList);
            // 미사용 설비,msds항목 제거
            params.put("gridData", new JRBeanCollectionDataSource(resultList));

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);

            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
        }
        if (spSearchVO.isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
        }
        return jasperPrintList;

    }
}

