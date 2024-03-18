package org.zerock.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 아래 순서대로 동작하는지 테스트하기 위해 작성하 테스트 코드
 * 1. 스프링 프레임워크가 시작되면, 스프링이 사용하는 메모리 영역(=Context)을 만들게 된다. 스프링에서 ApplicationContext 객체가 만들어진다.
 * 2. root-context.xml 에서 스프링이 생성하고 관리해야하는 객체에 대한 설정을 지정
 * 3. root-comtext.xml 에 설정되어있는 <context:component-scan> 태그 내용을 통해 sample 패키지 스캔
 * 4. Restaurant 객체는 Chef 객체가 필요하다는 어노테이션(=@Autowired) 설정이 있으므로 스프링은 Chef 객체의 레퍼런스를 Restaurant 객체에 주입한다.
 * 
 */
/**
 * @RunWith : 현재 테스트 코드가 스프링을 실행하는 역할(SpringJUnit4ClassRunner)을 할것임을 표시 
 * @ContextConfiguration : 지정된 클래스나 문자열을 이용해서 필요한 객체들을 스프링 내에 Bean(빈)으로 등록
 * @Log4j : Lombok을 이용해서 로그를 기록하는 Logger 를 변수로 생성
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleTests {
	
	@Setter(onMethod_ = {@Autowired})
	private Restaurant restaurant;
	
	@Test // JUnit 에서 테스트 대상을 표시하는 어노테이션. 해당 method를 선택하고 JUnit Test 기능을 실행 
	public void testExist() { // 
		assertNotNull(restaurant); // [restaurant] 변수가 null이 아니어야만 테스트가 성공한다.
		
		log.info(restaurant);
		log.info("--------------------");
		log.info(restaurant.getChef());
	}
}
