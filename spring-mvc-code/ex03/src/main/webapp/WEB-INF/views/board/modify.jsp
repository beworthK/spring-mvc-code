<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp" %>

<!-- 
	[수정/삭제] 페이지
 -->
 
<script type="text/javascript">
$(document).ready(function(){
	var formObj = $("form");

	$('button').on("click", function(e){
		
		e.preventDefault(); // submit 자동처리 막기

		var operation = $(this).data("oper"); //<button> 태그의 data-oper 속성 가져오기

		console.log(operation);

		if(operation === 'remove'){
			// 글 삭제
			formObj.attr("action", "/board/remove"); //action 속성 변경
			
		} else if (operation === 'list'){
			// 목록 이동
			formObj.attr("action", "/board/list").attr("method", "get"); //action 속성과 method 속성 변경

			// 이전 목록 정보 담기
			var pageNumTag = $("input[name='pageNum']").clone();
			var amountTag = $("input[name='amount']").clone();
			var keywordTag = $("input[name='keyword']").clone();
			var typeTag = $("input[name='type']").clone();

			formObj.empty(); // form 태그 정보 삭제 (목록 이동 시, 수정/삭제 취소)
	
			// 목록 페이지로 페이징 & 검색 정보 전달
			formObj.append(pageNumTag);
			formObj.append(amountTag);
			formObj.append(keywordTag);
			formObj.append(typeTag);

			// self.location = "/board/list";
			// return;
			
		}

		formObj.submit();
	})	
})
</script>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">게시글 수정</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">수정하기</div>
			
			<div class="panel-body">
			
				<form role="form" action="/board/modify" method="post">
				
					<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum }"/>'>
					<input type="hidden" name="amount" value='<c:out value="${cri.amount }"/>'>
					<input type="hidden" name="type" value='<c:out value="${cri.type }"/>'>
					<input type="hidden" name="keyword" value='<c:out value="${cri.keyword }"/>'>
					
					<div class="form-group">
						<label>글번호</label> <input class="form-control" name='bno' value='<c:out value="${board.bno}"/>' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>제목</label> <input class="form-control" name='title' value='<c:out value="${board.title}"/>'>
					</div>
					
					<div class="form-group">
						<label>내용</label> <textarea class="form-control" rows="3" name='content'><c:out value="${board.content}"/></textarea>
					</div>
					
					<div class="form-group">
						<label>작성자</label> <input class="form-control" name='writer' value='<c:out value="${board.writer}"/>' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>등록일</label> <input class="form-control" name='regDate' value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.regdate}" />' readonly="readonly">
					</div>
					
					<div class="form-group">
						<label>수정일</label> <input class="form-control" name='updatedate' value='<fmt:formatDate pattern = "yyyy/MM/dd" value = "${board.updateDate}" />' readonly="readonly">
					</div>
				
					<button type="submit" data-oper='modify' class="btn btn-default">수정</button>
					<button type="submit" data-oper='remove' class="btn btn-default">삭제</button>
					<button type="submit" data-oper='list' class="btn btn-default">목록</button>
				</form>
				
			</div>
			
		</div>
		
	</div>
</div>
<%@ include file="../includes/footer.jsp" %>