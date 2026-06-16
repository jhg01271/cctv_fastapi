package kr.co.igns.business.comp.master.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.comp.master.dao.postgres.CompCodeDAO;
import kr.co.igns.business.comp.master.model.CompCodeVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompCodeService {

	private final CompCodeDAO compCodeDAO;

	public BaseVO create(CompCodeVO reqVo) throws Exception {
		compCodeDAO.insert(reqVo);
		return reqVo;

	}

	public BaseVO modify(CompCodeVO reqVo) throws Exception {
		CompCodeVO vo = compCodeDAO.findOne(reqVo.getCodeSeq());
		if (vo == null)
			return null;
		vo = (CompCodeVO) SpUtils.objectMap(reqVo, vo);

		// 순환 참조 검사
		ArrayNode tree = treeCompCode(null);
		SpUtils.validateTree(tree, vo.getCodeSeq() ,vo.getUpCodeSeq(), "compCodeSeq");

		compCodeDAO.update(vo);

		return vo;
	}

	public BaseVO remove(Long compCodeSeq) throws Exception {
		BaseVO vo = compCodeDAO.findOne(compCodeSeq);
		removeSingle(compCodeSeq);

		return vo;
	}
	private int removeSingle(Long compCodeSeq) throws Exception {
		return compCodeDAO.updateDelYn(compCodeSeq);
	}


	public List<CompCodeVO> search(SpSearchVO searchVo) throws Exception {
		List<CompCodeVO> voList = compCodeDAO.search(searchVo);
		return voList;
	}

	public ArrayNode treeCompCode(SpSearchVO searchVo) throws Exception {
		String delYn = "";
		if(searchVo != null) {
			delYn = "".equals(searchVo.getDelYn()) ? "" : searchVo.getDelYn(); ;
			searchVo.setDelYn("");
		}
		List<CompCodeVO> voList = compCodeDAO.search(searchVo);
		ArrayNode tree = SpUtils.setNodeTree(voList, "codeSeq", "upCodeSeq");
		SpUtils.treeFilter(tree, delYn);
		return tree;
	}

	public BaseVO findOne(Long codeSeq) throws Exception {
		CompCodeVO vo = compCodeDAO.findOne(codeSeq);
		return vo;
	}

	public List<CompCodeVO> getSubCodeList(String compId, String code) throws Exception {
		List<CompCodeVO> voList = compCodeDAO.findByUpCode(compId, code);
		return voList;
	}

	public int searchCount(SpSearchVO searchVo) throws Exception {
		return compCodeDAO.searchCount(searchVo);
	}
}
