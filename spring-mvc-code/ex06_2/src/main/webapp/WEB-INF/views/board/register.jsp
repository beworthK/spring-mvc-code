<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ include file="../includes/header.jsp" %>

<!-- 
	[등록] 페이지
	sec : 스프링 시큐리티 태그 라이브러리
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
		<h1 class="page-header">게시글 등록</h1>
	</div>
</div>

<!-- div#게시글 등록 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">등록하기</div>
			
			<div class="panel-body">
				<form role="form" action="/board/register" method="post">
					<!-- 스프링 시큐리티를 사용할 때 POST 방식 전송은 반드시 CSRF 토큰의 값을 추가한다 -->
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				
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
						<!-- 시큐리티 태그 라이브러리로 로그인한 사용자의 아이디 출력 (스프링 시큐리티에서는 username 이 사용자 아이디) -->
						<input class="form-control" name="writer"
							value='<sec:authentication property="principal.username"/>' readonly>
					</div>
					<button type="submit" class="btn btn-default">등록</button>
					<button type="reset" class="btn btn-default">새로고침</button>
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
			<div class="panel-heading">첨부파일</div>
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

<script>
$(document).ready(function(e){
	
	var formObj = $("form[role='form']");
	
	$("button[type='submit']").on("click", function(e){
		
		e.preventDefault(); // 첨부파일 관련 처리를 위해 기본동작 막기
		
		var str = "";

		$(".uploadResult ul li").each(function(i, obj){
			var obj = $(obj);
			
			console.dir(obj);
		    console.log("-------------------------");
		    console.log(obj.data("filename"));
			
			// input hidden 의 name 값 = attachList[인덱스번호] => bordVO에서 attachList 이름의 변수로 첨브파일 정보를 수집하므로
			str += "<input type='hidden' name='attachList["+i+"].fileName' value='" + obj.data("filename") + "'>";
			str += "<input type='hidden' name='attachList["+i+"].uuid' value='" + obj.data("uuid") + "'>";
			str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='" + obj.data("path") + "'>";
			str += "<input type='hidden' name='attachList["+i+"].fileType' value='" + obj.data("type") + "'>";
		});

		formObj.append(str).submit();
	});
	
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
	
	//--------------------------------------------------
	// 스프링 시큐리티가 적용되면, POST, PUT, PATCH, DELETE 와 같은 방식으로 데이터를 전송하는 경우, 
	// 반드시 추가적으로 'X-CSRF-TOKEN' 과 같은 헤더 정보를 추가해서 CSRF 토근 값을 전달하도록 수정해야 한다.
	// Ajax 는 JavaScript를 이용하기 떄문에, 브라우저에서 CSRF 토큰과 관련된 값을 변수(csrfHeaderName, csrfTokenValue) 로 선언하고
	// Ajax 전송 시 beforeSend 를 이용해서 추가적인 헤더를 지정해서 전송한다.
	//--------------------------------------------------
	
	var csrfHeaderName ="${_csrf.headerName}";
	var csrfTokenValue="${_csrf.token}";
	
	
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
			//--------------------------------------------------
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			//--------------------------------------------------
			data: formData,
			type: 'POST',
			dataType:'json',
			success: function(result){
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
	
	
	//--------------------------------------------------
	// 첨부파일을 삭제하는 경우에도 POST 방식으로 동작하므로,
	// CSRF 토큰의 처리가 필요하다
	//--------------------------------------------------
	// 첨부파일 삭제
	$(".uploadResult").on("click", "button", function(e){
		
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		
		var targetLi = $(this).closest("li");
		
		$.ajax({
			url: '/deleteFile',
			data: {fileName: targetFile, type:type},
			//--------------------------------------------------
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			//--------------------------------------------------
			dataType:'text',
			type: 'POST',
			success: function(result){
				alert(result);
				targetLi.remove();
			}
		}); //$.ajax
		
	});
	
});	
</script>

<%@ include file="../includes/footer.jsp" %>