package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;
//Repository : 저장소, 주로 DB(저장소)와 관련된 작업을 처리하는 영역이기때문에 dao에는 Repository 어노테이션을 부여한다.
@Repository
public class MemberDao {

	//로그인 기능
	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		
		//sqlSession.selectOne() : 조회구문중 결과값이 하나만 나올때 사용할 수 있는 메소드
		//첫번째 매개변수에는 매퍼별칭.구문키값 으로 해당 구문을 지정한다.
		//두번째 매개변수에는 전달값이 있을경우 해당 전달값을 작성한다.(단, 하나의 전달값만 가능-묶어보내기)
		Member loginUser = sqlSession.selectOne("memberMapper.loginMember",m);
		
		return loginUser;
	}
	
	//회원가입 기능
	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		
//		int result = sqlSession.insert("memberMapper.insertMember",m);
//		return result;
		
		//한줄처리
		return sqlSession.insert("memberMapper.insertMember",m);
	}
	
	//정보수정 메소드 
	public int updateMember(SqlSessionTemplate sqlSession, Member m) {

		return sqlSession.update("memberMapper.updateMember",m);
	}
	
	//회원 탈퇴 메소드
	public int deleteMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.update("memberMapper.deleteMember",m);
	}

	//아이디 중복확인
	public int idCheck(SqlSessionTemplate sqlSession, String userId) {
		
		return sqlSession.selectOne("memberMapper.idCheck",userId);
	}

	
	
	
	
	
	
}
