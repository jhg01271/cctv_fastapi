package kr.co.igns.business.improvement.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.business.improvement.dao.postgres.ContinualImprovementDAO;
import kr.co.igns.business.improvement.model.ContinualImprovementVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContinualImprovementService {
    private final ContinualImprovementDAO continualImprovementDao;

	public List<ContinualImprovementVO> getContinualImprovementTitleMenu(@Valid SpSearchVO searchVo) {
		List<ContinualImprovementVO> voList = continualImprovementDao.getContinualImprovementTitleMenu(searchVo);
		return voList;
	}

	

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertNearMissReport(ContinualImprovementVO vo) throws Exception {
    	//continualImprovementDao.insertNearMissReport(vo);

        String targetType = "msds";

        return vo;
    }

	
	public BaseVO insertCorrectiveActionRequest(ContinualImprovementVO vo) {
		vo.setCompId(SecurityUtil.getCurrentCompId());
		vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
		//continualImprovementDao.insertCorrectiveActionRequest(vo);

//       String targetType = "msds";
		return vo;
	}

	@Transactional(rollbackFor = Throwable.class)
	public BaseVO updateCorrectiveActionRequest(ContinualImprovementVO vo) {
		vo.setCreatedBy(SecurityUtil.getCurrentMemberId());

		//continualImprovementDao.updateCorrectiveActionRequest(vo);

       String targetType = "msds";
		return vo;
	}


	public BaseVO deleteCorrectiveActionRequest(ContinualImprovementVO vo) {
		vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
		//continualImprovementDao.deleteCorrectiveActionRequest(vo);
       return vo;
	}
}
