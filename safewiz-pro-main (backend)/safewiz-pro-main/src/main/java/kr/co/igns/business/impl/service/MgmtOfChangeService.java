package kr.co.igns.business.impl.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.impl.dao.postgres.MgmtOfChangeDAO;
import kr.co.igns.business.impl.model.MgmtOfChangeVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.setting.service.SystemCodeService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service
@RequiredArgsConstructor
public class MgmtOfChangeService {

    private final MgmtOfChangeDAO dao;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final SystemCodeService syscodeService;
    private final NasFileService nasFileService;

	@Transactional(rollbackFor = Throwable.class)
	public MgmtOfChangeVO saveMgmtOfChange(List<MultipartFile> files, MgmtOfChangeVO vo) throws Throwable {
        
        if (vo.getCmd()!=null && vo.getCmd().equals("U")) {
            dao.updateMgmtOfChange(vo);
            fileService.deleteFile(vo.getDeleteFiles(), vo.getDocType(), vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
        } else {
            vo.setWriteYear(vo.getReqDt().substring(0,4));
            vo.setDocType("MOC");
            dao.insertMgmtOfChange(vo);
        }
		//파일 저장
		fileService.saveFile(files, "MOC", vo.getWriteYear()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
		return vo;
	}

    @Transactional(rollbackFor = Throwable.class)
    public SpSearchVO deleteMgmtOfChangeDetail(List<SpSearchVO> voList) throws Exception{
        for(SpSearchVO vo:voList){
            dao.deleteMgmtOfChange(vo);
        }
        return voList.get(0);
    }

    public List<MgmtOfChangeVO> getMgmtOfChange(SpSearchVO searchVo) throws Exception{
        List<MgmtOfChangeVO> vo = dao.getMgmtOfChange(searchVo);
        return vo;
    }

    public MgmtOfChangeVO getMgmtOfChangeDetail(SpSearchVO searchVo) throws Exception{
        MgmtOfChangeVO vo = dao.getMgmtOfChangeDetail(searchVo);
        vo.setFiles(fileService.getFileList(vo.getWriteYear()+vo.getDocNo(), "MOC"));
        return vo;
    }

	public List<JasperPrint> getMgmtOfChangeReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
		List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
		String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
		String title = "안전보건 변경검토 요청서/결과서";
		String fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";
		fileNm += title;

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

		for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
			ReportVO reportVO = new ReportVO();
			reportVO.setFileName("안전보건 변경검토 요청서/결과서");
			reportVO.setJrxmlPath("report/impl/mgmtOfChange/MgmtOfChange.jrxml");
			reportVO.setType(spSearchVO.getType());
			MgmtOfChangeVO res = getMgmtOfChangeDetail(vo);
			if (spSearchVO.getCheckedObjList().size() == 1) {
				fileNm += "_" + res.getReqOrgnNm();
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("title", "안전보건 변경검토 요청서/결과서");
			params.put("subTitle", "사업장명:" + compNm);

			InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
			params.put("logo", clntLogo);

			params.put("page", page);
			params.put("subPage", subPage);
			params.put("localPage", localPage);
			UtilsVO signParam = new UtilsVO();
			signParam.setTargetType("MOC");
			signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
			
			Map<String, String> types = Map.of("reqOrg", "signReq", "review", "signReview", "change", "signChange");

			for (var entry : types.entrySet()) {
			    signParam.setType(entry.getKey());
			    for (UtilsVO info : utilsService.getApprovalInfo(signParam)) {
			        params.put(entry.getValue(), utilsService.getSignatureFromBase64String(info.getSignature()));
			        params.put(entry.getValue() + "Nm", info.getHrNm());
			    }
			}
			
			params.put("docNo", res.getWriteYear() + "-" + res.getDocType() + "-" + res.getDocNo());
			
			// 요청일자 날짜 포맷팅
			params.put("reqDt", DateUtils.formatDate(res.getReqDt()));
			
			params.put("reqOrgnNm", res.getReqOrgnNm());
			params.put("content", res.getContent());
			SystemCodeVO sysVo = new SystemCodeVO();
			sysVo.setMajorCd("C0033");
			List<SystemMinorCodeVO> divCodeList = syscodeService.findDetail(sysVo);
			String[] divList = res.getDiv().split(",");
			String div = "";
			for (SystemMinorCodeVO divCode : divCodeList) {
				if (Arrays.asList(divList).contains(divCode.getMinorCd())) {
					div = div + "■ " + divCode.getMinorNm() + "\n\n";
				} else {
					div = div + "□ " + divCode.getMinorNm() + "\n\n";
				}
			}
			div = div.substring(0, div.length() - 2);

			if (res.getDivEtc() != null) {
				div = div + " : " + res.getDivEtc();
			}

			params.put("div", div);
			params.put("changeOrgnNm", res.getChangeOrgnNm());
			params.put("changeOverview", res.getChangeOverview());
			
			// 변경 적용 기한 날짜 포맷팅
			String changeDt = res.getChangeDt() != null ? DateUtils.formatDate(res.getChangeDt()) : "";
			params.put("changeDt", changeDt);
			
			params.put("reviewContent", res.getReviewContent());
			params.put("resultContent", res.getResultContent());
			sysVo.setMajorCd("C0035");
			List<SystemMinorCodeVO> resultCodeList = syscodeService.findDetail(sysVo);
			String result = "검토결과에 따라 변경을 (";
			for (SystemMinorCodeVO resultCode : resultCodeList) {
				if (resultCode.getMinorCd().equals(res.getResult())) {
					result = result + "■ " + resultCode.getMinorNm() + " / ";
				} else {
					result = result + "□ " + resultCode.getMinorNm() + " / ";
				}
			}
			result = result.substring(0, result.length() - 2);
			result = result + ") 합니다.";
			params.put("result", result);
			
			// 변경 검토 일자 날짜 포맷팅
			String reviewDt = res.getReviewDt() != null ? DateUtils.formatDate(res.getReviewDt()) : "";
			params.put("reviewDt", reviewDt);

			reportVO.setParameter(params);
			JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
			reportList.add(JasperReport);
			page = page + JasperReport.getPages().size();
			localPage = localPage + JasperReport.getPages().size();
			spSearchVO.setPage(page);
			spSearchVO.setLocalPage(localPage);

			if (res.getFiles() != null && !res.getFiles().isEmpty()) {
				JasperPrint JasperFileReport = utilsService.getFilesReport(spSearchVO, res.getFiles());
				reportList.add(JasperFileReport);
				page = page + JasperFileReport.getPages().size();
				localPage = localPage + JasperFileReport.getPages().size();
			}
		}

		
		if(spSearchVO.isPrintAll()) return reportList;
		reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
//		reportService.exportReport(request, response, reportVO);

		return reportList;
    }
}
