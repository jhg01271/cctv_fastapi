package kr.co.igns.system.setting.service;

import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.model.PrcsVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.CompDAO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompService {
    private final CompDAO compDao;
    private final FileService fileService;

    public List<CompVO> getComp(SpSearchVO searchVo) throws Exception {
        List<CompVO> voList = compDao.getCompList(searchVo);
        return voList;
    }

    //해당 고객에 매핑된 사업장만 선택
    public List<CompVO> getCompByClnt(SpSearchVO searchVo) throws Exception {
        List<CompVO> voList = compDao.getCompListByClnt(searchVo);
        return voList;
    }

    //유저에 매핑된 사업장만 선택
    public List<CompVO> getCompListByUserId(SpSearchVO searchVo) throws Exception {
        List<CompVO> voList = compDao.getCompListByUserId(searchVo);
        return voList;
    }

    //로그인 사용자의 사업장 조회 (메뉴 상단 사업장 표시용)
    public List<CompVO> getCompByHr(SpSearchVO searchVo) throws Exception {
        searchVo.setUserId(SecurityUtil.getCurrentMemberId());
        List<CompVO> voList = null;
        searchVo.setRole(SecurityUtil.getCurrentRole());
        if(searchVo.getRole().equals("MASTER")) {// MASTER는 tb_company_info 기준으로 use_yn='y'인 사업장 전체 조회
        	voList = compDao.getCompListByMaster(searchVo);
        }
        else {// 그 외는 tb_hr_info_comp_map 기준으로 사업장 목록 조회
	        voList = compDao.getCompListByHr(searchVo);
        }
        return voList;
    }

    public CompVO getCompDetail(String compId) throws Exception {
        CompVO voList = compDao.getCompDetail(compId);
        voList.setClntList(compDao.getCompClntDetail(compId));
        return voList;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return compDao.searchCount(searchVo);
    }

    public BaseVO insertComp(List<MultipartFile> files, CompVO reqVo) throws Exception {
        // 사업장아이디 자동 부여 (년월일순번)

        compDao.insertComp(reqVo);

        fileService.saveFile(files, "compLogo", reqVo.getCompId(), SecurityUtil.getCurrentCompId(), "system");
        return reqVo;
    }

    public BaseVO updateComp(List<MultipartFile> files, CompVO reqVo,String compId) throws Exception {
        CompVO vo = compDao.getCompById(reqVo);
        if (vo == null)
            return null;
//        vo = (CompVO) SpUtils.objectMap(reqVo, vo);
//        //comp id가 세션 comp id로 덮어씌어져서 다시 넣어줌
//        vo.setCompId(compId);

        compDao.updateComp(reqVo);

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "compLogo", reqVo.getCompId(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(files, "compLogo", reqVo.getCompId(), SecurityUtil.getCurrentCompId(), "system");
        vo.setFileId(fileId);   //수정된 파일의 file id를 front에 적용해줌

        return vo;
    }

    public BaseVO deleteComp(CompVO reqVo) throws Exception {
        BaseVO vo = compDao.getCompById(reqVo);
        compDao.deleteComp(reqVo);
        return vo;
    }

    public void deleteComps(List<CompVO> list) throws Exception {
        for (CompVO tmp : list) {
            compDao.deleteComp(tmp);
        }
    }
}
