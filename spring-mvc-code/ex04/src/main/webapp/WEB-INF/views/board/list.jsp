<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<!-- JSTL 사용을 위한 태그 라이브러리 추가 -->    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- 중복 되는 코드 include 지시자 활용 -->
<%@ include file="../includes/header.jsp" %>

<script type="text/javascript">
	$(document).ready(function(){
		
		var result = '<c:out value="${result}"/>'; //boardController 에서 전달

		// CURD 후 모달창 띄우기
		checkModal(result);

		// 뒤로가기 문제
		// '뒤로가기' 나 '앞으로가기' 를 하면 서버가 다시 호출되는게 아니라, 과거의 데이터를 활용한다. (등록완료 후 뒤로가면 모달창이 계속 띄워지는 등)
		// window 의 history 객체를 이용해서 모달창을 다시 띄우지 않도록 한다
		history.replaceState({}, null, null); // history 스택 이전기록 대체

		function checkModal(result){
			
			// history.state 체크 (모달창 다시 안띄우도록)
			if(result === '' || history.state){
				return;
			}

			if(parseInt(result) > 0){ // result에 등록된 글 번호 담겨옴
				$('.modal-body').html("게시글 " + parseInt(result) + "번이 등록되었습니다.");
			}
			$('#myModal').modal("show"); // 모달창 띄우기
		}
		
		// [글쓰기] 버튼 클릭 시, 등록 페이지 이동
		$('#regBtn').on("click", function(){
			self.location = "/board/register";
		});

		// [페이지 번호] 클릭 시, 
		var actionForm = $("#actionForm");

		$('.paginate_button a').on('click', function(e){
			e.preventDefault(); // a태그 페이지 이동 막기

			// console.log('click');

			actionForm.find("input[name='pageNum']").val($(this).attr("href")); // pageNum 값을 href 속성 값으로 변경 => form 태그 내의 페이지 번호 변경
			actionForm.submit(); // 페이지 이동
		})

		// 글제목 클릭 시 상세보기 페이지 이동
		$(".move").on("click", function(e){
			e.preventDefault();

			actionForm.append("<input type='hidden' name='bno' value='"+$(this).attr("href")+"'>"); // input 태그 생성
			actionForm.attr("action", "/board/get");
			actionForm.submit();
		})

		// searchForm
		var searchForm = $('#searchForm');
		$('#searchForm button').on('click', function(e){
			if(!searchForm.find('option:selected').val()){
				alert("검색종류를 선택하세요.");
				return false;
			}

			if(!searchForm.find("input[name='keyword']").val()){
				alert("키워드를 입력하세요");
				return false;
			}

			searchForm.find("input[name='pageNum']").val("1");
			e.preventDefault();
			searchForm.submit();
		})
		
	});
</script>

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">게시판</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                게시판 목록
                <button id="regBtn" type="button" class="btn btn-xs pull-right">글쓰기</button><!-- 등록 페이지 이동 -->
            </div>
            <!-- /.panel-heading -->
            
            <div class="panel-body">
                <table width="100%" class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>#번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>수정일</th>
                        </tr>
                    </thead>
                    
                    <c:forEach items="${list}" var="board">
                    
	                    <tr>
	                    	<td><c:out value="${board.bno}"/></td>
	                    	<td><!-- 제목 클릭 시 페이지 이동 -->
	                    		<a class="move" href='<c:out value="${board.bno}"/>'>
	                    			<c:out value="${board.title}"/>   <b>[  <c:out value="${board.replyCnt}" />  ]</b> 
	                    		</a>
	                    	</td>
	                    	<td><c:out value="${board.writer}"/></td>
	                    	<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.regdate}"/></td>
	                    	<td><fmt:formatDate pattern="yyyy-MM-dd" value="${board.updateDate }"/></td>
	                    </tr>
                    </c:forEach>
                </table>
                <!-- /.table-responsive -->
                
                <div class="row">
                	<div class="col-lg-12">
                		<form id='searchForm' action="/board/list" method="get">
                			<select name='type'>
                				<option value='' <c:out value="${pageMaker.cri.type == null ? 'selected' : ''}"/>>--</option>
                				<option value='T' <c:out value="${pageMaker.cri.type eq 'T' ? 'selected' : ''}"/>>제목</option>
                				<option value='C' <c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : ''}"/>>내용</option>
                				<option value='W' <c:out value="${pageMaker.cri.type eq 'W' ? 'selected' : ''}"/>>작성자</option>
                				<option value='TC' <c:out value="${pageMaker.cri.type eq 'TC' ? 'selected' : ''}"/>>제목 or 내용</option>
                				<option value='TW' <c:out value="${pageMaker.cri.type eq 'TW' ? 'selected' : ''}"/>>제목 or 작성자</option>
                				<option value='TWC' <c:out value="${pageMaker.cri.type eq 'TWC' ? 'selected' : ''}"/>>제목 or 내용 or 작성자</option>
                			</select>
                			
                			<input type='text' name='keyword' value='<c:out value="${pageMaker.cri.keyword}"/>'/>
                			<input type='hidden' name='pageNum' value='<c:out value="${pageMaker.cri.pageNum}"/>'/>
                			<input type='hidden' name='amount' value='<c:out value="${pageMaker.cri.amount}"/>'/>
                			<button class='btn btn-default'>검색</button>
                		</form>
                	</div>
                </div>
                
                <!-- 페이징 처리  -->
                <div class="pull-right">
                	<ul class="pagination">
                		<c:if test="${pageMaker.prev}">
                			<li class="paginate_button previous">
                				<a href="${pageMaker.startPage-1 }">이전</a>
                			</li>
                		</c:if>
                		
                		<!-- 페이지 목록 / c:out 대신 가독성을 위해 el 태그 사용 -->
                		<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage }">
                			<li class="paginate_button ${pageMaker.cri.pageNum == num ? "active": ""}">
                				<a href="${num }">${num }</a>
                			</li>
                		</c:forEach>
                		
                		<c:if test="${pageMaker.next}">
                			<li class="paginate_button next">
                				<a href="${pageMaker.endPage + 1 }">다음</a>
                			</li>
                		</c:if>
                	</ul>
                	
                	<!-- 실제 페이지 클릭 시 동작하는 form 태그 -->
                	<form id="actionForm" action="/board/list" method="get">
                		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
                		<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
                		<input type="hidden" name="type" value='<c:out value="${pageMaker.cri.type }"/>'>
                		<input type="hidden" name="keyword" value='<c:out value="${pageMaker.cri.keyword }"/>'>
                	</form>
                </div>
                <!-- end Pagination -->
                
                
                <!-- Modal 추가 -->
                <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                	<div class="modal-dialog">
                		<div class="modal-content">
                			<div class="modal-header">
                				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                				<h4 class="modal-title" id="myModalLabel">Modal title</h4>
                			</div>
                			<div class="modal-body">처리가 완료되었습니다.</div>
                			<div class="modal-footer">
                				<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                				<button type="button" class="btn btn-primary">변경사항 저장</button>
                			</div>
                		</div>
                	</div><!-- /#myModal -->
                </div>
                
            </div>
            <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
    </div>
    <!-- /.col-lg-12 -->
</div>
            
            
<%@ include file="../includes/footer.jsp" %>