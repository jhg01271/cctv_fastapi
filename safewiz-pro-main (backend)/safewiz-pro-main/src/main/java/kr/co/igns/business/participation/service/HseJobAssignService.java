package kr.co.igns.business.participation.service;

import kr.co.igns.business.participation.dao.postgres.HseJobAssignDAO;
import kr.co.igns.business.participation.model.HseJobAssignVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.igns.framework.config.security.SecurityUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HseJobAssignService {
    private final HseJobAssignDAO hsejobAssignDao;

    public List<HseJobAssignVO> getJobAssign(SpSearchVO searchVo) throws Exception {
        List<HseJobAssignVO> voList = hsejobAssignDao.getJobAssign(searchVo);

        return voList;
    }

    public HseJobAssignVO getJobAssignDetail(SpSearchVO searchVo) throws Exception {
        if (searchVo.getDocNo() == null || Objects.equals(searchVo.getDocNo(), "")) {
            SpSearchVO repVo = hsejobAssignDao.getPermitDocNo(searchVo);
            if (repVo != null) {
                searchVo.setDocNo(repVo.getDocNo());
                searchVo.setWriteYear(repVo.getWriteYear());
            }
        }
        HseJobAssignVO vo = hsejobAssignDao.getJobAssignDetail(searchVo);
//        List<HseJobAssignVO> vo = hsejobAssignDao.getJobAssign(searchVo);

//        return vo.get(0);
        return vo;
    }

    public List<HseJobAssignVO> getMyJobAssign(SpSearchVO searchVo) throws Exception {
        searchVo.setHrId(SecurityUtil.getCurrentHrId());
        List<HseJobAssignVO> voList = hsejobAssignDao.getJobAssign(searchVo);

        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveJobAssign(List<HseJobAssignVO> voList) throws Exception {
        for (HseJobAssignVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                hsejobAssignDao.insertJobAssign(vo);
            } else {
                //HSE 업무분장 수정
                HseJobAssignVO hseJobAssignVO = hsejobAssignDao.getJobAssignById(vo);
                if (hseJobAssignVO == null)
                    return null;
                hseJobAssignVO = (HseJobAssignVO) SpUtils.objectMap(vo, hseJobAssignVO);

                hsejobAssignDao.updateJobAssign(hseJobAssignVO);
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteJobAssign(HseJobAssignVO reqVo) throws Exception {
        BaseVO vo = hsejobAssignDao.getJobAssignById(reqVo);
        hsejobAssignDao.deleteJobAssign(reqVo);
        return vo;
    }

    public void deleteJobAssigns(List<HseJobAssignVO> list) throws Exception {
        for (HseJobAssignVO tmp : list) {
            hsejobAssignDao.deleteJobAssign(tmp);
        }
    }
}
