<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> <!-- 스프링 시큐리티 태그 라이브러리 -->
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
</head>
<body>
	
	<h1>/sample/admin page</h1>
	
	<!-- <sec:authentication property="principal"/> 는 
		 UserDetailService에서 반환된 객체를 의미 (principal:현재 사용자 정보)
		 즉, principal = CustomUser -->
	<p>principal : <sec:authentication property="principal"/></p>
	<p>MemberVO : <sec:authentication property="principal.member"/></p><!-- CustomUser 객체의 getMember() 호출 -->
	<p>사용자이름 : <sec:authentication property="principal.member.userName"/></p>
	<p>사용자아이디 : <sec:authentication property="principal.username"/></p>
	<p>사용자 권한 리스트  : <sec:authentication property="principal.member.authList"/></p>
	
	<a href="/customLogout">Logout</a>

</body>
</html>
