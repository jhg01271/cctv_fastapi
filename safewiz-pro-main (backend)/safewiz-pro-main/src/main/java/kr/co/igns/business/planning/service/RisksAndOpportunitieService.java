package kr.co.igns.business.planning.service;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.RisksAndOpportunitieDAO;
import kr.co.igns.business.planning.model.LegalReviewVO;
import kr.co.igns.business.planning.model.RisksAndOpportunitieVO;
import kr.co.igns.business.utils.model.UtilsVO;
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
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class RisksAndOpportunitieService {
    private final RisksAndOpportunitieDAO risksAndOpportunitieDAO;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final ClientDAO clientDao;
    private final CompDAO compDao;

    public List<RisksAndOpportunitieVO> getRisksAndOpportunities(RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> voList = risksAndOpportunitieDAO.getRisksAndOpportunities(vo);
        return voList;
    }

    public List<RisksAndOpportunitieVO> getRiskDetail(RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> voList = risksAndOpportunitieDAO.getRiskDetail(vo);
        return voList;
    }

    public List<RisksAndOpportunitieVO> getOppDetail(RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> voList = risksAndOpportunitieDAO.getOppDetail(vo);
        return voList;
    }

    public List<RisksAndOpportunitieVO> getParDetail(RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> voList = risksAndOpportunitieDAO.getParDetail(vo);
        return voList;
    }

    public List<RisksAndOpportunitieVO> getAsmtApi(RisksAndOpportunitieVO vo) throws Exception {
        List<RisksAndOpportunitieVO> voList = risksAndOpportunitieDAO.getAsmtApi(vo);
        return voList;
    }

    // 저장
    public void saveRisksAndOpportunities(List<RisksAndOpportunitieVO> reqVo) throws Exception {
        String newDocNo = null;
        boolean applyDocNo = false;

        for (RisksAndOpportunitieVO item : reqVo) {
            if (item.getDocNo() == null || item.getDocNo().equals("")) {
                item.setCreatedBy(SecurityUtil.getCurrentMemberId());

                // 메인 데이터 저장
                if (!applyDocNo) {
                    risksAndOpportunitieDAO.insertRisksAndOpp(item);
                    applyDocNo = true;
                    newDocNo = item.getDocNo();
                }

                // 신규 데이터의 경우 공통 문서 번호 설정
                item.setDocNo(newDocNo);

                // 세부 데이터 저장 - 리스크, 기회, 참여자
                if ("RISK".equals(item.getType())) {
                    risksAndOpportunitieDAO.insertRiskDetail(item);
                } else if ("OPP".equals(item.getType())) {
                    risksAndOpportunitieDAO.insertOppDetail(item);
                } else if ("PAR".equals(item.getType())) {
                    risksAndOpportunitieDAO.insertParDetail(item);
                }
            } else {
                // 기존 데이터 업데이트
                item.setUpdatedBy(SecurityUtil.getCurrentMemberId());
                risksAndOpportunitieDAO.updateRisksAndOpp(item);

                // 리스크 세부 데이터 업데이트
                if ("RISK".equals(item.getType())) {
                    if (item.getCmd().equals("I")) {
                        item.setCreatedBy(SecurityUtil.getCurrentMemberId());
                        risksAndOpportunitieDAO.insertRiskDetail(item);
                    } else {
                        risksAndOpportunitieDAO.updateRiskDetail(item);
                    }
                }
                // 기회 세부 데이터 업데이트
                else if ("OPP".equals(item.getType())) {
                    if (item.getCmd().equals("I")) {
                        item.setCreatedBy(SecurityUtil.getCurrentMemberId());
                        risksAndOpportunitieDAO.insertOppDetail(item);
                    } else {
                        risksAndOpportunitieDAO.updateOppDetail(item);
                    }
                }
                // 참여자 세부 데이터 업데이트
                else if ("PAR".equals(item.getType())) {
                    if (item.getCmd().equals("I")) {
                        item.setCreatedBy(SecurityUtil.getCurrentMemberId());
                        risksAndOpportunitieDAO.insertParDetail(item);
                    } else {
                        risksAndOpportunitieDAO.updateParDetail(item);
                    }
                }
            }
        }
    }

    // 메인 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRisksAndOpportunitesMain(List<RisksAndOpportunitieVO> reqVo) throws Exception {
        for (RisksAndOpportunitieVO item : reqVo) {
            item.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            risksAndOpportunitieDAO.deleteRisksAndOpportunitesMain(item);
            /* 문서 삭제 시 참여자 TASK의 useYn처리를 하기 위함 */
            String reqInfoKey = item.getDocType()+"^&"+item.getWriteYear()+"^&"+item.getDocNo();
            risksAndOpportunitieDAO.customUpdateTaskYn(reqInfoKey);
        }
    }

    // 상세 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRisksAndOpportunitesDetail(List<RisksAndOpportunitieVO> reqVo) throws Exception {
        for (RisksAndOpportunitieVO item : reqVo) {
            item.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            if ("RISK".equals(item.getType())) {
                risksAndOpportunitieDAO.deleteRisk(item);
            }
            if ("OPP".equals(item.getType())) {
                risksAndOpportunitieDAO.deleteOpp(item);
            }
            if ("PAR".equals(item.getType())) {
                risksAndOpportunitieDAO.deletePar(item);
            }
        }
    }

    // 리포트 출력
    public List<JasperPrint> getRisksAndOpportunitiesReport(HttpServletRequest request, HttpServletResponse response, List<RisksAndOpportunitieVO> spSearchVO) throws Exception {
        // 고객사 ID 조회
        List<JasperPrint> reportList = new ArrayList<>();
        spSearchVO.get(0).setClntId(SecurityUtil.getCurrentClntId());
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
        SpSearchVO spVo = new SpSearchVO();
        spVo.setPage(page);
        spVo.setSubPage(subPage);
        spVo.setLocalPage(localPage);
        String title = "리스크 및 기회 관리대장";
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront", spVo, title);
        reportList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();

        // 1. 서명 데이터 조회
        SpSearchVO signParam = new SpSearchVO();
        signParam.setDocType(spSearchVO.get(0).getDocType());
        signParam.setWriteYear(spSearchVO.get(0).getWriteYear());
        signParam.setDocNo(spSearchVO.get(0).getDocNo());
        signParam.setType("sign");
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        // 1. 조직명
        params.put("orgnNm", spSearchVO.get(0).getOrgnNm());

        List<RisksAndOpportunitieVO> riskData = new ArrayList<>();
        List<RisksAndOpportunitieVO> oppData = new ArrayList<>();
        List<RisksAndOpportunitieVO> parData = new ArrayList<>();

        if (spSearchVO.get(0).getNoCheckPrint().equals("N")) {
            // 2. 리스크 관리 대장 조회
            for (RisksAndOpportunitieVO item : spSearchVO) {
                if (item.getRiskDocSeq() != null) {
                    List<RisksAndOpportunitieVO> data = risksAndOpportunitieDAO.getReportRisk(item);

                    for(RisksAndOpportunitieVO dataVO : data){
                        dataVO.setRiskMeasureEdDt(dataVO.getRiskMeasureEdDt() != null && !dataVO.getRiskMeasureEdDt().isEmpty() ? DateUtils.formatDate(dataVO.getRiskMeasureEdDt()) : "");
                        dataVO.setRiskMeasureStDt(dataVO.getRiskMeasureStDt() != null && !dataVO.getRiskMeasureStDt().isEmpty() ? DateUtils.formatDate(dataVO.getRiskMeasureStDt()) : "");
                    }

                    riskData.addAll(data);
                }
            }

            // 3. 기회 관리 대장 조회
            for (RisksAndOpportunitieVO item : spSearchVO) {
                if (item.getOppDocSeq() != null) {
                    List<RisksAndOpportunitieVO> data = risksAndOpportunitieDAO.getReportOpp(item);
                    for(RisksAndOpportunitieVO dataVO : data){
                        dataVO.setOppMeasureEdDt(dataVO.getOppMeasureEdDt() != null && !dataVO.getOppMeasureEdDt().isEmpty() ? DateUtils.formatDate(dataVO.getOppMeasureEdDt()) : "");
                        dataVO.setOppMeasureStDt(dataVO.getOppMeasureStDt() != null && !dataVO.getOppMeasureStDt().isEmpty() ? DateUtils.formatDate(dataVO.getOppMeasureStDt()) : "");
                    }
                    oppData.addAll(data);
                }
            }

            // 4. 리스크 및 기회 평가 참여자 조회
            for (RisksAndOpportunitieVO item : spSearchVO) {
                if (item.getParDocSeq() != null) {
                    List<RisksAndOpportunitieVO> data = risksAndOpportunitieDAO.getReportPar(item);
                    parData.addAll(data);
                }
            }
        } else {
            List<RisksAndOpportunitieVO> data1 = risksAndOpportunitieDAO.getReportRisk2(spSearchVO.get(0));
            for(RisksAndOpportunitieVO data : data1){
                data.setRiskMeasureEdDt(data.getRiskMeasureEdDt() != null && !data.getRiskMeasureEdDt().isEmpty() ? DateUtils.formatDate(data.getRiskMeasureEdDt()) : "");
                data.setRiskMeasureStDt(data.getRiskMeasureStDt() != null && !data.getRiskMeasureStDt().isEmpty() ? DateUtils.formatDate(data.getRiskMeasureStDt()) : "");
            }

            List<RisksAndOpportunitieVO> data2 = risksAndOpportunitieDAO.getReportOpp2(spSearchVO.get(0));
            for(RisksAndOpportunitieVO data : data2){
                data.setOppMeasureEdDt(data.getOppMeasureEdDt() != null && !data.getOppMeasureEdDt().isEmpty() ? DateUtils.formatDate(data.getOppMeasureEdDt()) : "");
                data.setOppMeasureStDt(data.getOppMeasureStDt() != null && !data.getOppMeasureStDt().isEmpty() ? DateUtils.formatDate(data.getOppMeasureStDt()) : "");
            }

            List<RisksAndOpportunitieVO> data3 = risksAndOpportunitieDAO.getReportPar2(spSearchVO.get(0));

            riskData.addAll(data1);
            oppData.addAll(data2);
            parData.addAll(data3);
        }

        for (int i = 0; i < parData.size(); i++) {
            parData.get(i).setNo(Integer.toString(i + 1));

            if (parData.get(i).getSignature() != null) {
                parData.get(i).setSignatureImg(utilsService.getSignatureFromBase64String(parData.get(i).getSignature()));
            }
        }

        if (riskData.size() == 0) {
            riskData.add(new RisksAndOpportunitieVO());
        }

        if (oppData.size() == 0) {
            oppData.add(new RisksAndOpportunitieVO());
        }

        if (parData.size() == 0) {
            parData.add(new RisksAndOpportunitieVO());
        }

        params.put("Dataset1", new JRBeanCollectionDataSource(riskData));
        params.put("Dataset2", new JRBeanCollectionDataSource(oppData));
        params.put("Dataset3", new JRBeanCollectionDataSource(parData));
        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        ReportVO reportVO = new ReportVO();

        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String fileNm = "";
        fileNm += "(" + spSearchVO.get(0).getWriteYear() + ")";
        fileNm += "리스크 및 기회 관리대장_" + spSearchVO.get(0).getOrgnNm();
//    	fileNm += spSearchVO.get(0).getOrgnNm();
//		fileNm += "_리스크 및 기회 관리대장";

        // 출력 파일 명 설정
        reportVO.setFileName(fileNm);

        // 출력 생성용 Jasper 파일 위치
        reportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage.jrxml");

        // 출력  파일 형식 지정
        reportVO.setType(spSearchVO.get(0).getType());

        /**********************/
        /****** 출력물 출력 *******/
        /**********************/
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page += JasperReport.getPages().size();
        localPage += JasperReport.getPages().size();

        // 통합 출력인 경우 reportVO만 반환하고 종료
        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (spSearchVO.get(0).isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.get(0).getType(), fileNm);
        }
        return reportList;
    }

    // 일괄출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<JasperPrint> jaspers = new ArrayList<>();
        List<RisksAndOpportunitieVO> list = risksAndOpportunitieDAO.getAllReport(spSearchVO);
        if (!list.isEmpty()) {
            list.get(0).setPrintAll(true);
            list.get(0).setPage(page);
            list.get(0).setSubPage(subPage);
            list.get(0).setLocalPage(localPage);
            list.get(0).setExtra1(spSearchVO.getWriteYear() + "년도");
            jaspers = getRisksAndOpportunitiesCardReport(request, response, list);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    // 리포트 출력 card
    public List<JasperPrint> getRisksAndOpportunitiesCardReport(HttpServletRequest request, HttpServletResponse response, List<RisksAndOpportunitieVO> dataVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();
        RisksAndOpportunitieVO riskVo = new RisksAndOpportunitieVO();
        riskVo.setCompId((String) dataVO.get(0).getCompId());

        String yearValue = "";
        Set<String> yearSet = new HashSet<>();  // 중복 제거를 위한 Set 사용

        for (RisksAndOpportunitieVO item : dataVO) {
            yearSet.add(item.getWriteYear());  // Set에 추가하면 자동으로 중복이 제거됨
        }

        // Set에 저장된 값을 구분자로 연결하여 yearValue 생성
        yearValue = String.join(",", yearSet);

        // 고객사 ID 조회
        riskVo.setClntId(SecurityUtil.getCurrentClntId());
        // 고객사 명칭 조회
        // 사업장 ID 조회
        riskVo.setCompId(SecurityUtil.getCurrentCompId());

        // 사업장 명칭 조회
        CompVO cpVO = new CompVO();
        cpVO.setClntId(riskVo.getClntId());
        cpVO.setCompId(riskVo.getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();

        String fileNm = "";
        fileNm = "(" + yearValue + ")리스크 및 기회 관리대장";

        int page = dataVO.get(0).getPage();
        int subPage = dataVO.get(0).getSubPage();
        int localPage = dataVO.get(0).getLocalPage();
        SpSearchVO spSearchVO = new SpSearchVO();
        spSearchVO.setPage(page);
        spSearchVO.setSubPage(subPage);
        spSearchVO.setLocalPage(localPage);
        if (dataVO.get(0).getExtra1() != null) {
            // 일괄출력 시 년도 구분용
            spSearchVO.setExtra1(dataVO.get(0).getExtra1());
        }
        if (!dataVO.get(0).isPrintAll()) {
            JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "리스크 및 기회 관리대장");
            jasperPrintList.add(JasperFrontReport);
            page = page + JasperFrontReport.getPages().size();
            localPage = localPage + JasperFrontReport.getPages().size();
        }

        for (RisksAndOpportunitieVO row : dataVO) {

            RisksAndOpportunitieVO riskDataVo = new RisksAndOpportunitieVO();
            Map<String, Object> params = new HashMap<String, Object>();

            row.setClntId(SecurityUtil.getCurrentClntId());
            InputStream logo = utilsService.getClntLogo(row.getClntId());
            params.put("logo", logo);
            params.put("compNm", compNm);
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 1. 서명 데이터 조회
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(row.getDocType());
            signParam.setTargetId(row.getWriteYear() + row.getDocNo());
            signParam.setType("sign");

            List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회
            for (UtilsVO signInfo : signList) {
                // 서명 데이터 입력
                if (signInfo.getParam().equals("writer")) {
                    params.put("signWriter", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                    params.put("signWriterNm", signInfo.getHrNm());
                }
                if (signInfo.getParam().equals("reviewer")) {
                    params.put("signReviewer", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                    params.put("signReviewerNm", signInfo.getHrNm());
                }
                if (signInfo.getParam().equals("approver")) {
                    params.put("signApprover", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                    params.put("signApproverNm", signInfo.getHrNm());
                }
            }

            // 1. 조직명
            params.put("orgnNm", row.getOrgnNm());

            ReportVO riskReportVO = new ReportVO();
            riskReportVO.setFileName(fileNm);
            riskReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Risk.jrxml");
            riskReportVO.setType("pdf");

            List<RisksAndOpportunitieVO> riskData = risksAndOpportunitieDAO.getReportRisk2(row);

            if (riskData.size() == 0) {
                riskData.add(new RisksAndOpportunitieVO());
            }else{
                for(RisksAndOpportunitieVO risk : riskData){
                    risk.setRiskMeasureEdDt(risk.getRiskMeasureEdDt() != null && !risk.getRiskMeasureEdDt().isEmpty() ? DateUtils.formatDate(risk.getRiskMeasureEdDt()) : "");
                    risk.setRiskMeasureStDt(risk.getRiskMeasureStDt() != null && !risk.getRiskMeasureStDt().isEmpty() ? DateUtils.formatDate(risk.getRiskMeasureStDt()) : "");
                }
            }

            params.put("Dataset_risk", new JRBeanCollectionDataSource(riskData));

            riskReportVO.setParameter(params);

            JasperPrint riskJasperReport = reportService.allReportJasperPrint(riskReportVO);
            jasperPrintList.add(riskJasperReport);
            page = page + riskJasperReport.getPages().size();
            localPage = localPage + riskJasperReport.getPages().size();


            Map<String, Object> paramsOpp = new HashMap<String, Object>();

            ReportVO oppReportVO = new ReportVO();
            oppReportVO.setFileName(fileNm);
            oppReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Opp.jrxml");
            oppReportVO.setType("pdf");

            List<RisksAndOpportunitieVO> oppData = risksAndOpportunitieDAO.getReportOpp2(row);

            if (oppData.size() == 0) {
                oppData.add(new RisksAndOpportunitieVO());
            }else{
                for(RisksAndOpportunitieVO opp : oppData){
                    opp.setOppMeasureEdDt(opp.getOppMeasureEdDt() != null && !opp.getOppMeasureEdDt().isEmpty() ? DateUtils.formatDate(opp.getOppMeasureEdDt()) : "");
                    opp.setOppMeasureStDt(opp.getOppMeasureStDt() != null && !opp.getOppMeasureStDt().isEmpty() ? DateUtils.formatDate(opp.getOppMeasureStDt()) : "");
                }
            }

            paramsOpp.put("Dataset_opp", new JRBeanCollectionDataSource(oppData));
            InputStream opplogo = utilsService.getClntLogo(row.getClntId());
            paramsOpp.put("logo", opplogo);
            paramsOpp.put("page", page);
            paramsOpp.put("subPage", subPage);
            paramsOpp.put("localPage", localPage);

            oppReportVO.setParameter(paramsOpp);

            JasperPrint oppJasperReport = reportService.allReportJasperPrint(oppReportVO);
            jasperPrintList.add(oppJasperReport);
            page = page + oppJasperReport.getPages().size();
            localPage = localPage + oppJasperReport.getPages().size();


            Map<String, Object> paramsPar = new HashMap<String, Object>();

            ReportVO parReportVO = new ReportVO();
            parReportVO.setFileName(fileNm);
            parReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Par.jrxml");
            parReportVO.setType("pdf");

            List<RisksAndOpportunitieVO> parData = risksAndOpportunitieDAO.getReportPar2(row);

            for (int i = 0; i < parData.size(); i++) {
                parData.get(i).setNo(Integer.toString(i + 1));

                if (parData.get(i).getSignature() != null) {
                    parData.get(i).setSignatureImg(utilsService.getSignatureFromBase64String(parData.get(i).getSignature()));
                }
            }
            paramsPar.put("Dataset_par", new JRBeanCollectionDataSource(parData));
            InputStream parlogo = utilsService.getClntLogo(row.getClntId());
            paramsPar.put("logo", parlogo);
            paramsPar.put("page", page);
            paramsPar.put("subPage", subPage);
            paramsPar.put("localPage", localPage);

            parReportVO.setParameter(paramsPar);

            JasperPrint parJasperReport = reportService.allReportJasperPrint(parReportVO);
            jasperPrintList.add(parJasperReport);
            page += parJasperReport.getPages().size();
            localPage += parJasperReport.getPages().size();

        }

        if (dataVO.get(0).isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, dataVO.get(0).getType(), fileNm);
        }
        return jasperPrintList;
    }


//		public void getRisksAndOpportunitiesCardReport(HttpServletRequest request, HttpServletResponse response, List<RisksAndOpportunitieVO> spSearchVO) throws Exception {
//			
//			List<JasperPrint> jasperPrintList = new ArrayList<>();
//			
//			
//			String yearValue = "";
//			Set<String> yearSet = new HashSet<>();
//			
//			System.out.print("111111111");
//			
//			for(RisksAndOpportunitieVO item : spSearchVO) {
//				yearSet.add(item.getWriteYear());
//			}
//			
//			yearValue = String.join(",", yearSet);
//			
//			System.out.print("222222222");
//			System.out.print("yearValue");
//			
//			
//			// 고객사 ID 조회
//			spSearchVO.get(0).setClntId(SecurityUtil.getCurrentClntId());
//	    	// 고객사 명칭 조회
//			ClientVO cVo = clientDao.getClientById(spSearchVO.get(0).getClntId());
//			String clntNm = cVo.getClntNm();
//			// 사업장 ID 조회
//			spSearchVO.get(0).setCompId(SecurityUtil.getCurrentCompId());
//	    	
//			// 사업장 명칭 조회
//			CompVO cpVO = new CompVO();
//			cpVO.setClntId(spSearchVO.get(0).getClntId());
//			cpVO.setCompId(spSearchVO.get(0).getCompId());
//			CompVO cPo = compDao.getCompById(cpVO);
//			String compNm = cPo.getCompNm();
//			
//			System.out.print("3333333333");
//			
//			Map<String, Object> params = new HashMap<String, Object>();
//			
//			InputStream logo = utilsService.getClntLogo(spSearchVO.get(0).getClntId());
//			params.put("logo", logo);
//			params.put("compNm", compNm);
//			
//			System.out.print("4444444444");
//			
//			// 1. 서명 데이터 조회
//			UtilsVO signParam = new UtilsVO();
//			signParam.setTargetType(spSearchVO.get(0).getDocType());
//			signParam.setTargetId(spSearchVO.get(0).getWriteYear() + spSearchVO.get(0).getDocNo());
//			
//			List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회
//			for (UtilsVO signInfo : signList) {
//				// 서명 데이터 입력
//				if (signInfo.getParam().equals("writer")) {
//					params.put("signWriter", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//					params.put("signWriterNm", signInfo.getHrNm());
//				}
//				if (signInfo.getParam().equals("reviewer")) {
//					params.put("signReviewer", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//					params.put("signReviewerNm", signInfo.getHrNm());
//				}
//				if (signInfo.getParam().equals("approver")) {
//					params.put("signApprover", utilsService.getSignatureFromBase64String(signInfo.getSignature()));
//					params.put("signApproverNm", signInfo.getHrNm());
//				}
//			}
//			
//			System.out.print("5555555555");
//			
//			// 1. 조직명
//			params.put("orgnNm", spSearchVO.get(0).getOrgnNm());
//			
//			
//			
//			ReportVO introReportVO = new ReportVO();
//			SpSearchVO searchVO = new SpSearchVO();
//			
//			// 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜) 
//	    	String fileNm = "";
//	    	fileNm += "(" + yearValue + ")";
//	    	fileNm += "_리스크 및 기회 관리대장_" + spSearchVO.get(0).getOrgnNm();
////	    	fileNm += spSearchVO.get(0).getOrgnNm();
////			fileNm += "_리스크 및 기회 관리대장";
//	    	introReportVO.setFileName(fileNm);
//	    	introReportVO.setJrxmlPath("report/utils/basicFront_reverse.jrxml");
//			introReportVO.setType("pdf");
//			
//			
//			Map<String, Object> paramsTitle = new HashMap<>();
//			paramsTitle.put("clntNm", clntNm); 
//			paramsTitle.put("compNm", compNm);
//			paramsTitle.put("title", "리스크와 기회 관리대");
//			paramsTitle.put("subTitle", "사업장명: " + compNm);
//			paramsTitle.put("page", searchVO.getPage());
//		    paramsTitle.put("subPage", searchVO.getSubPage());
//		    paramsTitle.put("localPage", searchVO.getLocalPage());
//		    InputStream logoTitle = utilsService.getClntLogo(spSearchVO.get(0).getClntId());
//		    
//		    BufferedImage image = ImageIO.read(logoTitle);
//		    
//		    System.out.print("이미지?");
//		    System.out.print(image);
//		    paramsTitle.put("logo", logoTitle);
//		    
//		    introReportVO.setParameter(paramsTitle);
//	        JasperPrint JasperIntroReport = reportService.allReportJasperPrint(introReportVO);
//	        jasperPrintList.add(JasperIntroReport);
//			
//	        
//	        System.out.print("6666666666");
//			
//			
//			
//			
//			for(RisksAndOpportunitieVO row : spSearchVO) {
//				
//				ReportVO riskReportVO = new ReportVO();
////				ReportVO oppReportVO = new ReportVO();
////				ReportVO parReportVO = new ReportVO();
//				
//				List<RisksAndOpportunitieVO> riskData = new ArrayList<>();
////				List<RisksAndOpportunitieVO> oppData = new ArrayList<>();
////				List<RisksAndOpportunitieVO> parData = new ArrayList<>();
////				
//				//reportVO.setFileName(fileNm);
//				//reportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage.jrxml");
//				//reportVO.setType("pdf");
//				
//				riskReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Risk.jrxml");
////				oppReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Opp.jrxml");
////				parReportVO.setJrxmlPath("report/planning/risksAndOpportunitiesManage/RisksAndOpportunitiesManage_Par.jrxml");
////				
//				
//				List<RisksAndOpportunitieVO> dataRisk = risksAndOpportunitieDAO.getReportRisk2(row);
////				List<RisksAndOpportunitieVO> dataOpp = risksAndOpportunitieDAO.getReportOpp2(row);
////				List<RisksAndOpportunitieVO> dataPar = risksAndOpportunitieDAO.getReportPar2(row);
////										
//				riskData.addAll(dataRisk);
////				oppData.addAll(dataOpp);
////				parData.addAll(dataPar);
////				
////				for(int i = 0; i < parData.size(); i++) {
////					parData.get(i).setNo(Integer.toString(i + 1));
////					
////					if (parData.get(i).getSignature() != null) {
////						parData.get(i).setSignatureImg(utilsService.getSignatureFromBase64String(parData.get(i).getSignature()));
////					}
////					System.out.print("777777777777");
////				}
//				
//				if(riskData.size() == 0) {
//					riskData.add(new RisksAndOpportunitieVO());
//				}
////				
////				System.out.print("888888888888");
////				if(oppData.size() == 0) {
////					oppData.add(new RisksAndOpportunitieVO());
////				}
////				System.out.print("99999999999999");
////				if(parData.size() == 0) {
////					parData.add(new RisksAndOpportunitieVO()); 
////				}
////				System.out.print("101001010101010");
////				
////				Map<String, Object> paramsOpp = new HashMap<String, Object>();
////				Map<String, Object> paramsPar = new HashMap<String, Object>();
////				
//				
//				
//				params.put("page", searchVO.getPage());
//				params.put("subPage", searchVO.getSubPage());
//				params.put("localPage", searchVO.getLocalPage());
//				params.put("logo", logoTitle);
//				params.put("Dataset_risk", new JRBeanCollectionDataSource(riskData));
////				
////				paramsOpp.put("page", searchVO.getPage());
////				paramsOpp.put("subPage", searchVO.getSubPage());
////				paramsOpp.put("localPage", searchVO.getLocalPage());
////				paramsOpp.put("logo", logoTitle);
////				paramsOpp.put("Dataset_opp", new JRBeanCollectionDataSource(oppData));
////				
////				paramsPar.put("page", searchVO.getPage());
////				paramsPar.put("subPage", searchVO.getSubPage());
////				paramsPar.put("localPage", searchVO.getLocalPage());
////				paramsPar.put("logo", logoTitle);
////				paramsPar.put("Dataset_par", new JRBeanCollectionDataSource(parData));
////				
//				System.out.print("11 11 11 11 11 11");
//				riskReportVO.setParameter(params); 
////				oppReportVO.setParameter(paramsOpp);
////				parReportVO.setParameter(paramsPar);
//				System.out.print("12 12 12 12 12");
//				JasperPrint RiskJasperReport = reportService.allReportJasperPrint(riskReportVO);
////				JasperPrint OppJasperReport = reportService.allReportJasperPrint(oppReportVO);
////				JasperPrint ParJasperReport = reportService.allReportJasperPrint(parReportVO);
//				System.out.print("13 13 13 13 13 13");
//				jasperPrintList.add(RiskJasperReport);
////				jasperPrintList.add(OppJasperReport);
////				jasperPrintList.add(ParJasperReport);
//				System.out.print("141414141414");
//			}
//			
//			reportService.exportReportAll(request, response, jasperPrintList, "pdf", fileNm);
//			
//	    }

}
