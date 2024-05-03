package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * @RequestMapping - URL을 분기
 * @AllArgsConstructor - 생성자를 만들고 자동으로 주입
 */
@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	
	private final String UPLOAD_FOLDER = "D:\\Projects\\reese\\spring-mvc-code\\upload";
	private BoardService service;
	
	/**
	 * [목록] 페이지 이동
	 * @param cri
	 * @param model
	 */
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		
		log.info("list: " + cri);
		model.addAttribute("list", service.getList(cri));
		
		// 전체 페이지 수 가져오기
		int total = service.getTotal(cri);
		log.info("total : " + total);
		
		// 페이징 처리
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	/**
	 * [등록] 페이지 이동
	 * @PreAuthorize("isAuthenticated()") - 로그인이 성공한 사용자만 해당 기능을 사용할 수 있다
	 */
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {
		
	}
	
	/**
	 * [등록] 처리
	 * @PreAuthorize("isAuthenticated()") - 로그인이 성공한 사용자만 해당 기능을 사용할 수 있다
	 */
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		// 'redirect:' 스프링mvc 가 내부적으로 response.sendRedirect() 처리를 해줌
		
		log.info("==========================");
		log.info("register : " + board);
		if (board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		log.info("==========================");
		
		service.register(board);
		
		// addFlashAttribute - 보관된 데이터를 단 한번만 사용할 수 있게 보관된다(내부적으로는 HttpSesion을 이용해서 처리)
		rttr.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list"; //등록작업이 끝난 후 목록으로 보낸다 - 새로고침을 통해 반복적으로 등록하는 행위 방지
	}
	
	/**
	 * [상세보기] & [수정/삭제] 페이지 이동
	 * @param bno - @RequestParam 을 통해 명시적으로 처리
	 * @param cri - @ModelAttribute : 자동으로 Model에 데이터를 지정한 이름으로 담는걸 좀더 '명시적'으로 처리
	 * @param model
	 */
	@GetMapping({"/get", "/modify"}) //url 배열처리
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
		
		log.info("/get or modify");
		
		model.addAttribute("board", service.get(bno));
	}
	
	/**
	 * [수정] 처리
	 * @PreAuthorize("principal.username == #board.writer")
	 *  - 로그인한 사용자와 현재 파라미터로 전달되는 작성자가 일치하는지 체크
	 *  - @PreAuthorize는 문자열로 표현식을 지정할 수 있다.
	 */
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify : " + board);
		
		if(service.modify(board)) { // service.modify 는 수정 여부를 boolean 처리
			rttr.addFlashAttribute("result", "success");
		}
		
		//사용안함_추후테스트
		//rttr.addAttribute("pageNum", cri.getPageNum());
		//rttr.addAttribute("amount", cri.getAmount());
		//rttr.addAttribute("type", cri.getType());
		//rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/board/list" + cri.getListLink();	
	}
	
	/**
	 * [삭제] 처리
	 * @PreAuthorize("principal.username == #writer")
	 * 	- 로그인한 사용자와 현재 파라미터로 전달되는 작성자(writer 파라미터 추가)가 일치하는지 체크
	 */
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove") //반드시 post 처리
	public String remove(@RequestParam("bno") Long bno, String writer, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		
		log.info("remove......." + bno);
		
		// 1) 해당 게시물의 첨부파일 목록 가져오기
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if(service.remove(bno)) { // 2) DB 상에서 해당 게시물과 첨부파일 데이터 삭제
			//3) 실제 파일 삭제
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result", "success");
		}
		
		return "redirect:/board/list" + cri.getListLink();	
	}
	
	
	/*
	 * [파일] 게시물의 첨부파일 목록 가져오기
	 * @ResponseBody 를 적용해서 json 데이터 반환
	 */
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {

		log.info("getAttachList " + bno);

		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}
	
	/*
	 * [파일] 첨부파일 삭제
	 *  - 게시물 삭제 시, 게시물에 포함된 첨부파일을 함께 삭제한다.
	 *  - 첨부파일 목록을 이용해서 해당 폴더의 썸네일 파일(이미지파일인 경우)과 일반 파일 삭제
	 */
	private void deleteFiles(List<BoardAttachVO> attachList) {
		
		if (attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files...................");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			
			try {
				Path file = Paths.get( UPLOAD_FOLDER + "\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName() ); 
				
				Files.deleteIfExists(file); // 실제 파일 삭제
				
				// 이미지파일인 경우 썸네일 파일 삭제
				if (Files.probeContentType(file).startsWith("image")) { 
					Path thumbNail = Paths.get( UPLOAD_FOLDER + "\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName() );
					
					Files.delete(thumbNail);
				}
				
			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			}
			
		});
		
	}
	
}
