package kr.co.igns.business.support.service;


import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.participation.dao.postgres.HsePolicyDAO;
import kr.co.igns.business.participation.model.ParticipationVo;
import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.business.support.dao.postgres.SafetyDutiesDocDAO;
import kr.co.igns.business.support.model.SafetyDutiesDocVO;
import kr.co.igns.business.support.model.SafetyDutiesUserVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.model.ClientVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;                                                                                                                                                                                                                          
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SafetyDutiesDocService {
    private final SafetyDutiesDocDAO dao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final ClientDAO clientDao;
    private final HsePolicyDAO hsePolicyDao;

    @Transactional
    public BaseVO delete(List<SafetyDutiesDocVO> voList) {
        for(SafetyDutiesDocVO vo : voList){
            dao.delete(vo);
        }
        return voList.get(0);
        
    }

    @Transactional
    public BaseVO create(SafetyDutiesDocVO reqVo) {
        SafetyDutiesDocVO maxData = null;
        String dt = reqVo.getWriteDate();
        if(reqVo.getCmd()!=null && reqVo.getCmd().equals("U")){
            dao.update(reqVo);
        }else{            reqVo.setDocType(reqVo.getDocType());
            reqVo.setWriteYear(reqVo.getWriteDate().substring(0,4));
            dao.insert(reqVo);
        }
        List<SafetyDutiesUserVO> cardMemberList =  reqVo.getCardMemberList();
        SafetyDutiesDocVO searchVo = new SafetyDutiesDocVO();
        searchVo.setCompId(SecurityUtil.getCurrentCompId());
        searchVo.setWriteYear(reqVo.getWriteYear());
        searchVo.setDocNo(reqVo.getDocNo());
        List<SafetyDutiesUserVO> vo = dao.searchMember(searchVo);
        List<String> keyList = reqVo.getMemberList().stream().map(item -> item.getId() + item.getDocSeq()).collect(Collectors.toList());
        List<SafetyDutiesUserVO> notInMemberList = vo.stream().filter(member -> !keyList.contains(member.getHrId() + member.getDocSeq())).collect(Collectors.toList() );
        for(SafetyDutiesUserVO member : notInMemberList){
            dao.deleteMember(member);
        }
        for(SafetyDutiesUserVO member : cardMemberList){
            if(member.getCmd().equals("I")){
                member.setWriteYear(reqVo.getWriteYear());
                member.setDocNo(reqVo.getDocNo());
                member.setDocType(reqVo.getDocType());
                dao.insertMember(member);
            }else if(member.getCmd().equals("U")){
                dao.updateMember(member);
            }
        }
        return reqVo;
	}

    public List<SafetyDutiesDocVO> getSafetyDutyType(SpSearchVO searchVo) throws Exception{
        searchVo.setCompId(SecurityUtil.getCurrentCompId());
        List<SafetyDutiesDocVO> voList = dao.getSafetyDutyType(searchVo);
        return voList;
    }

    public List<SafetyDutiesDocVO> searchList(SpSearchVO searchVo) throws Exception{
        List<SafetyDutiesDocVO> volist = dao.searchList(searchVo);
        for(SafetyDutiesDocVO vo : volist){
            List<SafetyDutiesUserVO> member = dao.filteredSearchMember(vo);
            vo.setMemberList(member);
        }
        return volist;
    }
    
    public SafetyDutiesDocVO search(SpSearchVO searchVo) throws Exception{
        SafetyDutiesDocVO vo = dao.search(searchVo);
        List<SafetyDutiesUserVO> member = dao.searchMember(vo);
        vo.setMemberList(member);
        return vo;
    }

    public SafetyDutiesDocVO searchDataset(SafetyDutiesDocVO vo) throws Exception{
        SafetyDutiesDocVO resultVo = dao.searchDataset(vo);

        return resultVo;
    }

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVo)
			throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        String title = "안전업무 지정서";
        String fileNm = "";
        fileNm += title;
        
        int page = spSearchVo.getPage();
        int subPage = spSearchVo.getSubPage();
        int localPage = spSearchVo.getLocalPage();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(),SecurityUtil.getCurrentCompId());

        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVo, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        Boolean isSingle = false;
        if(spSearchVo.getCheckedObjList() != null && spSearchVo.getCheckedObjList().size()==1){
            isSingle=true;    
        }
        for(SpSearchVO reqVo : spSearchVo.getCheckedObjList()){
            SafetyDutiesDocVO vo = search(reqVo);
            SafetyDutiesDocVO voData = getSafetyDutyType(spSearchVo).stream().filter(item -> vo.getOrgnRoleType().equals(item.getOrgnRoleType())).filter(item -> vo.getOrgnRoleId().equals(item.getOrgnRoleId())).findFirst().orElse(null);
            List<SafetyDutiesUserVO> userlist = vo.getMemberList();
            if(isSingle==true){
                fileNm =  "(" + vo.getWriteYear() + ") 안전업무 지정서_" + voData.getLabel() + "_" + vo.getWriteDate();
            }
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(vo.getDocType());
            signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
            signParam.setType("sign");

            List<UtilsVO> approvalInfo = utilsService.getApprovalInfo(signParam);
            for(SafetyDutiesUserVO user : userlist){
                ReportVO reportVO = new ReportVO();
                reportVO.setFileName(fileNm);
                reportVO.setJrxmlPath("report/support/SafetyDutiesDoc.jrxml");
                reportVO.setType(spSearchVo.getType());
                Map<String, Object> params = new HashMap<String, Object>();
                if(user.getDismissalDt() == null || user.getDismissalDt().equals("")){ //임명장
                    params.put("docTitle", vo.getTitle() != null ? vo.getTitle() : "");
                    params.put("docSubTitle", vo.getSubtitle() != null ? vo.getSubtitle() : "");
                    params.put("content", vo.getContent() != null ? vo.getContent() : "");
                }else{ //해임장
                    params.put("docTitle", vo.getDismissalTitle() != null ? vo.getDismissalTitle() : "");
                    params.put("docSubTitle", vo.getDismissalSubtitle() != null ? vo.getDismissalSubtitle() : "");
                    params.put("content", vo.getContent() != null ? vo.getContent() : "");
                }
                params.put("title",title);
                params.put("subTitle","사업장명:"+compNm);
                params.put("subPage", subPage);
                params.put("localPage", localPage);
                InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                params.put("logo", clntLogo);

                params.put("label",vo.getLabel());
                params.put("hrNm",user.getHrNm());
                params.put("jbrpNm",user.getJbrpNm());
                params.put("orgnNm",user.getOrgnNm());
                vo.setWriteDate(DateUtils.formatDate(vo.getWriteDate()));
                params.put("writeDt",vo.getWriteDate());

                if(!approvalInfo.isEmpty()){
                    for(UtilsVO sign : approvalInfo) {
                        if (sign.getParam().equals("reviewer")) {
                            params.put("signature", utilsService.getSignatureFromBase64String(sign.getSignature()));
                            String ceoNm = sign.getJbrpNm() == null ? "" : (String) sign.getJbrpNm() + " ";
                            ceoNm += sign.getHrNm() == null ? "" : (String) sign.getHrNm();
                            params.put("ceoNm", ceoNm);
                        }
                    }
                }
                reportVO.setParameter(params);
                JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                reportList.add(JasperReport);
                page = page + JasperReport.getPages().size();
                localPage = localPage + JasperReport.getPages().size();
                reqVo.setPage(page);
                reqVo.setLocalPage(localPage);
            }
        }
		//if (!reqVo.isPrintAll()) {
			reportService.exportReportAll(request, response, reportList, spSearchVo.getType(), fileNm);
		//}
		return reportList;
	}

	
	
}
