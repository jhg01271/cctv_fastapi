package kr.co.igns.system.base.dao.postgres;

import kr.co.igns.system.base.model.FavoritesVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.MainMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FavoritesDAO {
    List<FavoritesVO> getFavoritesList(FavoritesVO vo);
    List<FavoritesVO> getFavoritesSide(SpSearchVO searchVo);
    List<String> findUserGroup(SpSearchVO searchVo);
    List<FavoritesVO> getExampleSearch(SpSearchVO searchVo);

    Integer checkFavoritesMenuFolder(FavoritesVO vo);
    Integer checkFavoritesMenu(FavoritesVO vo);

    void insertFavoritesMenu(FavoritesVO vo);
    void updateFavoritesMenu(FavoritesVO vo);
    void deleteFavoritesMenu(FavoritesVO vo);

    void insertFavoritesExample(SpSearchVO searchVo);
    void deleteFavoritesExample(SpSearchVO searchVo);

    String getAffCompIdByUserId(FavoritesVO vo);
    String getAffHrIdByUserId(FavoritesVO vo);
    String getMenuId(FavoritesVO vo);
    Integer getMenuOdr(FavoritesVO vo);

}
