# spring-mvc-code
스프링으로 웹 프로젝트에서 사용되는 게시물 관리 솔루션 개발<br>

서적명 : 코드로 배우는 스프링 웹 프로젝트 참조<br>
네이버 카페 : 구멍가게 코딩단 (<https://cafe.naver.com/gugucoding>)
<br/><br/>

## 목차
* Part 1. 스프링 개발 환경 구축 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex00) <br>
  > 프로젝트에서 사용되는 IDE, 데이터베이스, WAS(tomcat) 등 설정 진행
  *  스프링의 개발 환경(STS, Lombok 등) 설정
  *  MariaDB 설치 및 계정 설정
  *  스프링과 MariaDB 연동 설정
  *  스프링 MVC 의 구성 설정 및 테스트

* Part 2. 스프링 MVC 설정 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex01)<br>
  >스프링 프레임워크가 가장 많이 활용되는 Web 관련 개발 환경과 라이브러리 설정 진행<br>
  >웹 관련 스프링 라이브러리인 스프링 MVC 구조 학습
  *  스프링 MVC 프로젝트의 생성과 동작 과정 학습
  *  스프링 MVC 구조의 이해와 예제 작성
  *  스프링 MVC 를 이용하는 파일업로드 처리
  *  스프링 MVC 예외처리

* Part 3. 기본적인 웹 게시물 관리 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex02)<br>
  > MyBatis 를 이용해서 기본적인 CRUD 구현<br>
  > 게시글 목록 페이징 처리, 게시글 검색 기능의 게시물 관리 기능 작성<br>
  *  스프링 MVC 를 이용하는 웹 프로젝트 전체 구조에 대한 이해 학습
  *  개발의 각 단계에 필요한 설정 및 테스트 환경 구축
  *  기본적인 CRUD(등록, 수정, 삭제, 조회) 와 리스트(목록) 구현
  *  리스트(목록) 화면의 페이징 처리
  *  검색 처리와 페이지 이동 구현

* Part 4. REST 방식과 Ajax를 이용하는 댓글 처리 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex03)<br>
  > REST 방식 데이터 처리를 위한 스프링의 어노테이션과 기능 학습 <br>
  > REST 방식에서 가장 많이 사용하는 형태인 브라우저에서 Ajax를 이용해 호출하는 방식 구현<br>
  *  REST 방식 학습
  *  Ajax 를 활용한 댓글 기능 추가

* Part 5. AOP와 트랜잭션 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex04)<br>
  > AOP(관점 지향 프로그래밍)을 적용하여 기존의 코드에 첨삭없이, 메서드의 호출 이전 혹은 이후에 필요한 로직을 수행하는 방법 학습<br>
  > 스프링에서 XML 이나 어노테이션을 통해 트랜잭션이 처리된 결과 구현<br>
  *  AOP 설정 및 테스트 진행
  *  트랜잭션 설정 및 테스트 진행
  *  댓글과 댓글 수 의 변동에 관한 작업 트랜잭션 처리 적용
 
* Part 6. 파일 업로드 처리 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex05)<br>
  > 첨부파일을 별도로 업로드해서 사용자가 최종적으로 게시물을 등록하기 전에, 자신의 업로드 파일을 확인할 수 있는 방식 적용<br>
  *  파일 업로드 기능 구현 - Ajax를 이용하여 첨부파일을 별도로 처리하는 방식으로 구현
  *  파일 업로드 상세 처리 - 이미지 파일은 썸네일 이미지를 생성하도록 구현
  *  잘못 업로드된 파일 정리 - Quartz 라이브러리를 활용한 실제 파일과 DB 일관성 유지

* Part 7-1. Spring Web Security 를 이용한 로그인 처리 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex06)<br>
  > 스프링 웹 시큐리티를 이용하여, 사용자 권한이나 등급에 기반을 두는 로그인 체크 이용<br>
  *  로그인 처리와 CSRF 토큰 처리 적용
  *  암호화 처리 
  *  자동 로그인 구현

* Part 7-2. 기존 프로젝트(Part 6)에 Spring Web Security 접목하기 - [바로가기](https://github.com/beworthK/spring-mvc-code/tree/main/spring-mvc-code/ex06_2)<br>
  *  로그인과 회원 가입 페이지 작성
  *  기존 화면과 컨트롤러에 시큐리티 관련 내용 추가
  *  Ajax 부분 변경

## 기술스택
* 프로그래밍 언어 - JAVA 1.8  
* 프레임워크 - SPRING   
* 빌드 - MAVEN   
* DB - MARIA DB   
* 서버 - TOMCAT 9.0  
* 서버언어 - JSP  
* 라이브러리 - JSTL  
