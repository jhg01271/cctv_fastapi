package kr.co.igns.system.setting.service;

import kr.co.igns.business.support.model.TrainingInstructorVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.ClientCompDAO;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.model.ClientCompVO;
import kr.co.igns.system.setting.model.ClientVO;
import kr.co.igns.system.setting.model.CompVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCompService {
    private final ClientCompDAO clientCompDao;

    public List<ClientVO> getClient(SpSearchVO searchVo) throws Exception {
        List<ClientVO> voList = clientCompDao.getClientList(searchVo);
        return voList;
    }

    public List<ClientCompVO> getComp(SpSearchVO searchVo) throws Exception {
        List<ClientCompVO> voList = clientCompDao.getCompList(searchVo);
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveCompByClient(List<ClientCompVO> req) throws Exception {
        for (ClientCompVO tmp : req) {
            clientCompDao.insertCompByClient(tmp);
        }
    }

    public void deleteCompByClient(List<ClientCompVO> req) throws Exception {
        for (ClientCompVO tmp : req) {
            clientCompDao.deleteCompByClient(tmp);
        }
    }
}
