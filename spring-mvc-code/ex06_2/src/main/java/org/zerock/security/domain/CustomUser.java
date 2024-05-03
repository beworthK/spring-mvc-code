package org.zerock.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.domain.MemberVO;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private MemberVO member;

	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	// MemberVO를 파라미터로 전달해서 User 클래스에 맞게 생성자를 호출
	public CustomUser(MemberVO vo) {
		super(vo.getUserid()
				, vo.getUserpw()
				, vo.getAuthList().stream().map( auth -> new SimpleGrantedAuthority(auth.getAuth()) ).collect( Collectors.toList()) );
		
		this.member = vo;
	}
}
