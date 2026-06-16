package kr.co.igns.business.support.service;

import kr.co.igns.business.support.dao.postgres.ManageDocDAO;
import kr.co.igns.business.support.model.ManageDocVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.SystemCodeDAO;
import kr.co.igns.system.setting.model.SystemCodeVO;
import kr.co.igns.system.setting.model.SystemMinorCodeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ManageDocService {
    private final ManageDocDAO manageDocDao;
    private final FileService fileService;
    private final SystemCodeDAO systemCodeDao;
    private final String targetType = "DOC";

    public List<ManageDocVO> getManageDoc(SpSearchVO searchVo) throws Exception {
        List<ManageDocVO> voList = manageDocDao.getManageDoc(searchVo);

        return voList;
    }

    public List<ManageDocVO> getManageDocDetail(SpSearchVO searchVo) throws Exception {
        List<ManageDocVO> voList = manageDocDao.getManageDocDetail(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO saveManageDoc(Map<String,List<MultipartFile>> files, List<ManageDocVO> voList) throws Exception {
        boolean firstYn = true;
        for (ManageDocVO vo : voList) {
            String key = vo.getWriteYear() + vo.getDocNo()+vo.getManageDocType();
            if (vo.getCmd().equals("I") && firstYn) {
                // 문서 신규
                vo.setDocType(targetType);
                manageDocDao.insertManageDoc(vo);
                firstYn = false;
                key = vo.getWriteYear() + vo.getDocNo()+vo.getManageDocType();
            } else {
                if (firstYn) {
                    // 문서 수정
                    ManageDocVO resultVO = manageDocDao.getManageDocById(vo);
                    if (resultVO == null)
                        return null;
                    resultVO = (ManageDocVO) SpUtils.objectMap(vo, resultVO);
                    manageDocDao.updateManageDoc(resultVO);
                    firstYn = false;
                }

                fileService.deleteFile(vo.getDeleteFiles(), targetType, key, SecurityUtil.getCurrentCompId());
            }

            //파일 저장
            if(files != null) {
                fileService.saveFile(files.get(vo.getManageDocType()), targetType, key, SecurityUtil.getCurrentCompId());
            }
        }

        return voList.get(0);
    }

    public BaseVO deleteManageDoc(ManageDocVO reqVo) throws Exception {
        BaseVO vo = manageDocDao.getManageDocById(reqVo);
        manageDocDao.deleteManageDoc(reqVo);
        return vo;
    }

    public void deleteManageDocs(List<ManageDocVO> list) throws Exception {
        for (ManageDocVO tmp : list) {
            manageDocDao.deleteManageDoc(tmp);
        }
    }

}

