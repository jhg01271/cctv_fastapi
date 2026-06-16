package kr.co.igns.business.planning.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.planning.model.ImprovementAndImplementationVO;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ImprovementAndImplementationDAO {
	List<ImprovementAndImplementationVO> searchData(ImprovementAndImplementationVO vo);
	List<ImprovementAndImplementationVO> searchDataDetail(ImprovementAndImplementationVO vo);
	List<ImprovementAndImplementationVO> searchReport(SpSearchVO vo);
}

