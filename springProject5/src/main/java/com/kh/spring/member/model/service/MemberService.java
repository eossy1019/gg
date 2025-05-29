package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {

	//로그인 기능 (SELECT)
	Member loginMember(Member m);
	
	//회원가입 기능 (INSERT)
	int insertMember(Member m);
	
	//회원 정보 수정 기능 (UPDATE)
	int updateMember(Member m);

	//회원 탈퇴 기능 (UPDATE)
	int deleteMember(Member m);

	//아이디 중복확인 기능
	int idCheck(String userId);
	
	
}
