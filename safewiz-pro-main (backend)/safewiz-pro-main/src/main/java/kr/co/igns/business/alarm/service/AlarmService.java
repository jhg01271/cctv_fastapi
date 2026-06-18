package kr.co.igns.business.alarm.service;
import kr.co.igns.business.alarm.dao.postgres.AlarmDAO;
import kr.co.igns.business.alarm.model.AlarmVO;
import kr.co.igns.business.alarm.model.TrainingPlanImplAlarmVO;
import kr.co.igns.business.support.model.TrainingPlanImplVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.comm.model.FcmMessageVO;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.base.model.HrVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.MainMenuVO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.user.model.UserVO;
import kr.co.igns.business.support.dao.postgres.TrainingPlanImplDAO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.igns.system.user.service.UserService;
import kr.co.igns.framework.comm.service.FcmService;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.dao.postgres.MainMenuDAO;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 기능명 :
 * ※ 작성자 : 심지성
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 */

@Service
@RequiredArgsConstructor
public class AlarmService {
	private static final Logger log = LogManager.getLogger("com.project");
	private final AlarmDAO alarmDao;
	private final FileService fileService;
	private final UserService userService;
	private final FcmService fcmService;
	private final SystemCodeDAO systemCodeDao;
	private final MainMenuDAO mainMenuDao;
	private final TrainingPlanImplDAO trainingPlanImplDao;

	public List<AlarmVO> getAlarm() throws Throwable {
		AlarmVO vo = new AlarmVO();
		vo.setClntId(SecurityUtil.getCurrentClntId());
		vo.setUserId(SecurityUtil.getCurrentMemberId());
		List<AlarmVO> alarmVoList = new ArrayList<>();
		alarmVoList = alarmDao.getAlarmList(vo); //공지사항 알람 조회

		return alarmVoList;
	}

	//알림 팝업 열람 버튼 클릭 이벤트
	//해당 알림을 read_yn = 'Y'로 업데이트 후, 메뉴 ID로 routerNm 찾아서 반환
	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> updateReadAlarm(AlarmVO vo) throws Throwable {
		vo.setReadYn("Y");
		vo.setUserId(SecurityUtil.getCurrentMemberId());
		MainMenuVO menuVO = new MainMenuVO();

		Map<String, Object> result = new HashMap<>();
		if(vo.getMenuId() != null){
			menuVO = mainMenuDao.findOne(vo.getMenuId()); //메뉴 ID 존재할 경우 메뉴 정보 조회
			AlarmVO reqVO = new AlarmVO();
			if(menuVO.getRouterNm().equals("noticeDetail")){ //공지사항 상세
				alarmDao.updateReadNoticeAlarm(vo);
//				reqVO = alarmDao.getNoticeAlarmDetail(vo);
//				result.put("writeDt", reqVO.getWriteDt());
//				result.put("docNo", reqVO.getDocNo());
//				result.put("routerNm", menuVO.getRouterNm());
			}else if(menuVO.getRouterNm().equals("SAndHTrainingImplPlanDetail")){
				alarmDao.updateReadTrainingPlanImplAlarm(vo);
			}
		}else{ //메뉴 ID가 존재하지 않을 경우
			alarmDao.updateReadEmergencyAlarm(vo);
		}
		return result;
	}

	//비상 상황 메세지 발송
	public AlarmVO sendEmergencyMsg() throws Throwable {
		UserVO userVo = new UserVO();
		userVo.setCompId(SecurityUtil.getCurrentCompId());
		List<String> fcmUserIdList = userService.getAllTokenUser(userVo);
		AlarmVO vo = AlarmService.this.insertEmergencyAlarmHistory(fcmUserIdList);
		Map<String, Object> params = new HashMap<>();
		params.put("alarmDt", vo.getAlarmDt());
		params.put("alarmSeq", String.valueOf(vo.getAlarmSeq()));
		params.put("type", "emergency"); //백그라운드 알림 구분을 위한 타입 추가
		//웹,앱 푸시 알람

		FcmMessageVO oMsg = FcmMessageVO.builder()
				.title("[비상상황 알림]") //제목
				.targetUserIdList(fcmUserIdList) //웹,앱 푸시 알람 보낼 대상자들의 ID
				.routerNm("") //라우터 경로
				.content("비상상황이 발생했습니다.\n관련 매뉴얼에 따라 대응하시기 바랍니다.") //웹,앱 푸시 알람 내용
				.params(params)
				.build();
		try {
			fcmService.sendFcmToSpecificDevice(oMsg); // 웹,앱 푸시 알람 발송
		} catch (Exception e) {
			throw new CUserDefinedException("FCM 전송 실패: "+e.getMessage(), e);
		}
		return vo;
	}

	//비상 상황 알림 내역 저장
	@Transactional(rollbackFor = Throwable.class)
	public AlarmVO insertEmergencyAlarmHistory(List<String>fcmUserIdList) throws Exception{
		AlarmVO alarmVO = new AlarmVO();
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		for(int i = 0; i < fcmUserIdList.size(); i++) {
			alarmVO.setClntId(SecurityUtil.getCurrentClntId());
			alarmVO.setUserId(fcmUserIdList.get(i));
			alarmVO.setAlarmDt(formattedDate);
			alarmVO.setFcmTitle("[비상상황 알림]");//FcmMessageVO.title 저장 
			alarmVO.setFcmBody("비상상황이 발생했습니다.\n관련 매뉴얼에 따라 대응하시기 바랍니다.");//FcmMessageVO.content 저장
			alarmVO.setCreatedBy(SecurityUtil.getCurrentMemberId()); //유저 아이디
			alarmDao.insertEmergencyAlarmHistory(alarmVO);
		}
		return alarmVO;
	}

	//안전보건 교육실시 계획서 메세지 발송
	@Transactional(rollbackFor = Throwable.class)
	public void sendTrainingPlanImplMsg(TrainingPlanImplVO reqVo, String title, String menuId) throws Exception {
		SpSearchVO searchVo = new SpSearchVO();
		searchVo.setCompId(SecurityUtil.getCurrentCompId());
		searchVo.setWriteYear(reqVo.getWriteYear());
		searchVo.setDocNo(reqVo.getDocNo());
		searchVo.setDocType(reqVo.getDocType());
		TrainingPlanImplVO detailVo = trainingPlanImplDao.getTrainingPlanImplDetail(searchVo);

		List<String> fcmUserIdList = new ArrayList<>();
		String userList = "";
		for(HrVO vo : reqVo.getHrList()){
			vo.setCompId(SecurityUtil.getCurrentCompId());
			if(userList.equals("")){
				userList = vo.getUserId();
			}else{
				userList += ',' + vo.getUserId();
			}
		}
		HrVO userInfoData = new HrVO();
		userInfoData.setCompId(SecurityUtil.getCurrentCompId());
		userInfoData.setUserId(userList);
		List<HrVO> tokenUserList = alarmDao.getSelectedTokenUserInfo(userInfoData);

		for(HrVO vo : tokenUserList){
			fcmUserIdList.add(vo.getUserId());
		}

		MainMenuVO menuVO = mainMenuDao.findOne(menuId);
		TrainingPlanImplAlarmVO vo = AlarmService.this.insertTrainingPlanImplAlarmHistory(detailVo, fcmUserIdList, title, menuId);
		Map<String, Object> params = new HashMap<>();
		params.put("routerNm", menuVO.getRouterNm());
		params.put("alarmDt", vo.getAlarmDt());
		params.put("docNo", detailVo.getDocNo());
		params.put("docType", detailVo.getDocType());
		params.put("writeYear", detailVo.getWriteYear());
		params.put("alarmSeq", String.valueOf(vo.getAlarmSeq()));
		params.put("type", "trainingPlanImpl"); //백그라운드 알림 구분을 위한 타입 추가
		params.put("menuId", menuId);
		//웹,앱 푸시 알람
		FcmMessageVO oMsg = FcmMessageVO.builder()
				.title("[안전보건 교육 실시 계획서 알림]") //제목
				.targetUserIdList(fcmUserIdList) //웹,앱 푸시 알람 보낼 대상자들의 ID
				.routerNm(menuVO.getRouterNm()) //라우터 경로
				.content("안전보건 교육 실시 계획서가 등록되었습니다.\n" + title) //웹,앱 푸시 알람 내용
				.params(params)
				.build();
		try {
			fcmService.sendFcmToSpecificDevice(oMsg); // 웹,앱 푸시 알람 발송
		} catch (Exception e) {
			throw new CUserDefinedException("FCM 전송 실패: "+e.getMessage(), e);
		}
	}

	//안전보건 교육실시 계획서 알림 내역 저장
	@Transactional(rollbackFor = Throwable.class)
	public TrainingPlanImplAlarmVO insertTrainingPlanImplAlarmHistory(TrainingPlanImplVO vo, List<String> fcmUserIdList, String title, String menuId) throws Exception{
		TrainingPlanImplAlarmVO alarmVO = new TrainingPlanImplAlarmVO();
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		for(int i = 0; i < fcmUserIdList.size(); i++) {
			alarmVO.setUserId(fcmUserIdList.get(i));
			alarmVO.setAlarmDt(formattedDate);
			alarmVO.setFcmTitle("[안전보건 교육 실시 계획서 알림]");//FcmMessageVO.title 저장
			alarmVO.setFcmBody("안전보건 교육 실시 계획서가 등록되었습니다.\n" + title);//FcmMessageVO.content 저장
			alarmVO.setMenuId(menuId);
			alarmVO.setWriteYear(vo.getWriteYear());
			alarmVO.setDocType(vo.getDocType());
			alarmVO.setDocNo(vo.getDocNo());
			alarmVO.setCreatedBy(SecurityUtil.getCurrentMemberId()); //유저 아이디
			alarmDao.insertTrainingPlanImplAlarmHistory(alarmVO);
		}
		return alarmVO;
	}

	//알림 발송을 위한 token 여부 조회
	@Transactional(rollbackFor = Throwable.class)
	public List<HrVO> getSelectedTokenUserInfo(List<String>voList) throws Exception{
		List<HrVO> hrVoList = new ArrayList<>();
		HrVO vo = new HrVO();
		int count = 0;
		String userList = "";
		for(String id : voList){
			if(userList.equals("")){
				userList = id;
			}else{
				userList += ',' + id;
			}
			count++;
		}
		vo.setCompId(SecurityUtil.getCurrentCompId());
		vo.setUserId(userList);
		hrVoList = alarmDao.getSelectedTokenUserInfo(vo);
		return hrVoList;
	}
}
