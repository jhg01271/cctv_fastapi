package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.HazmatChecklistDAO;
import kr.co.igns.business.impl.dao.postgres.WHMgmtGuideDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.planning.model.MsdsVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HazmatChecklistService {
    private final HazmatChecklistDAO hazmatChecklistDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final String targetType = "HCL";

    public List<HazmatChecklistVO> getHazmatChecklist(SpSearchVO searchVo) throws Exception {
        List<HazmatChecklistVO> voList = hazmatChecklistDao.getHazmatChecklist(searchVo);

        //점검사항 조회
        for(HazmatChecklistVO vo : voList){
            vo.setDetailList(hazmatChecklistDao.getHazmatChecklistDetail(vo));
            vo.setMsdsList(hazmatChecklistDao.getMsdsByWorkplace(vo));
        }
        return voList;
    }

    public HazmatChecklistVO getHazmatChecklistDetail(HazmatChecklistVO searchVo) throws Exception {

        //작업장, 점검일자로 조회
        HazmatChecklistVO vo = new HazmatChecklistVO();
        if(searchVo.getWorkplaceId() == null){
            //출력물 조회용(출력물은 docNo로 조회하기때문에 따로 분기)
            vo = hazmatChecklistDao.getHazmatChecklistByDocNo(searchVo);
        }
        else {
            vo = hazmatChecklistDao.existHazmatChecklist(searchVo);
        }

        if(vo != null) {
            List<HazmatChecklistDetailVO> detailList = hazmatChecklistDao.getHazmatChecklistDetail(vo);
            if (!detailList.isEmpty()) {
                vo.setDetailList(detailList);
            }
        }
        else{
            //점검일지 없으면 빈값 반환
            vo = new HazmatChecklistVO();
            vo.setWorkplaceId(searchVo.getWorkplaceId());
            vo.setChecklistDt(searchVo.getChecklistDt());
        }

        vo.setMsdsList(hazmatChecklistDao.getMsdsByWorkplace(vo));

        return vo;
    }

    public List<MsdsVO> getMsdsByWorkplace(HazmatChecklistVO searchVo) throws Exception {
        List<MsdsVO> voList = hazmatChecklistDao.getMsdsByWorkplace(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHazmatChecklist(HazmatChecklistVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 작업장 점검 마스터 추가
            vo.setDocType(targetType);
            hazmatChecklistDao.insertHazmatChecklist(vo);
            // 작업장 점검사항 추가
            for(HazmatChecklistDetailVO detail : vo.getDetailList()){
            	detail.setWriteYear(vo.getWriteYear());
            	detail.setDocType(vo.getDocType());
                detail.setDocNo(vo.getDocNo());
                detail.setCreatedBy(vo.getCreatedBy());
                hazmatChecklistDao.insertHazmatChecklistDetail(detail);
            }
        }
        else {
            // 작업장 점검 마스터 수정
            HazmatChecklistVO resultVO = hazmatChecklistDao.getHazmatChecklistById(vo);
            if (resultVO == null)
                return null;
            resultVO = (HazmatChecklistVO) SpUtils.objectMap(vo, resultVO);

            hazmatChecklistDao.updateHazmatChecklist(resultVO);

          //작업장 점검사항 추가/수정
            if (vo.getDetailList() != null && !vo.getDetailList().isEmpty()) {
                hazmatChecklistDao.deleteHazmatChecklistDetail(vo);
	            for(HazmatChecklistDetailVO detail : vo.getDetailList()){
                    detail.setWriteYear(vo.getWriteYear());
                    detail.setDocType(vo.getDocType());
                    detail.setDocNo(vo.getDocNo());
                    detail.setCreatedBy(vo.getCreatedBy());
                    hazmatChecklistDao.insertHazmatChecklistDetail(detail);
	            }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHazmatChecklist(List<HazmatChecklistVO> list) throws Exception {
        for (HazmatChecklistVO tmp : list) {
            hazmatChecklistDao.deleteHazmatChecklist(tmp);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHazmatChecklistDetail(HazmatChecklistVO vo) throws Exception {
        hazmatChecklistDao.initHazmatChecklistDetail(vo);
    }

    public List<JasperPrint> getHazmatChecklistReport(HttpServletRequest request, HttpServletResponse response,
                                                   SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "위험물/유해화학물질 점검리스트" ;
        String title = "위험물/유해화학물질 점검리스트";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
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

        //조회조건 세팅
        HazmatChecklistVO vo = new HazmatChecklistVO();
        vo.setWriteYear(spSearchVO.getWriteYear());
        vo.setDocType(spSearchVO.getDocType());
        vo.setCompId(spSearchVO.getCompId());
        for(String docNo : spSearchVO.getCheckedList()) {
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            //데이터 조회
            vo.setChecklistDt(null);
            vo.setWorkplaceId(null);
            vo.setDocNo(docNo);
            vo = getHazmatChecklistDetail(vo);
            if(spSearchVO.getCheckedList().size() == 1) {
                fileNm = "(" + spSearchVO.getWriteYear() + ")" + "위험물/유해화학물질 점검리스트_" + vo.getWorkplaceNm();
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setFileName(fileNm);
            reportVO.setJrxmlPath("report/impl/hazmatChecklist/hazmatChecklist.jrxml");
            reportVO.setType(spSearchVO.getType());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("logo", logo2);
            params.put("title", title);
            params.put("workplaceNm", vo.getWorkplaceNm());

            String dt = DateUtils.formatDate(vo.getChecklistDt());
            String time = "";
            if (vo.getChecklistStart() != null && !vo.getChecklistStart().isEmpty()) {
                time = " " + vo.getChecklistStart() + " ~ " + vo.getChecklistEnd();
            }
            String period = dt + time;
            params.put("checklistDt", period);

            // 서명 데이터 조회
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(spSearchVO.getDocType());
            signParam.setTargetId(spSearchVO.getWriteYear() + docNo);
            
            //점검자, 확인자
            List<String> types = List.of("inspector", "confirmer");

            for (String type : types) {
                signParam.setType(type);
                for (UtilsVO info : utilsService.getApprovalInfo(signParam)) {
                    params.put(type + "Nm", info.getHrNm());
                    params.put(type, utilsService.getSignatureFromBase64String(info.getSignature()));
                }
            }

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // 위험물/유해화학물질 점검사항
            List<Map<String, Object>> datasource = new ArrayList<>();

            //점검 항목이 없을때, 빈값 생성
            if(vo.getDetailList() == null){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("checklistNm", "");
                datasource.add(data);
            }
            if (vo.getDetailList() != null) {
                for (HazmatChecklistDetailVO detail : vo.getDetailList()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("checklistSeq", detail.getChecklistId());
                    data.put("checklistNm", detail.getChecklistNm());
                    data.put("checklistItemSeq", detail.getDocSeq());
                    data.put("checklistItemNm", detail.getChecklistItemNm());
                    String result = detail.getAcceptableYn().equals("Y") ? "O" :
                            detail.getNonCompliantYn().equals("Y") ? "X" :
                                    detail.getRequireCheckYn().equals("Y") ? "△" : "";
                    data.put("resultYn", result);
                    data.put("checklistAction", detail.getChecklistAction());
                    data.put("compId", detail.getCompId());
                    datasource.add(data);
                }
            }

            params.put("checklistData", new JRBeanCollectionDataSource(datasource));
            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();
        }

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public List<JasperPrint> getHazmatStatusReport(HttpServletRequest request, HttpServletResponse response,
                                                     SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());

        //위험물/유해화학물질 현황표 조회
        HazmatChecklistVO vo = new HazmatChecklistVO();
        vo.setWriteYear(spSearchVO.getWriteYear());
        vo.setDocType(spSearchVO.getDocType());
        vo.setDocNo(spSearchVO.getDocNo());
        vo.setChecklistDt(spSearchVO.getSearchText2());
        vo.setWorkplaceId(spSearchVO.getSearchText());
        vo.setCompId(spSearchVO.getCompId());
        vo =  getHazmatChecklistDetail(vo);
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "위험물/유해화학물질 현황표_" + vo.getWorkplaceNm();
        String title = "위험물/유해화학물질 현황표";

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

        ReportVO reportVO = new ReportVO();
        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/impl/hazmatChecklist/hazmatCheckStatus.jrxml");
        reportVO.setType(spSearchVO.getType());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("logo", logo2);
        params.put("title", title);

        params.put("page", page);
        params.put("subPage", subPage);
        params.put("localPage", localPage);

        // 위험물/유해화학물질 리스트
        List<Map<String, Object>> datasource = new ArrayList<>();

        //위험물/유해화학물질이 없을때, 빈값 생성
        if(vo.getMsdsList().isEmpty()){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("characteristic", "");
            datasource.add(data);
        }
        for (int i = 0; i < vo.getMsdsList().size(); i++) {
            MsdsVO msds = vo.getMsdsList().get(i);
            if (msds != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("no", i+1);
                data.put("type", "유해화학물질/" + msds.getMsdsNm());
                data.put("characteristic", "");
                data.put("use", "");
                data.put("orgn", "");
                data.put("remark", "");
                datasource.add(data);
            }
        }

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

    //마지막 점검사항 불러오기
    public List<HazmatChecklistDetailVO> getLastChecklist(SpSearchVO searchVo) throws Exception {
        List<HazmatChecklistDetailVO> vo = hazmatChecklistDao.getLastChecklist(searchVo);

        return vo;
    }

    //#region 위험물/유해화학물질 점검사항 관리 팝업

    //예시불러오기
    public List<DatasetHazmatChecklistVO> getBaseDatasetHazmatChecklist(SpSearchVO searchVo) throws Exception {
        List<DatasetHazmatChecklistVO> voList = hazmatChecklistDao.getBaseDatasetHazmatChecklist(searchVo);
        for (DatasetHazmatChecklistVO vo : voList ) {
            List<DatasetHazmatChecklistVO> itemList= hazmatChecklistDao.getBaseDatasetHazmatChecklistDetail(vo);
            vo.setItemList(itemList);
        }
        return voList;
    }

    public List<DatasetHazmatChecklistVO> getDatasetHazmatChecklist(SpSearchVO searchVo) throws Exception {
        List<DatasetHazmatChecklistVO> voList = hazmatChecklistDao.getDatasetHazmatChecklist(searchVo);
        for (DatasetHazmatChecklistVO vo : voList ) {
	   		 List<DatasetHazmatChecklistVO> itemList= hazmatChecklistDao.getDatasetHazmatChecklistDetail(vo);
	   		 vo.setItemList(itemList);
        }
        return voList;
    }

    public List<DatasetHazmatChecklistVO> getDatasetHazmatChecklistDetail(DatasetHazmatChecklistVO vo) throws Exception {
        List<DatasetHazmatChecklistVO> voList = hazmatChecklistDao.getDatasetHazmatChecklistDetail(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<DatasetHazmatChecklistVO> saveDatasetHazmatChecklist(List<DatasetHazmatChecklistVO> voList) throws Exception {
        for(DatasetHazmatChecklistVO vo : voList){
            if (vo.getCmd().equals("I")) {
                // 점검사항 추가
                hazmatChecklistDao.insertDatasetHazmatChecklist(vo);

                // 점검사항 항목 추가
                for(DatasetHazmatChecklistVO detail : vo.getItemList()){
                    detail.setChecklistId(vo.getChecklistId());
                    hazmatChecklistDao.insertDatasetHazmatChecklistDetail(detail);
                }
            } else {
                // 점검사항 수정
                DatasetHazmatChecklistVO resultVO = hazmatChecklistDao.getDatasetHazmatChecklistById(vo);
                if (resultVO == null)
                    return null;
                resultVO = (DatasetHazmatChecklistVO) SpUtils.objectMap(vo, resultVO);

                hazmatChecklistDao.updateDatasetHazmatChecklist(resultVO);

                // 점검사항 항목 추가/수정
                for(DatasetHazmatChecklistVO detail : vo.getItemList()){
                    if (detail.getCmd().equals("I")) {
                        // 점검사항 추가
                        hazmatChecklistDao.insertDatasetHazmatChecklistDetail(detail);
                    } else {
                        // 점검사항 수정
                        DatasetHazmatChecklistVO resultVO2 = hazmatChecklistDao.getDatasetHazmatChecklistDetailById(detail);
                        if (resultVO2 == null)
                            return null;
                        resultVO2 = (DatasetHazmatChecklistVO) SpUtils.objectMap(detail, resultVO2);

                        hazmatChecklistDao.updateDatasetHazmatChecklistDetail(resultVO2);
                    }
                }
            }
        }

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteDatasetHazmatChecklistDetail(List<DatasetHazmatChecklistVO> voList) throws Exception {
        for(DatasetHazmatChecklistVO vo : voList){
            hazmatChecklistDao.deleteDatasetHazmatChecklistDetail(vo);
        }
    }
    //#endregion

}
