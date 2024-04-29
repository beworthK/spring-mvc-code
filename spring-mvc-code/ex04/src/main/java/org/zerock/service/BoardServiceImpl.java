package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

/**
 * 9.1 비즈니스 계층의 설정
 * - 설계를 할 때 각 계층 간의 연결은 인터페이스를 이용해서 느슨한 (loose) 연결(결합)을 한다.
 * - BoardService 인터페이스를 '구현한' 클래스
 * - @Service 주로 비즈니스 영역을 담당하는 객체임을 표시
 * - @AllArgsConstructor 모든 파라미터를 이용하는 생성자를 만들어줌 -boardMapper 주입받는 생성자 만들어짐
 */
@Log
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	// Spring 4.3 이상에서 자동 처리 
	private BoardMapper mapper; // BoardServiceImpl 이 정상적으로 동작하기 위해 필요

//	setter를 이용한 처리
//	@Setter(onMethod_ = @Autowired)
//	private BoardMapper mapper;
	
	
	@Override
	public void register(BoardVO board) { 
		// 파라미터로 전달되는 BoardVO 타입의 객체를 BoardMapper 를 통해서 처리한다
		log.info("register..............." + board);
		
		mapper.insertSelectKey(board);
	}

	@Override
	public BoardVO get(Long bno) {
		
		log.info("get................." + bno);
		
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		
		log.info("modify.........." + board);
		
		return mapper.update(board) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		
		log.info("remove...." + bno);
		
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getAllList() {
		log.info("getList............................");
		
		return mapper.getList();
	}
	
	@Override
	public List<BoardVO> getList(Criteria cri){
		log.info("get List with Creteria : " + cri);
		
		return mapper.getListWithPaging(cri);
	}
	
	// 전체 데이터 개수
	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		
		return mapper.getTotalCount(cri);
	}
	
	

}
