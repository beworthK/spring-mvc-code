package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

/**
 * 9.1 비즈니스 계층의 설정
 * - 설계를 할 때 각 계층 간의 연결은 인터페이스를 이용해서 느슨한 (loose) 연결(결합)을 한다.
 * - BoardService 인터페이스
 */
public interface BoardService {

	public void register(BoardVO board);
	
	public BoardVO get(Long bno);
	
	public boolean modify(BoardVO board);
	
	public boolean remove(Long bno);
	
	public List<BoardVO> getList(Criteria cri); 
	
	public int getTotal(Criteria cri); // 전체 데이터 개수
	
	// 첨부파일 관련
	public List<BoardAttachVO> getAttachList(Long bno);
	
}
