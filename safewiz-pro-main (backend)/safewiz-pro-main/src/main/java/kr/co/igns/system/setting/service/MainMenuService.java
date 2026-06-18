package kr.co.igns.system.setting.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;
import kr.co.igns.system.setting.model.MainMenuVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainMenuService {

    private final MainMenuDAO mainMenuDao;

    public BaseVO create(MainMenuVO reqVo) throws Exception {
        mainMenuDao.insert(reqVo);
        return reqVo;

    }

    public BaseVO modify(MainMenuVO reqVo) throws Exception {
        MainMenuVO vo = mainMenuDao.findOne(reqVo.getMenuId());
        if (vo == null)
            return null;
        vo = (MainMenuVO) SpUtils.objectMap(reqVo, vo);

        mainMenuDao.update(vo);

        return vo;
    }

    public BaseVO remove(String menuId) throws Exception {
        BaseVO vo = mainMenuDao.findOne(menuId);
        removeSingle(menuId);

        SpSearchVO searchVO = SpSearchVO.builder()
                .searchCd(menuId)
                .build();
        List<MainMenuVO> children = search(searchVO);
        for (MainMenuVO child : children) {
            // 하위 메뉴들 재귀 삭제
            remove(child.getMenuId());
        }

        return vo;
    }

    private int removeSingle(String menuId) throws Exception {
        return mainMenuDao.delete(menuId);
    }


    public List<MainMenuVO> search(SpSearchVO searchVo) throws Exception {
        List<MainMenuVO> voList = new ArrayList<>();
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority(); // "ROLE_USER" 같은 값
        searchVo.setRole(role.replaceFirst("ROLE_", ""));
        String roleCd = SecurityUtil.getCurrentRole();
        if ("MASTER".equals(roleCd)) {
            voList = mainMenuDao.searchMaster(searchVo);
        } else {
            List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
            searchVo.setUserGroup(userGroup);
            //로그인 한 계정의 소속 사업장 comp_id 삽입
            String affCompId = mainMenuDao.getAffCompIdByUserId(searchVo);
            searchVo.setCompId(affCompId);
            voList = mainMenuDao.search(searchVo);
        }
        return voList;
    }

    public ArrayNode treeMenu(SpSearchVO searchVo) throws Exception {
        List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
        searchVo.setUserGroup(userGroup);
        //로그인 한 계정의 소속 사업장 comp_id 삽입
        String affCompId = mainMenuDao.getAffCompIdByUserId(searchVo);
        searchVo.setCompId(affCompId);
        List<MainMenuVO> voList = mainMenuDao.search(searchVo);
        System.out.println("값 --> " + voList);
        ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
        return tree;
    }

    public ArrayNode treeMenuFavorites(SpSearchVO searchVo) throws Exception {
        List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
        searchVo.setUserGroup(userGroup);
        //로그인 한 계정의 소속 사업장 comp_id 삽입
        String affCompId = mainMenuDao.getAffCompIdByUserId(searchVo);
        searchVo.setCompId(affCompId);
        String affHrId = mainMenuDao.getAffHrIdByUserId(searchVo);
        searchVo.setHrId(affHrId);
        List<MainMenuVO> voList = mainMenuDao.searchFavorites(searchVo);
        System.out.println("값 --> " + voList);
        ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
        return tree;
    }

    public List<MainMenuVO> searchAdmin(SpSearchVO searchVo) throws Exception {
        List<MainMenuVO> voList = mainMenuDao.searchAdminOrderByName(searchVo);
        return voList;
    }

    public ArrayNode treeMenuAdmin(SpSearchVO searchVo) throws Exception {
        List<MainMenuVO> voList = mainMenuDao.searchAdmin(searchVo);
        ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
        return tree;
    }

    public BaseVO findOne(String menuId) throws Exception {
        MainMenuVO vo = mainMenuDao.findOne(menuId);
        return vo;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return mainMenuDao.searchCount(searchVo);
    }


}
