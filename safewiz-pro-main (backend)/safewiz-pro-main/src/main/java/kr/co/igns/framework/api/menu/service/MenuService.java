//package kr.co.igns.framework.api.menu.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import kr.co.igns.framework.api.menu.dto.MenuTreeResponstDto;
//import kr.co.igns.framework.api.menu.entity.MenuRepository;
//import kr.co.igns.framework.api.menu.inf.MenuInterface;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class MenuService {
//
//	private final MenuRepository menuRepository;
//
//	public Map<String, MenuTreeResponstDto> getMenuByAuth(String userId, String svcGubn) {
//
//		List<MenuInterface> entity = menuRepository.getMenu();
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
//
//	}
//
//	public List<MenuTreeResponstDto> getMenuByParam(String type, String search) {
//		List<MenuInterface> menus = menuRepository.findMenuByParam(search);
//		List<MenuTreeResponstDto> menuList = new ArrayList<MenuTreeResponstDto>();
//		for (MenuInterface menu : menus) {
//			if (type.equals("ALL")||type.equals(menu.getSvcType())) {
//				MenuTreeResponstDto menuItem = new MenuTreeResponstDto();
//				menuItem.setMenuId(Integer.toString(menu.getMenuId()));
//				menuItem.setMenuIcon(menu.getMenuIcon());
//				menuItem.setMenuNm(menu.getMenuNm());
//				menuItem.setMenuEngNm(menu.getMenuEngNm());
//				menuItem.setSort(menu.getSort());
//				menuItem.setRouterName(menu.getRouterName());
//				menuItem.setRouterPath(menu.getRouterPath());
//				menuItem.setSvcType(menu.getSvcType());
//				menuItem.setParentId(menu.getParentId());
//				menuItem.setLvl(menu.getLvl());
//				menuList.add(menuItem);
//			}
//
//		}
//		return menuList;
//
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
//}
