package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.ContractorInvestigationFormDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyControlOrganChartDAO;
import kr.co.igns.business.impl.dao.postgres.EmergencyResponseDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.participation.model.SafetyOrgChartVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.dao.postgres.PartnerDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.PartnerVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
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

@Service
@RequiredArgsConstructor
public class ContractorInvestigationFormService {
    private final ContractorInvestigationFormDAO contractorInvestigationFormDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasFileService;
    private final PartnerDAO partnerDao;
    private final HrDAO hrDao;

    public List<ContractorInvestigationFormVO> getInvestigationFormList(SpSearchVO searchVo) throws Exception {
        List<ContractorInvestigationFormVO> voList = contractorInvestigationFormDAO.getInvestigationFormList(searchVo);
        for (ContractorInvestigationFormVO vo : voList) {
            vo.setDetailList(contractorInvestigationFormDAO.getInvestigationFormDetail(vo));
            if (searchVo.getDocNo() != null && !searchVo.getDocNo().isEmpty()) {
                // 상세조회일 경우에만 파일 조회
                List<FileVO> files = fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "CIF");
                if (!files.isEmpty()) vo.setFileId(files.get(0).getFileId());
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertPartner(List<MultipartFile> files, List<MultipartFile> files2, PartnerVO reqVo) throws Exception {

        reqVo.setCompId(SecurityUtil.getCurrentCompId());
        //협력사 생성
        //협력사 ID 자동 부여 (년월일순번)
        partnerDao.insertPartner(reqVo);

        //인원 매핑 - 본사담당자(정/부), 협력사담당자(정/부) 추가(target_id = part_comp_id)
//        String targetType = "partner";
//        BaseMapVO HrInfo1 = new BaseMapVO(targetType,reqVo.getPartCompId(),reqVo.getHrIdH(),"HEAD",reqVo.getCreatedBy());
//        BaseMapVO HrInfo2 = new BaseMapVO(targetType,reqVo.getPartCompId(),reqVo.getHrIdS(),"2ND",reqVo.getCreatedBy());
//        BaseMapVO HrInfo3 = new BaseMapVO(targetType,reqVo.getPartCompId(),reqVo.getPartnerHrIdH(),"P_HEAD",reqVo.getCreatedBy());
//        BaseMapVO HrInfo4 = new BaseMapVO(targetType,reqVo.getPartCompId(),reqVo.getPartnerHrIdS(),"P_2ND",reqVo.getCreatedBy());
//
//        hrDao.addHrMap(HrInfo1);
//        hrDao.addHrMap(HrInfo2);
//        hrDao.addHrMap(HrInfo3);
//        hrDao.addHrMap(HrInfo4);


        //계약 대상업체 조사표 테이블 part_comp_id 업데이트
        ContractorInvestigationFormVO vo = new ContractorInvestigationFormVO();
        vo.setPartCompId(reqVo.getPartCompId());
        vo.setDocType(reqVo.getDocType());
        vo.setDocNo(reqVo.getDocNo());
        vo.setWriteYear(reqVo.getWriteYear());
        vo.setCompId(reqVo.getCompId());
        contractorInvestigationFormDAO.updateInvestigationFormToPartCompId(vo);
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveInvestigationForm(List<MultipartFile> files, ContractorInvestigationFormVO vo) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(userId);
            contractorInvestigationFormDAO.insertInvestigationForm(vo);
        } else {
            vo.setUpdatedBy(userId);
            contractorInvestigationFormDAO.updateInvestigationForm(vo);
            fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
        }
        contractorInvestigationFormDAO.deleteInvestigationFormDetail(vo);
        for (ContractorInvestigationFormDetailVO detail : vo.getDetailList()) {
            detail.setWriteYear(vo.getWriteYear());
            detail.setDocType(vo.getDocType());
            detail.setDocNo(vo.getDocNo());
            detail.setCreatedBy(userId);
            detail.setCompId(vo.getCompId());
            contractorInvestigationFormDAO.insertInvestigationFormDetail(detail);
//            else {
//                detail.setUpdatedBy(userId);
//                detail.setCompId(vo.getCompId());
//                contractorInvestigationFormDAO.updateInvestigationFormDetail(detail);
//            }
        }
        fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
        return vo;
    }

    public List<ContractorInvestigationFormDetailVO> getFinalUseInspectionType(SpSearchVO searchVo) throws Exception {
        List<ContractorInvestigationFormDetailVO> voList = contractorInvestigationFormDAO.getFinalUseInspectionType(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteInvestigationForm(List<ContractorInvestigationFormVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();

        for (ContractorInvestigationFormVO vo : voList) {
            contractorInvestigationFormDAO.deleteInvestigationFormDetail(vo);
        }
        return voList.get(0);
    }

    public List<JasperPrint> getInvestigationReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String title = "계약대상 업체 조사표";
        String fileNm = "(" + spSearchVO.getWriteYear() + ")";
        fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath("report/impl/contractorInvestigationForm/contractorInvestigationForm.jrxml");
            reportVO.setType(spSearchVO.getType());


            Map<String, Object> params = new HashMap<String, Object>();
            List<ContractorInvestigationFormVO> resultList = getInvestigationFormList(vo);
            ContractorInvestigationFormVO result = resultList.get(0);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + result.getPartCompNm();
            }

            params.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo2);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            params.put("partCompNm", result.getPartCompNm());
            params.put("examDt", result.getExamDt() == null || result.getExamDt().equals("")  ? null : DateUtils.formatDate(result.getExamDt()));
            params.put("investigator1Nm", result.getInvestigator1Nm() == null ? "조사자①" : result.getInvestigator1Nm());
            params.put("investigator1Sign", utilsService.convertToInputStream(result.getInvestigator1Sign()));
            params.put("investigator2Nm", result.getInvestigator2Nm() == null ? "조사자②" : result.getInvestigator2Nm());
            params.put("investigator2Sign", utilsService.convertToInputStream(result.getInvestigator2Sign()));
            if(result.getAddrs1() == null){
                result.setAddrs1("");
            }
            if(result.getAddrs2() == null){
                result.setAddrs2("");
            }
            if(!result.getAddrs1().isEmpty() && !result.getAddrs2().isEmpty()){
                result.setAddrs2(", " + result.getAddrs2());
            }
            params.put("addrs", result.getAddrs1() + result.getAddrs2());
            params.put("desc", result.getDesc());
            params.put("partCompItemNm", result.getPartCompItemNm());
            params.put("partnerLocation", result.getFileId() == null || result.getFileId().equals("") ? null : nasFileService.getNasFileInputStream(result.getFileId()));


            List<Map<String, Object>> datasource = new ArrayList<>();
            int upperSum = 0;
            int middleSum = 0;
            int lowerSum = 0;
            int scoreSum = 0;
            for (int i = 0; i < result.getDetailList().size(); i++) {
                Map<String, Object> dataMap = new HashMap<String, Object>();
                ContractorInvestigationFormDetailVO data = result.getDetailList().get(i);
                dataMap.put("writeYear", data.getWriteYear());
                dataMap.put("type", data.getType());
                dataMap.put("typeNm", data.getInspectionNm());
                dataMap.put("detail", data.getOrdSeq());
                dataMap.put("detailContent", data.getInspectionItemNm());
                dataMap.put("upperScore", data.getUpperScore());
                dataMap.put("middleScore", data.getMiddleScore());
                dataMap.put("lowerScore", data.getLowerScore());
                dataMap.put("comment", data.getComment());
                dataMap.put("score", data.getScore());
                datasource.add(dataMap);
                upperSum += data.getUpperScore();
                middleSum += data.getMiddleScore();
                lowerSum += data.getLowerScore();
                scoreSum += data.getScore();
            }
            params.put("upperSum", upperSum);
            params.put("middleSum", middleSum);
            params.put("lowerSum", lowerSum);
            params.put("scoreSum", scoreSum);
            params.put("detailList", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }
        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

        return reportList;
    }

    // 점검사항 관리 관련 API
    public List<ContractorInvestigationFormDetailVO> getInspectionType(SpSearchVO searchVo) throws Exception {
        List<ContractorInvestigationFormDetailVO> voList = contractorInvestigationFormDAO.getInspectionType(searchVo);
        for (ContractorInvestigationFormDetailVO vo : voList) {
            vo.setDetailList(contractorInvestigationFormDAO.getInspectionTypeDetail(vo));
        }
        return voList;
    }

    // 점검사항 관리 관련 API
    public List<ContractorInvestigationFormDetailVO> getInspectionTypeDetail(SpSearchVO searchVo) throws Exception {
        ContractorInvestigationFormDetailVO vo = new ContractorInvestigationFormDetailVO();
        vo.setCompId(SecurityUtil.getCurrentCompId());
        vo.setInspectionId(searchVo.getSearchText());
        List<ContractorInvestigationFormDetailVO> voList = contractorInvestigationFormDAO.getInspectionTypeDetail(vo);

        return voList;
    }

    public List<ContractorInvestigationFormDetailVO> getInspectionTypeDataset(SpSearchVO searchVo) throws Exception {
        List<ContractorInvestigationFormDetailVO> voList = contractorInvestigationFormDAO.getInspectionTypeDataset(searchVo);
        for (ContractorInvestigationFormDetailVO vo : voList) {
            vo.setDetailList(contractorInvestigationFormDAO.getInspectionTypeDatasetDetail(vo));
        }
        return voList;
    }


    @Transactional(rollbackFor = Throwable.class)
    public List<ContractorInvestigationFormDetailVO> saveInspectionType(List<ContractorInvestigationFormDetailVO> voList) throws Exception {
        String userId = SecurityUtil.getCurrentMemberId();
        for(ContractorInvestigationFormDetailVO vo : voList){
            if (vo.getCmd().equals("I")) {
                vo.setCreatedBy(userId);
                contractorInvestigationFormDAO.insertInspectionType(vo);
            } else {
                vo.setUpdatedBy(userId);
                contractorInvestigationFormDAO.updateInspectionType(vo);
            }
            for (ContractorInvestigationFormDetailVO detail : vo.getDetailList()) {
                detail.setType(vo.getType());
                if (detail.getCmd().equals("I")) {
                    detail.setCreatedBy(userId);
                    detail.setCompId(vo.getCompId());
                    detail.setInspectionId(vo.getInspectionId());

                    contractorInvestigationFormDAO.insertInspectionTypeDetail(detail);
                } else {
                    detail.setUpdatedBy(userId);
                    detail.setCompId(vo.getCompId());
                    contractorInvestigationFormDAO.updateInspectionTypeDetail(detail);
                }
            }
        }

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<ContractorInvestigationFormDetailVO> deleteInspectionType(List<ContractorInvestigationFormDetailVO> voList) throws Exception {
        //String userId = SecurityUtil.getCurrentMemberId();
        //vo.setCreatedBy(userId);
        for(ContractorInvestigationFormDetailVO vo : voList){
            contractorInvestigationFormDAO.deleteInspectionType(vo);
            for (ContractorInvestigationFormDetailVO detail : vo.getDetailList()) {
                contractorInvestigationFormDAO.deleteInspectionTypeDetail(detail);
            }
        }

        return voList;
    }

    // 년도별 협력사 합격점수 관련
    public ContractorInvestigationFormVO getPassScore(ContractorInvestigationFormVO searchVo) throws Exception {
        System.out.println("서치 브이오 확인" + searchVo);
        ContractorInvestigationFormVO vo = contractorInvestigationFormDAO.getPassScore(searchVo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ContractorInvestigationFormVO savePassScore(ContractorInvestigationFormVO vo) throws Exception {

        ContractorInvestigationFormVO searchData = contractorInvestigationFormDAO.getPassScore(vo);
        vo.setId(SecurityUtil.getCurrentMemberId());
        if(searchData == null) {
            contractorInvestigationFormDAO.savePassScore(vo);
        }else{
            contractorInvestigationFormDAO.updatePassScore(vo);
        }

        return vo;
    }

}
