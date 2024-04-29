package org.zerock.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.ibatis.javassist.tools.reflect.Sample;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.SampleDTO;
import org.zerock.domain.SampleDTOList;
import org.zerock.domain.TodoDTO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/sample/*")
@Log4j
public class SampleController {

	/**
	 * 6.3.4 @InitBinder
	 * - 파라미터 수집을 binding(바인딩) 이라고도 한다.
	 * - 파라미터를 변환해서 처리해야 하는 경우, 파라미터를 바인딩 할 때 자동으로 호출되는 @InitBinder 를 이용해서 변환할 수 있다
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		
		// 파라미터 수집하여 Date 형태로 변환처리 -- ex03 메서드 호출시 해당 로직을 거쳐 변환되어서 에러가 발생하지 않는다. 
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat,  false));
	}
	
	
	
	// GET , POST 방식모두 지원해야하는 경우 배열로 처리
	@RequestMapping(value="/basic", method= {RequestMethod.GET, RequestMethod.POST})
	public void basicGet() {
		log.info("basic get...........................");
	}
	

	@GetMapping
	public void basicGet2() {
		log.info("basic get only get...........................");
	}
	

	/**
	 * 6.3 Controller 의 파라미터 수집
	 * - 파라미터가 자동으로 수집되어 request.getParameter() 를 이용하는 불편을 해소
	 * @param dto
	 * @return
	 */
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) {
		log.info("" + dto);
		
		return "ex01";
	}
	
	/**
	 * 6.3.1 파라미터의 수집과 변환
	 * - @RequestParam : 파라미터로 사용된 변수의 이름과 전달되는 파라미터 이름이 다른경우 유용
	 */
	@GetMapping("/ex02")
	public String ex02(@RequestParam("firstName") String name, @RequestParam("age") int age) {
		log.info("name : " + name);
		log.info("age : " + age);
		
		return "ex02";	
	}

	
	/**
	 * 6.3.2 리스트, 배열 처리
	 * 동일한 이름의 파라미터가 여러개 전달되는 경우 ArrayList<> 등을 이용해 처리
	 * 스프링은 파라미터의 '타입'을 보고 객체를 생성하므로 List<> 같은 인터페이스 타입이 아닌,
	 * 실제적인 클래스 타입으로 지정한다.
	 */
	@GetMapping("/ex02List")
	public String ex02List(@RequestParam("ids") ArrayList<String> ids) {
		log.info("ids : " + ids.get(0)); // ArrayList<String> 형태로 자동으로 수집된다
		
		return "ex02List";
	}
	
	@GetMapping("/ex02Array")
	public String ex02Array(@RequestParam("ids") String[] ids) {
		log.info("array ids : " + Arrays.toString(ids)); // 배열 형태로 받는 경우
		
		return "ex02Array";
	}
	
	
	/**
	 * 6.3.3 객체 리스트
	 * - 전달하는 데이터가 SampleDTO와 같은 객체 타입이고, 여러개를 처리해야할 때
	 * SampleDTO 의 리스트를 포함하는 SampleDTOList 클래스를 설계 후, 
	 * controller 에서 SampleDTOList 타입을 파라미터로 사용하는 매서드를 작성
	 * 
	 * - 파라미터는 [인덱스] 와 같은 형식으로 전달해서 처리할 수 있다.
	 * ex. /ex02Bean?list[0].name=aaa&list[2].name=bbb
	 * 	   /ex02Bean?list%5B0%5D.name=aaa&list%5B2%5D.name=bbb
	 */
	@GetMapping("/ex02Bean")
	public String ex02Bean(SampleDTOList list) {
		log.info("list dtos : " + list);
		
		return "ex02Bean";
	}
	
	
	/**
	 * 6.3.4 @InitBinder
	 */
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		log.info("todo : " + todo);
		
		return "ex03";
	}
	
	
	/**
	 * 6.4 Model 이라는 데이터 전달자 
	 * - Model 객체는 JSP에 <- 컨트롤러에서 생성된 데이터를 담아서 전달하는 역할
	 * - JSP 와 같은 뷰(View)로 전달해야 하는 데이터를 담아서 보낼 수 있다.
	 */
	
	/**
	 * 6.4.1 @ModelAttribute 어노테이션
	 * 스프링 MVC Controller 는 Java Beans 규칙에 맞는 객체는 다시 화면으로 자동전달해준다.
	 * Java Beans 규칙 - 좁은 의미에서는 생성자가 없거나 빈생성자를 가져야하며 getter/setter를 가지 클래스의 객체들
	 * SampleDTO 은 Java Bean 규칙에 맞기 때문에 자동으로 화면(뷰단) 까지 전달 됨 <- 단 클래스명의 앞글자는 소문자로 처리된다
	 * 기본 자료형은 파라미터로 선언해도 화면까지 전달되지 않으므로 @ModelAttribute 를 통해 담아서 전달한다.
	 */
	@GetMapping("/ex04")
	public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {
		log.info("dto : " + dto);
		log.info("page : " + page);
		
		return "/sample/ex04";
	}
	
	/**
	 * 6.5.4 ResponseEntity 타입 
	 * ResponseEntity 를 통해서 워하는 헤더 정보나 데이터를 전달할 수 있다.
	 */
	@GetMapping("/ex07")
	public ResponseEntity<String> ex07(){
		log.info("/ex07...........................");
		
		String msg = "{\"name\" : \"홍똥\"}";
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "application/json;charset=UTF-8");
		
		return new ResponseEntity<>(msg, header, HttpStatus.OK); // 브라우저에는 JSON 타입이라는 헤더 메세지와 200 ok 라는 상태코드를 전송한다
	}
	
	
	/**
	 * 6.5.5 파일 업로드 처리
	 */
	@GetMapping("/exUpload")
	public void exUpload() {
		log.info("/exUpload................................");
	}
	
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		files.forEach(file -> {
			log.info("----------------------");
			log.info("name : " + file.getOriginalFilename());
			log.info("size : " + file.getSize());
		});
	}
	
	
	/**
	 * 6.6 Controller 의 Exception 처리
	 * 6.6.1 @ControllerAdvice
	 * - AOP 를 이용ㅇ하는 방식. 공통적인 예외사항에 대해서는 별도로 @ControllerAdvice 를 이용해서 분리하는 방식입니다
	 */
	
}
