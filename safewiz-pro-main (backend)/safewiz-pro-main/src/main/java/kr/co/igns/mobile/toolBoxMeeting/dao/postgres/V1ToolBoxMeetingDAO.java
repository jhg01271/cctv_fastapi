package kr.co.igns.mobile.toolBoxMeeting.dao.postgres;

import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingSearchVO;
import kr.co.igns.mobile.toolBoxMeeting.model.ToolBoxMeetingVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface V1ToolBoxMeetingDAO {
    List<ToolBoxMeetingVO> getTbmSchedule(ToolBoxMeetingSearchVO vo);

    void insertMain(ToolBoxMeetingVO vo);

    void updateMain(ToolBoxMeetingVO vo);

    void saveRiskFactors(ToolBoxMeetingVO vo);

    void saveParticipants(ToolBoxMeetingVO vo);

    ToolBoxMeetingVO getTBM(ToolBoxMeetingSearchVO vo);

    void deleteTbm(ToolBoxMeetingSearchVO vo);
}
