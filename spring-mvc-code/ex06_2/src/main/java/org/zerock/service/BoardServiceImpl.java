package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * @Service 주로 비즈니스 영역을 담당하는 객체임을 표시
 * @AllArgsConstructor 모든 파라미터를 이용하는 생성자를 만들어줌 -boardMapper 주입받는 생성자 만들어짐
 */
@Log
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private BoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;

	/* 게시글 등록
	 * 
	 * 첨부파일 등록 - @Transactional 
	 *  - 게시물 등록 작업은 tbl_board 테이블과 tbl_attach 테이블 둘다 
	 *    insert가 진행되어야 하므로 트랜잭션 처리
	 */
	@Transactional
	@Override
	public void register(BoardVO board) { 
		log.info("register..............." + board);
		
		mapper.insertSelectKey(board);
		
		// 첨부파일 등록
		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	/*	게시글 가져오기
	 * 
	 */
	@Override
	public BoardVO get(Long bno) {
		log.info("get................." + bno);
		
		return mapper.read(bno);
	}

	/* 게시물 수정
	 * 
	 * 첨부파일 수정 - @Transactional
	 * - 기존의 첨부파일 관련 데이터를 전부 삭제한 후에,
	 * 	 다시 첨부파일 데이터를 추가하는 방식으로 동작
	 */  
	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		log.info("modify.........." + board);
		
		attachMapper.deleteAll(board.getBno()); // 첨부파일 전부 삭제
		
		boolean modifyResult = mapper.update(board) == 1; // 게시글 수정
		
		if (modifyResult && board.getAttachList() != null) { // 첨부파일 다시 추가
			
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return modifyResult;
	}

	/* 게시글 삭제
	 * 
	 * 게시글 관련 첨부파일 함께 삭제 - @Transactional
	 */
	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("remove...." + bno);
		
		attachMapper.deleteAll(bno); // 첨부파일 삭제
		
		return mapper.delete(bno) == 1;
	}

	// 게시글 목록 가져오기
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
	
	// 게시물의 첨부파일 목록 가져오기
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
	
}
