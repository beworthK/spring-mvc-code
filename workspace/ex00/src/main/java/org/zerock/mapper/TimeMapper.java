package org.zerock.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * Mapper
 * - SQL 과 그에 대한 처리를 지정하는 역할 (자동처리)
 * - MyBatis-SSpring 을 이용하는 경우, Mapper를 XML과 인터페이스 + 어노테이션의 형태로 작성한다
 */
public interface TimeMapper {

	@Select("SELECT now() FROM dual")
	public String getTime();
	
	public String getTime2();
	
}
