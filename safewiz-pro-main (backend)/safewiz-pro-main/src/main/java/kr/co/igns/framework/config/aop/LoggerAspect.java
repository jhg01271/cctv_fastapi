package kr.co.igns.framework.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.aop
 * @ 파일명		: LoggerAspect.java
 * @ 기능명 		: AOP 설정
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Component
@Aspect
public class LoggerAspect {
	private static final Logger logger = LogManager.getLogger("com.project");
	
	@Around("execution(* kr.co.igns.system..controller.*Controller.*(..)) || execution(* kr.co.igns.system..service.*Service.*(..)) || execution(* kr.co.igns.business..controller.*Controller.*(..)) || execution(* kr.co.igns.business..service.*Service.*(..)) ") //|| execution(* mappers.postgres..*Mapper.*(..)) || execution(* mappers.db2..*Mapper.*(..)) // 오류가 나서 임시로 주석 
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();

		if (name.contains("Controller") == true) {
			type = "Controller ===> ";

		} else if (name.contains("Service") == true) {
			type = "ServiceImpl ===> ";

		} else if (name.contains("Mapper") == true) {
			type = "Mapper ===> ";
		}
		
		String methodName = joinPoint.getSignature().getName();
	    Object[] args = joinPoint.getArgs();

	    // 인자 배열을 문자열로 변환
	    StringBuilder argString = new StringBuilder();
	    for (int i = 0; i < args.length; i++) {	        
	    	argString.append("arg[").append(i).append("]: ");
	        argString.append(args[i] != null ? args[i].toString() : "null");
	        if (i < args.length - 1) argString.append(","+'\n');
	    }

	    logger.info(type + name+ "." + methodName+"\n"+argString);
		return joinPoint.proceed();
	}

}
