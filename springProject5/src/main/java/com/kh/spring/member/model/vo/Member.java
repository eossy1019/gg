package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



/*
 * Lombok (롬복)
 * -자동 코드 생성 라이브러리
 * -반복되는 getter,setter,toString 등의 메소드 작성 코드를 줄여주는 코드 라이브러리 
 * Lombok 사용시 주의사항
 * -uName,bTitle과 같이 앞글자가 외자인 필드명은 작성하지 말것
 * -필드명 작성시 적어도 소문자 두글자 이상으로 시작해야함.
 * -EL 표기법을 사용하여 내부적으로 getter 메소드를 호출할때 getuName()과 같이 잘못된 메소드를 호출할 수 있음
 *
 *  어노테이션을 이용하여 필요한 메소드들을 사용한다.
 * */

@NoArgsConstructor //기본 생성자 
@AllArgsConstructor//모든 필드를 갖는 매개변수 생성자
@Setter//setter 메소드
@Getter//getter 메소드
@ToString//toString 메소드 
@EqualsAndHashCode // equals와 hashcode 메소드
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode 를 포함한 어노테이션
public class Member {
	
	private String userId;// USER_ID VARCHAR2(30 BYTE)
	private String userPwd;// USER_PWD VARCHAR2(100 BYTE)
	private String userName;// USER_NAME VARCHAR2(15 BYTE)
	private String email;// EMAIL VARCHAR2(100 BYTE)
	private String gender;// GENDER VARCHAR2(1 BYTE)
	private String age;// AGE NUMBER
	private String phone;// PHONE VARCHAR2(13 BYTE)
	private String address;// ADDRESS VARCHAR2(100 BYTE)
	private Date enrollDate;// ENROLL_DATE DATE
	private Date modifyDate;// MODIFY_DATE DATE
	private String status;// STATUS VARCHAR2(1 BYTE)
	
}
