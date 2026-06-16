package kr.co.igns.business.participation.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.igns.business.dataset.model.TypeofEquipmentVO;
import kr.co.igns.business.participation.dao.postgres.HsePolicyDAO;
import kr.co.igns.business.participation.model.HsePolicySuggestionVO;
import kr.co.igns.business.participation.model.HsePolicyVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.model.EquipVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.util.SpUtils;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class HsePolicyService {
    private final FileService fileService;
    private final HsePolicyDAO hsePolicyDao;
    private final ReportService reportService;
    private final UtilsService utilsService;

    // 확정항목만 조회
    public List<HsePolicyVO> getConfirmedHsePolicyList(SpSearchVO searchVo) throws Exception {
        searchVo.setSearchText("Y");
        List<HsePolicyVO> voList = hsePolicyDao.getHsePolicyList(searchVo);
        return voList;
    }

    // 미확정 항목 조회
    public List<HsePolicyVO> getHsePolicyList(SpSearchVO searchVo) throws Exception {
        searchVo.setSearchText("N");
        List<HsePolicyVO> voList = hsePolicyDao.getHsePolicyList(searchVo);
        return voList;
    }

    public HsePolicyVO getHsePolicyDetail(SpSearchVO searchVo) throws Exception {
        HsePolicyVO vo = hsePolicyDao.getHsePolicyDetail(searchVo);
        vo.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo() + "additional", "PLC"));
        return vo;
    }

    public HsePolicyVO getSampleHsePolicy(SpSearchVO searchVo) throws Exception {
        HsePolicyVO vo = hsePolicyDao.getSampleHsePolicy(searchVo);

        vo.setContentHeader(changeVariables(vo.getContentHeader(), null));
        vo.setContentBody(changeVariables(vo.getContentBody(), vo.getCompNm()));
        vo.setContentFooter(changeVariables(vo.getContentFooter(), null));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHsePolicy(List<MultipartFile> insertFiles, HsePolicyVO reqVo) throws Exception {
        reqVo.setCompId(SecurityUtil.getCurrentCompId());
        if (reqVo.getCmd().equals("I")) {
            reqVo.setCreatedBy(SecurityUtil.getCurrentMemberId());
            hsePolicyDao.insertHsePolicy(reqVo);
        } else {
            reqVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            hsePolicyDao.updateHsePolicy(reqVo);
        }
        //파일 추가/변경
        if (reqVo.getDataType().equals("0002")) {
            fileService.deleteFile(reqVo.getDeleteFiles(), "PLC", reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());
            String fileId = fileService.saveFile(insertFiles, "PLC", reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());
            reqVo.setFileId(fileId);   //수정
        }
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHsePolicyFiles(List<MultipartFile> insertFiles, HsePolicyVO reqVo) throws Exception {
        reqVo.setCompId(SecurityUtil.getCurrentCompId());

        fileService.deleteFile(reqVo.getDeleteFiles(), "PLC", reqVo.getWriteYear() + reqVo.getDocNo() + "additional", SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "PLC", reqVo.getWriteYear() + reqVo.getDocNo() + "additional", SecurityUtil.getCurrentCompId());
        reqVo.setFileId(fileId);   //수정
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateHsePolicyConfirmYn(HsePolicyVO reqVo) throws Exception {
        hsePolicyDao.updateHsePolicyConfirmYn(reqVo);
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHsePolicy(List<HsePolicyVO> list) throws Exception {
        for (HsePolicyVO tmp : list) {
            hsePolicyDao.deleteHsePolicy(tmp);
        }
    }

    // 일괄출력 시 사용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "안전보건경영 방침");
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();
        spSearchVO.setPage(page);
        spSearchVO.setLocalPage(localPage);

        List<SpSearchVO> hsePolicyList = hsePolicyDao.getAllReport(spSearchVO);
        for (SpSearchVO plc : hsePolicyList) {
            plc.setPage(page);
            plc.setSubPage(subPage);
            plc.setLocalPage(localPage);
            plc.setType(spSearchVO.getType());
            plc.setPrintAll(true);
            ReportVO plcReport = getHsePolicyReport(request, response, plc);
            JasperPrint JasperReport = reportService.allReportJasperPrint(plcReport);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }
        // 본문

        return reportList;
    }

    public ReportVO getHsePolicyReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        ReportVO reportVO = new ReportVO();
        spSearchVO.setSearchText2(SecurityUtil.getCurrentCompId());
        HsePolicyVO result = hsePolicyDao.getHsePolicyDetail(spSearchVO);

        reportVO.setFileName("(" + result.getWriteYear() + ")안전보건경영방침_" + result.getContentHeader());
        reportVO.setJrxmlPath("report/participation/hsePolicy/hsePolicy.jrxml");
        reportVO.setType(spSearchVO.getType());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contentHeader", result.getContentHeader());
        params.put("contentBody", result.getContentBody());
        params.put("contentFooter", result.getContentFooter());

        params.put("page", spSearchVO.getPage());
        params.put("subPage", spSearchVO.getSubPage());
        params.put("localPage", spSearchVO.getLocalPage());

        // 고객사 로고
        InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", clntLogo);
        // 서명
        UtilsVO signParam = new UtilsVO();
        signParam.setDocType(spSearchVO.getDocType());
        signParam.setTargetId(spSearchVO.getWriteYear() + spSearchVO.getDocNo());
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(signParam, signParam.getTargetId());
        if (approvalInfo.get(1).get("signature") != null) {
            params.put("signature", approvalInfo.get(1).get("signature"));
        } else params.put("signature", null);
        params.put("hrNm", approvalInfo.get(1).get("name"));
//        String ceoHrId = hsePolicyDao.getCeoHrId();
//        String ceoHrNm = hsePolicyDao.getCeoHrNm();
//        if (ceoHrId != null) {
//            InputStream signature = utilsService.getUserSignatureByHrId(SecurityUtil.getCurrentClntId(), ceoHrId);
//            params.put("signature", signature);
//            params.put("ceoNm", ceoHrNm);
//        }

        reportVO.setParameter(params);
        if (!spSearchVO.isPrintAll()) {
            reportService.exportReport(request, response, reportVO);
        }
        return reportVO;
    }

    // 환경변수 변경 함수
    public String changeVariables(String content, String compNm) {

        Map<String, String> variables = new HashMap<>();

        LocalDate now = LocalDate.now();
        String year = Integer.toString(now.getYear());
        String month = Integer.toString(now.getMonthValue());
        String day = Integer.toString(now.getDayOfMonth());

        variables.put("year", year);
        variables.put("month", month);
        variables.put("day", day);
        variables.put("compNm", compNm);

        Pattern pattern = Pattern.compile("\\{\\{([^}]+)}}"); // 정규 표현식을 이용하여 {{}} 패턴을 찾아 변수로 바꿈
        Matcher matcher = pattern.matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement = variables.getOrDefault(variableName, ""); // 맵에서 변수 값을 가져옴
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    // 근로자 의견 관련 API
    public List<HsePolicySuggestionVO> getHsePolicySug(SpSearchVO spSearchVO) throws Exception {
        if (spSearchVO.isMyDataOnly()) {
            // 로그인 유저 본인의 의견 조회 시
            spSearchVO.setUserId(SecurityUtil.getCurrentMemberId());
        }
        List<HsePolicySuggestionVO> voList = hsePolicyDao.getHsePolicySug(spSearchVO);
        return voList;
    }

    // 근로자 의견 관련 API
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHsePolicySug(List<HsePolicySuggestionVO> voList) throws Exception {
        for (HsePolicySuggestionVO vo : voList) {
            vo.setUserId(SecurityUtil.getCurrentMemberId());
//			vo.setCompId(SecurityUtil.getCurrentCompId());
            if (vo.getCmd().equals("I")) {
                hsePolicyDao.insertHsePolicySug(vo);
            } else {
                hsePolicyDao.updateHsePolicySug(vo);
            }
        }
        return voList.get(0);
    }

    // 근로자 의견 관련 API
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO deleteHsePolicySug(List<HsePolicySuggestionVO> voList) throws Exception {
        for (HsePolicySuggestionVO vo : voList) {
            vo.setUserId(SecurityUtil.getCurrentMemberId());
            hsePolicyDao.deleteHsePolicySug(vo);
        }
        return voList.get(0);
    }
}
