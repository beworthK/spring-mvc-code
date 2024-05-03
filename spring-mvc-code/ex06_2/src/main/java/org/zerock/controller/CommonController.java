package org.zerock.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;


@Controller
@Log4j
public class CommonController {

	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {
		
		log.info("access Denied : " + auth);
		
		model.addAttribute("msg", "Access Denied"); // 간단히 사용자가 알아볼 수 있는 에러 메시지만을 Model에 추가한다
	}

	// login-page 속성의 URI 는 반드시 GET 방식
	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {

		log.info("error: " + error);
		log.info("logout: " + logout);

		if (error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}

		if (logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}
	
	// 로그아웃 - get 방식(login-page 속성)
	@GetMapping("/customLogout")
	public void logoutGET() {
		
		log.info("custom logout");
	}

	// 로그아웃 - post 방식 
	@PostMapping("/customLogout")
	public void logoutPost() {
		
		log.info("post custom logout");
	}

}
