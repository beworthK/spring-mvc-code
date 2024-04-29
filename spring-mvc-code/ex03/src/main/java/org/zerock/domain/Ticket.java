package org.zerock.domain;

import lombok.Data;

@Data
public class Ticket {
	
	private int tno; // 번호
	private String owner; // 소유주
	private String grade; // 등급
}
