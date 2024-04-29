package org.zerock.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TodoDTO {

	private String title;
	private Date dueDate;
	
	/*
	 * 하위 방식으로 하면 controller 단에서 @InitBinder 사용하지 않아도 됨
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date dueDate;
	*/
}
