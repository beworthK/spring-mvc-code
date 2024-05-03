<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp" %>

<!-- 
	[수정/삭제] 페이지
 -->
 
<!-- uploadResult css  -->
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
}

.uploadResult ul li img {
	width: 100px;
}
</style>
<!-- bigPicture css -->
<style>
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
}

.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
</style>

<!-- div#페이지 제목 -->
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">게시글 수정</h1>
	</div>
</div>

<!-- div#게시글 수정 -->
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

<!-- 첨부파일 원본 이미지 보기 -->
<div class='bigPictureWrapper'>
	<div class='bigPicture'>
	</div>
</div>

<!-- div#파일등록 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">파일 첨부</div>
    		<div class="panel-body">
				<div class="form-group uploadDiv">
					<input type="file" name='uploadFile' multiple>
				</div>      
				<div class='uploadResult'> 
					<ul>
					</ul>
      			</div>
			</div>
		</div>
	</div>
</div>

<!-- 게시글 수정 -->
<script type="text/javascript">
$(document).ready(function(){
	
	var formObj = $("form");

	$('button').on("click", function(e){
		
		e.preventDefault(); // submit 자동처리 막기

		var operation = $(this).data("oper"); //<button> 태그의 data-oper 속성 가져오기

		//console.log(operation);

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

		} else if (operation === 'modify'){
			// 글 수정
			var str = "";
			
			$(".uploadResult ul li").each(function(i, obj){
				var obj = $(obj);
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='" + obj.data("filename") + "'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='" + obj.data("uuid") + "'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='" + obj.data("path") + "'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='" + obj.data("type") + "'>";
			});
			
			formObj.append(str).submit();
		}

		formObj.submit();
	})	
});
</script>

<!-- 첨부파일 -->
<script>
$(document).ready(function(e){
	
	// 즉시 실행 함수를 이용해서 첨부파일 목록 가져오기
	(function(){
		
		var bno = '<c:out value="${board.bno}"/>';
		
		$.getJSON("/board/getAttachList", {bno: bno}, function(arr){
			
			//console.log(arr);
			
			var str = "";
			$(arr).each(function(i, attach){
				
				if(attach.fileType){ //image type
					var fileCallPath =  encodeURIComponent( attach.uploadPath+ "/s_"+attach.uuid +"_"+attach.fileName);
				
					str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' >";
					str += 	"<div>";
					str += 		"<span> "+ attach.fileName+"</span>";
					str += 		"<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' class='btn btn-warning btn-circle'>";
					str += 			"<i class='fa fa-times'></i>";
					str += 		"</button><br>";
					str += 		"<img src='/display?fileName="+fileCallPath+"'>";
					str += 	"</div></li>";
				}else{
					
					str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' >";
					str += 	"<div>";
					str += 		"<span> "+ attach.fileName+"</span><br/>";
					str += 		"<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' class='btn btn-warning btn-circle'>";
					str += 			"<i class='fa fa-times'></i>";
					str += 		"</button><br>";
					str += 		"<img src='/resources/img/attach.png'></a>";
					str += 	"</div></li>";
				}
			});
			
			$(".uploadResult ul").html(str);
			
		}); // $.getJSON
		
	})();//end function
	
	// 특정 첨부파일 삭제
	$(".uploadResult").on("click", "button", function(e){
		
		// 특정 첨부파일 삭제 시, 화면에서만 삭제하고 게시물 수정(submit)을 진행했을 시에 실제로 삭제한다 
		if(confirm("해당 파일을 삭제하시겠습니까?")){
			var targetLi = $(this).closest("li");
			targetLi.remove();
		}
	});
	
	// 첨부파일 추가
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880; //5MB

	// 파일 업로드 조건식 
	function checkExtension(fileName, fileSize){
		
		if(fileSize >= maxSize){
			alert("파일 사이즈 초과");
			return false;
		}
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		
		return true;
	}
	
	// 파일 업로드 시, 처리(input file 태그의 내용이 변경되는 것을 감지)
	$("input[type='file']").change(function(e){

		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;

		for(var i = 0; i < files.length; i++){
			
			if(!checkExtension(files[i].name, files[i].size) ){
		    	return false;
		  	}
			formData.append("uploadFile", files[i]);
		}
		
		$.ajax({ // 파일 저장
			url: '/uploadAjaxAction',
			processData: false, 
			contentType: false,
			data:formData,
			type: 'POST',
			dataType:'json',
			success: function(result){
				//console.log(result); 
				showUploadResult(result); //파일 업로드 결과 처리 함수 
		  	}
		}); //$.ajax

	}); // $("input[type='file']")

	// 파일 업로드 결과 처리 함수 
	function showUploadResult(uploadResultArr){
		    
		if(!uploadResultArr || uploadResultArr.length == 0){ return; }
			
		var uploadUL = $(".uploadResult ul");
			
		var str ="";
			
		$(uploadResultArr).each(function(i, obj){
			
			if(obj.image){ //image type
				var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
				str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
				str += 	"<div>";
				str += 		"<span> "+ obj.fileName+"</span>";
				str += 		"<button type='button' data-file=\'"+obj.fileName+"\' data-type='image' class='btn btn-warning btn-circle'>";
				str += 			"<i class='fa fa-times'></i>";
				str += 		"</button><br>";
				str += 		"<img src='/display?fileName="+fileCallPath+"'>";
				str += 	"</div></li>";
			}else{
				var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);            
				var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
				
				str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
				str += 	"<div>";
				str += 		"<span> "+ obj.fileName+"</span>";
				str += 		"<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' class='btn btn-warning btn-circle'>";
				str += 			"<i class='fa fa-times'></i>";
				str += 		"</button><br>";
				str += 		"<img src='/resources/img/attach.png'></a>";
				str += 	"</div></li>";
			}
		});
		    
		uploadUL.append(str);
		    
	} // fnc_showUploadResult
	
});
</script>

<%@ include file="../includes/footer.jsp" %>