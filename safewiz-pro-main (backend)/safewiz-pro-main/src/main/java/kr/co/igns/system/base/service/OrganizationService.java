package kr.co.igns.system.base.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import kr.co.igns.business.impl.model.EmergencyControlOrganChartVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.dao.postgres.OrganizationDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.OrgChartVO;
import kr.co.igns.system.base.model.OrganizationVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import kr.co.igns.system.setting.dao.FileTemplateRepository;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationDAO organizationDao;
    private final HrDAO hrDao;
    private final SystemCodeDAO systemCodeDao;
    private final UtilsService utilsService;
    private final ReportService reportService;
    private final FileTemplateRepository fileTemplateRepository;
    private final FileProperty property;
    private final PartnerService partnerService;

    public List<OrganizationVO> getOrganization(SpSearchVO searchVo) throws Exception {
        List<OrganizationVO> voList = organizationDao.getOrganizationList(searchVo);
        return voList;
    }

    public OrganizationVO getOrganizationDetail(SpSearchVO searchVo) throws Exception {
        OrganizationVO voList = organizationDao.getOrganizationDetail(searchVo);
        return voList;
    }

    public List<OrgChartVO> getOrganizationChart(SpSearchVO searchVo) throws Exception {
        List<OrgChartVO> voList = organizationDao.getChartList(searchVo);
        return voList;
    }

    public OrgChartVO getOrganizationChartDetail(String chartId) throws Exception {
        OrgChartVO voList = organizationDao.getChartDetail(chartId);
        return voList;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertOrganization(OrganizationVO reqVo) throws Exception {
        //조직 생성
        //조직ID 자동 부여 (년월일순번)

        organizationDao.insertOrganization(reqVo);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertOrganizationChart(OrgChartVO reqVo) throws Exception {
        //조직 생성
        //조직ID 자동 부여 (년월일순번)
        organizationDao.insertOrganizationChart(reqVo);

        return reqVo;
    }
    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateOrganizationChart(OrgChartVO reqVo) throws Exception {
        //조직 수정
        OrgChartVO  vo = organizationDao.getChartDetail(reqVo.getChartId());
        if (vo == null)
            return null;
        vo = (OrgChartVO) SpUtils.objectMap(reqVo, vo);

        organizationDao.updateOrganizationChart(vo);
        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO confirmOrganizationChart(OrgChartVO reqVo) throws Exception {
        organizationDao.confirmOrganizationChart(reqVo);
        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateOrganization(OrganizationVO reqVo) throws Exception {
        //조직 수정
        OrganizationVO vo = organizationDao.getOrgnById(reqVo.getOrgnId());
        if (vo == null)
            return null;
        vo = (OrganizationVO) SpUtils.objectMap(reqVo, vo);

        organizationDao.updateOrganization(vo);

        //인원 매핑 - 조직장,부장 수정(target_id = orgn_id, hr_id = id)
        BaseMapVO searchVo = new BaseMapVO("orgn",reqVo.getOrgnId());
        List<BaseMapVO> hrInfo = hrDao.getHrMap(searchVo);
        String targetType = "orgn";

        // 기존 데이터 업데이트
        if(!hrInfo.isEmpty()){
            hrInfo.forEach(hrMap -> {
                if ("HEAD".equals(hrMap.getGubun())) {
                    hrMap.setId(reqVo.getHeadHrId());
                    hrMap.setUpdatedBy(reqVo.getUpdatedBy());
                    hrDao.updateHrMap(hrMap);
                } else if ("2ND".equals(hrMap.getGubun())) {
                    hrMap.setId(reqVo.getSecondHrId());
                    hrMap.setUpdatedBy(reqVo.getUpdatedBy());
                    hrDao.updateHrMap(hrMap);
                }
            });
        }

        // HEAD가 없으면 추가
        if (hrInfo.stream().noneMatch(hr -> "HEAD".equals(hr.getGubun())) && reqVo.getHeadHrId() != null && !reqVo.getHeadHrId().equals("")) {
            BaseMapVO headHrInfo = new BaseMapVO(targetType, reqVo.getOrgnId(), reqVo.getHeadHrId(), "HEAD", reqVo.getCreatedBy());
            hrDao.addHrMap(headHrInfo);
        }

        // 2ND가 없으면 추가
        if (hrInfo.stream().noneMatch(hr -> "2ND".equals(hr.getGubun())) && reqVo.getSecondHrId() != null && !reqVo.getSecondHrId().equals("")) {
            BaseMapVO secondHrInfo = new BaseMapVO(targetType, reqVo.getOrgnId(), reqVo.getSecondHrId(), "2ND", reqVo.getCreatedBy());
            hrDao.addHrMap(secondHrInfo);
        }

        return vo;
    }
    public void deleteOrganizationChart(List<OrgChartVO> list) throws Exception {
        for (OrgChartVO tmp : list) {
            organizationDao.deleteOrganizationChart(tmp);
        }
    }

    public BaseVO deleteOrganization(OrganizationVO reqVo) throws Exception {
        BaseVO vo = organizationDao.getOrgnById(reqVo.getOrgnId());
        organizationDao.deleteOrganization(reqVo);
        return vo;
    }

    public void deleteOrganizations(List<OrganizationVO> list) throws Exception {
        for (OrganizationVO tmp : list) {
            organizationDao.deleteOrganization(tmp);
        }
    }

    public List<JasperPrint> getOrgChartReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>(); // 일괄출력용 변수

        String fileNm = "조직도 구성도";
        String title = "조직 구성도";

        // 레포트 출력 타입이 새로형인지 가로형인지
        // 표지 리포트
        String typeMinorCd = "HOR";
        SystemCodeVO systemCodeReqVo = new SystemCodeVO();
        systemCodeReqVo.setMajorCd("OT01");
        List<SystemMinorCodeVO> systemCodeList = systemCodeDao.findDetail(systemCodeReqVo);
        SystemMinorCodeVO selectedType = systemCodeList.stream()
                .filter(el -> "Y".equals(el.getExtra1()))
                .findFirst()
                .orElse(null); // 혹은 기본값을 설정 가능
        if (selectedType != null) {
            typeMinorCd = selectedType.getMinorCd();
        }
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        JasperPrint JasperFrontReport = utilsService.getFrontReport(selectedType.getExtra2(), spSearchVO, title);
        reportList.add(JasperFrontReport);
        page = page + JasperFrontReport.getPages().size();
        localPage = localPage + JasperFrontReport.getPages().size();

        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            OrgChartVO chartData  = getOrganizationChartDetail(vo.getSearchText());
            if (spSearchVO.getCheckedObjList().size() == 1) {
                fileNm = "조직구성도" + "_" + chartData.getChartNm();
            }
            String jrxmlPath = "report/base/orgChart/";
            if ("HOR".equals(typeMinorCd)) {
                // 가로형
                jrxmlPath = jrxmlPath + "chart_hor.jrxml";
            } else if ("VER".equals(typeMinorCd)) {
                // 세로형
                jrxmlPath = jrxmlPath + "chart_VER.jrxml";
            }

            //파라미터 설정
            ReportVO reportVO = new ReportVO();
            reportVO.setJrxmlPath(jrxmlPath); // 레포트 파일 경로
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("page", page); // 페이지
            params.put("subPage", subPage); // 서브페이지
            params.put("localPage", localPage); // 로컬페이지

            // Logo 파일 조회
            InputStream logo = utilsService.getClntLogo(SecurityUtil.getCurrentClntId());
            params.put("logo", logo);

            // 데이터 바인딩
            params.put("chartId", chartData.getChartId());
            params.put("chartNm", chartData.getChartNm());
            if ("Y".equals(chartData.getConfirmYn())) {
                String confirmAt = DateUtils.formatDate(chartData.getConfirmDt());
                params.put("confirmAt", confirmAt);
            }
            String createdAt = DateUtils.formatDate(chartData.getCreatedAt());
            params.put("createdAt", createdAt);

            params.put("chartBlobData", utilsService.convertToInputStream(chartData.getChartBlobData()));
            reportVO.setParameter(params);

            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page = page + JasperReport.getPages().size();
            localPage = localPage + JasperReport.getPages().size();


        }
        if (spSearchVO.isPrintAll())
            return reportList;
        reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);

        return reportList;
    }

    public void downloadOrgnExcelTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        // flow
        // NAS에서 파일 읽기 (InputStream)
        //엑셀 파싱 > 데이터 추가
        //수정된 엑셀을 HttpServletResponse에 써서 클라이언트로 전송
        if ("local".equals(property.getTarget())) {
            getLocalTemplate(request, response, id);
        } else if ("nas".equals(property.getTarget())) {
            getNasTemplate(request, response, id);
        }
    }

    // 로컬
    public void getLocalTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        FileTemplateEntity fileEntity = fileTemplateRepository.findById(id);
        String filePath = property.getUploadDir() + File.separator + fileEntity.getFilePath() + "." + fileEntity.getExtension();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다: " + filePath);
        }

        try (
                InputStream in = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(in);
                OutputStream out = new BufferedOutputStream(response.getOutputStream())
        ) {
            String[] parts = fileEntity.getFilePath().split("/");
            String lastPart = parts[parts.length - 1];
            String fileName = lastPart + "." + fileEntity.getExtension();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            response.setHeader("Content-Transfer-Encoding", "binary");

            workbook.write(out);
            out.flush();

        } catch (Exception e) {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: downloadFile", e.getMessage(), null);
            throw e;
        }
    }

    // NAS
    public void getNasTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        FileTemplateEntity fileEntity = fileTemplateRepository.findById(id);

        String filePath = property.getNas().getFoPath() + File.separator + fileEntity.getFilePath() + "." + fileEntity.getExtension();
        String user = property.getNas().getUser();
        String pass = property.getNas().getPass();
        String address = property.getNas().getIp();
        String sharedFolder = property.getNas().getShareFolder();

        SMBClient client = new SMBClient();
        InputStream in = null;
        Workbook workbook = null;
        OutputStream out = null;

        try (Connection connection = client.connect(address)) {
            AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
            Session session = connection.authenticate(ac);
            DiskShare share = (DiskShare) session.connectShare(sharedFolder);

            if (!share.fileExists(filePath)) {
                throw new CUserDefinedException("File doesn't exist: " + filePath);
            }

            com.hierynomus.smbj.share.File smbFile = share.openFile(
                    filePath,
                    EnumSet.of(AccessMask.GENERIC_READ),
                    null,
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    null
            );

            in = new BufferedInputStream(smbFile.getInputStream());

            // === Apache POI를 사용하여 엑셀 파일 파싱 ===
            workbook = WorkbookFactory.create(in);

            // === 사용자에게 전송 ===
            String encodedFileName = URLEncoder.encode(smbFile.getFileName(), StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20"); // 공백 + 방지

            // 파일 유형 설정
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            out = response.getOutputStream();
            workbook.write(out);

        } catch (Exception e) {
            response.setHeader("CustomHeader", e.getMessage());
            throw e;
        } finally {
            if (workbook != null) workbook.close();
            if (in != null) in.close();
            if (out != null) out.flush();
            client.close();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<OrganizationVO> insertOrgnExcel(List<OrganizationVO> voList) throws Exception {

        //조직 생성
        //조직ID 자동 부여 (년월일순번)
        List<OrganizationVO> returnVo = new ArrayList<>();
        for(OrganizationVO reqVo : voList){
            reqVo.setUseYn(YesNo.Y);
            organizationDao.insertOrganization(reqVo);
            returnVo.add(reqVo);

            String targetType = "orgn";
            BaseMapVO headHrInfo = new BaseMapVO(targetType,reqVo.getOrgnId(),reqVo.getHeadHrId(),"HEAD",reqVo.getCreatedBy());
            BaseMapVO secondHrInfo = new BaseMapVO(targetType,reqVo.getOrgnId(),reqVo.getSecondHrId(),"2ND",reqVo.getCreatedBy());

            hrDao.addHrMap(headHrInfo);
            hrDao.addHrMap(secondHrInfo);
        }

        return returnVo;
    }
}
