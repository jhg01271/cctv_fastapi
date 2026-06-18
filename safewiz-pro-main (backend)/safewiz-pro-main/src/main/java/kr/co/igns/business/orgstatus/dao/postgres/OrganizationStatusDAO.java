package kr.co.igns.business.orgstatus.dao.postgres;
import kr.co.igns.business.orgstatus.model.OrganizationStatusVO;
import kr.co.igns.business.utils.model.UtilsVO;
import kr.co.igns.system.common.model.SpSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.business.safewiz.orgstatus.dao.postgres
 * ※ 파일명 : OrganizationStatusDAO.java
 * ※ 기능명 :
 * ※ 작성자 : 김성현
 * ※ 최초생성일 : 2024. 9. 04.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface OrganizationStatusDAO {
	
	int insert(OrganizationStatusVO vo);
	
	int insertDetail(OrganizationStatusVO vo);
	
	int insertSignature(UtilsVO vo);
	
	int update(OrganizationStatusVO vo);
	
	int updateDetail(OrganizationStatusVO vo);
	
	int updateSignature(UtilsVO vo);
	
	int updateUseYn(String statusId);
	
	int changeUseNOrg(OrganizationStatusVO vo);
	
	int changeUseNOrgDetail(OrganizationStatusVO vo);
	
	int deleteOrgDetail(OrganizationStatusVO vo);
	
	int deleteOrg(OrganizationStatusVO vo);
	
	int deleteSignature(UtilsVO vo);
	
	List<UtilsVO> searchHrId(UtilsVO vo);
	
	int searchSignatureCnt(UtilsVO vo);
	
	int searchCount(SpSearchVO searchVo);
	
	// 년도별 조직 상황 조회
	List<OrganizationStatusVO> search(OrganizationStatusVO searchVo);
	
	// 조직 상황 상세 조회
	List<OrganizationStatusVO> searchDetail(OrganizationStatusVO searchVo);

	// 출력물 용 데이터 조회
	OrganizationStatusVO getReportData(SpSearchVO searchVo);
	List<OrganizationStatusVO>  getReportGridData(SpSearchVO searchVo);
	
	OrganizationStatusVO findOne(String compId);
	OrganizationStatusVO findByInviteKey(String inviteKey);


	// 일괄출력용
	List<SpSearchVO> getAllReport(SpSearchVO vo);
}
