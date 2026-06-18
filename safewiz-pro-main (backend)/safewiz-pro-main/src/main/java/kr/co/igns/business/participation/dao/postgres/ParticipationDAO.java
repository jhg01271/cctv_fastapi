package kr.co.igns.business.participation.dao.postgres;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.participation.model.ContentDto;
import kr.co.igns.business.participation.model.ParticipationVo;
import kr.co.igns.business.participation.model.SignatureDto;
import kr.co.igns.system.common.model.SpSearchVO;

@Mapper
@Repository
public interface ParticipationDAO {
    void insert(ParticipationVo vo);
    void insertMember(SignatureDto dto);
    void deleteMember(String targetType, String targetId);
    void deleteContent(ParticipationVo vo);
    void insertContent(ContentDto dto);
    void update(ParticipationVo vo);
    void delete(ParticipationVo vo);

    List<ParticipationVo> searchList(SpSearchVO searchVo);
    ParticipationVo search(SpSearchVO searchVo);
    List<SignatureDto> findMemberById(ParticipationVo targetId);
    List<ContentDto> findContentById(ParticipationVo vo);

}
