//package kr.co.igns.framework.api.menu.entity;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//
//public interface AuthGroupMapRepository extends JpaRepository<AuthGroupMapEntity, Long>, JpaSpecificationExecutor<AuthGroupMapEntity>{
//	
//	@Query(value="SELECT" 
//			+ " t1.grp_auth_cd," 
//			+ " t3.grp_auth_nm," 
//			+ " t2.menu_id,"
//			+ " t2.menu_nm,"
//			+ " 'N' AS auth" 
//			+ " FROM tb_auth_group_map_m t1" 
//			+ " JOIN tb_menu_info t2 ON (t2.menu_id = t1.menu_id AND t2.parent_id = :menu_id)" 
//			+ " JOIN tb_auth_group t3 ON (t1.grp_auth_cd = t3.grp_auth_cd)"
//			+ " WHERE t1.grp_auth_cd = :grp_auth_cd"
//			+ "" , nativeQuery=true)
//	List<AuthGroupMapEntity> findByChildByGrpAuthCd(@Param("grp_auth_cd") String grpAuthCd , @Param("menu_id") int menuId);
//
//	@Query(value="DELETE FROM tb_auth_group_map_m t1"
//			+ " WHERE t1.grp_auth_cd = :grp_auth_cd"
//			+ " AND t1.menu_id = :menu_id"
//			+ "" , nativeQuery=true)
//	int deleteAuthMenu(@Param("grp_auth_cd") String grpAuthCd, @Param("menu_id") int menuId);
//}
