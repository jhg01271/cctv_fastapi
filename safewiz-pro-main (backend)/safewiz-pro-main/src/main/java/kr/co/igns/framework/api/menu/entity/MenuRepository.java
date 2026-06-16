//package kr.co.igns.framework.api.menu.entity;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import kr.co.igns.framework.api.menu.inf.MenuInterface;
//
//@Repository
//public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
//
//	@Query(value = "SELECT" +
//			" DISTINCT" +
//			" T1.menu_id menuId," +
//			" CASE WHEN tagmm.grp_auth_cd IS NULL THEN true ELSE false END auth," +
//			" tag.grp_auth_cd," +
//			" tag.grp_auth_nm," +
//			" T1.menu_nm menuNm," +
//			" T1.menu_nm_cd menuNmCd," +
//			" T1.menu_eng_nm menuEngNm," +
//			" COALESCE(T1.parent_id, 0) AS parentId," +
//			" COALESCE(T1.lvl, 0) AS lvl," +
//			" T1.sort," +
//			" COALESCE(T1.router_path, '') AS routerPath," +
//			" COALESCE(T1.router_name, '') AS routerName," +
//			" COALESCE(T1.menu_icon, '') AS menuIcon," +
//			" T1.created_at createAt," +
//			" T1.svc_gubn svcGubun," +
//			" T1.svc_type svcType," +
//			" T1.use_yn useYn," +
//			" COALESCE(T1.created_by, '') AS createdBy," +
//			" T1.updated_at updatedAt," +
//			" COALESCE(T1.updated_by, '') AS updatedBy" +
//			" FROM tb_menu_info T1" +
//			" INNER JOIN tb_auth_group_map_m tagmm ON T1.menu_id = tagmm.menu_id AND tagmm.grp_auth_cd IN ('ADMIN', 'USER', 'ASSET')"+
//			" LEFT JOIN tb_auth_group tag ON tag.grp_auth_cd = tagmm.grp_auth_cd" +
//			" WHERE T1.use_yn ='Y'" +
//			" ORDER BY lvl ASC", nativeQuery = true)
//	List<MenuInterface> getMenu();
//
//	@Query(value ="SELECT"
//			+ " menu_id menuId,"
//			+ " menu_nm menuNm,"
//			+ " svc_type svcType,"
//			+ " menu_eng_nm menuEngNm,"
//			+ " parent_id parentId, "
//			+ " sort,"
//			+ " router_path routerPath,"
//			+ " router_name RouterName,"
//			+ " menu_icon menuIcon,"
//			+ " created_at createdAt,"
//			+ " created_by createdBy,"
//			+ " updated_at updatedAt,"
//			+ " updated_by updatedBy,"
//			+ " lvl"
//			+ " FROM tb_menu_info tmi "
//			+ " WHERE tmi.parent_id IN"
//			+ " ("
//			+ " SELECT menu_id FROM tb_menu_info"
//			+ " WHERE lvl=0 "
//			+ " AND menu_nm LIKE CASE WHEN :search != '' THEN CONCAT('%',:search , '%') ELSE '%%' END"
//			+ " ) "
//			+ " OR lvl=0"
//			+ " AND tmi.menu_nm LIKE CASE WHEN :search != '' THEN CONCAT('%',:search , '%') ELSE '%%' END"
//			+ " ORDER BY lvl ASC" , nativeQuery = true)
//	List<MenuInterface> findMenuByParam(@Param("search") String search);
//
//}
