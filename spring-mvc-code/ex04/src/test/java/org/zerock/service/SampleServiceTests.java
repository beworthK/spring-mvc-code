package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class SampleServiceTests {

  //@Setter(onMethod = @__({ @Autowired }))
  @Setter(onMethod_ = @Autowired)
  private SampleService service;
  
  
  /*
   * AOP설정을 한 Target에 대해서 Proxy 객체가 정상적으로 만들어져 있는지를 확인
   * <aop:aspect-autoproxy></aop:aspect-autoproxy> 가 정상적으로 동작하고,
   * LogAdvice에 설정 문제가 없다면 service 변수의 클래스는 생성된 Proxy 클래스의 인스턴스가 된다. 
   */
//  @Test
  public void testClass() {
    
    log.info(service); 
    // toString() 결과
    // INFO : org.zerock.service.SampleServiceTests - org.zerock.service.SampleServiceImpl@4c550889
    
    log.info(service.getClass().getName()); 
    // getClass를 통해 service 변수의 클래스 확인
    // INFO : org.zerock.service.SampleServiceTests - com.sun.proxy.$Proxy34
    // com.sun.proxy.$Proxy - JDK의 다이나믹 프록시 기법이 적용된 결과
  }
  
  /*
   *  18.4.1 args를 이용한 파라미터 추적_테스트
   */
//  @Test
  public void testAdd() throws Exception {
    
    log.info(service.doAdd("123", "456"));
    
    /* 
    실행 결과
    	INFO : org.zerock.aop.LogAdvice - ======================== 
    	 ㄴ logBefore 메서드
    	INFO : org.zerock.aop.LogAdvice - str1: 123
		INFO : org.zerock.aop.LogAdvice - str2: 456
		 ㄴ logBeforeWithParam 메서드 : 명시된 doAdd() 가 실행되기 전, 파라미터 log를 찍는다
		INFO : org.zerock.service.SampleServiceTests - 579 
   */
  }
  
//  @Test
  public void testAddError() throws Exception {
    
    log.info(service.doAdd("123", "ABC")); // 강제로 exception 발생시키기
    /*
      실행 결과
      	INFO : org.zerock.aop.LogAdvice - ========================
		INFO : org.zerock.aop.LogAdvice - str1: 123
		INFO : org.zerock.aop.LogAdvice - str2: ABC
		 ㄴ logBeforeWithParam 로그
		INFO : org.zerock.aop.LogAdvice - Exception....!!!!
		INFO : org.zerock.aop.LogAdvice - exception: java.lang.NumberFormatException: For input string: "ABC"
      	 ㄴ logException 로그
     */
    
  }  


  
}

