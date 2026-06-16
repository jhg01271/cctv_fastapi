package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.SafetyAndHealthInfoRegisterDAO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentDetailVO;
import kr.co.igns.business.support.model.JobCompetencyAssessmentVO;
import kr.co.igns.business.support.model.SafetyAndHealthInfoRegisterVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
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
public class SafetyAndHealthInfoRegisterService {
    private final SafetyAndHealthInfoRegisterDAO safetyAndHealthInfoRegisterDAO;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final FileService fileService;

    public List<SafetyAndHealthInfoRegisterVO> getShInfoRegisterList(SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthInfoRegisterVO> voList = safetyAndHealthInfoRegisterDAO.getShInfoRegisterList(searchVo);
        
        for(SafetyAndHealthInfoRegisterVO vo : voList) {
        	vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo(), "SHR"));
        }
        
        return voList;
    }

    public List<SafetyAndHealthInfoRegisterVO> getMyShInfoRegisterList(SpSearchVO searchVo) throws Exception {
        List<SafetyAndHealthInfoRegisterVO> voList = safetyAndHealthInfoRegisterDAO.getMyShInfoRegisterList(searchVo);

        for(SafetyAndHealthInfoRegisterVO vo : voList) {
            vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo(), "SHR"));
        }

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveShInfoRegister(List<MultipartFile> files, SafetyAndHealthInfoRegisterVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            safetyAndHealthInfoRegisterDAO.insertShInfoRegister(vo);
        } else {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
		    fileService.deleteFile(vo.getDeleteFiles(), "SHR", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            safetyAndHealthInfoRegisterDAO.updateShInfoRegister(vo);
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteShInfoRegister(List<SafetyAndHealthInfoRegisterVO> voList) throws Exception {
        for (SafetyAndHealthInfoRegisterVO vo : voList) {
            vo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
		    fileService.deleteFile(vo.getDeleteFiles(), "SHR", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            safetyAndHealthInfoRegisterDAO.deleteShInfoRegister(vo);
        }
        return voList.get(0);
    }

    public ReportVO getShInfoRegisterReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        ReportVO reportVO = new ReportVO();

        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "("+spSearchVO.getWriteYear() + ")";
        fileNm += "안전보건정보 관리대장";
        String title = "안전보건정보 관리대장";
        reportVO.setJrxmlPath("report/support/safetyAndHealthInfoRegister/safetyAndHealthInfoRegister.jrxml");
        reportVO.setType(spSearchVO.getType());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("title", title);
        param.put("subTitle", "사업장명:" + compNm);
        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        param.put("logo", logo);
        param.put("page", spSearchVO.getPage());
        param.put("subPage", spSearchVO.getSubPage());
        param.put("localPage", spSearchVO.getLocalPage());

        List<Map<String, Object>> datasource = new ArrayList<>();

        for (int i = 0; i < spSearchVO.getCheckedList().size(); i++) {
            spSearchVO.setDocNo(spSearchVO.getCheckedList().get(i));
            List<SafetyAndHealthInfoRegisterVO> resultList = getShInfoRegisterList(spSearchVO); // 데이터
            SafetyAndHealthInfoRegisterVO result = resultList.get(0);
            if(spSearchVO.getCheckedList().size()==1) {
                fileNm += "_"+result.getWriteYear()+"-"+result.getDocType()+"-"+result.getDocNo();
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title", title);
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            data.put("logo", logo2);

            data.put("no", String.valueOf(i+1));
            data.put("receiptDt", DateUtils.formatDate(result.getReceiptDt()));
            data.put("docNo", result.getWriteYear() + "-" + result.getDocType() + "-" + result.getDocNo());
            data.put("keyContent", result.getKeyContent());
            data.put("actionOrgnNm", result.getActionOrgnNm());
            if (result.getActionDt() != null && !result.getActionDt().equals("")) {
                data.put("actionDt", DateUtils.formatDate(result.getActionDt()));
            }
            if (result.getReplyDt() != null && !result.getReplyDt().equals("")) {
                data.put("replyDt", DateUtils.formatDate(result.getReplyDt()));
            }
            data.put("actionContent", result.getActionContent());
            data.put("remark", result.getRemark());

            datasource.add(data);
        }
        reportVO.setFileName(fileNm);
        param.put("dataList", new JRBeanCollectionDataSource(datasource));

        reportVO.setParameter(param);

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll()) return reportVO;
        reportService.exportReport(request, response, reportVO);

        return reportVO;
    }
}
