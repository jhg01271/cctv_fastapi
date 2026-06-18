package kr.co.igns.business.orgstatus.service;

import kr.co.igns.business.orgstatus.dao.postgres.OrganizationStatusDAO;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.utils.dao.postgres.UtilsDAO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.business.utils.service.UtilsService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.framework.report.service.ReportService;
import kr.co.igns.framework.report.model.ReportVO;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.orgstatus.service
 * ※ 파일명 : OrganizationStatusService.java
 * ※ 기능명 :
 * ※ 작성자 : 김성현
 * ※ 최초생성일 : 2024. 9. 04.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
//@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationStatusService {

    private final OrganizationStatusDAO organizationStatusDao;
    private final ClientDAO clientDao;
    private final CompDAO compDao;
    private final ReportService reportService;
    private final UtilsService utilsService;
    private final JwtService jwtService;
    @Value("${jwt.invite.expiration}")
    private Long inviteTokenExpirationPeriod;

//	public BaseVO create(OrganizationStatusVO organizationStatusVo) throws Exception {
//		String token = jwtService.createCustomToken("status_cd", organizationStatusVo.getStatusCd() , inviteTokenExpirationPeriod);
//		
//		organizationStatusVo.setInviteKey(token);
//		organizationStatusDao.insert(organizationStatusVo);
//
//		return organizationStatusVo;
//
//	}

    public BaseVO insert(OrganizationStatusVO organizationStatusVo) throws Exception {
        //String token = jwtService.createCustomToken("status_cd", organizationStatusVo.getStatusCd() , inviteTokenExpirationPeriod);

        //organizationStatusVo.setInviteKey(token);
        organizationStatusDao.insert(organizationStatusVo);

        return organizationStatusVo;
    }

    public BaseVO insertDetail(OrganizationStatusVO organizationStatusVo) throws Exception {
        //String token = jwtService.createCustomToken("status_cd", organizationStatusVo.getStatusCd() , inviteTokenExpirationPeriod);

        //organizationStatusVo.setInviteKey(token);
        organizationStatusDao.insertDetail(organizationStatusVo);

        return organizationStatusVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO setSignature(UtilsVO saveVo) throws Exception {
        // 기존의 서명 삭제 후 새로 삽입
        utilsService.insertApprovalInfo(saveVo);
//		utilsDao.deleteApprovalInfo(saveVo);
//		utilsDao.insertApprovalInfo(saveVo);
        return saveVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<OrganizationStatusVO> setDetail(List<OrganizationStatusVO> saveVoList) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 마스터 데이터 useYn 저장해야 함으로 첫번째 useYn을 임시로 저장한다.
        String tempUseYn;
        // 배열 길이가 0일 시 return
        if (saveVoList.size() == 0) return saveVoList;

        OrganizationStatusVO firstVo = saveVoList.get(0);
        // 첫번째 useYn 임시 저장
        tempUseYn = firstVo.getUseYn();
        firstVo.setUseYn(firstVo.getUseYnM());

        // 신규 등록일 시 조직 메인 테이블 insert, 아닐 시 update
        if ("".equals(firstVo.getDocNo()) || firstVo.getDocNo() == null) {
            firstVo.setCreatedBy(authentication.getName());
            // statusId가 반환되어 firstVo에 저장된다 >> 나머지에도 모두 statusId를 넣어줘야한다.
            organizationStatusDao.insert(firstVo);

        } else {
            firstVo.setUpdatedBy(authentication.getName());
            organizationStatusDao.update(firstVo);
        }

        // 다시 원복
        firstVo.setUseYn(tempUseYn);
        for (OrganizationStatusVO saveVo : saveVoList) {
            // 신규 등록인 경우 statusId가 없기 때문에 넣어줘야한다.
            if ("".equals(saveVo.getWriteYear()) || saveVo.getWriteYear() == null) {
                saveVo.setWriteYear(String.valueOf(LocalDate.now().getYear()));
            }

            saveVo.setDocType("ORGST");
            saveVo.setDocNo(firstVo.getDocNo());
            if ("".equals(saveVo.getIssue())) continue;
            if ("U".equals(saveVo.getCmd())) {
                saveVo.setUpdatedBy(authentication.getName());
                organizationStatusDao.updateDetail(saveVo);
            } else {
                saveVo.setCreatedBy(authentication.getName());
                organizationStatusDao.insertDetail(saveVo);
            }
        }
        return saveVoList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<UtilsVO> insertSignature(List<UtilsVO> saveVoList) throws Exception {
        //final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (UtilsVO saveVo : saveVoList) {
			/*
			saveVo.setUserId(authentication.getName());
			if("writer".equals(saveVo.getParam())) {
				List<UtilsVO> utilsVoList = organizationStatusDao.searchHrId(saveVo);
				System.out.println(utilsVoList.size());
				if(utilsVoList.size() > 0) {
					saveVo.setHrId(utilsVoList.get(0).getHrId());
					saveVo.setHrNm(utilsVoList.get(0).getHrNm());
				}
			}
			// 사람이 지정되어 있지 않을 경우 continue
			if (saveVo.getHrId() == null || saveVo.getHrId().isEmpty()) {
				continue;
			}
			*/
            utilsService.insertApprovalInfo(saveVo);
//			utilsDao.deleteApprovalInfo(saveVo);
//			utilsDao.insertApprovalInfo(saveVo);
        }

        return saveVoList;
    }

    public BaseVO update(OrganizationStatusVO organizationStatusVo) throws Exception {
        organizationStatusDao.update(organizationStatusVo);
        return organizationStatusVo;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<OrganizationStatusVO> delete(List<OrganizationStatusVO> saveVoList) throws Exception {
        for (OrganizationStatusVO saveVo : saveVoList) {
            saveVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            organizationStatusDao.changeUseNOrg(saveVo);
        }
        return saveVoList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<OrganizationStatusVO> deleteDetail(List<OrganizationStatusVO> saveVoList) throws Exception {
        for (OrganizationStatusVO saveVo : saveVoList) {
            saveVo.setUpdatedBy(SecurityUtil.getCurrentMemberId());
            organizationStatusDao.changeUseNOrgDetail(saveVo);
        }
        return saveVoList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public UtilsVO deleteSignature(UtilsVO saveVo) throws Exception {
        utilsService.deleteApprovalInfo(saveVo);
        return saveVo;
    }

    public BaseVO deleteOrg(OrganizationStatusVO organizationStatusVo) throws Exception {
        //String token = jwtService.createCustomToken("status_cd", organizationStatusVo.getStatusCd() , inviteTokenExpirationPeriod);

        //organizationStatusVo.setInviteKey(token);
        organizationStatusDao.deleteOrg(organizationStatusVo);

        return organizationStatusVo;

    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return organizationStatusDao.searchCount(searchVo);
    }

    public List<OrganizationStatusVO> search(OrganizationStatusVO searchVo) throws Exception {
        List<OrganizationStatusVO> voList = organizationStatusDao.search(searchVo);
        return voList;
    }

    public List<OrganizationStatusVO> searchDetail(OrganizationStatusVO searchVo) throws Exception {
        List<OrganizationStatusVO> voList = organizationStatusDao.searchDetail(searchVo);
        return voList;
    }

    public List<UtilsVO> getSignature(OrganizationStatusVO searchVo) throws Exception {
        UtilsVO utisVO = new UtilsVO();
        utisVO.setTargetId(searchVo.getWriteYear() + searchVo.getDocNo());
        utisVO.setTargetType(searchVo.getDocType());
        List<UtilsVO> voList = utilsService.getApprovalInfo(utisVO);
        return voList;
    }

    public BaseVO findOne(String status_cd) throws Exception {
        OrganizationStatusVO vo = organizationStatusDao.findOne(status_cd);

        return vo;
    }

    // 일괄출력용
    public List<JasperPrint> getAllReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO) throws Exception {
        List<JasperPrint> reportList = new ArrayList<>();
        int localPage = spSearchVO.getLocalPage();
        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        List<SpSearchVO> checkedObjList = new ArrayList<>();

        List<JasperPrint> jaspers = new ArrayList<>();
        checkedObjList = organizationStatusDao.getAllReport(spSearchVO);
        if (!checkedObjList.isEmpty()) {
            SpSearchVO reportParam = new SpSearchVO();
            reportParam.setPrintAll(true);
            reportParam.setCheckedObjList(checkedObjList);
            reportParam.setPage(page);
            reportParam.setSubPage(subPage);
            reportParam.setLocalPage(localPage);
            jaspers = getReport(request, response, reportParam);
            reportList.addAll(jaspers);
        }
//        for (JasperPrint report : jaspers) {
//            page += report.getPages().size();
//            localPage += report.getPages().size();
//        }
        return reportList;
    }

    //**********************레포트 관련******************************
    // 전체 예산 레포트
    public List<JasperPrint> getReport(HttpServletRequest request, HttpServletResponse response, SpSearchVO spSearchVO)
            throws Exception {

        /***********************/
        /****** 공통 정보 조회 ******/
        /***********************/
        List<JasperPrint> reportList = new ArrayList<>();

        // 고객사 ID 조회
        spSearchVO.setClntId(SecurityUtil.getCurrentClntId());

        // 고객사 명칭 조회
        ClientVO cVo = clientDao.getClientById(spSearchVO.getClntId());
        String clntNm = cVo.getClntNm();

        // 사업장 ID 조회
        spSearchVO.setCompId(SecurityUtil.getCurrentCompId());

        // 사업장 명칭 조회
        CompVO cpVO = new CompVO();
        cpVO.setClntId(spSearchVO.getClntId());
        cpVO.setCompId(spSearchVO.getCompId());
        CompVO cPo = compDao.getCompById(cpVO);
        String compNm = cPo.getCompNm();


        /**********************************/
        /****** 출력물 공통 Parameter 입력 *****/
        /**********************************/

        Map<String, Object> params = new HashMap<String, Object>();

        //고객사 명 입력


        // Logo 파일 조회
        InputStream logo = utilsService.getClntLogo(spSearchVO.getClntId());
        params.put("logo", logo);

        int page = spSearchVO.getPage();
        int subPage = spSearchVO.getSubPage();
        int localPage = spSearchVO.getLocalPage();
        SpSearchVO spVo = new SpSearchVO();
        spVo.setPage(page);
        spVo.setSubPage(subPage);
        spVo.setLocalPage(localPage);
        String title = "조직의 상황 분석표";
        if (!spSearchVO.isPrintAll()) {
            JasperPrint JasperFrontReport = utilsService.getFrontReport("basicFront_reverse", spVo, title);
            reportList.add(JasperFrontReport);
            page += JasperFrontReport.getPages().size();
            localPage += JasperFrontReport.getPages().size();
        }

        /*************************************/
        /****** 출력물 Custom Parameter 입력 *****/
        /*************************************/
        String fileNm = "";
        String orgnNm = "";
        List<String> yearSet = new ArrayList<>();
        for (SpSearchVO vo : spSearchVO.getCheckedObjList()) {
            params.put("logo", utilsService.getClntLogo(spSearchVO.getClntId()));
            params.put("year", vo.getWriteYear());
            params.put("page", page);
            params.put("subPage", subPage);
            params.put("localPage", localPage);
            yearSet.add(vo.getWriteYear());
            //출력물 용 데이터 조회
            OrganizationStatusVO reportData = organizationStatusDao.getReportData(vo);
            if (spSearchVO.getCheckedObjList().size() == 1) {
                orgnNm = reportData.getOrgnNm();
            }

            //조직명 입력
            params.put("title", reportData.getOrgnNm());

            //gridData 조회
            List<OrganizationStatusVO> resultList = organizationStatusDao.getReportGridData(vo);

            //gridData 입력
            params.put("gridData", new JRBeanCollectionDataSource(resultList));

            // 서명 데이터 조회
//            UtilsVO signParam = new UtilsVO();
//            signParam.setTargetType(vo.getDocType());
//            signParam.setTargetId(vo.getWriteYear() + vo.getDocNo());

//            List<UtilsVO> signList = utilsService.getApprovalInfo(signParam); // 서명 데이터 조회

            List<Map<String, Object>> approvalInfo = utilsService.getApprovalInfoToArray(vo);
            params.put("signatureList", new JRBeanCollectionDataSource(approvalInfo));
//            List<Object> approvalInfo = utilsService.getApprovalInfoToArray(vo);
//            params.put("signWriter", approvalInfo.get(0));
//            params.put("signWriterNm", approvalInfo.get(1));
//            params.put("signReviewer", approvalInfo.get(2));
//            params.put("signReviewerNm", approvalInfo.get(3));
//            params.put("signApprover", approvalInfo.get(4));
//            params.put("signApproverNm", approvalInfo.get(5));


            /*************************************/
            /****** Jasper Export File Setup *****/
            /*************************************/

            ReportVO reportVO = new ReportVO();

            // 출력 파일 명 설정
            reportVO.setFileName(fileNm);

            // 출력 생성용 Jasper 파일 위치
            reportVO.setJrxmlPath("report/orgnStatus/organizationStatus/organizationStatus.jrxml");

            // 출력  파일 형식 지정
            reportVO.setType(spSearchVO.getType());

            /**********************/
            /****** 출력물 출력 *******/
            /**********************/

            reportVO.setParameter(params);
            JasperPrint JasperReport = reportService.allReportJasperPrint(reportVO);
            reportList.add(JasperReport);
            page += JasperReport.getPages().size();
            localPage += JasperReport.getPages().size();
        }
        // 출력 파일 명 : 고객사 명칭_출력 파일 명(날짜)
        String years = yearSet.stream().distinct().collect(Collectors.joining(","));
        fileNm = "(" + years + ")";
        fileNm += "조직의 상황 분석표";
        if (spSearchVO.getCheckedObjList().size() == 1) {
            fileNm += "_" + orgnNm;
        }

        // 통합 출력인 경우 reportVO만 반환하고 종료
        if (spSearchVO.isPrintAll()) {
            return reportList;
        } else {
            reportService.exportReportAll(request, response, reportList, spSearchVO.getType(), fileNm);
        }
        return reportList;
    }


}
