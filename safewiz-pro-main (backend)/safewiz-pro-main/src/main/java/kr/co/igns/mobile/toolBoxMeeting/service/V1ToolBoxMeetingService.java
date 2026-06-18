package kr.co.igns.mobile.toolBoxMeeting.service;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.mobile.toolBoxMeeting.dao.postgres.V1ToolBoxMeetingDAO;
import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingSearchVO;
import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingVO;
import kr.co.igns.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class V1ToolBoxMeetingService {
    private final V1ToolBoxMeetingDAO dao;
    private final UserService userService;

    public List<ToolBoxMeetingVO> getTbmSchedule(ToolBoxMeetingSearchVO vo) {
        vo.setHrId(SecurityUtil.getCurrentHrId());
        return dao.getTbmSchedule(vo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ToolBoxMeetingVO saveTbm(ToolBoxMeetingVO vo) throws Exception {
        vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
        if (vo.getId() == null || vo.getId().isEmpty()) {
            dao.insertMain(vo);
        } else {
            dao.updateMain(vo);
        }
        dao.saveRiskFactors(vo);
        dao.saveParticipants(vo);
        return vo;
    }


    public ToolBoxMeetingVO getTbm(String id) {
        ToolBoxMeetingSearchVO vo = new ToolBoxMeetingSearchVO();
        vo.setId(id);
        try {
            vo.setHrId(userService.findOne().getHrId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dao.getTBM(vo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteTbm(String id) throws Exception {
        ToolBoxMeetingSearchVO vo = new ToolBoxMeetingSearchVO();
        vo.setId(id);
        vo.setCreatedBy(SecurityUtil.getCurrentMemberId());
        dao.deleteTbm(vo);
    }
}
