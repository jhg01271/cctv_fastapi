package kr.co.igns.business.improvement.dao.postgres;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.improvement.model.ProvisionAndMgmtPPEVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ProvisionAndMgmtPPEDAO {

	List<ProvisionAndMgmtPPEVO> getProvisionAndMgmtPPEList(SpSearchVO searchVo);
	List<ProvisionAndMgmtPPEVO> getProvisionAndMgmtPPEDetailList(ProvisionAndMgmtPPEVO vo);
	int insertProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo);
	int deleteProvisionAndMgmtPPE(ProvisionAndMgmtPPEVO vo);
	List<ProvisionAndMgmtPPEVO> getReportDataProvisionAndMgmt(ProvisionAndMgmtPPEVO vo);
	
	List<ProvisionAndMgmtPPEVO> getDatasetPPEList(SpSearchVO searchVo);	
	List<ProvisionAndMgmtPPEVO> getDatasetPPEListReport(SpSearchVO searchVo);
	List<ProvisionAndMgmtPPEVO> getReportPPEManagementchk(Map<String, Object> paramMap);
	int insertDatasetPPE(ProvisionAndMgmtPPEVO vo);
	int updateDatasetPPE(ProvisionAndMgmtPPEVO vo);
	int deleteDatasetPPE(ProvisionAndMgmtPPEVO vo);
}
