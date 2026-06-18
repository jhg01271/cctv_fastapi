package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.PermitToWorkDAO;
import kr.co.igns.business.impl.model.*;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermitToWorkService {
    private final PermitToWorkDAO permitToWorkDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final String targetType = "PTW";
    private final SpUtils spUtils;
    private final FileService fileService;


    public List<PermitToWorkVO> getPermitToWork(SpSearchVO searchVo) throws Exception {
        List<PermitToWorkVO> voList = permitToWorkDao.getPermitToWork(searchVo);
        for (PermitToWorkVO vo : voList) {
            searchVo.setDocNo(vo.getDocNo());
            //작업 종류 조회
            vo.setWorkTypeList(permitToWorkDao.getPermitToWorkType(searchVo).stream()
                    .filter(el -> "Yes".equals(el.getCheckYn().getKey()))
                    .collect(Collectors.toList()));
            //안전작업 점검사항 조회
            List<PermitToWorkDetailVO> detail = permitToWorkDao.getPermitToWorkDetail(searchVo);
            vo.setDetailList(detail);
        }
        return voList;
    }

    public PermitToWorkVO getPermitToWorkDetail(SpSearchVO searchVo) throws Exception {
        PermitToWorkVO vo = permitToWorkDao.getPermitToWorkByDocNo(searchVo);
        //안전작업 점검사항 조회
        List<PermitToWorkDetailVO> detail = permitToWorkDao.getPermitToWorkDetail(searchVo);
        for (PermitToWorkDetailVO detailVo : detail) {
            searchVo.setDocSeq(detailVo.getDocSeq());
            //안전기구 매핑 조회
            detailVo.setSafetyEqList(permitToWorkDao.getPermitToWorkEq(searchVo));
        }

        //#region ===== Inspection(점검사항만 조회) JS.SIM =====
        // `ordSeq` 순서대로 정렬된 inspectionId 리스트 생성
        List<String> sortedInspectionIds = detail.stream()
                .filter(detailVO -> detailVO.getInspectionId() != null) // null 제거
                .sorted(Comparator.comparing(PermitToWorkDetailVO::getOrdSeq)) // ordSeq 기준 정렬
                .map(PermitToWorkDetailVO::getInspectionId) // inspectionId 추출
                .distinct() // 중복 제거
                .collect(Collectors.toList()); // List로 변환하여 순서 유지

        //#region inspectionList가 null이면 빈 리스트로 초기화
        if (vo.getInspectionList() == null) {
            vo.setInspectionList(new ArrayList<>());
        }
        //#endregion

        // `ordSeq` 순서대로 inspectionId를 사용하여 데이터 추가
        for (String inspectionId : sortedInspectionIds) {
            SafetyWorkChecklistVO item = new SafetyWorkChecklistVO();
            item.setInspectionId(inspectionId);
            item.setCompId(vo.getCompId());

            // permitToWorkDao에서 데이터를 조회하여 추가
            vo.getInspectionList().add(permitToWorkDao.getSafetyWorkChecklistById(item));
        }

        //#endregion

        vo.setDetailList(detail);
        //작업 종류 조회
        vo.setWorkTypeList(permitToWorkDao.getPermitToWorkType(searchVo));
        //인원 매핑 조회
        vo.setHrList(permitToWorkDao.getPermitToWorkHr(searchVo));
        vo.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "PTW"));
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO savePermitToWork(PermitToWorkVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 작업허가서 추가
            vo.setDocType(targetType);
            permitToWorkDao.insertPermitToWork(vo);
            // 작업 종류 추가
            if (vo.getWorkTypeList() != null && !vo.getWorkTypeList().isEmpty()) {
                for (PermitToWorkTypeVO item : vo.getWorkTypeList()) {
                    item.setWriteYear(vo.getWriteYear());
                    item.setDocNo(vo.getDocNo());
                    item.setDocType(targetType);
                    item.setCreatedBy(vo.getCreatedBy());
                    permitToWorkDao.insertPermitToWorkType(item);
                }
            }
            // 안전작업 점검사항 추가
            if (vo.getDetailList() != null && !vo.getDetailList().isEmpty()) {
                for (PermitToWorkDetailVO item : vo.getDetailList()) {
                    item.setWriteYear(vo.getWriteYear());
                    item.setDocNo(vo.getDocNo());
                    item.setDocType(targetType);
                    item.setCreatedBy(vo.getCreatedBy());
                    permitToWorkDao.insertPermitToWorkDetail(item);

                    //#region 안전기구 매핑
                    if (item.getSafetyEqList() != null && !item.getSafetyEqList().isEmpty()) {
                        for (SafetyEquipmentVO eq : item.getSafetyEqList()) {
                            eq.setWriteYear(item.getWriteYear());
                            eq.setDocType(item.getDocType());
                            eq.setDocNo(item.getDocNo());
                            eq.setDocSeq(item.getDocSeq());
                            permitToWorkDao.insertPermitToWorkEq(eq);
                        }
                    }
                    //#endregion
                }
            }
            //#region 작업자 매핑
            if (vo.getHrList() != null && !vo.getHrList().isEmpty()) {
                for (HrVO hr : vo.getHrList()) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCreatedBy(vo.getCreatedBy());
                    permitToWorkDao.insertPermitToWorkHr(hr);
                }
            }
            //#endregion

        } else {
            // 작업허가서 수정
            PermitToWorkVO resultVO = permitToWorkDao.getPermitToWorkById(vo);
            if (resultVO == null)
                return null;
            resultVO = (PermitToWorkVO) SpUtils.objectMap(vo, resultVO);
            resultVO.setPartCompId(vo.getPartCompId());
            permitToWorkDao.updatePermitToWork(resultVO);
            // 작업 종류 수정 (신규일 때 무조건 생성하기 때문에 수정밖에 없음)
            for (PermitToWorkTypeVO item : vo.getWorkTypeList()) {
                if (item.getCheckYn() == YesNo.Y) {
                    PermitToWorkTypeVO resultVO2 = permitToWorkDao.getPermitToWorkTypeById(item);
                    if (resultVO2 == null) {
                        permitToWorkDao.insertPermitToWorkType(item);
                    } else {
                        permitToWorkDao.updatePermitToWorkType(item);
                    }
                } else {
                    permitToWorkDao.deletePermitToWorkType(item);
                }
            }

            // 안전작업 점검사항 추가/수정
            if (vo.getDetailList() != null && !vo.getDetailList().isEmpty()) {
                for (PermitToWorkDetailVO item : vo.getDetailList()) {
                    if (item.getCmd().equals("I")) {
                        item.setWriteYear(vo.getWriteYear());
                        item.setDocNo(vo.getDocNo());
                        item.setDocType(targetType);
                        item.setCreatedBy(vo.getCreatedBy());
                        permitToWorkDao.insertPermitToWorkDetail(item);

                        //#region 안전기구 매핑
                        if (item.getSafetyEqList() != null && !item.getSafetyEqList().isEmpty()) {
                            for (SafetyEquipmentVO eq : item.getSafetyEqList()) {
                                eq.setWriteYear(item.getWriteYear());
                                eq.setDocType(item.getDocType());
                                eq.setDocNo(item.getDocNo());
                                eq.setDocSeq(item.getDocSeq());
                                eq.setCreatedBy(item.getCreatedBy());
                                permitToWorkDao.insertPermitToWorkEq(eq);
                            }
                        }
                        //#endregion
                    } else if (item.getCmd().equals("D")) {
                        // 점검사항 - 안전작업 점검사항 삭제
                        permitToWorkDao.deletePermitToWorkDetail(item);
                        // 필요안전기구 삭제
                        permitToWorkDao.deletePermitToEqByWorkDetail(item);
                    } else {
                        PermitToWorkDetailVO resultVO2 = permitToWorkDao.getPermitToWorkDetailById(item);
                        if (resultVO2 == null)
                            return null;
                        resultVO2 = (PermitToWorkDetailVO) SpUtils.objectMap(item, resultVO2);
                        permitToWorkDao.updatePermitToWorkDetail(resultVO2);
                        //#region 안전기구 매핑
                        //안전기구 매핑 조회를 위한 파라미터 생성
                        SpSearchVO searchVo = new SpSearchVO();
                        searchVo.setWriteYear(item.getWriteYear());
                        searchVo.setDocType(item.getDocType());
                        searchVo.setDocNo(item.getDocNo());
                        searchVo.setDocSeq(item.getDocSeq());
                        List<SafetyEquipmentVO> eqList = permitToWorkDao.getPermitToWorkEq(searchVo);

                        List<SafetyEquipmentVO> ParamEqList = item.getSafetyEqList();

                        boolean isEqualIgnoreOrder = eqList != null && ParamEqList != null
                                && eqList.size() == ParamEqList.size()
                                && new HashSet<>(eqList).containsAll(ParamEqList)
                                && new HashSet<>(ParamEqList).containsAll(eqList);

                        // 1. eqList에만 있는 항목 삭제
                        if (!isEqualIgnoreOrder) {
                            List<SafetyEquipmentVO> toDelete;
                            if (Objects.requireNonNull(eqList).isEmpty() || Objects.requireNonNull(ParamEqList).isEmpty()) {
                                toDelete = eqList;
                            } else {
                                toDelete = eqList.stream()
                                        .filter(eq -> {
                                            return ParamEqList.stream().noneMatch(newEq -> newEq.getSafetyEqItemId().equals(eq.getSafetyEqItemId()));
                                        })
                                        .collect(Collectors.toList());
                            }

                            for (SafetyEquipmentVO eq : toDelete) {
                                permitToWorkDao.deletePermitToWorkEq(eq);
                            }

                            // 2. safetyEqList에만 있는 항목 추가
                            List<SafetyEquipmentVO> toInsert;
                            if (eqList.isEmpty() || ParamEqList.isEmpty()) {
                                toInsert = ParamEqList;
                            } else {
                                toInsert = ParamEqList.stream()
                                        .filter(newEq -> eqList.stream().noneMatch(eq -> eq.getSafetyEqItemId().equals(newEq.getSafetyEqItemId())))
                                        .collect(Collectors.toList());
                            }

                            for (SafetyEquipmentVO eq : toInsert) {
                                eq.setWriteYear(item.getWriteYear());
                                eq.setDocType(item.getDocType());
                                eq.setDocNo(item.getDocNo());
                                eq.setDocSeq(item.getDocSeq());
                                eq.setCreatedAt(item.getCreatedAt());
                                eq.setCreatedBy(item.getCreatedBy());
                                permitToWorkDao.insertPermitToWorkEq(eq);
                            }
                        }
                    }
                    //#endregion
                }
            }
            //#region 작업자 매핑
            SpSearchVO deleteVO = new SpSearchVO();
            deleteVO.setWriteYear(vo.getWriteYear());
            deleteVO.setDocType(vo.getDocType());
            deleteVO.setDocNo(vo.getDocNo());
            permitToWorkDao.deletePermitToWorkHr(deleteVO);
            if (vo.getHrList() != null && !vo.getHrList().isEmpty()) {
                for (HrVO hr : vo.getHrList()) {
                    hr.setWriteYear(vo.getWriteYear());
                    hr.setDocType(vo.getDocType());
                    hr.setDocNo(vo.getDocNo());
                    hr.setCreatedBy(vo.getCreatedBy());
                    permitToWorkDao.insertPermitToWorkHr(hr);
                }
            }

            //인원 매핑 조회를 위한 파라미터 생성
//            SpSearchVO searchVo = new SpSearchVO();
//            searchVo.setWriteYear(vo.getWriteYear());
//            searchVo.setDocType(vo.getDocType());
//            searchVo.setDocNo(vo.getDocNo());
//
//            List<HrVO> hrList =  permitToWorkDao.getPermitToWorkHr(searchVo);
//            List<HrVO> ParamHrList = vo.getHrList();
//
//            // 1. hrList에만 있는 항목 삭제
//            List<HrVO> toDelete = hrList.stream()
//                    .filter(hr -> ParamHrList.stream().noneMatch(newHr -> newHr.getHrId().equals(hr.getHrId())))
//                    .collect(Collectors.toList());
//
//            for (HrVO hr : toDelete) {
//                permitToWorkDao.deletePermitToWorkHr(hr);
//            }
//
//            // 2. hrListH에만 있는 항목 추가
//            List<HrVO> toInsert = ParamHrList.stream()
//                    .filter(newHr -> hrList.stream().noneMatch(hr -> hr.getHrId().equals(newHr.getHrId())))
//                    .collect(Collectors.toList());
//
//            for (HrVO hr : toInsert) {
//                hr.setWriteYear(vo.getWriteYear());
//                hr.setDocType(vo.getDocType());
//                hr.setDocNo(vo.getDocNo());
//                hr.setCreatedAt(vo.getCreatedAt());
//                hr.setCreatedBy(vo.getCreatedBy());
//                permitToWorkDao.insertPermitToWorkHr(hr);
//            }
            //#endregion
        }

        return vo;
    }

    // 안전작업 허가서 상세 파일 저장
    @Transactional(rollbackFor = Throwable.class)
    public void savePermitToWorkFiles(List<MultipartFile> files, PermitToWorkVO vo) throws Exception {
        if (!vo.getDeleteFiles().isEmpty()) {
            fileService.deleteFile(vo.getDeleteFiles(), "PTW", vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
        }
        fileService.saveFile(files, vo.getDocType(), vo.getWriteYear() + vo.getDocNo(), SecurityUtil.getCurrentCompId());
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deletePermitToWorkList(List<PermitToWorkVO> list) throws Exception {
        for (PermitToWorkVO tmp : list) {
            permitToWorkDao.deletePermitToWorkList(tmp);
        }

        fileService.deleteFile(list.get(0).getDeleteFiles(), "PTW", list.get(0).getWriteYear() + list.get(0).getDocNo(), SecurityUtil.getCurrentCompId());
    }

    //region ===== 점검사항 초기화 ====
    @Transactional(rollbackFor = Throwable.class)
    public void deletePermitToWork(PermitToWorkVO vo) throws Exception {
        //종합의견 -> null
        permitToWorkDao.deletePermitToWork(vo);
        //양호, 불량, 허용불가 -> N
        permitToWorkDao.initPermitToWorkDetail(vo);
        //필요안전기구 삭제
        permitToWorkDao.deletePermitToWorkEqs(vo);
    }
    //endregion


    public List<JasperPrint> getPermitToWorkReport(HttpServletRequest request, HttpServletResponse response,
                                                   SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수
        String compNm = utilsService.getCompNm(SecurityUtil.getCurrentClntId(), SecurityUtil.getCurrentCompId());

        PermitToWorkVO vo = new PermitToWorkVO();
        String fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전작업 허가서";
        if (spSearchVO.getCheckedList() != null && spSearchVO.getCheckedList().size() == 1) {
            //안전작업 허가서 내용 조회(상세 출력이거나 1개일 경우)
            spSearchVO.setDocNo(spSearchVO.getCheckedList().get(0));
            vo = getPermitToWorkDetail(spSearchVO);
            fileNm = "(" + spSearchVO.getWriteYear() + ")" + "안전작업 허가서_" + vo.getWorkplace() + "_" + vo.getWorkDt();
        }

        String title = "안전작업 허가서";

        InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        // 표지 리포트
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();

        for (String docNo : spSearchVO.getCheckedList()) {
            InputStream logo2 = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());

            //리스트 개별 조회
            if (spSearchVO.getCheckedList() != null && spSearchVO.getCheckedList().size() != 1) {
                spSearchVO.setDocNo(docNo);
                vo = getPermitToWorkDetail(spSearchVO);
            }

            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath("report/impl/permitToWork/permitToWork.jrxml");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("logo", logo2);
            params.put("title", title);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            //안전작업 허가서
            String dt = SpUtils.formatDate2(vo.getWorkDt());
            String period = dt + " " + SpUtils.formatTime1(vo.getWorkStart()) + " ~ " + dt + " " + SpUtils.formatTime1(vo.getWorkEnd());
            params.put("workDt", period);
            params.put("workplace", vo.getWorkplace());
            params.put("equipmentNm", vo.getEquipmentNm());
            params.put("workContent", vo.getWorkContent());
            params.put("overallOpinion", vo.getOverallOpinion());
            params.put("overallOpinionTitle", " " + (vo.getDetailList().size() + 1) + "." + "종합 의견(전문가 기술)");    // 작업자 파트 : 외 -명
            params.put("workerCnt", vo.getHrList().size() - 1);    // 작업자 파트 : 외 -명

            //작업 종류
            StringJoiner typeJoiner = new StringJoiner(",");
            for (PermitToWorkTypeVO wt : vo.getWorkTypeList()) {
                String check = wt.getCheckYn().getKey().equals("Yes") ? " V " : "   ";
                String result = " " + wt.getWorkTypeNm() + "(" + check + ")";
                if (Objects.equals(wt.getWorkTypeNm(), "기타")) {
                    String additional = wt.getAdditionalInfo() == null ? "" : wt.getAdditionalInfo();
                    result = " " + wt.getWorkTypeNm() + "( " + additional + " )";
                }
                typeJoiner.add(result);
            }
            params.put("workTypeList", typeJoiner.toString());

            //서명(조회해야됨->작업자 서명, 현장 감독관 서명, 수급업체 서명, 승인권자 서명)
            Map<String, String> signMap = Map.of(
                    "worker", "Worker",
                    "siteSupervisor", "SiteSupervisor",
                    "subcontractorRep", "SubcontractorRep",
                    "workApprover", "WorkApprover"
            );

            // 서명 파라미터 세팅
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(spSearchVO.getDocType());
            signParam.setTargetId(spSearchVO.getWriteYear() + spSearchVO.getDocNo());

            // 서명 데이터 조회 및 처리
            for (var entry : signMap.entrySet()) {
                signParam.setType(entry.getKey());
                for (UtilsVO info : utilsService.getApprovalInfo(signParam)) {
                    params.put(entry.getKey() + "Nm", info.getHrNm());
                    params.put("sign" + entry.getValue(), utilsService.getSignatureFromBase64String(info.getSignature()));
                }
            }


            // 점검항목
            List<Map<String, Object>> datasource = new ArrayList<>();
            //점검 사항이 없을때, 빈값 생성
            if (vo.getDetailList().isEmpty()) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("inspectionItem", "");
                datasource.add(data);
            }
            for (int i = 0; i < vo.getDetailList().size(); i++) {
                PermitToWorkDetailVO detail = vo.getDetailList().get(i);
                if (detail != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("inspectionItem", " " + (i + 1) + "." + detail.getInspectionItemNm());
                    data.put("acceptableYn", detail.getAcceptableYn());
                    data.put("nonCompliantYn", detail.getNonCompliantYn());
                    data.put("unacceptableYn", detail.getUnacceptableYn());

                    StringJoiner joiner = new StringJoiner(",");
                    if (detail.getSafetyEqList() != null && !detail.getSafetyEqList().isEmpty()) {
                        for (int j = 0; j < detail.getSafetyEqList().size(); j++) {
                            SafetyEquipmentVO eq = detail.getSafetyEqList().get(j);
                            joiner.add(" " + eq.getSafetyEqItemNm());
                        }
                    }
                    data.put("equipList", joiner.toString());
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

        // 통합 출력인 경우 reportList만 반환하고 종료
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    //#region 점검사항 관리 팝업

    public List<SafetyWorkChecklistVO> getSafetyWorkChecklist(SpSearchVO searchVo) throws Exception {
        List<SafetyWorkChecklistVO> voList = permitToWorkDao.getSafetyWorkChecklist(searchVo);
        for (SafetyWorkChecklistVO vo : voList) {
            List<SafetyWorkChecklistVO> itemList = permitToWorkDao.getSafetyWorkChecklistDetail(vo);
            vo.setItemList(itemList);
        }
        return voList;
    }

    public List<SafetyWorkChecklistVO> getSafetyWorkChecklistDetail(SafetyWorkChecklistVO vo) throws Exception {
        List<SafetyWorkChecklistVO> voList = permitToWorkDao.getSafetyWorkChecklistDetail(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveSafetyWorkChecklist(SafetyWorkChecklistVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 점검사항 추가
            permitToWorkDao.insertSafetyWorkChecklist(vo);

            // 점검사항 항목 추가
            for (SafetyWorkChecklistVO detail : vo.getItemList()) {
                detail.setInspectionId(vo.getInspectionId());
                permitToWorkDao.insertSafetyWorkChecklistDetail(detail);
            }
        } else {
            // 점검사항 수정
            SafetyWorkChecklistVO resultVO = permitToWorkDao.getSafetyWorkChecklistById(vo);
            if (resultVO == null)
                return null;
            resultVO = (SafetyWorkChecklistVO) SpUtils.objectMap(vo, resultVO);

            permitToWorkDao.updateSafetyWorkChecklist(resultVO);

            // 점검사항 항목 추가/수정
            for (SafetyWorkChecklistVO detail : vo.getItemList()) {
                if (detail.getCmd().equals("I")) {
                    // 점검사항 추가
                    permitToWorkDao.insertSafetyWorkChecklistDetail(detail);
                } else {
                    // 점검사항 수정
                    SafetyWorkChecklistVO resultVO2 = permitToWorkDao.getSafetyWorkChecklistDetailById(detail);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (SafetyWorkChecklistVO) SpUtils.objectMap(detail, resultVO2);

                    permitToWorkDao.updateSafetyWorkChecklistDetail(resultVO2);
                }
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteSafetyWorkChecklistDetail(List<SafetyWorkChecklistVO> voList) throws Exception {
        for (SafetyWorkChecklistVO vo : voList) {
            permitToWorkDao.deleteSafetyWorkChecklistDetail(vo);
        }
    }
    //#endregion

    //#region 안전기구 관리 팝업

    public List<SafetyEquipmentVO> getSafetyEquipment(SpSearchVO searchVo) throws Exception {
        List<SafetyEquipmentVO> voList = permitToWorkDao.getSafetyEquipment(searchVo);
        for (SafetyEquipmentVO vo : voList) {
            List<SafetyEquipmentVO> itemList = permitToWorkDao.getSafetyEquipmentDetail(vo);
            vo.setItemList(itemList);
        }
        return voList;
    }

    public List<SafetyEquipmentVO> getSafetyEquipmentDetail(SafetyEquipmentVO vo) throws Exception {
        List<SafetyEquipmentVO> voList = permitToWorkDao.getSafetyEquipmentDetail(vo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveSafetyEquipment(SafetyEquipmentVO vo) throws Exception {
        if (vo.getCmd().equals("I")) {
            // 점검사항 추가
            permitToWorkDao.insertSafetyEquipment(vo);

            // 점검사항 항목 추가
            for (SafetyEquipmentVO detail : vo.getItemList()) {
                detail.setSafetyEqId(vo.getSafetyEqId());
                permitToWorkDao.insertSafetyEquipmentDetail(detail);
            }
        } else {
            // 점검사항 수정
            SafetyEquipmentVO resultVO = permitToWorkDao.getSafetyEquipmentById(vo);
            if (resultVO == null)
                return null;
            resultVO = (SafetyEquipmentVO) SpUtils.objectMap(vo, resultVO);

            permitToWorkDao.updateSafetyEquipment(resultVO);

            // 점검사항 항목 추가/수정
            for (SafetyEquipmentVO detail : vo.getItemList()) {
                if (detail.getCmd().equals("I")) {
                    // 점검사항 추가
                    permitToWorkDao.insertSafetyEquipmentDetail(detail);
                } else {
                    // 점검사항 수정
                    SafetyEquipmentVO resultVO2 = permitToWorkDao.getSafetyEquipmentDetailById(detail);
                    if (resultVO2 == null)
                        return null;
                    resultVO2 = (SafetyEquipmentVO) SpUtils.objectMap(detail, resultVO2);

                    permitToWorkDao.updateSafetyEquipmentDetail(resultVO2);
                }
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteSafetyEquipmentDetail(List<SafetyEquipmentVO> voList) throws Exception {
        for (SafetyEquipmentVO vo : voList) {
            permitToWorkDao.deleteSafetyEquipmentDetail(vo);
        }
    }
    //#endregion
}
