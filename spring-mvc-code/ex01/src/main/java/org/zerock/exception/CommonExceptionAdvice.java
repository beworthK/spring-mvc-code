package org.zerock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j;

/**
 * @ControllerAdvice 어노테이션을 적용하지만, 예외 처리를 목적으로 생성하는 클래스이므로 
 * 별도의 로직처리는 하지 않는다.
 * - @ControllerAdvice 해당 객체가 스트링의 컨트롤러에서 발생하는 예외를 처리하는 존재임을 명시
 * - @ExceptionHandler 속성으로 Exception 클래스 타입을 지정. 
 *   Exception.class를 지정하였으므로 모든 예외에 대한 처리를 except()만을 이용해서 처리할 수 있다
 */

@ControllerAdvice
@Log4j
public class CommonExceptionAdvice {

	@ExceptionHandler(Exception.class)
	public String except(Exception ex, Model model) {
		
		log.error("Exception ......" + ex.getMessage());
		model.addAttribute("exception", ex);
		log.error(model);
		
		return "error_page";
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handle404(NoHandlerFoundException ex) {
		
		return "custom404";
	}
	
}
