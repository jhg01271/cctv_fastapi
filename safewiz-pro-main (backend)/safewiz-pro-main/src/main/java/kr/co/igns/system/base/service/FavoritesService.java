package kr.co.igns.system.base.service;


import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.dao.postgres.FavoritesDAO;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.base.model.FavoritesVO;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.base.model.WpVO;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;
import kr.co.igns.system.setting.model.MainMenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritesService {
    private final FavoritesDAO favoritesDao;
    private final MainMenuDAO mainMenuDao;


    public List<FavoritesVO> getFavoritesList(FavoritesVO vo) throws Exception {
        List<FavoritesVO> voList = favoritesDao.getFavoritesList(vo);
        return voList;
    }

    public ArrayNode getFavoritesSide(SpSearchVO searchVo) throws Exception {
        List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
        searchVo.setUserGroup(userGroup);
        String compId = SecurityUtil.getCurrentCompId();
        searchVo.setCompId(compId);
        String affHrId = mainMenuDao.getAffHrIdByUserId(searchVo);
        searchVo.setHrId(affHrId);

        List<FavoritesVO> voList = favoritesDao.getFavoritesSide(searchVo);

        // searchCd ADD >> 개별 추가시 (불러오기 X)
        if (voList.isEmpty() && "ADD".equals(searchVo.getSearchCd())) {
            FavoritesVO newFolder = new FavoritesVO();
            newFolder.setCompId(compId);
            newFolder.setHrId(affHrId);
            newFolder.setMenuNm("나의 즐겨찾기");
            newFolder.setUseYn("Y");
            newFolder.setNewYn("N");
            String id = favoritesDao.getMenuId(newFolder);
            Integer odr = favoritesDao.getMenuOdr(newFolder);
            newFolder.setMenuId(id);
            newFolder.setMenuOdr(odr);
            favoritesDao.insertFavoritesMenu(newFolder);
            voList = favoritesDao.getFavoritesSide(searchVo);
        }

        ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
        return tree;
    }

    @Transactional(rollbackFor = Throwable.class)
    public FavoritesVO addFavoritesFolder(SpSearchVO searchVo) throws Exception {

        FavoritesVO favoritesVO = new FavoritesVO();

        String compId = SecurityUtil.getCurrentCompId();
        favoritesVO.setCompId(compId);
        String affHrId = mainMenuDao.getAffHrIdByUserId(searchVo);
        favoritesVO.setHrId(affHrId);
        String newMenuId = favoritesDao.getMenuId(favoritesVO);
        favoritesVO.setMenuId(newMenuId);
        favoritesVO.setMenuNm("새폴더");
        favoritesVO.setUseYn("Y");
        favoritesVO.setNewYn("Y");
        Integer odr = favoritesDao.getMenuOdr(favoritesVO);
        favoritesVO.setMenuOdr(odr);

        favoritesDao.insertFavoritesMenu(favoritesVO);

        return favoritesVO;
    }

    @Transactional(rollbackFor = Throwable.class)
    public FavoritesVO addFavoritesMenu(List<Object> dataVo) throws Exception {

        Map<String, Object> masterDataVo = new HashMap<>();
        masterDataVo = (Map<String, Object>) dataVo.get(0);

        FavoritesVO parentsFavoritesVO = new FavoritesVO();
        parentsFavoritesVO.setMenuId((String) masterDataVo.get("parentsMenuId"));
        parentsFavoritesVO.setMenuNm((String) masterDataVo.get("parentsMenuNm"));
        parentsFavoritesVO.setLoginId(SecurityUtil.getCurrentMemberId());
        String compId = SecurityUtil.getCurrentCompId();
        parentsFavoritesVO.setCompId(compId);
        String affHrId = favoritesDao.getAffHrIdByUserId(parentsFavoritesVO);
        parentsFavoritesVO.setHrId(affHrId);

        //폴더 확인
        Integer cnt = favoritesDao.checkFavoritesMenuFolder(parentsFavoritesVO);

        if (cnt == 0){
           Integer odr = favoritesDao.getMenuOdr(parentsFavoritesVO);
           parentsFavoritesVO.setMenuOdr(odr);
           favoritesDao.insertFavoritesMenu(parentsFavoritesVO);
        }

        FavoritesVO favoritesVO = new FavoritesVO();

        favoritesVO.setMenuId((String) masterDataVo.get("menuId"));
        favoritesVO.setMenuNm((String) masterDataVo.get("menuNm"));
        favoritesVO.setUpMenuId((String) masterDataVo.get("parentsMenuId"));
        favoritesVO.setUseYn((String) masterDataVo.get("useYn"));
        favoritesVO.setNewYn((String) masterDataVo.get("newYn"));
        favoritesVO.setFaveId((String) masterDataVo.get("faveId"));
        Integer odr = favoritesDao.getMenuOdr(favoritesVO);
        favoritesVO.setMenuOdr(odr);
        favoritesVO.setCompId(compId);
        favoritesVO.setHrId(affHrId);

        favoritesDao.insertFavoritesMenu(favoritesVO);

        return favoritesVO;
    }

    @Transactional(rollbackFor = Throwable.class)
    public FavoritesVO updateFavoritesFolder(List<Object> dataVo) throws Exception {

        Map<String, Object> masterDataVo = new HashMap<>();
        masterDataVo = (Map<String, Object>) dataVo.get(0);

        FavoritesVO favoritesVO = new FavoritesVO();
        favoritesVO.setMenuId((String) masterDataVo.get("menuId"));
        favoritesVO.setMenuNm((String) masterDataVo.get("menuNm"));
        favoritesVO.setMenuOdr((Integer) masterDataVo.get("menuOdr"));
        favoritesVO.setFaveId((String) masterDataVo.get("faveId"));
        favoritesVO.setLoginId((String) masterDataVo.get("loginId"));
        String compId = SecurityUtil.getCurrentCompId();
        favoritesVO.setCompId(compId);
        String affHrId = favoritesDao.getAffHrIdByUserId(favoritesVO);
        favoritesVO.setHrId(affHrId);
        favoritesVO.setNewYn("N");
        favoritesVO.setUseYn("Y");

        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setCompId(compId);
        searchVo.setHrId(affHrId);

        Integer cnt = favoritesDao.checkFavoritesMenu(favoritesVO);

        if(cnt > 0){
            favoritesDao.updateFavoritesMenu(favoritesVO);
        }else{
            Integer odr = favoritesDao.getMenuOdr(favoritesVO);
            favoritesVO.setMenuOdr(odr);
            favoritesDao.insertFavoritesMenu(favoritesVO);
        }

        return favoritesVO;
    }

    @Transactional(rollbackFor = Throwable.class)
    public FavoritesVO updateFavoritesOdr(List<Object> dataVo) throws Exception {
        String compId = SecurityUtil.getCurrentCompId();

        for ( Object obj : dataVo){
            Map<String, Object> masterDataVo = new HashMap<>();
            masterDataVo = (Map<String, Object>) obj;

            FavoritesVO favoritesVO = new FavoritesVO();
            favoritesVO.setMenuId((String) masterDataVo.get("menuId"));
            favoritesVO.setUpMenuId((String) masterDataVo.get("upMenuId"));
            favoritesVO.setMenuNm((String) masterDataVo.get("menuNm"));
            favoritesVO.setMenuOdr((Integer) masterDataVo.get("index"));
            favoritesVO.setLoginId((String) masterDataVo.get("loginId"));
            favoritesVO.setCompId(compId);
            String affHrId = favoritesDao.getAffHrIdByUserId(favoritesVO);
            favoritesVO.setHrId(affHrId);
            favoritesVO.setNewYn("N");
            favoritesVO.setUseYn((String) masterDataVo.get("useYn"));
            favoritesDao.updateFavoritesMenu(favoritesVO);
        }
        return null;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteFavoritesMenu(List<FavoritesVO> dataVo) throws Exception {
        favoritesDao.deleteFavoritesMenu(dataVo.get(0));
    }

    public ArrayNode exampleMenu(SpSearchVO searchVo) throws Exception {
        List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
        searchVo.setUserGroup(userGroup);
        //로그인 한 계정의 소속 사업장 comp_id 삽입
        String affCompId = mainMenuDao.getAffCompIdByUserId(searchVo);
        searchVo.setCompId(affCompId);
        List<FavoritesVO> voList = favoritesDao.getExampleSearch(searchVo);
        System.out.println("값 --> " + voList);
        ArrayNode tree = SpUtils.setNodeTree(voList, "menuId", "upMenuId");
        return tree;
    }

    @Transactional(rollbackFor = Throwable.class)
    public SpSearchVO saveFavoritesExample(List<FavoritesVO> voList) throws Exception {
        SpSearchVO searchVo = new SpSearchVO();
        searchVo.setCreatedBy(SecurityUtil.getCurrentMemberId());
        List<String> userGroup = mainMenuDao.findUserGroup(searchVo);
        searchVo.setUserGroup(userGroup);
        String compId = SecurityUtil.getCurrentCompId();
        searchVo.setCompId(compId);

        List<FavoritesVO> authExamples = favoritesDao.getExampleSearch(searchVo);

        String hrId = SecurityUtil.getCurrentHrId();
        for (FavoritesVO vo : voList) {
            authExamples.stream()
                    .filter(exam -> exam.getMenuId().equals(vo.getMenuId()))
                    .findFirst()
                    .ifPresent(exam -> {
                        exam.setHrId(hrId);
                        exam.setCompId(vo.getCompId());
                        exam.setUseYn("Y");
                        exam.setNewYn("N");
                        favoritesDao.insertFavoritesMenu(exam);
                        vo.getChildren().forEach(examChild -> {
                            examChild.setHrId(hrId);
                            examChild.setCompId(vo.getCompId());
                            examChild.setUseYn("Y");
                            examChild.setNewYn("N");
                            favoritesDao.insertFavoritesMenu(examChild);
                        });
                    });
        }
        return searchVo;
    }

}
