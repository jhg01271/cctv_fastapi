package kr.co.igns.system.setting.dao.postgres;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.base.base.dao.postgres;
 * ※ 파일명 : SystemCode.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 7. 8.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Mapper
@Repository
public interface SystemCodeDAO {

    int createMasterCode(SystemCodeVO vo);

    int createDetailCode(SystemMinorCodeVO vo);

    int updateMasterCode(SystemCodeVO vo);

    int updateDetailCode(SystemMinorCodeVO vo);

    int update(SystemCodeVO vo);

    void deleteMaster(SystemCodeVO vo);
    
    void deleteDetail(SystemMinorCodeVO vo);

    void deleteDetailAll(SystemMinorCodeVO vo);
    
    int searchCount(SpSearchVO searchVo);

    int searchCountDetail(SpSearchVO searchVo);

    List<SystemCodeVO> search(SpSearchVO searchVo);

    List<SystemMinorCodeVO> findDetail(SystemCodeVO vo);
    
    List<SystemMinorCodeVO> findDetailTaskSign(SystemCodeVO vo); // 2025.04.23 BHJ C0044 TASK 구분에서 sign 함목만 조회 (sign001~sign006) 

    List<SystemMinorCodeVO> findDetailPage(SpSearchVO searchVo);

    List<SystemMinorCodeVO> findDatasetCode(SpSearchVO searchVo);

    SystemCodeVO findOne(Long codeSeq);

    //커스텀 시스템 코드 조회, 저장, 삭제
    List<SystemMinorCodeVO> findEsgCommonDetail(SystemCodeVO vo);

    int createEsgDetailCode(SystemMinorCodeVO vo);
    int updateEsgDetailCode(SystemMinorCodeVO vo);

    void deleteEsgDetailCode(SystemMinorCodeVO vo);
    void deleteEsgDetailCodeAll(SystemMinorCodeVO vo);

}
