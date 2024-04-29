package org.zerock.domain;

import java.util.Date;

import lombok.Data;

/** 영속 계층의 작업 
 * [1. 테이블의 컬럼 구조를 반영하ㅡ VO(Value Obejct) 클래스의 생성]
 * 2. MyBatis 의 Mapper 인터페이스의 작성/XML 처리
 * 3. 작성한 Mapper 인터페이스의 테스트
 */
@Data
public class BoardVO {

	private Long bno;
	private String title;
	private String content;
	private String writer; 
	private Date regdate;
	private Date updateDate;

}
