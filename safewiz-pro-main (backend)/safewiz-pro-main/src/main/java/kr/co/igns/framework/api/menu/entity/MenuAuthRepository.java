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
//public interface MenuAuthRepository extends JpaRepository<MenuEntity, Long> {
//
//	@Query(value = "SELECT" +
//			" DISTINCT" +
//			" T1.menu_id menuId," +
//			" CASE WHEN tagmm.grp_auth_cd IS NULL THEN false ELSE true END auth," +
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
//			" LEFT JOIN tb_auth_group_map_m tagmm ON T1.menu_id = tagmm.menu_id AND tagmm.grp_auth_cd = UPPER(:grp_auth_cd)" +
//			" LEFT JOIN tb_auth_group tag ON tag.grp_auth_cd = tagmm.grp_auth_cd" +
//			" WHERE T1.svc_gubn =UPPER(:svc_gubn)" +
//			" AND T1.use_yn ='Y'" +
//			" ORDER BY lvl ASC", nativeQuery = true)
//	List<MenuInterface> listMenuOfgroupManageAll(@Param("grp_auth_cd") String grpAuthCd, @Param("svc_gubn") String svcGubn);
//
//	@Query(value = "SELECT" +
//			" DISTINCT" +
//			" T1.menu_id menuId," +
//			" CASE WHEN tagmm.grp_auth_cd IS NULL THEN false ELSE true END auth," +
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
//			" INNER JOIN tb_auth_group_map_m tagmm ON T1.menu_id = tagmm.menu_id AND tagmm.grp_auth_cd = UPPER(:grp_auth_cd)" +
//			" LEFT JOIN tb_auth_group tag ON tag.grp_auth_cd = tagmm.grp_auth_cd" +
//			" WHERE T1.svc_gubn =UPPER(:svc_gubn)" +
//			" AND T1.use_yn ='Y'" +
//			" ORDER BY lvl ASC", nativeQuery = true)
//	List<MenuInterface> listMenuOfgroupManage(@Param("grp_auth_cd") String grpAuthCd, @Param("svc_gubn") String svcGubn);
//
//}
