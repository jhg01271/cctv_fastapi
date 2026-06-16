//package kr.co.igns.framework.api.notification.controller;
//
//import java.util.HashMap;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.framework.api.notification.dto.NotificationRequestDto;
//import kr.co.igns.framework.api.notification.dto.NotificationResponseDto;
//import kr.co.igns.framework.api.notification.service.NotificationService;
//import kr.co.igns.framework.config.message.MessageService;
//import kr.co.igns.framework.config.response.ListResponseDto;
//import kr.co.igns.framework.config.response.ResponseUtil;
//import kr.co.igns.framework.config.response.SingleResponseDto;
//import lombok.RequiredArgsConstructor;
//
//@Tag(name = "Notification", description = "알림 설정")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/igns/common")
//public class NotificationController {
//
//	private final MessageService messageService;
//	private final NotificationService notificationService;
//
//	@Operation(summary = "알람수신 동의", description = "알람수신항목을 저장한다.")
//	@PutMapping("/notification")
//	public ResponseEntity<SingleResponseDto<String>> saveNotification(HttpServletRequest request, @RequestBody NotificationRequestDto param) {
//		String userId = request.getAttribute("userId").toString();
//		notificationService.saveNotification(userId, param);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("updateSuccess.code"), messageService.getMessage("updateSuccess.msg"), null);
//	}
//
//	@Operation(summary = "알람수신목록 조회", description = "알람수신목록을 조회한다.")
//	@GetMapping("/notification")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getNotification(HttpServletRequest request) {
//		String userId = request.getAttribute("userId").toString();
//		List<NotificationResponseDto> res = notificationService.getNotification(userId);
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("list", result);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//	}
//
//}
