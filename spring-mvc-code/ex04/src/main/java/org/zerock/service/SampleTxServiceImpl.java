package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mapper.Sample1Mapper;
import org.zerock.mapper.Sample2Mapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * 트랜잭션은 비즈니스 계층에서 진행!
 */
@Service
@Log4j
public class SampleTxServiceImpl implements SampleTxService {

  @Setter(onMethod_ = { @Autowired })
  private Sample1Mapper mapper1;
  
  @Setter(onMethod_ = { @Autowired })
  private Sample2Mapper mapper2;
  
  
  /*
   *  col1 은 varchar(500), col2 는 varchar(50) 이므로.
   *  만일 50바이트 이상 넣는 상황이라먄, 
   *  	tbl_sample1 에는 정상적으로 insert 되지만.
   *  	tbl_sample2 는 최대칼럼 길이보다 크기 떄문에 문제가 발생할 것이다.
   *  
   *  @Transactional 
   *   - 트랜잭션 처리를 통해 둘중 하나의 테이블에만 insert 되는 상황이 생기지 않도록 한다!
   *  
   *  @Transactional 어노테이션 속성들 (경우에 따라 속성들 조정해서 사용해야 한다)
   *  
   *  1) 전파(Propagation) 속성
   *  - PROPAGATION_MADATORY : 작업은 반드시 특정한 트랜잭션이 존재한 상태에서만 가능
   *  - PROPAGATION_NESTED : 기존에 트랜잭션이 있는 경우, 포함되어서 실행
   *  - PROPAGATION_NEVER : 트랜잭션 상황하에 실행되면 예외 발생
   *  - PROPAGATION_REQUIRED : 트랜잭션이 있으면 그 상황에서 실행, 없으면 새로운 트랜잭션 실행(기본설정)
   *  - PROPAGATION_REQUIRED_NEW : 대상은 자신만의 고유한 트랜잭션으로 실행
   *  - PROPAGATION_SUPPORTS : 트랜잭션을 필요로 하지 않으나, 트랜잭션 상황하에 있다면 포함되어서 실행
   *  
   *  2) 격리(Isolation) 레벨
   *  - DEFAULT : DB 설정, 기본 격리 수준
   *  - SERIALIZABLE : 가장 높은 격리, 성능 저하 우려있음!
   *  - READ_UNCOMMITED : 커밋되지 않은 데이터에 대한 읽기를 허용
   *  - READ_COMMITED : 커밋된 데이터에 대해 읽기 허용
   *  - REPEATEABLE_READ : 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장
   *  
   *  3) Read-only 속성 
   *  - true : insert, updatem delete 실행 시, 예외 발생 (기본설정은 false)
   *  
   *  4) Rollback-for-예외
   *  - 특정 예외가 발생 시 강제로 Rollback
   *  
   *  5) No-rollback-for-예외
   *  - 특정 예외의 발생 시에는 Rollback 처리되지 않음
   *  
   *  
   *  @Transactional 적용 순서
   *  - 메서드의 @Transactional 설정이 가장 우선
   *  - 클래스의 @Transactional 우선순위 < 메서드의 @Transactional 우선순위
   *  - 인터페이스의 @Transactional 설정 가장 낮은 우선순위
   *  
   *  => 인터페이스에는 가장 기준이 되는 @Transactional 과 같은 설정을 지정하고,
   *  클래스나 메서드에 필요한 어노테이션을 처리하는 것이 좋다 
   * 
   */
  @Transactional
  @Override
  public void addData(String value) {
    
    log.info("mapper1....................");
    mapper1.insertCol1(value);
    
    log.info("mapper2.....................");
    mapper2.insertCol2(value);
    
    log.info("end..........................");
    
  }

}


