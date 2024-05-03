package org.zerock.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;



@Log4j
@RequestMapping("/sample/*")
@Controller
public class SampleController {
	
	//로그인을 하지 않은 사용자도 접근 가능한 URI
	@GetMapping("/all")
	public void doAll() {
	
		log.info("do all can access everybody");
	}

	// 로그인 한 사용자들만이 접근할 수 있는 URI
	@GetMapping("/member")
	public void doMember() {
  
		log.info("logined member");
	}

	// 로그인한 사용자들 중에서 관리자 권한을 가진 사용자만이 접근할 수 있는 URI
	@GetMapping("/admin")
	public void doAdmin() {
  
		log.info("admin only");
	}
	
	
	/***********************************************
	 * 어노테이션을 이용하는 스프링 시큐리티 설정
	 * - 매번 필요한 URL 에 따라서 설정을 변경하는 일은 번거로우므로 
	 *   어노테이션을 이용해서 필요한 설정을 추가할 수 있다
	 ***********************************************/
	
	// @PreAuthorize, @PostAuthorize : () 안에 스프링 시큐리티 표현식을 사용할 수 있다
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@GetMapping("/annoMember")
	public void doMember2() {
	    
		log.info("logined annotation member");
	}
	
	// @Secured : 문자열 혹은 문자열 배열 이용(*값만 추가할 수 있다)
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/annoAdmin")
	public void doAdmin2() {
	
	  log.info("admin annotaion only");
	}
 
}
