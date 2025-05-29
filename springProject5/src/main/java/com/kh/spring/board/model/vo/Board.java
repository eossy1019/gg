package com.kh.spring.board.model.vo;

import java.sql.Date;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Board {
	private int boardNo;//	BOARD_NO	NUMBER
	private String boardTitle;//	BOARD_TITLE	VARCHAR2(100 BYTE)
	private String boardWriter;//	BOARD_WRITER	VARCHAR2(4000 BYTE)
	private String boardContent;//	BOARD_CONTENT	VARCHAR2(4000 BYTE)
	private String originName;//	ORIGIN_NAME	VARCHAR2(100 BYTE)
	private String changeName;//	CHANGE_NAME	VARCHAR2(100 BYTE)
	private String filePath; //저장경로 담을 필드
	private int count;//	COUNT	NUMBER
	private Date createDate;//	CREATE_DATE	DATE
	private String status;//	STATUS	VARCHAR2(1 BYTE)
	
	
	//첨부파일정보를 담아올 리스트 
	private ArrayList<Attachment> atList;
	

}
