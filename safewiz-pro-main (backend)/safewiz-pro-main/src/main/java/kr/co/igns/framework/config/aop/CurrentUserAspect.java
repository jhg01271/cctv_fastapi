package kr.co.igns.framework.config.aop;
import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.utils.type.StringUtils;
import kr.co.igns.system.common.model.BaseVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.framework.config.aop
 * ※ 파일명 : CurrentUserAspect.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 5. 10.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Aspect
@Component
public class CurrentUserAspect {

	private static final Logger logger = LogManager.getLogger("com.project");

	@Pointcut("execution(* kr.co.igns.business..service.*Service.*(..)) || execution(* kr.co.igns.framework..service.*Service.*(..))")
	private void publicTarget() {
	}

	@Before("execution(* kr.co.igns.*..service.*Service.*(..)) && !within(kr.co.igns.framework.config.security.jwt.service..*) && !execution(* kr.co.igns.system.user.service.LoginService.saveLoginHistory(..))")
	public void beforeServiceMethodExecution(JoinPoint joinPoint) {
//		for (Object arg : joinPoint.getArgs()) {
//			if (arg instanceof BaseVO) {
//				setCommonFields((BaseVO) arg);
////				break;  // BaseVO를 찾았으므로 검사 종료
//			} else if (arg instanceof List<?>) {
//				List<?> list = (List<?>) arg;
//				for (Object item : list) {
//					if (item instanceof BaseVO) {
//						setCommonFields((BaseVO) item);
////						break;  // BaseVO를 찾았으므로 검사 종료
//					}
//				}
//			} else if (arg instanceof Map<?, ?>) {
//				Map<?, ?> map = (Map<?, ?>) arg;
//				for (Object value : map.values()) {
//					if (value instanceof BaseVO) {
//						setCommonFields((BaseVO) value);
////						break;  // BaseVO를 찾았으므로 검사 종료
//					}
//				}
//			}
//		}
		for (Object arg : joinPoint.getArgs()) {
			processObject(arg); // 모든 매개변수에 대해 재귀 처리
		}
	}
	private void processObject(Object obj) {
		if (obj instanceof BaseVO) {
			// 단일 BaseVO 객체 처리
			setCommonFields((BaseVO) obj);

			// BaseVO 내부 필드 탐색 (리스트나 맵이 있을 경우 처리)
			for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true); // private 필드 접근 허용
				try {
					Object fieldValue = field.get(obj);
					if (fieldValue != null) {
						processObject(fieldValue); // 재귀 호출
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} else if (obj instanceof List<?>) {
			// 리스트 내부 객체 탐색
			List<?> list = (List<?>) obj;
			for (Object item : list) {
				processObject(item); // 재귀 호출
			}
		} else if (obj instanceof Map<?, ?>) {
			// 맵 내부 값 탐색
			Map<?, ?> map = (Map<?, ?>) obj;
			for (Object value : map.values()) {
				processObject(value); // 재귀 호출
			}
		}
	}

	private void setCommonFields(BaseVO vo) {
		String currentMemberId = SecurityUtil.getCurrentMemberId();
		vo.setCreatedBy(currentMemberId);
		vo.setUpdatedBy(currentMemberId);	

		String role = SecurityContextHolder.getContext()
                  .getAuthentication()
                  .getAuthorities()
                  .iterator()
                  .next()
                  .getAuthority(); // "ROLE_USER" 같은 값

        String currentMemberGroup = role.replaceFirst("ROLE_", "");
		vo.setRole(currentMemberGroup);
		
		String compId = vo.getCompId();
		if(StringUtils.isBlank(vo.getCompId())){
			compId = SecurityUtil.getCurrentCompId();
			vo.setCompId(compId);
		}
		
		String clntId = SecurityUtil.getCurrentClntId();
		vo.setClntId(clntId);
		String currentHrId = SecurityUtil.getCurrentHrId();
		vo.setCurrentUserHrId(currentHrId);
		
		logger.info("setCommonFields : createdBy={},updatedBy={},role={},compId={},clntId={},hrId={}", vo.getCreatedBy(), vo.getUpdatedBy(), vo.getRole(), vo.getCompId(), vo.getClntId(), vo.getCurrentUserHrId());
	}

}
