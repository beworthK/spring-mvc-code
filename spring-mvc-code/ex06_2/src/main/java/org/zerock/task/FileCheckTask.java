package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 잘못 업로드된 파일의 정리
 * - 사용자가 게시물을 등록하거나 수정하기 위해 첨부파일을 등록했지만,
 * 	 최종적으로 submit 을 하지 않은 경우
 * 	 폴더에 파일들은 업로드 되지만, 데이터베이스에는 아무런 변화가 없게 된다.
 * => upload 폴더의 실 첨부파일과 tbl_attach 테이블에 기록된 데이터를 비교하여,
 * 	  필요없는 파일들은 삭제한다.
 * 
 * 
 * cf. Stream ------------------------------------------------
 * Stream : 자바의 스트림은 뭔가 연속된 정보를 처리하는 데 사용한다.(컬렉션)
 * 스트림 구조 
 * 	1) 스트림 생성 : 컬랛녀 목록을 스트림 객체로 변환
 * 	2) 중개 연산 : 생성된 스트림 객체를 사용하여 중개 연산부분에서 처리(아무런 결과를 리턴하지 못한다)
 * 	3) 종단 연산 : 중개 연산에서 작업된 내용을 바탕으로 결과를 리턴
 * Stream() 은 순차적으로 데이터를 처리한다 (index 순)
 * 
 * Stream에서 제공하는 주요 연산자
 * - map(Function <T, R> mapper) : 데이터를 특정 데이터로 변환
 * - forEach(Consumer <? super T> action) : for 루프를 수행하는 것처럼 각각의 항목을 꺼냄
 * 
 * cf. Lambda Expression --------------------------------------
 * 람다식 : 가독성도 떨어지는 걸 보완하기 위해 사용, 인터페이스에 메소드가 하나인 것들만 적용 가능
 * 람다식 3구성
 * 	1) 매개변수 목록
 * 	2) -> (화살표 토큰)
 * 	3) 처리식 (한 줄 이상일 때는 중괄호로 묶을 수 있음)
 * => 좌측에서 넘겨지는 매개변수들의 타입이 선언되고, 가장 우측에 리턴되는 값을 표시한다
 * 
 */
@Log4j
@Component
public class FileCheckTask {

	private final String UPLOAD_FOLDER = "D:\\Projects\\reese\\spring-mvc-code\\upload";
	
	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper;

	private String getFolderYesterDay() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}
	
	/**
	 * @Scheduled
	 * cron = "0 0 2 * * *" => 매일 새벽 2시 정각마다
	 * 첫번째 - seconds(0~59)
	 * 두번째 - minutes(0~59)
	 * 세번째 - hours(0~23)
	 * 네번째 - day(1~31)
	 * 다섯번째 - month(1~12)
	 * 여섯번째 - day of week(1~7)
	 * 일곱번째 - year(optional) 
	 * * = 모든 수
	 */
	@Scheduled(cron = "0 0 2 * * *")
	public void checkFiles() throws Exception {

		log.warn("File Check Task run.................");
		log.warn(new Date());
		
		// 1) 어제 날짜로 DB에 등록된 첨부파일 목록 가져오기(오늘 날짜로 하는 경우, 현재 작업중인 파일들을 삭제할 수 있으므로)
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();

		// 비교을 위해 java.nio.Paths 의 목록(=fileListPaths)으로 변환
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get(UPLOAD_FOLDER, vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());

		// 이미지 파일의 경우 썸네일 파일도 목록에 필요하므로 별도 처리
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(
					vo -> Paths.get(UPLOAD_FOLDER, vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName())
				)
				.forEach(p -> fileListPaths.add(p)); // fileListPaths 에 추가

		log.warn("===========================================");
		fileListPaths.forEach(p -> log.warn(p));

		// 2) 어제 실제 등록된 파일 목록에서 DB에 없는 파일들을 찾아서 목록으로 준비(removeFiles)
		File targetDir = Paths.get(UPLOAD_FOLDER, getFolderYesterDay()).toFile();
		File[] removeFiles = targetDir.listFiles(
								file -> fileListPaths.contains( file.toPath() ) == false
							 );

		log.warn("-----------------------------------------");
		
		// 3) 데이터베이스에 없는 파일들 삭제
		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}
}
