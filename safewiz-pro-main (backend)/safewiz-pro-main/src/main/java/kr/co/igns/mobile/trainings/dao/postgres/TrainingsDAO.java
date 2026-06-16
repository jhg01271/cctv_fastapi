package kr.co.igns.mobile.trainings.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.trainings.model.TrainingsDetailVO;
import kr.co.igns.mobile.trainings.model.TrainingsSearchVO;
import kr.co.igns.mobile.trainings.model.TrainingsVO;

@Mapper
@Repository
public interface TrainingsDAO {
	// 교육관리 달력 count 조회
	TrainingsVO getTrainingsRORATCalendarCount(TrainingsSearchVO trainingsSearchVO);
	TrainingsVO getTrainingsTRRCalendarCount(TrainingsSearchVO trainingsSearchVO);
	// 교육 결과 목록 조회
	List<TrainingsVO> getTrainings(TrainingsSearchVO trainingsSearchVO);
	// 교육 결과 상세 조회
	TrainingsDetailVO getTrainingsRORATDetail(TrainingsSearchVO trainingsSearchVO);
	TrainingsDetailVO getTrainingsTRRDetail(TrainingsSearchVO trainingsSearchVO);
}
