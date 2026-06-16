//package kr.co.igns.framework.api.menu.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import kr.co.igns.framework.api.menu.dto.MenuAuthRequestDto;
//import kr.co.igns.framework.api.menu.dto.MenuTreeResponstDto;
//import kr.co.igns.framework.api.menu.entity.AuthGroupMapRepository;
//import kr.co.igns.framework.api.menu.entity.MenuAuthRepository;
//import kr.co.igns.framework.api.menu.inf.MenuInterface;
//import kr.co.igns.framework.config.exception.CDataAccessException;
//import kr.co.igns.framework.config.exception.CIndexBoundException;
//import kr.co.igns.framework.config.exception.CNotFoundException;
//import kr.co.igns.framework.config.exception.CNullPointException;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class MenuAuthService {
//
//	private final MenuAuthRepository menuAuthRepository;
//	private final AuthGroupMapRepository authGroupMapRepository;
//
//	public Map<String, MenuTreeResponstDto> listMenuOfgroupManage(MenuAuthRequestDto dto) {
//
//		List<MenuInterface> entity = new ArrayList<MenuInterface>();
//		if(dto.getIsAllMenu()) {
//			entity = menuAuthRepository.listMenuOfgroupManageAll(dto.getGrpAuthCd(), dto.getSvcGubn());
//		}else {
//			entity = menuAuthRepository.listMenuOfgroupManage(dto.getGrpAuthCd(), dto.getSvcGubn());
//		}
//
//		Map<String, MenuTreeResponstDto> menus = new HashMap<String, MenuTreeResponstDto>();
//		int lvl = 0;
//		int iCompareCnt = (int) entity.stream().filter(m -> m.getLvl() == 0).count();
//		while (iCompareCnt > 0 && lvl < 5) {
//			for (MenuInterface menu : entity) {
//				if (menu.getLvl() == lvl) {
//					MenuTreeResponstDto menuItem = new MenuTreeResponstDto();
//					menuItem.setMenuId(Integer.toString(menu.getMenuId()));
//					menuItem.setMenuIcon(menu.getMenuIcon());
//					menuItem.setMenuNm(menu.getMenuNm());
//					menuItem.setMenuNmCd(menu.getMenuNmCd());
//					menuItem.setMenuEngNm(menu.getMenuEngNm());
//					menuItem.setSort(menu.getSort());
//					menuItem.setRouterName(menu.getRouterName());
//					menuItem.setRouterPath(menu.getRouterPath());
//					menuItem.setSvcGubun(menu.getSvcGubun());
//					menuItem.setSvcType(menu.getSvcType());
//					menuItem.setUseYn(menu.getUseYn());
//					menuItem.setParentId(menu.getParentId());
//					menuItem.setLvl(menu.getLvl());
//					menuItem.setAuth(menu.getAuth());
//
//					if (menu.getLvl() == 0 && menu.getParentId().equals("0")) {
//						menus.put(menuItem.getMenuId(), menuItem);
//					} else {
//						boolean bFindParent = false;
//						MenuTreeResponstDto parentItem = new MenuTreeResponstDto();
//						// 메뉴에서 서브아이템 대상 메뉴 정보 조회
//						List<MenuTreeResponstDto> tempMenuList = menus.values().stream()
//								.collect(Collectors.toCollection(ArrayList::new));
//
//						// top level menu
//						for (MenuTreeResponstDto topMenu : tempMenuList) {
//							parentItem = FindParentItem(topMenu, menu.getParentId());
//							if (parentItem != null) {
//								bFindParent = true;
//								break;
//							}
//						}
//						if (bFindParent == true) {
//							if (parentItem.getItems() == null) {
//								parentItem.setItems(new ArrayList<MenuTreeResponstDto>());
//							}
//							parentItem.getItems().add(menuItem);
//						}
//					}
//
//				}
//
//			}
//			lvl++;
//			int tmplvl = lvl;
//			iCompareCnt = (int) entity.stream().filter(m -> m.getLvl() == tmplvl).count();
//		}
//		return menus;
//	}
//
//	private MenuTreeResponstDto FindParentItem(MenuTreeResponstDto item, String a_sParentId) {
//		if (item.getMenuId().equals(a_sParentId) == true) {
//			return item;
//		} else {
//			List<MenuTreeResponstDto> subItems = item.getItems();
//			if (subItems != null && subItems.size() > 0) {
//				for (MenuTreeResponstDto vo : subItems) {
//					MenuTreeResponstDto parentItem = null;
//					parentItem = FindParentItem(vo, a_sParentId);
//					if (parentItem != null)
//						return parentItem;
//				}
//			}
//			return null;
//		}
//	}
//
//	@Transactional
//	public void saveMenuOfgroupManage(String userId, MenuAuthRequestDto dto) throws Exception {
//		try {
//			if(dto.getMenuIdList().length > 0) {
//				for (int menuId : dto.getMenuIdList()) {
//					dto.setMenuId(menuId);
//					if(dto.getSaveType() == 0) {
//						dto.setCreatedBy(userId);
//						authGroupMapRepository.save(dto.toEntity());
//					}else if(dto.getSaveType() == 1) {
//						authGroupMapRepository.delete(dto.toEntity());
////						현재 삭제 로직에 문제가 있음. 원인 불명
////						authGroupMapRepository.deleteAuthMenu(dto.getGrpAuthCd(), menuId);
//					}else {
//						throw new CNotFoundException();
//					}
//				}
//			}
//		}catch (NullPointerException e) {
//			throw new CNullPointException(e.getMessage());
//		}catch (IndexOutOfBoundsException e) {
//			throw new CIndexBoundException(e.getMessage());
//		}catch (DataAccessException e) {
//			throw new CDataAccessException(e.getMessage());
//		}
//	}
//
//}
