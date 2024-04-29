package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

/** 영속 계층의 작업 
 * 1. 테이블의 컬럼 구조를 반영하ㅡ VO(Value Obejct) 클래스의 생성
 * [2. MyBatis 의 Mapper 인터페이스의 작성/XML 처리]
 * 3. 작성한 Mapper 인터페이스의 테스트
 */
public interface BoardMapper {
	
	//@Select("select * from BOOK.tbl_board where bno > 0") 
	// 간단한 SQL 이라면, 어노테이션을 이용해서 처리하는 것이 무난하지만, 
	// SQL이 점점 복잡해지고 검색과 같이 상황에 따라 다른 sql 문이 처리되는 경우에는 어노테이션은 그다지 유용하지 못하다는 단점이 있음.
	public List<BoardVO> getList();
	
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	// 8.2 영속 영역의 CURD 구현
	
	// 8.2.1 CREATE(INSERT) 처리
	// 아래 두 방법은 병행 불가
	public void insert(BoardVO board); // PK 값 알 필요 없는 경우 - PK 컬럼이 auto_increment 속성을 가지는 경우 자동으로 증가 --> 
	
	public void insertSelectKey(BoardVO board); // inert 문이 실행되고 생성된 PK 값 알아야 하는 경우 - selectKey 사용
	
	// 8.2.2 READ(SELECT) 처리 : PK 값을 이용해서 처리
	public BoardVO read(Long bno);
	
	// 8.2.3 DELETE 처리 : PK 값을 이용해서 처리 -- 삭제 처리된 데이터 수(=int) 반환됨
	public int delete(Long bno);
	
	// 8.2.4 UPDATE 처리 : PK 값을 이용해서 처리 -- 수정 처리된 데이터 수(=int) 반환됨
	public int update(BoardVO board);
	
	
	public int getTotalCount(Criteria cri);
	
	// Part 5. 트랜잭션 - 댓글 수 업데이트
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	
}
