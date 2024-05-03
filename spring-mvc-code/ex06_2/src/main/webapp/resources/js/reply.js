/*
모듈 패턴은 Java의 클래스처럼 JavaScript를 이용해서 메서드를 가지는 객체를 구성한다.
JavaScript 의 즉시 실행함수와 '{}'를 이용해서 객체를 구성한다.
*/

var replyService = (function(){
	
	// 댓글 등록 ajax
	function add(reply, callback, error){
		console.log("add reply................");
		
		$.ajax({
			type: 'post',
			url: '/replies/new',
			data: JSON.stringify(reply),
			contentType: "application/json; charset=utf-8",
			success: function(result, status, xhr){
				if(callback){
					callback(result);
				}
			},
			error: function(xhr, status, er) {
				if(error){
					error(er);
				}
			}
		})
	};
	
	// 댓글 목록 가져오기(댓글 등록 후)
	function getList(param, callback, error){
		var bno = param.bno;
		var page = param.page || 1;
		
		// JSON 형태가 필요하므로 url 호출 시 확장자를 ".json" 으로 요구
		$.getJSON("/replies/pages/" + bno + "/" + page + ".json", 
			function(data){
				if (callback) {
	            	callback(data.replyCnt, data.list); //댓글 숫자와 목록 가져오기
				}
			}).fail(function(xhr, status, err){
			if (error) {
				error();
			}		
		});
	};
	
	// 댓글 삭제
	function remove(rno, replyer, callback, error) {
		
		console.log( JSON.stringify({rno:rno, replyer:replyer}) );
		
		$.ajax({
			type: 'delete', //delete 방식으로 데이터를 전달
			url: '/replies/' + rno,
			//--------------------------------------------------
			data: JSON.stringify({rno:rno, replyer:replyer}),
			contentType: "application/json; charset=utf-8",
			//--------------------------------------------------
			success: function(deleteResult, status, xhr){
				if(callback){
					callback(deleteResult);
				}
			},
			error: function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		})
	};
	
	// 댓글 수정
	function update(reply, callback, error){
		
		$.ajax({
			type: 'put',
			url: '/replies/' + reply.rno, //댓글번호 함께 전송
			data: JSON.stringify(reply),
			contentType: "application/json; charset=utf-8",
			success: function(result, status, xhr){
				if(callback){
					callback(result);
				}
			},
			error: function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		});
	};
	
	// 댓글 조회
	function get(rno, callback, error){
		$.get("/replies/" + rno + ".json", function(result){
			if(callback){
				callback(result);
			}
		}).fail(function(xhr, status, err){
			if(error){
				error();
			}
		})
	};
	
	// 시간에 대한 처리
	// XML 이나 JSON 형태로 데이털르 받을 때는 순수하게 숫자로 표현되는 시간 앖이 나오므로 포맷처리
	function displayTime(timeValue){
		
		var today = new Date();
		
		var gap = today.getTime() - timeValue;
		
		var dateObj = new Date(timeValue);
		var str = "";
		
		if( gap < (1000 * 60 * 60 *24 )) {
			// 24시간 이내 댓글은 시/분/초 표시
			var hh = dateObj.getHours();
			var mi = dateObj.getMinutes();
			var ss = dateObj.getSeconds();
			
			return [ (hh > 9 ? '' : '0') 
					+ hh, ':', (mi > 9 ? '' : '0' ) 
					+ mi, ':', (ss>9 ? '': '0' ) + ss].join(''); 
		} else {
			// 24시간 지난 댓글은 날짜만 표시(연/월/일)
			var yy = dateObj.getFullYear();
			var mm = dateObj.getMonth() + 1;	// getMonth() is zero-based
			var dd = dateObj.getDate();
			
			return [ yy, '/', 
					(mm > 9 ? '' : '0') + mm, '/', 
					(dd > 9 ? '' : '0') + dd ].join('');
		}
	};
	
	// replyService 변수에 담김
	return {
		add: add,
		getList: getList,
		remove: remove,
		update: update,
		get: get,
		displayTime: displayTime
	};
	
})();