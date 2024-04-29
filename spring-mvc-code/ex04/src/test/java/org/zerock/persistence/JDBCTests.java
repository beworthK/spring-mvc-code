package org.zerock.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class JDBCTests {
	
	/** JDBC 테스트 코드
	 * JDBC 드라이버가 정상적으로 추가되었고, 데이터베이스의 연결이 가능한지 확인하는 테스트 코드
	 */
	
	static {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConnection() {
		
		try(Connection con = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/BOOK" //url
				, "book_ex"
				, "book_ex")) {
			
			log.info(con);
		}catch (Exception e) {
			fail(e.getMessage());
		}
		
		
	}
	

}
