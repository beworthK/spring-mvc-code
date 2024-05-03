<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Upload with Ajax</h1>

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
	
	<div class='bigPictureWrapper'>
	  <div class='bigPicture'>
	  </div>
	</div>
	
	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>
	
	<div class='uploadResult'> <!-- 업로드된 파일 목록 -->
		<ul>
		</ul>
	</div>
	
	<button id='uploadBtn'>파일업로드</button>
	
	<!-- jQuery CDN -->
	<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
	<script>
		// jQuery의 $(document).ready() 바깥쪽에 함수 작성 -> <a> 태그에서 직접 함수를 호출할 수 있는 방식으로 작성하기 위해	
	
		// 이미지 보여주는 <div> 처리
		function showImage(fileCallPath){
		  
		  //alert(fileCallPath);
			
		  // ligth box 효과 적용
		  // - 첨부파일이 이미지인 경우, 썸네일을 클릭했을 때 화면에 크게 원본 파일을 보여주는 형태로 처리한다
		  $(".bigPictureWrapper").css("display","flex").show(); // 화면 정중앙 배치
		  
		  $(".bigPicture")
		  .html("<img src='/display?fileName="+ encodeURI(fileCallPath)+"'>")
		  .animate({width:'100%', height: '100%'}, 1000);
	
		} // fnc_showImage
		
		// 다시 클릭하면 원본 이미지 사라짐		
		$(".bigPictureWrapper").on("click", function(e){
			
			$(".bigPicture").animate({width:'0%', height: '0%'}, 1000);
			
			setTimeout(function() {
				$(".bigPictureWrapper").hide();
			}, 1000);
			
		});
		
		// 파일의 확장자나 크기 체크
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$"); //첨부파일 이용한 웹 공격 방지
		var maxSize = 5242880; //5MB
		
		function checkExtension(fileName, fileSize) {

			if (fileSize >= maxSize) {
				alert("파일 사이즈 초과");
				return false;
			}
			
			if (regex.test(fileName)) {
				alert("해당 종류의 파일은 업로드할 수 없습니다."); 
				return false;
			}
			
			return true;
		}

		var cloneObj = $(".uploadDiv").clone(); // <input type='file'> 객체가 포함된 div 복사
		
		$("#uploadBtn").on("click", function(e) { // 파일업로드 버튼 클릭시,

			var formData = new FormData();

			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files; //
			
			// checkExtension
			for (var i = 0; i < files.length; i++) {
				if (!checkExtension(files[i].name, files[i].size)) {
					return false; // 조건에 걸리면 formData에 담지 않는다 
				}
				
				// formData 에 전송할 파라미터 담기
				formData.append("uploadFile", files[i]);
			}
			
			// ajax 통신
			$.ajax({
				url : '/uploadAjaxAction',
				processData : false,
				contentType : false,
				data : formData,
				type : 'POST',
				dataType : 'json', //json 타입으로 결과 받음
				success : function(result) {
					//console.log(result);
					
					// 업로드된 결과 처리
					showUploadedFile(result); 
					
					// 업로드 부분 초기화
					$(".uploadDiv").html(cloneObj.html()); // 복사한 객체를 div 내에 다시 추가
				}
			});

		});

		// 업로드된 파일 처리 - 썸네일이나 파일 아이콘 등을 적합하게 보여준다
		var uploadResult = $(".uploadResult ul");
 
		function showUploadedFile(uploadResultArr){
		
			var str = "";
		  
			$(uploadResultArr).each(function(i, obj){
				
				// encodeURIComponent - 파일이름에 한글이나 공백 등 문자가 들어가면 문제가 발생할 수 있으므로 문자열 처리
				if(!obj.image){
				  
					// 일반파일
					var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName );
					var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
					
					str += "<li><div>"
						+ "<a href='/download?fileName="+fileCallPath+"'>" // 다운로드 경로
						+ "<img src='/resources/img/attach.png'>"+ obj.fileName + "</a>"
						+ "<span data-file=\'" + fileCallPath + "\' data-type='file'> x </span>"
						+ "</div></li>";
						
				}else{
					// 이미지파일	
					
					var fileCallPath = encodeURIComponent( obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName ); //s_ 추가!
					
					// 이미지 첨부파일의 경우, 업로드된 경로와 uuid 가 붙은 파일의 이름이 필요하기 때문에 하나의 문자열로 생성
					var originPath = obj.uploadPath+ "\\" + obj.uuid + "_" + obj.fileName;
					originPath = originPath.replace(new RegExp(/\\/g),"/"); // '\' 를 '/' 로 변경 
					
					str += "<li>"
						+   "<a href=\"javascript:showImage(\'"+originPath+"\')\">" // showImage() 에 originPath 변수 전달
						+    "<img src='display?fileName="+fileCallPath+"'></a>" 	// uploadController 의 display
					    +    "<span data-file=\'"+fileCallPath+"\' data-type='image'> x </span>" //파일삭제'x' 버튼표시
					    +  "</li>";
				}
		  	});
		  
		  	uploadResult.append(str);
		  	
		} // fnc_showUploadedFile

		
		// 파일 삭제
		$(".uploadResult").on("click", "span", function(e){
			
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			
			$.ajax({
				url: '/deleteFile',
				data: {fileName: targetFile, type:type},
				dataType:'text',
				type: 'POST',
				success: function(result){
					alert(result);
				}
			}); //$.ajax
		});
		
	</script>

</body>
</html>
