package kr.co.igns.system.setting.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.system.setting.dao.postgres.AuthDAO;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;
import kr.co.igns.system.setting.model.AuthVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MainMenuDAO mainMenuDao;
	private final AuthDAO authDao;

	public BaseVO create(AuthVO reqVo) throws Exception {
		authDao.insert(reqVo);
		return reqVo;

	}

	@Transactional
	public BaseVO saveGroup(List<AuthVO> reqVoList) throws Exception {
		AuthVO firstReqVo = reqVoList.get(0);
		authDao.removeGrpMapByParam(firstReqVo);
		
		if(firstReqVo.getMenuId()!=null && !firstReqVo.getMenuId().equals("")){
			for(AuthVO reqVo : reqVoList) {
				authDao.insertGrpMap(reqVo);
			}
		}
		return reqVoList.get(0);
	}

	@Transactional
	public BaseVO saveMember(List<AuthVO> reqVoList) throws Exception {
		AuthVO firstReqVo = reqVoList.get(0);
		authDao.removeMemMapByParam(reqVoList.get(0));
		if(firstReqVo.getMenuId()!=null && !firstReqVo.getMenuId().equals("")){
			for(AuthVO reqVo : reqVoList) {
				authDao.insertMemMap(reqVo);
				
			}
		}
		return reqVoList.get(0);
	}

	public BaseVO modify(AuthVO reqVo) throws Exception {
		AuthVO vo = authDao.findOne(reqVo.getMenuSeq());
		if (vo == null)
			return null;
		vo = (AuthVO) SpUtils.objectMap(reqVo, vo);
		
		authDao.update(vo);

		return vo;
	}

	public BaseVO remove(Long menuSeq) throws Exception {
		BaseVO vo = authDao.findOne(menuSeq);
		removeSingle(menuSeq);

		return vo;
	}

	public int removeByGrpCdAndParam(AuthVO vo) {
		return authDao.deleteByGrpCdAndParam(vo);
	}

	public int removeByGrpCd(String grpCd) {
		return authDao.deleteByGrpCd(grpCd);
	}

	private int removeSingle(Long menuSeq) throws Exception {
		return authDao.delete(menuSeq);
	}


	public List<AuthVO> search(SpSearchVO searchVo) throws Exception {
		List<AuthVO> voList = authDao.search(searchVo);
		return voList;
	}

	public ArrayNode treeAuth(SpSearchVO searchVo) throws Exception {
		List<AuthVO> voList = authDao.search(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
		return tree;
	}

	public ArrayNode groupTreeAuth(SpSearchVO searchVo) throws Exception {
		List<AuthVO> voList = authDao.searchByGroup(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
		return tree;
	}

	public ArrayNode userTreeAuth(SpSearchVO searchVo) throws Exception {
		List<String> userGroup = authDao.findUserGroup(searchVo);
		searchVo.setUserGroup(userGroup);
		if(userGroup.size() != 0){
			searchVo.setGrpId(userGroup.get(0));
		}
		List<AuthVO> voList = authDao.searchByUser(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
		return tree;
	}


	public BaseVO findOne(Long menuSeq) throws Exception {
		AuthVO vo = authDao.findOne(menuSeq);
		return vo;
	}

	public int searchCount(SpSearchVO searchVo) throws Exception {
		return authDao.searchCount(searchVo);
	}
	public int searchCountMenu(SpSearchVO searchVo) throws Exception {
		return authDao.searchCountMenu(searchVo);
	}
}
