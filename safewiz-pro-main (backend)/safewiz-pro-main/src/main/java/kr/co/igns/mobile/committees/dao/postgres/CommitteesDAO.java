package kr.co.igns.mobile.committees.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.mobile.committees.model.CommitteesDetailVO;
import kr.co.igns.mobile.committees.model.CommitteesSearchVO;
import kr.co.igns.mobile.committees.model.CommitteesVO;

@Mapper
@Repository
public interface CommitteesDAO {
	// 위원회 달력 count 조회
	List<CommitteesVO> getCommitteesCalendarCount(CommitteesSearchVO committeesSearchVO);
	// 위원회 일정 조회
	List<CommitteesVO> getCommittees(CommitteesSearchVO committeesSearchVO);
	// 위원회 일정 상세 조회
	CommitteesDetailVO getCommitteesDetail(CommitteesSearchVO committeesSearchVO);
}
