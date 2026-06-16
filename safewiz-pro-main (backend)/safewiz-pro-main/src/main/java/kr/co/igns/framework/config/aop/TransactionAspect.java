package kr.co.igns.framework.config.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.aop
 * @ 파일명		: TransactionAspect.java
 * @ 기능명 		: 트랜잭션 설정
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

@Configuration
public class TransactionAspect {

	@Autowired
	private PlatformTransactionManager transaction01Manager;

	private static final String EXPRESSION = "execution(* kr.co.igns.fems..service.*Service.*(..)) || execution(* kr.co.igns.business..service.*Service.*(..)) || execution(* kr.co.igns.framework..service.*Service.*(..))";

	@SuppressWarnings("deprecation")
	@Bean
	public TransactionInterceptor transactionAdvice() {

		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*");

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		return new TransactionInterceptor(transaction01Manager, attributeSource);
	}

	@Bean
	public Advisor transactionAdvisor() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);

		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}

}
