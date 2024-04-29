package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {
	
	public int insert(ReplyVO vo);
	
	public ReplyVO read(Long bno);
	
	public int delete(Long rno);
	
	public int update(ReplyVO reply);
	
	/* 댓글 목록과 페이징 처리
	 * MyBatis 에 두 개 이상의 파라미터를 전달하기 위해서는
	 * 1) 별도의 객체로 구성
	 * 2) Map 을 이용
	 * 3) @Param 을 이용해서 이름을 사용
	 */
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);
	
	public int getCountByBno(Long bno);
}
