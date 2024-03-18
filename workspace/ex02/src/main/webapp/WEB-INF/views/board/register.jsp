<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp" %>

<!-- 
	[등록] 페이지
 -->
 
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">게시글 등록</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">등록하기</div>
			
			<div class="panel-body">
				<form role="form" action="/board/register" method="post">
					<div class="form-group">
						<label>제목</label>
						<input class="form-control" name="title"></input>
					</div>
					<div class="form-group">
						<label>내용</label>
						<textarea class="form-control" rows="3" name="content"></textarea>
					</div>
					<div class="form-group">
						<label>작성자</label>
						<input class="form-control" name="writer"></input>
					</div>
					<button type="submit" class="btn btn-default">등록</button>
					<button type="reset" class="btn btn-default">새로고침</button>
				</form>
			</div>
		</div>
	</div>
</div>
<%@ include file="../includes/footer.jsp" %>