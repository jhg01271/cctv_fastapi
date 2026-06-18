package kr.co.igns.business.planning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.planning.dao.postgres.MsdsUnitDAO;
import kr.co.igns.business.planning.model.MsdsUnitVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsdsUnitService {
    private final MsdsUnitDAO msdsUnitDao;


    public List<MsdsUnitVO> getMsdsUnit(SpSearchVO searchVo) throws Exception {
        List<MsdsUnitVO> voList = msdsUnitDao.getMsdsUnit(searchVo);
        
        return voList;
    }
    
    public List<MsdsUnitVO> getMsdsUnitDataset(SpSearchVO searchVo) throws Exception {
    	List<MsdsUnitVO> voList = msdsUnitDao.getMsdsUnitDataset(searchVo);
    	
    	return voList;
    }


    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertMsdsUnit(List<MsdsUnitVO> reqVo) throws Exception {
    	for(MsdsUnitVO vo : reqVo) {
    		vo.setCompId(SecurityUtil.getCurrentCompId());

    		 if(vo.getCmd().equals("U")) {
    			 msdsUnitDao.updateMsdsUnit(vo);
             } else {
                 msdsUnitDao.insertMsdsUnit(vo);
             }
    	}

        return null;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateMsdsUnit(MsdsUnitVO reqVo) throws Exception {
        //MSDS 수정
    	MsdsUnitVO vo = msdsUnitDao.getMsdsUnitById(reqVo);
        if (vo == null)
            return null;
        vo = (MsdsUnitVO) SpUtils.objectMap(reqVo, vo);

        msdsUnitDao.updateMsdsUnit(vo);


        return vo;
    }

    public BaseVO deleteMsdsUnit(MsdsUnitVO reqVo) throws Exception {
        BaseVO vo = msdsUnitDao.getMsdsUnitById(reqVo);
        msdsUnitDao.deleteMsdsUnit(reqVo);
        
        return vo;
    }

   
	


}
