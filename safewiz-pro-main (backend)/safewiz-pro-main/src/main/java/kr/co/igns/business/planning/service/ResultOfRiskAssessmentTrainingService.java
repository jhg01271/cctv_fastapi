package kr.co.igns.business.planning.service;

import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.business.planning.dao.postgres.ResultOfRiskAssessmentTrainingDAO;
import kr.co.igns.business.planning.model.ResultOfRiskAssessmentTrainingVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.framework.utils.type.StringUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.FileVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.service.NasFileService;
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
public class ResultOfRiskAssessmentTrainingService {
    private final ResultOfRiskAssessmentTrainingDAO resultOfRiskAssessmentTrainingDAO;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final NasFileService nasFileService;

    public List<ResultOfRiskAssessmentTrainingVO> getResultOfRiskAssessmentTraining(SpSearchVO vo) throws Exception {
        return resultOfRiskAssessmentTrainingDAO.getResultOfRiskAssessmentTraining(vo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertResultOfRiskAssessmentTraining(
            List<MultipartFile> files,
            ResultOfRiskAssessmentTrainingVO reqVo) throws Exception {

        reqVo.setCompId(SecurityUtil.getCurrentCompId());
        String targetType = "RORAT";

        reqVo.setDocType(targetType);

        resultOfRiskAssessmentTrainingDAO.insertResultOfRiskAssessmentTraining(reqVo);

        // 파일 등록
        fileService.saveFile(files, targetType, reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());

        return reqVo;
    }

    public ResultOfRiskAssessmentTrainingVO getResultOfRiskAssessmentTrainingDetail(
            SpSearchVO vo) throws Exception {
        ResultOfRiskAssessmentTrainingVO result = new ResultOfRiskAssessmentTrainingVO();
        // 데이터 가져오기
        result = resultOfRiskAssessmentTrainingDAO.getResultOfRiskAssessmentTrainingDetail(vo);

        // 파일 목록 가져오기
        result.setFiles(resultOfRiskAssessmentTrainingDAO.getResultOfRiskAssessmentTrainingFileList(vo));

        // 참석자 목록 가져오기
        result.setAsmntAttendees(resultOfRiskAssessmentTrainingDAO.getResultOfRiskAssessmentTrainingAttendees(vo));

        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateResultOfRiskAssessmentTraining(
            List<MultipartFile> files,
            ResultOfRiskAssessmentTrainingVO reqVo) throws Exception {

        resultOfRiskAssessmentTrainingDAO.updateResultOfRiskAssessmentTraining(reqVo);

        String targetType = "RORAT";

        // 파일 삭제 목록 처리
        fileService.deleteFile(reqVo.getDeleteFiles(), targetType, reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());

        // 파일 추가 목록 처리
        fileService.saveFile(files, targetType, reqVo.getWriteYear() + reqVo.getDocNo(), SecurityUtil.getCurrentCompId());

        return reqVo;
    }

    public void deleteResultOfRiskAssessmentTraining(List<ResultOfRiskAssessmentTrainingVO> voList) throws Exception {
        for (ResultOfRiskAssessmentTrainingVO vo : voList) {
            resultOfRiskAssessmentTrainingDAO.deleteResultOfRiskAssessmentTraining(vo);
        }
    }

    // 안전보건정보 출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        List<JasperPrint> jaspers = new ArrayList<>();
        spSearchVO.setCheckedObjList(resultOfRiskAssessmentTrainingDAO.getAllReport(spSearchVO));
        if (!spSearchVO.getCheckedObjList().isEmpty()) {
            spSearchVO.setPrintAll(true);
            jaspers = getResultOfRiskAssessmentTrainingReport(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    public List<JasperPrint> getResultOfRiskAssessmentTrainingReport(
            HttpServletRequest request,
            HttpServletResponse response,
            SpSearchVO spSearchVO) throws Exception {

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        /*
         * 인트로 페이지
         */
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "위험성평가 교육/참여 결과";

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, "위험성평가 교육/참여 결과");
        jasperPrintList.add(JasperFrontReport);
        page += JasperFrontReport.getPages().size();
        localPage += JasperFrontReport.getPages().size();

        /*
         * 데이터 페이지
         */

        String fileNm2 = "";
        // 선택한 데이터 전부 출력
        for (int i = 0; i < spSearchVO.getCheckedObjList().size(); i++) {
            SpSearchVO vo = spSearchVO.getCheckedObjList().get(i);
            // 데이터 조회
            ResultOfRiskAssessmentTrainingVO result = getResultOfRiskAssessmentTrainingDetail(vo);

            ReportVO reportVO = new ReportVO();

            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/planning/resultOfRiskAssessmentTraining/resultOfRiskAssessmentTraining.jrxml");
            reportVO.setType("pdf");

            // 파라미터 세팅
            Map<String, Object> params = new HashMap<>();
            params.put("title", "위험성평가 교육/참여 결과");
            params.put("subTitle", "사업장명: " + compNm);

            // 고객사 로고
            InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", clntLogo);

            // 파일 목록
            List<Map<String, Object>> imageList = new ArrayList<>();
            List<FileVO> fileVOList = resultOfRiskAssessmentTrainingDAO.getResultOfRiskAssessmentTrainingFileList(vo);
            for (FileVO fileVO : fileVOList) {
                HashMap<String, Object> image = new HashMap<>();
                // 이미지 파일만 추출
                if (fileVO.getDocType().equals("IMAGE")) {
                    image.put("img", nasFileService.getNasFileInputStream(fileVO.getFileId()));
                    imageList.add(image);
                }
            }

            // 파일이 없을때 null 이라도 넣어서 출력물 디자인 조정
            if (imageList.isEmpty()) {
                HashMap<String, Object> image = new HashMap<>();
                image.put("img", null);
                imageList.add(image);
            }

            params.put("imageList", new JRBeanCollectionDataSource(imageList));

            // 데이터
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            params.put("asmntDate", DateUtils.formatDate(result.getAsmntDate()));

            if (i == 0) {
                fileNm2 = DateUtils.formatDate(result.getAsmntDate());
            }

            params.put("asmntStartTime", result.getAsmntStartTime());
            params.put("asmntEndTime", result.getAsmntEndTime());
            params.put("asmntPlace", result.getAsmntPlace());
            params.put("asmntContent", result.getAsmntContent());

            // 참석자 리스트 데이터
            List<Map<String, Object>> attendeesList1 = new ArrayList<>();
            List<Map<String, Object>> attendeesList2 = new ArrayList<>();

            int index = 1;
            // 참석자 목록
            for (SignatureDto dto : result.getAsmntAttendees()) {

                Map<String, Object> attendee = new HashMap<>();

                attendee.put("orgnNm", StringUtils.defaultString(dto.getOrgnNm(), ""));
                attendee.put("jbrpNm", StringUtils.defaultString(dto.getJbrpNm(), ""));
                attendee.put("hrNm", StringUtils.defaultString(dto.getHrNm(), ""));
                attendee.put("signature", utilsService.convertToInputStream(dto.getSignature()));

                // 순서가 짝수인 데이터는 2번 리스트로 세팅
                if (index % 2 == 0) {
                    attendeesList2.add(attendee);
                } else {
                    attendeesList1.add(attendee);
                }
                index++;
            }
            if (index % 2 == 0) {
                /* 참석자가 홀수명 일때 빈 값을 채워 짝수로 맞춤 */
                /* 마지막에 ++ 하므로 %2 == 0 으로 조건 */
                Map<String, Object> attendee = new HashMap<>();
                attendee.put("orgnNm", "");
                attendee.put("jbrpNm", "");
                attendee.put("hrNm", "");
                attendee.put("signature", null);
                attendeesList2.add(attendee);

            }

            params.put("attendeesList1", new JRBeanCollectionDataSource(attendeesList1));
            params.put("attendeesList2", new JRBeanCollectionDataSource(attendeesList2));

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            jasperPrintList.add(JasperReport);

            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
        }

        if (spSearchVO.getCheckedObjList().size() == 1) {
            fileNm = fileNm + "_" + fileNm2;
        }

        fileNm = "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")" + fileNm;

        if (spSearchVO.isPrintAll()) {
            return jasperPrintList;
        } else {
            reportService.exportReportAll(request, response, jasperPrintList, spSearchVO.getType(), fileNm);
        }
        return jasperPrintList;
    }
}
