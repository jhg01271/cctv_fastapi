package kr.co.igns.system.setting.service;

import kr.co.igns.system.setting.dao.postgres.AuthDAO;
import kr.co.igns.system.setting.dao.postgres.CompGroupDAO;
import kr.co.igns.system.setting.model.GroupVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompGroupService {

	private final CompGroupDAO groupDao;
	private final AuthDAO authDao;

	public BaseVO create(GroupVO reqVo) throws Exception {
		groupDao.insert(reqVo);
		return reqVo;

	}

	public BaseVO modify(GroupVO reqVo) throws Exception {
		GroupVO vo = groupDao.findOne(reqVo.getGrpId());
		if (vo == null)
			return null;
		vo = (GroupVO) SpUtils.objectMap(reqVo, vo);
		
		groupDao.update(vo);

		return vo;
	}

	public BaseVO remove(String grpCd) throws Exception {
		BaseVO vo = groupDao.findOne(grpCd);
		removeSingle(grpCd);
		// 저장된 그룹메뉴 목록도 함께 삭제되도록
		authDao.deleteByGrpCd(grpCd);

		return vo;
	}
	private int removeSingle(String grpCd) throws Exception {
		return groupDao.delete(grpCd);
	}


	public List<GroupVO> search(SpSearchVO searchVo) throws Exception {
		return groupDao.search(searchVo);
	}


	public List<GroupVO> searchLists() throws Exception {
		return groupDao.searchLists();
	}

	public BaseVO findOne(String grpCd) throws Exception {
		GroupVO vo = groupDao.findOne(grpCd);
		return vo;
	}

	public int searchCount(SpSearchVO searchVo) throws Exception {
		return groupDao.searchCount(searchVo);
	}
}
