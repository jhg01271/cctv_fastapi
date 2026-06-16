package kr.co.igns.business.comp.company.service;
import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.comp.company.dao.postgres.CompanyMyDAO;
import kr.co.igns.business.comp.company.model.CompVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.company.service
 * ※ 파일명 : CompService.java
 * ※ 기능명 :
 * ※ 작성자 : 조동하
 * ※ 최초생성일 : 2024. 8. 14.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 * FIXME companyUserService랑 합쳐 져야 함
 * </pre>
 */
//@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyMyService {

    private final CompanyMyDAO companyMyDao;

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return companyMyDao.searchCount(searchVo);
    }

    public List<CompVO> search(SpSearchVO searchVo) throws Exception {
        List<CompVO> voList = companyMyDao.search(searchVo);
        return voList;
    }

    public ArrayNode tree(SpSearchVO searchVo) throws Exception {
        String delYn = "";
        if(searchVo != null) {
            delYn = "".equals(searchVo.getDelYn()) ? "" : searchVo.getDelYn(); ;
            searchVo.setDelYn("");
        }
        List<CompVO> voList = companyMyDao.search(searchVo);
        ArrayNode tree = SpUtils.setNodeTree(voList, "compId", "upCompId");
        SpUtils.treeFilter(tree, delYn);
        return tree;
    }

}
