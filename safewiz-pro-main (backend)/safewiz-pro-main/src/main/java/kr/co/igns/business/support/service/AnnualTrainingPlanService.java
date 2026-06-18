package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.AnnualTrainingPlanDAO;
import kr.co.igns.business.support.model.*;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AnnualTrainingPlanService {
    private final AnnualTrainingPlanDAO annualTrainingPlanDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final HrDAO hrDao;
    private final String targetType = "ATP";

    public List<TrainingCoursesVO> getSubjectsMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingCoursesVO> voList = annualTrainingPlanDao.getSubjectsMngList(searchVo);
        return voList;
    }

    public List<TrainingCoursesVO> getDataSetSubjectsMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingCoursesVO> voList = annualTrainingPlanDao.getDataSetSubjectsMngList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingCoursesVO> saveSubjectsMngList(List<TrainingCoursesVO> voList) throws Exception {
        for(TrainingCoursesVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                annualTrainingPlanDao.insertSubjectsMngList(vo);
            }else{
                annualTrainingPlanDao.updateSubjectsMngList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingCoursesVO> deleteSubjectsMngList(List<TrainingCoursesVO> voList) throws Exception {
        for(TrainingCoursesVO vo : voList){
            annualTrainingPlanDao.deleteSubjectsMngList(vo);
        }
        return voList;
    }

    public List<TrainingCenterVO> getEduMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingCenterVO> voList = annualTrainingPlanDao.getEduMngList(searchVo);
        return voList;
    }

    public List<TrainingCenterVO> getDataSetEduMngList(SpSearchVO searchVo) throws Exception {
        List<TrainingCenterVO> voList = annualTrainingPlanDao.getDataSetEduMngList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingCenterVO> saveEduMngList(List<TrainingCenterVO> voList) throws Exception {
        for(TrainingCenterVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                annualTrainingPlanDao.insertEduMngList(vo);
            }else{
                annualTrainingPlanDao.updateEduMngList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainingCenterVO> deleteEduMngList(List<TrainingCenterVO> voList) throws Exception {
        for(TrainingCenterVO vo : voList){
            annualTrainingPlanDao.deleteEduMngList(vo);
        }
        return voList;
    }

    public List<TrainningInstructorVO> getInstMngList(SpSearchVO searchVo) throws Exception {
        List<TrainningInstructorVO> voList = annualTrainingPlanDao.getInstMngList(searchVo);
        return voList;
    }

    public List<TrainningInstructorVO> getDataSetInstMngList(SpSearchVO searchVo) throws Exception {
        List<TrainningInstructorVO> voList = annualTrainingPlanDao.getDataSetInstMngList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainningInstructorVO> saveInstMngList(List<TrainningInstructorVO> voList) throws Exception {
        for(TrainningInstructorVO vo : voList){
            vo.setCompId(SecurityUtil.getCurrentCompId());
            if(vo.getOrdSeq() == null){
                vo.setOrdSeq(99);
            }
            if(vo.getCmd().equals("I")){
                annualTrainingPlanDao.insertInstMngList(vo);
            }else{
                annualTrainingPlanDao.updateInstMngList(vo);
            }

        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<TrainningInstructorVO> deleteInstMngList(List<TrainningInstructorVO> voList) throws Exception {
        for(TrainningInstructorVO vo : voList){
            annualTrainingPlanDao.deleteInstMngList(vo);
        }
        return voList;
    }

    public List<AnnualTrainingPlanVO> getAnnualTrainingPlan(SpSearchVO searchVo) throws Exception {
        List<AnnualTrainingPlanVO> voList = annualTrainingPlanDao.getAnnualTrainingPlan(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveAnnualTrainingPlan(List<AnnualTrainingPlanVO> voList) throws Exception {
        for (AnnualTrainingPlanVO vo : voList) {
            String key = vo.getWriteYear() + vo.getDocNo();
            if (vo.getCmd().equals("I")) {
                vo.setDocType(targetType);
                annualTrainingPlanDao.insertAnnualTrainingPlan(vo);

                key = vo.getWriteYear() + vo.getDocNo();
                //인원 매핑 생성
                for (HrVO hrinfo: vo.getHrList()) {
                    hrinfo.setWriteYear(vo.getWriteYear());
                    hrinfo.setDocType(vo.getDocType());
                    hrinfo.setDocNo(vo.getDocNo());

                    annualTrainingPlanDao.insertAnnualTrainingPlanAnnualLearner(hrinfo);
                }
            } else {
                //HSE 업무분장 수정
                AnnualTrainingPlanVO annualTrainingPlanVO = annualTrainingPlanDao.getAnnualTrainingPlanById(vo);
                if (annualTrainingPlanVO == null)
                    return null;
                annualTrainingPlanVO = (AnnualTrainingPlanVO) SpUtils.objectMap(vo, annualTrainingPlanVO);

                annualTrainingPlanDao.updateAnnualTrainingPlan(annualTrainingPlanVO);
                annualTrainingPlanDao.deleteAnnualTrainingPlanAnnualLearner(vo);
                for (HrVO hrinfo: vo.getHrList()) {
                    hrinfo.setWriteYear(vo.getWriteYear());
                    hrinfo.setDocType(vo.getDocType());
                    hrinfo.setDocNo(vo.getDocNo());

                    annualTrainingPlanDao.insertAnnualTrainingPlanAnnualLearner(hrinfo);
                }
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteAnnualTrainingPlan(AnnualTrainingPlanVO reqVo) throws Exception {
        BaseVO vo = annualTrainingPlanDao.getAnnualTrainingPlanById(reqVo);
        annualTrainingPlanDao.deleteAnnualTrainingPlan(reqVo);
        return vo;
    }

    public void deleteAnnualTrainingPlans(List<AnnualTrainingPlanVO> list) throws Exception {
        for (AnnualTrainingPlanVO tmp : list) {
            annualTrainingPlanDao.deleteAnnualTrainingPlan(tmp);
        }
    }

    //region 교육 강사
    public List<TrainingInstructorVO> getTrainingInstructor(SpSearchVO searchVo) throws Exception {
        List<TrainingInstructorVO> voList = annualTrainingPlanDao.getTrainingInstructor(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveTrainingInstructor(List<TrainingInstructorVO> voList) throws Exception {
        for (TrainingInstructorVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                //교육 강사 ID 자동 부여 (년월일순번)
                annualTrainingPlanDao.insertTrainingInstructor(vo);
            } else {
                //교육 강사 수정
                TrainingInstructorVO resultVo = annualTrainingPlanDao.getTrainingInstructorById(vo);
                if (resultVo == null)
                    return null;
                resultVo = (TrainingInstructorVO) SpUtils.objectMap(vo, resultVo);

                annualTrainingPlanDao.updateTrainingInstructor(resultVo);
            }
        }
        return voList.get(0);
    }

    public void deleteTrainingInstructors(List<TrainingInstructorVO> list) throws Exception {
        for (TrainingInstructorVO tmp : list) {
            annualTrainingPlanDao.deleteTrainingInstructor(tmp);
        }
    }
    //endregion

    // 일괄출력
    // 일괄출력 파라미터 조회
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        List<String> checkedList = annualTrainingPlanDao.getAllReport(spSearchVO);
        if (!checkedList.isEmpty()) {
            List<JasperPrint> jaspers = new ArrayList<>();
            spSearchVO.setCheckedList(checkedList);
            spSearchVO.setPrintAll(true);
            spSearchVO.setPage(page);
            spSearchVO.setSubPage(subPage);
            spSearchVO.setLocalPage(localPage);
            jaspers = getAnnualReport(request, response, spSearchVO);
            reportList.addAll(jaspers);
        }
        return reportList;
    }

    public List<JasperPrint> getAnnualReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {

        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전보건 교육 계획서";
        String title = spSearchVO.getWriteYear() + "년도 안전보건 교육 계획서";

        // 표지 리포트
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        ReportVO reportVO = new ReportVO();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse",spSearchVO,title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        reportVO.setFileName(fileNm);
        reportVO.setJrxmlPath("report/support/annualTrainingPlan/annualTrainingPlan.jrxml");
        reportVO.setType("pdf");

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        // 해당하는 페이지는 전체출력에 사용될 예정이므로 필수가 아닙니다.
        params.put("page", page); // 페이지
        params.put("subPage", subPage); // 서브페이지
        params.put("localPage", localPage); // 로컬페이지

        spSearchVO.setSearchText2(SecurityUtil.getCurrentCompId());
        List<AnnualTrainingPlanVO> resultList = annualTrainingPlanDao.getAnnualTrainingPlan(spSearchVO);

        if(!spSearchVO.isCheckedPrint()){
            resultList = resultList.stream()
                    .filter(plan -> plan.getUseYn().equals(YesNo.valueOf("Y")))
                    .collect(Collectors.toList());
        }
        else{
            resultList = resultList.stream()
                    .filter(item -> Objects.equals(item.getWriteYear(), spSearchVO.getWriteYear()) && spSearchVO.getCheckedList().contains(item.getDocNo()))
                    .collect(Collectors.toList());
        }

        //서명 추가
        BaseVO vo = new BaseVO();
        vo.setWriteYear(resultList.get(0).getWriteYear());
        vo.setDocType("ATP");
        vo.setDocNo(resultList.get(0).getCompId());
        List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
        params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));

        for (AnnualTrainingPlanVO bvo : resultList) {
            List<HrVO> hrlist = annualTrainingPlanDao.getAnnualTrainingPlanAnnualLearner(bvo);
            bvo.setHrList(hrlist);

            // hrList가 null인 경우를 대비하여 예외 처리
            if (bvo.getHrList().size() != 0) {
                int peoCnt = Math.max(0, bvo.getHrList().size() - 1); // 배열이 아닌 List라서 .size() 사용
                String peopleDisplay = peoCnt != 0 ? bvo.getHrList().get(0).getHrNm() + " 외 " + peoCnt + "명" : bvo.getHrList().get(0).getHrNm();
                bvo.setRole(peopleDisplay);
            } else {
                // hrList가 null이면 빈 값 설정
                bvo.setRole("");
            }
        }
        // resultList를 데이터소스로 만들어서 params에 넣기
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(resultList);
        resultList.stream().forEach(item -> {
            String instructorNm = item.getTrainingInstructorNm() == null? "": item.getTrainingInstructorNm();
            String instituteNm = item.getTrainingInstituteNm() == null? "": item.getTrainingInstituteNm();
            item.setTrainingInstructorNm(instructorNm + "\n(" + instituteNm +")");
        });
        params.put("dataList", dataSource);  // 리스트 자체를 Jasper에 넘김

        // 기타 파라미터 설정
        params.put("page", spSearchVO.getPage());
        params.put("subPage", spSearchVO.getSubPage());
        params.put("localPage", spSearchVO.getLocalPage());

        // 고객사 로고
        InputStream clntLogo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        params.put("logo", clntLogo);

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

    public List<AnnualTrainingPlanVO> getCurrentAndPreviousYear(SpSearchVO searchVo) throws Exception {
        if(searchVo.getWriteYear() != null){
            searchVo.setWriteYear(String.valueOf(Integer.parseInt(searchVo.getWriteYear()) - 1));
        }
        List<AnnualTrainingPlanVO> voList = annualTrainingPlanDao.getCurrentAndPreviousYear(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveCurrentAndPreviousYear(List<AnnualTrainingPlanVO> voList) throws Exception {
        if(voList == null)
            return null;
//        String newWriteYear = SpUtils.addNumToString(voList.get(0).getWriteYear(),1);
        voList.forEach(vo -> {
//            vo.setWriteYear(newWriteYear);
            vo.setCmd("I");
            vo.setUpdatedBy(null);
            vo.setUpdatedAt(null);
            vo.setUseYn(YesNo.valueOf("Y"));
        });
        return saveAnnualTrainingPlan(voList);
    }
}
