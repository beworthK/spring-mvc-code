package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
@RestController
@Log4j
public class ReplyController {
	
	@Setter(onMethod_ = @Autowired)
	private ReplyService service;
	
	/* 
	 * 17.3.2 등록 작업과 테스트
	 * 브라우저에서는 json 타입으로 된 댓글 데이터를 전송하고
	 * 서버에서는 댓글의 처리 결과가 정상적으로 되었는지 문자열로 결과를 알려주도록 한다.
	 * 
	 * consumes - json 방식의 데이터만 처리하도록 하고,
	 * produces - 문자열로 반환하도록 설계
	 */
	@PostMapping(value = "/new", 
				 consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) {
		
		log.info("ReplyVO : " + vo);
		
		int insertCount = service.register(vo);
		
		log.info("Reply INSERT COUNT : " + insertCount);
		
		return 	insertCount == 1 
				? new ResponseEntity<>("success", HttpStatus.OK) 
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				// 삼항연산자
	}
	
	/* 
	 * 17.3.3 특정 게시물의 댓글 목록 확인
	 * @GetMapping
	 */
	@GetMapping(value = "/pages/{bno}/{page}", 
				produces = {MediaType.APPLICATION_XML_VALUE, 
							MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno) {
		
		log.info("getList.............");
		
		// Criteria 를 생성해서 직접 처리
		Criteria cri = new Criteria(page, 10); 
		
		log.info(cri);
		
		return new ResponseEntity<>( service.getListPage(cri, bno), HttpStatus.OK ); // 페이징처리
	}
	
	/* 
	 * 17.3.4 댓글 조회
	 * @GetMapping
	 */
	@GetMapping(value = "/{rno}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) {
		
		log.info("get : " + rno);
		
		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}
	
	/*
	 * 17.3.4 댓글 삭제
	 * @DeleteMapping
	 */
	@DeleteMapping(value = "/{rno}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
		
		log.info("remove: " + rno);
		
		return service.remove(rno) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK) 
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	/*
	 * 17.3.5 댓글 수정
	 * - 댓글 수정은 JSON 형태로 전달되는 데이터와 파라미터로 전달되는 댓글번호(rno)를 처리한다 
	 * - method : 댓글 수정은 PUT 이나 PATCH 방식을 이용
	 * - @RequestBody : 실제 수정되는 데이터는 JSON 포맷이므로 @RequestBody 이용해서 처리
	 * - @RequestBody로 처리되는 데이터는 일반 파라미터나 @PathVariable 파라미터를 처리할 수 없기 떄문에 직접 처리해 주는 부분을 주의
	 */
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH},
			value = "/{rno}",
			consumes = "application/json",
			produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
		vo.setRno(rno);
		
		log.info("rno: " + rno);
		
		log.info("modify: " + vo);
		
		return service.modify(vo) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
