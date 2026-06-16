package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.WHMgmtGuideDAO;
import kr.co.igns.business.impl.model.WHMgmtGuideVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WHMgmtGuideService {
    private final WHMgmtGuideDAO wHMgmtGuideDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final String targetType = "WHMG";
    private final FileService fileService;

    public List<WHMgmtGuideVO> getWHMgmtGuide(SpSearchVO searchVo) throws Exception {
        List<WHMgmtGuideVO> voList = wHMgmtGuideDao.getWHMgmtGuide(searchVo);

        //인원매핑
        for(WHMgmtGuideVO vo : voList){
            searchVo.setSearchText("H");    //근로자 건강 상담일지 - 상담자
            vo.setHrListH(wHMgmtGuideDao.getWHMgmtGuideHr(searchVo));
        }
        return voList;
    }

    public WHMgmtGuideVO getWHMgmtGuideDetail(SpSearchVO searchVo) throws Exception {
        WHMgmtGuideVO vo = wHMgmtGuideDao.getWHMgmtGuideDetail(searchVo);

        searchVo.setSearchText("H");    //근로자 건강 상담일지 - 상담자
        vo.setHrListH(wHMgmtGuideDao.getWHMgmtGuideHr(searchVo));
        searchVo.setSearchText("R");    //근로자 보건 및 작업환경 개선요청 상담일지 - 상담자
        vo.setHrListR(wHMgmtGuideDao.getWHMgmtGuideHr(searchVo));
        searchVo.setSearchText("C");    //근로자 보건 및 작업환경 개선요청 상담일지 - 내담자
        vo.setHrListC(wHMgmtGuideDao.getWHMgmtGuideHr(searchVo));
        vo.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "WHMG"));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveWHMgmtGuide(List<MultipartFile> files, WHMgmtGuideVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 근로자 건강관리 지침 신규
            vo.setDocType(targetType);
            wHMgmtGuideDao.insertWHMgmtGuide(vo);
            fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            //근로자 건강 상담일지 추가/수정
            if(vo.getCounselChecked().getKey().equals("Yes")) {
                wHMgmtGuideDao.insertWHMgmtGuideCounsel(vo);
                //#region인원 매핑
                for (HrVO hr : vo.getHrListH()) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("H");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }
                //#endregion
            }
            //근로자 보건 및 작업환경 개선요청 상담일지 추가/수정
            if(vo.getRequestChecked().getKey().equals("Yes")) {
                wHMgmtGuideDao.insertWHMgmtGuideRequest(vo);
                //#region인원 매핑
                for (HrVO hr : vo.getHrListR()) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("R");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }
                for (HrVO hr : vo.getHrListC()) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("C");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }
                //#endregion
            }
        } else {
            //인원 매핑 조회를 위한 파라미터 생성
            SpSearchVO searchVo = new SpSearchVO();
            searchVo.setWriteYear(vo.getWriteYear());
            searchVo.setDocType(vo.getDocType());
            searchVo.setDocNo(vo.getDocNo());
            searchVo.setCompId(vo.getCompId());
            List<HrVO> hrList =  wHMgmtGuideDao.getWHMgmtGuideHr(searchVo);

            // 근로자 건강관리 지침 수정
            WHMgmtGuideVO resultVO = wHMgmtGuideDao.getWHMgmtGuideById(vo);
            if (resultVO == null)
                return null;
                resultVO = (WHMgmtGuideVO) SpUtils.objectMap(vo, resultVO);

            wHMgmtGuideDao.updateWHMgmtGuide(resultVO);
            fileService.deleteFile(vo.getDeleteFiles(), targetType, vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            fileService.saveFile(files, targetType, vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
            //근로자 건강 상담일지 추가/수정
            if(vo.getCounselChecked().getKey().equals("Yes")) {
                if (vo.getCounselCmd().equals("I")) {
                    wHMgmtGuideDao.insertWHMgmtGuideCounsel(vo);
                } else {
                    WHMgmtGuideVO resultVO2 = wHMgmtGuideDao.getWHMgmtGuideCounselById(vo);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (WHMgmtGuideVO) SpUtils.objectMap(vo, resultVO2);
                    wHMgmtGuideDao.updateWHMgmtGuideCounsel(resultVO2);
                }
                //#region인원 매핑

                List<HrVO> filteredHrList = hrList.stream()
                        .filter(hr -> "H".equals(hr.getGubun()))
                        .collect(Collectors.toList());
                List<HrVO> ParamHrList = vo.getHrListH();

                // 1. hrList에만 있는 항목 삭제
                List<HrVO> toDelete = filteredHrList.stream()
                        .filter(hr -> ParamHrList.stream().noneMatch(newHr -> newHr.getHrId().equals(hr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toDelete) {
                    wHMgmtGuideDao.deleteWHMgmtGuideHr(hr);
                }

                // 2. hrListH에만 있는 항목 추가
                List<HrVO> toInsert = ParamHrList.stream()
                        .filter(newHr -> filteredHrList.stream().noneMatch(hr -> hr.getHrId().equals(newHr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toInsert) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("H");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }
                //#endregion
            }
            //근로자 보건 및 작업환경 개선요청 상담일지 추가/수정
            if(vo.getRequestChecked().getKey().equals("Yes")) {
                if (vo.getRequestCmd().equals("I")) {
                    wHMgmtGuideDao.insertWHMgmtGuideRequest(vo);
                } else {
                    WHMgmtGuideVO resultVO2 = wHMgmtGuideDao.getWHMgmtGuideRequestById(vo);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (WHMgmtGuideVO) SpUtils.objectMap(vo, resultVO2);

                    wHMgmtGuideDao.updateWHMgmtGuideRequest(resultVO2);
                }
                //#region인원 매핑

                List<HrVO> filteredHrList = hrList.stream()
                        .filter(hr -> "R".equals(hr.getGubun()))
                        .collect(Collectors.toList());
                List<HrVO> ParamHrListR = vo.getHrListR();

                // 1. hrList에만 있는 항목 삭제
                List<HrVO> toDelete = filteredHrList.stream()
                        .filter(hr -> ParamHrListR.stream().noneMatch(newHr -> newHr.getHrId().equals(hr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toDelete) {
                    wHMgmtGuideDao.deleteWHMgmtGuideHr(hr);
                }

                // 2. 파라미터에만 있는 항목 추가
                List<HrVO> toInsert = ParamHrListR.stream()
                        .filter(newHr -> filteredHrList.stream().noneMatch(hr -> hr.getHrId().equals(newHr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toInsert) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("R");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }

                List<HrVO> filteredHrList2 = hrList.stream()
                        .filter(hr -> "C".equals(hr.getGubun()))
                        .collect(Collectors.toList());
                List<HrVO> ParamHrListC = vo.getHrListC();

                // 1. hrList에만 있는 항목 삭제
                List<HrVO> toDelete2 = filteredHrList2.stream()
                        .filter(hr -> ParamHrListC.stream().noneMatch(newHr -> newHr.getHrId().equals(hr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toDelete2) {
                    wHMgmtGuideDao.deleteWHMgmtGuideHr(hr);
                }

                // 2. 파라미터에만 있는 항목 추가
                List<HrVO> toInsert2 = ParamHrListC.stream()
                        .filter(newHr -> filteredHrList2.stream().noneMatch(hr -> hr.getHrId().equals(newHr.getHrId())))
                        .collect(Collectors.toList());

                for (HrVO hr : toInsert2) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCompId(vo.getCompId());
                    hr.setGubun("C");
                    hr.setCreatedAt(vo.getCreatedAt());
                    hr.setCreatedBy(vo.getCreatedBy());
                    wHMgmtGuideDao.insertWHMgmtGuideHr(hr);
                }
                //#endregion
            }
        }

        return vo;
    }

    // 오늘 날짜로 등록되지않은 인원 수 조회
    public List<WHMgmtGuideVO> validateGuideByOrgn(WHMgmtGuideVO vo) throws Exception {
        List<WHMgmtGuideVO> nonExistList = wHMgmtGuideDao.getNonExistCntHrToday(vo);
        return nonExistList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveWHMgmtGuideByOrgn(List<WHMgmtGuideVO> voList) throws Exception {

        for (WHMgmtGuideVO vo : voList) {
            vo.setWriteYear(vo.getCounselDt().substring(0, 4));
            wHMgmtGuideDao.insertWHMgmtGuide(vo);
            wHMgmtGuideDao.insertWHMgmtGuideCounsel(vo);
            wHMgmtGuideDao.insertWHMgmtGuideRequest(vo);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteWHMgmtGuide(List<WHMgmtGuideVO> list) throws Exception {
        for (WHMgmtGuideVO tmp : list) {
            wHMgmtGuideDao.deleteWHMgmtGuide(tmp);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteWHMgmtGuideDetail(WHMgmtGuideVO vo) throws Exception {
        if(vo.getCounselChecked() != null && vo.getCounselChecked().getKey().equals("Yes")) {
            wHMgmtGuideDao.deleteWHMgmtGuideCounsel(vo);
        }
        else if(vo.getRequestChecked() != null && vo.getRequestChecked().getKey().equals("Yes")){
            wHMgmtGuideDao.deleteWHMgmtGuideRequest(vo);
        }
    }


    public List<JasperPrint> getWHMgmtGuideReport(HttpServletRequest request, HttpServletResponse response,
                                                     SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "근로자 건강 상담일지";
        String title = "근로자 건강 상담일지";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        ReportVO frontReportVO = new ReportVO();
        frontReportVO.setFileName(fileNm);
        frontReportVO.setJrxmlPath("report/utils/basicFront_reverse.jrxml");
        frontReportVO.setType(spSearchVO.getType());
        Map<String, Object> frontParam = new HashMap<String, Object>();
        frontParam.put("logo", logo);
        frontParam.put("title", title);
        frontParam.put("subTitle", "사업장명:" + compNm);
        frontParam.put("page", spSearchVO.getPage());
        frontParam.put("subPage", spSearchVO.getSubPage());
        frontParam.put("localPage", spSearchVO.getLocalPage());
        frontReportVO.setParameter(frontParam);
        JasperPrint JasperFrontReport = reportService.allReportJasperPrint(frontReportVO);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        ReportVO reportVO = new ReportVO();
        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/impl/tWHMgmtGuide/tWHMgmtGuide.jrxml");
        reportVO.setType(spSearchVO.getType());
        //spSearchVO.setDocNo(docNo);   //docNo는 어디에 쓰는건지

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("logo", logo2);
        params.put("title", title);

        
        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        
        // 인원
        List<Map<String, Object>> datasource = new ArrayList<>(); // 평가데이터
        
        
        for (String docNo : spSearchVO.getCheckedList()) {
        	spSearchVO.setDocNo(docNo);
            WHMgmtGuideVO detailList = wHMgmtGuideDao.getWHMgmtGuideDetail(spSearchVO);
            spSearchVO.setSearchText("H");    //근로자 건강 상담일지 - 상담자
            detailList.setHrListH(wHMgmtGuideDao.getWHMgmtGuideHr(spSearchVO));
            if (detailList != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("orgnNm", detailList.getOrgnNm());
                data.put("hrNm", detailList.getHrNm());
                data.put("sex", detailList.getSex() == null ? null : detailList.getSex().equals("M") ? "남" : "여");
                data.put("age", detailList.getAge());
                data.put("serviceYears", detailList.getServiceYears());
                data.put("healthOpinion", detailList.getHealthOpinion());
                data.put("aftercareOpinion", detailList.getAftercareOpinion());
                data.put("workAptitudeYn", detailList.getWorkAptitudeYn() == null ? "" : detailList.getWorkAptitudeYn().toString());
                data.put("correctiveAction", detailList.getCorrectiveAction());
                data.put("counselDt", detailList.getCounselDt());
                // 상담자 - 배열을 콤마(,)로 구분하여 string으로 넣음
                List<Map<String, Object>> datasource1 = new ArrayList<>(); // 평가데이터
                if (detailList.getHrListH() != null && !detailList.getHrListH().isEmpty()) {
                    StringBuilder counselors = new StringBuilder();
                    for (HrVO hr : detailList.getHrListH()) {
                        if (counselors.length() > 0) {
                            counselors.append(",");
                        }
                        counselors.append(hr.getHrNm());
                    }
                    data.put("counselorList", counselors.toString());
                }
                datasource.add(data);
            }
            
        }
        // 기간 구하여 넣기
     // 최소, 최대 상담일자 구하기
        String minCounselDt = null;
        String maxCounselDt = null;

        for (Map<String, Object> data : datasource) {
            String counselDtStr = (String) data.get("counselDt");
            if (counselDtStr != null) {
                // 숫자로 변환하여 비교
                long counselDtNum = Long.parseLong(counselDtStr);
                if (minCounselDt == null || Long.parseLong(minCounselDt) > counselDtNum) {
                    minCounselDt = counselDtStr;
                }
                if (maxCounselDt == null || Long.parseLong(maxCounselDt) < counselDtNum) {
                    maxCounselDt = counselDtStr;
                }
            }
        }
      
        params.put("counselDt",  DateUtils.formatDate(minCounselDt) + " ~ " + DateUtils.formatDate(maxCounselDt));  //기간 yyyy.mm.dd 포맷으로 출력
        params.put("dataList", new JRBeanCollectionDataSource(datasource));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page = page + JasperReport.getPages().size();
        localPage = localPage + JasperReport.getPages().size();


        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public List<JasperPrint> getWHMgmtGuideRequestReport(HttpServletRequest request, HttpServletResponse response,
                                                  SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());

        String docNo = spSearchVO.getDocNo();
        spSearchVO.setDocNo(docNo);
        WHMgmtGuideVO detailList = getWHMgmtGuideDetail(spSearchVO);
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "근로자 보건 및 작업환경 개선요청 상담일지_" + detailList.getHrNm() + "_" + detailList.getCounselDt();
        String title = "근로자 보건 및 작업환경 \n 개선요청 상담일지";

        ReportVO reportVO = new ReportVO();
        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/impl/tWHMgmtGuide/tWHMgmtGuideRequest.jrxml");
        reportVO.setType(spSearchVO.getType());

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        ReportVO frontReportVO = new ReportVO();
        frontReportVO.setFileName(fileNm);
        frontReportVO.setJrxmlPath("report/utils/basicFront.jrxml");
        frontReportVO.setType(spSearchVO.getType());
        Map<String, Object> frontParam = new HashMap<String, Object>();
        frontParam.put("logo", logo);
        frontParam.put("title", title);
        frontParam.put("subTitle", "사업장명:" + compNm);
        frontParam.put("page", spSearchVO.getPage());
        frontParam.put("subPage", spSearchVO.getSubPage());
        frontParam.put("localPage", spSearchVO.getLocalPage());
        frontReportVO.setParameter(frontParam);
        JasperPrint JasperFrontReport = reportService.allReportJasperPrint(frontReportVO);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        spSearchVO.setSearchText("R");    //근로자 보건 및 작업환경 개선요청 상담일지 - 상담자
        detailList.setHrListR(wHMgmtGuideDao.getWHMgmtGuideHr(spSearchVO));

        // 상담자
        List<Map<String, Object>> datasource1 = new ArrayList<>(); // 평가데이터
        if (!detailList.getHrListR().isEmpty()) {
            for (HrVO hr : detailList.getHrListR()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("hrInfo", hr.getHrNm());
                datasource1.add(data);
            }
        }


        spSearchVO.setSearchText("C");    //근로자 보건 및 작업환경 개선요청 상담일지 - 내담자
        detailList.setHrListC(wHMgmtGuideDao.getWHMgmtGuideHr(spSearchVO));

        // 내담자
        List<Map<String, Object>> datasource2 = new ArrayList<>(); // 평가데이터
        if (!detailList.getHrListC().isEmpty()) {
            for (HrVO hr : detailList.getHrListC()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("hrInfo", hr.getHrNm());
                datasource2.add(data);
            }
        }

        // 두 데이터소스의 길이를 맞추기 위해 빈값 추가
        int maxSize = Math.max(datasource1.size(), datasource2.size());
        while (datasource1.size() < maxSize) {
            Map<String, Object> emptyData = new HashMap<>();
            emptyData.put("hrInfo", "");
            datasource1.add(emptyData);
        }
        while (datasource2.size() < maxSize) {
            Map<String, Object> emptyData = new HashMap<>();
            emptyData.put("hrInfo", "");
            datasource2.add(emptyData);
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("logo", logo2);
        params.put("title", title);


        // 기본 컬럼
        String time = "";
        if(detailList.getCounselTime() != null){
            time = " " + detailList.getCounselTime();
        }
        params.put("counselDt", SpUtils.formatDate1(detailList.getCounselDt()) + time);
        params.put("counselLocation", detailList.getCounselLocation());
        params.put("counselReason", detailList.getCounselReason());
        params.put("counselContent", detailList.getCounselContent());
        params.put("counselResult", detailList.getCounselResult());

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        params.put("counselorList", new JRBeanCollectionDataSource(datasource1));
        params.put("participantList", new JRBeanCollectionDataSource(datasource2));
        reportVO.setParameter(params);
        JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
        reportList.add(JasperReport);
        page = page + JasperReport.getPages().size();
        localPage = localPage + JasperReport.getPages().size();


        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    //이름(조직, 직책) 형태로 string 반환
    public String returnHrinfo(HrVO hr){
        String hrInfo = hr.getHrNm() + "(" + hr.getOrgnNm() + "," + hr.getJbrpNm()+ ")";
        if(hr.getOrgnNm() != null && !hr.getOrgnNm().isEmpty() && hr.getJbrpNm() != null && !hr.getJbrpNm().isEmpty()){
            hrInfo = hrInfo + "(" + hr.getOrgnNm()+ ", " + hr.getJbrpNm() + ")";
        }
        else if(hr.getOrgnNm() != null && !hr.getOrgnNm().isEmpty()){
            hrInfo = hrInfo + "(" + hr.getOrgnNm() + ")";
        }
        else if(hr.getJbrpNm() != null && !hr.getJbrpNm().isEmpty()){
            hrInfo = hrInfo + "(" + hr.getJbrpNm() + ")";
        }
        return hrInfo;
    }
}
