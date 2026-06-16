package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.ContractorRegisterDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.support.model.SafetyAndHealthCommPlanDetailVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractorRegisterService {
    private final ContractorRegisterDAO contractorRegisterDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasFileService;

    public List<ContractorRegisterVO> getConstractorRegisterList(SpSearchVO searchVo) throws Exception {

        List<ContractorRegisterVO> voList = contractorRegisterDAO.getConstractorRegisterList(searchVo);
        for (ContractorRegisterVO vo : voList) {
            vo.setHrList(contractorRegisterDAO.getConstractorRegisterHrList(vo));
        }

        return voList;
    }

    public List<JasperPrint> getConstractorRegisterReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        //String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "협력업체 등록대장";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        ReportVO reportVO = new ReportVO();
        reportVO.setJrxmlPath("report/impl/contractorRegister/contractorRegister.jrxml");
        reportVO.setType(spSearchVO.getType());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("title", title);
        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", logo2);

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        UtilsVO signParam = new UtilsVO();
        signParam.setDocType("CTR");
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, spSearchVO.getWriteYear()+spSearchVO.getCompId());
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));




        List<ContractorRegisterVO> resultList = contractorRegisterDAO.getSelectedConstractorRegisterList(spSearchVO.getSearchText());
        List<Map<String, Object>> datasource = new ArrayList<>();
        int index = 1;
        int rowCount = 0;
        for(ContractorRegisterVO vo : resultList) {
            fileNm += "_" +vo.getPartCompNm();  //파일명
            Map<String, Object> data = new HashMap<>();
            data.put("regDt", DateUtils.formatDate(vo.getRegDt()));   //등록일자
            data.put("partCompNm", vo.getPartCompNm()); //협력업체명
            //담당자 데이터는 resultList에 없으므로 for문으로 일치하는 partCompId 찾아서 일치하는 협력업체에 담당자가 들어갈 수 있도록 함
            for(int i = 0; i < spSearchVO.getReportNmList().size(); i++){
                if(spSearchVO.getReportNmList().get(i).get("partCompId") != null && vo.getPartCompId().equals(spSearchVO.getReportNmList().get(i).get("partCompId"))){
                    if(spSearchVO.getReportNmList().get(i).get("hrNm") != null){
                        data.put("hrNm", spSearchVO.getReportNmList().get(i).get("hrNm") + " (정)");
                    }
                    if(spSearchVO.getReportNmList().get(i).get("hrSubNm") != null){
                        data.put("hrNm", data.get("hrNm") + "  " + spSearchVO.getReportNmList().get(i).get("hrSubNm") + " (부)");
                    }
                    if(spSearchVO.getReportNmList().get(i).get("partnerNm") != null){
                        data.put("partnerNm", spSearchVO.getReportNmList().get(i).get("partnerNm") + " (정)");
                    }
                    if(spSearchVO.getReportNmList().get(i).get("partnerSubNm") != null){
                        data.put("partnerNm", data.get("partnerNm") + "  " + spSearchVO.getReportNmList().get(i).get("partnerSubNm") + " (부)");
                    }
                    if(spSearchVO.getReportNmList().get(i).get("partShHrNm") != null){
                        data.put("partShHrNm", spSearchVO.getReportNmList().get(i).get("partShHrNm"));
                    }
                }
            }
            String address = "";
            address += vo.getZipCd() != null && !vo.getZipCd().equals("") ? "(" + vo.getZipCd() + ")" : "";
            address += vo.getAddrs1() != null && !vo.getAddrs1().equals("") ? " " + vo.getAddrs1() : "";
            address += vo.getAddrs2() != null && !vo.getAddrs2().equals("") ? " ," + vo.getAddrs2() : "";
            data.put("fullAddress", address); //주소
            data.put("partCompItemNm", vo.getPartCompItemNm()); //품목
            data.put("telNo", vo.getTelNo()); //전화번호
            data.put("index", index);   //등록번호
            index++;
            datasource.add(data);
        }

        if (datasource.isEmpty()) {
            Map<String, Object> data = new HashMap<>();
            data.put("index", null);
            datasource.add(data);
        }
        params.put("dataList", new JRBeanCollectionDataSource(datasource));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }
}
