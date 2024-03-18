package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

/** 10. 프레젠테이션(웹) 계층의 CRUD 구현
 * 컨트롤러는 하나의 클래스 내에서 여러 메서드를 작성하고,
 * @RequestMapping 등을 이용해서 URL을 분기하는 구조로 작성할 수 있기 때문에
 * 하나의 클래스에서 필요하 만큼 메서드의 분기를 이용하는 구조로 작성한다
 * 
 * BoardController 는 BoardService에 대해 의존적이므로 @AllArgsConstructor 를 이용해서 생성자를 만들고 자동으로 주입한다
 * 
 * 해당 단계에서WAS 를 실행하지 앟고 Controller를 테스트 하도록 한다(was 방식 오래걸리고 자동화가 어려움)
 */

@Controller
@Log
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService service;
	
	@GetMapping("/allList")
	public String allList(Model model) {
		
		log.info("list");
		
		model.addAttribute("list", service.getAllList());
		
		return "/board/list";
	}
	
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
	 * 등록 작업은 post 방식으로 처리하지만, 
	 * 화면에서 입력을 받아야 하므로 get 방식으로 입력 페이지를 볼 수 있도록 한다.
	 */
	@GetMapping("/register")
	public void register() {
		
	}
	
	/**
	 * [등록] 처리
	 * @param board
	 * @param rttr
	 * @return
	 */
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		// 'redirect:' 스프링mvc 가 내부적으로 response.sendRedirect() 처리를 해줌
		
		log.info("register : " + board);
		
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
	 * @param board
	 * @param cri
	 * @param rttr
	 * @return
	 */
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify : " + board);
		
		if(service.modify(board)) { // service.modify 는 수정 여부를 boolean 처리
			rttr.addFlashAttribute("result", "success");
		}
		
		// 페이지 & 검색 관련 파라미터 처리
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getK30yword());
//		
//		return "redirect:/board/list";
		
		// getListLink() 메서드를 이용하면 위 코드를 이렇게 바꿀 수 있다
		return "redirect:/board/list" + cri.getListLink();	
	}
	
	/**
	 * [삭제] 처리
	 * @param bno
	 * @param cri
	 * @param rttr
	 * @return
	 */
	@PostMapping("/remove") //반드시 post 처리
	public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		
		log.info("remove......." + bno);
		
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}
		
//		rttr.addAttribute("pageNum", cri.getPageNum());
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getKeyword());
//		
//		return "redirect:/board/list";
		
		// getListLink() 메서드를 이용하면 위 코드를 이렇게 바꿀 수 있다
		return "redirect:/board/list" + cri.getListLink();	
	}
	
	
}
