package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService{
	
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private BoardDao dao;
	
	
	@Override
	public int listCount() {
		
		return dao.listCount(sqlSession);
	}


	@Override
	public ArrayList<Board> boardList(PageInfo pi) {
		
		return dao.boardList(sqlSession,pi);
	}

	
	@Override
	public int increaseCount(int bno) {
	
		return dao.increaseCount(sqlSession,bno);
	}

	@Override
	public Board boardDetail(int bno) {

		return dao.boardDetail(sqlSession,bno);
	}
	
	
	@Override
	public int insertBoard(Board b) {
		
		return dao.insertBoard(sqlSession,b);
	}
	
	
	@Override
	public int deleteBoard(int bno) {
	
		return dao.deleteBoard(sqlSession,bno);
	}
	
	
	@Override
	public int updateBoard(Board b) {
		
		return dao.updateBoard(sqlSession,b);
		
	}
	
	
	@Override
	public int searchCount(HashMap<String, String> map) {
	
		return dao.searchCount(sqlSession,map);
	}
	
	@Override
	public ArrayList<Board> searchList(HashMap<String, String> map, PageInfo pi) {

		return dao.searchList(sqlSession,map,pi);
	}
	
	
	@Override
	public ArrayList<Reply> replyList(int boardNo) {
		
		return dao.replyList(sqlSession,boardNo);
	}
	
	@Override
	public int insertReply(Reply r) {
		return dao.insertReply(sqlSession,r);
	}
	
	@Override
	public ArrayList<Board> topList() {
	
		return dao.topList(sqlSession);
	}
	
	
	//사진게시글 등록
	@Transactional //트랜잭션 묶음 처리하기
	@Override
	public int insertPhoto(Board b, ArrayList<Attachment> atList) {
		
		//게시글 등록 전 번호 추출 (첨부파일 등록시에도 사용)
		int boardNo = dao.selectBoardNo(sqlSession);
		
		//게시글번호 추출이 잘 되었다면 
		if(boardNo>0) {
			b.setBoardNo(boardNo); //게시글 정보에 추가
		}else {
			return boardNo; //실패처리 될수있도록 반환
		}
		
		//사진게시글 등록용 메소드 호출
		int result = dao.insertAtBoard(sqlSession,b);
		
		int result2 = 1; //첨부파일 등록 반환값 담을 변수
		
		if(result>0) {
			//반복문을 이용하여 각 첨부파일 정보 등록 작업 
			for(Attachment at : atList) {
				at.setRefBno(boardNo); //참조게시글번호 추가
				//각 첨부파일 정보 등록작업 후 처리결과 곱연산 처리
				result2 *= dao.insertAttachment(sqlSession,at);
			}
			//첨부파일 등록 작업 반환값 * 게시글 등록반환값 리턴
			return result * result2;
			
		
		}else {
			
			return result; //게시글정보 등록 실패 반환값 
		}
		
		
	}
	
	
	//사진게시글 목록조회
	@Override
	public ArrayList<Board> photoList() {
		
		return dao.photoList(sqlSession);
	}
	
	
	//사진게시글 상세조회
	@Override
	public Board photoDetail(int bno) {
		
		return dao.photoDetail(sqlSession,bno);
	}
	
	
	
}
