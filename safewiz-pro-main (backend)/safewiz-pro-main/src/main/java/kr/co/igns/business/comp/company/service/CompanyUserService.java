package kr.co.igns.business.comp.company.service;
import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.comp.company.dao.postgres.CompanyUserDAO;
import kr.co.igns.business.comp.company.model.CompanyUserVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.comp.company.service
 * ※ 파일명 : CompanyService.java
 * ※ 기능명 :
 * ※ 작성자 : 이하운
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */

//[2024-07-23] 패키지 변경 - 신택훈

//@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyUserService {

	private final CompanyUserDAO companyUserDao;
	
	public BaseVO create(CompanyUserVO reqVo) throws Exception {
		companyUserDao.insert(reqVo);
		return reqVo;
	}
	
	public int removeByUserId(String userId) {
		return companyUserDao.deleteByUserId(userId);
	}
	
	private int removeSingle(Long memSeq) throws Exception {
		return companyUserDao.delete(memSeq);
	}
	
	public int searchCount(SpSearchVO searchVo) throws Exception {
		return companyUserDao.searchCount(searchVo);
	}
	
	public List<CompanyUserVO> search(SpSearchVO searchVo) throws Exception {
		List<CompanyUserVO> voList = companyUserDao.search(searchVo);
		return voList;
	}
	
	public ArrayNode treeCompanyUser(SpSearchVO searchVo) throws Exception {
		String delYn = "";
		if(searchVo != null) {
			delYn = "".equals(searchVo.getDelYn()) ? "" : searchVo.getDelYn(); ;
			searchVo.setDelYn("");
		}
		List<CompanyUserVO> voList = companyUserDao.search(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "compId", "upCompId");
		SpUtils.treeFilter(tree, delYn);
		return tree;
	}

	public BaseVO searchByCompIdAndUserId(SpSearchVO searchVo) throws Exception {
		CompanyUserVO vo = companyUserDao.searchByCompIdAndUserId(searchVo);

		return vo;
	}

}
