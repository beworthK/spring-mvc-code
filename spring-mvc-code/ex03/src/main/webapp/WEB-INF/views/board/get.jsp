<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="../includes/header.jsp" %>

<!-- 
	[상세보기] 페이지
	
	댓글 화면 처리하는 방식
	- 게시물을 조회하는 페이지에 들어오면, 기본적으로 가장 '오래된' 댓글들을 가져와서 1페이지를 보여준다
	- 1페이지의 게시물을 가져올 때, 해당 게시물의 댓글의 숫자를 파악해서 댓글의 페이지 번호를 출력한다.
	- 댓글이 추가되면, 댓글의 숫자만 가져와서 '최종 페이지'를 찾아서 이동한다
	- 댓글의 수정과 삭제 후에는, 다시 '동일' 페이지를 호출한다
	
 -->
 
<!-- 댓글 모듈 -->
<script type="text/javascript" src="/resources/js/reply.js"></script>
 
 <!-- 댓글 관련 -->
<script type="text/javascript">
$(document).ready(function(){
	
	var bnoValue = '<c:out value="${board.bno}"/>'; // 게시글번호
	var replyUL = $(".chat");
	
	showList(1); // 최초 진입시 첫번째 댓글 목록 조회
	
	// 댓글 목록 조회
	function showList(page){
		
		//
		replyService.getList({bno:bnoValue, page: page || 1}, function(replyCnt, list){
			
			if(page == -1){ // 마지막 페이지를 찾아서 다시 호출 
				// 사용자가 댓글을 추가하면 showList(-1); 을 호출하여 전체 댓글 수를 파악한 후, 마지막 페이지를 호출해서 이동한다
				pageNum = Math.ceil(replyCnt/10.0);
				showList(pageNum);
				return;
			}
			
			
			var str = "";
			if( list == null || list.length == 0){
				replyUL.html("");
				return;
			}
			
			// 댓 목록 html 생성
			for (var i = 0, len = list.length || 0; i < len; i++){
				str += "<li class='list clearfix' data-rno='" + list[i].rno + "'>";
				str += "<div><div class='header'><strong class='primary-font'>" + list[i].replyer + "</strong>"; // 댓글작성자
				str += "<small class='pull-right text-muted'>" + replyService.displayTime(list[i].replyDate) + "</small></div>"; // 댓글작성일시
				str += "<p>" + list[i].reply + "</p></div></li>"; //댓글
			}

			replyUL.html(str);
			
			// 댓글 페이징 html 처리(panel-footer)
			showReplyPage(replyCnt);
			
		});
		
	}// end fn_showList
	
	
	// 댓글 페이징 처리
	var pageNum = 1;
	var replyPageFooter = $(".panel-footer");
	
	function showReplyPage(replyCnt){
	  
	  var endNum = Math.ceil(pageNum / 10.0) * 10;  
	  var startNum = endNum - 9; 
	  
	  var prev = startNum != 1;
	  var next = false;
	  
	  if(endNum * 10 >= replyCnt){
	    endNum = Math.ceil(replyCnt/10.0);
	  }
	  
	  if(endNum * 10 < replyCnt){
	    next = true;
	  }
	  
	  //페이징 html
	  var str = "<ul class='pagination pull-right'>";
	  
	  if(prev){
	    str+= "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>Previous</a></li>";
	  }
	  
	  for(var i = startNum ; i <= endNum; i++){
	    var active = pageNum == i? "active":"";
	    str+= "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
	  }
	  
	  if(next){
	    str+= "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>Next</a></li>";
	  }
	  
	  str += "</ul></div>";
	  
	  //console.log(str);
	  
	  replyPageFooter.html(str);
	  
	} // end fn_showReplyPage
	
	
	// 댓글 모달창
	var modal = $('.modal');
	var modalInputReply = modal.find("input[name='reply']");
	var modalInputReplyer = modal.find("input[name='replyer']");
	var modalInputReplyDate = modal.find("input[name='replyDate']");

	var modalModBtn = $('#modalModBtn');
	var modalRemoveBtn = $('#modalRemoveBtn');
	var modalRegisterBtn = $('#modalRegisterBtn');

	// [댓글쓰기] 버튼 클릭 시, 모달창 보이기
	$("#addReplyBtn").on('click', function(e){
		
		modal.find("input").val("");
		modalInputReplyDate.closest('div').hide();
		modal.find("button[id != 'modalCloseBtn']").hide();

		modalRegisterBtn.show();

		$(".modal").modal("show");
	});
	
	// 모달[등록] 버튼 클릭 시, 댓글 등록 및 목록 갱신
	modalRegisterBtn.on("click", function(e){
		
		// 파라미터 담기
		var reply = {
			reply: modalInputReply.val(),
			replyer: modalInputReplyer.val(),
			bno: bnoValue
		};
		
		// 댓글 등록 ajax
		replyService.add(reply, function(result){
			alert(result);
			
			modal.find("input").val(""); //동일한 내용등록 못하도록 제거(도배방지)
			modal.modal("hide"); //모달창 숨기기
			
			showList(-1); //댓글목록 불러오기(댓글 등록 후에는 전체 댓글 수를 파악한 후, 마지막 페이지를 호출해서 이동한다)
		});
	});
	
	// 모달[수정] 버튼 클릭 시, 댓글 수정 및 목록 갱신
	modalModBtn.on("click", function(e){

		// 파라미터 담기
  		var reply = { rno:modal.data("rno"), reply: modalInputReply.val() };
  		
  		// 댓글 수정 ajax
		replyService.update(reply, function(result){
			alert(result);
			
			modal.modal("hide");
			
			showList(pageNum);
		});
	});

	// 모달[삭제] 버튼 클릭 시, 댓글 삭제 및 목록 갱신
	modalRemoveBtn.on("click", function (e){
		var rno = modal.data("rno");
		
		replyService.remove(rno, function(result){
	   	      alert(result);

	   	      modal.modal("hide");
	   	      
	   	      showList(pageNum);
		});
	});
	
	// 특정 댓글 클릭 이벤트 처리 
	$(".chat").on("click", "li", function(e){ // click 이벤트 위임
		
		var rno = $(this).data("rno"); //댓글번호
		
		replyService.get(rno, function(reply){
  
			modalInputReply.val(reply.reply);
			modalInputReplyer.val(reply.replyer);
			modalInputReplyDate.val( replyService.displayTime(reply.replyDate) ).attr("readonly", "readonly");
			modal.data("rno", reply.rno);
			
			modal.find("button[id !='modalCloseBtn']").hide(); //버튼숨기기
			modalModBtn.show();
			modalRemoveBtn.show();
		
			$(".modal").modal("show");
  		});
	});
	
	// 댓글 페이지 번호 클릭 시, 새로운 댓글 목록 조회
	replyPageFooter.on("click","li a", function(e){
		e.preventDefault(); // a태그 기본 동작 제한
		
		var targetPageNum = $(this).attr("href");
		pageNum = targetPageNum;
		
		showList(pageNum); // 댓글 목록 조회
	});     
	
});
</script>

<script type="text/javascript">
$(document).ready(function() {
	// 수정 / 목록 form
	var operForm = $("#operForm");
	
	// 수정버튼 클릭 시 
	$("button[data-oper='modify']").on("click", function(e){
		operForm.attr("action", "/board/modify").submit();
	});
	
	// 목록버튼 클릭 시 	
	$("button[data-oper='list']").on("click", function(e){
		operForm.find("#bno").remove(); // 글번호 불필요하므로 제거
		operForm.attr("action", "/board/list");
		operForm.submit();
	});
});
</script>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">게시글 상세보기</h1>
	</div>
</div>

<div class="row">
	<!-- 본문 -->
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">상세보기</div>
			<!-- read 페이지므로 readonly를 지정해둔다  -->
			<div class="panel-body">
				<div class="form-group">
					<label>글번호</label>
					<input class="form-control" name='bno' value='<c:out value="${board.bno}"/>' readonly="readonly">
				</div>
				
				<div class="form-group">
					<label>제목</label>
					<input class="form-control" name='title' value='<c:out value="${board.title}"/>' readonly="readonly">
				</div>
				
				<div class="form-group">
					<label>내용</label>
					<textarea class="form-control" rows="3" name='content' readonly="readonly"><c:out value="${board.content}"/></textarea>
				</div>
				
				<div class="form-group">
					<label>작성자</label>
					<input class="form-control" name='writer' value='<c:out value="${board.writer}"/>' readonly="readonly">
				</div>
			
				<button data-oper='modify' class="btn btn-default">수정</button>
				<button data-oper='list' class="btn btn-info">목록</button>
				
				<!-- 확장성을 위해 form 태그를 이용 -->
				<form id='operForm' action="/board/modify" method="get">
					<input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
					<!-- 페이지 이동 시 페이징 정보를 가지고 감  -->
					<input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>
					<input type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
					<!-- 페이지 이동 시 검색 정보를 가지고 감  -->
					<input type='hidden' name='keyword' value='<c:out value="${cri.keyword}"/>'>
					<input type='hidden' name='type' value='<c:out value="${cri.type}"/>'>
				</form>
				
			</div>
			
		</div>	
	</div>
	
	<!-- 댓글 목록 -->
	<div class="col-lg-12">
		<div class="panel panel-default">
			
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i> 댓글
				<button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">댓글쓰기</button>
			</div>
			
			<div class="panel-body">
				<ul class="chat">
					
				</ul>
			</div>
			
			<div class="panel-footer">
			
			</div>
		</div>
	</div>
	<!-- // 댓글 목록 -->
	
</div>

<!-- 댓글 등록 모달 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">댓글창</h4>
			</div>
			<div class="modal-body">
				<div class="form-grouop">
					<label>댓글</label>
					<input class="form-control" name="reply" value="">
				</div>
				<div class="form-group">
					<label>작성자</label>
					<input class="form-control" name="replyer" value="">
				</div>
				<div class="form-group">
					<label>작성일</label>
					<input class="form-control" name="replyDate" value="">
				</div>
			</div>
			<div class="modal-footer">
				<button id="modalModBtn" type="button" class="btn btn-warning">수정</button>
				<button id="modalRemoveBtn" type="button" class="btn btn-danger">삭제</button>
				<button id="modalRegisterBtn" type="button" class="btn btn-primary">등록</button>
				<button id="modalCloseBtn" type="button" class="btn btn-default">닫기</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="../includes/footer.jsp" %>


