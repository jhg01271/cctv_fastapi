//package kr.co.igns.framework.api.menu.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import kr.co.igns.framework.api.menu.dto.MenuRequestDto;
//import kr.co.igns.framework.api.menu.dto.MenuResponseDto;
//import kr.co.igns.framework.api.menu.entity.MenuEntity;
//import kr.co.igns.framework.api.menu.entity.MenuManageRepository;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class MenuManageService {
//
//	private final MenuManageRepository menuManageRepository;
//
//	// 1. SELECT
//	public List<MenuResponseDto> getMenuManage() {
//		List<MenuResponseDto> resultList = new ArrayList<MenuResponseDto>();
//		List<MenuEntity> entity = menuManageRepository.findAll();
//		for (MenuEntity p : entity) {
//			MenuResponseDto dto = new MenuResponseDto(p);
//			resultList.add(dto);
//		}
//		return resultList;
//
//	}
//
//	// 2. INSERT 및 UPDATE
//	// 기존 만들어진 메뉴가 부모의 아이디가 될 경우는 newParentId가 -1.
//	public int saveMenuManage(String userId, List<MenuRequestDto> param, int newParentId) {
//		for (MenuRequestDto dto : param) {
//			// 부모가 신규라면
//			if (newParentId != -1) {
//				dto.setParentId(newParentId);
//			}
//			// 신규 메뉴면
//			if (dto.is__created__()) {
//				int menuId = menuManageRepository.findNewMenuId(dto.getLvl());
//				dto.setMenuId(menuId);
//				dto.setCreatedBy(userId);
//				menuManageRepository.save(dto.toEntity());
//				if (!dto.getItems().isEmpty()) {
//					newParentId = menuId;
//					saveMenuManage(userId, dto.getItems(), newParentId);
//				}
//			} else {
//				dto.setUpdatedBy(userId);
//				menuManageRepository.save(dto.toEntity());
//				// 자식있다면
//				if (!dto.getItems().isEmpty()) {
//					saveMenuManage(userId, dto.getItems(), -1);
//				}
//			}
//		}
//		return param.size();
//	}
//
//	// DELETE
//	public void deleteMenuManage(List<MenuRequestDto> param) {
//		for (MenuRequestDto dto : param) {
//			List<MenuEntity> childList = menuManageRepository.findByMenuId(dto.getMenuId());
//			if (!childList.isEmpty()) {
//				ModelMapper modelMapper = new ModelMapper();
//				childList.stream().map(child -> modelMapper.map(child, MenuRequestDto.class))
//						.collect(Collectors.toList());
//			}
//			menuManageRepository.delete(dto.toEntity());
//		}
//
//	}
//
//}
