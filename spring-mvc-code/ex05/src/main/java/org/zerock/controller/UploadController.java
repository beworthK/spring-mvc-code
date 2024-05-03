package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

/**
 * 첨부파일 처리 컨트롤러
 */
@Controller
@Log4j
public class UploadController {

	private final String UPLOAD_FOLDER = "D:\\Projects\\reese\\spring-mvc-code\\upload";
	
	@GetMapping("file/uploadForm")
	public void uploadForm() {

		log.info("upload form");
	}

	/*
	 * MultipartFile
	 *  스프링에서 제공하는 MultipartFile 타입 사용
	 *  첨부파일 여러개 선택하므로 배열타입
	 *  메서드
	 *  - getName() : 파라미터의 이름<input> 태그의 이름
	 *  - getOriginalFilename() : 업로드 되는 파일 이름
	 *  - isEmpty() : 파일이 존재하지 않는 경우 true
	 *  - getSize() : 업로드 되는 파일 크기
	 *  - getBytes() : byte[]로 파일 데이터 변환
	 *  - getInputStream() : 파일 데이터와 연결된 InputStream을 반환
	 *  - transferTo(File file) : 파일 저장
	 */
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {

		String uploadFolder = UPLOAD_FOLDER;

		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				//파일저장
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			} // end catch
		} // end for

	}

	/*
	 * Ajax 를 이용하는 파일 업로드 
	 */
	@GetMapping("file/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}

	/* 
	 * 22.1 파일업로드 처리 (Ajax)
	 * 
	 * 장점) 사용자가 게시물을 등록하거나 수정하기 전에 미리 업로드 시킨 파일을 볼 수 있다
	 * 단점) 첨부파일만 등록하고 게시글 등록하지 않았을 경우 > 의미없는 파일이 서버에 업로드 됨
	 * 		게시물 수정 시, 파일을 삭제했지만 실제 파일은 삭제되지 않는 문제 > 데이터베이스와 실제 파일 싱크가 맞지 않음
	 * 
	 * 1) 중복된 파일 이름 처리
	 * - 현재시간을 밀리세컨드까지 구분해서 파일이름 생성
	 * - UUID를 이용해서 중복 가능성 없는 문자열 생성
	 * 
	 * 2) getFolder() 
	 * 하나의 폴더에 너무 많은 파일이 있는 경우 속도의 저하와 개수의 제한 문제가 생기므로 
	 * 이를 방지하고자, '년/월/일' 단위의 폴더를 생성
	 * 
	 * 3) 이미지 파일 판단 > 썸네일 이미지 생성 및 저장
	 * - 화면에서 파일 확장자를 검사하지만, Ajax 로 사용하는 호출은 
	 *   반드시 브라우저만을 통해서 들어오는 것이 아니므로 확인할 필요가 있다.
	 * - Thumbnailator 라이브러리를 통해 썸네일을 생성한다
	 * 
	 * 4) 업로드된 파일의 데이터 반환
	 * - 서버에서 Ajax 결과로 전달해야하는 데이터는 업로드된 파일의 경로가 포함된 파일 이름
	 * - 브라우저에 전송할 데이터 목록
	 *   업로드된 파일 이름과 원본 파일 이름
	 *   파일이 저장된 경로
	 *   업로드된 파일이 이미지인지에 대한 정보
	 * - 업로드된 경로가 포함된 파일 이름을 반환하는 방식과 별도의 객체를 생성해서 처리하는 방법 중 후자로 진행
	 */
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		
		List<AttachFileDTO> list = new ArrayList<>();
		
		String uploadFolder = UPLOAD_FOLDER;
		String uploadFolderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, uploadFolderPath);

		if (uploadPath.exists() == false) {
			uploadPath.mkdirs(); // 폴더 생성
		}
		
		// 파일저장
		for (MultipartFile multipartFile : uploadFile) {
		
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// IE has file path - IE의 경우, 전체 파일 경로가 전송되므로, 마지막 "\\" 기준으로 잘라낸 문자열이 실제 파일 이름
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);
			
			attachDTO.setFileName(uploadFileName); // 원본파일명
			
			// 중복 방지를 위한 UUID 적용
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName; //_ 로 원래파일명과 구분해준다
			
		
			try {
				
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile); // 파일저장
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				// 이미지파일인 경우 썸네일 생성
				if(checkImageType(saveFile)) {
					
					attachDTO.setImage(true); //이미지 생성여부
					
					//File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);
					//FileOutputStream thumbnail = new FileOutputStream( thumbnailFile );
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName)); // s_ 로 시작하는 규칙 지정
					
					// Thumbnailator 는 InputStream과 java.io.File 객체를 이용해서 파일을 생성한다.
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100); //가로 100, 세로 100 

					thumbnail.close();
				}
				
				list.add(attachDTO); //list 에 담기 
				
			} catch (Exception e) {
				log.error(e.getMessage());
			} // end catch
		
		} // end for
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/* 
	 * 23.2.3 썸네일 이미지 보여주기
	 * - 특정한 파일 이름을 받아서 이미지 데이터를 전송하는 코드
	 * - 파일의 경로가 포함된 fileName 문자열을 파라미터로 받아서 byte[]로 전송한다
	 * 	 byte[] 로 이미지 파일의 데이터를 전송할 때, 브라우저에 보내주는 MIME 타입이 파일의 종류에 따라 달라지므로
	 *   probeContentType() 를 이용해서 적절한 마임(MIME) 타입 데이터를 http 의 헤더 메시지에 포함할 수 있도록 처리한다 
	 */
	@GetMapping("/display")	
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName);

		File file = new File(UPLOAD_FOLDER + "\\" + fileName);

		log.info("file: " + file);

		ResponseEntity<byte[]> result = null;

		try {
			
			HttpHeaders header = new HttpHeaders();
			
			header.add( "Content-Type", Files.probeContentType(file.toPath()) ); // 마임(MIME) 타입 가져오기
			
			// FileCopyUtils - 파일 및 스트림 복사를 위한 유틸리티 메소드 집합체. 사용 후 영향을 받는 모든 스트림을 닫아준다
			result = new ResponseEntity<>( FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/*
	 * 24.1 첨부파일 다운로드
	 * 
	 * - 서버에서 MIME 타입을 다운로드 타입으로 지정하고, 
	 *   적절한 헤더 메시지를 통해서 다운로드 이름을 지정하게 처리한다.
	 *   다운로드는 MIME 타입이 고정된다 (APPLICATION_OCTET_STREAM_VALUE = application/octet-stream)
	 * 
	 *  - org.springframework.core.io.Resource 타입을 이용해서 간단하게 처리
	 */
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {

		Resource resource = new FileSystemResource(UPLOAD_FOLDER + "\\" + fileName);

		if (resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String resourceName = resource.getFilename();

		// 파일이름에서 UUID 제거
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

		// HttpHeaders 객체를 이용해서 다운로드 시 파일 이름 처리
		HttpHeaders headers = new HttpHeaders();
		try {

			String downloadName = null;

			// HttpServletRequest 에 포함된 헤더 정보 중 'User-Agent' 을 이용해서 디바이스 정보 구분
			if ( userAgent.contains("Trident")) {
				log.info("IE browser"); // Trident = IE 브라우저의 엔진 이름
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName =  URLEncoder.encode(resourceOriginalName,"UTF-8");
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1"); // 한글 깨짐 방지
			}
			
			log.info("downloadName: " + downloadName);

			// Content-Disposition 을 이용해서 다운로드 되는 이름 지정
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	/*
	 * 24.3 첨부파일 삭제
	 * 
	 * - 이미지 파일은 썸네일도 함께 삭제
	 * - 비정상적으로 브라우저가 종료 된 경우 업로드된 파일의 처리
	 *   ㄴ 최종 결과와 서버에 업로드된 파일의 목록을 비교해서 처리 (spring-batch 나 Quartz 라이브러리 이용)
	 */
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {

		log.info("deleteFile: " + fileName);

		File file;

		try {
			file = new File( UPLOAD_FOLDER + "\\" + URLDecoder.decode(fileName, "UTF-8") );

			file.delete();

			if (type.equals("image")) {

				String largeFileName = file.getAbsolutePath().replace("s_", ""); //썸네일도 함께 삭제
				log.info("largeFileName: " + largeFileName);

				file = new File(largeFileName);
				file.delete();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("deleted", HttpStatus.OK);
		
	}
	
	
	// 금일 날짜를 기준으로 년/월/일 폴더 생성
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(); // 오늘 날짜
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}

	// 이미지 파일여부 판단 (썸네일 생성여부)
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
