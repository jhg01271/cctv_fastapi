package kr.co.igns.framework.comm.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;
import kr.co.igns.system.setting.model.MainMenuVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;

import kr.co.igns.framework.comm.model.FcmMessageVO;
import kr.co.igns.system.user.dao.postgres.UserDAO;
import lombok.RequiredArgsConstructor;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ 프로젝트       : ESG
 * @ 패키지          : kr.co.igns.framework.comm.service
 * @ 파일명          : FcmService.java
 * @ 기능명          :
 * @ 작성자          : 소민환
 * @ 최초생성일     :
 * @ 프로그램 설명  :
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경내용 - 수정자)
 *    [2024-01-31] fcm http -> http v1 마이그레이션 - 소민환
 */

@RequiredArgsConstructor
@Service
@PropertySource("classpath:global.properties")
public class FcmService {
	private static final Logger log = LogManager.getLogger("com.project");
	private final WebClientService webClientService;
	private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
	@Value("${fcm.apiurl}")
	private String FCM_API_URL;
	@Value("classpath:firebase/firebase_service_key.json")
	private Resource resource;
	@Value("${spring.config.activate.on-profile}")
	private String profiles;
	private final UserDAO userDao;
	private final MainMenuDAO mainMenuDao;

	public void sendFcmToAllDevice(FcmMessageVO vo) throws IOException {
//		if (profiles.equals("dev")) {
//            System.out.println("fcm 중단");
//            return;  // dev 프로필일 경우 메소드 실행 중단
//        }
		String message = "";

		if(vo.getParams() != null)  {
			message = multiPlatforms(vo.getTitle(), vo.getContent(), vo.getRouterNm(), vo.getParams(), vo.getTopic());
		} else {
			message = multiPlatforms(vo.getTitle(), vo.getContent(), vo.getRouterNm(), null, vo.getTopic());
		}
		log.info("메시지 양식 --> " + message);
		webClientService.webClientPostTopicApi(
				FCM_API_URL,
				getAccessToken(),
				message
		);
		vo.setTargetUserId("ALL");
	}

	// 구글 사용자 인증 정보를 이용해 AccessToken 발급
	private String getAccessToken() throws IOException {

//		if (profiles.equals("dev")) {
//			System.out.println("fcm 중단");
//            return null;  // dev 프로필일 경우 메소드 실행 중단
//        }

		InputStream serviceAccount = resource.getInputStream();

		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
				.createScoped(Arrays.asList(MESSAGING_SCOPE));

		credentials.refresh();
		AccessToken accessToken = credentials.getAccessToken();

		String token = accessToken.getTokenValue();
		return token;
	}

	public void sendFcmToSpecificDevice(FcmMessageVO vo) throws Exception {
		List<String> targetUserIdList = vo.getTargetUserIdList();

		if(targetUserIdList != null && !targetUserIdList.isEmpty()) {
			for (String userId : targetUserIdList) {
				String fcmTokenByUserID = userDao.getTokenByUserId(userId); //유저 ID로 FCM_TOKEN 가져옴
				if(fcmTokenByUserID == null)
					continue;
				log.info("userId : " + userId + " / " + "사용자에 대한 fcm token : " + fcmTokenByUserID);
				//메세지 구성
				String message = "";
				if(vo.getParams() != null)  {
					message = singlePlatforms(vo.getTitle(), vo.getContent(), vo.getRouterNm(), vo.getParams(), fcmTokenByUserID);
				} else {
					message = singlePlatforms(vo.getTitle(), vo.getContent(), vo.getRouterNm(), null, fcmTokenByUserID);
				}
				log.info("메시지 양식 --> " + message);
				// WebClient를 사용하여 FCM 서버에 메시지를 전송.
				webClientService.webClientPostTokenApi(
						FCM_API_URL,
						getAccessToken(),
						userId,
						message
				);
			}
		}
	}

	// 다중 플랫폼 타겟팅
	public String multiPlatforms(String title, String content, String alarmType, Map<String, Object> params, String topic) {
		//payload에 notification 항목에 데이터를 보낼 경우, 자동으로 알림 객체를 감지하여 알림 메세지를 기본으로 생성해서
		//중복으로 웹 푸시 알람이 전송되기에 notification 주석 처리 후 data 객체에 title, body 구성
		JsonObject message = new JsonObject();
		//JsonObject notification = new JsonObject();
		JsonObject data = new JsonObject();
		JsonObject android = new JsonObject();
		JsonObject androidNotification = new JsonObject();
		JsonObject apns = new JsonObject();
		JsonObject apnsPayload = new JsonObject();
		JsonObject aps = new JsonObject();
		// "notification" 객체 구성
		//notification.addProperty("title", title);
		//notification.addProperty("body", content);
		// "data" 객체 구성
		data.addProperty("alarmType", alarmType);
		data.addProperty("title", title);
		data.addProperty("body", content);
		// "androidNotification" 객체 구성
		androidNotification.addProperty("click_action", "FLUTTER_NOTIFICATION_CLICK");
		// "notification" 객체와 "data" 객체를 "android" 객체에 추가
		android.add("notification", androidNotification);
		// "apns" 객체 구성
		aps.addProperty("category", "NEW_MESSAGE_CATEGORY");
		apnsPayload.add("aps", aps);
		apns.add("payload", apnsPayload);
		// "message" 객체에 "topic" 속성 추가
		message.addProperty("topic", topic);
		// 최종 "message" 객체 구성
		//message.add("notification", notification);
		message.add("data", data);
		message.add("android", android);
		message.add("apns", apns);
		// 최종 JSON 구성
		JsonObject messageWrapper = new JsonObject();
		messageWrapper.add("message", message);
		return messageWrapper.toString();
		/* 다중 플랫폼 타겟팅(targeting multiple platforms) 양식
		 *	{
		 *	  "message":{
		 *	    "topic":"",
		 *	    "notification":{
		 *	      "title":"주제",
		 *	      "body":"내용"
		 *	    },
		 *	    "data":{
		 *	      "alarmType":"",
		 *	    },
		 *	    "android":{
		 *	      "notification":{
		 * 	        "click_action":"NOTIFICATION_CLICK"
		 *	      }
		 *	    },
		 *      "apns": {
		 *		  "payload": {
		 *		    "aps": {
		 *               "category" : "NEW_MESSAGE_CATEGORY"
		 *          }
		 *        }
		 *      }
		 *	  }
		 * }
		 */
	}
	// 특정 기기 타겟팅
	public String singlePlatforms(String title, String content, String alarmType, Map<String, Object> params, String fcmTokenByUserID) {
		JsonObject message = new JsonObject();
		//payload에 notification 항목에 데이터를 보낼 경우, 자동으로 알림 객체를 감지하여 알림 메세지를 기본으로 생성해서
		//중복으로 웹 푸시 알람이 전송되기에 notification 주석 처리 후 data 객체에 title, body 구성
		//JsonObject notification = new JsonObject();
		JsonObject data = new JsonObject();
		JsonObject android = new JsonObject();
		JsonObject androidNotification = new JsonObject();
		JsonObject apns = new JsonObject();
		JsonObject apnsPayload = new JsonObject();
		JsonObject aps = new JsonObject();
		// "notification" 객체 구성
		//notification.addProperty("title", "");
		//notification.addProperty("body", "");
			// "data" 객체 구성
		data.addProperty("alarmType", alarmType);
		data.addProperty("title", title);
		data.addProperty("body", content);
		if(params != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			String json;
			try {
				json = objectMapper.writeValueAsString(params);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			data.addProperty("params", json);
		}
		// "androidNotification" 객체 구성
		androidNotification.addProperty("click_action", "FLUTTER_NOTIFICATION_CLICK");
		// "notification" 객체와 "data" 객체를 "android" 객체에 추가
		android.add("notification", androidNotification);
		// "apns" 객체 구성
		aps.addProperty("category", "NEW_MESSAGE_CATEGORY");
		apnsPayload.add("aps", aps);
		apns.add("payload", apnsPayload);
		// "message" 객체에 "topic" 속성 추가
		message.addProperty("token", fcmTokenByUserID);
		// 최종 "message" 객체 구성
		//message.add("notification", notification);
		message.add("data", data);
		message.add("android", android);
		message.add("apns", apns);
		// 최종 JSON 구성
		JsonObject messageWrapper = new JsonObject();
		messageWrapper.add("message", message);
		return messageWrapper.toString();
		/* 특정 기기 타겟팅(targeting specific devices) 양식
		 *	{
		 *	  "message":{
		 *	    "token":"",
		 *	    "notification":{
		 *	      "title":"주제",
		 *	      "body":"내용"
		 *	    },
		 *	    "data":{
		 *	      "alarmType":""
		 *	    },
		 *	    "android":{
		 *	      "notification":{
		 * 	        "click_action":"NOTIFICATION_CLICK"
		 *	      }
		 *	    },
		 *      "apns": {
		 *		  "payload": {
		 *		    "aps": {
		 *               "category" : "NEW_MESSAGE_CATEGORY"
		 *          }
		 *        }
		 *      }
		 *	  }
		 *	}
		 */
	}

	public String getRouterNm(String menuId) throws IOException {
		MainMenuVO menuVO = mainMenuDao.findOne(menuId);
		String result = menuVO.getRouterNm();
		return result;
	}
}
