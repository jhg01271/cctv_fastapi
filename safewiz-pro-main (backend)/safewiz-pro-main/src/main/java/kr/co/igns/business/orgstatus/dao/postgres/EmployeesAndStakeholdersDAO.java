package kr.co.igns.business.orgstatus.dao.postgres;

import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.orgstatus.model.EmployeesAndStakeholdersVO;

import java.util.List;

import javax.validation.Valid;

@Mapper
@Repository
public interface EmployeesAndStakeholdersDAO {

	List<EmployeesAndStakeholdersVO> searchEmployeesStakeholders(EmployeesAndStakeholdersVO reqVo);

	List<EmployeesAndStakeholdersVO> searchYear();

	void addEmployeesStakeholders( EmployeesAndStakeholdersVO reqVo);
	
	void modifyEmployeesStakeholders(EmployeesAndStakeholdersVO reqVo);
	
	void deleteEmployeesStakeholdersList(EmployeesAndStakeholdersVO reqVo);
	
	List<EmployeesAndStakeholdersVO> searchEmployeesStakeholdersDetail(EmployeesAndStakeholdersVO reqVo);
	
	void addEmployeesStakeholdersDetail(EmployeesAndStakeholdersVO reqVo);
	
	void modifyEmployeesStakeholdersDetail(EmployeesAndStakeholdersVO reqVo);
	
	void deleteEmployeesStakeholdersDetail(EmployeesAndStakeholdersVO reqVo);
	
	// 출력물 조회
	EmployeesAndStakeholdersVO getReportData(SpSearchVO reqVo);
	List<EmployeesAndStakeholdersVO> getReportGridData(SpSearchVO reqVo);

	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);
}
