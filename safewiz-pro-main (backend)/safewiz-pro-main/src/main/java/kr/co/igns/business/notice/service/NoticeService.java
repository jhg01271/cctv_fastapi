package kr.co.igns.business.notice.service;
import kr.co.igns.business.notice.dao.postgres.NoticeDAO;
import kr.co.igns.business.notice.model.NoticeAlarmVO;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.business.notice.model.NoticeVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.comm.model.FcmMessageVO;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.setting.model.MainMenuVO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import kr.co.igns.system.user.service.UserService;
import kr.co.igns.framework.comm.service.FcmService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
public class NoticeService {
    private static final Logger log = LogManager.getLogger("com.project");
    private final NoticeDAO noticeDao;
    private final FileService fileService;
	private final UserService userService;
	private final FcmService fcmService;
	private final SystemCodeDAO systemCodeDao;
	private final MainMenuDAO mainMenuDao;

    public List<NoticeVO> getNotice(NoticeVO vo) throws Throwable {
        return noticeDao.getNotice(vo);
    }

    public NoticeDetailVO getNoticeDetail(NoticeVO vo) throws Throwable {
	    NoticeDetailVO data = noticeDao.getNoticeDetail(vo);
         return data;
    }

    //	 공지사항 저장
	@Transactional(rollbackFor = Throwable.class)
	public NoticeDetailVO saveNotice(List<MultipartFile> files, NoticeDetailVO vo) throws Throwable {
		String targetId = "";
		String userId = SecurityUtil.getCurrentMemberId();
		UserVO userVo = new UserVO();
		userVo.setCompId(vo.getCompId());
		List<String> fcmUserIdList = userService.getAllTokenUser(userVo);

			if (vo.getCmd().equals("I")) {
				vo.setCreatedBy(userId);
				noticeDao.insertNotice(vo);
				targetId = vo.getClntId()+"_"+vo.getWriteDt()+"_"+vo.getKeyDocNo();
				String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				System.out.println(today);
				vo.setWriteDt(today);
				vo.setDocNo(vo.getKeyDocNo());
			} else {
				vo.setUpdatedBy(userId);
				noticeDao.updateNotice(vo);
				targetId = vo.getClntId()+"_"+vo.getWriteDt()+"_"+vo.getDocNo();
				fileService.deleteFile(vo.getDeleteFiles(), "notice", vo.getWriteDt()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
			}
		//파일 저장
		fileService.saveFile(files, "notice", vo.getWriteDt()+vo.getDocNo(), SecurityUtil.getCurrentCompId());
		return vo;
	}

    //	 공지사항 삭제
    @Transactional(rollbackFor = Throwable.class)
    public void deleteNotice(List<NoticeDetailVO> voList) throws Throwable {
        for (NoticeDetailVO vo : voList) {
	        fileService.deleteFile(vo.getDeleteFiles(), "notice", vo.getWriteDt()+"_"+vo.getDocNo(), SecurityUtil.getCurrentCompId());
            log.info("삭제 값 --> " + vo);
            noticeDao.deleteNotice(vo);
        }
    }

	@Transactional(rollbackFor = Throwable.class)
	public NoticeAlarmVO insertNoticeAlarmHistory(NoticeDetailVO vo, List<String>fcmUserIdList, String menuId, String userId) throws Exception{
		NoticeAlarmVO alarmVO = new NoticeAlarmVO();
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		for(int i = 0; i < fcmUserIdList.size(); i++) {
			alarmVO.setUserId(fcmUserIdList.get(i));
			alarmVO.setAlarmDt(formattedDate);
			alarmVO.setFcmTitle("[공지사항 알림]"); //FcmMessageVO.title 저장 
			alarmVO.setFcmBody("공지사항이 등록되었습니다.\n" +vo.getSubject());//FcmMessageVO.content 저장
			alarmVO.setMenuId(menuId);
			alarmVO.setClntId(vo.getClntId());
			alarmVO.setWriteDt(vo.getWriteDt());
			alarmVO.setDocNo(vo.getDocNo());
			alarmVO.setCreatedBy(userId);
			noticeDao.insertNoticeAlarmHistory(alarmVO);
		}
		return alarmVO;
	}


	public NoticeVO sendPushMsg(NoticeVO vo, String menuId) throws Throwable {
		NoticeDetailVO detailVo = noticeDao.getNoticeDetail(vo);
		vo.setUserId(SecurityUtil.getCurrentMemberId());
		UserVO userVo = new UserVO();
		userVo.setCompId(vo.getCompId());
		List<String> fcmUserIdList = userService.getAllTokenUser(userVo);
		MainMenuVO menuVO = mainMenuDao.findOne(menuId);
		NoticeAlarmVO alarmVO = NoticeService.this.insertNoticeAlarmHistory(detailVo, fcmUserIdList, menuId, vo.getUserId());
		Map<String, Object> params = new HashMap<>();
		params.put("routerNm", menuVO.getRouterNm());
		params.put("docNo", detailVo.getDocNo());
		params.put("clntId", detailVo.getClntId());
		params.put("writeDt", detailVo.getWriteDt());
		params.put("alarmDt", alarmVO.getAlarmDt());
		params.put("alarmSeq", String.valueOf(alarmVO.getAlarmSeq()));
		params.put("type", "notice"); //백그라운드 알림 구분을 위한 타입 추가
		params.put("menuId", menuId);
		//웹,앱 푸시 알람

		FcmMessageVO oMsg = FcmMessageVO.builder()
				.title("[공지사항 알림]") //제목
				.targetUserIdList(fcmUserIdList) //웹,앱 푸시 알람 보낼 대상자들의 ID
				.routerNm(menuVO.getRouterNm()) //라우터 경로
				.content("공지사항이 등록되었습니다.\n" +detailVo.getSubject()) //웹,앱 푸시 알람 내용
				.params(params)
				.build();
		try {
			fcmService.sendFcmToSpecificDevice(oMsg); // 웹,앱 푸시 알람 발송
		} catch (Exception e) {
			throw new CUserDefinedException("FCM 전송 실패: "+e.getMessage(), e);
		}
		return vo;
	}


}
