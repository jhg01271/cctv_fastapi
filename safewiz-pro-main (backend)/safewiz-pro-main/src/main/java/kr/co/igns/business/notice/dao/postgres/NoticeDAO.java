package kr.co.igns.business.notice.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.notice.model.NoticeAlarmVO;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.business.notice.model.NoticeVO;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 기능명 :
 * ※ 작성자 : 심지성
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 */
@Mapper
@Repository
public interface NoticeDAO {

	List<NoticeVO> getNotice(NoticeVO vo);
	NoticeDetailVO getNoticeDetail(NoticeVO vo);

	void insertNotice(NoticeDetailVO vo);
	void updateNotice(NoticeDetailVO vo);
	void deleteNotice(NoticeDetailVO vo);
	
	//웹,앱 푸시 알림 전송 이력
	void insertNoticeAlarmHistory(NoticeAlarmVO vo);

}
