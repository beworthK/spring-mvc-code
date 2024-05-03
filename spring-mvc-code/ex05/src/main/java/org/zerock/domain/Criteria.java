package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data 
public class Criteria {

	private int pageNum; // 페이지 번호
	private int amount;  // 한 페이지당 조회 수 		
	
	private String type; // 검색조건
	private String keyword; // 검색 키워드
	
	public Criteria() {
		this(1, 10); // 기본값 pageNum = 1, amount 10
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() { // 검색 조건이 T,W,C 로 구성되어있으므로 배열처리
		return type == null ? new String[] {} : type.split("");
	}
	
	/*
	 * UriComponentsBuilder 를 이용하는 링크 생성
	 * 매번 파라미터를 유지하는 일이 번거롭다면, 
	 * UriComponentsBuilder 를 사용 여러개의 파라미터를 연결해서 URL 형태로 만들 수 있다
	 * - UriComponentsBuilder 는 GET 방식 등의 파라미터 전송에 사용되는 문자열(쿼리스트링)을 손쉽게 처리해주는 클래스
	 */
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
			.queryParam("pageNum", this.pageNum)
			.queryParam("amount", this.amount)
			.queryParam("type", this.type)
			.queryParam("keyword", this.keyword);
			
		return builder.toUriString();
	}
}
