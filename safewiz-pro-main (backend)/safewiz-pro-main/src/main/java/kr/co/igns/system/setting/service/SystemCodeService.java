package kr.co.igns.system.setting.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import kr.co.igns.business.support.model.AnnualTrainingPlanVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemCodeService {

    private final SystemCodeDAO systemCodeDao;
    private final FileService fileService;

    public List<SystemMinorCodeVO> findDetail(SystemCodeVO vo) throws Exception {
        List<SystemMinorCodeVO> voList = systemCodeDao.findDetail(vo);
        return voList;
    }

    public List<SystemMinorCodeVO> findDetailPage(SpSearchVO searchVo) throws Exception {
        List<SystemMinorCodeVO> voList = systemCodeDao.findDetailPage(searchVo);
        return voList;
    }

    public List<SystemMinorCodeVO> findDatasetCode(SpSearchVO searchVo) throws Exception {
        List<SystemMinorCodeVO> voList = systemCodeDao.findDatasetCode(searchVo);
        return voList;
    }

    public int searchCountDetail(SpSearchVO searchVo) throws Exception {
        return systemCodeDao.searchCountDetail(searchVo);
    }

    public String removeMasterCode(SystemCodeVO vo) throws Exception {
        systemCodeDao.deleteMaster(vo);
        return vo.getMajorCd();
    }

    public String removeDetailCode(SystemMinorCodeVO vo) throws Exception {
        systemCodeDao.deleteDetail(vo);
        return vo.getMinorCd();
    }

    public BaseVO upsertMasterCode(SystemCodeVO reqVo) throws Exception {
        if (reqVo.is__created__()) {
            reqVo.setMajorType("S");
            systemCodeDao.createMasterCode(reqVo);
        } else {
            systemCodeDao.updateMasterCode(reqVo);
        }
        return reqVo;
    }

    public String updateEsgDetailCode(List<SystemMinorCodeVO> reqVo) throws Exception {
        SystemMinorCodeVO firstVo = reqVo.get(0);
        systemCodeDao.deleteEsgDetailCodeAll(firstVo);
        for (SystemMinorCodeVO vo : reqVo) {
            systemCodeDao.createEsgDetailCode(vo);
        }
        return firstVo.getMajorCd();
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO upsertDetailCode(List<MultipartFile> files, SystemMinorCodeVO reqVo) throws Exception {
        String majorCd = reqVo.getMajorCd();
        if (reqVo.is__created__()) {
            reqVo.setCompId("999999999999");
            systemCodeDao.createDetailCode(reqVo);
            fileService.saveFile(files, majorCd, reqVo.getMinorCd(), reqVo.getCompId(), "system");
        } else {
            reqVo.setCompId("999999999999");
            systemCodeDao.updateDetailCode(reqVo);
            fileService.deleteFile(reqVo.getDeleteFiles(), majorCd, reqVo.getMinorCd(), reqVo.getCompId());
            fileService.saveFile(files, majorCd, reqVo.getMinorCd(), reqVo.getCompId(),"system");
        }
        return reqVo;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return systemCodeDao.searchCount(searchVo);
    }

    public List<SystemCodeVO> search(SpSearchVO searchVo) throws Exception {
        List<SystemCodeVO> voList = systemCodeDao.search(searchVo);
        return voList;
    }

    public ArrayNode tree(SpSearchVO searchVo) throws Exception {
        String useYn = "";
        if (searchVo != null) {
            useYn = "".equals(searchVo.getUseYn()) ? "" : searchVo.getUseYn();
            ;
            searchVo.setUseYn("");
        }
        List<SystemCodeVO> voList = systemCodeDao.search(searchVo);
        ArrayNode tree = SpUtils.setNodeTree(voList, "codeSeq", "upCodeSeq");
        SpUtils.treeFilter(tree, useYn);
        return tree;
    }

    //커스텀 시스템 코드 조회, 저장, 삭제
    public List<SystemMinorCodeVO> findEsgCommonDetail(SystemCodeVO vo) throws Exception {
        List<SystemMinorCodeVO> voList = systemCodeDao.findEsgCommonDetail(vo);
        return voList;
    }

    public BaseVO saveEsgDetailCode(List<SystemMinorCodeVO> voList) throws Exception {
        for (SystemMinorCodeVO vo : voList) {
            if (vo.getCmd().equals("I")) {
                systemCodeDao.createEsgDetailCode(vo);
            } else {
                systemCodeDao.updateEsgDetailCode(vo);
            }
        }
        return voList.get(0);
    }

    public BaseVO deleteEsgDetailCode(List<SystemMinorCodeVO> voList) throws Exception {
        for (SystemMinorCodeVO vo : voList) {
            systemCodeDao.deleteEsgDetailCode(vo);
        }
        return voList.get(0);
    }

}
