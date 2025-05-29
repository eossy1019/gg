package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;


@Service //String이 관리하는 Bean으로 등록되어야하기 때문에 구현체인 ServiceImpl에 Service 어노테이션 부여해야한다.
public class MemberServiceImpl implements MemberService{
	
	//dao도 스프링이 관리할 수 있도록 bean 등록처리 해주기 
	
	@Autowired
	private MemberDao dao;
	
	
	//sqlSession 객체도 스프링이 관리할 수 있도록 의존성 주입 
	
	//root-context에서 만들어 둔 sqlSession 의존성 주입 
	@Autowired
	private SqlSessionTemplate sqlSession;
	//스프링 컨테이너가 sqlSession의 생명주기도 관리하기 때문에 직접 close할 필요 없고
	//트랜잭션 처리 또한 스프링이 직접 처리하기 때문에 생략한다.
	//단 트랜잭션 처리를 직접해야되는 경우엔 해당 메소드에 
	//@Transactional 어노테이션을 부여하고 직접 관리하면 된다.
	

	@Override
	public Member loginMember(Member m) {

		Member loginUser = dao.loginMember(sqlSession,m);
		
		return loginUser;
	}
	
	//회원가입
	@Override
	public int insertMember(Member m) {
		//한줄 처리
//		int result = dao.insertMember(sqlSession,m);
//		return result;
		return dao.insertMember(sqlSession,m); //한줄처리
	}

	//회원 수정
	@Override
	public int updateMember(Member m) {
		
		return dao.updateMember(sqlSession,m);
	}
	
	//회원 탈퇴
	@Override
	public int deleteMember(Member m) {
		
		return dao.deleteMember(sqlSession,m);
	}
	
	//아이디 중복확인
	@Override
	public int idCheck(String userId) {
		
		return dao.idCheck(sqlSession,userId);
	}
	

	
	
	
}
