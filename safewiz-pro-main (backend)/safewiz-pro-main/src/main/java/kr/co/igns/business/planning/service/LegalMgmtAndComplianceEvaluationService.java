package kr.co.igns.business.planning.service;

import kr.co.igns.business.planning.dao.postgres.LegalMgmtAndComplianceEvaluationDAO;
import kr.co.igns.business.planning.dao.postgres.LegalManageDAO;
import kr.co.igns.business.planning.model.LegalMgmtAndComplianceEvaluationVO;
import kr.co.igns.business.planning.model.LegalManageDetailVO;
import kr.co.igns.business.planning.model.LegalManageVO;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.system.common.model.BaseVO;
import kr.co.igns.system.common.model.SpSearchVO;
import kr.co.igns.system.common.util.SpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LegalMgmtAndComplianceEvaluationService {
    private final LegalMgmtAndComplianceEvaluationDAO legalMgmtAndComplianceEvaluationDao;

    public List<LegalMgmtAndComplianceEvaluationVO> getLegalMgmtAndComplianceEvaluation(SpSearchVO searchVo) throws Exception {
        List<LegalMgmtAndComplianceEvaluationVO> voList = legalMgmtAndComplianceEvaluationDao.getLegalMgmtAndComplianceEvaluation(searchVo);
        return voList;
    }
}
