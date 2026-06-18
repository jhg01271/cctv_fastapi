package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.HazmatChecklistDAO;
import kr.co.igns.business.impl.dao.postgres.HazmatInoutDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.code.YesNo;
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

@Service
@RequiredArgsConstructor
public class HazmatInoutService {
    private final HazmatInoutDAO hazmatInoutDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final String targetType = "HIO";

    public List<HazmatInoutVO> getHazmatInout(SpSearchVO searchVo) throws Exception {
        List<HazmatInoutVO> voList = hazmatInoutDao.getHazmatInout(searchVo);
        return voList;
    }

    public HazmatInoutVO getHazmatInoutDetail(SpSearchVO searchVo) throws Exception {
        List<HazmatInoutVO> vo = hazmatInoutDao.getHazmatInout(searchVo);
        if (vo == null || vo.isEmpty()) {
            return null;
        }
        vo.get(0).setDetailList(hazmatInoutDao.getHazmatInoutDetail(searchVo));

        return vo.get(0);
    }

    //입출력 출력물 조회용
    public HazmatInoutVO getHazmatInoutDetailForReport(HazmatInoutVO pVo, SpSearchVO searchVo) throws Exception {
        List<HazmatInoutDetailVO> detailVo = hazmatInoutDao.getHazmatInoutByMonth(searchVo);
        // 입출고 합계
        pVo.setTotalInQty(detailVo.stream().mapToInt(vo -> vo.getInQty() == null ? 0 : vo.getInQty()).sum());
        pVo.setTotalOutQty(detailVo.stream().mapToInt(vo -> vo.getOutQty() == null ? 0 : vo.getOutQty()).sum());

        // 재고 합계 : doc_seq가 null이 아닌 데이터 중에서, inout_dt가 가장 큰 데이터의 store_qty를 구하기
        Optional<HazmatInoutDetailVO> latest = detailVo.stream()
                .filter(detail -> detail.getDocSeq() != null && detail.getStoreQty() != null)  // doc_seq가 null이 아닌 데이터만 필터링
                .max(Comparator.comparing(HazmatInoutDetailVO::getInoutDt));  // inout_dt가 큰 데이터를 찾기

        // 가장 큰 inout_dt를 가진 데이터가 있으면 store_qty를 리턴
        pVo.setTotalStoreQty(latest.map(HazmatInoutDetailVO::getStoreQty).orElse(0));
        pVo.setDetailList(detailVo);

        return pVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveHazmatInout(HazmatInoutVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 유해화학물질 입출고 마스터 추가
            vo.setDocType(targetType);
            hazmatInoutDao.insertHazmatInout(vo);
            // 유해화학물질 입출고 내역 추가
            for(HazmatInoutDetailVO detail : vo.getDetailList()){
            	detail.setWriteYear(vo.getWriteYear());
            	detail.setDocType(vo.getDocType());
                detail.setDocNo(vo.getDocNo());
                detail.setCreatedBy(vo.getCreatedBy());
                hazmatInoutDao.insertHazmatInoutDetail(detail);
            }
        }
        else {
            // 유해화학물질 입출고 마스터 수정
            HazmatInoutVO resultVO = hazmatInoutDao.getHazmatInoutById(vo);
            if (resultVO == null)
                return null;
            resultVO = (HazmatInoutVO) SpUtils.objectMap(vo, resultVO);

            hazmatInoutDao.updateHazmatInout(resultVO);

            //유해화학물질 입출고 내역 추가/수정
            for(HazmatInoutDetailVO detail : vo.getDetailList()){
                detail.setCreatedBy(vo.getUpdatedBy());
                detail.setUpdatedBy(vo.getUpdatedBy());
                if (detail.getCmd().equals("I")) {
                	detail.setWriteYear(resultVO.getWriteYear());
                	detail.setDocType(resultVO.getDocType());
                	detail.setDocNo(resultVO.getDocNo());
                    hazmatInoutDao.insertHazmatInoutDetail(detail);
                }
                else if (detail.getCmd().equals("D")) {
                    hazmatInoutDao.deleteHazmatInoutDetail(detail);
                } else {
                    HazmatInoutDetailVO resultVO2 = hazmatInoutDao.getHazmatInoutDetailById(detail);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (HazmatInoutDetailVO) SpUtils.objectMapNull(detail, resultVO2);

                    hazmatInoutDao.updateHazmatInoutDetail(resultVO2);
                }
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHazmatInout(List<HazmatInoutVO> voList) throws Exception {
        for(HazmatInoutVO vo : voList) {
            hazmatInoutDao.deleteHazmatInout(vo);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteHazmatInoutDetail(List<HazmatInoutDetailVO> voList) throws Exception {
        for(HazmatInoutDetailVO vo : voList) {
            hazmatInoutDao.deleteHazmatInoutDetail(vo);
        }
    }

    public List<JasperPrint> getHazmatInoutReport(HttpServletRequest request, HttpServletResponse response,
                                                      SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "위험물/유해화학물질 입출고" ;
        String title = "위험물/유해화학물질 입출고";

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

        //위험물/유해화학물질
        for(String docNo : spSearchVO.getCheckedList()) {
            if(spSearchVO.getSearchWords() == null || spSearchVO.getSearchWords().isEmpty()) {
                //월 전체 출력일 경우, 월 arrayList 생성
                ArrayList<String> months = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    // 월 데이터를 두 자리로 포맷하여 리스트에 추가
                    months.add(spSearchVO.getWriteYear() + String.format("%02d", i));
                }
                spSearchVO.setSearchWords(months);
            }

            //유해화학물질 마스터 조회
            spSearchVO.setDocNo(docNo);
            List<HazmatInoutVO> voList = getHazmatInout(spSearchVO);
            HazmatInoutVO vo = voList.get(0);
            if (spSearchVO.getCheckedList().size() == 1) {
                fileNm = "(" + spSearchVO.getWriteYear() + ")" + "위험물/유해화학물질 입출고_" + vo.getHazmatNm();
            }

            // 사용유무가 Y일 경우에만 출력
            if(vo.getUseYn().equals(YesNo.valueOf("Y"))) {
                //월별
                for (String month : spSearchVO.getSearchWords()) {
                    InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
                    //월 입출고 데이터 조회
                    spSearchVO.setSearchCd(month);
                    vo = getHazmatInoutDetailForReport(vo, spSearchVO);

                    ReportVO reportVO = new ReportVO();
                    reportVO.setFileName(fileNm);
                    reportVO.setJrxmlPath("report/impl/hazmatInout/hazmatInout.jrxml");
                    reportVO.setType(spSearchVO.getType());

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("logo", logo2);
                    params.put("title", title);

                    params.put("month", month.substring(0, 4) + "년 " + month.substring(4, 6) + "월");
                    params.put("totalInQty", vo.getTotalInQty());
                    params.put("totalOutQty", vo.getTotalOutQty());
                    params.put("totalStoreQty", vo.getTotalStoreQty());
                    params.put("hazmatNm", vo.getHazmatNm());

                    params.put("page", page);
                    params.put("subPage", subPage);
                    params.put("localPage", localPage);

                    // 위험물/유해화학물질 월별 입출고 데이터
                    List<Map<String, Object>> datasource = new ArrayList<>();

                    if (vo.getDetailList() != null) {
                        for (HazmatInoutDetailVO detail : vo.getDetailList()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("day", getDay(detail.getInoutDt()));
                            data.put("inQty", detail.getInQty());
                            data.put("outQty", detail.getOutQty());
                            data.put("storeQty", detail.getStoreQty());
                            data.put("remark", detail.getRemark());
                            datasource.add(data);
                        }
                    }

                    params.put("dataList", new JRBeanCollectionDataSource(datasource));
                    reportVO.setParameter(params);
                    JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
                    reportList.add(JasperReport);
                    page = page + JasperReport.getPages().size();
                    localPage = localPage + JasperReport.getPages().size();
                }
            }
        }

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    // 날짜 문자열에서 dd 부분을 추출하고 앞의 0을 제거
    public static int getDay(String date) {
        String day = date.substring(6, 8); // "yyyyMMdd"에서 dd 부분을 추출
        return Integer.parseInt(day); // 앞의 0을 자동으로 제거
    }

}
