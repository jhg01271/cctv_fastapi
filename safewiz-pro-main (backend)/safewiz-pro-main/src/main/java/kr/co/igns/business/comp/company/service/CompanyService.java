package kr.co.igns.business.comp.company.service;
import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.comp.company.dao.postgres.CompanyDAO;
import kr.co.igns.business.comp.company.model.CompanyVO;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.service
 * ※ 파일명 : CompanyService.java
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
public class CompanyService {

	private final CompanyDAO companyDao;
	//private final FacilityDAO facilityDao;
	private final JwtService jwtService;
	@Value("${jwt.invite.expiration}")
	private Long inviteTokenExpirationPeriod;

	public BaseVO create(CompanyVO compVo) throws Exception {
		String token = jwtService.createCustomToken("compId", compVo.getCompId(), inviteTokenExpirationPeriod);
		compVo.setInviteKey(token);
		companyDao.insert(compVo);

		return compVo;

	}
	
	public BaseVO modify(CompanyVO compVo) throws Exception {

		CompanyVO vo = companyDao.findOne(compVo.getCompId());
		if (vo == null)
			return null;

		vo = (CompanyVO) SpUtils.objectMap(compVo, vo);
		// 순환 참조 검사
		ArrayNode tree = treeCompany(null);
		SpUtils.validateTree(tree, vo.getCompId() ,vo.getUpCompId(), "compId");

		
		companyDao.update(vo);

		return vo;
	}
	
	public BaseVO remove(String compId) throws Exception {
		BaseVO vo = new BaseVO();

		if (compId.contains(",")) { // 복수의 ID 값에 대해 반복하여 삭제 수행
			String[] ids = compId.split(",");
			int cnt = 0;
			for (String singleId : ids) {
				removeSingle(singleId.trim());
				cnt++;
			}
			vo.setResultCnt(cnt);

		} else { // 단일 ID 값에 대해 삭제 수행

			vo = companyDao.findOne(compId);
			removeSingle(compId.trim());
		}

		return vo;
	}

	// [2024-07-11] 물리삭제, 논리삭제 추가 - 신택훈
	private int removeSingle(String compId) throws Exception {
		
		SpSearchVO searchVo = SpSearchVO.builder()//
				.compId(compId)//
				.delYn(null).build();
		
		// 자식이 있는지, 다른 테이블에서 사용중인지 확인 
		int childCount = companyDao.searchCount(searchVo);		
		//int facilityUsageCount = facilityDao.searchCount(searchVo);
		
//        if (childCount == 0 && facilityUsageCount == 0) {
            return companyDao.delete(compId); // 물리 삭제
//        } else {
//            return companyDao.updateDelYn(compId); // 논리 삭제
//        }
	}
	
	public int searchCount(SpSearchVO searchVo) throws Exception {
		return companyDao.searchCount(searchVo);
	}
	
	public List<CompanyVO> search(SpSearchVO searchVo) throws Exception {
		List<CompanyVO> voList = companyDao.search(searchVo);
		return voList;
	}
	
	public ArrayNode treeCompany(SpSearchVO searchVo) throws Exception {
		String delYn = "";
		if(searchVo != null) {
			delYn = "".equals(searchVo.getDelYn()) ? "" : searchVo.getDelYn(); ;
			searchVo.setDelYn("");
		}
		List<CompanyVO> voList = companyDao.search(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "compId", "upCompId");
		SpUtils.treeFilter(tree, delYn);
		return tree;
	}

	public BaseVO findOne(String compId) throws Exception {
		CompanyVO vo = companyDao.findOne(compId);

		return vo;
	}

	public String updateInvatekey(String compId) throws Exception {
		String token = jwtService.createCustomToken("compId", compId, inviteTokenExpirationPeriod);

		CompanyVO companyVO = companyDao.findOne(compId);
		companyVO.setInviteKey(token);

		companyDao.update(companyVO);

		return token;
	}
	 


}
