package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {

	// 특정 게시물 첨부파일 목록
	public List<BoardAttachVO> findByBno(Long bno);

	// 전날 등록된 첨부파일 목록을 가져오는 메서드
	public List<BoardAttachVO> getOldFiles();
	
	public void insert(BoardAttachVO vo);

	public void delete(String uuid);

	// 첨부파일 삭제 처리
	public void deleteAll(Long bno);

}