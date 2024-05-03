package org.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

// DTO 인 이유!!
/**
 * 댓글 목록과 댓글 수 정보를 담는 클래스
 * @AllArgsConstructor - replyCnt 와 list 생성자를 파라미터로 처리 
 */
@Data
@AllArgsConstructor
@Getter
public class ReplyPageDTO {

	private int replyCnt;
	private List<ReplyVO> list;
		
}
