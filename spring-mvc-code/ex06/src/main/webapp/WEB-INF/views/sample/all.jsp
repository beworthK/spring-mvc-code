<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
</head>
<body>
	<!-- all or member or admin -->
	<h1>/sample/all page</h1>
	
	<!-- 스프링시큐리티 조건식 -->
	
	<!-- isAnonymous() : 익명의 사용자인 경우(로그인 하지 않은 경우 포함)  -->
	<sec:authorize access="isAnonymous()">
		<a href="/customLogin">로그인</a>
	</sec:authorize>
	
	<!-- isAuthenticated() : 인증된 사용자면 true return -->
	<sec:authorize access="isAuthenticated()">
		<a href="/customLogout">로그아웃</a>
	</sec:authorize>
	
	<!-- 그 외
		hasRole( [role] ) : 해당 권한이 있으면 true 
		hasAuthority( [authority] ) : 해당 권한이 있으면 true
		hasAnyRole( [role,role2] ) : 여러 권한들 중에서 하나라도 해당하는 권한이 있으면 true 
		hasAnyAuthority( [authority] ) : 여러 권한들 중에서 하나라도 해당하는 권한이 있으면 true
		
		permitAll : 모든 사용자에게 허용
		denyAll : 모든 사용자에게 거부
		
		isFullyAuthenticated() : Remember-me(자동로그인)으로 인증된 것이 아닌 인증된 사용자인 경우 true
	-->

</body>
</html>
