//package kr.co.igns.framework.api.notification.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import kr.co.igns.framework.api.notification.dto.NotificationRequestDto;
//import kr.co.igns.framework.api.notification.dto.NotificationResponseDto;
//import kr.co.igns.framework.api.notification.entity.NotificationEntity;
//import kr.co.igns.framework.api.notification.entity.NotificationRepository;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//	private final NotificationRepository notificationRepository;
//
//	@Transactional
//	public void saveNotification(String userId, NotificationRequestDto param) {
//
//		List<NotificationEntity> insertList = new ArrayList<NotificationEntity>();
//		List<NotificationEntity> deleteList = new ArrayList<NotificationEntity>();
//		List<NotificationEntity> entites = notificationRepository.findByUserId(userId);
//		Map<String, Boolean> map = param.getNotification();
//		for (NotificationEntity obj : entites) {
//			String key = obj.getMethod();
//			Boolean value = obj.getAgreed();
//
//			if (map.containsKey(key)) {
//				if (map.get(key).equals(value)) {
//				} else {
//					deleteList.add(obj);
//				}
//			} else {
//				deleteList.add(obj);
//			}
//		}
//		for (Map.Entry<String, Boolean> entry : map.entrySet()) {
//			String key = entry.getKey();
//			boolean found = false;
//			for (NotificationEntity obj : entites) {
//				if (obj.getMethod().equals(key)) {
//					found = true;
//					break;
//				}
//			}
//			if (!found) {
//				NotificationEntity tmp = NotificationEntity.builder().userId(userId).method(key)
//						.agreed(param.getNotification().get(key)).build();
//				insertList.add(tmp);
//			}
//		}
//		notificationRepository.saveAll(insertList);
//		notificationRepository.deleteAll(deleteList);
//	}
//
//	public List<NotificationResponseDto> getNotification(String userId) {
//		List<NotificationResponseDto> resultList = new ArrayList<NotificationResponseDto>();
//		List<NotificationEntity> entity = notificationRepository.findByUserId(userId);
//		for (NotificationEntity tmp : entity) {
//			NotificationResponseDto dto = new NotificationResponseDto(tmp);
//			resultList.add(dto);
//		}
//		return resultList;
//	}
//
//}
