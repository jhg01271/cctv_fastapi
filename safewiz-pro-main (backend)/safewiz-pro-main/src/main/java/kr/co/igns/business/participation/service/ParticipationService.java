package kr.co.igns.business.participation.service;


import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.utils.type.DateUtils;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.participation.dao.postgres.ParticipationDAO;
import kr.co.igns.business.participation.model.ContentDto;
import kr.co.igns.business.participation.model.ParticipationVo;
import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.model.ClientVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ParticipationService {
    private final ParticipationDAO participationdao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final ClientDAO clientDao;

    @Transactional
    public BaseVO delete(List<ParticipationVo> voList) {
        for (ParticipationVo vo : voList) {
            participationdao.delete(vo);
//            participationdao.deleteContent(vo);
//            participationdao.deleteMember(vo.getDocType(),vo.getWriteYear()+vo.getDocNo());
        }
        return voList.get(0);

    }

    @Transactional
    public BaseVO create(ParticipationVo reqVo) throws Exception {
        String dt = reqVo.getCommDt();
        reqVo.setCommDt(DateUtils.formatDateForDB(dt));
        if (reqVo.getCmd() != null && reqVo.getCmd().equals("U")) {
            participationdao.update(reqVo);
            participationdao.deleteContent(reqVo);
        } else {
            reqVo.setDocType(reqVo.getDocType());
            reqVo.setWriteYear(reqVo.getCommDt().substring(0, 4));
            participationdao.insert(reqVo);
        }
        List<ContentDto> contents = reqVo.getContents();
        for (ContentDto dto : contents) {
            dto.setDocNo(reqVo.getDocNo());
            dto.setDocType(reqVo.getDocType());
            dto.setWriteYear(reqVo.getWriteYear());
            participationdao.insertContent(dto);
        }

        ParticipationVo result = new ParticipationVo();
        result.setDocNo(reqVo.getDocNo());
        result.setDocType(reqVo.getDocType());
        result.setWriteYear(reqVo.getWriteYear());
        return result;
    }

    public List<ParticipationVo> searchList(SpSearchVO searchVo) throws Exception {
        List<ParticipationVo> vo = participationdao.searchList(searchVo);
        return vo;
    }

    public ParticipationVo search(SpSearchVO searchVo) throws Exception {
        ParticipationVo vo = participationdao.search(searchVo);
        List<SignatureDto> signature = participationdao.findMemberById(vo);

        List<SignatureDto> writer = signature.stream().filter(el -> el.getParam().equals("writer")).collect(Collectors.toList());
        if (writer != null && writer.size() > 0) {
            vo.setWriter(writer.get(0));
        }
        List<SignatureDto> reviewer = signature.stream().filter(el -> el.getParam().equals("reviewer")).collect(Collectors.toList());
        if (reviewer != null && reviewer.size() > 0) {
            vo.setReviewer(reviewer.get(0));
        }
        List<SignatureDto> approver = signature.stream().filter(el -> el.getParam().equals("approver")).collect(Collectors.toList());
        if (approver != null && approver.size() > 0) {
            vo.setApprover(approver.get(0));
        }
        List<SignatureDto> company = signature.stream().filter(el -> el.getType().equals("company")).collect(Collectors.toList());
        vo.setCompanyMember(company);
        List<SignatureDto> employee = signature.stream().filter(el -> el.getType().equals("employee")).collect(Collectors.toList());
        vo.setEmployeeMember(employee);
        List<SignatureDto> partner = signature.stream().filter(el -> el.getType().equals("partner")).collect(Collectors.toList());
        vo.setPartnerMember(partner);

        List<ContentDto> contents = participationdao.findContentById(vo);
        vo.setContents(contents);
        return vo;
    }


    public String getName(SignatureDto dto) {
        String result = dto.getHrNm();

        if(dto.getPartCompNm() != null){
            result += " (" + dto.getPartCompNm() + ")";
        }else{
            if (dto.getOrgnNm() != null && !dto.getOrgnNm().equals("") && dto.getJbrpNm() != null && !dto.getJbrpNm().equals("")) {
                result += " (" + dto.getOrgnNm() + ", " + dto.getJbrpNm() + ")";
            }
        }

        return result;
    }

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {

        List<JasperPrint> reportList = new ArrayList<>();

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "협력업체위원회 보고서");
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        String fileNm = "(" + spSearchVO.getWriteYear() + ") 협력업체위원회 보고서";
        for (SpSearchVO spVO : spSearchVO.getCheckedObjList()) {
            ParticipationVo vo = search(spVO);
            if(spSearchVO.getCheckedObjList().size()== 1) {
                fileNm += "_"+vo.getCommNm();
            }
            ReportVO reportVO = new ReportVO();
            reportVO.setFileName("(" + spSearchVO.getWriteYear() + ") 협력업체위원회 보고서");
            reportVO.setJrxmlPath("report/participation/participation/participation.jrxml");
            reportVO.setType(spSearchVO.getType());
            Map<String, Object> params = new HashMap<String, Object>();
            // params.put("contentHeader", result.getContentHeader());
            // params.put("contentBody", result.getContentBody());
            // params.put("contentFooter", result.getContentFooter());
            params.put("title", "협력업체위원회");
            // 고객사 로고
            InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);


            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 서명
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

            String commdt = "";
            if (vo.getCommDt() != null && !vo.getCommDt().equals("")) {
                commdt = DateUtils.formatDate(vo.getCommDt());
            }
            params.put("commDt", commdt);
            params.put("commAt", vo.getCommAt());
            params.put("location", vo.getLocation());
            params.put("commNm", vo.getCommNm());
            params.put("commAgenda", vo.getCommAgenda());


            String content = "";
            for (ContentDto dto : vo.getContents()) {
                content += dto.getContent();
                content += "( 협의율 : ";
                content += dto.getPercent() == null ? 0 : dto.getPercent();
                content += "% ) \n\n";
            }
            System.out.println(content);
            params.put("content", content);
            params.put("createdAt", DateUtils.formatDate(vo.getCreatedAt()));
            params.put("createdBy", vo.getWriter().getHrNm());
            params.put("remark", vo.getRemark());


            List<Map<String, Object>> companyMapList = new ArrayList<>();
            List<SignatureDto> companyMember = vo.getCompanyMember();
            for (int i = 0; i < companyMember.size(); i += 2) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("name1", getName(companyMember.get(i)));
                data.put("signature1", utilsService.convertToInputStream(companyMember.get(i).getSignature()));
                if (companyMember.size() > i + 1) {
                    data.put("name2", getName(companyMember.get(i + 1)));
                    data.put("signature2", utilsService.convertToInputStream(companyMember.get(i + 1).getSignature()));
                }
                companyMapList.add(data);
            }
            params.put("companyMember", new JRBeanCollectionDataSource(companyMapList));

            List<Map<String, Object>> partnerMapList = new ArrayList<>();
            List<SignatureDto> partnerMember = vo.getPartnerMember();
            for (int i = 0; i < partnerMember.size(); i += 2) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("name1", getName(partnerMember.get(i)));
                data.put("signature1", utilsService.convertToInputStream(partnerMember.get(i).getSignature()));
                if (partnerMember.size() > i + 1) {
                    data.put("name2", getName(partnerMember.get(i + 1)));
                    data.put("signature2", utilsService.convertToInputStream(partnerMember.get(i + 1).getSignature()));
                }
                partnerMapList.add(data);
            }
            params.put("partnerMember", new JRBeanCollectionDataSource(partnerMapList));


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

    public List<JasperPrint> getOhsCommitteeReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();

        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "산업안전보건위원회 보고서");
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        String fileNm = "(" + spSearchVO.getWriteYear() + ") 산업안전보건위원회 보고서";
        for (SpSearchVO spVO : spSearchVO.getCheckedObjList()) {
            ParticipationVo vo = search(spVO);
            if(spSearchVO.getCheckedObjList().size()== 1) {
                fileNm += (vo.getCommNm() == null || vo.getCommNm().trim().equals("")) ? "" : "_"+vo.getCommNm();
            }
            ReportVO reportVO = new ReportVO();
            // 다운로드되는 파일 명
            reportVO.setFileName("(" + spSearchVO.getWriteYear() + ") 산업안전보건위원회 보고서");
            reportVO.setJrxmlPath("report/participation/participation/ohsCommittee.jrxml");
            reportVO.setType(spSearchVO.getType());

            // 파라미터 세팅
            Map<String, Object> params = new HashMap<String, Object>();

            // 고객사 로고
            InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);

            params.put("title", "산업안전보건위원회");
            // 서명
//            if (vo.getWriter() != null) {
//                InputStream signWriter = utilsService.convertToInputStream(vo.getWriter().getSignature());
//                params.put("signWriter", signWriter);
//                params.put("signWriterNm", vo.getWriter().getHrNm());
//            }
//            if (vo.getReviewer() != null) {
//                InputStream signReviewer = utilsService.convertToInputStream(vo.getReviewer().getSignature());
//                params.put("signReviewer", signReviewer);
//                params.put("signReviewerNm", vo.getReviewer().getHrNm());
//            }
//            if (vo.getApprover() != null) {
//                InputStream signApprover = utilsService.convertToInputStream(vo.getApprover().getSignature());
//                params.put("signApprover", signApprover);
//                params.put("signApproverNm", vo.getApprover().getHrNm());
//            }
            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));


            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            String commdt = "";
            if (vo.getCommDt() != null && !vo.getCommDt().equals("")) {
                commdt = DateUtils.formatDate(vo.getCommDt());//.substring(0, 4) + "." + vo.getCommDt().substring(4, 6) + "." + vo.getCommDt().substring(6, 8);
            }
            params.put("commDt", commdt);
            params.put("commAt", vo.getCommAt());
            params.put("commDiv", vo.getCommDiv());
            params.put("location", vo.getLocation());
            params.put("commNm", vo.getCommNm());
            params.put("commAgenda", vo.getCommAgenda());

            String contents = "";
            for (ContentDto dto : vo.getContents()) {
                if (!dto.getContent().equals("")) {
                    contents += dto.getContent();
                    contents += "\n( 협의율 : ";
                    contents += dto.getPercent() == null ? 0 : dto.getPercent();
                    contents += "% ) \n\n";
                }
            }
            params.put("contents", contents);
            params.put("createdAt", vo.getCreatedAt().substring(0, 19));
            params.put("createdBy", vo.getCreatedBy());
            params.put("remark", vo.getRemark());

            List<Map<String, Object>> memberMapList = new ArrayList<>();

            int repeatKey = Math.max(vo.getCompanyMember().size(), vo.getEmployeeMember().size());
            if (repeatKey == 0) repeatKey = 1;
            for (int i = 0; i < repeatKey; i++) {
                Map<String, Object> data = new HashMap<String, Object>();
                // 사측 위원
                if (vo.getCompanyMember().size() > i) {
                    data.put("jbrpNm1", vo.getCompanyMember().get(i).getJbrpNm());
                    data.put("name1", vo.getCompanyMember().get(i).getHrNm());
                    data.put("signature1", utilsService.convertToInputStream(vo.getCompanyMember().get(i).getSignature()));
                } else {
                    data.put("jbrpNm1", null);
                    data.put("name1", null);
                    data.put("signature1", null);
                }
                // 근로자측 위원
                if (vo.getEmployeeMember().size() > i) {
                    data.put("jbrpNm2", vo.getEmployeeMember().get(i).getJbrpNm());
                    data.put("name2", vo.getEmployeeMember().get(i).getHrNm());
                    data.put("signature2", utilsService.convertToInputStream(vo.getEmployeeMember().get(i).getSignature()));
                } else {
                    data.put("jbrpNm2", null);
                    data.put("name2", null);
                    data.put("signature2", null);
                }
                memberMapList.add(data);
            }
            params.put("memberList", new JRBeanCollectionDataSource(memberMapList));

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
