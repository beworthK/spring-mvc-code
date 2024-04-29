package org.zerock.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * SampleDTO 의 리스트를 포함하는 SampleDTOList 클래스
 */
@Data
public class SampleDTOList {

	private List<SampleDTO> list;
	
	public SampleDTOList() {
		list = new ArrayList<>();
	}
	
}
