package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

/**
 * @RestController - 순수한 데이터를 반환하는 형태이므로 다양한 포맷의 데이터 전송 가능
 * 
 */

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {
	
	/* 16.2.1 단순 문자열 반환
	 * produces 속성 - 해당 메서드가 생산하는 MIME 타입 
	 */
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		
		// MediaType.TEXT_PLAIN_VALUE = "text/plain;"
		log.info("MIME TYPE : " + MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}
	
	/* 16.2.2 객체 반환
	 * SampleVO 객체 반환
	 * @GetMapping, @RequestMapping 의 produces 는 생략 가능
	 */
	@GetMapping(value = "/getSample", 
			produces = {
					MediaType.APPLICATION_JSON_VALUE,  //MediaType.APPLICATION_JSON_UTF8_VALUE 스프링 5.2 부터 없어짐
					MediaType.APPLICATION_XML_VALUE
			})
	public SampleVO getSample() {
		return new SampleVO(112, "스타", "로드");
	}
	
	/* 16.2.3 컬렉션 타입의 객체 반환
	 * 
	 */
	@GetMapping(value = "/getList")
	public List<SampleVO> getList(){
		// 1~ 10미만 까지 루프를 돌면서 sampleVO 객체를 만들어서 List<SampleVO> 에 담아서 반환  
		return IntStream.range(1, 10).mapToObj( i -> new SampleVO(i, i+"First", i+"Last") ).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap(){
		Map<String, SampleVO> map = new HashMap<>();
		map.put("First", new SampleVO(111, "그루트", "주니어")); // map 은 키 와 값을 가지는 객체로 간주 된다
		
		return map;
	}
	
	/* 16.2.4 ResponseEntity 타입
	 * ResponseEntity 는 데이터와 함께 HTTP 헤더의 상태 메시지 등을 같이 전달하는 용도로 사용
	 *  
	 */
	@GetMapping(value = "/check", params= {"height", "weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight) {
		SampleVO vo = new SampleVO(0, ""+height, ""+weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if(height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo); // 상태코드 와 에러 메시지 등을 함께 전달할 수 있다
		}
		
		return result;
	}
	
	/* 16.3.1 @PathVariable
	 * REST 방식에서는 url 에 최댜한 많은 정보를 담으려고 노력한다. 
	 * @PathVariable 어노테이션을 이용해서 url 상에 경로의 일부를 파라미터로 사용할 수 있다 
	 * int, double 같은 기본 자료형은 사용할 수 없다
	 */
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid){
		return new String[] {"category:"+cat, "productid:" + pid};
	}
	
	/* 16.3.2 @RequestBody
	 * @RequestBody 는 전달된 요청(request) 의 내용(body)을 이용해서 해당 파라미터 타입으로 변환을 요구한다.
	 * 내부적으로 HttpMessageConverter 타입의 객체들을 이용해서 다양한 포맷의 입력 데이터를 변환할 수 있다.
	 * 원하는 포맷의 데이터를 보내고, 이를 해석해서 원하는 타입으로 사용하기도 한다
	 * 
	 * @RequestBody 가 요청한 내용을 처리하기 때문에 일반적인 파라미터 전달방식을 사용할 수 없으므로 @PostMapping
	 */
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("convert............ticket" + ticket);
		
		return ticket;
	}
}
