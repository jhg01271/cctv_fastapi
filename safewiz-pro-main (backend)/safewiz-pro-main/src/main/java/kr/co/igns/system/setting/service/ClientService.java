package kr.co.igns.system.setting.service;

import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.system.setting.dao.postgres.ClientDAO;
import kr.co.igns.system.setting.model.ClientVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientDAO clientDao;

    private final FileService fileService;

    public List<ClientVO> getClient(SpSearchVO searchVo) throws Exception {
        List<ClientVO> voList = clientDao.getClientList(searchVo);
        return voList;
    }

    public ClientVO getClientDetail(String clntId) throws Exception {
        ClientVO voList = clientDao.getClientDetail(clntId);
        return voList;
    }

    public int searchCount(SpSearchVO searchVo) throws Exception {
        return clientDao.searchCount(searchVo);
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO insertClient(List<MultipartFile> files, ClientVO dto) throws Exception {
        //고객번호 자동 부여 (년월일순번)
        clientDao.insertClient(dto);
        fileService.saveFile(files, "clientLogo", dto.getClntId(), SecurityUtil.getCurrentCompId(), "system");
        return dto;
    }

    @Transactional(rollbackFor = Throwable.class)
    public BaseVO updateClient(List<MultipartFile> insertFiles, ClientVO reqVo) throws Exception {
        ClientVO vo = clientDao.getClientById(reqVo.getClntIdTemp());
        if (vo == null)
            return null;
        vo = (ClientVO) SpUtils.objectMap(reqVo, vo);

        clientDao.updateClient(vo);

        //파일 추가/변경
        fileService.deleteFile(reqVo.getDeleteFiles(), "clientLogo", reqVo.getClntIdTemp(), SecurityUtil.getCurrentCompId());
        String fileId = fileService.saveFile(insertFiles, "clientLogo", reqVo.getClntIdTemp(), SecurityUtil.getCurrentCompId(), "system");
        vo.setFileId(fileId);   //수정된 파일의 file id를 front에 적용해줌

        return vo;
    }

    public BaseVO deleteClient(ClientVO reqVo) throws Exception {
        BaseVO vo = clientDao.getClientById(reqVo.getClntIdTemp());
        clientDao.deleteClient(reqVo);
        return vo;
    }

    public void deleteClients(List<ClientVO> list) throws Exception {
        for (ClientVO tmp : list) {
            clientDao.deleteClient(tmp);
        }
    }
}
