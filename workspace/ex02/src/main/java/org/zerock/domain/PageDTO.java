package org.zerock.domain;

import lombok.Data;

/**
 * page - 현재 페이지 번호
 * prev/next - 이전/다음 이동 링크 표시 여부
 * startPage/endPage - 화면에서 보여지는 페이지의 시작/끝 번호
 */
@Data
public class PageDTO {

	private int startPage;
	private int endPage;
	private boolean prev, next;
	
	private int total;
	private Criteria cri; // amount 와 pageNum 값 보유
	
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		
		/*
		 *  현재 사용자가 5페이지를 본다면, 화면의 페이지 번호는 1부터 시작하지만,
		 *  19페이지를 본다면 11부터 시작판다(화면에 10개씩 페이지번호를 출력한다고 할 때,) 
		 */
		this.endPage = (int) (Math.ceil(cri.getPageNum()/10.0)) * 10; //페이지 번호는 10개씩 보여준다
		this.startPage = this.endPage - 9; // 10개씩 보여준다면, 시작번호 = 끝번호 - 9
		
		/*
		 * 전체 데이터 수가 80개라면, 10개씩 보여줄 때, 끝번호는 10이 아닌 '8' 이어야 한다 
		 * 그러므로, endPage*amout > total(전체 데이터 수) 라면, endPage 는 다시 계산되어야 한다.
		 */
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
		
		if (realEnd < this.endPage) {
			this.endPage = realEnd; //endPage 값 다시 지정
		}
		
		this.prev = this.startPage > 1; // 이전 = 시작 페이지 번호 가 1보다 크면 존재
		this.next = this.endPage < realEnd; // 다음 = 끝 페이지 번호 가 마지막 페이지 보다 작으면 존재
	}
	
}
