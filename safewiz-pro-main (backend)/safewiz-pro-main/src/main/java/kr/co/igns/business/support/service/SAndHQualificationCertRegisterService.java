package kr.co.igns.business.support.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.utils.type.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.support.dao.postgres.SAndHQualificationCertRegisterDAO;
import kr.co.igns.business.support.model.SAndHQualificationCertRegisterVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SAndHQualificationCertRegisterService {
    private final SAndHQualificationCertRegisterDAO dao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final FileService fileService;

    @Transactional
    public BaseVO delete(List<SAndHQualificationCertRegisterVO> voList) throws Exception{
        for(SAndHQualificationCertRegisterVO vo : voList){
            dao.delete(vo);
		    fileService.deleteFile(vo.getDeleteFiles(), "SHQ", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
        }
        return voList.get(0);
        
    }

    @Transactional
    public BaseVO create(List<MultipartFile> files, SAndHQualificationCertRegisterVO reqVo) throws Exception{
        SAndHQualificationCertRegisterVO maxData = null;
        String writeDt = reqVo.getWriteDate();
        if(reqVo.getCmd()!=null && reqVo.getCmd().equals("U")){
		    fileService.deleteFile(reqVo.getDeleteFiles(), "SHQ", reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());
            fileService.saveFile(files, reqVo.getDocType(), reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());
            dao.update(reqVo);

        }else{
            reqVo.setDocType(reqVo.getDocType());
            fileService.saveFile(files, reqVo.getDocType(), reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());
            dao.insert(reqVo);
        }
        return reqVo; 
	}

    public List<SAndHQualificationCertRegisterVO> searchList(SpSearchVO searchVo) throws Exception{
        List<SAndHQualificationCertRegisterVO> volist = dao.searchList(searchVo);

        return volist;
    }
    
    public SAndHQualificationCertRegisterVO search(SAndHQualificationCertRegisterVO searchVo) throws Exception{
        SAndHQualificationCertRegisterVO vo = dao.search(searchVo);
        vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo(), "SHQ"));
        return vo;
    }


    public ReportVO getReport(HttpServletRequest request, HttpServletResponse response, List<SAndHQualificationCertRegisterVO> reqVoList)
			throws Exception {
        ReportVO reportVO = new ReportVO();
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "안전보건 자격 인증 관리 대장 (" + reqVoList.get(0).getWriteYear() + ") ";
        reportVO.setJrxmlPath("report/support/SAndHQualificationCertRegister.jrxml");
        reportVO.setType(reqVoList.get(0).getType());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("title","안전보건 자격 인증 관리 대장");
        params.put("subTitle", "사업장명:" + compNm);
        InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", clntLogo);
        List<Map<String,String>> dataset = new ArrayList<>();
        Boolean isSingle = false;
        if(reqVoList!=null && reqVoList.size()==1){
            isSingle=true;
        }
        Map<String, Object> searchList = new HashMap<>();
        searchList.put("compId", reqVoList.get(0).getCompId());
        searchList.put("docType", reqVoList.get(0).getDocType());
        searchList.put("writeYear", reqVoList.get(0).getWriteYear());
        List<String> docNoList = new ArrayList<>();
        for(int i = 0; i < reqVoList.size(); i++){
            docNoList.add(reqVoList.get(i).getDocNo());
        }
        searchList.put("docNoList", docNoList);
        List<SAndHQualificationCertRegisterVO> result = dao.searchListReport(searchList);
        int index = 1;
        for(SAndHQualificationCertRegisterVO vo : result){
            if(isSingle){
                fileNm +=  "_" + vo.getHrNm();
            }
            Map<String, String> data = new HashMap<String, String>();
            data.put("index",String.valueOf(index++));
            data.put("hrNm",vo.getHrNm());
            data.put("deptNm",vo.getOrgnNm());
            data.put("regNm",vo.getRegNm());

            data.put("regDetail",vo.getRegDetail());
            data.put("regCheckYn",vo.getRegCheckYn());
            if(vo.getRegDt() != null){
                data.put("regDt",DateUtils.formatDate(vo.getRegDt()));
            }
            if(vo.getCancleDt() != null){
                data.put("cancleDt",DateUtils.formatDate(vo.getCancleDt()));
            }
            data.put("cancleDetail",vo.getCancleDetail());
            data.put("cancleCheckYn",vo.getCancleCheckYn());
            dataset.add(data);
        }

        params.put("dataset", new JRBeanCollectionDataSource(dataset));
        reportVO.setParameter(params);
        reportVO.setFileName(fileNm);
		if (reqVoList.get(0).isPrintAll()) return reportVO;
        reportService.exportReport(request, response, reportVO);

        return reportVO;
	}

	
	
}
