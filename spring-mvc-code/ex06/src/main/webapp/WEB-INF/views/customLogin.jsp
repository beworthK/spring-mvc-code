<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<h1>Custom Login Page</h1>
	<h2><c:out value="${error}"/></h2>
	<h2><c:out value="${logout}"/></h2>
	
	<form method='post' action="/login"> <!-- action 값 '/login' 기 -->
	
	<div>
		<input type='text' name='username' value='admin'><!-- 기본적으로는 username과 password 속성을 이용 -->
	</div>
	<div>
		<input type='password' name='password' value='admin'><!-- 기본적으로는 username과 password 속성을 이용 -->
	</div>
	<div>
		<input type='checkbox' name='remember-me'> 로그인 상태 유지
	</div>
	<div>
	  	<input type='submit'>
	</div>
	
	<!--  
	이 EL은 실제 브라우저에서는 '_csrf'이름으로 처리된다. value 값은 임의로 저장된다
	
	CSRF (Cross-site request forgery) 토큰
		- 사용자간 위조 방지 를 목적으로 특정한 값의 토큰을 사용하는 방식
		- 스프링 시큐리티에서 POST 방식을 사용하는 경우 기본적으로 CSRF 토큰이 사용됨
		- 사용자가 임의로 변하는 특정한 토큰 값을 서버에서 체크하는 방식
		- 서버에서 브라우저로 데이터를 전송할 때 CSRF 토큰을 같이 전송한 후,
		  사용자가 POST 방식 등으로 특정한 작업을 할 때 브라우저에서 전송된 CSRF 토큰의 값과 
		  서버가 보관하고 있는 토큰의 값을 비교하여, 값이 다르면 작업을 처리하지 않는 방식
		  
	CSRF (Cross-site request forgery) 공격
		- 사이트간 요청 위조. 서버에서 받아들이는 정보가 특별히 사전 조건을 검증하지 않는다는 단점을 이용한 공격방식
		- 사용자의 요청에 대한 출처를 의미하는 referer 헤더를 체크하거나 
		  REST 방식에서 사용되는 PUT, DELETE 와 같은 방식 이용 	
	-->
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	
	</form>
  
</body>
</html>
