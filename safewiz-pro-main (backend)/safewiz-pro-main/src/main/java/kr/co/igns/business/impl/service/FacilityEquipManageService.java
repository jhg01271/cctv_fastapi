package kr.co.igns.business.impl.service;

import kr.co.igns.business.impl.dao.postgres.FacilityEquipManageDAO;
import kr.co.igns.business.impl.model.FacilityEquipManageDetailVO;
import kr.co.igns.business.impl.model.FacilityEquipManageVO;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityEquipManageService {
    private final FacilityEquipManageDAO facilityEquipManageDAO;
    private final FileService fileService;

    public List<FacilityEquipManageVO> getFacilityEquipManage(SpSearchVO searchVo) throws Exception {
        List<FacilityEquipManageVO> voList = facilityEquipManageDAO.getFacilityEquipManage(searchVo);
        return voList;
    }
    
    public List<FacilityEquipManageVO> getFacilityEquipManageDetail(FacilityEquipManageVO searchVo) throws Exception {
        List<FacilityEquipManageVO> voList = facilityEquipManageDAO.getFacilityEquipManageDetail(searchVo);
        return voList;
    }
    
    @Transactional(rollbackFor = Throwable.class)
	public FacilityEquipManageVO insertFacilityEquipManage(List<MultipartFile> files, FacilityEquipManageVO vo) throws Exception {
    	if("I".equals(vo.getCmd())) {
    		facilityEquipManageDAO.insertFacilityEquipManage(vo);
    	} else {
    		facilityEquipManageDAO.updateFacilityEquipManage(vo);
    		fileService.deleteFile(vo.getDeleteFiles(), "notice", vo.getFacilitisId(), SecurityUtil.getCurrentCompId());
    	}
		
		fileService.saveFile(files, "facilityEquip", vo.getFacilitisId(), SecurityUtil.getCurrentCompId());
		
		return vo;
	}
    
    @Transactional(rollbackFor = Throwable.class)
	public List<FacilityEquipManageVO> deleteFacilityEquipManage(List<FacilityEquipManageVO> voList) throws Exception {
		for(FacilityEquipManageVO vo : voList) {
			facilityEquipManageDAO.updateFacilityEquipManageUseN(vo);
		}
		return voList;
	}
}
