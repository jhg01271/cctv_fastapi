package kr.co.igns.system.base.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import kr.co.igns.business.dataset.model.TypeofbusinessVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.dao.postgres.HrDAO;
import kr.co.igns.system.base.dao.postgres.PartnerDAO;
import kr.co.igns.system.base.model.*;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.entity.FileTemplateEntity;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.igns.system.setting.dao.FileTemplateRepository;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.business.dataset.dao.postgres.DatasetDAO;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerDAO partnerDao;
    private final HrDAO hrDao;
    private final FileService fileService;
    private final UtilsService utilsService;
    private final FileTemplateRepository fileTemplateRepository;
    private final FileProperty property;
    private final DatasetDAO datasetDao;

    public List<PartnerVO> getPartner(SpSearchVO searchVo) throws Exception {
        List<PartnerVO> voList = partnerDao.getPartnerList(searchVo);
        return voList;
    }

    public PartnerVO getPartnerDetail(SpSearchVO searchVo) throws Exception {
        PartnerVO voList = partnerDao.getPartnerDetail(searchVo);

        //인원 매핑 정보 - 상세
        voList.setHrListH(utilsService.getHrIncharge("partner", voList.getPartCompId(), "HEAD"));
        voList.setHrListS(utilsService.getHrIncharge("partner", voList.getPartCompId(), "2ND"));
        voList.setPartnerHrListH(utilsService.getHrIncharge("partner", voList.getPartCompId(), "P_HEAD"));
        voList.setPartnerHrListS(utilsService.getHrIncharge("partner", voList.getPartCompId(), "P_2ND"));

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertPartner(List<MultipartFile> files, List<MultipartFile> files2, PartnerVO reqVo) throws Exception {

        //TODO: 왜 자동으로 comp_id 안되는지 확인해야됨
        reqVo.setCompId(SecurityUtil.getCurrentCompId());
        //협력사 생성
        //협력사 ID 자동 부여 (년월일순번)
        partnerDao.insertPartner(reqVo);

        //인원 매핑 - 본사담당자(정/부), 협력사담당자(정/부) 추가(target_id = part_comp_id)
        String targetType = "partner";
        BaseMapVO hrInfo = new BaseMapVO(targetType, reqVo.getPartCompId());
        hrInfo.setCreatedBy(reqVo.getCreatedBy());
        for (HrVO hr : reqVo.getHrListH()) {
            // 담당자(정) 업데이트
            hrInfo.setId(hr.getId());
            hrInfo.setUseYn(YesNo.Y);
            hrInfo.setGubun("HEAD");
            hrDao.addHrMap(hrInfo);
        }
        for (HrVO hr : reqVo.getHrListS()) {
            // 담당자(정) 업데이트
            hrInfo.setId(hr.getId());
            hrInfo.setUseYn(YesNo.Y);
            hrInfo.setGubun("2ND");
            hrDao.addHrMap(hrInfo);
        }
        for (HrVO hr : reqVo.getPartnerHrListH()) {
            // 담당자(정) 업데이트
            hrInfo.setId(hr.getId());
            hrInfo.setUseYn(YesNo.Y);
            hrInfo.setGubun("P_HEAD");
            hrDao.addHrMap(hrInfo);
        }
        for (HrVO hr : reqVo.getPartnerHrListS()) {
            // 담당자(정) 업데이트
            hrInfo.setId(hr.getId());
            hrInfo.setUseYn(YesNo.Y);
            hrInfo.setGubun("P_2ND");
            hrDao.addHrMap(hrInfo);
        }


        //파일 저장
        String fileId = fileService.saveFile(files, "partnerLogo", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId(), "base");
        String fileId2 = fileService.saveFile(files2, "partnerLocation", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId(), "base");
        reqVo.setFileId(fileId);
        reqVo.setFileId2(fileId2);

        return reqVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updatePartner(List<MultipartFile> insertFiles, List<MultipartFile> insertFiles2, PartnerVO reqVo) throws Exception {
        //협력사 수정
        PartnerVO vo = partnerDao.getPartnerById(reqVo.getPartCompId());
        if (vo == null)
            return null;
        vo = (PartnerVO) SpUtils.objectMap(reqVo, vo);

        partnerDao.updatePartner(vo);

        //인원 매핑 - 본사담당자(정/부), 협력사담당자(정/부) 수정(target_id = part_comp_id, hr_id = id)
        BaseMapVO baseMapVO = new BaseMapVO("partner", reqVo.getPartCompId());
        hrDao.hardDeleteHrMap(baseMapVO);
        baseMapVO.setCreatedBy(reqVo.getCreatedBy());
        for (HrVO hr : reqVo.getHrListH()) {
            // 본사 담당자(정) 업데이트
            baseMapVO.setId(hr.getId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("HEAD");
            hrDao.addHrMap(baseMapVO);
        }
        for (HrVO hr : reqVo.getHrListS()) {
            // 본사 담당자(부) 업데이트
            baseMapVO.setId(hr.getId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("2ND");
            hrDao.addHrMap(baseMapVO);
        }
        for (HrVO hr : reqVo.getPartnerHrListH()) {
            // 협력사 담당자(부) 업데이트
            baseMapVO.setId(hr.getId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("P_HEAD");
            hrDao.addHrMap(baseMapVO);
        }
        for (HrVO hr : reqVo.getPartnerHrListS()) {
            // 협력사 담당자(부) 업데이트
            baseMapVO.setId(hr.getId());
            baseMapVO.setUseYn(YesNo.Y);
            baseMapVO.setGubun("P_2ND");
            hrDao.addHrMap(baseMapVO);
        }

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "partnerLogo", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "partnerLogo", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId(), "base");
        vo.setFileId(fileId);   //수정된 파일의 file id를 front에 적용해줌
        fileService.deleteFile(reqVo.getDeleteFiles2(), "partnerLocation", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId());
        String fileId2 = fileService.saveFile(insertFiles2, "partnerLocation", reqVo.getPartCompId(), SecurityUtil.getCurrentCompId(), "base");
        vo.setFileId2(fileId2);   //수정된 파일의 file id를 front에 적용해줌

        return vo;
    }

    public BaseVO deletePartner(PartnerVO reqVo) throws Exception {
        BaseVO vo = partnerDao.getPartnerById(reqVo.getPartCompId());
        partnerDao.deletePartner(reqVo);
        return vo;
    }

    public void deletePartners(List<PartnerVO> list) throws Exception {
        for (PartnerVO tmp : list) {
            partnerDao.deletePartner(tmp);
        }
    }

    public void downloadPartnerExcelTemplate(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
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
                Workbook workbook = setExcelSheet(in);
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
        ;
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
            workbook = setExcelSheet(in);

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

    // 엑셀 시트 데이터 바인딩
    public Workbook setExcelSheet(InputStream in) throws Exception {
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(1); // 두 번째 시트(데이터셋 시트)
        SpSearchVO searchVo = new SpSearchVO();
        List<TypeofbusinessVO> businessVoList = datasetDao.getTypeofbusinessList(searchVo);
        CellStyle textStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@")); // "@"는 텍스트 형식 의미

        // === 엑셀에 데이터 추가 ===
        // === 업종 ===
        int currentRow = 2; // ==> 3행
        int currentCol = 1; // ==> B열

        for (int i = 0; i < businessVoList.size(); i++) {
            Row row = sheet.getRow(currentRow); // 엑셀의 행
            if (row == null) row = sheet.createRow(currentRow);
            // -- minorCd
            Cell minorCdCell = row.createCell(currentCol); // 엑셀의 열
            minorCdCell.setCellStyle(textStyle); // <-- 스타일 지정 (String 타입으로 지정)
            minorCdCell.setCellValue(businessVoList.get(i).getDetailCd());

            // -- minorNm
            Cell minorNmCell = row.createCell(currentCol + 1); // 엑셀의 열
            minorNmCell.setCellValue(businessVoList.get(i).getDetailNm());
            currentRow++;
        }


        return workbook;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<PartnerVO> insertPartnerExcel(List<PartnerVO> voList) throws Exception {

        //조직 생성
        //조직ID 자동 부여 (년월일순번)
        List<PartnerVO> returnVo = new ArrayList<>();
        SpSearchVO searchVo = new SpSearchVO();
        List<TypeofbusinessVO> businessList = datasetDao.getTypeofbusinessList(searchVo);
        for (PartnerVO reqVo : voList) {
            reqVo.setUseYn(YesNo.Y);
            for (TypeofbusinessVO business : businessList) {
                if (business.getDetailCd().equals(reqVo.getBizCd())) {
                    reqVo.setBizNm(business.getDetailNm());
                }
            }
            partnerDao.insertPartner(reqVo);
        }

        return returnVo;
    }

    // 거래품목 관리 관련 API
    // 조회
    public List<PartnerVO> getPartCompItemDataset(SpSearchVO spSearchVO) throws Exception {
        return partnerDao.getPartCompItemDataset(spSearchVO);
    }

    // 예시 조회
    public List<PartnerVO> getSamplePartCompItemDataset(SpSearchVO spSearchVO) throws Exception {
        return partnerDao.getSamplePartCompItemDataset(spSearchVO);
    }

    // 저장
    @Transactional(rollbackFor = Throwable.class)
    public void savePartCompItemDataset(List<PartnerVO> voList) throws Exception {
        for (PartnerVO vo : voList) {
            if ("I".equals(vo.getCmd())) {
                // 신규
                partnerDao.insertPartCompItemDataset(vo);
            } else if ("U".equals(vo.getCmd())) {
                // 기존
                partnerDao.updatePartCompItemDataset(vo);
            }
        }
    }

    // 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deletePartCompItemDataset(List<PartnerVO> voList) throws Exception {
        for (PartnerVO vo : voList) {
            partnerDao.deletePartCompItemDataset(vo);
        }
    }
}
