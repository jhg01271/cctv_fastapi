package kr.co.igns.system.setting.service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.setting.dao.postgres.AuthDAO;
import kr.co.igns.system.setting.dao.postgres.GroupDAO;
import kr.co.igns.system.setting.model.GroupMemberDeleteDto;
import kr.co.igns.system.setting.model.GroupMemberVO;
import kr.co.igns.system.setting.model.GroupVO;
import kr.co.igns.framework.utils.etc.Aes256;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

	private final GroupDAO groupDao;
	private final AuthDAO authDao;

	public BaseVO create(GroupVO reqVo) throws Exception {
		groupDao.insert(reqVo);
		return reqVo;

	}

	@Transactional
	public int createMember(List<GroupMemberVO> reqVo) throws Exception {
		int result=0;
		for(GroupMemberVO vo : reqVo){
			result += groupDao.insertMember(vo);
		}
		return result;

	}

	public BaseVO modify(GroupVO reqVo) throws Exception {
		GroupVO vo = groupDao.findOne(reqVo);
		if (vo == null)
			return null;
		vo = (GroupVO) SpUtils.objectMap(reqVo, vo);
		
		groupDao.update(vo);

		return vo;
	}

	public BaseVO remove(GroupVO reqVo) throws Exception {
		BaseVO vo = groupDao.findOne(reqVo);
		removeSingle(reqVo);
		// 저장된 그룹메뉴 목록도 함께 삭제되도록
		authDao.deleteByGrpCd(reqVo.getGrpCd());
		authDao.deleteGroupHrByGrpCd(reqVo);
		authDao.deleteGroupMenuByGrpCd(reqVo);

		return vo;
	}

	public int removeMember(GroupMemberDeleteDto vo) throws Exception {
		groupDao.deleteMemberMenuByGroup(vo);
		groupDao.deleteMember(vo);
		return 1;
	}
	private int removeSingle(GroupVO groupVO) throws Exception {
		return groupDao.delete(groupVO);
	}

    public List<HrVO> searchMember(SpSearchVO searchVo) throws Exception {
		List<HrVO> voList = groupDao.searchMember(searchVo);
		for (HrVO hr : voList) {
            decryptHr(hr);
        }
        return voList;
	}

	private void decryptHr(HrVO hrVo) {
        hrVo.setPhone(Aes256.decryptString(hrVo.getPhone()));
        hrVo.setEmail(Aes256.decryptString(hrVo.getEmail()));
    }

	public List<GroupVO> search(SpSearchVO searchVo) throws Exception {
		return groupDao.search(searchVo);
	}

	public List<GroupVO> searchLists() throws Exception {
		return groupDao.searchLists();
	}

	public BaseVO findOne(GroupVO reqVo) throws Exception {
		GroupVO vo = groupDao.findOne(reqVo);
		return vo;
	}

	public int searchCount(SpSearchVO searchVo) throws Exception {
		return groupDao.searchCount(searchVo);
	}

	// 데이터셋 API
	// 그룹목록 데이터 조회
	public List<GroupVO> getDatasetGroupList(SpSearchVO vo) {
		List<GroupVO> resultList = groupDao.getDatasetGroupList(vo);
		return resultList;
	}

	// 불러온 데이터셋 데이터 저장(복사)
    @Transactional(rollbackFor = Throwable.class)
	public void saveGroupDataset(List<GroupVO> voList) {
        String userId = SecurityUtil.getCurrentMemberId();
		for(GroupVO vo : voList){
			vo.setCompId(SecurityUtil.getCurrentCompId());
			vo.setCreatedBy(userId);
			groupDao.insertGroupDataset(vo); // 그룹 목록 저장(복사)
			groupDao.insertGroupMenuDataset(vo); // 그룹 목록에 해당하는 그룹 메뉴 저장(복사)
		}

	}

	public List<GroupVO> findUserGroup(List<String> userList) throws Exception {
		List<GroupVO> userGroupList = new ArrayList<>();
		for(String hrId : userList){
			GroupVO vo = new GroupVO();
			vo.setCompId(SecurityUtil.getCurrentCompId());
			vo.setHrId(hrId);
			GroupVO result = new GroupVO();
			result = groupDao.findUserGroup(vo);
			if(result != null){
				userGroupList.add(result);
			}
		}
		return userGroupList;
	}
}
