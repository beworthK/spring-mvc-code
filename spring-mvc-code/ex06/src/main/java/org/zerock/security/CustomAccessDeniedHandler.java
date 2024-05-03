package org.zerock.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

/**
 * 접근 제한이 된 경우에 다양한 처리를 하기위해 
 * AccessDeniedHandler 인터페이스 구현
 * 
 */
@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

/*
 * HttpServletRequest, HttpServletResponse 를 파라미터로 사용하기 때문에 
 * 직접적으로 서블릿 API를 이용하는 처리가 가능하다
 */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessException)
      throws IOException, ServletException {

    log.error("Access Denied Handler");

    log.error("Redirect....");

    response.sendRedirect("/accessError"); // 접근제한이 걸리면 리다이렉트
  }

}
