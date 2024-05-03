package org.zerock.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j;

/**
 * 스프링 시큐리티 5 부터는 PasswordEncoder를 지정해야 한다.
 * 스프링 시큐리티의 PasswordEncoder는 인터페이스로 설계되어 잇고, 여러 종류의 구현 클래스가 존재한다
 * 
 * NoOpPasswordEncoder 는 스프링 5 버전부터 Deprecated 되었으므로
 * 직접 암호화 없는 PasswordEncoder를 구현한 클래스
 */
@Log4j
public class CustomNoOpPasswordEncoder implements PasswordEncoder {

	public String encode(CharSequence rawPassword) {

		log.warn("before encode :" + rawPassword);

		return rawPassword.toString();
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {

		log.warn("matches: " + rawPassword + ":" + encodedPassword);

		return rawPassword.toString().equals(encodedPassword);
	}

}
