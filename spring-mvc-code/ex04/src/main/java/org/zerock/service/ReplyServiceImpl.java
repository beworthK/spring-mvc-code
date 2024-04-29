package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService{

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;
	
	/*
	 *  트랜잭션 처리 - 댓글 등록 & 게시글 댓글 수 +1
	 */
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		
		log.info("register........." + vo);
		
		// update ReplyCnt
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		
		return mapper.insert(vo);
	}
	
	@Override
	public ReplyVO get(Long rno) {
		
		log.info("get............" + rno);
		
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		
		log.info("modify............" + vo);
		
		return mapper.update(vo);
	}

	/*
	 *  트랜잭션 처리 - 댓글 삭제 & 게시글 댓글 수 -1
	 */
	@Transactional
	@Override
	public int remove(Long rno) {
		
		log.info("remove.............." + rno);
		
		ReplyVO vo = mapper.read(rno);
		
		// update ReplyCnt
		boardMapper.updateReplyCnt(vo.getBno(), -1); // 댓글수 제거
		
		return mapper.delete(rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		
		log.info("get Reply List of a Board " + bno);
		
		return mapper.getListWithPaging(cri, bno);
//		return List<ReplyVO>	
	}

	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		
		return new ReplyPageDTO(
						mapper.getCountByBno(bno)
						, mapper.getListWithPaging(cri, bno)
					);
	}
	
	

	
	
}
