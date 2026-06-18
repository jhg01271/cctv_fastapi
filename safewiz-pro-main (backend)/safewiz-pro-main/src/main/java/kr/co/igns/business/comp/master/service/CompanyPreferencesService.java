package kr.co.igns.business.comp.master.service;
import kr.co.igns.business.comp.master.dao.postgres.CompanyPreferencesDAO;
import kr.co.igns.business.comp.master.model.CompanyPreferencesVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.factory.service
 * ※ 파일명 : CompanyPreferencesService.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
//@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyPreferencesService {

	private final CompanyPreferencesDAO companyPreferencesDao;

	public BaseVO findOne(String clntId) throws Exception {
		CompanyPreferencesVO vo = companyPreferencesDao.findOne(clntId);

		return vo;
	}

	public BaseVO modify(CompanyPreferencesVO reqVo) throws Exception {

		CompanyPreferencesVO vo = companyPreferencesDao.findOne(reqVo.getCompId());
		if (vo == null)
			return null;

		vo = (CompanyPreferencesVO) SpUtils.objectMap(reqVo, vo);
		companyPreferencesDao.update(vo);

		return vo;
	}

	public BaseVO remove(String prcssSeq) throws Exception {
		BaseVO vo = new BaseVO();

		if (prcssSeq.contains(",")) { // 복수의 ID 값에 대해 반복하여 삭제 수행
			String[] ids = prcssSeq.split(",");
			int cnt = 0;
			for (String singleId : ids) {
				removeSingle(singleId.trim());
				cnt++;
			}
			vo.setResultCnt(cnt);

		} else { // 단일 ID 값에 대해 삭제 수행

			vo = companyPreferencesDao.findOne(prcssSeq);
			removeSingle(prcssSeq.trim());
		}

		return vo;
	}

	public BaseVO create(CompanyPreferencesVO reqVo) throws Exception {
//		String uuid = FemsUtils.getUuid();
//		reqVo.getClntId(uuid);

		companyPreferencesDao.insert(reqVo);
//		log.info("insert PK : " + reqVo.getPrcssSeq());// pk는 가져온다.

		return reqVo;

	}

	public int searchCount(SpSearchVO searchVo) throws Exception {
		return companyPreferencesDao.searchCount(searchVo);
	}

	public List<CompanyPreferencesVO> search(SpSearchVO searchVo) throws Exception {
		return companyPreferencesDao.search(searchVo);
	}

	private int removeSingle(String prcssSeq) throws Exception {

//		FemsSearchVO searchSubVo = FemsSearchVO.builder().build();
//		int subCount = facilityDao.searchCount(searchSubVo);
		int subCount = 1;
		if (subCount > 0) {// 논리 삭제
			return companyPreferencesDao.updateDelYn(prcssSeq);
		} else {// 물리 삭제
			return companyPreferencesDao.delete(prcssSeq);
		}
	}

}
