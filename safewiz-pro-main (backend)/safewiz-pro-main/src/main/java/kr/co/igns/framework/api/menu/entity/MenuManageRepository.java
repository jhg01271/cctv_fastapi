//package kr.co.igns.framework.api.menu.entity;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface MenuManageRepository extends JpaRepository<MenuEntity, Long> {
//
//	List<MenuEntity> findByMenuId(int menuId);
//
//	@Query(value = "SELECT "
//			+ " MAX(menu_id)"
//			+ " FROM "
//			+ " ("
//			+ " SELECT"
//			+ " menu_id +1 AS menu_id"
//			+ " FROM tb_menu_info tmi"
//			+ " WHERE CAST(menu_id AS text) LIKE (:lvl + 1)||'%'"
//			+ " UNION ALL"
//			+ " SELECT (:lvl+1)*1000 +1 AS test"
//			+ " ) AS tb" +
//			"", nativeQuery = true)
//	int findNewMenuId(@Param("lvl") int lvl);
//
//}
