package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class SampleTxServiceTests {

	@Setter(onMethod_ = { @Autowired })
	private SampleTxService service;
	
	@Test
	public void testLong() {
	  
	  String str ="Starry\r\n" + 
	      "Starry night\r\n" + 
	      "Paint your palette blue and grey\r\n" + 
	      "Look out on a summer's day";
	  
	  log.info(str.getBytes().length); 
	  
	  // tbl_sample1 은 성공하지만, tbl_sample2 는 길이 제한으로 실패
	  service.addData(str);
	  
	  /*
	   SampleTxServiceImpl에 트랜잭션 어노테이션 추가 후 실행로그
	   
	   ... 생략 ...
	   INFO : jdbc.audit - 1. PreparedStatement.close() returned 
	   INFO : jdbc.audit - 1. Connection.getMetaData() returned org.mariadb.jdbc.DatabaseMetaData@37b70343
	   INFO : jdbc.audit - 1. Connection.rollback() returned
	    ㄴ 롤백 진행함!
	   INFO : jdbc.audit - 1. Connection.setAutoCommit(true) returned 
	    ㄴ commit
	   INFO : jdbc.audit - 1. Connection.clearWarnings() returned 
	   ... 생략 ...
	    
	    
	   */
	}
	
  
}


