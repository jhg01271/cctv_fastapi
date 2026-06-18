package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.SafetyAndHealthObjectivesVO;
import kr.co.igns.business.task.model.TaskVO;
import kr.co.igns.system.base.model.OrganizationVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface SafetyAndHealthObjectivesDAO {

	// 메인 화면 조회
	List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

	// 상세 메인 화면 조회
	List<SafetyAndHealthObjectivesVO> getMainSafetyAndHealthObjective(SafetyAndHealthObjectivesVO vo);

	// 상세 화면 조회
	List<SafetyAndHealthObjectivesVO> getSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo);

	// 안전보건목표에 등록되지 않은 조직 조회
	List<OrganizationVO> getOrganizationInSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

	// 문서번호 생성
	String getDocNo(SafetyAndHealthObjectivesVO vo);

	// 메인 신규추가
	int insertSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

	// 메인 수정
	int updateSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

	// 수정시 아코디언을 체크안하고 저장했을때(사용여부만 업데이트)
	int saveSafetyAndHealthObjectivesTop(SafetyAndHealthObjectivesVO vo);

	// 상세 신규추가
	int insertSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo);

	// 상세 수정
	int updateSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo);

	// 메인 삭제
	int delSafetyAndHealthObjectives(SafetyAndHealthObjectivesVO vo);

	// 상세 삭제
	int delSafetyAndHealthObjectivesDetail(SafetyAndHealthObjectivesVO vo);

	// 리포트
	SafetyAndHealthObjectivesVO getReportData(SafetyAndHealthObjectivesVO vo);

	// 상세에서 체크하고 선택했을대
	List<SafetyAndHealthObjectivesVO> getReportGridData(SafetyAndHealthObjectivesVO vo);

	// 상세에서 체크하지 않고 출력했을때
	List<SafetyAndHealthObjectivesVO> getReportGridDataUseYn(SafetyAndHealthObjectivesVO vo);

	// 메인에서 리포트를 출력했을때
	List<SafetyAndHealthObjectivesVO> getSafetyHealthObjectivesReportMain(SafetyAndHealthObjectivesVO vo);
	
	//테스크 리스트 조회
	List<TaskVO> getTaskList(SafetyAndHealthObjectivesVO vo);
	
	void updateTask(TaskVO vo);
}
