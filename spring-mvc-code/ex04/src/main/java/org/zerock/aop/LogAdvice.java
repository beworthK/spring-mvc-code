package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

/**
 * AOP(Aspect Oriented Perspective) - 관점(관심사) 지향 프로그래밍 
 * - Aspect(관심사) : 추상적인 의미, 핵심 비즈니스 로직을 제외한 고려사항
 * - Advice : Aspect를 구현한 코드 
 * - Target : 순수한 비즈니스 로직
 * - Proxy : Target을 전체적으로 감싸고 있는 존재, 필요한 관심사(Aspect)들을 거쳐서 Target을 호출하도록 작성된다
 * - JoinPoint : Target 객체가 가진 메서드
 * - Pointcut : 관심사(Aspect) 와 비즈니스 로직이 결합되는 지점
 * @Component - 스프링에서 빈(bean) 으로 인식
 * 
 * LogAdvice
 * - Aspect : 로그를 기록하는 일(반복적이면서도 필요한 기능이지만 핵심 로직은 아님)
 * - 로그를 기록해주는 Advice
 */

@Aspect
@Log4j
@Component
public class LogAdvice {

	
	/* Before Advice 구현 메소드
	 * - Target의 JoinPoint 를 호출하기 전 실행
	 * 표현식
	 *  - 'execution' : 접근제한자(맨 앞의 *) 와 특정 클래스의 메서드(맨 뒤의 *) 지정
 	 */
	@Before( "execution(* org.zerock.service.SampleService*.*(..))")
	public void logBefore() {
		log.info("========================");
	}
	
	/* 18.4.1 args를 이용한 파라미터 추적
	 * 
	 * 해당 메서드에 전달되는 파라미터가 무엇인지 기록
	 * 
	 * pointcut 설정
	 *  - doAdd() : 메서드 명시, 파라미터 타입 지정
	 *  - && args( : 변수명 지정
	 */
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1, str2)")
	public void logBeforeWithParam(String str1, String str2) {
		
		log.info("str1: " + str1);
		log.info("str2: " + str2);
	}
	
	/* After Throwing Advice 구현 메소드
	 * - 예외가 발생한 뒤에 동작하는 코드
	 * 
	 */
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	public void logException(Exception exception) {
		
		log.info("Exception....!!!!");
		log.info("exception: "+ exception);
	}
	
	
	/* Around Advice 구현 메소드
	 * - @Around 는 직접 대상 메서드를 실행할 수 있는 권한을 가지고 있으며
	 *   메서드의 [실행 전]과 [실행 후]에 처리가 가능하다
	 * 
	 * - 메서드의 실행 자체를 제어할 수 있는 강력한 코드
	 * - 직접 대상 메서드를 호출하고, 결과나 예외를 처리할 수 있음
	 * 	 ㄴ 리턴타입을 설정하고, 실행결과를 직접 반환하는 형태로 작성해야 한다
	 * 
	 * 
	 * ProceedingJoinPoint 
	 *  - AOP의 대상이 되는 Target이나 파라미터 등 파악
	 *  - 직접 실행을 결정할 수 있음 
	 */
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	public Object logTime( ProceedingJoinPoint pjp) {
	  
	  long start = System.currentTimeMillis();
	  
	  log.info("Target: " + pjp.getTarget());
	  log.info("Param: " + Arrays.toString(pjp.getArgs()));
	  
	  //invoke method 
	  Object result = null; // 리턴타입
	  
	  try {
	    result = pjp.proceed();
	  } catch (Throwable e) { 
	    // 예외처리를 여기서 해주고 있으므로
		// 예외발생해도 logException(After Throwing Advice) 안탐!
	    e.printStackTrace();
	  }
	  
	  long end = System.currentTimeMillis();
	  
	  log.info("TIME: "  + (end - start));
	  
	  // 결과처리
	  return result;
	}
	
	

}
