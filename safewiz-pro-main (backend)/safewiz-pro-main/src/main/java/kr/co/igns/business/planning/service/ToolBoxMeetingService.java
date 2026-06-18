package kr.co.igns.business.planning.service;

import java.io.InputStream;
import java.util.*;

import kr.co.igns.business.planning.model.ToolBoxMeetingSettingVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.igns.business.planning.dao.postgres.ToolBoxMeetingDAO;
import kr.co.igns.business.planning.model.ToolBoxMeetingVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class ToolBoxMeetingService {
    private final ToolBoxMeetingDAO dao;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final UtilsDAO utilsDao;
    private final FileService fileService;

    public List<ToolBoxMeetingVO> searchData(ToolBoxMeetingVO vo) {
        List<ToolBoxMeetingVO> result = dao.searchData(vo);
        return result;
    }

    public List<ToolBoxMeetingVO> searchDataDetail(ToolBoxMeetingVO vo) {
        List<ToolBoxMeetingVO> result = dao.searchDataDetail(vo);
        
        for (ToolBoxMeetingVO item : result) {
            item.setFiles(fileService.getFileList(vo.getWriteYear() + vo.getDocNo(), "TBM")); // 모든 항목에 동일한 파일 리스트 세팅
        }
        return result;
    }



    public List<ToolBoxMeetingSettingVO> searchSetting(ToolBoxMeetingSettingVO vo) {
        List<ToolBoxMeetingSettingVO> result = dao.searchSetting(vo);

        if (result.isEmpty()) {
            dao.insertSetting(vo);
            result = dao.searchSetting(vo);
        }

        return result;
    }

    public List<ToolBoxMeetingSettingVO> saveSetting(List<ToolBoxMeetingSettingVO> voList) {
        for (ToolBoxMeetingSettingVO vo : voList) {
             if ("I".equals(vo.getCmd())) {
                 dao.insertSetting(vo);
             } else {
                 dao.updateSetting(vo);
             }
        }
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ToolBoxMeetingVO saveData(HashMap<String, Object> param, List<MultipartFile> files) throws Exception {
        ObjectMapper objMap = new ObjectMapper();
        ToolBoxMeetingVO main = objMap.convertValue(param.get("main"), ToolBoxMeetingVO.class);
        List<ToolBoxMeetingVO> potentialRisk = Arrays.asList(objMap.convertValue(param.get("potentialRisk"), ToolBoxMeetingVO[].class));
        List<ToolBoxMeetingVO> keyRisk = Arrays.asList(objMap.convertValue(param.get("keyRisk"), ToolBoxMeetingVO[].class));
        List<ToolBoxMeetingVO> safeCheck = Arrays.asList(objMap.convertValue(param.get("safeCheck"), ToolBoxMeetingVO[].class));
        List<ToolBoxMeetingVO> selectRiskAssessment = Arrays.asList(objMap.convertValue(param.get("selectRiskAssessment"), ToolBoxMeetingVO[].class));
        List<ToolBoxMeetingVO> selectProcess = Arrays.asList(objMap.convertValue(param.get("selectProcess"), ToolBoxMeetingVO[].class));

        main.setUserId(SecurityUtil.getCurrentMemberId());

        if (main.getCmd().equals("I")) {
            dao.insertMain(main);
            fileService.saveFile(files, main.getDocType(), main.getWriteYear() + main.getDocNo(), SecurityUtil.getCurrentCompId());
        } else {
            dao.updateMain(main);
		    fileService.deleteFile(main.getDeleteFiles(), "TBM", main.getWriteYear() + main.getDocNo(), SecurityUtil.getCurrentCompId());
            fileService.saveFile(files, main.getDocType(), main.getWriteYear() + main.getDocNo(), SecurityUtil.getCurrentCompId());
        }

        if(main.getRiskYn().equals("Y")){   //위험성 평가 실시 여부 Y일 경우
                List<ToolBoxMeetingVO> searchList = dao.searchSelectedDatas(main);  //조회 시 데이터가 존재하지 않을 경우 insert
                if(searchList.size() == 0){
                    for(ToolBoxMeetingVO item : selectRiskAssessment){
                        item.setWriteYear(main.getWriteYear());
                        item.setDocType(main.getDocType());
                        item.setDocNo(main.getDocNo());
                        item.setRiskWriteYear(selectRiskAssessment.get(0).getWriteYear());
                        item.setDocType(selectRiskAssessment.get(0).getDocType());
                        item.setDocNo(selectRiskAssessment.get(0).getDocNo());
                        item.setCreatedBy(SecurityUtil.getCurrentMemberId());

                        for(ToolBoxMeetingVO item2 : selectProcess){
                            item.setProcessId(item2.getProcessId());

                            dao.insertSelectRiskAssessment(item);
                        }
                    }
                }else{//조회 시 데이터가 존재할 경우 delete 후 insert
                    dao.deleteSelectRiskAssessment(main);
                    for(ToolBoxMeetingVO item : selectRiskAssessment){
                        item.setWriteYear(main.getWriteYear());
                        item.setDocType(main.getDocType());
                        item.setDocNo(main.getDocNo());
                        item.setRiskWriteYear(selectRiskAssessment.get(0).getWriteYear());
                        item.setDocType(selectRiskAssessment.get(0).getDocType());
                        item.setDocNo(selectRiskAssessment.get(0).getDocNo());
                        item.setCreatedBy(SecurityUtil.getCurrentMemberId());

                        for(ToolBoxMeetingVO item2 : selectProcess){
                            item.setProcessId(item2.getProcessId());
                            dao.insertSelectRiskAssessment(item);
                        }
                    }
                }
        }else{
            dao.deleteSelectRiskAssessment(main);
        }

        // 작업 전 안전조치 확인 저장
        for (ToolBoxMeetingVO item : safeCheck) {
            item.setWriteYear(main.getWriteYear());
            item.setDocType(main.getDocType());
            item.setDocNo(main.getDocNo());
            item.setUserId(main.getUserId());

            if (item.getCmd().equals("D")) {
                dao.deleteSafeCheck(item);
            } else if (item.getCmd().equals("I")) {
                dao.insertSafeCheck(item);
            } else {
                dao.updateSafeCheck(item);
            }
        }

        // 중점위험요인 저장
        for (ToolBoxMeetingVO item : keyRisk) {
            item.setWriteYear(main.getWriteYear());
            item.setDocType(main.getDocType());
            item.setDocNo(main.getDocNo());
            item.setUserId(main.getUserId());

            if (item.getCmd().equals("D")) {
                dao.deleteKeyRisk(item);
            } else if (item.getCmd().equals("I")) {
                dao.insertKeyRisk(item);
            } else {
                dao.updateKeyRisk(item);
            }

            ToolBoxMeetingVO keyRiskCountParam = new ToolBoxMeetingVO();
            keyRiskCountParam.setWriteYear(main.getWriteYear());
            keyRiskCountParam.setDocType(main.getDocType());
            keyRiskCountParam.setDocNo(main.getDocNo());
            keyRiskCountParam.setPotentialRiskSeq(item.getPotentialRiskSeq());
            int keyCnt = dao.searchKeyRiskCount(keyRiskCountParam).get(0).getCnt();
            if (keyCnt > 1) {
                throw new Exception("중복되는 중점위험요인이 있습니다.");
            }
        }

        // 잠재위험요인 저장
        for (ToolBoxMeetingVO item : potentialRisk) {
            item.setWriteYear(main.getWriteYear());
            item.setDocType(main.getDocType());
            item.setDocNo(main.getDocNo());
            item.setUserId(main.getUserId());

            if (item.getCmd().equals("D")) {
                dao.deletePotentialRisk(item);

                // 작업 전 안전조치 확인 자동 삭제
                ToolBoxMeetingVO scpVO = new ToolBoxMeetingVO();
                scpVO.setWriteYear(item.getWriteYear());
                scpVO.setDocType(item.getDocType());
                scpVO.setDocNo(item.getDocNo());
                scpVO.setPotentialRiskSeq(item.getDocSeq());

                dao.deleteSafeCheckPotential(scpVO);

                // 중점위험요인 자동 삭제
                ToolBoxMeetingVO krVO = new ToolBoxMeetingVO();
                krVO.setWriteYear(item.getWriteYear());
                krVO.setDocType(item.getDocType());
                krVO.setDocNo(item.getDocNo());
                krVO.setPotentialRiskSeq(item.getDocSeq());

                dao.deleteKeyRiskPotential(krVO);
            } else if (item.getCmd().equals("I")) {
                dao.insertPotentialRisk(item);

                // 작업 전 안전조치 확인 자동 저장
                ToolBoxMeetingVO scVO = new ToolBoxMeetingVO();
                scVO.setWriteYear(item.getWriteYear());
                scVO.setDocType(item.getDocType());
                scVO.setDocNo(item.getDocNo());
                scVO.setPotentialRiskSeq(item.getDocSeq());
                scVO.setActionYn("N");
                scVO.setUserId(item.getUserId());

                dao.insertSafeCheck(scVO);
            } else {
                dao.updatePotentialRisk(item);
            }
        }



        return main;
    }

    public void deleteData(List<ToolBoxMeetingVO> vo) throws Exception{
        for (ToolBoxMeetingVO item : vo) {
            dao.deleteMain(item);
            fileService.deleteFile(item.getDeleteFiles(), "TBM", item.getWriteYear() + item.getDocNo(), SecurityUtil.getCurrentCompId());
        }
        
    }

    public List<ToolBoxMeetingVO> searchPotenialRisk(ToolBoxMeetingVO vo) {
        List<ToolBoxMeetingVO> result = dao.searchPotenialRisk(vo);
        return result;
    }

    public List<ToolBoxMeetingVO> searchKeyRisk(ToolBoxMeetingVO vo) {
        List<ToolBoxMeetingVO> result = dao.searchKeyRisk(vo);
        return result;
    }

    public List<ToolBoxMeetingVO> searchSafeCheck(ToolBoxMeetingVO vo) {
        List<ToolBoxMeetingVO> result = dao.searchSafeCheck(vo);
        return result;
    }
    public List<ToolBoxMeetingVO> searchSelectedDatas(ToolBoxMeetingVO vo) {

        List<ToolBoxMeetingVO> result = dao.searchSelectedDatas(vo);
        return result;
    }

    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, List<SpSearchVO> spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int localPage = 2;
        int page = spSearchVO.get(0).getPage();
        for (SpSearchVO vo : spSearchVO) {
            List<JasperPrint> jaspers = new ArrayList<>();
            vo.setCheckedObjList(dao.getAllReport(vo));
            vo.setPrintAll(true);
            String month = vo.getSearchText();
            if(Integer.parseInt(vo.getSearchText())<10) {
                month = String.valueOf(Integer.valueOf(month));
            }
            vo.setExtra1(month+ "월");
            vo.setPage(page);
            vo.setLocalPage(localPage);
            jaspers = getReport(request, response, vo);
            reportList.addAll(jaspers);
            vo.setSubPage(vo.getSubPage());
            for (JasperPrint report : jaspers) {
                page += report.getPages().size();
                localPage += report.getPages().size();
            }
        }
        return reportList;
    }

    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        SpSearchVO spVo = new SpSearchVO();
        String title = "TBM";
        if(spSearchVO.isPrintAll()) {
            title = spSearchVO.getExtra1() + " TBM";
        }
        int page = 1;
        int subPage = 0;
        int localPage = 1;
        if (spSearchVO.getPage() > 0) {
            spVo.setPage(spSearchVO.getPage());
            page = spSearchVO.getPage() + 1;
        }
        if (spSearchVO.getSubPage() > 0) {
            spVo.setSubPage(spSearchVO.getSubPage());
            subPage = spSearchVO.getSubPage();
        }
        if (spSearchVO.getLocalPage() > 0) {
            spVo.setLocalPage(spSearchVO.getLocalPage());
            localPage = spSearchVO.getLocalPage()+1;
        }
        JasperPrint JasperFrontReport = utilsService.getFrontReport(spVo, title);
        reportList.add(JasperFrontReport);

        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String fileNm = "";
        fileNm += "(" + spSearchVO.getCheckedObjList().get(0).getWriteYear() + ")";
        fileNm += "TBM";
//		if (vo.getCheckedObjList().size() == 1) {
//			fileNm += "_"+vo.getTbmDate();
//		}
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            /***********************/
            /****** 공통 정보 조회 ******/
            /***********************/

            // 고객사 ID 조회
            vo.setClntId(SecurityUtil.getCurrentClntId());

            // 고객사 명칭 조회
            ClientVO cVo = clientDao.getClientById(vo.getClntId());
            String clntNm = cVo.getClntNm();

            // 사업장 ID 조회
            vo.setCompId(SecurityUtil.getCurrentCompId());

            // 사업장 명칭 조회
            CompVO cpVO = new CompVO();
            cpVO.setClntId(vo.getClntId());
            cpVO.setCompId(vo.getCompId());
            CompVO cPo = compDao.getCompById(cpVO);
            String compNm = cPo.getCompNm();
            
            // 서명 일시 표시 여부 조회
            ToolBoxMeetingSettingVO reqVO = new ToolBoxMeetingSettingVO();
            reqVO.setSettingKey("showSignTime");
            reqVO.setCompId(vo.getCompId());
            List<ToolBoxMeetingSettingVO> settingVOList = searchSetting(reqVO);
            boolean showSignFlag = !settingVOList.isEmpty() && "Y".equals(settingVOList.get(0).getSettingValue());

            /*************************************/
            /****** Jasper Export File Setup *****/
            /*************************************/
            ReportVO reportVO = new ReportVO();

            // 출력 파일 명 설정
//				reportVO.setFileName(fileNm);

            // 출력 생성용 Jasper 파일 위치
            reportVO.setJrxmlPath("report/planning/toolBoxMeeting/toolBoxMeeting.jrxml");

            // 출력  파일 형식 지정
//				reportVO.setType("pdf");

            /**********************************/
            /****** 출력물 공통 Parameter 입력 *****/
            /**********************************/
            Map<String, Object> params = new HashMap<String, Object>();

            //고객사 명 입력
            params.put("clntNm", clntNm);
            //사업장 명 입력
            params.put("compNm", compNm);
            params.put("showSignFlag", showSignFlag);

            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);

            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(vo.getClntId());
            params.put("logo", logo);

            /*************************************/
            /****** 출력물 Custom Parameter 입력 *****/
            /*************************************/
            ToolBoxMeetingVO mainData = dao.searchReport(vo);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm += "_" + mainData.getTbmDate();
            }
            params.put("tbmDate", mainData.getTbmDate());
            params.put("sameDateYn", mainData.getSameDateYn());
            params.put("workNm", mainData.getWorkNm());
            params.put("workDesc", mainData.getWorkDesc());
            params.put("workPlace", mainData.getWorkPlace());
            params.put("riskYn", mainData.getRiskYn());
            params.put("checkResult", mainData.getCheckResult());
            params.put("workEndMeeting", mainData.getWorkEndMeeting());

            List<ToolBoxMeetingVO> potentialRisk = dao.searchReportPotentialRisk(vo);
            if (potentialRisk.isEmpty()) {
                potentialRisk.add(new ToolBoxMeetingVO());
            }
            params.put("potentialRisk", new JRBeanCollectionDataSource(potentialRisk));

            List<ToolBoxMeetingVO> keyRisk = dao.searchReportKeyRisk(vo);
            if (keyRisk.isEmpty()) {
                keyRisk.add(new ToolBoxMeetingVO());
            }
            params.put("keyRisk", new JRBeanCollectionDataSource(keyRisk));

            // 서명 조회
            Map<String, String> signTypes = Map.of("leader", "leaderSign","attend", "attendSign");
            
            UtilsVO signParam = new UtilsVO();
            signParam.setTargetType(vo.getDocType());
            signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());
            
            List<ToolBoxMeetingVO> attendSignList = new ArrayList<>();

            int z = 0;
            for (var signType : signTypes.entrySet()) {
            	signParam.setType(signType.getKey());

                List<UtilsVO> signList = utilsDao.getApprovalInfo(signParam);

                for (UtilsVO signInfo : signList) {
                    String type = signType.getKey();

                    if (type.equals("leader")) {
                        params.put(signType.getValue(), utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                        params.put("orgnNm", signInfo.getOrgnNm());
                        params.put("jbrpNm", signInfo.getJbrpNm());
                        params.put("hrNm", signInfo.getHrNm());
                    } else if (type.equals("attend")) {
                        z++;
                        if (showSignFlag) { // 서명 일시 표시 될 경우 2열로
                            if (z % 2 == 1) {
                                ToolBoxMeetingVO tempVo = new ToolBoxMeetingVO();
                                tempVo.setAttend1(signInfo.getHrNm());
                                tempVo.setAttendSign1(utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                                tempVo.setAttendSignTime1(signInfo.getSignTime() );
                                attendSignList.add(tempVo);
                            } else {
                                ToolBoxMeetingVO lastVo = attendSignList.get(attendSignList.size() - 1);
                                lastVo.setAttend2(signInfo.getHrNm());
                                lastVo.setAttendSign2(utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                                lastVo.setAttendSignTime2(signInfo.getSignTime());
                            }
                        } else { // 서명 일시 표시 안될 경우에 3열로
                            if (z % 3 == 1) {
                                ToolBoxMeetingVO tempVo = new ToolBoxMeetingVO();
                                tempVo.setAttend1(signInfo.getHrNm());
                                tempVo.setAttendSign1(utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                                attendSignList.add(tempVo);
                            } else {
                                ToolBoxMeetingVO lastVo = attendSignList.get(attendSignList.size() - 1);
                                if (z % 3 == 2) {
                                    lastVo.setAttend2(signInfo.getHrNm());
                                    lastVo.setAttendSign2(utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                                } else {
                                    lastVo.setAttend3(signInfo.getHrNm());
                                    lastVo.setAttendSign3(utilsService.getSignatureFromBase64String(signInfo.getSignature()));
                                }
                            }
                        }
                    }
                }
            }

            if (attendSignList.size() < 1) {
                attendSignList.add(new ToolBoxMeetingVO());
            }
            params.put("attendSign", new JRBeanCollectionDataSource(attendSignList));

            List<ToolBoxMeetingVO> safeCheck = dao.searchReportSafeCheck(vo);
            if (safeCheck.isEmpty()) {
                safeCheck.add(new ToolBoxMeetingVO());
            }
            params.put("safeCheck", new JRBeanCollectionDataSource(safeCheck));

            /**********************/
            /****** 출력물 출력 *******/
            /**********************/
            reportVO.setParameter(params);

            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = JasperReport.getPages().size() + page;
            localPage += JasperReport.getPages().size();
        }
        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }
}
